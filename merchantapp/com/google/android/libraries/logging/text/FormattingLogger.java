package com.google.android.libraries.logging.text;

public interface FormattingLogger {
    void d(String str, Object... objArr);

    void d(Throwable th, String str, Object... objArr);

    void e(String str, Object... objArr);

    void e(Throwable th, String str, Object... objArr);

    void i(String str, Object... objArr);

    void w(String str, Object... objArr);

    void w(Throwable th, String str, Object... objArr);
}
