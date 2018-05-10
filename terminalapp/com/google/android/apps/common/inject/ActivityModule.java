package com.google.android.apps.common.inject;

import android.app.Activity;
import android.content.Context;
import com.google.android.apps.common.inject.annotation.ActivityContext;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class ActivityModule {
    private final Activity mActivity;

    @ActivityContext
    @Provides
    public Context getContext() {
        return this.mActivity;
    }

    @Provides
    public Activity getActivity() {
        return this.mActivity;
    }
}
