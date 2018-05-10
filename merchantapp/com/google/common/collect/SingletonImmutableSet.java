package com.google.common.collect;

import com.google.common.base.Preconditions;

final class SingletonImmutableSet<E> extends ImmutableSet<E> {
    private transient int cachedHashCode;
    final transient E element;

    SingletonImmutableSet(E e) {
        this.element = Preconditions.checkNotNull(e);
    }

    SingletonImmutableSet(E e, int i) {
        this.element = e;
        this.cachedHashCode = i;
    }

    public int size() {
        return 1;
    }

    public boolean contains(Object obj) {
        return this.element.equals(obj);
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    ImmutableList<E> createAsList() {
        return ImmutableList.of(this.element);
    }

    boolean isPartialView() {
        return false;
    }

    int copyIntoArray(Object[] objArr, int i) {
        objArr[i] = this.element;
        return i + 1;
    }

    public final int hashCode() {
        int i = this.cachedHashCode;
        if (i != 0) {
            return i;
        }
        i = this.element.hashCode();
        this.cachedHashCode = i;
        return i;
    }

    boolean isHashCodeFast() {
        return this.cachedHashCode != 0;
    }

    public String toString() {
        String valueOf = String.valueOf(this.element.toString());
        return new StringBuilder(String.valueOf(valueOf).length() + 2).append("[").append(valueOf).append("]").toString();
    }
}
