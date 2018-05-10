package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Map.Entry;

final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    private static final long serialVersionUID = 0;
    private final transient Entry<K, V>[] entries;
    private final transient int mask;
    private final transient ImmutableMapEntry<K, V>[] table;

    static final class KeySet<K, V> extends Indexed<K> {
        private final RegularImmutableMap<K, V> map;

        static class SerializedForm<K> implements Serializable {
            private static final long serialVersionUID = 0;
            final ImmutableMap<K, ?> map;

            SerializedForm(ImmutableMap<K, ?> immutableMap) {
                this.map = immutableMap;
            }

            Object readResolve() {
                return this.map.keySet();
            }
        }

        KeySet(RegularImmutableMap<K, V> regularImmutableMap) {
            this.map = regularImmutableMap;
        }

        K get(int i) {
            return this.map.entries[i].getKey();
        }

        public boolean contains(Object obj) {
            return this.map.containsKey(obj);
        }

        boolean isPartialView() {
            return true;
        }

        public int size() {
            return this.map.size();
        }

        Object writeReplace() {
            return new SerializedForm(this.map);
        }
    }

    static final class Values<K, V> extends ImmutableList<V> {
        final RegularImmutableMap<K, V> map;

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

        Values(RegularImmutableMap<K, V> regularImmutableMap) {
            this.map = regularImmutableMap;
        }

        public V get(int i) {
            return this.map.entries[i].getValue();
        }

        public int size() {
            return this.map.size();
        }

        boolean isPartialView() {
            return true;
        }

        Object writeReplace() {
            return new SerializedForm(this.map);
        }
    }

    static <K, V> RegularImmutableMap<K, V> fromEntries(Entry<K, V>... entryArr) {
        return fromEntryArray(entryArr.length, entryArr);
    }

    static <K, V> RegularImmutableMap<K, V> fromEntryArray(int i, Entry<K, V>[] entryArr) {
        Entry[] entryArr2;
        Preconditions.checkPositionIndex(i, entryArr.length);
        if (i == entryArr.length) {
            entryArr2 = entryArr;
        } else {
            Object[] createEntryArray = ImmutableMapEntry.createEntryArray(i);
        }
        int closedTableSize = Hashing.closedTableSize(i, 1.2d);
        ImmutableMapEntry[] createEntryArray2 = ImmutableMapEntry.createEntryArray(closedTableSize);
        int i2 = closedTableSize - 1;
        for (int i3 = 0; i3 < i; i3++) {
            Entry entry = entryArr[i3];
            Object key = entry.getKey();
            Object value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int smear = Hashing.smear(key.hashCode()) & i2;
            ImmutableMapEntry immutableMapEntry = createEntryArray2[smear];
            if (immutableMapEntry == null) {
                Object obj = ((entry instanceof ImmutableMapEntry) && ((ImmutableMapEntry) entry).isReusable()) ? 1 : null;
                entry = obj != null ? (ImmutableMapEntry) entry : new ImmutableMapEntry(key, value);
            } else {
                entry = new NonTerminalImmutableMapEntry(key, value, immutableMapEntry);
            }
            createEntryArray2[smear] = entry;
            entryArr2[i3] = entry;
            checkNoConflictInKeyBucket(key, entry, immutableMapEntry);
        }
        return new RegularImmutableMap(entryArr2, createEntryArray2, i2);
    }

    private RegularImmutableMap(Entry<K, V>[] entryArr, ImmutableMapEntry<K, V>[] immutableMapEntryArr, int i) {
        this.entries = entryArr;
        this.table = immutableMapEntryArr;
        this.mask = i;
    }

    static void checkNoConflictInKeyBucket(Object obj, Entry<?, ?> entry, ImmutableMapEntry<?, ?> immutableMapEntry) {
        while (immutableMapEntry != null) {
            ImmutableMap.checkNoConflict(!obj.equals(immutableMapEntry.getKey()), "key", entry, immutableMapEntry);
            immutableMapEntry = immutableMapEntry.getNextInKeyBucket();
        }
    }

    public V get(Object obj) {
        return get(obj, this.table, this.mask);
    }

    static <V> V get(Object obj, ImmutableMapEntry<?, V>[] immutableMapEntryArr, int i) {
        if (obj == null) {
            return null;
        }
        for (ImmutableMapEntry immutableMapEntry = immutableMapEntryArr[Hashing.smear(obj.hashCode()) & i]; immutableMapEntry != null; immutableMapEntry = immutableMapEntry.getNextInKeyBucket()) {
            if (obj.equals(immutableMapEntry.getKey())) {
                return immutableMapEntry.getValue();
            }
        }
        return null;
    }

    public int size() {
        return this.entries.length;
    }

    boolean isPartialView() {
        return false;
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return new RegularEntrySet(this, this.entries);
    }

    ImmutableSet<K> createKeySet() {
        return new KeySet(this);
    }

    ImmutableCollection<V> createValues() {
        return new Values(this);
    }
}
