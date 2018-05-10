package com.google.common.util.concurrent;

public final class Futures extends GwtFuturesCatchingSpecialization {
    private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new AsyncFunction();

    private Futures() {
    }

    public static <V> ListenableFuture<V> immediateFuture(V v) {
        if (v == null) {
            return ImmediateSuccessfulFuture.NULL;
        }
        return new ImmediateSuccessfulFuture(v);
    }
}
