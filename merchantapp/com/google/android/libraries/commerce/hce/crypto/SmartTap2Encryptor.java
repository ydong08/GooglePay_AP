package com.google.android.libraries.commerce.hce.crypto;

import com.google.android.libraries.commerce.hce.crypto.SmartTap2SharedKey.Factory;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.inject.Inject;

public class SmartTap2Encryptor {
    private final SmartTap2Cipher cipher;
    private final ExecutorService executorService;
    private final SmartTap2HmacGenerator hmacGenerator;
    private final SmartTap2ECKeyManager keyManager;
    private Future<KeyPair> keyPairFuture;
    private int macLength;
    private final SecureRandom random;
    private final Factory sharedKeyFactory;
    private Future<SmartTap2SharedKey> sharedKeyFuture;

    @Inject
    public SmartTap2Encryptor(SmartTap2HmacGenerator smartTap2HmacGenerator, SmartTap2ECKeyManager smartTap2ECKeyManager, Factory factory, SecureRandom secureRandom, ExecutorService executorService, SmartTap2Cipher smartTap2Cipher) {
        this.hmacGenerator = smartTap2HmacGenerator;
        this.sharedKeyFactory = factory;
        this.random = secureRandom;
        this.keyManager = smartTap2ECKeyManager;
        this.executorService = executorService;
        this.cipher = smartTap2Cipher;
        this.keyPairFuture = smartTap2ECKeyManager.generateKeyPairFuture(executorService);
    }

    public void setCryptoParams(Version0EncryptionParameters version0EncryptionParameters) throws ValuablesCryptoException {
        this.sharedKeyFuture = this.sharedKeyFactory.futureSharedKey(this.executorService, this.keyManager.decodeCompressedPublicKey(version0EncryptionParameters.getPeerKeyBytes()), getPrivateKey(), this.keyManager.compressPublicKey(getPublicKey()), version0EncryptionParameters.getInfoBytes());
        this.macLength = version0EncryptionParameters.getMacLength();
    }

    public byte[] encryptMessage(byte[] bArr) throws ValuablesCryptoException {
        Throwable e;
        Preconditions.checkState(isInitialized(), "Must call setCryptoParams before calling encryptMessage");
        try {
            SmartTap2SharedKey smartTap2SharedKey = (SmartTap2SharedKey) this.sharedKeyFuture.get(2000, TimeUnit.MILLISECONDS);
            Key aesEncryptionKey = smartTap2SharedKey.getAesEncryptionKey();
            byte[] hmacSha256Key = smartTap2SharedKey.getHmacSha256Key();
            SmartTap2InitializationVector smartTap2InitializationVector = new SmartTap2InitializationVector(this.random);
            try {
                this.cipher.init(1, aesEncryptionKey, smartTap2InitializationVector.getIvParameterSpec());
                hmacSha256Key = this.hmacGenerator.generateHmac(hmacSha256Key, smartTap2InitializationVector, this.cipher.doFinal(bArr), this.macLength);
                return Bytes.concat(smartTap2InitializationVector.getBytes(), r1, hmacSha256Key);
            } catch (InvalidKeyException e2) {
                e = e2;
                throw new ValuablesCryptoException("AES encryption failed", e);
            } catch (InvalidAlgorithmParameterException e3) {
                e = e3;
                throw new ValuablesCryptoException("AES encryption failed", e);
            } catch (IllegalBlockSizeException e4) {
                e = e4;
                throw new ValuablesCryptoException("AES encryption failed", e);
            } catch (BadPaddingException e5) {
                e = e5;
                throw new ValuablesCryptoException("AES encryption failed", e);
            }
        } catch (InterruptedException e6) {
            e = e6;
            throw new ValuablesCryptoException("Unable to get shared key", e);
        } catch (ExecutionException e7) {
            e = e7;
            throw new ValuablesCryptoException("Unable to get shared key", e);
        } catch (TimeoutException e8) {
            e = e8;
            throw new ValuablesCryptoException("Unable to get shared key", e);
        }
    }

    public byte[] getEphemeralPublicKey() {
        try {
            return this.keyManager.compressPublicKey(getPublicKey());
        } catch (Throwable e) {
            throw new IllegalStateException("Can't compress ephemeral public key", e);
        }
    }

    public boolean isInitialized() {
        return this.sharedKeyFuture != null;
    }

    public void reset() {
        this.keyPairFuture.cancel(true);
        if (this.sharedKeyFuture != null) {
            this.sharedKeyFuture.cancel(true);
            this.sharedKeyFuture = null;
        }
        this.keyPairFuture = this.keyManager.generateKeyPairFuture(this.executorService);
    }

    private ECPublicKey getPublicKey() {
        Throwable e;
        try {
            return (ECPublicKey) ((KeyPair) this.keyPairFuture.get()).getPublic();
        } catch (InterruptedException e2) {
            e = e2;
            throw new IllegalStateException("Unable to generate ephemeral key pair", e);
        } catch (ExecutionException e3) {
            e = e3;
            throw new IllegalStateException("Unable to generate ephemeral key pair", e);
        }
    }

    private ECPrivateKey getPrivateKey() {
        Throwable e;
        try {
            return (ECPrivateKey) ((KeyPair) this.keyPairFuture.get()).getPrivate();
        } catch (InterruptedException e2) {
            e = e2;
            throw new IllegalStateException("Unable to generate ephemeral key pair", e);
        } catch (ExecutionException e3) {
            e = e3;
            throw new IllegalStateException("Unable to generate ephemeral key pair", e);
        }
    }

    public void setCryptoParams(Version1EncryptionParameters version1EncryptionParameters) throws ValuablesCryptoException {
        this.sharedKeyFuture = this.sharedKeyFactory.futureSharedKey(this.executorService, this.keyManager.decodeCompressedPublicKey(version1EncryptionParameters.getPeerKeyBytes()), getPrivateKey(), this.keyManager.compressPublicKey(getPublicKey()), version1EncryptionParameters.getInfoBytes());
        this.macLength = version1EncryptionParameters.getMacLength();
    }
}
