package com.google.android.libraries.commerce.hce.base;

public class Nonnulls {
    public static <T> T nonnull(T t, String str) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(str);
    }

    public static <T> T nonnull(T t) {
        return nonnull(t, "Parameter is null.");
    }
}
