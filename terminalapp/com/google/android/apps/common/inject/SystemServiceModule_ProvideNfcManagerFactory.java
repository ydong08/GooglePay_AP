package com.google.android.apps.common.inject;

import android.content.Context;
import android.nfc.NfcManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideNfcManagerFactory implements Factory<NfcManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public NfcManager get() {
        return (NfcManager) Preconditions.checkNotNull(this.module.provideNfcManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
