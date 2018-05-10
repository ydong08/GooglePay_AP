package com.google.android.apps.common.inject;

import android.content.Context;
import android.net.wifi.WifiManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideWifiManagerFactory implements Factory<WifiManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public WifiManager get() {
        return (WifiManager) Preconditions.checkNotNull(this.module.provideWifiManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
