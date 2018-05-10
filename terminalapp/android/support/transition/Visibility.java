package android.support.transition;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;

public abstract class Visibility extends Transition {
    private static final String[] sTransitionProperties = new String[]{"android:visibility:visibility", "android:visibility:parent"};

    static class VisibilityInfo {
        ViewGroup mEndParent;
        int mEndVisibility;
        boolean mFadeIn;
        ViewGroup mStartParent;
        int mStartVisibility;
        boolean mVisibilityChange;

        private VisibilityInfo() {
        }
    }

    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.values.put("android:visibility:visibility", Integer.valueOf(transitionValues.view.getVisibility()));
        transitionValues.values.put("android:visibility:parent", transitionValues.view.getParent());
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private VisibilityInfo getVisibilityChangeInfo(TransitionValues transitionValues, TransitionValues transitionValues2) {
        VisibilityInfo visibilityInfo = new VisibilityInfo();
        visibilityInfo.mVisibilityChange = false;
        visibilityInfo.mFadeIn = false;
        if (transitionValues != null) {
            visibilityInfo.mStartVisibility = ((Integer) transitionValues.values.get("android:visibility:visibility")).intValue();
            visibilityInfo.mStartParent = (ViewGroup) transitionValues.values.get("android:visibility:parent");
        } else {
            visibilityInfo.mStartVisibility = -1;
            visibilityInfo.mStartParent = null;
        }
        if (transitionValues2 != null) {
            visibilityInfo.mEndVisibility = ((Integer) transitionValues2.values.get("android:visibility:visibility")).intValue();
            visibilityInfo.mEndParent = (ViewGroup) transitionValues2.values.get("android:visibility:parent");
        } else {
            visibilityInfo.mEndVisibility = -1;
            visibilityInfo.mEndParent = null;
        }
        if (!(transitionValues == null || transitionValues2 == null)) {
            if (visibilityInfo.mStartVisibility == visibilityInfo.mEndVisibility && visibilityInfo.mStartParent == visibilityInfo.mEndParent) {
                return visibilityInfo;
            }
            if (visibilityInfo.mStartVisibility != visibilityInfo.mEndVisibility) {
                if (visibilityInfo.mStartVisibility == 0) {
                    visibilityInfo.mFadeIn = false;
                    visibilityInfo.mVisibilityChange = true;
                } else if (visibilityInfo.mEndVisibility == 0) {
                    visibilityInfo.mFadeIn = true;
                    visibilityInfo.mVisibilityChange = true;
                }
            } else if (visibilityInfo.mEndParent == null) {
                visibilityInfo.mFadeIn = false;
                visibilityInfo.mVisibilityChange = true;
            } else if (visibilityInfo.mStartParent == null) {
                visibilityInfo.mFadeIn = true;
                visibilityInfo.mVisibilityChange = true;
            }
        }
        if (transitionValues == null) {
            visibilityInfo.mFadeIn = true;
            visibilityInfo.mVisibilityChange = true;
        } else if (transitionValues2 == null) {
            visibilityInfo.mFadeIn = false;
            visibilityInfo.mVisibilityChange = true;
        }
        return visibilityInfo;
    }

    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        Object obj = null;
        int i = -1;
        VisibilityInfo visibilityChangeInfo = getVisibilityChangeInfo(transitionValues, transitionValues2);
        if (visibilityChangeInfo.mVisibilityChange) {
            if (this.mTargets.size() > 0 || this.mTargetIds.size() > 0) {
                View view;
                int id;
                Object obj2;
                View view2 = transitionValues != null ? transitionValues.view : null;
                if (transitionValues2 != null) {
                    view = transitionValues2.view;
                } else {
                    view = null;
                }
                if (view2 != null) {
                    id = view2.getId();
                } else {
                    id = -1;
                }
                if (view != null) {
                    i = view.getId();
                }
                if (isValidTarget(view2, (long) id) || isValidTarget(view, (long) r0)) {
                    obj2 = 1;
                } else {
                    obj2 = null;
                }
                obj = obj2;
            }
            if (!(obj == null && visibilityChangeInfo.mStartParent == null && visibilityChangeInfo.mEndParent == null)) {
                if (visibilityChangeInfo.mFadeIn) {
                    return onAppear(viewGroup, transitionValues, visibilityChangeInfo.mStartVisibility, transitionValues2, visibilityChangeInfo.mEndVisibility);
                }
                return onDisappear(viewGroup, transitionValues, visibilityChangeInfo.mStartVisibility, transitionValues2, visibilityChangeInfo.mEndVisibility);
            }
        }
        return null;
    }

    public Animator onAppear(ViewGroup viewGroup, TransitionValues transitionValues, int i, TransitionValues transitionValues2, int i2) {
        return null;
    }

    public Animator onDisappear(ViewGroup viewGroup, TransitionValues transitionValues, int i, TransitionValues transitionValues2, int i2) {
        return null;
    }
}
