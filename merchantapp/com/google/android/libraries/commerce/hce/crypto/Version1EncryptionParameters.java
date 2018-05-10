package com.google.android.libraries.commerce.hce.crypto;

import com.google.common.primitives.Bytes;

public class Version1EncryptionParameters {
    private final byte[] infoBytes;
    private final byte[] peerKeyBytes;

    public Version1EncryptionParameters(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6) {
        this.peerKeyBytes = bArr;
        this.infoBytes = Bytes.concat(bArr3, bArr2, bArr4, bArr5, bArr6);
    }

    public byte[] getPeerKeyBytes() {
        return this.peerKeyBytes;
    }

    public byte[] getInfoBytes() {
        return this.infoBytes;
    }

    public int getMacLength() {
        return 32;
    }
}
