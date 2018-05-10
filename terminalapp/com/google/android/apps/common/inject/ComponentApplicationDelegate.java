package com.google.android.apps.common.inject;

import android.app.Application;

public abstract class ComponentApplicationDelegate<C, CB extends ApplicationComponentBuilder<C>> implements HasComponent<C> {
    private final Application application;

    public ComponentApplicationDelegate(Application application) {
        this.application = application;
    }
}
