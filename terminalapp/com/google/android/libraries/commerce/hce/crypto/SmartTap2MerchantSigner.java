package com.google.android.libraries.commerce.hce.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import javax.inject.Inject;

public class SmartTap2MerchantSigner {
    private final SmartTap2ECKeyManager keyManager;
    private final Signature signature;

    @Inject
    public SmartTap2MerchantSigner(SmartTap2ECKeyManager smartTap2ECKeyManager) {
        Throwable e;
        String str;
        String valueOf;
        this.keyManager = smartTap2ECKeyManager;
        try {
            this.signature = Signature.getInstance("SHA256withECDSA", "AndroidOpenSSL");
        } catch (NoSuchAlgorithmException e2) {
            e = e2;
            str = "Unable to initialize crypto";
            valueOf = String.valueOf(e.toString());
            throw new IllegalStateException(valueOf.length() == 0 ? str.concat(valueOf) : new String(str), e);
        } catch (NoSuchProviderException e3) {
            e = e3;
            str = "Unable to initialize crypto";
            valueOf = String.valueOf(e.toString());
            if (valueOf.length() == 0) {
            }
            throw new IllegalStateException(valueOf.length() == 0 ? str.concat(valueOf) : new String(str), e);
        }
    }

    public byte[] generateSignature(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5) throws ValuablesCryptoException {
        Throwable e;
        try {
            this.signature.initSign(this.keyManager.decodePrivateKey(bArr));
            this.signature.update(bArr2);
            this.signature.update(bArr3);
            this.signature.update(bArr4);
            this.signature.update(bArr5);
            return this.signature.sign();
        } catch (InvalidKeyException e2) {
            e = e2;
            throw new ValuablesCryptoException("Unable to sign message", e);
        } catch (SignatureException e3) {
            e = e3;
            throw new ValuablesCryptoException("Unable to sign message", e);
        }
    }
}
