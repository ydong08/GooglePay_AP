package com.google.android.libraries.commerce.hce.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.inject.Inject;

public class SmartTap2Cipher {
    private final Cipher cipher;

    @Inject
    public SmartTap2Cipher() {
        try {
            this.cipher = Cipher.getInstance("AES/CTR/NOPADDING", "AndroidOpenSSL");
        } catch (Throwable e) {
            throw new IllegalStateException("Unable to initialize crypto", e);
        }
    }

    public void init(int i, Key key, IvParameterSpec ivParameterSpec) throws InvalidAlgorithmParameterException, InvalidKeyException {
        this.cipher.init(i, key, ivParameterSpec);
    }

    public byte[] doFinal(byte[] bArr) throws BadPaddingException, IllegalBlockSizeException {
        return this.cipher.doFinal(bArr);
    }
}
