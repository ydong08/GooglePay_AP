package android.support.v4.view;

import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewParent;

class ViewCompatHC {
    ViewCompatHC() {
    }

    static long getFrameTime() {
        return ValueAnimator.getFrameDelay();
    }

    public static float getAlpha(View view) {
        return view.getAlpha();
    }

    public static void setLayerType(View view, int i, Paint paint) {
        view.setLayerType(i, paint);
    }

    public static int getLayerType(View view) {
        return view.getLayerType();
    }

    public static int resolveSizeAndState(int i, int i2, int i3) {
        return View.resolveSizeAndState(i, i2, i3);
    }

    public static int getMeasuredWidthAndState(View view) {
        return view.getMeasuredWidthAndState();
    }

    public static int getMeasuredState(View view) {
        return view.getMeasuredState();
    }

    public static float getTranslationX(View view) {
        return view.getTranslationX();
    }

    public static float getTranslationY(View view) {
        return view.getTranslationY();
    }

    public static float getScaleX(View view) {
        return view.getScaleX();
    }

    public static void setTranslationX(View view, float f) {
        view.setTranslationX(f);
    }

    public static void setTranslationY(View view, float f) {
        view.setTranslationY(f);
    }

    public static Matrix getMatrix(View view) {
        return view.getMatrix();
    }

    public static void setAlpha(View view, float f) {
        view.setAlpha(f);
    }

    public static void setScaleX(View view, float f) {
        view.setScaleX(f);
    }

    public static void setScaleY(View view, float f) {
        view.setScaleY(f);
    }

    public static void setPivotX(View view, float f) {
        view.setPivotX(f);
    }

    public static void setPivotY(View view, float f) {
        view.setPivotY(f);
    }

    public static void jumpDrawablesToCurrentState(View view) {
        view.jumpDrawablesToCurrentState();
    }

    public static void setSaveFromParentEnabled(View view, boolean z) {
        view.setSaveFromParentEnabled(z);
    }

    public static void setActivated(View view, boolean z) {
        view.setActivated(z);
    }

    public static int combineMeasuredStates(int i, int i2) {
        return View.combineMeasuredStates(i, i2);
    }

    static void offsetTopAndBottom(View view, int i) {
        view.offsetTopAndBottom(i);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                tickleInvalidationFlag((View) parent);
            }
        }
    }

    static void offsetLeftAndRight(View view, int i) {
        view.offsetLeftAndRight(i);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                tickleInvalidationFlag((View) parent);
            }
        }
    }

    private static void tickleInvalidationFlag(View view) {
        float translationY = view.getTranslationY();
        view.setTranslationY(1.0f + translationY);
        view.setTranslationY(translationY);
    }
}
