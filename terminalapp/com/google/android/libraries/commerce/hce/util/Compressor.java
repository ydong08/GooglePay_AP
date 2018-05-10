package com.google.android.libraries.commerce.hce.util;

import java.util.zip.Deflater;
import javax.inject.Inject;

public class Compressor {
    private static int BUFFER_SIZE = 255;
    private final Deflater deflater = new Deflater();

    @Inject
    public Compressor() {
        this.deflater.setLevel(9);
    }
}
