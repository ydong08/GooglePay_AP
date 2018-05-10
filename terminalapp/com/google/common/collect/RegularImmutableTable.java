package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table.Cell;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {

    class AnonymousClass1 implements Comparator<Cell<R, C, V>> {
        final /* synthetic */ Comparator val$columnComparator;
        final /* synthetic */ Comparator val$rowComparator;

        AnonymousClass1(Comparator comparator, Comparator comparator2) {
            this.val$rowComparator = comparator;
            this.val$columnComparator = comparator2;
        }

        public int compare(Cell<R, C, V> cell, Cell<R, C, V> cell2) {
            int i;
            if (this.val$rowComparator == null) {
                i = 0;
            } else {
                i = this.val$rowComparator.compare(cell.getRowKey(), cell2.getRowKey());
            }
            if (i != 0) {
                return i;
            }
            if (this.val$columnComparator != null) {
                return this.val$columnComparator.compare(cell.getColumnKey(), cell2.getColumnKey());
            }
            return 0;
        }
    }

    final class CellSet extends Indexed<Cell<R, C, V>> {
        private CellSet() {
        }

        public int size() {
            return RegularImmutableTable.this.size();
        }

        Cell<R, C, V> get(int i) {
            return RegularImmutableTable.this.getCell(i);
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Cell)) {
                return false;
            }
            Cell cell = (Cell) obj;
            Object obj2 = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
            if (obj2 == null || !obj2.equals(cell.getValue())) {
                return false;
            }
            return true;
        }

        boolean isPartialView() {
            return false;
        }
    }

    final class Values extends ImmutableList<V> {
        private Values() {
        }

        public int size() {
            return RegularImmutableTable.this.size();
        }

        public V get(int i) {
            return RegularImmutableTable.this.getValue(i);
        }

        boolean isPartialView() {
            return true;
        }
    }

    abstract Cell<R, C, V> getCell(int i);

    abstract V getValue(int i);

    RegularImmutableTable() {
    }

    final ImmutableSet<Cell<R, C, V>> createCellSet() {
        return isEmpty() ? ImmutableSet.of() : new CellSet();
    }

    final ImmutableCollection<V> createValues() {
        return isEmpty() ? ImmutableList.of() : new Values();
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Cell<R, C, V>> list, Comparator<? super R> comparator, Comparator<? super C> comparator2) {
        Preconditions.checkNotNull(list);
        if (!(comparator == null && comparator2 == null)) {
            Collections.sort(list, new AnonymousClass1(comparator, comparator2));
        }
        return forCellsInternal(list, comparator, comparator2);
    }

    private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Cell<R, C, V>> iterable, Comparator<? super R> comparator, Comparator<? super C> comparator2) {
        ImmutableSet copyOf;
        ImmutableSet copyOf2;
        Collection linkedHashSet = new LinkedHashSet();
        Collection linkedHashSet2 = new LinkedHashSet();
        ImmutableList copyOf3 = ImmutableList.copyOf((Iterable) iterable);
        for (Cell cell : iterable) {
            linkedHashSet.add(cell.getRowKey());
            linkedHashSet2.add(cell.getColumnKey());
        }
        if (comparator == null) {
            copyOf = ImmutableSet.copyOf(linkedHashSet);
        } else {
            copyOf = ImmutableSet.copyOf(ImmutableList.sortedCopyOf(comparator, linkedHashSet));
        }
        if (comparator2 == null) {
            copyOf2 = ImmutableSet.copyOf(linkedHashSet2);
        } else {
            copyOf2 = ImmutableSet.copyOf(ImmutableList.sortedCopyOf(comparator2, linkedHashSet2));
        }
        return forOrderedComponents(copyOf3, copyOf, copyOf2);
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(ImmutableList<Cell<R, C, V>> immutableList, ImmutableSet<R> immutableSet, ImmutableSet<C> immutableSet2) {
        if (((long) immutableList.size()) > (((long) immutableSet.size()) * ((long) immutableSet2.size())) / 2) {
            return new DenseImmutableTable(immutableList, immutableSet, immutableSet2);
        }
        return new SparseImmutableTable(immutableList, immutableSet, immutableSet2);
    }
}
