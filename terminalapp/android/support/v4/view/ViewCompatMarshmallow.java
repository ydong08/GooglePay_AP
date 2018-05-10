package android.support.v4.view;

import android.view.View;

class ViewCompatMarshmallow {
    ViewCompatMarshmallow() {
    }

    public static void setScrollIndicators(View view, int i, int i2) {
        view.setScrollIndicators(i, i2);
    }

    static void offsetTopAndBottom(View view, int i) {
        view.offsetTopAndBottom(i);
    }

    static void offsetLeftAndRight(View view, int i) {
        view.offsetLeftAndRight(i);
    }
}
