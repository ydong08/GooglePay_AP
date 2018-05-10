package com.google.android.libraries.commerce.hce.basictlv;

import com.google.common.collect.ImmutableList;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class BasicTlvUtil {
    private BasicTlvUtil() {
    }

    public static BasicConstructedTlv tlv(int i, BasicTlv... basicTlvArr) {
        try {
            return BasicConstructedTlv.getInstance(i).addAll(Arrays.asList(basicTlvArr));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static BasicPrimitiveTlv tlv(int i, byte b) {
        try {
            return BasicPrimitiveTlv.getByteInstance(i, b);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static BasicPrimitiveTlv tlv(int i, byte[] bArr) {
        try {
            return BasicPrimitiveTlv.getInstance(i, bArr.length, bArr, 0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] tlvToByteArray(BasicTlv basicTlv) {
        try {
            return basicTlv.toByteArray();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] tlvToByteArray(BasicTlv... basicTlvArr) {
        return tlvToByteArray(ImmutableList.copyOf((Object[]) basicTlvArr));
    }

    public static byte[] tlvToByteArray(Iterable<BasicTlv> iterable) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            for (BasicTlv toByteArray : iterable) {
                byte[] toByteArray2 = toByteArray.toByteArray();
                byteArrayOutputStream.write(toByteArray2, 0, toByteArray2.length);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
