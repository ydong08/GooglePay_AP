package dagger.internal;

import dagger.releasablereferences.ReleasableReferenceManager;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Queue;

@GwtIncompatible
public final class ReferenceReleasingProviderManager implements ReleasableReferenceManager {
    private final Queue<WeakReference<ReferenceReleasingProvider<?>>> providers;
    private final Class<? extends Annotation> scope;

    enum Operation {
        RELEASE {
            void execute(ReferenceReleasingProvider<?> referenceReleasingProvider) {
                referenceReleasingProvider.releaseStrongReference();
            }
        },
        RESTORE {
            void execute(ReferenceReleasingProvider<?> referenceReleasingProvider) {
                referenceReleasingProvider.restoreStrongReference();
            }
        };

        abstract void execute(ReferenceReleasingProvider<?> referenceReleasingProvider);
    }

    public Class<? extends Annotation> scope() {
        return this.scope;
    }

    public void releaseStrongReferences() {
        execute(Operation.RELEASE);
    }

    public void restoreStrongReferences() {
        execute(Operation.RESTORE);
    }

    private void execute(Operation operation) {
        Iterator it = this.providers.iterator();
        while (it.hasNext()) {
            ReferenceReleasingProvider referenceReleasingProvider = (ReferenceReleasingProvider) ((WeakReference) it.next()).get();
            if (referenceReleasingProvider == null) {
                it.remove();
            } else {
                operation.execute(referenceReleasingProvider);
            }
        }
    }
}
