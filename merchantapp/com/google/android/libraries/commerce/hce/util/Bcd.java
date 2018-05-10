package com.google.android.libraries.commerce.hce.util;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class Bcd {
    public static byte[] encode(long j) {
        return encode(Long.toString(j));
    }

    public static byte[] encodeWithPadding(long j, int i) {
        byte[] encode = encode(j);
        if (encode.length > i) {
            throw new IllegalArgumentException(String.format("Cannot encode %d into %d bytes", new Object[]{Long.valueOf(j), Integer.valueOf(i)}));
        } else if (encode.length >= i) {
            return encode;
        } else {
            return Bytes.concat(new byte[(i - encode.length)], encode);
        }
    }

    public static byte[] encode(String str) {
        int i;
        Object obj;
        char[] toCharArray = str.toCharArray();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(8);
        if (toCharArray.length % 2 == 1) {
            i = -16;
            obj = null;
        } else {
            i = 0;
            int i2 = 1;
        }
        int length = toCharArray.length;
        int i3 = 0;
        Object obj2 = obj;
        while (i3 < length) {
            byte b;
            Object obj3;
            byte b2 = (byte) (toCharArray[i3] - 48);
            if (obj2 != null) {
                b2 = (byte) (b2 << 4);
            } else {
                b = (byte) (i | b2);
                byteArrayOutputStream.write(b);
                b2 = b;
            }
            if (obj2 == null) {
                obj3 = 1;
            } else {
                obj3 = null;
            }
            i3++;
            obj2 = obj3;
            b = b2;
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static long decode(byte[] bArr) {
        long j = 0;
        if (bArr.length != 0) {
            int i;
            while (bArr[0] == (byte) 0) {
                bArr = Arrays.copyOfRange(bArr, 1, bArr.length);
            }
            if ((bArr[0] & 240) != 240 || (bArr[0] & 15) >= 10) {
                i = 0;
            } else {
                j = (long) (bArr[0] & 15);
                i = 1;
            }
            while (i < bArr.length) {
                byte b = (byte) ((bArr[i] & 255) >>> 4);
                byte b2 = (byte) (bArr[i] & 15);
                if (b > (byte) 9 || b2 > (byte) 9 || b < (byte) 0 || b2 < (byte) 0) {
                    throw new IllegalArgumentException(String.format("Invalid BCD: %s digits: %d, %d", new Object[]{BaseEncoding.base16().encode(bArr), Byte.valueOf(b), Byte.valueOf(b2)}));
                }
                j = ((j * 100) + ((long) (b * 10))) + ((long) b2);
                i++;
            }
        }
        return j;
    }
}
