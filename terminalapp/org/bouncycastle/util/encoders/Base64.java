package org.bouncycastle.util.encoders;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class Base64 {
    private static final Encoder encoder = new Base64Encoder();

    public static byte[] decode(String str) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream((str.length() / 4) * 3);
        try {
            encoder.decode(str, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable e) {
            Throwable th = e;
            String str2 = "unable to decode base64 string: ";
            String valueOf = String.valueOf(th.getMessage());
            throw new DecoderException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), th);
        }
    }
}
