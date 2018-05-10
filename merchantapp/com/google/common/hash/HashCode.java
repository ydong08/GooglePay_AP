package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;

public abstract class HashCode {
    private static final char[] hexDigits = "0123456789abcdef".toCharArray();

    static final class BytesHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final byte[] bytes;

        BytesHashCode(byte[] bArr) {
            this.bytes = (byte[]) Preconditions.checkNotNull(bArr);
        }

        public int bits() {
            return this.bytes.length * 8;
        }

        public byte[] asBytes() {
            return (byte[]) this.bytes.clone();
        }

        public int asInt() {
            boolean z;
            if (this.bytes.length >= 4) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkState(z, "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", this.bytes.length);
            return (((this.bytes[0] & 255) | ((this.bytes[1] & 255) << 8)) | ((this.bytes[2] & 255) << 16)) | ((this.bytes[3] & 255) << 24);
        }

        void writeBytesToImpl(byte[] bArr, int i, int i2) {
            System.arraycopy(this.bytes, 0, bArr, i, i2);
        }

        byte[] getBytesInternal() {
            return this.bytes;
        }

        boolean equalsSameBits(HashCode hashCode) {
            if (this.bytes.length != hashCode.getBytesInternal().length) {
                return false;
            }
            boolean z = true;
            for (int i = 0; i < this.bytes.length; i++) {
                int i2;
                if (this.bytes[i] == hashCode.getBytesInternal()[i]) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                z &= i2;
            }
            return z;
        }
    }

    public abstract byte[] asBytes();

    public abstract int asInt();

    public abstract int bits();

    abstract boolean equalsSameBits(HashCode hashCode);

    abstract void writeBytesToImpl(byte[] bArr, int i, int i2);

    HashCode() {
    }

    public int writeBytesTo(byte[] bArr, int i, int i2) {
        int min = Ints.min(i2, bits() / 8);
        Preconditions.checkPositionIndexes(i, i + min, bArr.length);
        writeBytesToImpl(bArr, i, min);
        return min;
    }

    byte[] getBytesInternal() {
        return asBytes();
    }

    static HashCode fromBytesNoCopy(byte[] bArr) {
        return new BytesHashCode(bArr);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof HashCode)) {
            return false;
        }
        HashCode hashCode = (HashCode) obj;
        if (bits() == hashCode.bits() && equalsSameBits(hashCode)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        if (bits() >= 32) {
            return asInt();
        }
        byte[] bytesInternal = getBytesInternal();
        int i = bytesInternal[0] & 255;
        for (int i2 = 1; i2 < bytesInternal.length; i2++) {
            i |= (bytesInternal[i2] & 255) << (i2 * 8);
        }
        return i;
    }

    public final String toString() {
        byte[] bytesInternal = getBytesInternal();
        StringBuilder stringBuilder = new StringBuilder(bytesInternal.length * 2);
        for (byte b : bytesInternal) {
            stringBuilder.append(hexDigits[(b >> 4) & 15]).append(hexDigits[b & 15]);
        }
        return stringBuilder.toString();
    }
}
