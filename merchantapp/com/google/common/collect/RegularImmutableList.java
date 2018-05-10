package com.google.common.collect;

class RegularImmutableList<E> extends ImmutableList<E> {
    static final ImmutableList<Object> EMPTY = new RegularImmutableList(ObjectArrays.EMPTY_ARRAY);
    private final transient Object[] array;

    RegularImmutableList(Object[] objArr) {
        this.array = objArr;
    }

    public int size() {
        return this.array.length;
    }

    boolean isPartialView() {
        return false;
    }

    int copyIntoArray(Object[] objArr, int i) {
        System.arraycopy(this.array, 0, objArr, i, this.array.length);
        return this.array.length + i;
    }

    public E get(int i) {
        return this.array[i];
    }

    public UnmodifiableListIterator<E> listIterator(int i) {
        return Iterators.forArray(this.array, 0, this.array.length, i);
    }
}
