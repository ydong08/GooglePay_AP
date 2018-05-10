package com.google.android.apps.common.inject;

import android.content.ClipboardManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideClipboardManagerFactory implements Factory<ClipboardManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public ClipboardManager get() {
        return (ClipboardManager) Preconditions.checkNotNull(this.module.provideClipboardManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
