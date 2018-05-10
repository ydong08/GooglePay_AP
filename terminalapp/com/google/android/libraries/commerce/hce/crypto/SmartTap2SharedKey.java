package com.google.android.libraries.commerce.hce.crypto;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

public class SmartTap2SharedKey {
    private byte[] sharedKeyBytes;

    public static class Factory {
        private final KeyAgreementWrapper keyAgreementWrapper;

        @Inject
        public Factory(KeyAgreementWrapper keyAgreementWrapper) {
            this.keyAgreementWrapper = keyAgreementWrapper;
        }

        public Future<SmartTap2SharedKey> futureSharedKey(ExecutorService executorService, ECPublicKey eCPublicKey, ECPrivateKey eCPrivateKey, byte[] bArr, byte[] bArr2) {
            final ECPublicKey eCPublicKey2 = eCPublicKey;
            final ECPrivateKey eCPrivateKey2 = eCPrivateKey;
            final byte[] bArr3 = bArr;
            final byte[] bArr4 = bArr2;
            return executorService.submit(new Callable<SmartTap2SharedKey>() {
                public SmartTap2SharedKey call() throws ValuablesCryptoException {
                    return new SmartTap2SharedKey(Factory.this.keyAgreementWrapper, eCPublicKey2, eCPrivateKey2, bArr3, bArr4);
                }
            });
        }
    }

    public static class KeyAgreementWrapper {
        private final KeyAgreement keyAgreement;

        @Inject
        public KeyAgreementWrapper() {
            Throwable e;
            try {
                this.keyAgreement = KeyAgreement.getInstance("ECDH", "AndroidOpenSSL");
            } catch (NoSuchAlgorithmException e2) {
                e = e2;
                throw new IllegalStateException("Unable to get Conscrypt key agreement provider", e);
            } catch (NoSuchProviderException e3) {
                e = e3;
                throw new IllegalStateException("Unable to get Conscrypt key agreement provider", e);
            }
        }

        public synchronized byte[] generateSecret(ECPrivateKey eCPrivateKey, ECPublicKey eCPublicKey) throws InvalidKeyException {
            this.keyAgreement.init(eCPrivateKey);
            this.keyAgreement.doPhase(eCPublicKey, true);
            return this.keyAgreement.generateSecret();
        }
    }

    private SmartTap2SharedKey(KeyAgreementWrapper keyAgreementWrapper, ECPublicKey eCPublicKey, ECPrivateKey eCPrivateKey, byte[] bArr, byte[] bArr2) throws ValuablesCryptoException {
        try {
            this.sharedKeyBytes = HkdfWithSha256.extractAndExpand(keyAgreementWrapper.generateSecret(eCPrivateKey, eCPublicKey), bArr, bArr2, 48);
        } catch (Throwable e) {
            throw new ValuablesCryptoException("Unable to generate shared key", e);
        }
    }

    public Key getAesEncryptionKey() {
        return new SecretKeySpec(Arrays.copyOfRange(this.sharedKeyBytes, 0, 16), "AES");
    }

    public byte[] getHmacSha256Key() {
        return Arrays.copyOfRange(this.sharedKeyBytes, 16, 48);
    }
}
