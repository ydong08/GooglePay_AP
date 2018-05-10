package com.google.common.primitives;

import com.google.common.base.Preconditions;
import java.util.Arrays;

public final class Longs {
    private static final byte[] asciiDigits = createAsciiDigits();

    private Longs() {
    }

    public static byte[] toByteArray(long j) {
        byte[] bArr = new byte[8];
        for (int i = 7; i >= 0; i--) {
            bArr[i] = (byte) ((int) (255 & j));
            j >>= 8;
        }
        return bArr;
    }

    public static long fromByteArray(byte[] bArr) {
        boolean z;
        if (bArr.length >= 8) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "array too small: %s < %s", bArr.length, 8);
        return fromBytes(bArr[0], bArr[1], bArr[2], bArr[3], bArr[4], bArr[5], bArr[6], bArr[7]);
    }

    public static long fromBytes(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        return ((((((((((long) b) & 255) << 56) | ((((long) b2) & 255) << 48)) | ((((long) b3) & 255) << 40)) | ((((long) b4) & 255) << 32)) | ((((long) b5) & 255) << 24)) | ((((long) b6) & 255) << 16)) | ((((long) b7) & 255) << 8)) | (((long) b8) & 255);
    }

    private static byte[] createAsciiDigits() {
        int i = 0;
        byte[] bArr = new byte[128];
        Arrays.fill(bArr, (byte) -1);
        for (int i2 = 0; i2 <= 9; i2++) {
            bArr[i2 + 48] = (byte) i2;
        }
        while (i <= 26) {
            bArr[i + 65] = (byte) (i + 10);
            bArr[i + 97] = (byte) (i + 10);
            i++;
        }
        return bArr;
    }

    private static int digit(char c) {
        return c < '' ? asciiDigits[c] : -1;
    }

    public static Long tryParse(String str) {
        return tryParse(str, 10);
    }

    public static Long tryParse(String str, int i) {
        if (((String) Preconditions.checkNotNull(str)).isEmpty()) {
            return null;
        }
        if (i < 2 || i > 36) {
            throw new IllegalArgumentException("radix must be between MIN_RADIX and MAX_RADIX but was " + i);
        }
        int i2;
        int i3;
        if (str.charAt(0) == '-') {
            i2 = 1;
        } else {
            i2 = 0;
        }
        if (i2 != 0) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        if (i3 == str.length()) {
            return null;
        }
        int i4 = i3 + 1;
        i3 = digit(str.charAt(i3));
        if (i3 < 0 || i3 >= i) {
            return null;
        }
        long j = (long) (-i3);
        long j2 = Long.MIN_VALUE / ((long) i);
        while (i4 < str.length()) {
            int i5 = i4 + 1;
            i4 = digit(str.charAt(i4));
            if (i4 < 0 || i4 >= i || j < j2) {
                return null;
            }
            j *= (long) i;
            if (j < ((long) i4) - Long.MIN_VALUE) {
                return null;
            }
            j -= (long) i4;
            i4 = i5;
        }
        if (i2 != 0) {
            return Long.valueOf(j);
        }
        if (j == Long.MIN_VALUE) {
            return null;
        }
        return Long.valueOf(-j);
    }
}
