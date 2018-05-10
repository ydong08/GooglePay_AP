package android.support.v4.view;

import android.os.Build.VERSION;

public class WindowInsetsCompat {
    private static final WindowInsetsCompatImpl IMPL;
    private final Object mInsets;

    interface WindowInsetsCompatImpl {
        WindowInsetsCompatImpl() {
        }

        int getSystemWindowInsetLeft(Object obj) {
            return 0;
        }

        int getSystemWindowInsetTop(Object obj) {
            return 0;
        }

        int getSystemWindowInsetRight(Object obj) {
            return 0;
        }

        int getSystemWindowInsetBottom(Object obj) {
            return 0;
        }

        boolean hasSystemWindowInsets(Object obj) {
            return false;
        }

        boolean isConsumed(Object obj) {
            return false;
        }

        WindowInsetsCompat consumeSystemWindowInsets(Object obj) {
            return null;
        }

        WindowInsetsCompat replaceSystemWindowInsets(Object obj, int i, int i2, int i3, int i4) {
            return null;
        }
    }

    static class WindowInsetsCompatApi20Impl extends WindowInsetsCompatImpl {
        WindowInsetsCompatApi20Impl() {
        }

        public WindowInsetsCompat consumeSystemWindowInsets(Object obj) {
            return new WindowInsetsCompat(WindowInsetsCompatApi20.consumeSystemWindowInsets(obj));
        }

        public int getSystemWindowInsetBottom(Object obj) {
            return WindowInsetsCompatApi20.getSystemWindowInsetBottom(obj);
        }

        public int getSystemWindowInsetLeft(Object obj) {
            return WindowInsetsCompatApi20.getSystemWindowInsetLeft(obj);
        }

        public int getSystemWindowInsetRight(Object obj) {
            return WindowInsetsCompatApi20.getSystemWindowInsetRight(obj);
        }

        public int getSystemWindowInsetTop(Object obj) {
            return WindowInsetsCompatApi20.getSystemWindowInsetTop(obj);
        }

        public boolean hasSystemWindowInsets(Object obj) {
            return WindowInsetsCompatApi20.hasSystemWindowInsets(obj);
        }

        public WindowInsetsCompat replaceSystemWindowInsets(Object obj, int i, int i2, int i3, int i4) {
            return new WindowInsetsCompat(WindowInsetsCompatApi20.replaceSystemWindowInsets(obj, i, i2, i3, i4));
        }
    }

    static class WindowInsetsCompatApi21Impl extends WindowInsetsCompatApi20Impl {
        WindowInsetsCompatApi21Impl() {
        }

        public boolean isConsumed(Object obj) {
            return WindowInsetsCompatApi21.isConsumed(obj);
        }
    }

    static {
        int i = VERSION.SDK_INT;
        if (i >= 21) {
            IMPL = new WindowInsetsCompatApi21Impl();
        } else if (i >= 20) {
            IMPL = new WindowInsetsCompatApi20Impl();
        } else {
            IMPL = new WindowInsetsCompatImpl();
        }
    }

    WindowInsetsCompat(Object obj) {
        this.mInsets = obj;
    }

    public int getSystemWindowInsetLeft() {
        return IMPL.getSystemWindowInsetLeft(this.mInsets);
    }

    public int getSystemWindowInsetTop() {
        return IMPL.getSystemWindowInsetTop(this.mInsets);
    }

    public int getSystemWindowInsetRight() {
        return IMPL.getSystemWindowInsetRight(this.mInsets);
    }

    public int getSystemWindowInsetBottom() {
        return IMPL.getSystemWindowInsetBottom(this.mInsets);
    }

    public boolean hasSystemWindowInsets() {
        return IMPL.hasSystemWindowInsets(this.mInsets);
    }

    public boolean isConsumed() {
        return IMPL.isConsumed(this.mInsets);
    }

    public WindowInsetsCompat consumeSystemWindowInsets() {
        return IMPL.consumeSystemWindowInsets(this.mInsets);
    }

    public WindowInsetsCompat replaceSystemWindowInsets(int i, int i2, int i3, int i4) {
        return IMPL.replaceSystemWindowInsets(this.mInsets, i, i2, i3, i4);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        WindowInsetsCompat windowInsetsCompat = (WindowInsetsCompat) obj;
        if (this.mInsets != null) {
            return this.mInsets.equals(windowInsetsCompat.mInsets);
        }
        if (windowInsetsCompat.mInsets != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.mInsets == null ? 0 : this.mInsets.hashCode();
    }

    static WindowInsetsCompat wrap(Object obj) {
        return obj == null ? null : new WindowInsetsCompat(obj);
    }

    static Object unwrap(WindowInsetsCompat windowInsetsCompat) {
        return windowInsetsCompat == null ? null : windowInsetsCompat.mInsets;
    }
}
