package com.google.commerce.tapandpay.merchantapp.paypass;

import android.annotation.TargetApi;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvUtil;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.iso7816.CommandApduException;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.commerce.hce.util.MoreArrays;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Shorts;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@TargetApi(19)
public class PayPassMagStripe {
    private static final ResponseApdu CONDITIONS_NOT_SATISFIED = ResponseApdu.fromStatusWord(Iso7816StatusWord.CONDITIONS_NOT_SATISFIED);
    private static final ResponseApdu FILE_NOT_FOUND = ResponseApdu.fromStatusWord(Iso7816StatusWord.FILE_NOT_FOUND);
    private static final ResponseApdu INCORRECT_P1P2 = ResponseApdu.fromStatusWord(Iso7816StatusWord.INCORRECT_P1P2);
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private static final StatusWord NO_ERROR = Iso7816StatusWord.NO_ERROR;
    private static final ResponseApdu RECORD_NOT_FOUND = ResponseApdu.fromStatusWord(Iso7816StatusWord.RECORD_NOT_FOUND);
    private static final ResponseApdu WRONG_LENGTH = ResponseApdu.fromStatusWord(Iso7816StatusWord.WRONG_LENGTH);
    private CapturedAppletData mCapturedAppletData;
    private PayPassCredential mCredential;
    private PayPassState payPassState = PayPassState.IDLE;
    private final byte[] posCardholderInteractionInformation = new byte[3];
    private boolean transactionSuccessful;

    public void initialize(PayPassCredential payPassCredential) {
        this.mCredential = payPassCredential;
        this.mCapturedAppletData = new CapturedAppletData();
        Preconditions.checkState(Arrays.equals((byte[]) Preconditions.checkNotNull(payPassCredential.getUdol()), PayPassConstants.EXPECTED_UDOL));
        this.transactionSuccessful = false;
    }

    private ResponseApdu success(int i, ResponseApdu responseApdu) {
        switch (this.payPassState) {
            case IDLE:
                this.payPassState = PayPassState.SELECTED;
                break;
            case SELECTED:
                this.payPassState = i == 2 ? PayPassState.INITIATED : PayPassState.SELECTED;
                break;
            case INITIATED:
                PayPassState payPassState;
                if (i == 3) {
                    payPassState = PayPassState.INITIATED;
                } else {
                    payPassState = PayPassState.SELECTED;
                }
                this.payPassState = payPassState;
                break;
        }
        return responseApdu;
    }

    private ResponseApdu error(ResponseApdu responseApdu) {
        PayPassState payPassState;
        if (this.payPassState == PayPassState.IDLE) {
            payPassState = PayPassState.IDLE;
        } else {
            payPassState = PayPassState.SELECTED;
        }
        this.payPassState = payPassState;
        return responseApdu;
    }

    public ResponseApdu processCommand(byte[] bArr) {
        Preconditions.checkNotNull(this.mCredential);
        try {
            PayPassCommandApduIns fromByteArray = PayPassCommandApduIns.fromByteArray(bArr);
            if (!this.payPassState.acceptsCommand(fromByteArray)) {
                return error(CONDITIONS_NOT_SATISFIED);
            }
            int intValue = fromByteArray.intValue();
            ResponseApdu doProcessCommand = doProcessCommand(intValue, bArr);
            if (NO_ERROR.equals(doProcessCommand.getStatusWord())) {
                return success(intValue, doProcessCommand);
            }
            return error(doProcessCommand);
        } catch (CommandApduException e) {
            return error(e.getErrorResponse());
        }
    }

    private ResponseApdu doProcessCommand(int i, byte[] bArr) {
        switch (i) {
            case 1:
                return processSelect(bArr);
            case 2:
                return processGpo(bArr);
            case 3:
                return processReadRecord(bArr);
            case 4:
                return processCcc(bArr);
            default:
                throw new IllegalArgumentException(String.format("Unexpected PayPassCommandApduIns: value=%s command=%s", new Object[]{Integer.valueOf(i), Hex.encode(bArr)}));
        }
    }

    private ResponseApdu processSelect(byte[] bArr) {
        if (bArr[2] != (byte) 4 || bArr[3] != (byte) 0) {
            return INCORRECT_P1P2;
        }
        int lc = getLc(bArr);
        byte[] array = Aid.MASTERCARD_AID_PREFIX_CREDIT_OR_DEBIT.array();
        if (array.length != lc || !MoreArrays.equals(array, 0, bArr, 5, lc)) {
            return FILE_NOT_FOUND;
        }
        BasicTlv[] basicTlvArr = new BasicTlv[2];
        basicTlvArr[0] = BasicTlvUtil.tlv(132, array);
        basicTlvArr[1] = BasicTlvUtil.tlv(165, BasicTlvUtil.tlv(80, "MasterCard".getBytes(StandardCharsets.US_ASCII)), BasicTlvUtil.tlv(135, this.mCredential.getApplicationPriorityIndicator()), BasicTlvUtil.tlv(40721, this.mCredential.getIssuerCodeTableIndex()));
        return ResponseApdu.fromDataAndStatusWord(BasicTlvUtil.tlvToByteArray(BasicTlvUtil.tlv(111, basicTlvArr)), NO_ERROR);
    }

