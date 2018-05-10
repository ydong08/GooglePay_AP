package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public interface Multimap<K, V> {
    Map<K, Collection<V>> asMap();

    void clear();

    boolean containsEntry(Object obj, Object obj2);

    boolean containsKey(Object obj);

    boolean containsValue(Object obj);

    Collection<Entry<K, V>> entries();

    Collection<V> get(K k);

    boolean isEmpty();

    boolean put(K k, V v);

    boolean putAll(K k, Iterable<? extends V> iterable);

    boolean remove(Object obj, Object obj2);

    int size();
}
