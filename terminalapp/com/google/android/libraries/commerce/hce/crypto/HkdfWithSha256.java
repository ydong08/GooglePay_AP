package com.google.android.libraries.commerce.hce.crypto;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class HkdfWithSha256 {
    public static byte[] extractAndExpand(byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        return expand(extract(bArr, bArr2), bArr3, i);
    }

    public static byte[] extract(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[32];
        if (bArr2 == null || bArr2.length == 0) {
            bArr2 = new byte[32];
        }
        Hashing.hmacSha256(bArr2).hashBytes(bArr).writeBytesTo(bArr3, 0, 32);
        return bArr3;
    }

    public static byte[] expand(byte[] bArr, byte[] bArr2, int i) {
        Object obj = new byte[i];
        Object obj2 = new byte[32];
        int i2 = 0;
        int i3 = i;
        byte b = (byte) 1;
        while (i3 > 0) {
            byte[] bArr3 = new byte[]{b};
            Hasher newHasher = Hashing.hmacSha256(bArr).newHasher();
            if (b > (byte) 1) {
                newHasher.putBytes(obj2);
            }
            newHasher.putBytes(bArr2).putBytes(bArr3).hash().writeBytesTo(obj2, 0, 32);
            int i4 = 32 < i3 ? 32 : i3;
            System.arraycopy(obj2, 0, obj, i2, i4);
            i3 -= i4;
            b = (byte) (b + 1);
            i2 = i4 + i2;
        }
        return obj;
    }
}
