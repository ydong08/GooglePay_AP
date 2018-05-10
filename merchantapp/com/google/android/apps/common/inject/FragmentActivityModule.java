package com.google.android.apps.common.inject;

import android.support.v4.app.FragmentActivity;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class FragmentActivityModule {
    private final FragmentActivity mFragmentActivity;

    @Provides
    public FragmentActivity getFragmentActivity() {
        return this.mFragmentActivity;
    }
}
