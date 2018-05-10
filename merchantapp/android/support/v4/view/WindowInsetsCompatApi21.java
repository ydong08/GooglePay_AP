package android.support.v4.view;

import android.view.WindowInsets;

class WindowInsetsCompatApi21 {
    WindowInsetsCompatApi21() {
    }

    public static boolean isConsumed(Object obj) {
        return ((WindowInsets) obj).isConsumed();
    }
}
