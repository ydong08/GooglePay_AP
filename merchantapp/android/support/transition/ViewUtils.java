package android.support.transition;

import android.os.Build.VERSION;
import android.view.View;

class ViewUtils {
    private static final ViewUtilsImpl IMPL;

    ViewUtils() {
    }

    static {
        if (VERSION.SDK_INT >= 18) {
            IMPL = new ViewUtilsApi18();
        } else {
            IMPL = new ViewUtilsImpl();
        }
    }

    static ViewOverlayImpl getOverlay(View view) {
        return IMPL.getOverlay(view);
    }

    static WindowIdImpl getWindowId(View view) {
        return IMPL.getWindowId(view);
    }
}
