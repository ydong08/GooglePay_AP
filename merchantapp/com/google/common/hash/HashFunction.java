package com.google.common.hash;

public abstract class HashFunction {
    public abstract Hasher newHasher();

    HashFunction() {
    }

    public HashCode hashBytes(byte[] bArr) {
        return newHasher().putBytes(bArr).hash();
    }
}
