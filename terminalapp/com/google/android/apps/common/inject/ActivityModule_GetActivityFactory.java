package com.google.android.apps.common.inject;

import android.app.Activity;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class ActivityModule_GetActivityFactory implements Factory<Activity> {
    private final ActivityModule module;

    public Activity get() {
        return (Activity) Preconditions.checkNotNull(this.module.getActivity(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
