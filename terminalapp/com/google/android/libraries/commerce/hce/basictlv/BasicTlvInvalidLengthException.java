package com.google.android.libraries.commerce.hce.basictlv;

public class BasicTlvInvalidLengthException extends BasicTlvException {
    public BasicTlvInvalidLengthException(int i) {
        this(Integer.toHexString(i));
    }

    public BasicTlvInvalidLengthException(int i, int i2) {
        this("expected=" + i + " actual=" + i2);
    }

    public BasicTlvInvalidLengthException(String str) {
        super(str);
    }
}
