package com.google.android.libraries.commerce.hce.crypto;

import com.google.common.primitives.Bytes;

public class Version0EncryptionParameters {
    private final byte[] infoBytes;
    private final byte[] peerKeyBytes;

    public Version0EncryptionParameters(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        this.peerKeyBytes = bArr;
        this.infoBytes = Bytes.concat(bArr2, bArr3);
    }

    public byte[] getPeerKeyBytes() {
        return this.peerKeyBytes;
    }

    public byte[] getInfoBytes() {
        return this.infoBytes;
    }

    public int getMacLength() {
        return 8;
    }
}
