package com.google.android.apps.common.inject;

import android.app.KeyguardManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideKeyguardManagerFactory implements Factory<KeyguardManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public KeyguardManager get() {
        return (KeyguardManager) Preconditions.checkNotNull(this.module.provideKeyguardManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
