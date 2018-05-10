package com.google.android.libraries.commerce.hce.util;

import com.google.common.base.Preconditions;

public class Hex {
    static final char[] LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    static final char[] UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private Hex() {
    }

    public static String encodeUpper(byte b) {
        return doEncode(b, UPPER);
    }

    public static String encode(byte[] bArr) {
        return doEncode(bArr, LOWER);
    }

    public static String encodeUpper(byte[] bArr) {
        return doEncode(bArr, UPPER);
    }

    public static byte[] decode(String str) throws IllegalArgumentException {
        boolean z;
        int length = str.length();
        if (length % 2 == 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "String not of even length: %s", (Object) str);
        byte[] bArr = new byte[(length / 2)];
        int i = 0;
        int i2 = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            char charAt2 = str.charAt(i + 1);
            int digit = Character.digit(charAt, 16);
            int digit2 = Character.digit(charAt2, 16);
            if (digit != -1) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "Invalid character: '%s'", String.valueOf(charAt));
            if (digit2 != -1) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "Invalid character: '%s'", String.valueOf(charAt2));
            int i3 = i2 + 1;
            bArr[i2] = (byte) ((digit << 4) | digit2);
            i += 2;
            i2 = i3;
        }
        return bArr;
    }

    private static String doEncode(byte[] bArr, char[] cArr) {
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 2);
        for (byte doEncode : bArr) {
            stringBuilder.append(doEncode(doEncode, cArr));
        }
        return stringBuilder.toString();
    }

    private static String doEncode(byte b, char[] cArr) {
        return cArr[(b & 240) >> 4] + cArr[b & 15];
    }
}
