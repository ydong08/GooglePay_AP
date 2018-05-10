package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityEvent;

public final class AccessibilityEventCompat {
    private static final AccessibilityEventVersionImpl IMPL;

    interface AccessibilityEventVersionImpl {
        AccessibilityEventVersionImpl() {
        }

        void setContentChangeTypes(AccessibilityEvent accessibilityEvent, int i) {
        }

        int getContentChangeTypes(AccessibilityEvent accessibilityEvent) {
            return 0;
        }

        AccessibilityEventVersionImpl(byte b) {
            this();
        }

        AccessibilityEventVersionImpl(char c) {
            this((byte) 0);
        }
    }

    static class AccessibilityEventKitKatImpl extends AccessibilityEventVersionImpl {
        AccessibilityEventKitKatImpl() {
            super('\u0000');
        }

        public void setContentChangeTypes(AccessibilityEvent accessibilityEvent, int i) {
            AccessibilityEventCompatKitKat.setContentChangeTypes(accessibilityEvent, i);
        }

        public int getContentChangeTypes(AccessibilityEvent accessibilityEvent) {
            return AccessibilityEventCompatKitKat.getContentChangeTypes(accessibilityEvent);
        }
    }

    static {
        if (VERSION.SDK_INT >= 19) {
            IMPL = new AccessibilityEventKitKatImpl();
        } else if (VERSION.SDK_INT >= 16) {
            IMPL = new AccessibilityEventVersionImpl('\u0000');
        } else if (VERSION.SDK_INT >= 14) {
            IMPL = new AccessibilityEventVersionImpl((byte) 0);
        } else {
            IMPL = new AccessibilityEventVersionImpl();
        }
    }

    private AccessibilityEventCompat() {
    }

    public static AccessibilityRecordCompat asRecord(AccessibilityEvent accessibilityEvent) {
        return new AccessibilityRecordCompat(accessibilityEvent);
    }

    public static void setContentChangeTypes(AccessibilityEvent accessibilityEvent, int i) {
        IMPL.setContentChangeTypes(accessibilityEvent, i);
    }

    public static int getContentChangeTypes(AccessibilityEvent accessibilityEvent) {
        return IMPL.getContentChangeTypes(accessibilityEvent);
    }
}
