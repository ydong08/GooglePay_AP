package com.google.android.apps.common.inject;

import android.content.Context;
import android.location.LocationManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideLocationManagerFactory implements Factory<LocationManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public LocationManager get() {
        return (LocationManager) Preconditions.checkNotNull(this.module.provideLocationManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
