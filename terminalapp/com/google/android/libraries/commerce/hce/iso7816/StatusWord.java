package com.google.android.libraries.commerce.hce.iso7816;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class StatusWord implements Parcelable {
    protected final Code code;

    public enum Code {
        UNSPECIFIED("unspecified status"),
        SUCCESS("success"),
        SUCCESS_NO_PAYLOAD("no payload"),
        SUCCESS_PRESIGNED_AUTH("presigned authorization"),
        SUCCESS_MORE_PAYLOAD("more data remaining"),
        SUCCESS_WITH_PAYLOAD_PAYMENT_NOT_READY("success with payload but payment not ready"),
        SUCCESS_NO_PAYLOAD_PAYMENT_NOT_READY("no payload and payment not ready"),
        SUCCESS_PAYMENT_NOT_READY_UNKNOWN_REASON("success and payment not ready"),
        UNKNOWN_TRANSIENT_FAILURE("unknown transient failure"),
        CRYPTO_FAILURE("crypto exception"),
        TIMEOUT_FAILURE("operation timed out"),
        EXECUTION_FAILURE("execution exception"),
        UNKNOWN_HANDSET_ERROR("unknown handset error"),
        DEVICE_LOCKED("device locked"),
        NO_PAYMENT_INSTRUMENT("no payment card"),
        UNKNOWN_USER_ACTION_NEEDED("user action required"),
        UNKNOWN_TERMINAL_COMMAND("unknown terminal command"),
        UNKNOWN_NDEF_RECORD("unknown ndef record"),
        PARSING_FAILURE("parsing failure"),
        INVALID_CRYPTO_INPUT("invalid crypto params"),
        REQUEST_MORE_NOT_APPLICABLE("requested more data without requesting data"),
        MORE_DATA_NOT_AVAILABLE("requested more data when all has already been transmitted"),
        TOO_MANY_REQUESTS("too many requests in the alloted time"),
        NO_MERCHANT_SET("no merchant ID has been provided from the terminal"),
        INVALID_PUSHBACK_URI("push back URI from the terminal is invalid"),
        UNKNOWN_TERMINAL_ERROR("unknown terminal error"),
        AUTH_FAILED("cannot authenticate terminal"),
        PUSH_FAIL_NO_AUTH("cannot push data without authentication"),
        VERSION_NOT_SUPPORTED("No version match between terminal and mobile device"),
        UNKNOWN_PERMANENT_ERROR("unable to send data"),
        UNKNOWN_ERROR("unknown error"),
        UNKNOWN_CODE("unknown code"),
        UNKNOWN_AID("AID not found");
        
        private final String message;

        private Code(String str) {
            this.message = str;
        }

        public String getMessage() {
            return this.message;
        }
    }

    public abstract byte[] toBytes();

    public abstract int toInt();

    protected StatusWord(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return this.code;
    }

    public boolean isRetriableFailure() {
        return false;
    }

    public boolean allowsPayment() {
        return true;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.code.name());
    }

    protected StatusWord(Parcel parcel) {
        this.code = Code.valueOf(parcel.readString());
    }
}
