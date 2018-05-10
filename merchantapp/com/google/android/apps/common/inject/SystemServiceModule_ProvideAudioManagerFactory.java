package com.google.android.apps.common.inject;

import android.content.Context;
import android.media.AudioManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideAudioManagerFactory implements Factory<AudioManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public AudioManager get() {
        return (AudioManager) Preconditions.checkNotNull(this.module.provideAudioManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
