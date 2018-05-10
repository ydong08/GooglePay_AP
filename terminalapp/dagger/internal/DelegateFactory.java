package dagger.internal;

import javax.inject.Provider;

public final class DelegateFactory<T> implements Factory<T> {
    private Provider<T> delegate;

    public T get() {
        if (this.delegate != null) {
            return this.delegate.get();
        }
        throw new IllegalStateException();
    }
}
