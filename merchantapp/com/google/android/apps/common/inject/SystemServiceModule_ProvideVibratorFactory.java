package com.google.android.apps.common.inject;

import android.content.Context;
import android.os.Vibrator;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideVibratorFactory implements Factory<Vibrator> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public Vibrator get() {
        return (Vibrator) Preconditions.checkNotNull(this.module.provideVibrator((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
