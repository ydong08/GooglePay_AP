package com.google.android.apps.common.inject;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class ApplicationModule_ProvideMainLooperFactory implements Factory<Looper> {
    private final ApplicationModule module;

    public Looper get() {
        return (Looper) Preconditions.checkNotNull(this.module.provideMainLooper(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
