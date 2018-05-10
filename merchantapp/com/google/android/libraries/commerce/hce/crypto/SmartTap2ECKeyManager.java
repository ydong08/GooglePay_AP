package com.google.android.libraries.commerce.hce.crypto;

import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.security.payments.tap2pay.CryptoClient;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.inject.Inject;

public class SmartTap2ECKeyManager {
    private final CryptoClientWrapper cryptoClientWrapper;
    private final ECParameterSpec ecParameterSpec = CryptoClient.EC_SPEC;
    private final KeyFactoryWrapper keyFactoryWrapper;

    public static class CryptoClientWrapper {
        public KeyPair generateKeyPair() throws GeneralSecurityException {
            return CryptoClient.generateKeyPair();
        }

        public byte[] compressPoint(ECPoint eCPoint) throws GeneralSecurityException {
            return CryptoClient.compressPoint(eCPoint, CryptoClient.EC_SPEC.getCurve());
        }

        public ECPoint decompressPoint(byte[] bArr, ECParameterSpec eCParameterSpec) throws GeneralSecurityException {
            return CryptoClient.decompressPoint(bArr, eCParameterSpec);
        }
    }

    public static class KeyFactoryWrapper {
        private final KeyFactory keyFactory;

        @Inject
        public KeyFactoryWrapper() {
            Throwable e;
            try {
                this.keyFactory = KeyFactory.getInstance("EC", "AndroidOpenSSL");
            } catch (NoSuchAlgorithmException e2) {
                e = e2;
                throw new IllegalStateException("Required key type not found", e);
            } catch (NoSuchProviderException e3) {
                e = e3;
                throw new IllegalStateException("Required key type not found", e);
            }
        }

        public ECPublicKey generatePublic(KeySpec keySpec) throws InvalidKeySpecException {
            return (ECPublicKey) this.keyFactory.generatePublic(keySpec);
        }

        public ECPrivateKey generatePrivate(KeySpec keySpec) throws InvalidKeySpecException {
            return (ECPrivateKey) this.keyFactory.generatePrivate(keySpec);
        }
    }

    @Inject
    public SmartTap2ECKeyManager(CryptoClientWrapper cryptoClientWrapper, KeyFactoryWrapper keyFactoryWrapper) {
        this.cryptoClientWrapper = cryptoClientWrapper;
        this.keyFactoryWrapper = keyFactoryWrapper;
    }

    public KeyPair generateKeyPair() {
        try {
            return this.cryptoClientWrapper.generateKeyPair();
        } catch (Throwable e) {
            throw new IllegalStateException("Unable to generate an EC key pair", e);
        }
    }

    public Future<KeyPair> generateKeyPairFuture(ExecutorService executorService) {
        return executorService.submit(new Callable<KeyPair>() {
            public KeyPair call() throws Exception {
                return SmartTap2ECKeyManager.this.generateKeyPair();
            }
        });
    }

    public byte[] compressPublicKey(ECPublicKey eCPublicKey) throws ValuablesCryptoException {
        try {
            return this.cryptoClientWrapper.compressPoint(eCPublicKey.getW());
        } catch (Throwable e) {
            throw new ValuablesCryptoException("Unable to encode public key", e);
        }
    }

    public ECPublicKey decodeCompressedPublicKey(byte[] bArr) throws ValuablesCryptoException {
        try {
            return publicKeyFromSpec(new ECPublicKeySpec(this.cryptoClientWrapper.decompressPoint(bArr, this.ecParameterSpec), this.ecParameterSpec));
        } catch (Throwable e) {
            Throwable th = e;
            String str = "Unable to decode public key: ";
            String valueOf = String.valueOf(th.toString());
            throw new ValuablesCryptoException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), th);
        }
    }

    public ECPublicKey decodeX509EncodedPublicKey(byte[] bArr) throws ValuablesCryptoException {
        return publicKeyFromSpec(new X509EncodedKeySpec(bArr));
    }

    public ECPrivateKey decodePKCS8EncodedPrivateKey(byte[] bArr) throws ValuablesCryptoException {
        return privateKeyFromSpec(new PKCS8EncodedKeySpec(bArr));
    }

    public String hexRepresentation(ECPublicKey eCPublicKey) throws ValuablesCryptoException {
        return Hex.encode(compressPublicKey(eCPublicKey));
    }

    private ECPublicKey publicKeyFromSpec(KeySpec keySpec) throws ValuablesCryptoException {
        try {
            return this.keyFactoryWrapper.generatePublic(keySpec);
        } catch (Throwable e) {
            throw new ValuablesCryptoException("Unable to decode public key", e);
        }
    }

    private ECPrivateKey privateKeyFromSpec(KeySpec keySpec) throws ValuablesCryptoException {
        try {
            return this.keyFactoryWrapper.generatePrivate(keySpec);
        } catch (Throwable e) {
            throw new ValuablesCryptoException("Unable to decode private key", e);
        }
    }
}
