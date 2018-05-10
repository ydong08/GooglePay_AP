package dagger.internal;

import dagger.Lazy;
import javax.inject.Provider;

public final class SingleCheck<T> implements Lazy<T>, Provider<T> {
    private static final Object UNINITIALIZED = new Object();
    private volatile Object instance;
    private volatile Provider<T> provider;

    public T get() {
        Provider provider = this.provider;
        if (this.instance == UNINITIALIZED) {
            this.instance = provider.get();
            this.provider = null;
        }
        return this.instance;
    }
}
