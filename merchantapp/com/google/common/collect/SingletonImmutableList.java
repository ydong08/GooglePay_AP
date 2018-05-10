package com.google.common.collect;

import com.google.common.base.Preconditions;

final class SingletonImmutableList<E> extends ImmutableList<E> {
    final transient E element;

    SingletonImmutableList(E e) {
        this.element = Preconditions.checkNotNull(e);
    }

    public E get(int i) {
        Preconditions.checkElementIndex(i, 1);
        return this.element;
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    public int size() {
        return 1;
    }

    public ImmutableList<E> subList(int i, int i2) {
        Preconditions.checkPositionIndexes(i, i2, 1);
        return i == i2 ? ImmutableList.of() : this;
    }

    public String toString() {
        String valueOf = String.valueOf(this.element.toString());
        return new StringBuilder(String.valueOf(valueOf).length() + 2).append("[").append(valueOf).append("]").toString();
    }

    boolean isPartialView() {
        return false;
    }
}
