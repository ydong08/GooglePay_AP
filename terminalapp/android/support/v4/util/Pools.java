package android.support.v4.util;

public final class Pools {

    public interface Pool<T> {
        private final Object[] mPool;
        private int mPoolSize;

        Pool(int i) {
            if (i <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            this.mPool = new Object[i];
        }

        T acquire() {
            if (this.mPoolSize <= 0) {
                return null;
            }
            int i = this.mPoolSize - 1;
            T t = this.mPool[i];
            this.mPool[i] = null;
            this.mPoolSize--;
            return t;
        }

        boolean release(T t) {
            if (isInPool(t)) {
                throw new IllegalStateException("Already in the pool!");
            } else if (this.mPoolSize >= this.mPool.length) {
                return false;
            } else {
                this.mPool[this.mPoolSize] = t;
                this.mPoolSize++;
                return true;
            }
        }

        private boolean isInPool(T t) {
            for (int i = 0; i < this.mPoolSize; i++) {
                if (this.mPool[i] == t) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class SynchronizedPool<T> extends Pool {
        private final Object mLock = new Object();

        public SynchronizedPool(int i) {
            super(i);
        }

        public T acquire() {
            T acquire;
            synchronized (this.mLock) {
                acquire = super.acquire();
            }
            return acquire;
        }

        public boolean release(T t) {
            boolean release;
            synchronized (this.mLock) {
                release = super.release(t);
            }
            return release;
        }
    }

    private Pools() {
    }
}
