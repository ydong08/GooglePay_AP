package com.google.android.apps.common.inject;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideDevicePolicyManagerFactory implements Factory<DevicePolicyManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public DevicePolicyManager get() {
        return (DevicePolicyManager) Preconditions.checkNotNull(this.module.provideDevicePolicyManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
