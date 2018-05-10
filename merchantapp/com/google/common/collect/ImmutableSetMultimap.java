package com.google.common.collect;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet.Builder;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.Map.Entry;

public class ImmutableSetMultimap<K, V> extends ImmutableMultimap<K, V> implements SetMultimap<K, V> {
    private static final long serialVersionUID = 0;
    private final transient ImmutableSet<V> emptySet;
    private transient ImmutableSet<Entry<K, V>> entries;

    static final class EntrySet<K, V> extends ImmutableSet<Entry<K, V>> {
        private final transient ImmutableSetMultimap<K, V> multimap;

        EntrySet(ImmutableSetMultimap<K, V> immutableSetMultimap) {
            this.multimap = immutableSetMultimap;
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            return this.multimap.containsEntry(entry.getKey(), entry.getValue());
        }

        public int size() {
            return this.multimap.size();
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return this.multimap.entryIterator();
        }

        boolean isPartialView() {
            return false;
        }
    }

    public ImmutableSet<V> get(K k) {
        return (ImmutableSet) MoreObjects.firstNonNull((ImmutableSet) this.map.get(k), this.emptySet);
    }

    public ImmutableSet<Entry<K, V>> entries() {
        ImmutableSet<Entry<K, V>> immutableSet = this.entries;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = new EntrySet(this);
        this.entries = immutableSet;
        return immutableSet;
    }

    private static <V> ImmutableSet<V> emptySet(Comparator<? super V> comparator) {
        if (comparator == null) {
            return ImmutableSet.of();
        }
        return ImmutableSortedSet.emptySet(comparator);
    }

    private static <V> Builder<V> valuesBuilder(Comparator<? super V> comparator) {
        if (comparator == null) {
            return new Builder();
        }
        return new ImmutableSortedSet.Builder(comparator);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(valueComparator());
        Serialization.writeMultimap(this, objectOutputStream);
    }

    Comparator<? super V> valueComparator() {
        if (this.emptySet instanceof ImmutableSortedSet) {
            return ((ImmutableSortedSet) this.emptySet).comparator();
        }
        return null;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Comparator comparator = (Comparator) objectInputStream.readObject();
        int readInt = objectInputStream.readInt();
        if (readInt < 0) {
            throw new InvalidObjectException("Invalid key count " + readInt);
        }
        ImmutableMap.Builder builder = ImmutableMap.builder();
        int i = 0;
        for (int i2 = 0; i2 < readInt; i2++) {
            Object readObject = objectInputStream.readObject();
            int readInt2 = objectInputStream.readInt();
            if (readInt2 <= 0) {
                throw new InvalidObjectException("Invalid value count " + readInt2);
            }
            Builder valuesBuilder = valuesBuilder(comparator);
            for (int i3 = 0; i3 < readInt2; i3++) {
                valuesBuilder.add(objectInputStream.readObject());
            }
            ImmutableSet build = valuesBuilder.build();
            if (build.size() != readInt2) {
                String valueOf = String.valueOf(readObject);
                throw new InvalidObjectException(new StringBuilder(String.valueOf(valueOf).length() + 40).append("Duplicate key-value pairs exist for key ").append(valueOf).toString());
            }
            builder.put(readObject, build);
            i += readInt2;
        }
        try {
            FieldSettersHolder.MAP_FIELD_SETTER.set((Object) this, builder.build());
            FieldSettersHolder.SIZE_FIELD_SETTER.set((Object) this, i);
            FieldSettersHolder.EMPTY_SET_FIELD_SETTER.set((Object) this, emptySet(comparator));
        } catch (Throwable e) {
            throw ((InvalidObjectException) new InvalidObjectException(e.getMessage()).initCause(e));
        }
    }
}
