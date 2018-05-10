package com.google.android.apps.common.inject;

import android.support.v4.app.FragmentActivity;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FragmentActivityModule_GetFragmentActivityFactory implements Factory<FragmentActivity> {
    private final FragmentActivityModule module;

    public FragmentActivity get() {
        return (FragmentActivity) Preconditions.checkNotNull(this.module.getFragmentActivity(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
