package com.google.android.apps.common.inject;

import android.content.Context;
import android.net.ConnectivityManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideConnectivityManagerFactory implements Factory<ConnectivityManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public ConnectivityManager get() {
        return (ConnectivityManager) Preconditions.checkNotNull(this.module.provideConnectivityManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
