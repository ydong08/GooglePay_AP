package dagger.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

abstract class Memoizer<K, V> {
    private final Map<K, V> map = new HashMap();
    private final Lock readLock;
    private final Lock writeLock;

    abstract V create(K k);

    Memoizer() {
        ReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.readLock = reentrantReadWriteLock.readLock();
        this.writeLock = reentrantReadWriteLock.writeLock();
    }

    final V get(K k) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        this.readLock.lock();
        try {
            V v = this.map.get(k);
            if (v == null) {
                this.readLock.unlock();
                v = create(k);
                if (v == null) {
                    throw new NullPointerException("create returned null");
                }
                this.writeLock.lock();
                try {
                    this.map.put(k, v);
                } finally {
                    this.writeLock.unlock();
                }
            }
            return v;
        } finally {
            this.readLock.unlock();
        }
    }

    public final String toString() {
        this.readLock.lock();
        try {
            String obj = this.map.toString();
            return obj;
        } finally {
            this.readLock.unlock();
        }
    }
}
