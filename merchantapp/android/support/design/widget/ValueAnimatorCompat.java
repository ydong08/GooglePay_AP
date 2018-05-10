package android.support.design.widget;

import android.os.Build.VERSION;
import android.view.animation.Interpolator;

class ValueAnimatorCompat {
    private final Impl mImpl;

    interface AnimatorUpdateListener {
        void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat);
    }

    interface AnimatorListener {
        AnimatorListener() {
        }

        void onAnimationStart(ValueAnimatorCompat valueAnimatorCompat) {
        }

        void onAnimationEnd(ValueAnimatorCompat valueAnimatorCompat) {
        }
    }

    interface Creator {
        Creator() {
        }

        ValueAnimatorCompat createAnimator() {
            return new ValueAnimatorCompat(VERSION.SDK_INT >= 12 ? new ValueAnimatorCompatImplHoneycombMr1() : new ValueAnimatorCompatImplGingerbread());
        }
    }

    static abstract class Impl {

        interface AnimatorListenerProxy {
            final /* synthetic */ ValueAnimatorCompat this$0;
            final /* synthetic */ AnimatorListener val$listener;

            AnimatorListenerProxy(ValueAnimatorCompat valueAnimatorCompat, AnimatorListener animatorListener) {
                this.this$0 = valueAnimatorCompat;
                this.val$listener = animatorListener;
            }

            void onAnimationStart() {
                this.val$listener.onAnimationStart(this.this$0);
            }

            void onAnimationEnd() {
                this.val$listener.onAnimationEnd(this.this$0);
            }
        }

        interface AnimatorUpdateListenerProxy {
            final /* synthetic */ ValueAnimatorCompat this$0;
            final /* synthetic */ AnimatorUpdateListener val$updateListener;

            AnimatorUpdateListenerProxy(ValueAnimatorCompat valueAnimatorCompat, AnimatorUpdateListener animatorUpdateListener) {
                this.this$0 = valueAnimatorCompat;
                this.val$updateListener = animatorUpdateListener;
            }

            void onAnimationUpdate() {
                this.val$updateListener.onAnimationUpdate(this.this$0);
            }
        }

        abstract void addListener(AnimatorListenerProxy animatorListenerProxy);

        abstract void addUpdateListener(AnimatorUpdateListenerProxy animatorUpdateListenerProxy);

        abstract void cancel();

        abstract void end();

        abstract float getAnimatedFloatValue();

        abstract float getAnimatedFraction();

        abstract int getAnimatedIntValue();

        abstract long getDuration();

        abstract boolean isRunning();

        abstract void setDuration(long j);

        abstract void setFloatValues(float f, float f2);

        abstract void setIntValues(int i, int i2);

        abstract void setInterpolator(Interpolator interpolator);

        abstract void start();

        Impl() {
        }
    }

    ValueAnimatorCompat(Impl impl) {
        this.mImpl = impl;
    }

    public void start() {
        this.mImpl.start();
    }

    public boolean isRunning() {
        return this.mImpl.isRunning();
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mImpl.setInterpolator(interpolator);
    }

    public void addUpdateListener(AnimatorUpdateListener animatorUpdateListener) {
        if (animatorUpdateListener != null) {
            this.mImpl.addUpdateListener(new AnimatorUpdateListenerProxy(this, animatorUpdateListener));
        } else {
            this.mImpl.addUpdateListener(null);
        }
    }

    public void addListener(AnimatorListener animatorListener) {
        if (animatorListener != null) {
            this.mImpl.addListener(new AnimatorListenerProxy(this, animatorListener));
        } else {
            this.mImpl.addListener(null);
        }
    }

    public void setIntValues(int i, int i2) {
        this.mImpl.setIntValues(i, i2);
    }

    public int getAnimatedIntValue() {
        return this.mImpl.getAnimatedIntValue();
    }

    public void setFloatValues(float f, float f2) {
        this.mImpl.setFloatValues(f, f2);
    }

    public float getAnimatedFloatValue() {
        return this.mImpl.getAnimatedFloatValue();
    }

    public void setDuration(long j) {
        this.mImpl.setDuration(j);
    }

    public void cancel() {
        this.mImpl.cancel();
    }

    public float getAnimatedFraction() {
        return this.mImpl.getAnimatedFraction();
    }

    public void end() {
        this.mImpl.end();
    }

    public long getDuration() {
        return this.mImpl.getDuration();
    }
}
