package com.google.android.apps.common.inject;

import android.app.Application;

public abstract class ComponentApplication<C, CB extends ApplicationComponentBuilder<C>> extends Application implements HasComponent<C> {
    private final ComponentApplicationDelegate<C, CB> delegate = new ComponentApplicationDelegate<C, CB>(this) {
    };
}
