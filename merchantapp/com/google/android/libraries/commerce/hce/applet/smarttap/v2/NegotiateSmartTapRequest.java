package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionRequest.DecodedRequest;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.ndef.Primitive;
import com.google.android.libraries.commerce.hce.util.ByteArrays;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.Arrays;

public class NegotiateSmartTapRequest extends SessionRequest {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final byte[] encodedMerchantId;
    private final byte[] ephemeralPublicKey;
    private final boolean liveAuthenticate;
    private final int longTermKeyVersion;
    private final long merchantId;
    private final byte[] signature;
    private final byte[] terminalNonce;

    static class Builder extends com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionRequest.Builder {
        private byte[] encodedMerchantId;
        private byte[] ephemeralPublicKey;
        private boolean liveAuthenticate;
        private Integer longTermKeyVersion;
        private Long merchantId;
        private byte[] signature;
        private byte[] terminalNonce;

        private Builder() {
            this.liveAuthenticate = true;
        }

        public Builder setTerminalNonce(byte[] bArr) throws SmartTapV2Exception {
            boolean z = true;
            SmartTapV2Exception.check(bArr != null, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "Cannot set null terminal nonce.", new Object[0]);
            if (bArr.length != 32) {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "Terminal nonce is wrong length.", new Object[0]);
            this.terminalNonce = bArr;
            return this;
        }

        public Builder setLiveAuthenticate(boolean z) {
            this.liveAuthenticate = z;
            return this;
        }

        public Builder setEphemeralPublicKey(byte[] bArr) throws SmartTapV2Exception {
            boolean z = true;
            SmartTapV2Exception.check(bArr != null, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "Cannot set null ephemeral public key.", new Object[0]);
            if (bArr.length != 33) {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "Ephemeral public key is wrong length.", new Object[0]);
            this.ephemeralPublicKey = bArr;
            return this;
        }

        public Builder setLongTermKeyVersion(byte[] bArr) throws SmartTapV2Exception {
            boolean z = true;
            SmartTapV2Exception.check(bArr != null, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "Cannot set null long term key version.", new Object[0]);
            if (bArr.length > 4) {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "Long term key version is too long.", new Object[0]);
            this.longTermKeyVersion = Integer.valueOf(Ints.fromByteArray(ByteArrays.updatePayloadLength(bArr, 4)));
            return this;
        }

        public Builder setSignature(Primitive primitive) throws SmartTapV2Exception {
            boolean z;
            if (primitive != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "Cannot set null signature.", new Object[0]);
            this.signature = primitive.getPayload();
            return this;
        }

        public Builder setMerchantId(Primitive primitive) throws SmartTapV2Exception {
            boolean z = true;
            SmartTapV2Exception.check(primitive != null, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "Cannot set null merchant ID.", new Object[0]);
            this.merchantId = primitive.toLong();
            this.encodedMerchantId = primitive.getPayload();
            if (this.merchantId == null) {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "Failed to parse merchant ID.", new Object[0]);
            return this;
        }

