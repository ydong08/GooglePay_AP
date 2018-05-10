package com.google.android.apps.common.inject;

import android.content.Context;
import android.service.wallpaper.WallpaperService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideWallpaperServiceFactory implements Factory<WallpaperService> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public WallpaperService get() {
        return (WallpaperService) Preconditions.checkNotNull(this.module.provideWallpaperService((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
