package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;

final class ReverseOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final Ordering<? super T> forwardOrder;

    ReverseOrdering(Ordering<? super T> ordering) {
        this.forwardOrder = (Ordering) Preconditions.checkNotNull(ordering);
    }

    public int compare(T t, T t2) {
        return this.forwardOrder.compare(t2, t);
    }

    public <S extends T> Ordering<S> reverse() {
        return this.forwardOrder;
    }

    public int hashCode() {
        return -this.forwardOrder.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ReverseOrdering)) {
            return false;
        }
        return this.forwardOrder.equals(((ReverseOrdering) obj).forwardOrder);
    }

    public String toString() {
        String valueOf = String.valueOf(this.forwardOrder);
        return new StringBuilder(String.valueOf(valueOf).length() + 10).append(valueOf).append(".reverse()").toString();
    }
}
