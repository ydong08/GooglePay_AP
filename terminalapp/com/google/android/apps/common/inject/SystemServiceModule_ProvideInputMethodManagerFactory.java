package com.google.android.apps.common.inject;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideInputMethodManagerFactory implements Factory<InputMethodManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public InputMethodManager get() {
        return (InputMethodManager) Preconditions.checkNotNull(this.module.provideInputMethodManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
