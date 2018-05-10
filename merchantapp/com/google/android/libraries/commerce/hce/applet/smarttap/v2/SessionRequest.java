package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.primitives.Shorts;
import java.util.Arrays;

public class SessionRequest extends Session {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private short version;

    public static class Builder extends com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Builder {
        protected Short version;

        protected Builder() {
        }

        public Builder setVersion(short s) {
            this.version = Short.valueOf(s);
            return this;
        }

        private Builder(byte b) {
            this();
        }

        public GetAdditionalSmartTapDataRequest build() throws SmartTapV2Exception {
            boolean z = true;
            SmartTapV2Exception.check(this.version != null, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No NDEF version was set.", new Object[0]);
            if (this.version.shortValue() == (short) 0) {
                boolean z2;
                if (this.sessionId != null) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                SmartTapV2Exception.check(z2, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No session ID was set.", new Object[0]);
                if (this.sequenceNumber == null) {
                    z = false;
                }
                SmartTapV2Exception.check(z, Status.INVALID_SEQUENCE_NUMBER, Code.PARSING_FAILURE, "No sequence number was set.", new Object[0]);
            } else {
                this.sequenceNumber = Byte.valueOf((byte) -1);
            }
            return new GetAdditionalSmartTapDataRequest(this.version.shortValue(), this.sessionId, this.sequenceNumber.byteValue());
        }
    }

    public static class DecodedRequest {
        private NdefRecord[] records;
        private short version;

        DecodedRequest(short s, NdefRecord[] ndefRecordArr) {
            this.version = s;
            this.records = ndefRecordArr;
        }

        public short getVersion() {
            return this.version;
        }

        public NdefRecord[] getNdefRecords() {
            return this.records;
        }
    }

    protected SessionRequest(short s, byte[] bArr, byte b) {
        super(bArr, b, Status.OK);
        this.version = s;
    }

    public short getVersion() {
        return this.version;
    }

    public static DecodedRequest decode(byte[] bArr, byte[] bArr2) throws SmartTapV2Exception {
        int i = bArr[4] & 255;
        byte[] copyOfRange = Arrays.copyOfRange(bArr, 5, i + 5);
        LOG.d("Request payload: %s", Hex.encode(copyOfRange));
        if (copyOfRange.length != 0) {
            NdefRecord[] records = SmartTapV2Exception.tryParseNdefMessage(copyOfRange, "APDU payload").getRecords();
            if (records.length != 1) {
                throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, String.format("Request payload should contain exactly 1 NDEF record, but found %s", new Object[]{Integer.valueOf(records.length)}));
            }
            NdefRecord ndefRecord = records[0];
            short verifyAndGetVersion = verifyAndGetVersion(ndefRecord);
            if (bArr.length > (i + 5) + 1) {
                throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Extra data beyond Le byte");
            } else if (verifyAndGetVersion >= (short) 1 && bArr.length != (i + 5) + 1) {
                throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Le byte must be present");
            } else if (bArr.length != (i + 5) + 1 || bArr[i + 5] == (byte) 0) {
                if (!Arrays.equals(NdefRecords.getType(ndefRecord, verifyAndGetVersion), bArr2)) {
                    throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, String.format("Request NDEF record does not have expected type %s. Found: %s", new Object[]{SmartTap2Values.getNdefType(bArr2), SmartTap2Values.getNdefType(r0)}));
                } else if (ndefRecord.getPayload().length == 2) {
                    throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Request NDEF does not contain any nested NDEF records.");
                } else {
                    NdefMessage tryParseNdefMessage = SmartTapV2Exception.tryParseNdefMessage(NdefRecords.getAllBytes(ndefRecord, 2), "APDU nested payload");
                    if (tryParseNdefMessage.getRecords().length != 0) {
                        return new DecodedRequest(verifyAndGetVersion, tryParseNdefMessage.getRecords());
                    }
                    throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Request NDEF nested payload does not contain any records. Expected to at least find a session ndef record.");
                }
            } else {
                throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Le byte must be 0");
            }
        } else if (bArr[1] == (byte) -64) {
            return new DecodedRequest((short) 1, null);
        } else {
            throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Request payload contains no data.");
        }
    }

    private static short verifyAndGetVersion(NdefRecord ndefRecord) throws SmartTapV2Exception {
        if (ndefRecord.getPayload().length < 2) {
            throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Request NDEF record is too short to contain a version.");
        }
        short fromByteArray = Shorts.fromByteArray(NdefRecords.getBytes(ndefRecord, 2));
        if (fromByteArray < SmartTap2Values.SMARTTAP_MIN_VERSION_SHORT) {
            throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.VERSION_NOT_SUPPORTED, String.format("NDEF record contains versions less than minimum supported version. NDEF version: %s. Minimum supported version: %s.", new Object[]{Hex.encodeUpper(r0), Hex.encodeUpper(SmartTap2Values.SMARTTAP_MIN_VERSION)}));
        } else if (fromByteArray >= SmartTap2Values.SMARTTAP_MIN_VERSION_SHORT) {
            return fromByteArray;
        } else {
            throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.VERSION_NOT_SUPPORTED, String.format("NDEF record contains versions less than minimum supported version. NDEF version: %s. Minimum supported version: %s.", new Object[]{Hex.encodeUpper(r0), Hex.encodeUpper(SmartTap2Values.SMARTTAP_MIN_VERSION)}));
        }
    }
}
