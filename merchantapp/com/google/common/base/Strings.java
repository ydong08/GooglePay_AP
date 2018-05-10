package com.google.common.base;

public final class Strings {
    private Strings() {
    }

    public static boolean isNullOrEmpty(String str) {
        return Platform.stringIsNullOrEmpty(str);
    }
}
