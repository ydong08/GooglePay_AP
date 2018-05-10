package com.google.common.collect;

import java.util.Map;
import java.util.Set;

public interface Table<R, C, V> {

    public interface Cell<R, C, V> {
        C getColumnKey();

        R getRowKey();

        V getValue();
    }

    Set<Cell<R, C, V>> cellSet();

    Map<R, Map<C, V>> rowMap();

    int size();
}
