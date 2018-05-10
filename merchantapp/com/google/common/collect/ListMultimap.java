package com.google.common.collect;

import java.util.List;

public interface ListMultimap<K, V> extends Multimap<K, V> {
    List<V> get(K k);
}
