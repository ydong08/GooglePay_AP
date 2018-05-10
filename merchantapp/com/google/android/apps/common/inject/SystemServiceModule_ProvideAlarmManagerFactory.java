package com.google.android.apps.common.inject;

import android.app.AlarmManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideAlarmManagerFactory implements Factory<AlarmManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public AlarmManager get() {
        return (AlarmManager) Preconditions.checkNotNull(this.module.provideAlarmManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
