package com.google.common.collect;

import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Table.Cell;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
    static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable(ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());
    private final int[] cellColumnInRowIndices;
    private final int[] cellRowIndices;
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final ImmutableMap<R, Map<C, V>> rowMap;

    SparseImmutableTable(ImmutableList<Cell<R, C, V>> immutableList, ImmutableSet<R> immutableSet, ImmutableSet<C> immutableSet2) {
        Map indexMap = Maps.indexMap(immutableSet);
        Map newLinkedHashMap = Maps.newLinkedHashMap();
        Iterator it = immutableSet.iterator();
        while (it.hasNext()) {
            newLinkedHashMap.put(it.next(), new LinkedHashMap());
        }
        Map newLinkedHashMap2 = Maps.newLinkedHashMap();
        it = immutableSet2.iterator();
        while (it.hasNext()) {
            newLinkedHashMap2.put(it.next(), new LinkedHashMap());
        }
        int[] iArr = new int[immutableList.size()];
        int[] iArr2 = new int[immutableList.size()];
        for (int i = 0; i < immutableList.size(); i++) {
            Cell cell = (Cell) immutableList.get(i);
            Object rowKey = cell.getRowKey();
            Object columnKey = cell.getColumnKey();
            Object value = cell.getValue();
            iArr[i] = ((Integer) indexMap.get(rowKey)).intValue();
            Map map = (Map) newLinkedHashMap.get(rowKey);
            iArr2[i] = map.size();
            Object put = map.put(columnKey, value);
            if (put != null) {
                String valueOf = String.valueOf(rowKey);
                String valueOf2 = String.valueOf(columnKey);
                String valueOf3 = String.valueOf(value);
                String valueOf4 = String.valueOf(put);
                throw new IllegalArgumentException(new StringBuilder((((String.valueOf(valueOf).length() + 37) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()) + String.valueOf(valueOf4).length()).append("Duplicate value for row=").append(valueOf).append(", column=").append(valueOf2).append(": ").append(valueOf3).append(", ").append(valueOf4).toString());
            }
            ((Map) newLinkedHashMap2.get(columnKey)).put(rowKey, value);
        }
        this.cellRowIndices = iArr;
        this.cellColumnInRowIndices = iArr2;
        Builder builder = new Builder(newLinkedHashMap.size());
        for (Entry entry : newLinkedHashMap.entrySet()) {
            builder.put(entry.getKey(), ImmutableMap.copyOf((Map) entry.getValue()));
        }
        this.rowMap = builder.build();
        builder = new Builder(newLinkedHashMap2.size());
        for (Entry entry2 : newLinkedHashMap2.entrySet()) {
            builder.put(entry2.getKey(), ImmutableMap.copyOf((Map) entry2.getValue()));
        }
        this.columnMap = builder.build();
    }

    public ImmutableMap<C, Map<R, V>> columnMap() {
        return this.columnMap;
    }

    public ImmutableMap<R, Map<C, V>> rowMap() {
        return this.rowMap;
    }

    public int size() {
        return this.cellRowIndices.length;
    }

    Cell<R, C, V> getCell(int i) {
        Entry entry = (Entry) this.rowMap.entrySet().asList().get(this.cellRowIndices[i]);
        ImmutableMap immutableMap = (ImmutableMap) entry.getValue();
        Entry entry2 = (Entry) immutableMap.entrySet().asList().get(this.cellColumnInRowIndices[i]);
        return ImmutableTable.cellOf(entry.getKey(), entry2.getKey(), entry2.getValue());
    }

    V getValue(int i) {
        ImmutableMap immutableMap = (ImmutableMap) this.rowMap.values().asList().get(this.cellRowIndices[i]);
        return immutableMap.values().asList().get(this.cellColumnInRowIndices[i]);
    }

    SerializedForm createSerializedForm() {
        Map indexMap = Maps.indexMap(columnKeySet());
        int[] iArr = new int[cellSet().size()];
        Iterator it = cellSet().iterator();
        int i = 0;
        while (it.hasNext()) {
            int i2 = i + 1;
            iArr[i] = ((Integer) indexMap.get(((Cell) it.next()).getColumnKey())).intValue();
            i = i2;
        }
        return SerializedForm.create(this, this.cellRowIndices, iArr);
    }
}
