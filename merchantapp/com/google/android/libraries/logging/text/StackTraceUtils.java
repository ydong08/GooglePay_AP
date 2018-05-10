package com.google.android.libraries.logging.text;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

final class StackTraceUtils {
    private StackTraceUtils() {
    }

    static String getCallerClassName(Throwable th, int i) {
        boolean z = true;
        Preconditions.checkNotNull(th, "null throwable");
        Preconditions.checkArgument(i >= 0, "skipFrameCount must be >= 0, was: [%s]", i);
        StackTraceElement[] stackTrace = th.getStackTrace();
        if (stackTrace.length < i + 1) {
            z = false;
        }
        Preconditions.checkState(z, "Unexpected stack trace length (should be >= %s): [%s]", i + 1, stackTrace.length);
        return stackTrace[i].getClassName();
    }

    static String getSimpleClassName(String str) {
        String substring;
        boolean z = true;
        Preconditions.checkArgument(!Strings.isNullOrEmpty(str), "null or empty fullClassName");
        if (str.contains(".")) {
            substring = str.substring(str.lastIndexOf(46) + 1);
        } else {
            substring = str;
        }
        if (substring.length() <= 0) {
            z = false;
        }
        Preconditions.checkArgument(z, "empty simple class name for : [%s]", (Object) str);
        return substring;
    }
}
