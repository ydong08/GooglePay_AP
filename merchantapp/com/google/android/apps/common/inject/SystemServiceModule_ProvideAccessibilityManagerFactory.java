package com.google.android.apps.common.inject;

import android.content.Context;
import android.view.accessibility.AccessibilityManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideAccessibilityManagerFactory implements Factory<AccessibilityManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public AccessibilityManager get() {
        return (AccessibilityManager) Preconditions.checkNotNull(this.module.provideAccessibilityManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
