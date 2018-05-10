package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class Maps {
    static final MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");

    static abstract class EntrySet<K, V> extends ImprovedAbstractSet<Entry<K, V>> {
        abstract Map<K, V> map();

        EntrySet() {
        }

        public int size() {
            return map().size();
        }

        public void clear() {
            map().clear();
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            Object key = entry.getKey();
            Object safeGet = Maps.safeGet(map(), key);
            if (!Objects.equal(safeGet, entry.getValue())) {
                return false;
            }
            if (safeGet != null || map().containsKey(key)) {
                return true;
            }
            return false;
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public boolean remove(Object obj) {
            if (!contains(obj)) {
                return false;
            }
            return map().keySet().remove(((Entry) obj).getKey());
        }

        public boolean removeAll(Collection<?> collection) {
            try {
                return super.removeAll((Collection) Preconditions.checkNotNull(collection));
            } catch (UnsupportedOperationException e) {
                return Sets.removeAllImpl((Set) this, collection.iterator());
            }
        }

        public boolean retainAll(Collection<?> collection) {
            try {
                return super.retainAll((Collection) Preconditions.checkNotNull(collection));
            } catch (UnsupportedOperationException e) {
                Collection newHashSetWithExpectedSize = Sets.newHashSetWithExpectedSize(collection.size());
                for (Object next : collection) {
                    if (contains(next)) {
                        newHashSetWithExpectedSize.add(((Entry) next).getKey());
                    }
                }
                return map().keySet().retainAll(newHashSetWithExpectedSize);
            }
        }
    }

    static abstract class ViewCachingAbstractMap<K, V> extends AbstractMap<K, V> {
        private transient Set<Entry<K, V>> entrySet;
        private transient Set<K> keySet;
        private transient Collection<V> values;

        abstract Set<Entry<K, V>> createEntrySet();

        ViewCachingAbstractMap() {
        }

        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = createEntrySet();
            this.entrySet = set;
            return set;
        }

        public Set<K> keySet() {
            Set<K> set = this.keySet;
            if (set != null) {
                return set;
            }
            set = createKeySet();
            this.keySet = set;
            return set;
        }

        Set<K> createKeySet() {
            return new KeySet(this);
        }

        public Collection<V> values() {
            Collection<V> collection = this.values;
            if (collection != null) {
                return collection;
            }
            collection = createValues();
            this.values = collection;
            return collection;
        }

        Collection<V> createValues() {
            return new Values(this);
        }
    }

    static class KeySet<K, V> extends ImprovedAbstractSet<K> {
        final Map<K, V> map;

        KeySet(Map<K, V> map) {
            this.map = (Map) Preconditions.checkNotNull(map);
        }

        Map<K, V> map() {
            return this.map;
        }

        public Iterator<K> iterator() {
            return Maps.keyIterator(map().entrySet().iterator());
        }

        public int size() {
            return map().size();
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public boolean contains(Object obj) {
            return map().containsKey(obj);
        }

        public boolean remove(Object obj) {
            if (!contains(obj)) {
                return false;
            }
            map().remove(obj);
            return true;
        }

        public void clear() {
            map().clear();
        }
    }

    static class Values<K, V> extends AbstractCollection<V> {
        final Map<K, V> map;

        Values(Map<K, V> map) {
            this.map = (Map) Preconditions.checkNotNull(map);
        }

        final Map<K, V> map() {
            return this.map;
        }

        public Iterator<V> iterator() {
            return Maps.valueIterator(map().entrySet().iterator());
        }

        public boolean remove(Object obj) {
            try {
                return super.remove(obj);
            } catch (UnsupportedOperationException e) {
                for (Entry entry : map().entrySet()) {
                    if (Objects.equal(obj, entry.getValue())) {
                        map().remove(entry.getKey());
                        return true;
                    }
                }
                return false;
            }
        }

        public boolean removeAll(Collection<?> collection) {
            try {
                return super.removeAll((Collection) Preconditions.checkNotNull(collection));
            } catch (UnsupportedOperationException e) {
                Collection newHashSet = Sets.newHashSet();
                for (Entry entry : map().entrySet()) {
                    if (collection.contains(entry.getValue())) {
                        newHashSet.add(entry.getKey());
                    }
                }
                return map().keySet().removeAll(newHashSet);
            }
        }

        public boolean retainAll(Collection<?> collection) {
            try {
                return super.retainAll((Collection) Preconditions.checkNotNull(collection));
            } catch (UnsupportedOperationException e) {
                Collection newHashSet = Sets.newHashSet();
                for (Entry entry : map().entrySet()) {
                    if (collection.contains(entry.getValue())) {
                        newHashSet.add(entry.getKey());
                    }
                }
                return map().keySet().retainAll(newHashSet);
            }
        }

        public int size() {
            return map().size();
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public boolean contains(Object obj) {
            return map().containsValue(obj);
        }

        public void clear() {
            map().clear();
        }
    }

    class AnonymousClass1 extends TransformedIterator<K, Entry<K, V>> {
        final /* synthetic */ Function val$function;

        Entry<K, V> transform(K k) {
            return Maps.immutableEntry(k, this.val$function.apply(k));
        }
    }

    class AnonymousClass5 extends AbstractMapEntry<K, V> {
        final /* synthetic */ Entry val$entry;

        AnonymousClass5(Entry entry) {
            this.val$entry = entry;
        }

        public K getKey() {
            return this.val$entry.getKey();
        }

        public V getValue() {
            return this.val$entry.getValue();
        }
    }

    class AnonymousClass6 extends UnmodifiableIterator<Entry<K, V>> {
        final /* synthetic */ Iterator val$entryIterator;

        AnonymousClass6(Iterator it) {
            this.val$entryIterator = it;
        }

        public boolean hasNext() {
            return this.val$entryIterator.hasNext();
        }

        public Entry<K, V> next() {
            return Maps.unmodifiableEntry((Entry) this.val$entryIterator.next());
        }
    }

    enum EntryFunction implements Function<Entry<?, ?>, Object> {
        KEY {
            public Object apply(Entry<?, ?> entry) {
                return entry.getKey();
            }
        },
        VALUE {
            public Object apply(Entry<?, ?> entry) {
                return entry.getValue();
            }
        };
    }

    private Maps() {
    }

    static <K> Function<Entry<K, ?>, K> keyFunction() {
        return EntryFunction.KEY;
    }

    static <V> Function<Entry<?, V>, V> valueFunction() {
        return EntryFunction.VALUE;
    }

    static <K, V> Iterator<K> keyIterator(Iterator<Entry<K, V>> it) {
        return Iterators.transform(it, keyFunction());
    }

    static <K, V> Iterator<V> valueIterator(Iterator<Entry<K, V>> it) {
        return Iterators.transform(it, valueFunction());
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap();
    }

    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int i) {
        return new HashMap(capacity(i));
    }

    static int capacity(int i) {
        if (i < 3) {
            CollectPreconditions.checkNonnegative(i, "expectedSize");
            return i + 1;
        } else if (i < 1073741824) {
            return (int) ((((float) i) / 0.75f) + 1.0f);
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int i) {
        return new LinkedHashMap(capacity(i));
    }

    public static <K, V> Entry<K, V> immutableEntry(K k, V v) {
        return new ImmutableEntry(k, v);
    }

    static <K, V> Entry<K, V> unmodifiableEntry(Entry<? extends K, ? extends V> entry) {
        Preconditions.checkNotNull(entry);
        return new AnonymousClass5(entry);
    }

    static <K, V> UnmodifiableIterator<Entry<K, V>> unmodifiableEntryIterator(Iterator<Entry<K, V>> it) {
        return new AnonymousClass6(it);
    }

    static <K> Predicate<Entry<K, ?>> keyPredicateOnEntries(Predicate<? super K> predicate) {
        return Predicates.compose(predicate, keyFunction());
    }

    static <V> Predicate<Entry<?, V>> valuePredicateOnEntries(Predicate<? super V> predicate) {
        return Predicates.compose(predicate, valueFunction());
    }

    static <V> V safeGet(Map<?, V> map, Object obj) {
        V v = null;
        Preconditions.checkNotNull(map);
        try {
            v = map.get(obj);
        } catch (ClassCastException e) {
        } catch (NullPointerException e2) {
        }
        return v;
    }

    static boolean safeContainsKey(Map<?, ?> map, Object obj) {
        boolean z = false;
        Preconditions.checkNotNull(map);
        try {
            z = map.containsKey(obj);
        } catch (ClassCastException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    static <V> V safeRemove(Map<?, V> map, Object obj) {
        V v = null;
        Preconditions.checkNotNull(map);
        try {
            v = map.remove(obj);
        } catch (ClassCastException e) {
        } catch (NullPointerException e2) {
        }
        return v;
    }

    static boolean equalsImpl(Map<?, ?> map, Object obj) {
        if (map == obj) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        return map.entrySet().equals(((Map) obj).entrySet());
    }

    static String toStringImpl(Map<?, ?> map) {
        StringBuilder append = Collections2.newStringBuilderForCollection(map.size()).append('{');
        STANDARD_JOINER.appendTo(append, (Map) map);
        return append.append('}').toString();
    }

    static <K> K keyOrNull(Entry<K, ?> entry) {
        return entry == null ? null : entry.getKey();
    }

    static <E> ImmutableMap<E, Integer> indexMap(Collection<E> collection) {
        Builder builder = new Builder(collection.size());
        int i = 0;
        for (E put : collection) {
            int i2 = i + 1;
            builder.put(put, Integer.valueOf(i));
            i = i2;
        }
        return builder.build();
    }
}
