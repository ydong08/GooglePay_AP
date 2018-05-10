package dagger.internal;

import dagger.Lazy;
import javax.inject.Provider;

public final class ProviderOfLazy<T> implements Provider<Lazy<T>> {
    private final Provider<T> provider;

    public Lazy<T> get() {
        return DoubleCheck.lazy(this.provider);
    }
}
