package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.transition.Transition.TransitionListener;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

public class Fade extends Visibility {
    private int mFadingMode;

    public Fade(int i) {
        this.mFadingMode = i;
    }

    public Fade() {
        this(3);
    }

    private Animator createAnimation(View view, float f, float f2, AnimatorListenerAdapter animatorListenerAdapter) {
        Animator animator = null;
        if (f != f2) {
            animator = ObjectAnimator.ofFloat(view, "alpha", new float[]{f, f2});
            if (animatorListenerAdapter != null) {
                animator.addListener(animatorListenerAdapter);
            }
        } else if (animatorListenerAdapter != null) {
            animatorListenerAdapter.onAnimationEnd(null);
        }
        return animator;
    }

    private void captureValues(TransitionValues transitionValues) {
        int[] iArr = new int[2];
        transitionValues.view.getLocationOnScreen(iArr);
        transitionValues.values.put("android:fade:screenX", Integer.valueOf(iArr[0]));
        transitionValues.values.put("android:fade:screenY", Integer.valueOf(iArr[1]));
    }

    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }

    public Animator onAppear(ViewGroup viewGroup, TransitionValues transitionValues, int i, TransitionValues transitionValues2, int i2) {
        if ((this.mFadingMode & 1) != 1 || transitionValues2 == null) {
            return null;
        }
        final View view = transitionValues2.view;
        view.setAlpha(0.0f);
        addListener(new TransitionListener() {
            boolean mCanceled = false;
            float mPausedAlpha;

            public void onTransitionEnd(Transition transition) {
                if (!this.mCanceled) {
                    view.setAlpha(1.0f);
                }
            }

            public void onTransitionPause(Transition transition) {
                this.mPausedAlpha = view.getAlpha();
                view.setAlpha(1.0f);
            }

            public void onTransitionResume(Transition transition) {
                view.setAlpha(this.mPausedAlpha);
            }
        });
        return createAnimation(view, 0.0f, 1.0f, null);
    }

    public Animator onDisappear(ViewGroup viewGroup, TransitionValues transitionValues, int i, TransitionValues transitionValues2, int i2) {
        if ((this.mFadingMode & 2) != 2) {
            return null;
        }
        View view;
        View view2;
        View view3;
        int id;
        View view4 = transitionValues != null ? transitionValues.view : null;
        if (transitionValues2 != null) {
            view = transitionValues2.view;
        } else {
            view = null;
        }
        if (view == null || view.getParent() == null) {
            if (view != null) {
                view2 = view;
                view3 = view;
                view = null;
            } else {
                if (view4 != null) {
                    if (view4.getParent() == null) {
                        view = null;
                        view2 = view4;
                        view3 = view4;
                    } else if ((view4.getParent() instanceof View) && view4.getParent().getParent() == null) {
                        View view5;
                        id = ((View) view4.getParent()).getId();
                        if (id == -1 || viewGroup.findViewById(id) == null || !this.mCanRemoveViews) {
                            Object obj = null;
                            view4 = null;
                        } else {
                            view5 = view4;
                        }
                        view = null;
                        view2 = view5;
                        view3 = view4;
                    }
                }
                view = null;
                view2 = null;
                view3 = null;
            }
        } else if (i2 == 4) {
            view2 = null;
            view3 = view;
        } else if (view4 == view) {
            view2 = null;
            view3 = view;
        } else {
            view = null;
            view2 = view4;
            view3 = view4;
        }
        final int i3;
        final ViewGroup viewGroup2;
        if (view2 != null) {
            int intValue = ((Integer) transitionValues.values.get("android:fade:screenX")).intValue();
            id = ((Integer) transitionValues.values.get("android:fade:screenY")).intValue();
            int[] iArr = new int[2];
            viewGroup.getLocationOnScreen(iArr);
            ViewCompat.offsetLeftAndRight(view2, (intValue - iArr[0]) - view2.getLeft());
            ViewCompat.offsetTopAndBottom(view2, (id - iArr[1]) - view2.getTop());
            ViewGroupUtils.getOverlay(viewGroup).add(view2);
            i3 = i2;
            viewGroup2 = viewGroup;
            return createAnimation(view3, 1.0f, 0.0f, new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    view3.setAlpha(1.0f);
                    if (view != null) {
                        view.setVisibility(i3);
                    }
                    if (view2 != null) {
                        ViewGroupUtils.getOverlay(viewGroup2).remove(view2);
                    }
                }
            });
        } else if (view == null) {
            return null;
        } else {
            view.setVisibility(0);
            i3 = i2;
            viewGroup2 = viewGroup;
            return createAnimation(view3, 1.0f, 0.0f, new AnimatorListenerAdapter() {
                boolean mCanceled = false;
                float mPausedAlpha = -1.0f;

                public void onAnimationCancel(Animator animator) {
                    this.mCanceled = true;
                    if (this.mPausedAlpha >= 0.0f) {
                        view3.setAlpha(this.mPausedAlpha);
                    }
                }

                public void onAnimationEnd(Animator animator) {
                    if (!this.mCanceled) {
                        view3.setAlpha(1.0f);
                    }
                    if (!(view == null || this.mCanceled)) {
                        view.setVisibility(i3);
                    }
                    if (view2 != null) {
                        ViewGroupUtils.getOverlay(viewGroup2).remove(view2);
                    }
                }
            });
        }
    }
}
