package com.google.android.apps.common.inject;

import android.content.Context;
import android.hardware.SensorManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideSensorManagerFactory implements Factory<SensorManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public SensorManager get() {
        return (SensorManager) Preconditions.checkNotNull(this.module.provideSensorManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