    private ResponseApdu processGpo(byte[] bArr) {
        if (bArr[2] != (byte) 0 || bArr[3] != (byte) 0) {
            return INCORRECT_P1P2;
        }
        if (getLc(bArr) != 2) {
            return WRONG_LENGTH;
        }
        if (bArr[5] != (byte) -125 || bArr[6] != (byte) 0) {
            return CONDITIONS_NOT_SATISFIED;
        }
        return ResponseApdu.fromDataAndStatusWord(BasicTlvUtil.tlvToByteArray(BasicTlvUtil.tlv(119, BasicTlvUtil.tlv(130, (byte[]) Preconditions.checkNotNull(this.mCredential.getAip())), BasicTlvUtil.tlv(148, (byte[]) Preconditions.checkNotNull(this.mCredential.getAfl())))), NO_ERROR);
    }

    private ResponseApdu processReadRecord(byte[] bArr) {
        int i = bArr[2] & 255;
        int i2 = bArr[3] & 255;
        if (i == 0 || (i2 & 7) != 4) {
            return INCORRECT_P1P2;
        }
        if ((i2 >> 3) != 1) {
            return FILE_NOT_FOUND;
        }
        if (i != 1) {
            return RECORD_NOT_FOUND;
        }
        return ResponseApdu.fromDataAndStatusWord(BasicTlvUtil.tlvToByteArray(BasicTlvUtil.tlv(112, BasicTlvUtil.tlv(40812, Shorts.toByteArray(this.mCredential.getAvn())), BasicTlvUtil.tlv(40802, (byte[]) Preconditions.checkNotNull(this.mCredential.getPcvc3Track1())), BasicTlvUtil.tlv(40803, (byte[]) Preconditions.checkNotNull(this.mCredential.getPunatcTrack1())), BasicTlvUtil.tlv(86, this.mCredential.getTrack1Data()), BasicTlvUtil.tlv(40804, this.mCredential.getNatcTrack1()), BasicTlvUtil.tlv(40805, (byte[]) Preconditions.checkNotNull(this.mCredential.getPcvc3Track2())), BasicTlvUtil.tlv(40806, (byte[]) Preconditions.checkNotNull(this.mCredential.getPunatcTrack2())), BasicTlvUtil.tlv(40811, this.mCredential.getTrack2Data()), BasicTlvUtil.tlv(40807, this.mCredential.getNatcTrack2()), BasicTlvUtil.tlv(40809, PayPassConstants.EXPECTED_UDOL))), NO_ERROR);
    }

    private ResponseApdu processCcc(byte[] bArr) {
        int i = bArr[3] & 255;
        if ((bArr[2] & 255) != 142 || i != 128) {
            return INCORRECT_P1P2;
        }
        if (bArr[4] != (byte) 15) {
            return WRONG_LENGTH;
        }
        Arrays.fill(this.posCardholderInteractionInformation, (byte) 0);
        return accept(bArr);
    }

    private ResponseApdu accept(byte[] bArr) {
        byte[] bArr2;
        byte[] bArr3;
        ResponseApdu fromDataAndStatusWord;
        LOG.d("readerAtc: %s", Integer.valueOf(this.mCredential.getReaderAtc()));
        ByteBuffer wrap = ByteBuffer.wrap(bArr, 5, 15);
        byte[] copyOfRange = Arrays.copyOfRange(bArr, 5, 9);
        LOG.d("un in hex: %s", Hex.encode(copyOfRange));
        byte b = wrap.get(wrap.position() + 4);
        PayPassCrypto payPassCrypto = (PayPassCrypto) Preconditions.checkNotNull(this.mCredential.getPayPassCrypto());
        int secretAtc = this.mCredential.getSecretAtc();
        if (b > (byte) 0) {
            bArr2 = this.posCardholderInteractionInformation;
            bArr2[1] = (byte) (bArr2[1] | 16);
            try {
                bArr2 = payPassCrypto.getPinCvc3Track1(copyOfRange, secretAtc);
                bArr3 = bArr2;
                bArr2 = r0;
                fromDataAndStatusWord = ResponseApdu.fromDataAndStatusWord(BasicTlvUtil.tlvToByteArray(BasicTlvUtil.tlv(119, BasicTlvUtil.tlv(40801, payPassCrypto.getPinCvc3Track2(copyOfRange, secretAtc)), BasicTlvUtil.tlv(40800, bArr2), BasicTlvUtil.tlv(40758, Shorts.toByteArray((short) this.mCredential.getReaderAtc())), BasicTlvUtil.tlv(57163, this.posCardholderInteractionInformation))), NO_ERROR);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        try {
            bArr3 = payPassCrypto.getCvc3Track1(copyOfRange, secretAtc);
            fromDataAndStatusWord = ResponseApdu.fromDataAndStatusWord(BasicTlvUtil.tlvToByteArray(BasicTlvUtil.tlv(119, BasicTlvUtil.tlv(40801, payPassCrypto.getCvc3Track2(copyOfRange, secretAtc)), BasicTlvUtil.tlv(40800, bArr3), BasicTlvUtil.tlv(40758, Shorts.toByteArray((short) this.mCredential.getReaderAtc())))), NO_ERROR);
        } catch (Throwable e2) {
            throw new RuntimeException(e2);
        }
        LOG.d("cvc3Track1: %s", Hex.encode(bArr3));
        LOG.d("cvc3Track2: %s", Hex.encode(bArr2));
        this.transactionSuccessful = true;
        return fromDataAndStatusWord;
    }

    private static int getLc(byte[] bArr) {
        return bArr[4] & 255;
    }
}
