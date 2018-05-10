package com.google.common.collect;

import java.io.Serializable;
import java.util.Map.Entry;

final class ImmutableMapValues<K, V> extends ImmutableCollection<V> {
    private final ImmutableMap<K, V> map;

    static class SerializedForm<V> implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableMap<?, V> map;

        SerializedForm(ImmutableMap<?, V> immutableMap) {
            this.map = immutableMap;
        }

        Object readResolve() {
            return this.map.values();
        }
    }

    ImmutableMapValues(ImmutableMap<K, V> immutableMap) {
        this.map = immutableMap;
    }

    public int size() {
        return this.map.size();
    }

    public UnmodifiableIterator<V> iterator() {
        return new UnmodifiableIterator<V>() {
            final UnmodifiableIterator<Entry<K, V>> entryItr = ImmutableMapValues.this.map.entrySet().iterator();

            public boolean hasNext() {
                return this.entryItr.hasNext();
            }

            public V next() {
                return ((Entry) this.entryItr.next()).getValue();
            }
        };
    }

    public boolean contains(Object obj) {
        return obj != null && Iterators.contains(iterator(), obj);
    }

    boolean isPartialView() {
        return true;
    }

    public ImmutableList<V> asList() {
        final ImmutableList asList = this.map.entrySet().asList();
        return new ImmutableAsList<V>() {
            public V get(int i) {
                return ((Entry) asList.get(i)).getValue();
            }

            ImmutableCollection<V> delegateCollection() {
                return ImmutableMapValues.this;
            }
        };
    }

    Object writeReplace() {
        return new SerializedForm(this.map);
    }
}
