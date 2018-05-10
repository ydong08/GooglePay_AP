package com.google.android.apps.common.inject;

import android.app.NotificationManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideNotificationManagerFactory implements Factory<NotificationManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public NotificationManager get() {
        return (NotificationManager) Preconditions.checkNotNull(this.module.provideNotificationManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
