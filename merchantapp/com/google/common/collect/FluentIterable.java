package com.google.common.collect;

import com.google.common.base.Optional;

public abstract class FluentIterable<E> implements Iterable<E> {
    private final Optional<Iterable<E>> iterableDelegate = Optional.absent();

    protected FluentIterable() {
    }

    private Iterable<E> getDelegate() {
        return (Iterable) this.iterableDelegate.or(this);
    }

    public String toString() {
        return Iterables.toString(getDelegate());
    }
}
