package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.common.StatusWords;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefMessages;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.Set;

public class SessionResponse extends Session {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private StatusWord statusWord;

    public static class Builder extends com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Builder {
        protected StatusWord statusWord;

        protected Builder() {
        }

        public Builder setStatusWord(StatusWord statusWord) {
            this.statusWord = statusWord;
            return this;
        }

        public SessionResponse build() throws SmartTapV2Exception {
            boolean z;
            boolean z2 = true;
            SmartTapV2Exception.check(this.sessionId != null, Status.NDEF_FORMAT_ERROR, Code.EXECUTION_FAILURE, "No session ID was set.", new Object[0]);
            if (this.sequenceNumber != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.EXECUTION_FAILURE, "No sequence number was set.", new Object[0]);
            if (this.statusWord == null) {
                z2 = false;
            }
            SmartTapV2Exception.check(z2, Status.NDEF_FORMAT_ERROR, Code.EXECUTION_FAILURE, "No status word was set.", new Object[0]);
            return new SessionResponse(this.sessionId, this.sequenceNumber.byteValue(), this.statusWord);
        }
    }

    protected SessionResponse(byte[] bArr, byte b, StatusWord statusWord) {
        super(bArr, b);
        this.statusWord = statusWord;
    }

    public StatusWord getStatusWord() {
        return this.statusWord;
    }

    public ResponseApdu composeSimpleResponse(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, byte[] bArr, byte[] bArr2, short s) {
        NdefRecord[] ndefRecordArr = new NdefRecord[]{composeNdef(s)};
        return ResponseApdu.fromDataAndStatusWord(NdefMessages.compose((Multimap) multimap, (Set) set, bArr, s, NdefRecords.compose(bArr2, NdefMessages.compose((Multimap) multimap, (Set) set, bArr2, s, ndefRecordArr).toByteArray(), s)).toByteArray(), this.statusWord);
    }

    public static SessionResponse parse(byte[] bArr, byte[] bArr2, short s) throws SmartTapV2Exception {
        Builder builder = new Builder();
        for (NdefRecord ndefRecord : decode(bArr, bArr2, s)) {
            if (Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.SESSION_NDEF_TYPE)) {
                builder.setSession(ndefRecord, s);
            } else {
                LOG.w("Unrecognized nested NDEF type %s", SmartTap2Values.getNdefType(NdefRecords.getType(ndefRecord, s)));
            }
        }
        builder.setStatusWord(StatusWords.get(Arrays.copyOfRange(bArr, bArr.length - 2, bArr.length), s));
        return builder.build();
    }

    public static NdefRecord[] decode(byte[] bArr, byte[] bArr2, short s) throws SmartTapV2Exception {
        byte[] copyOfRange = Arrays.copyOfRange(bArr, 0, bArr.length - 2);
        LOG.d("Response payload: %s", Hex.encode(copyOfRange));
        if (copyOfRange.length == 0) {
            throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.EXECUTION_FAILURE, "Response payload contains no data.");
        }
        NdefRecord[] records = SmartTapV2Exception.tryParseNdefMessage(copyOfRange, "response").getRecords();
        if (records.length != 1) {
            throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.EXECUTION_FAILURE, String.format("Response payload should contain exactly 1 NDEF record, but found %s", new Object[]{Integer.valueOf(records.length)}));
        }
        NdefRecord ndefRecord = records[0];
        if (Arrays.equals(NdefRecords.getType(ndefRecord, s), bArr2)) {
            NdefMessage tryParseNdefMessage = SmartTapV2Exception.tryParseNdefMessage(ndefRecord.getPayload(), "nested response record");
            if (tryParseNdefMessage.getRecords().length != 0) {
                return tryParseNdefMessage.getRecords();
            }
            throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.EXECUTION_FAILURE, "Response NDEF nested payload does not contain any records. Expected to at least find a session ndef record.");
        }
        throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.EXECUTION_FAILURE, String.format("Response NDEF record does not have expected ID %s. Found: %s", new Object[]{SmartTap2Values.getNdefType(bArr2), SmartTap2Values.getNdefType(ndefRecord.getType())}));
    }
}
