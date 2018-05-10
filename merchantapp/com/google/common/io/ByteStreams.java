package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ByteStreams {
    private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream() {
        public void write(int i) {
        }

        public void write(byte[] bArr) {
            Preconditions.checkNotNull(bArr);
        }

        public void write(byte[] bArr, int i, int i2) {
            Preconditions.checkNotNull(bArr);
        }

        public String toString() {
            return "ByteStreams.nullOutputStream()";
        }
    };

    static byte[] createBuffer() {
        return new byte[8192];
    }

    private ByteStreams() {
    }

    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(outputStream);
        byte[] createBuffer = createBuffer();
        long j = 0;
        while (true) {
            int read = inputStream.read(createBuffer);
            if (read == -1) {
                return j;
            }
            outputStream.write(createBuffer, 0, read);
            j += (long) read;
        }
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream(Math.max(32, inputStream.available()));
        copy(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
