package com.google.common.base;

public final class Strings {
    private Strings() {
    }

    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    public static boolean isNullOrEmpty(String str) {
        return Platform.stringIsNullOrEmpty(str);
    }
}
