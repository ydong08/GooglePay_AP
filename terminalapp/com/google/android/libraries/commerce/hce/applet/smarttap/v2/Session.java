package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.common.base.Preconditions;
import java.util.Arrays;

public class Session {
    private byte sequenceNumber;
    private byte[] sessionId;
    private Status status;

    public static abstract class Builder {
        protected Byte sequenceNumber = null;
        protected byte[] sessionId = null;
        protected Status status = Status.OK;

        public void setSession(NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
            Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.SESSION_NDEF_TYPE));
            byte[] payload = ndefRecord.getPayload();
            if (payload.length != 10) {
                throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, String.format("Expected session ndef record to contain session ID, sequence number, and status with exactly length %s. Found length was %s.", new Object[]{Integer.valueOf(10), Integer.valueOf(payload.length)}));
            }
            setSessionId(Arrays.copyOfRange(payload, 0, 8));
            setSequenceNumber(payload[8]);
            setStatus(Status.get(payload[9]));
        }

        public Builder setSessionId(byte[] bArr) throws SmartTapV2Exception {
            boolean z;
            SmartTapV2Exception.check(bArr != null, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Cannot set null session ID.", new Object[0]);
            if (bArr.length == 8) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Session ID must have exactly length %s. Found length %s.", Integer.valueOf(8), Integer.valueOf(bArr.length));
            this.sessionId = bArr;
            return this;
        }

        public Builder setSequenceNumber(byte b) {
            this.sequenceNumber = Byte.valueOf(b);
            return this;
        }

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }
    }

    public enum Status {
        UNKNOWN((byte) 0),
        OK((byte) 1),   //
        NDEF_FORMAT_ERROR((byte) 2),  //
        UNSUPPORTED_VERSION((byte) 3),
        INVALID_SEQUENCE_NUMBER((byte) 4),   //
        UNSUPPORTED_MERCHANT((byte) 5),
        MERCHANT_DATA_MISSING((byte) 6),   //
        SERVICE_DATA_MISSING((byte) 7),
        RESEND_REQUEST((byte) 8),
        DATA_NOT_YET_AVAILABLE((byte) 9);
        
        private final byte value;

        private Status(byte b) {
            this.value = b;
        }

        public byte value() {
            return this.value;
        }

        public static Status get(byte b) {
            for (Status status : values()) {
                if (b == status.value()) {
                    return status;
                }
            }
            throw new RuntimeException("Byte " + b + " was not a backing value for Session.Status.");
        }
    }

    protected Session(byte[] bArr, byte b) {
        this(bArr, b, Status.OK);
    }

    protected Session(byte[] bArr, byte b, Status status) {
        this.sessionId = bArr;
        this.sequenceNumber = b;
        this.status = status;
    }

    public byte[] getSessionId() {
        return this.sessionId;
    }

    public byte getSequenceNumber() {
        return this.sequenceNumber;
    }

    public Status getStatus() {
        return this.status;
    }
}
