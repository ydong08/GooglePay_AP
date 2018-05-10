package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.ViewGroup;

public final class ViewGroupCompat {
    static final ViewGroupCompatImpl IMPL;

    interface ViewGroupCompatImpl {
        ViewGroupCompatImpl() {
        }

        void setMotionEventSplittingEnabled(ViewGroup viewGroup, boolean z) {
        }
    }

    static class ViewGroupCompatHCImpl extends ViewGroupCompatImpl {
        ViewGroupCompatHCImpl() {
        }

        public void setMotionEventSplittingEnabled(ViewGroup viewGroup, boolean z) {
            ViewGroupCompatHC.setMotionEventSplittingEnabled(viewGroup, z);
        }

        ViewGroupCompatHCImpl(byte b) {
            this();
        }
    }

    static class ViewGroupCompatJellybeanMR2Impl extends ViewGroupCompatHCImpl {
        ViewGroupCompatJellybeanMR2Impl() {
            super((byte) 0);
        }

        ViewGroupCompatJellybeanMR2Impl(byte b) {
            this();
        }
    }

    static {
        int i = VERSION.SDK_INT;
        if (i >= 21) {
            IMPL = new ViewGroupCompatJellybeanMR2Impl((byte) 0);
        } else if (i >= 18) {
            IMPL = new ViewGroupCompatJellybeanMR2Impl();
        } else if (i >= 14) {
            IMPL = new ViewGroupCompatHCImpl((byte) 0);
        } else if (i >= 11) {
            IMPL = new ViewGroupCompatHCImpl();
        } else {
            IMPL = new ViewGroupCompatImpl();
        }
    }

    private ViewGroupCompat() {
    }

    public static void setMotionEventSplittingEnabled(ViewGroup viewGroup, boolean z) {
        IMPL.setMotionEventSplittingEnabled(viewGroup, z);
    }
}
