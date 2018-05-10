package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class Hasher {
    private final ByteBuffer scratch;

    public abstract HashCode hash();

    protected abstract void update(byte b);

    Hasher() {
    }

    protected void update(byte[] bArr) {
        update(bArr, 0, bArr.length);
    }

    public Hasher putBytes(byte[] bArr) {
        Preconditions.checkNotNull(bArr);
        update(bArr);
        return this;
    }

    Hasher(byte b) {
        this();
        this.scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
    }

    protected void update(byte[] bArr, int i, int i2) {
        for (int i3 = i; i3 < i + i2; i3++) {
            update(bArr[i3]);
        }
    }
}
