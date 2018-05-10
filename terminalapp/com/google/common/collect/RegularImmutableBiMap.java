package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Map.Entry;

class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
    static final RegularImmutableBiMap<Object, Object> EMPTY = new RegularImmutableBiMap(null, null, ImmutableMap.EMPTY_ENTRY_ARRAY, 0, 0);
    private final transient Entry<K, V>[] entries;
    private final transient int hashCode;
    private transient ImmutableBiMap<V, K> inverse;
    private final transient ImmutableMapEntry<K, V>[] keyTable;
    private final transient int mask;
    private final transient ImmutableMapEntry<K, V>[] valueTable;

    final class Inverse extends ImmutableBiMap<V, K> {

        final class InverseEntrySet extends ImmutableMapEntrySet<V, K> {
            InverseEntrySet() {
            }

            ImmutableMap<V, K> map() {
                return Inverse.this;
            }

            boolean isHashCodeFast() {
                return true;
            }

            public int hashCode() {
                return RegularImmutableBiMap.this.hashCode;
            }

            public UnmodifiableIterator<Entry<V, K>> iterator() {
                return asList().iterator();
            }

            ImmutableList<Entry<V, K>> createAsList() {
                return new ImmutableAsList<Entry<V, K>>() {
                    public Entry<V, K> get(int i) {
                        Entry entry = RegularImmutableBiMap.this.entries[i];
                        return Maps.immutableEntry(entry.getValue(), entry.getKey());
                    }

                    ImmutableCollection<Entry<V, K>> delegateCollection() {
                        return InverseEntrySet.this;
                    }
                };
            }
        }

        private Inverse() {
        }

        public int size() {
            return inverse().size();
        }

        public ImmutableBiMap<K, V> inverse() {
            return RegularImmutableBiMap.this;
        }

        public K get(Object obj) {
            if (obj == null || RegularImmutableBiMap.this.valueTable == null) {
                return null;
            }
            for (ImmutableMapEntry immutableMapEntry = RegularImmutableBiMap.this.valueTable[Hashing.smear(obj.hashCode()) & RegularImmutableBiMap.this.mask]; immutableMapEntry != null; immutableMapEntry = immutableMapEntry.getNextInValueBucket()) {
                if (obj.equals(immutableMapEntry.getValue())) {
                    return immutableMapEntry.getKey();
                }
            }
            return null;
        }

        ImmutableSet<Entry<V, K>> createEntrySet() {
            return new InverseEntrySet();
        }

        boolean isPartialView() {
            return false;
        }

        Object writeReplace() {
            return new InverseSerializedForm(RegularImmutableBiMap.this);
        }
    }

    static class InverseSerializedForm<K, V> implements Serializable {
        private static final long serialVersionUID = 1;
        private final ImmutableBiMap<K, V> forward;

        InverseSerializedForm(ImmutableBiMap<K, V> immutableBiMap) {
            this.forward = immutableBiMap;
        }

        Object readResolve() {
            return this.forward.inverse();
        }
    }

    static <K, V> RegularImmutableBiMap<K, V> fromEntryArray(int i, Entry<K, V>[] entryArr) {
        Entry[] entryArr2;
        Preconditions.checkPositionIndex(i, entryArr.length);
        int closedTableSize = Hashing.closedTableSize(i, 1.2d);
        int i2 = closedTableSize - 1;
        ImmutableMapEntry[] createEntryArray = ImmutableMapEntry.createEntryArray(closedTableSize);
        ImmutableMapEntry[] createEntryArray2 = ImmutableMapEntry.createEntryArray(closedTableSize);
        if (i == entryArr.length) {
            entryArr2 = entryArr;
        } else {
            entryArr2 = ImmutableMapEntry.createEntryArray(i);
        }
        int i3 = 0;
        for (int i4 = 0; i4 < i; i4++) {
            ImmutableMapEntry immutableMapEntry;
            Entry entry = entryArr[i4];
            Object key = entry.getKey();
            Object value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int hashCode = key.hashCode();
            int hashCode2 = value.hashCode();
            int smear = Hashing.smear(hashCode) & i2;
            int smear2 = Hashing.smear(hashCode2) & i2;
            ImmutableMapEntry immutableMapEntry2 = createEntryArray[smear];
            RegularImmutableMap.checkNoConflictInKeyBucket(key, entry, immutableMapEntry2);
            ImmutableMapEntry immutableMapEntry3 = createEntryArray2[smear2];
            checkNoConflictInValueBucket(value, entry, immutableMapEntry3);
            if (immutableMapEntry3 == null && immutableMapEntry2 == null) {
                Object obj = ((entry instanceof ImmutableMapEntry) && ((ImmutableMapEntry) entry).isReusable()) ? 1 : null;
                immutableMapEntry = obj != null ? (ImmutableMapEntry) entry : new ImmutableMapEntry(key, value);
            } else {
                immutableMapEntry = new NonTerminalImmutableBiMapEntry(key, value, immutableMapEntry2, immutableMapEntry3);
            }
            createEntryArray[smear] = immutableMapEntry;
            createEntryArray2[smear2] = immutableMapEntry;
            entryArr2[i4] = immutableMapEntry;
            i3 += hashCode ^ hashCode2;
        }
        return new RegularImmutableBiMap(createEntryArray, createEntryArray2, entryArr2, i2, i3);
    }

    private RegularImmutableBiMap(ImmutableMapEntry<K, V>[] immutableMapEntryArr, ImmutableMapEntry<K, V>[] immutableMapEntryArr2, Entry<K, V>[] entryArr, int i, int i2) {
        this.keyTable = immutableMapEntryArr;
        this.valueTable = immutableMapEntryArr2;
        this.entries = entryArr;
        this.mask = i;
        this.hashCode = i2;
    }

    private static void checkNoConflictInValueBucket(Object obj, Entry<?, ?> entry, ImmutableMapEntry<?, ?> immutableMapEntry) {
        while (immutableMapEntry != null) {
            ImmutableMap.checkNoConflict(!obj.equals(immutableMapEntry.getValue()), "value", entry, immutableMapEntry);
            immutableMapEntry = immutableMapEntry.getNextInValueBucket();
        }
    }

    public V get(Object obj) {
        return this.keyTable == null ? null : RegularImmutableMap.get(obj, this.keyTable, this.mask);
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        if (isEmpty()) {
            return ImmutableSet.of();
        }
        return new RegularEntrySet(this, this.entries);
    }

    boolean isHashCodeFast() {
        return true;
    }

    public int hashCode() {
        return this.hashCode;
    }

    boolean isPartialView() {
        return false;
    }

    public int size() {
        return this.entries.length;
    }

    public ImmutableBiMap<V, K> inverse() {
        if (isEmpty()) {
            return ImmutableBiMap.of();
        }
        ImmutableBiMap<V, K> immutableBiMap = this.inverse;
        if (immutableBiMap != null) {
            return immutableBiMap;
        }
        immutableBiMap = new Inverse();
        this.inverse = immutableBiMap;
        return immutableBiMap;
    }
}
