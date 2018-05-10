package org.bouncycastle.util;

import java.math.BigInteger;

public final class BigIntegers {
    private static final BigInteger ZERO = BigInteger.valueOf(0);

    public static byte[] asUnsignedByteArray(int i, BigInteger bigInteger) {
        int i2 = 0;
        Object toByteArray = bigInteger.toByteArray();
        if (toByteArray.length == i) {
            return toByteArray;
        }
        if (toByteArray[0] == (byte) 0) {
            i2 = 1;
        }
        int length = toByteArray.length - i2;
        if (length > i) {
            throw new IllegalArgumentException("standard length exceeded for value");
        }
        Object obj = new byte[i];
        System.arraycopy(toByteArray, i2, obj, obj.length - length, length);
        return obj;
    }

    public static BigInteger fromUnsignedByteArray(byte[] bArr, int i, int i2) {
        if (!(i == 0 && i2 == bArr.length)) {
            Object obj = new byte[i2];
            System.arraycopy(bArr, i, obj, 0, i2);
            bArr = obj;
        }
        return new BigInteger(1, bArr);
    }
}
