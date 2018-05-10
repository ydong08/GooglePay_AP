package com.google.android.libraries.commerce.hce.iso7816;

public class CommandApduException extends Exception {
    private final ResponseApdu mErrorResponse;

    public CommandApduException(ResponseApdu responseApdu) {
        this.mErrorResponse = responseApdu;
    }

    public ResponseApdu getErrorResponse() {
        return this.mErrorResponse;
    }
}
