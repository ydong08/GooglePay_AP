package com.google.common.primitives;

import com.google.common.base.Preconditions;

public final class Shorts {
    private Shorts() {
    }

    public static byte[] toByteArray(short s) {
        return new byte[]{(byte) (s >> 8), (byte) s};
    }

    public static short fromByteArray(byte[] bArr) {
        boolean z;
        if (bArr.length >= 2) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "array too small: %s < %s", bArr.length, 2);
        return fromBytes(bArr[0], bArr[1]);
    }

    public static short fromBytes(byte b, byte b2) {
        return (short) ((b << 8) | (b2 & 255));
    }
}
