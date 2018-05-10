package com.google.android.libraries.commerce.hce.crypto;

import android.app.Application;
import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.Linker;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import java.security.SecureRandom;
import java.util.Set;
import javax.inject.Provider;

public final class SmartTap2CryptoModule$$ModuleAdapter extends ModuleAdapter<SmartTap2CryptoModule> {
    private static final Class<?>[] INCLUDES = new Class[0];
    private static final String[] INJECTS = new String[]{"members/com.google.android.libraries.commerce.hce.crypto.SmartTap2ConscryptDecryptor", "members/com.google.android.libraries.commerce.hce.crypto.SmartTap2ConscryptMerchantSigner", "members/com.google.android.libraries.commerce.hce.crypto.SmartTap2SharedKey"};
    private static final Class<?>[] STATIC_INJECTIONS = new Class[0];

    /* compiled from: SmartTap2CryptoModule$$ModuleAdapter */
    public static final class GetSecureRandomProvidesAdapter extends ProvidesBinding<SecureRandom> implements Provider<SecureRandom> {
        private final SmartTap2CryptoModule module;

        public GetSecureRandomProvidesAdapter(SmartTap2CryptoModule smartTap2CryptoModule) {
            super("java.security.SecureRandom", false, "com.google.android.libraries.commerce.hce.crypto.SmartTap2CryptoModule", "getSecureRandom");
            this.module = smartTap2CryptoModule;
            setLibrary(true);
        }

        public SecureRandom get() {
            return SmartTap2CryptoModule.getSecureRandom();
        }
    }

    /* compiled from: SmartTap2CryptoModule$$ModuleAdapter */
    public static final class GetSmartTap2ConscryptInstallerProvidesAdapter extends ProvidesBinding<SmartTap2ConscryptInstaller> implements Provider<SmartTap2ConscryptInstaller> {
        private Binding<Application> application;
        private final SmartTap2CryptoModule module;

        public GetSmartTap2ConscryptInstallerProvidesAdapter(SmartTap2CryptoModule smartTap2CryptoModule) {
            super("com.google.android.libraries.commerce.hce.crypto.SmartTap2ConscryptInstaller", true, "com.google.android.libraries.commerce.hce.crypto.SmartTap2CryptoModule", "getSmartTap2ConscryptInstaller");
            this.module = smartTap2CryptoModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.application = linker.requestBinding("android.app.Application", SmartTap2CryptoModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.application);
        }

        public SmartTap2ConscryptInstaller get() {
            return SmartTap2CryptoModule.getSmartTap2ConscryptInstaller((Application) this.application.get());
        }
    }

    /* compiled from: SmartTap2CryptoModule$$ModuleAdapter */
    public static final class GetSmartTap2ECKeyManagerProvidesAdapter extends ProvidesBinding<SmartTap2ECKeyManager> implements Provider<SmartTap2ECKeyManager> {
        private final SmartTap2CryptoModule module;

        public GetSmartTap2ECKeyManagerProvidesAdapter(SmartTap2CryptoModule smartTap2CryptoModule) {
            super("com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager", false, "com.google.android.libraries.commerce.hce.crypto.SmartTap2CryptoModule", "getSmartTap2ECKeyManager");
            this.module = smartTap2CryptoModule;
            setLibrary(true);
        }

        public SmartTap2ECKeyManager get() {
            return SmartTap2CryptoModule.getSmartTap2ECKeyManager();
        }
    }

    /* compiled from: SmartTap2CryptoModule$$ModuleAdapter */
    public static final class GetSmartTap2EncryptorProvidesAdapter extends ProvidesBinding<SmartTap2Encryptor> implements Provider<SmartTap2Encryptor> {
        private Binding<SmartTap2Cipher> cipher;
        private Binding<SmartTap2HmacGenerator> hmacGenerator;
        private Binding<SmartTap2ECKeyManager> keyManager;
        private final SmartTap2CryptoModule module;
        private Binding<SecureRandom> random;

        public GetSmartTap2EncryptorProvidesAdapter(SmartTap2CryptoModule smartTap2CryptoModule) {
            super("com.google.android.libraries.commerce.hce.crypto.SmartTap2Encryptor", false, "com.google.android.libraries.commerce.hce.crypto.SmartTap2CryptoModule", "getSmartTap2Encryptor");
            this.module = smartTap2CryptoModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.random = linker.requestBinding("java.security.SecureRandom", SmartTap2CryptoModule.class, getClass().getClassLoader());
            this.hmacGenerator = linker.requestBinding("com.google.android.libraries.commerce.hce.crypto.SmartTap2HmacGenerator", SmartTap2CryptoModule.class, getClass().getClassLoader());
            this.keyManager = linker.requestBinding("com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager", SmartTap2CryptoModule.class, getClass().getClassLoader());
            this.cipher = linker.requestBinding("com.google.android.libraries.commerce.hce.crypto.SmartTap2Cipher", SmartTap2CryptoModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.random);
            set.add(this.hmacGenerator);
            set.add(this.keyManager);
            set.add(this.cipher);
        }

        public SmartTap2Encryptor get() {
            return SmartTap2CryptoModule.getSmartTap2Encryptor((SecureRandom) this.random.get(), (SmartTap2HmacGenerator) this.hmacGenerator.get(), (SmartTap2ECKeyManager) this.keyManager.get(), (SmartTap2Cipher) this.cipher.get());
        }
    }

    /* compiled from: SmartTap2CryptoModule$$ModuleAdapter */
    public static final class GetSmartTap2MerchantVerifierProvidesAdapter extends ProvidesBinding<SmartTap2MerchantVerifier> implements Provider<SmartTap2MerchantVerifier> {
        private final SmartTap2CryptoModule module;

        public GetSmartTap2MerchantVerifierProvidesAdapter(SmartTap2CryptoModule smartTap2CryptoModule) {
            super("com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier", false, "com.google.android.libraries.commerce.hce.crypto.SmartTap2CryptoModule", "getSmartTap2MerchantVerifier");
            this.module = smartTap2CryptoModule;
            setLibrary(true);
        }

        public SmartTap2MerchantVerifier get() {
            return SmartTap2CryptoModule.getSmartTap2MerchantVerifier();
        }
    }

    public SmartTap2CryptoModule$$ModuleAdapter() {
        super(SmartTap2CryptoModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, true, true);
    }

    public SmartTap2CryptoModule newModule() {
        return new SmartTap2CryptoModule();
    }

    public void getBindings(BindingsGroup bindingsGroup, SmartTap2CryptoModule smartTap2CryptoModule) {
        bindingsGroup.contributeProvidesBinding("java.security.SecureRandom", new GetSecureRandomProvidesAdapter(smartTap2CryptoModule));
        bindingsGroup.contributeProvidesBinding("com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager", new GetSmartTap2ECKeyManagerProvidesAdapter(smartTap2CryptoModule));
        bindingsGroup.contributeProvidesBinding("com.google.android.libraries.commerce.hce.crypto.SmartTap2ConscryptInstaller", new GetSmartTap2ConscryptInstallerProvidesAdapter(smartTap2CryptoModule));
        bindingsGroup.contributeProvidesBinding("com.google.android.libraries.commerce.hce.crypto.SmartTap2Encryptor", new GetSmartTap2EncryptorProvidesAdapter(smartTap2CryptoModule));
        bindingsGroup.contributeProvidesBinding("com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier", new GetSmartTap2MerchantVerifierProvidesAdapter(smartTap2CryptoModule));
    }
}
