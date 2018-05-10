package com.google.android.apps.common.inject;

import android.app.SearchManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideSearchManagerFactory implements Factory<SearchManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public SearchManager get() {
        return (SearchManager) Preconditions.checkNotNull(this.module.provideSearchManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
