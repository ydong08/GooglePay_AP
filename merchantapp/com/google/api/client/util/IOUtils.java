package com.google.api.client.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class IOUtils {
    public static byte[] serialize(Object obj) throws IOException {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        serialize(obj, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static void serialize(Object obj, OutputStream outputStream) throws IOException {
        try {
            new ObjectOutputStream(outputStream).writeObject(obj);
        } finally {
            outputStream.close();
        }
    }

    public static <S extends Serializable> S deserialize(byte[] bArr) throws IOException {
        if (bArr == null) {
            return null;
        }
        return deserialize(new ByteArrayInputStream(bArr));
    }

    public static <S extends Serializable> S deserialize(InputStream inputStream) throws IOException {
        try {
            Serializable serializable = (Serializable) new ObjectInputStream(inputStream).readObject();
            inputStream.close();
            return serializable;
        } catch (Throwable e) {
            IOException iOException = new IOException("Failed to deserialize object");
            iOException.initCause(e);
            throw iOException;
        } catch (Throwable th) {
            inputStream.close();
        }
    }
}
