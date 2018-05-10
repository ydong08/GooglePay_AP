package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table.Cell;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Map.Entry;

final class DenseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
    private final int[] cellColumnIndices;
    private final int[] cellRowIndices;
    private final int[] columnCounts = new int[this.columnKeyToIndex.size()];
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final int[] rowCounts = new int[this.rowKeyToIndex.size()];
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableMap<R, Map<C, V>> rowMap;
    private final V[][] values;

    static abstract class ImmutableArrayMap<K, V> extends IteratorBasedImmutableMap<K, V> {
        private final int size;

        abstract V getValue(int i);

        abstract ImmutableMap<K, Integer> keyToIndex();

        ImmutableArrayMap(int i) {
            this.size = i;
        }

        private boolean isFull() {
            return this.size == keyToIndex().size();
        }

        K getKey(int i) {
            return keyToIndex().keySet().asList().get(i);
        }

        ImmutableSet<K> createKeySet() {
            return isFull() ? keyToIndex().keySet() : super.createKeySet();
        }

        public int size() {
            return this.size;
        }

        public V get(Object obj) {
            Integer num = (Integer) keyToIndex().get(obj);
            return num == null ? null : getValue(num.intValue());
        }

        UnmodifiableIterator<Entry<K, V>> entryIterator() {
            return new AbstractIterator<Entry<K, V>>() {
                private int index = -1;
                private final int maxIndex = ImmutableArrayMap.this.keyToIndex().size();

                protected Entry<K, V> computeNext() {
                    this.index++;
                    while (this.index < this.maxIndex) {
                        Object value = ImmutableArrayMap.this.getValue(this.index);
                        if (value != null) {
                            return Maps.immutableEntry(ImmutableArrayMap.this.getKey(this.index), value);
                        }
                        this.index++;
                    }
                    return (Entry) endOfData();
                }
            };
        }
    }

    final class Column extends ImmutableArrayMap<R, V> {
        private final int columnIndex;

        Column(int i) {
            super(DenseImmutableTable.this.columnCounts[i]);
            this.columnIndex = i;
        }

        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.this.rowKeyToIndex;
        }

        V getValue(int i) {
            return DenseImmutableTable.this.values[i][this.columnIndex];
        }

        boolean isPartialView() {
            return true;
        }
    }

    final class ColumnMap extends ImmutableArrayMap<C, Map<R, V>> {
        private ColumnMap() {
            super(DenseImmutableTable.this.columnCounts.length);
        }

        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.this.columnKeyToIndex;
        }

        Map<R, V> getValue(int i) {
            return new Column(i);
        }

        boolean isPartialView() {
            return false;
        }
    }

    final class Row extends ImmutableArrayMap<C, V> {
        private final int rowIndex;

        Row(int i) {
            super(DenseImmutableTable.this.rowCounts[i]);
            this.rowIndex = i;
        }

        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.this.columnKeyToIndex;
        }

        V getValue(int i) {
            return DenseImmutableTable.this.values[this.rowIndex][i];
        }

        boolean isPartialView() {
            return true;
        }
    }

    final class RowMap extends ImmutableArrayMap<R, Map<C, V>> {
        private RowMap() {
            super(DenseImmutableTable.this.rowCounts.length);
        }

        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.this.rowKeyToIndex;
        }

        Map<C, V> getValue(int i) {
            return new Row(i);
        }

        boolean isPartialView() {
            return false;
        }
    }

    DenseImmutableTable(ImmutableList<Cell<R, C, V>> immutableList, ImmutableSet<R> immutableSet, ImmutableSet<C> immutableSet2) {
        this.values = (Object[][]) Array.newInstance(Object.class, new int[]{immutableSet.size(), immutableSet2.size()});
        this.rowKeyToIndex = Maps.indexMap(immutableSet);
        this.columnKeyToIndex = Maps.indexMap(immutableSet2);
        int[] iArr = new int[immutableList.size()];
        int[] iArr2 = new int[immutableList.size()];
        for (int i = 0; i < immutableList.size(); i++) {
            boolean z;
            Cell cell = (Cell) immutableList.get(i);
            Object rowKey = cell.getRowKey();
            Object columnKey = cell.getColumnKey();
            int intValue = ((Integer) this.rowKeyToIndex.get(rowKey)).intValue();
            int intValue2 = ((Integer) this.columnKeyToIndex.get(columnKey)).intValue();
            if (this.values[intValue][intValue2] == null) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "duplicate key: (%s, %s)", rowKey, columnKey);
            this.values[intValue][intValue2] = cell.getValue();
            int[] iArr3 = this.rowCounts;
            iArr3[intValue] = iArr3[intValue] + 1;
            iArr3 = this.columnCounts;
            iArr3[intValue2] = iArr3[intValue2] + 1;
            iArr[i] = intValue;
            iArr2[i] = intValue2;
        }
        this.cellRowIndices = iArr;
        this.cellColumnIndices = iArr2;
        this.rowMap = new RowMap();
        this.columnMap = new ColumnMap();
    }

    public ImmutableMap<C, Map<R, V>> columnMap() {
        return this.columnMap;
    }

    public ImmutableMap<R, Map<C, V>> rowMap() {
        return this.rowMap;
    }

    public V get(Object obj, Object obj2) {
        Integer num = (Integer) this.rowKeyToIndex.get(obj);
        Integer num2 = (Integer) this.columnKeyToIndex.get(obj2);
        return (num == null || num2 == null) ? null : this.values[num.intValue()][num2.intValue()];
    }

    public int size() {
        return this.cellRowIndices.length;
    }

    Cell<R, C, V> getCell(int i) {
        int i2 = this.cellRowIndices[i];
        int i3 = this.cellColumnIndices[i];
        return ImmutableTable.cellOf(rowKeySet().asList().get(i2), columnKeySet().asList().get(i3), this.values[i2][i3]);
    }

    V getValue(int i) {
        return this.values[this.cellRowIndices[i]][this.cellColumnIndices[i]];
    }

    SerializedForm createSerializedForm() {
        return SerializedForm.create(this, this.cellRowIndices, this.cellColumnIndices);
    }
}
