package com.google.android.apps.common.inject;

import android.content.Context;
import android.content.pm.PackageManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvidePackageManagerFactory implements Factory<PackageManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public PackageManager get() {
        return (PackageManager) Preconditions.checkNotNull(this.module.providePackageManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
