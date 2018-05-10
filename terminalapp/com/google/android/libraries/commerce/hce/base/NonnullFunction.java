package com.google.android.libraries.commerce.hce.base;

import com.google.common.base.Function;

public abstract class NonnullFunction<F, T> implements Function<F, T> {
    public abstract T applyNonnull(F f);

    public T apply(F f) {
        return applyNonnull(Nonnulls.nonnull(f));
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }
}
