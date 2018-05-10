package android.support.v4.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.Interpolator;

class ViewPropertyAnimatorCompatICS {
    ViewPropertyAnimatorCompatICS() {
    }

    public static void setDuration(View view, long j) {
        view.animate().setDuration(j);
    }

    public static void alpha(View view, float f) {
        view.animate().alpha(f);
    }

    public static void translationX(View view, float f) {
        view.animate().translationX(f);
    }

    public static void translationY(View view, float f) {
        view.animate().translationY(f);
    }

    public static long getDuration(View view) {
        return view.animate().getDuration();
    }

    public static void setInterpolator(View view, Interpolator interpolator) {
        view.animate().setInterpolator(interpolator);
    }

    public static void setStartDelay(View view, long j) {
        view.animate().setStartDelay(j);
    }

    public static void cancel(View view) {
        view.animate().cancel();
    }

    public static void start(View view) {
        view.animate().start();
    }

    public static void setListener(final View view, final ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        if (viewPropertyAnimatorListener != null) {
            view.animate().setListener(new AnimatorListenerAdapter() {
                public void onAnimationCancel(Animator animator) {
                    viewPropertyAnimatorListener.onAnimationCancel(view);
                }

                public void onAnimationEnd(Animator animator) {
                    viewPropertyAnimatorListener.onAnimationEnd(view);
                }

                public void onAnimationStart(Animator animator) {
                    viewPropertyAnimatorListener.onAnimationStart(view);
                }
            });
        } else {
            view.animate().setListener(null);
        }
    }
}
