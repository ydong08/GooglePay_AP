package com.google.android.apps.common.inject;

import android.content.Context;
import android.os.PowerManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvidePowerManagerFactory implements Factory<PowerManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public PowerManager get() {
        return (PowerManager) Preconditions.checkNotNull(this.module.providePowerManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
