package com.google.android.apps.common.inject;

import android.app.Activity;
import android.content.Context;
import dagger.internal.BindingsGroup;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import javax.inject.Provider;

public final class ActivityModule$$ModuleAdapter extends ModuleAdapter<ActivityModule> {
    private static final Class<?>[] INCLUDES = new Class[0];
    private static final String[] INJECTS = new String[0];
    private static final Class<?>[] STATIC_INJECTIONS = new Class[0];

    /* compiled from: ActivityModule$$ModuleAdapter */
    public static final class GetActivityProvidesAdapter extends ProvidesBinding<Activity> implements Provider<Activity> {
        private final ActivityModule module;

        public GetActivityProvidesAdapter(ActivityModule activityModule) {
            super("android.app.Activity", false, "com.google.android.apps.common.inject.ActivityModule", "getActivity");
            this.module = activityModule;
            setLibrary(true);
        }

        public Activity get() {
            return this.module.getActivity();
        }
    }

    /* compiled from: ActivityModule$$ModuleAdapter */
    public static final class GetContextProvidesAdapter extends ProvidesBinding<Context> implements Provider<Context> {
        private final ActivityModule module;

        public GetContextProvidesAdapter(ActivityModule activityModule) {
            super("@com.google.android.apps.common.inject.annotation.ActivityContext()/android.content.Context", false, "com.google.android.apps.common.inject.ActivityModule", "getContext");
            this.module = activityModule;
            setLibrary(true);
        }

        public Context get() {
            return this.module.getContext();
        }
    }

    public ActivityModule$$ModuleAdapter() {
        super(ActivityModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, false, true);
    }

    public void getBindings(BindingsGroup bindingsGroup, ActivityModule activityModule) {
        bindingsGroup.contributeProvidesBinding("@com.google.android.apps.common.inject.annotation.ActivityContext()/android.content.Context", new GetContextProvidesAdapter(activityModule));
        bindingsGroup.contributeProvidesBinding("android.app.Activity", new GetActivityProvidesAdapter(activityModule));
    }
}
