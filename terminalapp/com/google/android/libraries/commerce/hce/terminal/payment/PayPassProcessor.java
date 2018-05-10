package com.google.android.libraries.commerce.hce.terminal.payment;

import android.content.Context;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvException;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.NfcMessageStatusException;
import com.google.android.libraries.commerce.hce.terminal.nfc.TlvParser;
import com.google.android.libraries.commerce.hce.terminal.nfc.TlvParser.ParsedTlvNfc;
import com.google.android.libraries.commerce.hce.terminal.nfc.Transceiver;
import com.google.common.primitives.Bytes;
import java.io.IOException;

class PayPassProcessor {
    private static final byte[] COMPUTE_CRYPTOGRAPHIC_CHECKSUM_PREFIX = new byte[]{Byte.MIN_VALUE, (byte) 42, (byte) -114, Byte.MIN_VALUE};
    private static final byte[] READ_RECORD = new byte[]{(byte) 0, (byte) -78, (byte) 1, (byte) 12, (byte) 0};
    private static final byte[] SELECT_PAY_PASS_PREFIX = new byte[]{(byte) 0, (byte) -92, (byte) 4, (byte) 0};
    private final Context context;
    private final TlvParser tlvParser;
    private final Transceiver transceiver;

    PayPassProcessor(Context context, Transceiver transceiver) {
        this.context = context;
        this.transceiver = transceiver;
        this.tlvParser = new TlvParser(context);
    }

    void handlePayPassPayment() throws BasicTlvException, NfcMessageStatusException, IOException {
        NfcMessage transceiveSelect = this.transceiver.transceiveSelect(getSelectPayPassCommand(), this.context.getString(R.string.select_paypass), this.tlvParser);
        transceiveSelect.verifyStatus(this.context, Iso7816StatusWord.NO_ERROR);
        this.transceiver.transceive(ProcessingOptions.getGetProcessingOptionsCommand(((ParsedTlvNfc) transceiveSelect.getParsedNfc()).getTlvs())).verifyStatus(this.context, Iso7816StatusWord.NO_ERROR);
        this.transceiver.transceive(getReadRecordCommand()).verifyStatus(this.context, Iso7816StatusWord.NO_ERROR);
        this.transceiver.transceive(getComputeCryptographicChecksumCommand());
    }

    private static byte[] getSelectPayPassCommand() {
        r0 = new byte[4][];
        r0[1] = new byte[]{(byte) Aid.MASTERCARD_AID_PREFIX_CREDIT_OR_DEBIT.getLength()};
        r0[2] = Aid.MASTERCARD_AID_PREFIX_CREDIT_OR_DEBIT.array();
        r0[3] = new byte[]{(byte) 0};
        return Bytes.concat(r0);
    }

    private static byte[] getReadRecordCommand() {
        return READ_RECORD;
    }

    private static byte[] getComputeCryptographicChecksumCommand() {
        r1 = new byte[4][];
        r1[1] = new byte[]{(byte) new byte[]{(byte) 0, (byte) 0, (byte) 4, (byte) 66, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 8, (byte) 64, (byte) 8, (byte) 64}.length};
        r1[2] = r0;
        r1[3] = new byte[]{(byte) 0};
        return Bytes.concat(r1);
    }
}
