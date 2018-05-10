package dagger.internal;

import dagger.Lazy;
import javax.inject.Provider;

public final class DoubleCheck<T> implements Lazy<T>, Provider<T> {
    private static final Object UNINITIALIZED = new Object();
    private volatile Object instance = UNINITIALIZED;
    private volatile Provider<T> provider;

    private DoubleCheck(Provider<T> provider) {
        this.provider = provider;
    }

    public T get() {
        T t = this.instance;
        if (t == UNINITIALIZED) {
            synchronized (this) {
                t = this.instance;
                if (t == UNINITIALIZED) {
                    t = this.provider.get();
                    T t2 = this.instance;
                    if (t2 == UNINITIALIZED || t2 == t) {
                        this.instance = t;
                        this.provider = null;
                    } else {
                        String valueOf = String.valueOf(t2);
                        String valueOf2 = String.valueOf(t);
                        throw new IllegalStateException(new StringBuilder((String.valueOf(valueOf).length() + 72) + String.valueOf(valueOf2).length()).append("Scoped provider was invoked recursively returning different results: ").append(valueOf).append(" & ").append(valueOf2).toString());
                    }
                }
            }
        }
        return t;
    }

    public static <T> Lazy<T> lazy(Provider<T> provider) {
        if (provider instanceof Lazy) {
            return (Lazy) provider;
        }
        return new DoubleCheck((Provider) Preconditions.checkNotNull(provider));
    }
}
