package com.google.commerce.tapandpay.merchantapp.paypass;

public class CryptoUtils {
    public static byte[] adjustParity(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            byte b = (byte) (bArr[i] & 254);
            byte b2 = (byte) ((b >>> 4) ^ b);
            b2 = (byte) (b2 ^ (b2 >>> 2));
            bArr[i] = (byte) (b | ((((byte) (b2 ^ (b2 >>> 1))) ^ -1) & 1));
        }
        return bArr;
    }
}
