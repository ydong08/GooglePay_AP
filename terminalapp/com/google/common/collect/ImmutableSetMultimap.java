package com.google.common.collect;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map.Entry;

public class ImmutableSetMultimap<K, V> extends ImmutableMultimap<K, V> implements SetMultimap<K, V> {
    private static final long serialVersionUID = 0;
    private final transient ImmutableSet<V> emptySet;
    private transient ImmutableSet<Entry<K, V>> entries;

    public static final class Builder<K, V> extends com.google.common.collect.ImmutableMultimap.Builder<K, V> {
        public Builder() {
            super(MultimapBuilder.linkedHashKeys().linkedHashSetValues().build());
        }

        public Builder<K, V> put(K k, V v) {
            this.builderMultimap.put(Preconditions.checkNotNull(k), Preconditions.checkNotNull(v));
            return this;
        }

        public ImmutableSetMultimap<K, V> build() {
            if (this.keyComparator != null) {
                Multimap build = MultimapBuilder.linkedHashKeys().linkedHashSetValues().build();
                for (Entry entry : Ordering.from(this.keyComparator).onKeys().immutableSortedCopy(this.builderMultimap.asMap().entrySet())) {
                    build.putAll(entry.getKey(), (Iterable) entry.getValue());
                }
                this.builderMultimap = build;
            }
            return ImmutableSetMultimap.copyOf(this.builderMultimap, this.valueComparator);
        }
    }

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

    public static <K, V> ImmutableSetMultimap<K, V> of() {
        return EmptyImmutableSetMultimap.INSTANCE;
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    private static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap, Comparator<? super V> comparator) {
        Preconditions.checkNotNull(multimap);
        if (multimap.isEmpty() && comparator == null) {
            return of();
        }
        if (multimap instanceof ImmutableSetMultimap) {
            ImmutableSetMultimap<K, V> immutableSetMultimap = (ImmutableSetMultimap) multimap;
            if (!immutableSetMultimap.isPartialView()) {
                return immutableSetMultimap;
            }
        }
        com.google.common.collect.ImmutableMap.Builder builder = new com.google.common.collect.ImmutableMap.Builder(multimap.asMap().size());
        int i = 0;
        for (Entry entry : multimap.asMap().entrySet()) {
            int i2;
            Object key = entry.getKey();
            ImmutableSet valueSet = valueSet(comparator, (Collection) entry.getValue());
            if (valueSet.isEmpty()) {
                i2 = i;
            } else {
                builder.put(key, valueSet);
                i2 = valueSet.size() + i;
            }
            i = i2;
        }
        return new ImmutableSetMultimap(builder.build(), i, comparator);
    }

    ImmutableSetMultimap(ImmutableMap<K, ImmutableSet<V>> immutableMap, int i, Comparator<? super V> comparator) {
        super(immutableMap, i);
        this.emptySet = emptySet(comparator);
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

    private static <V> ImmutableSet<V> valueSet(Comparator<? super V> comparator, Collection<? extends V> collection) {
        if (comparator == null) {
            return ImmutableSet.copyOf((Collection) collection);
        }
        return ImmutableSortedSet.copyOf((Comparator) comparator, (Collection) collection);
    }

    private static <V> ImmutableSet<V> emptySet(Comparator<? super V> comparator) {
        if (comparator == null) {
            return ImmutableSet.of();
        }
        return ImmutableSortedSet.emptySet(comparator);
    }

    private static <V> com.google.common.collect.ImmutableSet.Builder<V> valuesBuilder(Comparator<? super V> comparator) {
        if (comparator == null) {
            return new com.google.common.collect.ImmutableSet.Builder();
        }
        return new com.google.common.collect.ImmutableSortedSet.Builder(comparator);
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
        com.google.common.collect.ImmutableMap.Builder builder = ImmutableMap.builder();
        int i = 0;
        for (int i2 = 0; i2 < readInt; i2++) {
            Object readObject = objectInputStream.readObject();
            int readInt2 = objectInputStream.readInt();
            if (readInt2 <= 0) {
                throw new InvalidObjectException("Invalid value count " + readInt2);
            }
            com.google.common.collect.ImmutableSet.Builder valuesBuilder = valuesBuilder(comparator);
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
