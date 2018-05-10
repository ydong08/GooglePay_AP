package com.google.common.primitives;

public final class UnsignedBytes {
    private UnsignedBytes() {
    }

    public static int toInt(byte b) {
        return b & 255;
    }
}
