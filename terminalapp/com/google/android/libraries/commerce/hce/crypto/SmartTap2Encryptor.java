package com.google.android.libraries.commerce.hce.crypto;

import com.google.android.libraries.commerce.hce.crypto.SmartTap2SharedKey.Factory;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.inject.Inject;

public class SmartTap2Encryptor {
    private final SmartTap2Cipher cipher;
    private final ExecutorService executorService;
    private final SmartTap2HmacGenerator hmacGenerator;
    private final SmartTap2ECKeyManager keyManager;
    private Future<KeyPair> keyPairFuture;
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

    public void reset() {
        this.keyPairFuture.cancel(true);
        if (this.sharedKeyFuture != null) {
            this.sharedKeyFuture.cancel(true);
            this.sharedKeyFuture = null;
        }
        this.keyPairFuture = this.keyManager.generateKeyPairFuture(this.executorService);
    }
}
