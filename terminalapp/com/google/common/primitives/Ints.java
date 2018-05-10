package com.google.common.primitives;

import com.google.common.base.Preconditions;

public final class Ints {
    private Ints() {
    }

    public static int saturatedCast(long j) {
        if (j > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (j < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        return (int) j;
    }

    public static int min(int... iArr) {
        boolean z;
        int i = 1;
        if (iArr.length > 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z);
        int i2 = iArr[0];
        while (i < iArr.length) {
            if (iArr[i] < i2) {
                i2 = iArr[i];
            }
            i++;
        }
        return i2;
    }

    public static byte[] toByteArray(int i) {
        return new byte[]{(byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) i};
    }

    public static int fromByteArray(byte[] bArr) {
        boolean z;
        if (bArr.length >= 4) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "array too small: %s < %s", bArr.length, 4);
        return fromBytes(bArr[0], bArr[1], bArr[2], bArr[3]);
    }

    public static int fromBytes(byte b, byte b2, byte b3, byte b4) {
        return (((b << 24) | ((b2 & 255) << 16)) | ((b3 & 255) << 8)) | (b4 & 255);
    }
}
