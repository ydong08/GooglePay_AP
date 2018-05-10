package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Type;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.common.StatusWords;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2Encryptor;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier.AuthenticationState;
import com.google.android.libraries.commerce.hce.crypto.ValuablesCryptoException;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefMessages;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Compressor;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Bytes;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class GetDataVersion1 implements GetData {
    private final AuthenticationState authenticationState;
    private final boolean containsPayload;
    private final byte[] getDataPayloadBytes;
    private int getDataPayloadCursor;
    private final FormattingLogger logger = FormattingLoggers.newContextLogger();
    private final boolean readyForPayment;
    private byte sequenceNumber;
    private final Set<ServiceObject> serviceObjects;
    private final boolean unlockRequired;
    private final boolean useEncryption;

    public GetDataVersion1(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, byte[] bArr, byte b, short s, AuthenticationState authenticationState, boolean z, boolean z2, SmartTap2Encryptor smartTap2Encryptor, Compressor compressor, Set<ServiceObject> set2, boolean z3, boolean z4) throws ValuablesCryptoException {
        boolean z5;
        this.sequenceNumber = b;
        this.serviceObjects = set2;
        this.getDataPayloadCursor = 0;
        this.getDataPayloadBytes = generateGetDataPayloadBytes(multimap, set, bArr, s, z, z2, smartTap2Encryptor, compressor);
        for (ServiceObject type : set2) {
            if (type.type() != Type.WALLET_CUSTOMER) {
                z5 = true;
                break;
            }
        }
        z5 = false;
        this.containsPayload = z5;
        this.authenticationState = authenticationState;
        this.useEncryption = z;
        this.unlockRequired = z3;
        this.readyForPayment = z4;
    }

    private byte[] generateGetDataPayloadBytes(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, byte[] bArr, short s, boolean z, boolean z2, SmartTap2Encryptor smartTap2Encryptor, Compressor compressor) throws ValuablesCryptoException {
        byte[] bArr2;
        boolean z3;
        NdefRecord composeNdef;
        NdefRecord compose;
        Collection toNdef = IssuerServiceObjects.toNdef(multimap, set, s, this.serviceObjects);
        byte[] bArr3 = new byte[0];
        if (!toNdef.isEmpty()) {
            bArr3 = NdefMessages.compose((Multimap) multimap, (Set) set, SmartTap2Values.RECORD_BUNDLE_NDEF_TYPE, s, toNdef).toByteArray();
        }
        if (z2) {
            byte[] compress = compressor.compress(bArr3);
            this.logger.d("Uncompressed size %d; compressed size %d", Integer.valueOf(bArr3.length), Integer.valueOf(compress.length));
            if (compress.length < bArr3.length) {
                this.logger.d("Using compression", new Object[0]);
                bArr2 = compress;
                z3 = true;
                if (z) {
                    bArr2 = smartTap2Encryptor.encryptMessage(bArr2);
                }
                new byte[1][0] = (byte) (getCompressedBit(z3) | getEncryptedBit(z));
                composeNdef = new Session(bArr, this.sequenceNumber).composeNdef(s);
                compose = NdefRecords.compose(SmartTap2Values.RECORD_BUNDLE_NDEF_TYPE, Bytes.concat(bArr3, bArr2), s);
                compose = NdefRecords.compose(SmartTap2Values.SERVICE_RESPONSE_NDEF_TYPE, NdefMessages.compose((Multimap) multimap, (Set) set, SmartTap2Values.SERVICE_RESPONSE_NDEF_TYPE, s, composeNdef, compose).toByteArray(), s);
                return NdefMessages.compose((Multimap) multimap, (Set) set, SmartTap2Values.GET_DATA_NDEF_FLAG, s, compose).toByteArray();
            }
            this.logger.d("Not using compression", new Object[0]);
        }
        z3 = false;
        bArr2 = bArr3;
        if (z) {
            bArr2 = smartTap2Encryptor.encryptMessage(bArr2);
        }
        new byte[1][0] = (byte) (getCompressedBit(z3) | getEncryptedBit(z));
        composeNdef = new Session(bArr, this.sequenceNumber).composeNdef(s);
        compose = NdefRecords.compose(SmartTap2Values.RECORD_BUNDLE_NDEF_TYPE, Bytes.concat(bArr3, bArr2), s);
        compose = NdefRecords.compose(SmartTap2Values.SERVICE_RESPONSE_NDEF_TYPE, NdefMessages.compose((Multimap) multimap, (Set) set, SmartTap2Values.SERVICE_RESPONSE_NDEF_TYPE, s, composeNdef, compose).toByteArray(), s);
        return NdefMessages.compose((Multimap) multimap, (Set) set, SmartTap2Values.GET_DATA_NDEF_FLAG, s, compose).toByteArray();
    }

    private byte getEncryptedBit(boolean z) {
        return z ? (byte) 1 : (byte) 0;
    }

    private byte getCompressedBit(boolean z) {
        return z ? (byte) 2 : (byte) 0;
    }

    public SmartTapResponse getDataResponse(byte b) throws SmartTapV2Exception, ValuablesCryptoException {
        SmartTapV2Exception.check(this.sequenceNumber == b, Status.INVALID_SEQUENCE_NUMBER, Code.EXECUTION_FAILURE, "Expected Get Data Response to be in the same APDU as Get Data. Expected: %s. Actual: %s", Byte.valueOf(this.sequenceNumber), Byte.valueOf(b));
        this.sequenceNumber = (byte) (this.sequenceNumber + 2);
        return getSmartTapResponse(80);
    }

    public SmartTapResponse getMoreDataResponse(byte b) throws SmartTapV2Exception {
        SmartTapV2Exception.check(hasMoreData(), Status.DATA_NOT_YET_AVAILABLE, Code.MORE_DATA_NOT_AVAILABLE, "Get More Data command received without more data available", new Object[0]);
        return getSmartTapResponse(-64);
    }

    public boolean hasMoreData() {
        return this.getDataPayloadCursor < this.getDataPayloadBytes.length;
    }

    private SmartTapResponse getSmartTapResponse(int i) {
        byte[] copyOfRange;
        StatusWord statusWord;
        if (this.getDataPayloadBytes.length - this.getDataPayloadCursor <= 253) {
            copyOfRange = Arrays.copyOfRange(this.getDataPayloadBytes, this.getDataPayloadCursor, this.getDataPayloadBytes.length);
            this.getDataPayloadCursor = this.getDataPayloadBytes.length;
            if (this.readyForPayment) {
                statusWord = StatusWords.get(this.containsPayload ? Code.SUCCESS : Code.SUCCESS_NO_PAYLOAD, (short) 1);
            } else {
                Code code;
                if (this.containsPayload) {
                    code = Code.SUCCESS_WITH_PAYLOAD_PAYMENT_NOT_READY;
                } else {
                    code = Code.SUCCESS_NO_PAYLOAD_PAYMENT_NOT_READY;
                }
                statusWord = StatusWords.get(code, (short) 1);
            }
        } else {
            copyOfRange = Arrays.copyOfRange(this.getDataPayloadBytes, this.getDataPayloadCursor, this.getDataPayloadCursor + 253);
            this.getDataPayloadCursor += 253;
            statusWord = StatusWords.get(Code.SUCCESS_MORE_PAYLOAD, (short) 1);
        }
        return SmartTapResponse.create(i, ResponseApdu.fromDataAndStatusWord(copyOfRange, statusWord), this.serviceObjects, this.authenticationState, this.useEncryption, this.unlockRequired);
    }
}
