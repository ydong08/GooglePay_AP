package com.google.android.libraries.commerce.hce.crypto;

import com.google.android.libraries.commerce.hce.crypto.SmartTap2SharedKey.Factory;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Futures;
import java.security.Key;
import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.inject.Inject;

public class SmartTap2Decryptor {
    private final SmartTap2Cipher cipher;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final SmartTap2HmacGenerator hmacGenerator;
    private final SmartTap2ECKeyManager keyManager;
    private Future<KeyPair> keyPairFuture;
    private int macLength;
    private final Factory sharedKeyFactory;
    private Future<SmartTap2SharedKey> sharedKeyFuture;

    @Inject
    public SmartTap2Decryptor(SmartTap2HmacGenerator smartTap2HmacGenerator, SmartTap2ECKeyManager smartTap2ECKeyManager, Factory factory, SmartTap2Cipher smartTap2Cipher) {
        this.hmacGenerator = smartTap2HmacGenerator;
        this.sharedKeyFactory = factory;
        this.keyManager = smartTap2ECKeyManager;
        this.cipher = smartTap2Cipher;
        this.keyPairFuture = smartTap2ECKeyManager.generateKeyPairFuture(this.executorService);
    }

    public byte[] getEphemeralPublicKeyBytes() {
        try {
            return this.keyManager.compressPublicKey(getEphemeralPublicKey());
        } catch (Throwable e) {
            throw new IllegalStateException("Invalid ephemeral key!", e);
        }
    }

    public void setCryptoParams(Version0EncryptionParameters version0EncryptionParameters) throws ValuablesCryptoException {
        this.sharedKeyFuture = this.sharedKeyFactory.futureSharedKey(this.executorService, this.keyManager.decodeCompressedPublicKey(version0EncryptionParameters.getPeerKeyBytes()), getEphemeralPrivateKey(), version0EncryptionParameters.getPeerKeyBytes(), version0EncryptionParameters.getInfoBytes());
        this.macLength = version0EncryptionParameters.getMacLength();
    }

    public byte[] decryptMessage(byte[] bArr) throws ValuablesCryptoException {
        Throwable e;
        Preconditions.checkState(isInitialized(), "Must call setCryptoParams before calling encryptMessage");
        SmartTap2InitializationVector smartTap2InitializationVector = new SmartTap2InitializationVector(Arrays.copyOfRange(bArr, 0, 12));
        int length = (bArr.length - 12) - this.macLength;
        byte[] copyOfRange = Arrays.copyOfRange(bArr, 12, length + 12);
        length += 12;
        byte[] copyOfRange2 = Arrays.copyOfRange(bArr, length, this.macLength + length);
        try {
            SmartTap2SharedKey smartTap2SharedKey = (SmartTap2SharedKey) this.sharedKeyFuture.get(2000, TimeUnit.MILLISECONDS);
            Key aesEncryptionKey = smartTap2SharedKey.getAesEncryptionKey();
            byte[] hmacSha256Key = smartTap2SharedKey.getHmacSha256Key();
            try {
                this.cipher.init(2, aesEncryptionKey, smartTap2InitializationVector.getIvParameterSpec());
                byte[] doFinal = this.cipher.doFinal(copyOfRange);
                if (doFinal == null) {
                    doFinal = new byte[0];
                }
                if (Arrays.equals(copyOfRange2, this.hmacGenerator.generateHmac(hmacSha256Key, smartTap2InitializationVector, copyOfRange, this.macLength))) {
                    return doFinal;
                }
                throw new ValuablesCryptoException("HMAC not verified!");
            } catch (Throwable e2) {
                throw new ValuablesCryptoException("Unable to decrypt", e2);
            }
        } catch (InterruptedException e3) {
            e2 = e3;
            throw new ValuablesCryptoException("Unable to get shared key", e2);
        } catch (ExecutionException e4) {
            e2 = e4;
            throw new ValuablesCryptoException("Unable to get shared key", e2);
        } catch (TimeoutException e5) {
            e2 = e5;
            throw new ValuablesCryptoException("Unable to get shared key", e2);
        }
    }

    public boolean isInitialized() {
        return this.sharedKeyFuture != null;
    }

    private ECPublicKey getEphemeralPublicKey() {
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

    private ECPrivateKey getEphemeralPrivateKey() {
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

    public SmartTap2Decryptor(SmartTap2HmacGenerator smartTap2HmacGenerator, SmartTap2ECKeyManager smartTap2ECKeyManager, Factory factory, KeyPair keyPair, SmartTap2Cipher smartTap2Cipher) {
        this.hmacGenerator = smartTap2HmacGenerator;
        this.sharedKeyFactory = factory;
        this.keyManager = smartTap2ECKeyManager;
        this.cipher = smartTap2Cipher;
        this.keyPairFuture = Futures.immediateFuture(keyPair);
    }

    public void setCryptoParams(Version1EncryptionParameters version1EncryptionParameters) throws ValuablesCryptoException {
        this.sharedKeyFuture = this.sharedKeyFactory.futureSharedKey(this.executorService, this.keyManager.decodeCompressedPublicKey(version1EncryptionParameters.getPeerKeyBytes()), getEphemeralPrivateKey(), version1EncryptionParameters.getPeerKeyBytes(), version1EncryptionParameters.getInfoBytes());
        this.macLength = version1EncryptionParameters.getMacLength();
    }
}
