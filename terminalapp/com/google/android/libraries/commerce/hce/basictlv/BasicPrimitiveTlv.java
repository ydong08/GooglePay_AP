package com.google.android.libraries.commerce.hce.basictlv;

import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.common.base.Joiner;
import com.google.common.primitives.UnsignedBytes;
import java.util.Arrays;
import java.util.Objects;

public class BasicPrimitiveTlv extends BasicTlv {
    private final int mLength;
    private final byte[] mValue;

    private BasicPrimitiveTlv(int i, byte[] bArr) throws BasicTlvInvalidTagException {
        super(i);
        this.mLength = bArr.length;
        this.mValue = bArr;
        if (BasicTlv.tagIsConstructed(i)) {
            throw new BasicTlvInvalidTagException(i);
        }
    }

    public static BasicPrimitiveTlv getInstance(int i, int i2, byte[] bArr, int i3) throws BasicTlvInvalidTagException, BasicTlvInvalidValueException {
        int i4 = i3 + i2;
        if (i4 <= bArr.length) {
            return new BasicPrimitiveTlv(i, Arrays.copyOfRange(bArr, i3, i4));
        }
        throw new BasicTlvInvalidValueException(i2, bArr.length - i3);
    }

    public final int getLength() {
        return this.mLength;
    }

    public final byte[] getValue() {
        return this.mValue;
    }

    public final int getByte() throws BasicTlvInvalidLengthException {
        if (this.mLength == 1) {
            return UnsignedBytes.toInt(this.mValue[0]);
        }
        throw new BasicTlvInvalidLengthException("Invalid byte length: " + getLength());
    }

    public final int getShort() throws BasicTlvInvalidLengthException {
        if (this.mLength == 2) {
            return (UnsignedBytes.toInt(this.mValue[0]) << 8) | UnsignedBytes.toInt(this.mValue[1]);
        }
        throw new BasicTlvInvalidLengthException("Invalid short length: " + getLength());
    }

    protected final int encodeValue(byte[] bArr, int i) {
        int i2 = this.mLength;
        System.arraycopy(this.mValue, 0, bArr, i, i2);
        return i2 + i;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BasicPrimitiveTlv)) {
            return false;
        }
        BasicPrimitiveTlv basicPrimitiveTlv = (BasicPrimitiveTlv) obj;
        if (this.mLength == basicPrimitiveTlv.mLength && Arrays.equals(this.mValue, basicPrimitiveTlv.mValue)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mLength), Integer.valueOf(Arrays.hashCode(this.mValue))});
    }

    public final String toString() {
        String valueOf = String.valueOf(Joiner.on(",").join(new String[]{getTagAsString(), Integer.toHexString(getLength()), Hex.encode(this.mValue)}).toUpperCase());
        return new StringBuilder(String.valueOf(valueOf).length() + 2).append("(").append(valueOf).append(")").toString();
    }
}
