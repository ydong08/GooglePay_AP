package dagger.internal;

import java.lang.ref.WeakReference;
import javax.inject.Provider;

@GwtIncompatible
public final class ReferenceReleasingProvider<T> implements Provider<T> {
    private static final Object NULL = new Object();
    private final Provider<T> provider;
    private volatile Object strongReference;
    private volatile WeakReference<T> weakReference;

    public void releaseStrongReference() {
        Object obj = this.strongReference;
        if (obj != null && obj != NULL) {
            synchronized (this) {
                this.weakReference = new WeakReference(obj);
                this.strongReference = null;
            }
        }
    }

    public void restoreStrongReference() {
        Object obj = this.strongReference;
        if (this.weakReference != null && obj == null) {
            synchronized (this) {
                obj = this.strongReference;
                if (this.weakReference != null && obj == null) {
                    obj = this.weakReference.get();
                    if (obj != null) {
                        this.strongReference = obj;
                        this.weakReference = null;
                    }
                }
            }
        }
    }

    public T get() {
        T currentValue = currentValue();
        if (currentValue == null) {
            synchronized (this) {
                currentValue = currentValue();
                if (currentValue == null) {
                    currentValue = this.provider.get();
                    if (currentValue == null) {
                        currentValue = NULL;
                    }
                    this.strongReference = currentValue;
                }
            }
        }
        return currentValue == NULL ? null : currentValue;
    }

    private Object currentValue() {
        Object obj = this.strongReference;
        if (obj != null) {
            return obj;
        }
        if (this.weakReference != null) {
            return this.weakReference.get();
        }
        return null;
    }
}
