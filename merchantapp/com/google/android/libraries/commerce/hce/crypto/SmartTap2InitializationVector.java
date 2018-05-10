package com.google.android.libraries.commerce.hce.crypto;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import java.security.SecureRandom;
import javax.crypto.spec.IvParameterSpec;

public class SmartTap2InitializationVector {
    private byte[] bytes = new byte[12];
    private int counter = 0;

    public SmartTap2InitializationVector(SecureRandom secureRandom) {
        secureRandom.nextBytes(this.bytes);
    }

    public IvParameterSpec getIvParameterSpec() {
        return new IvParameterSpec(Bytes.concat(this.bytes, Ints.toByteArray(this.counter)));
    }

    public byte[] getBytes() {
        return (byte[]) this.bytes.clone();
    }
}
