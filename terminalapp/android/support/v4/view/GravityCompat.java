package android.support.v4.view;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.Gravity;

public final class GravityCompat {
    static final GravityCompatImpl IMPL;

    interface GravityCompatImpl {
        void apply(int i, int i2, int i3, Rect rect, Rect rect2, int i4);

        int getAbsoluteGravity(int i, int i2);
    }

    static class GravityCompatImplBase implements GravityCompatImpl {
        GravityCompatImplBase() {
        }

        public int getAbsoluteGravity(int i, int i2) {
            return -8388609 & i;
        }

        public void apply(int i, int i2, int i3, Rect rect, Rect rect2, int i4) {
            Gravity.apply(i, i2, i3, rect, rect2);
        }
    }

    static class GravityCompatImplJellybeanMr1 implements GravityCompatImpl {
        GravityCompatImplJellybeanMr1() {
        }

        public int getAbsoluteGravity(int i, int i2) {
            return GravityCompatJellybeanMr1.getAbsoluteGravity(i, i2);
        }

        public void apply(int i, int i2, int i3, Rect rect, Rect rect2, int i4) {
            GravityCompatJellybeanMr1.apply(i, i2, i3, rect, rect2, i4);
        }
    }

    static {
        if (VERSION.SDK_INT >= 17) {
            IMPL = new GravityCompatImplJellybeanMr1();
        } else {
            IMPL = new GravityCompatImplBase();
        }
    }

    public static void apply(int i, int i2, int i3, Rect rect, Rect rect2, int i4) {
        IMPL.apply(i, i2, i3, rect, rect2, i4);
    }

    public static int getAbsoluteGravity(int i, int i2) {
        return IMPL.getAbsoluteGravity(i, i2);
    }

    private GravityCompat() {
    }
}
