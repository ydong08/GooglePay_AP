package com.google.android.apps.common.inject;

import android.accounts.AccountManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemServiceModule_ProvideAccountManagerFactory implements Factory<AccountManager> {
    private final Provider<Context> contextProvider;
    private final SystemServiceModule module;

    public AccountManager get() {
        return (AccountManager) Preconditions.checkNotNull(this.module.provideAccountManager((Context) this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
