package com.google.android.apps.common.inject;

import android.support.v4.app.FragmentActivity;
import dagger.internal.BindingsGroup;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import javax.inject.Provider;

public final class FragmentActivityModule$$ModuleAdapter extends ModuleAdapter<FragmentActivityModule> {
    private static final Class<?>[] INCLUDES = new Class[0];
    private static final String[] INJECTS = new String[0];
    private static final Class<?>[] STATIC_INJECTIONS = new Class[0];

    /* compiled from: FragmentActivityModule$$ModuleAdapter */
    public static final class GetFragmentActivityProvidesAdapter extends ProvidesBinding<FragmentActivity> implements Provider<FragmentActivity> {
        private final FragmentActivityModule module;

        public GetFragmentActivityProvidesAdapter(FragmentActivityModule fragmentActivityModule) {
            super("android.support.v4.app.FragmentActivity", false, "com.google.android.apps.common.inject.FragmentActivityModule", "getFragmentActivity");
            this.module = fragmentActivityModule;
            setLibrary(true);
        }

        public FragmentActivity get() {
            return this.module.getFragmentActivity();
        }
    }

    public FragmentActivityModule$$ModuleAdapter() {
        super(FragmentActivityModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, false, true);
    }

    public void getBindings(BindingsGroup bindingsGroup, FragmentActivityModule fragmentActivityModule) {
        bindingsGroup.contributeProvidesBinding("android.support.v4.app.FragmentActivity", new GetFragmentActivityProvidesAdapter(fragmentActivityModule));
    }
}
