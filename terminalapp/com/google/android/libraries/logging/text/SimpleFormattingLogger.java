package com.google.android.libraries.logging.text;

import android.os.Build;
import android.util.Log;
import com.google.common.base.Preconditions;
import java.util.IllegalFormatException;

final class SimpleFormattingLogger implements FormattingLogger {
    private static final boolean IS_USER_BUILD = "user".equals(Build.TYPE);
    final String systemProperty;
    private final String tag;

    SimpleFormattingLogger(String str, String str2) {
        this.tag = (String) Preconditions.checkNotNull(str, "null tag");
        Preconditions.checkArgument(str2.length() <= 23, "systemProperty is too long: [%s]", (Object) str2);
        this.systemProperty = str2;
    }

    public void d(String str, Object... objArr) {
        logIfAllowed(3, this.systemProperty, this.tag, null, str, objArr);
    }

    public void d(Throwable th, String str, Object... objArr) {
        logIfAllowed(3, this.systemProperty, this.tag, th, str, objArr);
    }

    public void i(String str, Object... objArr) {
        logIfAllowed(4, this.systemProperty, this.tag, null, str, objArr);
    }

    public void w(String str, Object... objArr) {
        logIfAllowed(5, this.systemProperty, this.tag, null, str, objArr);
    }

    public void w(Throwable th, String str, Object... objArr) {
        logIfAllowed(5, this.systemProperty, this.tag, th, str, objArr);
    }

    public void e(String str, Object... objArr) {
        logIfAllowed(6, this.systemProperty, this.tag, null, str, objArr);
    }

    public void e(Throwable th, String str, Object... objArr) {
        logIfAllowed(6, this.systemProperty, this.tag, th, str, objArr);
    }

    private static boolean isLoggable(String str, int i) {
        if (IS_USER_BUILD) {
            return Log.isLoggable(str, i);
        }
        if (i >= 3) {
            return true;
        }
        return Log.isLoggable(str, i);
    }

    private static void logIfAllowed(int i, String str, String str2, Throwable th, String str3, Object... objArr) {
        if (isLoggable(str, i)) {
            formatAndLogMessage(i, str2, th, str3, objArr);
        }
    }

    private static void formatAndLogMessage(int i, String str, Throwable th, String str2, Object... objArr) {
        if (objArr != null) {
            try {
                if (objArr.length != 0) {
                    str2 = String.format(str2, objArr);
                }
            } catch (IllegalFormatException e) {
                Log.e("SimpleFormattingLogger", String.format("Error formatting message. format: [%s], args: [%s]", new Object[]{str2, objArr}));
                throw e;
            }
        }
        switch (i) {
            case 2:
                if (th != null) {
                    Log.v(str, str2, th);
                    return;
                } else {
                    Log.v(str, str2);
                    return;
                }
            case 3:
                if (th != null) {
                    Log.d(str, str2, th);
                    return;
                } else {
                    Log.d(str, str2);
                    return;
                }
            case 4:
                if (th != null) {
                    Log.i(str, str2, th);
                    return;
                } else {
                    Log.i(str, str2);
                    return;
                }
            case 5:
                if (th != null) {
                    Log.w(str, str2, th);
                    return;
                } else {
                    Log.w(str, str2);
                    return;
                }
            case 6:
                if (th != null) {
                    Log.e(str, str2, th);
                    return;
                } else {
                    Log.e(str, str2);
                    return;
                }
            default:
                return;
        }
    }
}
