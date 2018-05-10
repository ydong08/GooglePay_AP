package com.google.common.collect;

import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;

final class ImmutableEnumSet<E extends Enum<E>> extends ImmutableSet<E> {
    private final transient EnumSet<E> delegate;
    private transient int hashCode;

    static class EnumSerializedForm<E extends Enum<E>> implements Serializable {
        private static final long serialVersionUID = 0;
        final EnumSet<E> delegate;

        EnumSerializedForm(EnumSet<E> enumSet) {
            this.delegate = enumSet;
        }

        Object readResolve() {
            return new ImmutableEnumSet(this.delegate.clone());
        }
    }

    static ImmutableSet asImmutable(EnumSet enumSet) {
        switch (enumSet.size()) {
            case 0:
                return ImmutableSet.of();
            case 1:
                return ImmutableSet.of(Iterables.getOnlyElement(enumSet));
            default:
                return new ImmutableEnumSet(enumSet);
        }
    }

    private ImmutableEnumSet(EnumSet<E> enumSet) {
        this.delegate = enumSet;
    }

    boolean isPartialView() {
        return false;
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.unmodifiableIterator(this.delegate.iterator());
    }

    public int size() {
        return this.delegate.size();
    }

    public boolean contains(Object obj) {
        return this.delegate.contains(obj);
    }

    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof ImmutableEnumSet) {
            collection = ((ImmutableEnumSet) collection).delegate;
        }
        return this.delegate.containsAll(collection);
    }

    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ImmutableEnumSet) {
            obj = ((ImmutableEnumSet) obj).delegate;
        }
        return this.delegate.equals(obj);
    }

    boolean isHashCodeFast() {
        return true;
    }

    public int hashCode() {
        int i = this.hashCode;
        if (i != 0) {
            return i;
        }
        i = this.delegate.hashCode();
        this.hashCode = i;
        return i;
    }

    public String toString() {
        return this.delegate.toString();
    }

    Object writeReplace() {
        return new EnumSerializedForm(this.delegate);
    }
}
