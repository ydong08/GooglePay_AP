package com.google.android.apps.common.inject;

import android.app.Application;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class ApplicationModule_ProvideApplicationFactory implements Factory<Application> {
    private final ApplicationModule module;

    public Application get() {
        return (Application) Preconditions.checkNotNull(this.module.provideApplication(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
