package com.google.android.libraries.logging.text;

import com.google.common.base.Preconditions;

public final class FormattingLoggers {
    public static FormattingLogger newContextLogger() {
        return newForTag(getCallerClassSimpleName());
    }

    public static FormattingLogger newForTag(String str) {
        Preconditions.checkNotNull(str, "null tag");
        return new SimpleFormattingLogger(str, cropPropertyKey(str));
    }

    private static String getCallerClassSimpleName() {
        return StackTraceUtils.getSimpleClassName(StackTraceUtils.getCallerClassName(new Throwable(), 2));
    }

    private static String cropPropertyKey(String str) {
        if (str.length() > 23) {
            return str.substring(0, 23);
        }
        return str;
    }
}
