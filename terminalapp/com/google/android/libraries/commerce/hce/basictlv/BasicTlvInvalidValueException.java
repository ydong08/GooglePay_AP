package com.google.android.libraries.commerce.hce.basictlv;

public class BasicTlvInvalidValueException extends BasicTlvException {
    public BasicTlvInvalidValueException(int i, int i2) {
        this("expectedLength=" + i + " actualLength=" + i2);
    }

    public BasicTlvInvalidValueException(String str) {
        super(str);
    }
}
