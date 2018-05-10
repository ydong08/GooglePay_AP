package android.support.transition;

import android.os.Build.VERSION;
import android.view.ViewGroup;

class ViewGroupUtils {
    private static final ViewGroupUtilsImpl IMPL;

    ViewGroupUtils() {
    }

    static {
        if (VERSION.SDK_INT >= 19) {
            IMPL = new ViewGroupUtilsApi19();
        } else if (VERSION.SDK_INT >= 18) {
            IMPL = new ViewGroupUtilsApi18();
        } else {
            IMPL = new ViewGroupUtilsImpl();
        }
    }

    static ViewGroupOverlayImpl getOverlay(ViewGroup viewGroup) {
        return IMPL.getOverlay(viewGroup);
    }

    static void suppressLayout(ViewGroup viewGroup, boolean z) {
        IMPL.suppressLayout(viewGroup, z);
    }
}
