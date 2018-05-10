package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map.Entry;

final class ImmutableEnumMap<K extends Enum<K>, V> extends IteratorBasedImmutableMap<K, V> {
    private final transient EnumMap<K, V> delegate;

    static class EnumSerializedForm<K extends Enum<K>, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final EnumMap<K, V> delegate;

        EnumSerializedForm(EnumMap<K, V> enumMap) {
            this.delegate = enumMap;
        }

        Object readResolve() {
            return new ImmutableEnumMap(this.delegate);
        }
    }

    static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(EnumMap<K, V> enumMap) {
        switch (enumMap.size()) {
            case 0:
                return ImmutableMap.of();
            case 1:
                Entry entry = (Entry) Iterables.getOnlyElement(enumMap.entrySet());
                return ImmutableMap.of((Enum) entry.getKey(), entry.getValue());
            default:
                return new ImmutableEnumMap(enumMap);
        }
    }

    private ImmutableEnumMap(EnumMap<K, V> enumMap) {
        this.delegate = enumMap;
        Preconditions.checkArgument(!enumMap.isEmpty());
    }

    UnmodifiableIterator<K> keyIterator() {
        return Iterators.unmodifiableIterator(this.delegate.keySet().iterator());
    }

    public int size() {
        return this.delegate.size();
    }

    public boolean containsKey(Object obj) {
        return this.delegate.containsKey(obj);
    }

    public V get(Object obj) {
        return this.delegate.get(obj);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ImmutableEnumMap) {
            obj = ((ImmutableEnumMap) obj).delegate;
        }
        return this.delegate.equals(obj);
    }

    UnmodifiableIterator<Entry<K, V>> entryIterator() {
        return Maps.unmodifiableEntryIterator(this.delegate.entrySet().iterator());
    }

    boolean isPartialView() {
        return false;
    }

    Object writeReplace() {
        return new EnumSerializedForm(this.delegate);
    }
}
