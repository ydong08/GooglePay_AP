package com.google.android.apps.common.inject;

import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class ActivityModule_GetContextFactory implements Factory<Context> {
    private final ActivityModule module;

    public Context get() {
        return (Context) Preconditions.checkNotNull(this.module.getContext(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
