package android.support.v4.view;

import android.view.View;
import android.view.View.AccessibilityDelegate;

class ViewCompatICS {
    ViewCompatICS() {
    }

    public static boolean canScrollHorizontally(View view, int i) {
        return view.canScrollHorizontally(i);
    }

    public static boolean canScrollVertically(View view, int i) {
        return view.canScrollVertically(i);
    }

    public static void setAccessibilityDelegate(View view, Object obj) {
        view.setAccessibilityDelegate((AccessibilityDelegate) obj);
    }

    public static void setFitsSystemWindows(View view, boolean z) {
        view.setFitsSystemWindows(z);
    }
}
