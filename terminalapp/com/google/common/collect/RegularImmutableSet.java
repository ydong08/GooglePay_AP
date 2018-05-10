package com.google.common.collect;

final class RegularImmutableSet<E> extends Indexed<E> {
    static final RegularImmutableSet<Object> EMPTY = new RegularImmutableSet(ObjectArrays.EMPTY_ARRAY, 0, null, 0);
    private final transient Object[] elements;
    private final transient int hashCode;
    private final transient int mask;
    final transient Object[] table;

    RegularImmutableSet(Object[] objArr, int i, Object[] objArr2, int i2) {
        this.elements = objArr;
        this.table = objArr2;
        this.mask = i2;
        this.hashCode = i;
    }

    public boolean contains(Object obj) {
        Object[] objArr = this.table;
        if (obj == null || objArr == null) {
            return false;
        }
        int smearedHash = Hashing.smearedHash(obj);
        while (true) {
            smearedHash &= this.mask;
            Object obj2 = objArr[smearedHash];
            if (obj2 == null) {
                return false;
            }
            if (obj2.equals(obj)) {
                return true;
            }
            smearedHash++;
        }
    }

    public int size() {
        return this.elements.length;
    }

    E get(int i) {
        return this.elements[i];
    }

    int copyIntoArray(Object[] objArr, int i) {
        System.arraycopy(this.elements, 0, objArr, i, this.elements.length);
        return this.elements.length + i;
    }

    ImmutableList<E> createAsList() {
        return this.table == null ? ImmutableList.of() : new RegularImmutableAsList((ImmutableCollection) this, this.elements);
    }

    boolean isPartialView() {
        return false;
    }

    public int hashCode() {
        return this.hashCode;
    }

    boolean isHashCodeFast() {
        return true;
    }
}
