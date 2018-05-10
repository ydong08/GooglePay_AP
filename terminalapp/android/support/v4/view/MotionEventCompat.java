package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.MotionEvent;

public final class MotionEventCompat {
    static final MotionEventVersionImpl IMPL;

    interface MotionEventVersionImpl {
        MotionEventVersionImpl() {
        }

        float getAxisValue(MotionEvent motionEvent, int i) {
            return 0.0f;
        }
    }

    static class HoneycombMr1MotionEventVersionImpl extends MotionEventVersionImpl {
        HoneycombMr1MotionEventVersionImpl() {
        }

        public float getAxisValue(MotionEvent motionEvent, int i) {
            return MotionEventCompatHoneycombMr1.getAxisValue(motionEvent, i);
        }

        HoneycombMr1MotionEventVersionImpl(byte b) {
            this();
        }
    }

    static {
        if (VERSION.SDK_INT >= 14) {
            IMPL = new HoneycombMr1MotionEventVersionImpl((byte) 0);
        } else if (VERSION.SDK_INT >= 12) {
            IMPL = new HoneycombMr1MotionEventVersionImpl();
        } else {
            IMPL = new MotionEventVersionImpl();
        }
    }

    public static int getActionMasked(MotionEvent motionEvent) {
        return motionEvent.getAction() & 255;
    }

    public static int getActionIndex(MotionEvent motionEvent) {
        return (motionEvent.getAction() & 65280) >> 8;
    }

    @Deprecated
    public static int findPointerIndex(MotionEvent motionEvent, int i) {
        return motionEvent.findPointerIndex(i);
    }

    @Deprecated
    public static int getPointerId(MotionEvent motionEvent, int i) {
        return motionEvent.getPointerId(i);
    }

    @Deprecated
    public static float getX(MotionEvent motionEvent, int i) {
        return motionEvent.getX(i);
    }

    public static float getAxisValue(MotionEvent motionEvent, int i) {
        return IMPL.getAxisValue(motionEvent, i);
    }

    private MotionEventCompat() {
    }
}
