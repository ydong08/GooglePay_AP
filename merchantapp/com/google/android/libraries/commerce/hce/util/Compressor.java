package com.google.android.libraries.commerce.hce.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import javax.inject.Inject;

public class Compressor {
    private static int BUFFER_SIZE = 255;
    private final Deflater deflater = new Deflater();

    @Inject
    public Compressor() {
        this.deflater.setLevel(9);
    }

    public byte[] compress(byte[] bArr) {
        byte[] bArr2 = new byte[BUFFER_SIZE];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.deflater.setInput(bArr);
        this.deflater.finish();
        while (!this.deflater.finished()) {
            byteArrayOutputStream.write(bArr2, 0, this.deflater.deflate(bArr2));
        }
        this.deflater.reset();
        return byteArrayOutputStream.toByteArray();
    }
}
