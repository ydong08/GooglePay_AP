package android.support.v4.view;

import android.view.WindowInsets;

class WindowInsetsCompatApi20 {
    WindowInsetsCompatApi20() {
    }

    public static Object consumeSystemWindowInsets(Object obj) {
        return ((WindowInsets) obj).consumeSystemWindowInsets();
    }

    public static int getSystemWindowInsetBottom(Object obj) {
        return ((WindowInsets) obj).getSystemWindowInsetBottom();
    }

    public static int getSystemWindowInsetLeft(Object obj) {
        return ((WindowInsets) obj).getSystemWindowInsetLeft();
    }

    public static int getSystemWindowInsetRight(Object obj) {
        return ((WindowInsets) obj).getSystemWindowInsetRight();
    }

    public static int getSystemWindowInsetTop(Object obj) {
        return ((WindowInsets) obj).getSystemWindowInsetTop();
    }

    public static boolean hasSystemWindowInsets(Object obj) {
        return ((WindowInsets) obj).hasSystemWindowInsets();
    }

    public static Object replaceSystemWindowInsets(Object obj, int i, int i2, int i3, int i4) {
        return ((WindowInsets) obj).replaceSystemWindowInsets(i, i2, i3, i4);
    }
}
