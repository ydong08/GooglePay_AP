package com.google.android.apps.common.inject;

import android.app.ActivityManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideActivityManagerFactory implements Factory<ActivityManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public ActivityManager get() {
        return (ActivityManager) Preconditions.checkNotNull(this.module.provideActivityManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
