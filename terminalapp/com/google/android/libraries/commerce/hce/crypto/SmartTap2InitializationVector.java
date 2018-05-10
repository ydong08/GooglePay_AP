package com.google.android.libraries.commerce.hce.crypto;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import javax.crypto.spec.IvParameterSpec;

public class SmartTap2InitializationVector {
    private byte[] bytes;
    private int counter = 0;

    public SmartTap2InitializationVector(byte[] bArr) {
        boolean z;
        if (bArr.length == 12) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, String.format("IV must be %d bytes", new Object[]{Integer.valueOf(12)}));
        this.bytes = (byte[]) bArr.clone();
    }

    public IvParameterSpec getIvParameterSpec() {
        return new IvParameterSpec(Bytes.concat(this.bytes, Ints.toByteArray(this.counter)));
    }

    public byte[] getBytes() {
        return (byte[]) this.bytes.clone();
    }
}
