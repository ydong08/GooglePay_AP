package com.google.android.libraries.commerce.hce.crypto;

import android.app.Application;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager.CryptoClientWrapper;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager.KeyFactoryWrapper;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2SharedKey.Factory;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2SharedKey.KeyAgreementWrapper;
import dagger.Module;
import dagger.Provides;
import java.security.SecureRandom;
import java.util.concurrent.Executors;
import javax.inject.Singleton;

@Module(complete = true, injects = {SmartTap2ConscryptDecryptor.class, SmartTap2ConscryptMerchantSigner.class, SmartTap2SharedKey.class}, library = true)
public class SmartTap2CryptoModule {
    @Provides
    public static SecureRandom getSecureRandom() {
        return new SecureRandom();
    }

    @Provides
    public static SmartTap2ECKeyManager getSmartTap2ECKeyManager() {
        return new SmartTap2ECKeyManager(new CryptoClientWrapper(), new KeyFactoryWrapper());
    }

    @Singleton
    @Provides
    public static SmartTap2ConscryptInstaller getSmartTap2ConscryptInstaller(Application application) {
        return new SmartTap2ConscryptInstaller(application);
    }

    @Provides
    public static SmartTap2Encryptor getSmartTap2Encryptor(SecureRandom secureRandom, SmartTap2HmacGenerator smartTap2HmacGenerator, SmartTap2ECKeyManager smartTap2ECKeyManager, SmartTap2Cipher smartTap2Cipher) {
        return new SmartTap2Encryptor(smartTap2HmacGenerator, smartTap2ECKeyManager, new Factory(new KeyAgreementWrapper()), secureRandom, Executors.newSingleThreadExecutor(), smartTap2Cipher);
    }

    @Provides
    public static SmartTap2MerchantVerifier getSmartTap2MerchantVerifier() {
        return new SmartTap2MerchantVerifier();
    }
}
