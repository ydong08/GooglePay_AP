package com.google.android.libraries.commerce.hce.basictlv;

public class BasicTlvInvalidTagException extends BasicTlvException {
    public BasicTlvInvalidTagException(int i) {
        this(BasicTlv.getTagAsString(i));
    }

    public BasicTlvInvalidTagException(String str) {
        super(str);
    }
}
