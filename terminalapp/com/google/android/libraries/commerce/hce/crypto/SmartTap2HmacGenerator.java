package com.google.android.libraries.commerce.hce.crypto;

import com.google.common.hash.Hashing;
import com.google.common.primitives.Bytes;

public class SmartTap2HmacGenerator {
    public byte[] generateHmac(byte[] bArr, SmartTap2InitializationVector smartTap2InitializationVector, byte[] bArr2, int i) {
        byte[] bArr3 = new byte[i];
        Hashing.hmacSha256(bArr).hashBytes(Bytes.concat(smartTap2InitializationVector.getBytes(), bArr2)).writeBytesTo(bArr3, 0, i);
        return bArr3;
    }
}
