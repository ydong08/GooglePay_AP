package com.google.android.apps.common.inject;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import com.google.android.apps.common.inject.annotation.ApplicationContext;
import com.google.android.apps.common.inject.annotation.MainLooper;
import dagger.Module;
import dagger.Provides;

@Module(library = true)
public final class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    public Application provideApplication() {
        return this.application;
    }

    @ApplicationContext
    @Provides
    public Context provideContext() {
        return this.application;
    }

    @MainLooper
    @Provides
    public Looper provideMainLooper() {
        return Looper.getMainLooper();
    }
}
