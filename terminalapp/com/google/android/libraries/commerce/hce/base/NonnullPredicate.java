package com.google.android.libraries.commerce.hce.base;

import com.google.common.base.Predicate;

public abstract class NonnullPredicate<T> implements Predicate<T> {
    public abstract boolean applyNonnull(T t);

    public boolean apply(T t) {
        return applyNonnull(Nonnulls.nonnull(t));
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }
}
