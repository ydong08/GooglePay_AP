package com.google.android.apps.common.inject;

import android.content.Context;
import android.os.DropBoxManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideDropBoxManagerFactory implements Factory<DropBoxManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public DropBoxManager get() {
        return (DropBoxManager) Preconditions.checkNotNull(this.module.provideDropBoxManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
