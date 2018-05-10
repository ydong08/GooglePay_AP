package com.google.android.apps.common.inject;

import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class ApplicationModule_ProvideContextFactory implements Factory<Context> {
    private final ApplicationModule module;

    public Context get() {
        return (Context) Preconditions.checkNotNull(this.module.provideContext(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
