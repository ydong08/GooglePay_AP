package org.bouncycastle.util.encoders;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class Hex {
    private static final Encoder encoder = new HexEncoder();

    public static byte[] encode(byte[] bArr) {
        return encode(bArr, 0, bArr.length);
    }

    public static byte[] encode(byte[] bArr, int i, int i2) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encoder.encode(bArr, i, i2, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable e) {
            Throwable th = e;
            String str = "exception encoding Hex string: ";
            String valueOf = String.valueOf(th.getMessage());
            throw new EncoderException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), th);
        }
    }

    public static byte[] decode(String str) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encoder.decode(str, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable e) {
            Throwable th = e;
            String str2 = "exception decoding Hex string: ";
            String valueOf = String.valueOf(th.getMessage());
            throw new DecoderException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), th);
        }
    }
}
