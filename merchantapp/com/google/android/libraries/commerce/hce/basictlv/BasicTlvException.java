package com.google.android.libraries.commerce.hce.basictlv;

public class BasicTlvException extends Exception {
    public BasicTlvException(String str) {
        super(str);
    }

    public BasicTlvException(String str, Throwable th) {
        super(str, th);
    }
}