        public NegotiateSmartTapRequest build() throws SmartTapV2Exception {
            boolean z;
            boolean z2 = true;
            SmartTapV2Exception.check(this.version != null, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No NDEF version was set.", new Object[0]);
            if (this.sessionId != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No session ID was set.", new Object[0]);
            if (this.sequenceNumber != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No sequence number was set.", new Object[0]);
            if (this.terminalNonce != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "No terminal nonce was set.", new Object[0]);
            if (this.ephemeralPublicKey != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "No ephemeral public key was set.", new Object[0]);
            if (this.longTermKeyVersion != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "No long term key version was set.", new Object[0]);
            if (this.signature != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "No signature was set.", new Object[0]);
            if (this.merchantId == null) {
                z2 = false;
            }
            SmartTapV2Exception.check(z2, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "No merchant ID was set.", new Object[0]);
            return new NegotiateSmartTapRequest(this.version.shortValue(), this.sessionId, this.sequenceNumber.byteValue(), this.terminalNonce, this.liveAuthenticate, this.ephemeralPublicKey, this.longTermKeyVersion.intValue(), this.signature, this.merchantId.longValue(), this.encodedMerchantId);
        }
    }

    private NegotiateSmartTapRequest(short s, byte[] bArr, byte b, byte[] bArr2, boolean z, byte[] bArr3, int i, byte[] bArr4, long j, byte[] bArr5) {
        super(s, bArr, b);
        this.terminalNonce = bArr2;
        this.liveAuthenticate = z;
        this.ephemeralPublicKey = bArr3;
        this.longTermKeyVersion = i;
        this.signature = bArr4;
        this.merchantId = j;
        this.encodedMerchantId = bArr5;
    }

    public byte[] getTerminalNonce() {
        return this.terminalNonce;
    }

    public boolean getShouldLiveAuthenticate() {
        return this.liveAuthenticate;
    }

    public byte[] getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }

    public int getLongTermKeyVersion() {
        return this.longTermKeyVersion;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public long getMerchantId() {
        return this.merchantId;
    }

    public byte[] getEncodedMerchantId() {
        return this.encodedMerchantId;
    }

    public static NegotiateSmartTapRequest parse(byte[] bArr) throws SmartTapV2Exception {
        Builder builder = new Builder();
        DecodedRequest decode = SessionRequest.decode(bArr, SmartTap2Values.NEGOTIATE_NDEF_TYPE);
        short version = decode.getVersion();
        builder.setVersion(version);
        for (NdefRecord ndefRecord : decode.getNdefRecords()) {
            byte[] type = NdefRecords.getType(ndefRecord, version);
            if (Arrays.equals(type, SmartTap2Values.SESSION_NDEF_TYPE)) {
                builder.setSession(ndefRecord, version);
            } else if (Arrays.equals(type, SmartTap2Values.CRYPTO_PARAMS_NDEF_TYPE)) {
                setCryptoParams(builder, ndefRecord, version);
            } else {
                LOG.w("Unrecognized nested NDEF type %s", SmartTap2Values.getNdefType(type));
            }
        }
        return builder.build();
    }

    private static void setCryptoParams(Builder builder, NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
        Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.CRYPTO_PARAMS_NDEF_TYPE));
        int length = ndefRecord.getPayload().length;
        int cryptoParamsLengthForVersion = cryptoParamsLengthForVersion(s);
        if (length < cryptoParamsLengthForVersion) {
            throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "Negotiate SmartTap cryptography parameters is less than the min length of " + cryptoParamsLengthForVersion);
        }
        builder.setTerminalNonce(NdefRecords.getBytes(ndefRecord, 0, 32));
        if (s == (short) 0) {
            builder.setLiveAuthenticate(!Arrays.equals(NdefRecords.getBytes(ndefRecord, 32, 32), new byte[32]));
            builder.setEphemeralPublicKey(NdefRecords.getBytes(ndefRecord, 64, 33));
            builder.setLongTermKeyVersion(NdefRecords.getBytes(ndefRecord, 97, 4));
        } else if (s >= (short) 1) {
            boolean z;
            if ((NdefRecords.getByte(ndefRecord, 32) & 1) != 0) {
                z = true;
            } else {
                z = false;
            }
            builder.setLiveAuthenticate(z);
            builder.setEphemeralPublicKey(NdefRecords.getBytes(ndefRecord, 33, 33));
            builder.setLongTermKeyVersion(NdefRecords.getBytes(ndefRecord, 66, 4));
        }
        if (length == cryptoParamsLengthForVersion) {
            throw new SmartTapV2Exception(Status.MERCHANT_DATA_MISSING, Code.INVALID_CRYPTO_INPUT, "Negotiate SmartTap cryptography parameters does not contain a merchant ID record.");
        }
        for (NdefRecord ndefRecord2 : SmartTapV2Exception.tryParseNdefMessage(NdefRecords.getAllBytes(ndefRecord, cryptoParamsLengthForVersion), "nested crypto record").getRecords()) {
            byte[] type = NdefRecords.getType(ndefRecord2, s);
            if (Arrays.equals(type, SmartTap2Values.SIGNATURE_NDEF_TYPE)) {
                builder.setSignature(new Primitive(ndefRecord2));
            } else if (Arrays.equals(type, SmartTap2Values.MERCHANT_ID_V0_NDEF_TYPE) || Arrays.equals(type, SmartTap2Values.COLLECTOR_ID_NDEF_TYPE)) {
                builder.setMerchantId(new Primitive(ndefRecord2));
            } else {
                LOG.w("Unrecognized nested NDEF type %s inside of negotiate cryptography parameters request.", SmartTap2Values.getNdefType(type));
            }
        }
    }

    private static int cryptoParamsLengthForVersion(short s) {
        if (s == (short) 0) {
            return 101;
        }
        return 70;
    }
}
