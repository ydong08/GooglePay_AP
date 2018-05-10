package com.google.common.collect;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

abstract class ImmutableAsList<E> extends ImmutableList<E> {

    static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableCollection<?> collection;

        SerializedForm(ImmutableCollection<?> immutableCollection) {
            this.collection = immutableCollection;
        }

        Object readResolve() {
            return this.collection.asList();
        }
    }

    abstract ImmutableCollection<E> delegateCollection();

    ImmutableAsList() {
    }

    public boolean contains(Object obj) {
        return delegateCollection().contains(obj);
    }

    public int size() {
        return delegateCollection().size();
    }

    public boolean isEmpty() {
        return delegateCollection().isEmpty();
    }

    boolean isPartialView() {
        return delegateCollection().isPartialView();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    Object writeReplace() {
        return new SerializedForm(delegateCollection());
    }
}
