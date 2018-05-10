package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.ViewGroup.MarginLayoutParams;

public final class MarginLayoutParamsCompat {
    static final MarginLayoutParamsCompatImpl IMPL;

    interface MarginLayoutParamsCompatImpl {
        int getMarginEnd(MarginLayoutParams marginLayoutParams);

        int getMarginStart(MarginLayoutParams marginLayoutParams);
    }

    static class MarginLayoutParamsCompatImplBase implements MarginLayoutParamsCompatImpl {
        MarginLayoutParamsCompatImplBase() {
        }

        public int getMarginStart(MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.leftMargin;
        }

        public int getMarginEnd(MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.rightMargin;
        }
    }

    static class MarginLayoutParamsCompatImplJbMr1 implements MarginLayoutParamsCompatImpl {
        MarginLayoutParamsCompatImplJbMr1() {
        }

        public int getMarginStart(MarginLayoutParams marginLayoutParams) {
            return MarginLayoutParamsCompatJellybeanMr1.getMarginStart(marginLayoutParams);
        }

        public int getMarginEnd(MarginLayoutParams marginLayoutParams) {
            return MarginLayoutParamsCompatJellybeanMr1.getMarginEnd(marginLayoutParams);
        }
    }

    static {
        if (VERSION.SDK_INT >= 17) {
            IMPL = new MarginLayoutParamsCompatImplJbMr1();
        } else {
            IMPL = new MarginLayoutParamsCompatImplBase();
        }
    }

    public static int getMarginStart(MarginLayoutParams marginLayoutParams) {
        return IMPL.getMarginStart(marginLayoutParams);
    }

    public static int getMarginEnd(MarginLayoutParams marginLayoutParams) {
        return IMPL.getMarginEnd(marginLayoutParams);
    }

    private MarginLayoutParamsCompat() {
    }
}
