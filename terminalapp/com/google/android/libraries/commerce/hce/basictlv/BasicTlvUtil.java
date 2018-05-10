package com.google.android.libraries.commerce.hce.basictlv;

import java.io.ByteArrayOutputStream;

public class BasicTlvUtil {
    private BasicTlvUtil() {
    }

    public static BasicPrimitiveTlv tlv(int i, byte[] bArr) {
        try {
            return BasicPrimitiveTlv.getInstance(i, bArr.length, bArr, 0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
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
