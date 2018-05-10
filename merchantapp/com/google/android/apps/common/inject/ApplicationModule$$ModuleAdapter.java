package com.google.android.apps.common.inject;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import dagger.internal.BindingsGroup;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import javax.inject.Provider;

public final class ApplicationModule$$ModuleAdapter extends ModuleAdapter<ApplicationModule> {
    private static final Class<?>[] INCLUDES = new Class[0];
    private static final String[] INJECTS = new String[0];
    private static final Class<?>[] STATIC_INJECTIONS = new Class[0];

    /* compiled from: ApplicationModule$$ModuleAdapter */
    public static final class ProvideApplicationProvidesAdapter extends ProvidesBinding<Application> implements Provider<Application> {
        private final ApplicationModule module;

        public ProvideApplicationProvidesAdapter(ApplicationModule applicationModule) {
            super("android.app.Application", false, "com.google.android.apps.common.inject.ApplicationModule", "provideApplication");
            this.module = applicationModule;
            setLibrary(true);
        }

        public Application get() {
            return this.module.provideApplication();
        }
    }

    /* compiled from: ApplicationModule$$ModuleAdapter */
    public static final class ProvideContextProvidesAdapter extends ProvidesBinding<Context> implements Provider<Context> {
        private final ApplicationModule module;

        public ProvideContextProvidesAdapter(ApplicationModule applicationModule) {
            super("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", false, "com.google.android.apps.common.inject.ApplicationModule", "provideContext");
            this.module = applicationModule;
            setLibrary(true);
        }

        public Context get() {
            return this.module.provideContext();
        }
    }

    /* compiled from: ApplicationModule$$ModuleAdapter */
    public static final class ProvideMainLooperProvidesAdapter extends ProvidesBinding<Looper> implements Provider<Looper> {
        private final ApplicationModule module;

        public ProvideMainLooperProvidesAdapter(ApplicationModule applicationModule) {
            super("@com.google.android.apps.common.inject.annotation.MainLooper()/android.os.Looper", false, "com.google.android.apps.common.inject.ApplicationModule", "provideMainLooper");
            this.module = applicationModule;
            setLibrary(true);
        }

        public Looper get() {
            return this.module.provideMainLooper();
        }
    }

    public ApplicationModule$$ModuleAdapter() {
        super(ApplicationModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, true, true);
    }

    public void getBindings(BindingsGroup bindingsGroup, ApplicationModule applicationModule) {
        bindingsGroup.contributeProvidesBinding("android.app.Application", new ProvideApplicationProvidesAdapter(applicationModule));
        bindingsGroup.contributeProvidesBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", new ProvideContextProvidesAdapter(applicationModule));
        bindingsGroup.contributeProvidesBinding("@com.google.android.apps.common.inject.annotation.MainLooper()/android.os.Looper", new ProvideMainLooperProvidesAdapter(applicationModule));
    }
}
