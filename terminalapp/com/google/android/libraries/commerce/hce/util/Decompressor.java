package com.google.android.libraries.commerce.hce.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class Decompressor {
    private final Inflater inflater = new Inflater();

    public byte[] decompress(byte[] bArr) throws DataFormatException {
        byte[] bArr2 = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.inflater.setInput(bArr);
        while (!this.inflater.finished()) {
            byteArrayOutputStream.write(bArr2, 0, this.inflater.inflate(bArr2));
        }
        this.inflater.reset();
        return byteArrayOutputStream.toByteArray();
    }
}
