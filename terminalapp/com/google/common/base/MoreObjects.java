package com.google.common.base;

public final class MoreObjects {
    public static <T> T firstNonNull(T t, T t2) {
        return t != null ? t : Preconditions.checkNotNull(t2);
    }

    private MoreObjects() {
    }
}
