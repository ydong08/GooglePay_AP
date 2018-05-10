package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.NavigableMap;

public final class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V> {
    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap(ImmutableSortedSet.emptySet(Ordering.natural()), ImmutableList.of());
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
    private static final long serialVersionUID = 0;
    private transient ImmutableSortedMap<K, V> descendingMap;
    private final transient RegularImmutableSortedSet<K> keySet;
    private final transient ImmutableList<V> valueList;

    public static class Builder<K, V> extends com.google.common.collect.ImmutableMap.Builder<K, V> {
        private final Comparator<? super K> comparator;

        public Builder(Comparator<? super K> comparator) {
            this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        }

        public Builder<K, V> put(K k, V v) {
            super.put(k, v);
            return this;
        }

        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }

        public ImmutableSortedMap<K, V> build() {
            switch (this.size) {
                case 0:
                    return ImmutableSortedMap.emptyMap(this.comparator);
                case 1:
                    return ImmutableSortedMap.of(this.comparator, this.entries[0].getKey(), this.entries[0].getValue());
                default:
                    return ImmutableSortedMap.fromEntries(this.comparator, false, this.entries, this.size);
            }
        }
    }

    static class SerializedForm extends SerializedForm {
        private static final long serialVersionUID = 0;
        private final Comparator<Object> comparator;

        SerializedForm(ImmutableSortedMap<?, ?> immutableSortedMap) {
            super(immutableSortedMap);
            this.comparator = immutableSortedMap.comparator();
        }

        Object readResolve() {
            return createMap(new Builder(this.comparator));
        }
    }

    static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return of();
        }
        return new ImmutableSortedMap(ImmutableSortedSet.emptySet(comparator), ImmutableList.of());
    }

    public static <K, V> ImmutableSortedMap<K, V> of() {
        return NATURAL_EMPTY_MAP;
    }

    private static <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K k, V v) {
        return new ImmutableSortedMap(new RegularImmutableSortedSet(ImmutableList.of(k), (Comparator) Preconditions.checkNotNull(comparator)), ImmutableList.of(v));
    }

    private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean z, Entry<K, V>[] entryArr, int i) {
        int i2 = 0;
        switch (i) {
            case 0:
                return emptyMap(comparator);
            case 1:
                return of(comparator, entryArr[0].getKey(), entryArr[0].getValue());
            default:
                Object[] objArr = new Object[i];
                Object[] objArr2 = new Object[i];
                Object key;
                if (z) {
                    while (i2 < i) {
                        key = entryArr[i2].getKey();
                        Object value = entryArr[i2].getValue();
                        CollectPreconditions.checkEntryNotNull(key, value);
                        objArr[i2] = key;
                        objArr2[i2] = value;
                        i2++;
                    }
                } else {
                    Arrays.sort(entryArr, 0, i, Ordering.from(comparator).onKeys());
                    key = entryArr[0].getKey();
                    objArr[0] = key;
                    objArr2[0] = entryArr[0].getValue();
                    int i3 = 1;
                    while (i3 < i) {
                        boolean z2;
                        Object key2 = entryArr[i3].getKey();
                        Object value2 = entryArr[i3].getValue();
                        CollectPreconditions.checkEntryNotNull(key2, value2);
                        objArr[i3] = key2;
                        objArr2[i3] = value2;
                        if (comparator.compare(key, key2) != 0) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        ImmutableMap.checkNoConflict(z2, "key", entryArr[i3 - 1], entryArr[i3]);
                        i3++;
                        key = key2;
                    }
                }
                return new ImmutableSortedMap(new RegularImmutableSortedSet(new RegularImmutableList(objArr), comparator), new RegularImmutableList(objArr2));
        }
    }

    ImmutableSortedMap(RegularImmutableSortedSet<K> regularImmutableSortedSet, ImmutableList<V> immutableList) {
        this(regularImmutableSortedSet, immutableList, null);
    }

    ImmutableSortedMap(RegularImmutableSortedSet<K> regularImmutableSortedSet, ImmutableList<V> immutableList, ImmutableSortedMap<K, V> immutableSortedMap) {
        this.keySet = regularImmutableSortedSet;
        this.valueList = immutableList;
        this.descendingMap = immutableSortedMap;
    }

    public int size() {
        return this.valueList.size();
    }

    public V get(Object obj) {
        int indexOf = this.keySet.indexOf(obj);
        return indexOf == -1 ? null : this.valueList.get(indexOf);
    }

    boolean isPartialView() {
        return this.keySet.isPartialView() || this.valueList.isPartialView();
    }

    public ImmutableSet<Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return isEmpty() ? ImmutableSet.of() : new ImmutableMapEntrySet<K, V>() {
            public UnmodifiableIterator<Entry<K, V>> iterator() {
                return asList().iterator();
            }

            ImmutableList<Entry<K, V>> createAsList() {
                return new ImmutableAsList<Entry<K, V>>() {
                    public Entry<K, V> get(int i) {
                        return Maps.immutableEntry(ImmutableSortedMap.this.keySet.asList().get(i), ImmutableSortedMap.this.valueList.get(i));
                    }

                    ImmutableCollection<Entry<K, V>> delegateCollection() {
                        return AnonymousClass1EntrySet.this;
                    }
                };
            }

            ImmutableMap<K, V> map() {
                return ImmutableSortedMap.this;
            }
        };
    }

    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }

    public ImmutableCollection<V> values() {
        return this.valueList;
    }

    public Comparator<? super K> comparator() {
        return keySet().comparator();
    }

    public K firstKey() {
        return keySet().first();
    }

    public K lastKey() {
        return keySet().last();
    }

    private ImmutableSortedMap<K, V> getSubMap(int i, int i2) {
        if (i == 0 && i2 == size()) {
            return this;
        }
        if (i == i2) {
            return emptyMap(comparator());
        }
        return new ImmutableSortedMap(this.keySet.getSubSet(i, i2), this.valueList.subList(i, i2));
    }

    public ImmutableSortedMap<K, V> headMap(K k) {
        return headMap((Object) k, false);
    }

    public ImmutableSortedMap<K, V> headMap(K k, boolean z) {
        return getSubMap(0, this.keySet.headIndex(Preconditions.checkNotNull(k), z));
    }

    public ImmutableSortedMap<K, V> subMap(K k, K k2) {
        return subMap((Object) k, true, (Object) k2, false);
    }

    public ImmutableSortedMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(k2);
        Preconditions.checkArgument(comparator().compare(k, k2) <= 0, "expected fromKey <= toKey but %s > %s", (Object) k, (Object) k2);
        return headMap((Object) k2, z2).tailMap((Object) k, z);
    }

    public ImmutableSortedMap<K, V> tailMap(K k) {
        return tailMap((Object) k, true);
    }

    public ImmutableSortedMap<K, V> tailMap(K k, boolean z) {
        return getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(k), z), size());
    }

    public Entry<K, V> lowerEntry(K k) {
        return headMap((Object) k, false).lastEntry();
    }

    public K lowerKey(K k) {
        return Maps.keyOrNull(lowerEntry(k));
    }

    public Entry<K, V> floorEntry(K k) {
        return headMap((Object) k, true).lastEntry();
    }

    public K floorKey(K k) {
        return Maps.keyOrNull(floorEntry(k));
    }

    public Entry<K, V> ceilingEntry(K k) {
        return tailMap((Object) k, true).firstEntry();
    }

    public K ceilingKey(K k) {
        return Maps.keyOrNull(ceilingEntry(k));
    }

    public Entry<K, V> higherEntry(K k) {
        return tailMap((Object) k, false).firstEntry();
    }

    public K higherKey(K k) {
        return Maps.keyOrNull(higherEntry(k));
    }

    public Entry<K, V> firstEntry() {
        return isEmpty() ? null : (Entry) entrySet().asList().get(0);
    }

    public Entry<K, V> lastEntry() {
        return isEmpty() ? null : (Entry) entrySet().asList().get(size() - 1);
    }

    @Deprecated
    public final Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> immutableSortedMap = this.descendingMap;
        if (immutableSortedMap != null) {
            return immutableSortedMap;
        }
        if (isEmpty()) {
            return emptyMap(Ordering.from(comparator()).reverse());
        }
        return new ImmutableSortedMap((RegularImmutableSortedSet) this.keySet.descendingSet(), this.valueList.reverse(), this);
    }

    public ImmutableSortedSet<K> navigableKeySet() {
        return this.keySet;
    }

    public ImmutableSortedSet<K> descendingKeySet() {
        return this.keySet.descendingSet();
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
