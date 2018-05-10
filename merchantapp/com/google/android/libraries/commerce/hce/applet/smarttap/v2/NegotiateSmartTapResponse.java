package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.common.StatusWords;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.util.Arrays;

public class NegotiateSmartTapResponse extends SessionResponse {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final byte[] handsetEphemeralPublicKey;

    static class Builder extends com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionResponse.Builder {
        private byte[] handsetEphemeralPublicKey;

        private Builder() {
        }

        public Builder setHandsetEphemeralPublicKey(byte[] bArr) {
            this.handsetEphemeralPublicKey = bArr;
            return this;
        }

        public NegotiateSmartTapResponse build() throws SmartTapV2Exception {
            boolean z;
            boolean z2 = true;
            SmartTapV2Exception.check(this.sessionId != null, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No session ID was set.", new Object[0]);
            if (this.sequenceNumber != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No sequence number was set.", new Object[0]);
            if (this.statusWord == null) {
                z2 = false;
            }
            SmartTapV2Exception.check(z2, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No status word was set.", new Object[0]);
            return new NegotiateSmartTapResponse(this.sessionId, this.sequenceNumber.byteValue(), this.statusWord, this.handsetEphemeralPublicKey);
        }
    }

    private NegotiateSmartTapResponse(byte[] bArr, byte b, StatusWord statusWord, byte[] bArr2) {
        super(bArr, b, statusWord);
        this.handsetEphemeralPublicKey = bArr2;
    }

    public byte[] getHandsetEphemeralPublicKey() {
        return this.handsetEphemeralPublicKey;
    }

    public static NegotiateSmartTapResponse parse(byte[] bArr, short s) throws SmartTapV2Exception {
        Builder builder = new Builder();
        for (NdefRecord ndefRecord : SessionResponse.decode(bArr, SmartTap2Values.NEGOTIATE_RESPONSE_NDEF_TYPE, s)) {
            byte[] type = NdefRecords.getType(ndefRecord, s);
            if (Arrays.equals(type, SmartTap2Values.SESSION_NDEF_TYPE)) {
                builder.setSession(ndefRecord, s);
            } else if (Arrays.equals(type, SmartTap2Values.HANDSET_EPHEMERAL_PUBLIC_KEY_NDEF_TYPE)) {
                builder.setHandsetEphemeralPublicKey(ndefRecord.getPayload());
            } else {
                LOG.w("Unrecognized nested NDEF type %s", SmartTap2Values.getNdefType(type));
            }
        }
        builder.setStatusWord(StatusWords.get(Arrays.copyOfRange(bArr, bArr.length - 2, bArr.length), s));
        return builder.build();
    }
}
