package org.bouncycastle.util;

public abstract class Pack {
    public static void intToBigEndian(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) (i >>> 24);
        int i3 = i2 + 1;
        bArr[i3] = (byte) (i >>> 16);
        i3++;
        bArr[i3] = (byte) (i >>> 8);
        bArr[i3 + 1] = (byte) i;
    }
}
