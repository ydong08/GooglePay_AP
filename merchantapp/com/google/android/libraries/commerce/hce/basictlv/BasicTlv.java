package com.google.android.libraries.commerce.hce.basictlv;

import com.google.android.libraries.commerce.hce.base.NonnullFunction;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.common.base.Function;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedBytes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BasicTlv {
    public static final Function<BasicTlv, Integer> AS_TAG = new NonnullFunction<BasicTlv, Integer>() {
        public Integer applyNonnull(BasicTlv basicTlv) {
            return Integer.valueOf(basicTlv.getTag());
        }
    };
    private final int mTag;

    public static class TagLengthBuffer {
        private final byte[] mBuf;
        private int mOffset;

        TagLengthBuffer(byte[] bArr, int i) {
            this.mBuf = bArr;
            this.mOffset = i;
        }

        public int getTag() throws BasicTlvInvalidTagException {
            byte[] bArr = this.mBuf;
            int i = this.mOffset;
            this.mOffset = i + 1;
            int toInt = UnsignedBytes.toInt(bArr[i]);
            if ((toInt & 31) != 31) {
                return toInt;
            }
            byte[] bArr2 = this.mBuf;
            int i2 = this.mOffset;
            this.mOffset = i2 + 1;
            i = UnsignedBytes.toInt(bArr2[i2]);
            if ((i & 128) == 0) {
                return (toInt << 8) | i;
            }
            throw new BasicTlvInvalidTagException((toInt << 8) | i);
        }

        public int getLength() throws BasicTlvInvalidLengthException {
            byte[] bArr = this.mBuf;
            int i = this.mOffset;
            this.mOffset = i + 1;
            int toInt = UnsignedBytes.toInt(bArr[i]);
            if ((toInt & 128) == 0) {
                return toInt;
            }
            byte[] bArr2;
            int i2;
            switch (toInt & -129) {
                case 0:
                    throw new BasicTlvInvalidLengthException(toInt);
                case 1:
                    bArr = this.mBuf;
                    i = this.mOffset;
                    this.mOffset = i + 1;
                    return UnsignedBytes.toInt(bArr[i]);
                case 2:
                    bArr = this.mBuf;
                    i = this.mOffset;
                    this.mOffset = i + 1;
                    toInt = UnsignedBytes.toInt(bArr[i]) << 8;
                    bArr2 = this.mBuf;
                    i2 = this.mOffset;
                    this.mOffset = i2 + 1;
                    return toInt | UnsignedBytes.toInt(bArr2[i2]);
                case 3:
                    bArr = this.mBuf;
                    i = this.mOffset;
                    this.mOffset = i + 1;
                    toInt = UnsignedBytes.toInt(bArr[i]) << 16;
                    bArr2 = this.mBuf;
                    i2 = this.mOffset;
                    this.mOffset = i2 + 1;
                    toInt |= UnsignedBytes.toInt(bArr2[i2]) << 8;
                    bArr2 = this.mBuf;
                    i2 = this.mOffset;
                    this.mOffset = i2 + 1;
                    return toInt | UnsignedBytes.toInt(bArr2[i2]);
                default:
                    throw new BasicTlvInvalidLengthException(toInt);
            }
        }

        int getOffset() {
            return this.mOffset;
        }
    }

    protected abstract int encodeValue(byte[] bArr, int i) throws BasicTlvInvalidTagException, BasicTlvInvalidLengthException;

    public abstract int getLength();

    protected BasicTlv(int i) throws BasicTlvInvalidTagException {
        int tagSize = getTagSize(i);
        if (tagSize == 1) {
            if ((i & 31) == 31) {
                throw new BasicTlvInvalidTagException(i);
            }
        } else if ((((i >> ((tagSize - 1) * 8)) & 255) & 31) != 31) {
            throw new BasicTlvInvalidTagException(i);
        }
        this.mTag = i;
    }

    public static List<BasicTlv> getDecodedInstances(byte[] bArr) throws BasicTlvException {
        return getDecodedInstances(bArr, 0, bArr.length);
    }

    public static List<BasicTlv> getDecodedInstances(byte[] bArr, int i, int i2) throws BasicTlvInvalidLengthException, BasicTlvInvalidTagException, BasicTlvInvalidValueException {
        List<BasicTlv> arrayList = new ArrayList();
        while (i < i2) {
            BasicTlv decodedInstance = getDecodedInstance(bArr, i);
            arrayList.add(decodedInstance);
            i += decodedInstance.getSize();
        }
        return arrayList;
    }

    protected static BasicTlv getDecodedInstance(byte[] bArr, int i) throws BasicTlvInvalidLengthException, BasicTlvInvalidTagException, BasicTlvInvalidValueException {
        TagLengthBuffer tagLengthBuffer = new TagLengthBuffer(bArr, i);
        int tag = tagLengthBuffer.getTag();
        int length = tagLengthBuffer.getLength();
        int offset = tagLengthBuffer.getOffset();
        if (tagIsConstructed(tag)) {
            return BasicConstructedTlv.getDecodedInstance(tag, length, bArr, offset);
        }
        return BasicPrimitiveTlv.getInstance(tag, length, bArr, offset);
    }

    public final BasicPrimitiveTlv asPrimitiveTlv() throws BasicTlvInvalidTagException {
        if (!tagIsConstructed(this.mTag)) {
            return (BasicPrimitiveTlv) this;
        }
        String str = "Not primitive: ";
        String valueOf = String.valueOf(getTagAsString());
        throw new BasicTlvInvalidTagException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    public final BasicConstructedTlv asConstructedTlv() throws BasicTlvInvalidTagException {
        if (tagIsConstructed(this.mTag)) {
            return (BasicConstructedTlv) this;
        }
        String str = "Not constructed: ";
        String valueOf = String.valueOf(getTagAsString());
        throw new BasicTlvInvalidTagException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    public final byte[] toByteArray() throws BasicTlvInvalidTagException, BasicTlvInvalidLengthException {
        int size = getSize();
        byte[] bArr = new byte[size];
        int encode = encode(bArr, 0);
        if (size == encode) {
            return bArr;
        }
        throw new BasicTlvInvalidLengthException(size, encode);
    }

    public final int getTag() {
        return this.mTag;
    }

    public final String getTagAsString() {
        return getTagAsString(this.mTag);
    }

    public static final String getTagAsString(int i) {
        int tagSize;
        try {
            tagSize = 4 - getTagSize(i);
        } catch (BasicTlvInvalidTagException e) {
            tagSize = 0;
        }
        return Hex.encode(Arrays.copyOfRange(Ints.toByteArray(i), tagSize, 4)).toUpperCase();
    }

    public byte[] getValue() throws BasicTlvInvalidTagException, BasicTlvInvalidLengthException {
        byte[] bArr = new byte[getLength()];
        encodeValue(bArr, 0);
        return bArr;
    }

    public final int getSize() {
        return (getTagSize() + getLengthSize()) + getLength();
    }

    protected final int encode(byte[] bArr, int i) throws BasicTlvInvalidTagException, BasicTlvInvalidLengthException {
        int i2;
        int tagSize = (getTagSize() - 1) * 8;
        int i3 = i;
        while (tagSize >= 0) {
            i = i3 + 1;
            bArr[i3] = (byte) ((this.mTag >> tagSize) & 255);
            tagSize -= 8;
            i3 = i;
        }
        int length = getLength();
        tagSize = getLengthSize(length);
        if (tagSize > 1) {
            tagSize--;
            if (tagSize >= 128) {
                throw new BasicTlvInvalidLengthException(length);
            }
            i2 = i3 + 1;
            bArr[i3] = (byte) (tagSize | 128);
        } else {
            i2 = i3;
        }
        tagSize = (tagSize - 1) * 8;
        while (tagSize >= 0) {
            i3 = i2 + 1;
            bArr[i2] = (byte) ((length >> tagSize) & 255);
            tagSize -= 8;
            i2 = i3;
        }
        return encodeValue(bArr, i2);
    }

    static boolean tagIsConstructed(int i) throws BasicTlvInvalidTagException {
        switch (getTagSize(i)) {
            case 1:
                if ((i & 32) != 0) {
                    return true;
                }
                return false;
            case 2:
                if ((i & 8192) == 0) {
                    return false;
                }
                return true;
            case 3:
                return (2097152 & i) != 0;
            default:
                throw new BasicTlvInvalidTagException(i);
        }
    }

    public int getTagSize() {
        int tagSizeImpl = getTagSizeImpl(this.mTag);
        if (tagSizeImpl != -1) {
            return tagSizeImpl;
        }
        String str = "Invalid tag: ";
        String valueOf = String.valueOf(getTagAsString());
        throw new BasicTlvRuntimeException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    public static int getTagSize(int i) throws BasicTlvInvalidTagException {
        int tagSizeImpl = getTagSizeImpl(i);
        if (tagSizeImpl != -1) {
            return tagSizeImpl;
        }
        throw new BasicTlvInvalidTagException(i);
    }

    private static int getTagSizeImpl(int i) {
        if ((i & -256) == 0) {
            return 1;
        }
        if ((-65536 & i) == 0) {
            return 2;
        }
        if ((-16777216 & i) == 0) {
            return 3;
        }
        return -1;
    }

    public int getLengthSize() {
        int length = getLength();
        int lengthSizeImpl = getLengthSizeImpl(length);
        if (lengthSizeImpl != -1) {
            return lengthSizeImpl;
        }
        String str = "Invalid length: ";
        String valueOf = String.valueOf(Integer.toHexString(length));
        throw new BasicTlvRuntimeException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    private static int getLengthSize(int i) throws BasicTlvInvalidLengthException {
        int lengthSizeImpl = getLengthSizeImpl(i);
        if (lengthSizeImpl != -1) {
            return lengthSizeImpl;
        }
        throw new BasicTlvInvalidLengthException(i);
    }

    private static int getLengthSizeImpl(int i) {
        if (i >= 0) {
            if (i <= 127) {
                return 1;
            }
            if (i <= 255) {
                return 2;
            }
            if (i <= 65535) {
                return 3;
            }
        }
        return -1;
    }
}
