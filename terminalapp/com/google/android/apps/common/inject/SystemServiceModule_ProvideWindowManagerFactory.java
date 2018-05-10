package com.google.android.apps.common.inject;

import android.content.Context;
import android.view.WindowManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideWindowManagerFactory implements Factory<WindowManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public WindowManager get() {
        return (WindowManager) Preconditions.checkNotNull(this.module.provideWindowManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
