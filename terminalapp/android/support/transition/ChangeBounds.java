package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.transition.Transition.TransitionListener;
import android.view.View;
import android.view.ViewGroup;

public class ChangeBounds extends Transition {
    private static RectEvaluator sRectEvaluator = new RectEvaluator();
    private static final String[] sTransitionProperties = new String[]{"android:changeBounds:bounds", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY"};
    boolean mReparent = false;
    boolean mResizeClip = false;
    int[] mTempLocation = new int[2];

    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        transitionValues.values.put("android:changeBounds:bounds", new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
        transitionValues.values.put("android:changeBounds:parent", transitionValues.view.getParent());
        transitionValues.view.getLocationInWindow(this.mTempLocation);
        transitionValues.values.put("android:changeBounds:windowX", Integer.valueOf(this.mTempLocation[0]));
        transitionValues.values.put("android:changeBounds:windowY", Integer.valueOf(this.mTempLocation[1]));
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null || transitionValues2 == null) {
            return null;
        }
        ViewGroup viewGroup2 = (ViewGroup) transitionValues.values.get("android:changeBounds:parent");
        ViewGroup viewGroup3 = (ViewGroup) transitionValues2.values.get("android:changeBounds:parent");
        if (viewGroup2 == null || viewGroup3 == null) {
            return null;
        }
        final View view = transitionValues2.view;
        Object obj = (viewGroup2 == viewGroup3 || viewGroup2.getId() == viewGroup3.getId()) ? 1 : null;
        int intValue;
        int intValue2;
        int intValue3;
        int intValue4;
        if (this.mReparent && obj == null) {
            intValue = ((Integer) transitionValues.values.get("android:changeBounds:windowX")).intValue();
            intValue2 = ((Integer) transitionValues.values.get("android:changeBounds:windowY")).intValue();
            intValue3 = ((Integer) transitionValues2.values.get("android:changeBounds:windowX")).intValue();
            intValue4 = ((Integer) transitionValues2.values.get("android:changeBounds:windowY")).intValue();
            if (!(intValue == intValue3 && intValue2 == intValue4)) {
                viewGroup.getLocationInWindow(this.mTempLocation);
                Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
                view.draw(new Canvas(createBitmap));
                final Drawable bitmapDrawable = new BitmapDrawable(createBitmap);
                view.setVisibility(4);
                ViewUtils.getOverlay(viewGroup).add(bitmapDrawable);
                Rect rect = new Rect(intValue - this.mTempLocation[0], intValue2 - this.mTempLocation[1], (intValue - this.mTempLocation[0]) + view.getWidth(), (intValue2 - this.mTempLocation[1]) + view.getHeight());
                Rect rect2 = new Rect(intValue3 - this.mTempLocation[0], intValue4 - this.mTempLocation[1], (intValue3 - this.mTempLocation[0]) + view.getWidth(), (intValue4 - this.mTempLocation[1]) + view.getHeight());
                Animator ofObject = ObjectAnimator.ofObject(bitmapDrawable, "bounds", sRectEvaluator, new Object[]{rect, rect2});
                final ViewGroup viewGroup4 = viewGroup;
                ofObject.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        ViewUtils.getOverlay(viewGroup4).remove(bitmapDrawable);
                        view.setVisibility(0);
                    }
                });
                return ofObject;
            }
        }
        Rect rect3 = (Rect) transitionValues.values.get("android:changeBounds:bounds");
        rect2 = (Rect) transitionValues2.values.get("android:changeBounds:bounds");
        intValue2 = rect3.left;
        intValue3 = rect2.left;
        int i = rect3.top;
        int i2 = rect2.top;
        int i3 = rect3.right;
        int i4 = rect2.right;
        int i5 = rect3.bottom;
        int i6 = rect2.bottom;
        int i7 = i3 - intValue2;
        int i8 = i5 - i;
        int i9 = i4 - intValue3;
        int i10 = i6 - i2;
        intValue4 = 0;
        if (!(i7 == 0 || i8 == 0 || i9 == 0 || i10 == 0)) {
            if (intValue2 != intValue3) {
                intValue4 = 1;
            }
            if (i != i2) {
                intValue4++;
            }
            if (i3 != i4) {
                intValue4++;
            }
            if (i5 != i6) {
                intValue4++;
            }
        }
        if (intValue4 > 0) {
            Animator ofPropertyValuesHolder;
            if (this.mResizeClip) {
                if (i7 != i9) {
                    view.setRight(Math.max(i7, i9) + intValue3);
                }
                if (i8 != i10) {
                    view.setBottom(Math.max(i8, i10) + i2);
                }
                if (intValue2 != intValue3) {
                    view.setTranslationX((float) (intValue2 - intValue3));
                }
                if (i != i2) {
                    view.setTranslationY((float) (i - i2));
                }
                float f = (float) (intValue3 - intValue2);
                float f2 = (float) (i2 - i);
                i = i9 - i7;
                i2 = i10 - i8;
                intValue4 = 0;
                if (f != 0.0f) {
                    intValue4 = 1;
                }
                if (f2 != 0.0f) {
                    intValue4++;
                }
                if (!(i == 0 && i2 == 0)) {
                    intValue4++;
                }
                PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[intValue4];
                if (f != 0.0f) {
                    intValue4 = 1;
                    propertyValuesHolderArr[0] = PropertyValuesHolder.ofFloat("translationX", new float[]{view.getTranslationX(), 0.0f});
                } else {
                    intValue4 = 0;
                }
                if (f2 != 0.0f) {
                    propertyValuesHolderArr[intValue4] = PropertyValuesHolder.ofFloat("translationY", new float[]{view.getTranslationY(), 0.0f});
                }
                if (!(i == 0 && i2 == 0)) {
                    rect3 = new Rect(0, 0, i7, i8);
                    rect3 = new Rect(0, 0, i9, i10);
                }
                ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, propertyValuesHolderArr);
                if (view.getParent() instanceof ViewGroup) {
                    viewGroup2 = (ViewGroup) view.getParent();
                    ViewGroupUtils.suppressLayout(viewGroup2, true);
                    addListener(new TransitionListener() {
                        boolean mCanceled = false;

                        public void onTransitionEnd(Transition transition) {
                            if (!this.mCanceled) {
                                ViewGroupUtils.suppressLayout(viewGroup2, false);
                            }
                        }

                        public void onTransitionPause(Transition transition) {
                            ViewGroupUtils.suppressLayout(viewGroup2, false);
                        }

                        public void onTransitionResume(Transition transition) {
                            ViewGroupUtils.suppressLayout(viewGroup2, true);
                        }
                    });
                }
                ofPropertyValuesHolder.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                    }
                });
                return ofPropertyValuesHolder;
            }
            PropertyValuesHolder[] propertyValuesHolderArr2 = new PropertyValuesHolder[intValue4];
            if (intValue2 != intValue3) {
                view.setLeft(intValue2);
            }
            if (i != i2) {
                view.setTop(i);
            }
            if (i3 != i4) {
                view.setRight(i3);
            }
            if (i5 != i6) {
                view.setBottom(i5);
            }
            if (intValue2 != intValue3) {
                intValue4 = 1;
                propertyValuesHolderArr2[0] = PropertyValuesHolder.ofInt("left", new int[]{intValue2, intValue3});
            } else {
                intValue4 = 0;
            }
            if (i != i2) {
                intValue = intValue4 + 1;
                propertyValuesHolderArr2[intValue4] = PropertyValuesHolder.ofInt("top", new int[]{i, i2});
            } else {
                intValue = intValue4;
            }
            if (i3 != i4) {
                intValue4 = intValue + 1;
                propertyValuesHolderArr2[intValue] = PropertyValuesHolder.ofInt("right", new int[]{i3, i4});
            } else {
                intValue4 = intValue;
            }
            if (i5 != i6) {
                propertyValuesHolderArr2[intValue4] = PropertyValuesHolder.ofInt("bottom", new int[]{i5, i6});
            }
            ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, propertyValuesHolderArr2);
            if (view.getParent() instanceof ViewGroup) {
                viewGroup2 = (ViewGroup) view.getParent();
                ViewGroupUtils.suppressLayout(viewGroup2, true);
                addListener(new TransitionListener() {
                    boolean mCanceled = false;

                    public void onTransitionEnd(Transition transition) {
                        if (!this.mCanceled) {
                            ViewGroupUtils.suppressLayout(viewGroup2, false);
                        }
                    }

                    public void onTransitionPause(Transition transition) {
                        ViewGroupUtils.suppressLayout(viewGroup2, false);
                    }

                    public void onTransitionResume(Transition transition) {
                        ViewGroupUtils.suppressLayout(viewGroup2, true);
                    }
                });
            }
            return ofPropertyValuesHolder;
        }
        return null;
    }
}
