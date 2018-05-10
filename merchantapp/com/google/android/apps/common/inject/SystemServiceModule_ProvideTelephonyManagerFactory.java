package com.google.android.apps.common.inject;

import android.content.Context;
import android.telephony.TelephonyManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideTelephonyManagerFactory implements Factory<TelephonyManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public TelephonyManager get() {
        return (TelephonyManager) Preconditions.checkNotNull(this.module.provideTelephonyManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
