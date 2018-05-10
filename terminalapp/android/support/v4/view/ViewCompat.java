package android.support.v4.view;

import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.internal.view.TooltipCompat;
import android.support.v4.os.BuildCompat;
import android.support.v4.view.ViewCompatLollipop.OnApplyWindowInsetsListenerBridge;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

public class ViewCompat {
    static final ViewCompatImpl IMPL;

    interface ViewCompatImpl {
        private static Method sChildrenDrawingOrderMethod;
        WeakHashMap<View, ViewPropertyAnimatorCompat> mViewPropertyAnimatorCompatMap = null;

        ViewCompatImpl() {
        }

        boolean canScrollHorizontally(View view, int i) {
            return (view instanceof ScrollingView) && canScrollingViewScrollHorizontally((ScrollingView) view, i);
        }

        boolean canScrollVertically(View view, int i) {
            return (view instanceof ScrollingView) && canScrollingViewScrollVertically((ScrollingView) view, i);
        }

        void setAccessibilityDelegate(View view, AccessibilityDelegateCompat accessibilityDelegateCompat) {
        }

        boolean hasAccessibilityDelegate(View view) {
            return false;
        }

        boolean hasTransientState(View view) {
            return false;
        }

        void setHasTransientState(View view, boolean z) {
        }

        void postInvalidateOnAnimation(View view) {
            view.invalidate();
        }

        void postOnAnimation(View view, Runnable runnable) {
            view.postDelayed(runnable, getFrameTime());
        }

        void postOnAnimationDelayed(View view, Runnable runnable, long j) {
            view.postDelayed(runnable, getFrameTime() + j);
        }

        long getFrameTime() {
            return 10;
        }

        int getImportantForAccessibility(View view) {
            return 0;
        }

        void setImportantForAccessibility(View view, int i) {
        }

        float getAlpha(View view) {
            return 1.0f;
        }

        void setLayerType(View view, int i, Paint paint) {
        }

        int getLayerType(View view) {
            return 0;
        }

        void setLayerPaint(View view, Paint paint) {
        }

        int getLayoutDirection(View view) {
            return 0;
        }

        ViewParent getParentForAccessibility(View view) {
            return view.getParent();
        }

        int resolveSizeAndState(int i, int i2, int i3) {
            return View.resolveSize(i, i2);
        }

        int getMeasuredWidthAndState(View view) {
            return view.getMeasuredWidth();
        }

        int getMeasuredState(View view) {
            return 0;
        }

        void setAccessibilityLiveRegion(View view, int i) {
        }

        int getPaddingStart(View view) {
            return view.getPaddingLeft();
        }

        int getPaddingEnd(View view) {
            return view.getPaddingRight();
        }

        void setPaddingRelative(View view, int i, int i2, int i3, int i4) {
            view.setPadding(i, i2, i3, i4);
        }

        boolean hasOverlappingRendering(View view) {
            return true;
        }

        float getTranslationX(View view) {
            return 0.0f;
        }

        float getTranslationY(View view) {
            return 0.0f;
        }

        float getScaleX(View view) {
            return 0.0f;
        }

        Matrix getMatrix(View view) {
            return null;
        }

        int getMinimumWidth(View view) {
            return ViewCompatBase.getMinimumWidth(view);
        }

        int getMinimumHeight(View view) {
            return ViewCompatBase.getMinimumHeight(view);
        }

        ViewPropertyAnimatorCompat animate(View view) {
            return new ViewPropertyAnimatorCompat(view);
        }

        void setTranslationX(View view, float f) {
        }

        void setTranslationY(View view, float f) {
        }

        void setAlpha(View view, float f) {
        }

        void setScaleX(View view, float f) {
        }

        void setScaleY(View view, float f) {
        }

        void setPivotX(View view, float f) {
        }

        void setPivotY(View view, float f) {
        }

        String getTransitionName(View view) {
            return null;
        }

        int getWindowSystemUiVisibility(View view) {
            return 0;
        }

        void requestApplyInsets(View view) {
        }

        void setElevation(View view, float f) {
        }

        float getElevation(View view) {
            return 0.0f;
        }

        float getTranslationZ(View view) {
            return 0.0f;
        }

        void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean z) {
            if (sChildrenDrawingOrderMethod == null) {
                try {
                    sChildrenDrawingOrderMethod = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[]{Boolean.TYPE});
                } catch (Throwable e) {
                    Log.e("ViewCompat", "Unable to find childrenDrawingOrderEnabled", e);
                }
                sChildrenDrawingOrderMethod.setAccessible(true);
            }
            try {
                sChildrenDrawingOrderMethod.invoke(viewGroup, new Object[]{Boolean.valueOf(z)});
            } catch (Throwable e2) {
                Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", e2);
            } catch (Throwable e22) {
                Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", e22);
            } catch (Throwable e222) {
                Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", e222);
            }
        }

        boolean getFitsSystemWindows(View view) {
            return false;
        }

        void setFitsSystemWindows(View view, boolean z) {
        }

        void jumpDrawablesToCurrentState(View view) {
        }

        void setOnApplyWindowInsetsListener(View view, OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        }

        WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            return windowInsetsCompat;
        }

        WindowInsetsCompat dispatchApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            return windowInsetsCompat;
        }

        void setSaveFromParentEnabled(View view, boolean z) {
        }

        void setActivated(View view, boolean z) {
        }

        boolean isPaddingRelative(View view) {
            return false;
        }

        boolean isNestedScrollingEnabled(View view) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).isNestedScrollingEnabled();
            }
            return false;
        }

        void setBackground(View view, Drawable drawable) {
            view.setBackgroundDrawable(drawable);
        }

        ColorStateList getBackgroundTintList(View view) {
            return ViewCompatBase.getBackgroundTintList(view);
        }

        void setBackgroundTintList(View view, ColorStateList colorStateList) {
            ViewCompatBase.setBackgroundTintList(view, colorStateList);
        }

        void setBackgroundTintMode(View view, Mode mode) {
            ViewCompatBase.setBackgroundTintMode(view, mode);
        }

        Mode getBackgroundTintMode(View view) {
            return ViewCompatBase.getBackgroundTintMode(view);
        }

        private boolean canScrollingViewScrollHorizontally(ScrollingView scrollingView, int i) {
            int computeHorizontalScrollOffset = scrollingView.computeHorizontalScrollOffset();
            int computeHorizontalScrollRange = scrollingView.computeHorizontalScrollRange() - scrollingView.computeHorizontalScrollExtent();
            if (computeHorizontalScrollRange == 0) {
                return false;
            }
            if (i < 0) {
                if (computeHorizontalScrollOffset <= 0) {
                    return false;
                }
                return true;
            } else if (computeHorizontalScrollOffset >= computeHorizontalScrollRange - 1) {
                return false;
            } else {
                return true;
            }
        }

        private boolean canScrollingViewScrollVertically(ScrollingView scrollingView, int i) {
            int computeVerticalScrollOffset = scrollingView.computeVerticalScrollOffset();
            int computeVerticalScrollRange = scrollingView.computeVerticalScrollRange() - scrollingView.computeVerticalScrollExtent();
            if (computeVerticalScrollRange == 0) {
                return false;
            }
            if (i < 0) {
                if (computeVerticalScrollOffset <= 0) {
                    return false;
                }
                return true;
            } else if (computeVerticalScrollOffset >= computeVerticalScrollRange - 1) {
                return false;
            } else {
                return true;
            }
        }

        void stopNestedScroll(View view) {
            if (view instanceof NestedScrollingChild) {
                ((NestedScrollingChild) view).stopNestedScroll();
            }
        }

        boolean isLaidOut(View view) {
            return ViewCompatBase.isLaidOut(view);
        }

        int combineMeasuredStates(int i, int i2) {
            return i | i2;
        }

        float getZ(View view) {
            return getTranslationZ(view) + getElevation(view);
        }

        boolean isAttachedToWindow(View view) {
            return ViewCompatBase.isAttachedToWindow(view);
        }

        boolean hasOnClickListeners(View view) {
            return false;
        }

        void setScrollIndicators(View view, int i, int i2) {
        }

        void offsetLeftAndRight(View view, int i) {
            ViewCompatBase.offsetLeftAndRight(view, i);
        }

        void offsetTopAndBottom(View view, int i) {
            ViewCompatBase.offsetTopAndBottom(view, i);
        }

        void setPointerIcon(View view, PointerIconCompat pointerIconCompat) {
        }

        Display getDisplay(View view) {
            return ViewCompatBase.getDisplay(view);
        }

        void setTooltip(View view, CharSequence charSequence) {
            TooltipCompat.setTooltip(view, charSequence);
        }

        void postInvalidateOnAnimation(View view, int i, int i2, int i3, int i4) {
            view.invalidate(i, i2, i3, i4);
        }
    }

    static class HCViewCompatImpl extends ViewCompatImpl {
        HCViewCompatImpl() {
        }

        long getFrameTime() {
            return ViewCompatHC.getFrameTime();
        }

        public float getAlpha(View view) {
            return ViewCompatHC.getAlpha(view);
        }

        public void setLayerType(View view, int i, Paint paint) {
            ViewCompatHC.setLayerType(view, i, paint);
        }

        public int getLayerType(View view) {
            return ViewCompatHC.getLayerType(view);
        }

        public void setLayerPaint(View view, Paint paint) {
            setLayerType(view, getLayerType(view), paint);
            view.invalidate();
        }

        public int resolveSizeAndState(int i, int i2, int i3) {
            return ViewCompatHC.resolveSizeAndState(i, i2, i3);
        }

        public int getMeasuredWidthAndState(View view) {
            return ViewCompatHC.getMeasuredWidthAndState(view);
        }

        public int getMeasuredState(View view) {
            return ViewCompatHC.getMeasuredState(view);
        }

        public float getTranslationX(View view) {
            return ViewCompatHC.getTranslationX(view);
        }

        public float getTranslationY(View view) {
            return ViewCompatHC.getTranslationY(view);
        }

        public Matrix getMatrix(View view) {
            return ViewCompatHC.getMatrix(view);
        }

        public void setTranslationX(View view, float f) {
            ViewCompatHC.setTranslationX(view, f);
        }

        public void setTranslationY(View view, float f) {
            ViewCompatHC.setTranslationY(view, f);
        }

        public void setAlpha(View view, float f) {
            ViewCompatHC.setAlpha(view, f);
        }

        public void setScaleX(View view, float f) {
            ViewCompatHC.setScaleX(view, f);
        }

        public void setScaleY(View view, float f) {
            ViewCompatHC.setScaleY(view, f);
        }

        public void setPivotX(View view, float f) {
            ViewCompatHC.setPivotX(view, f);
        }

        public void setPivotY(View view, float f) {
            ViewCompatHC.setPivotY(view, f);
        }

        public float getScaleX(View view) {
            return ViewCompatHC.getScaleX(view);
        }

        public void jumpDrawablesToCurrentState(View view) {
            ViewCompatHC.jumpDrawablesToCurrentState(view);
        }

        public void setSaveFromParentEnabled(View view, boolean z) {
            ViewCompatHC.setSaveFromParentEnabled(view, z);
        }

        public void setActivated(View view, boolean z) {
            ViewCompatHC.setActivated(view, z);
        }

        public int combineMeasuredStates(int i, int i2) {
            return ViewCompatHC.combineMeasuredStates(i, i2);
        }

        public void offsetLeftAndRight(View view, int i) {
            ViewCompatHC.offsetLeftAndRight(view, i);
        }

        public void offsetTopAndBottom(View view, int i) {
            ViewCompatHC.offsetTopAndBottom(view, i);
        }
    }

    static class ICSViewCompatImpl extends HCViewCompatImpl {
        static boolean accessibilityDelegateCheckFailed = false;
        static Field mAccessibilityDelegateField;

        ICSViewCompatImpl() {
        }

        public boolean canScrollHorizontally(View view, int i) {
            return ViewCompatICS.canScrollHorizontally(view, i);
        }

        public boolean canScrollVertically(View view, int i) {
            return ViewCompatICS.canScrollVertically(view, i);
        }

        public void setAccessibilityDelegate(View view, AccessibilityDelegateCompat accessibilityDelegateCompat) {
            Object obj;
            if (accessibilityDelegateCompat == null) {
                obj = null;
            } else {
                obj = accessibilityDelegateCompat.getBridge();
            }
            ViewCompatICS.setAccessibilityDelegate(view, obj);
        }

        public boolean hasAccessibilityDelegate(View view) {
            boolean z = true;
            if (accessibilityDelegateCheckFailed) {
                return false;
            }
            if (mAccessibilityDelegateField == null) {
                try {
                    mAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate");
                    mAccessibilityDelegateField.setAccessible(true);
                } catch (Throwable th) {
                    accessibilityDelegateCheckFailed = true;
                    return false;
                }
            }
            try {
                if (mAccessibilityDelegateField.get(view) == null) {
                    z = false;
                }
                return z;
            } catch (Throwable th2) {
                accessibilityDelegateCheckFailed = true;
                return false;
            }
        }

        public ViewPropertyAnimatorCompat animate(View view) {
            if (this.mViewPropertyAnimatorCompatMap == null) {
                this.mViewPropertyAnimatorCompatMap = new WeakHashMap();
            }
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = (ViewPropertyAnimatorCompat) this.mViewPropertyAnimatorCompatMap.get(view);
            if (viewPropertyAnimatorCompat != null) {
                return viewPropertyAnimatorCompat;
            }
            viewPropertyAnimatorCompat = new ViewPropertyAnimatorCompat(view);
            this.mViewPropertyAnimatorCompatMap.put(view, viewPropertyAnimatorCompat);
            return viewPropertyAnimatorCompat;
        }

        public void setFitsSystemWindows(View view, boolean z) {
            ViewCompatICS.setFitsSystemWindows(view, z);
        }
    }

    static class ICSMr1ViewCompatImpl extends ICSViewCompatImpl {
        ICSMr1ViewCompatImpl() {
        }

        public boolean hasOnClickListeners(View view) {
            return ViewCompatICSMr1.hasOnClickListeners(view);
        }
    }

    static class JBViewCompatImpl extends ICSMr1ViewCompatImpl {
        JBViewCompatImpl() {
        }

        public boolean hasTransientState(View view) {
            return ViewCompatJB.hasTransientState(view);
        }

        public void setHasTransientState(View view, boolean z) {
            ViewCompatJB.setHasTransientState(view, z);
        }

        public void postInvalidateOnAnimation(View view) {
            ViewCompatJB.postInvalidateOnAnimation(view);
        }

        public void postInvalidateOnAnimation(View view, int i, int i2, int i3, int i4) {
            ViewCompatJB.postInvalidateOnAnimation(view, i, i2, i3, i4);
        }

        public void postOnAnimation(View view, Runnable runnable) {
            ViewCompatJB.postOnAnimation(view, runnable);
        }

        public void postOnAnimationDelayed(View view, Runnable runnable, long j) {
            ViewCompatJB.postOnAnimationDelayed(view, runnable, j);
        }

        public int getImportantForAccessibility(View view) {
            return ViewCompatJB.getImportantForAccessibility(view);
        }

        public void setImportantForAccessibility(View view, int i) {
            if (i == 4) {
                i = 2;
            }
            ViewCompatJB.setImportantForAccessibility(view, i);
        }

        public ViewParent getParentForAccessibility(View view) {
            return ViewCompatJB.getParentForAccessibility(view);
        }

        public int getMinimumWidth(View view) {
            return ViewCompatJB.getMinimumWidth(view);
        }

        public int getMinimumHeight(View view) {
            return ViewCompatJB.getMinimumHeight(view);
        }

        public void requestApplyInsets(View view) {
            ViewCompatJB.requestApplyInsets(view);
        }

        public boolean getFitsSystemWindows(View view) {
            return ViewCompatJB.getFitsSystemWindows(view);
        }

        public boolean hasOverlappingRendering(View view) {
            return ViewCompatJB.hasOverlappingRendering(view);
        }

        public void setBackground(View view, Drawable drawable) {
            ViewCompatJB.setBackground(view, drawable);
        }
    }

    static class JbMr1ViewCompatImpl extends JBViewCompatImpl {
        JbMr1ViewCompatImpl() {
        }

        public void setLayerPaint(View view, Paint paint) {
            ViewCompatJellybeanMr1.setLayerPaint(view, paint);
        }

        public int getLayoutDirection(View view) {
            return ViewCompatJellybeanMr1.getLayoutDirection(view);
        }

        public int getPaddingStart(View view) {
            return ViewCompatJellybeanMr1.getPaddingStart(view);
        }

        public int getPaddingEnd(View view) {
            return ViewCompatJellybeanMr1.getPaddingEnd(view);
        }

        public void setPaddingRelative(View view, int i, int i2, int i3, int i4) {
            ViewCompatJellybeanMr1.setPaddingRelative(view, i, i2, i3, i4);
        }

        public int getWindowSystemUiVisibility(View view) {
            return ViewCompatJellybeanMr1.getWindowSystemUiVisibility(view);
        }

        public boolean isPaddingRelative(View view) {
            return ViewCompatJellybeanMr1.isPaddingRelative(view);
        }

        public Display getDisplay(View view) {
            return ViewCompatJellybeanMr1.getDisplay(view);
        }

        JbMr1ViewCompatImpl(byte b) {
            this();
        }
    }

    static class KitKatViewCompatImpl extends JbMr1ViewCompatImpl {
        KitKatViewCompatImpl() {
            super((byte) 0);
        }

        public void setAccessibilityLiveRegion(View view, int i) {
            ViewCompatKitKat.setAccessibilityLiveRegion(view, i);
        }

        public void setImportantForAccessibility(View view, int i) {
            ViewCompatJB.setImportantForAccessibility(view, i);
        }

        public boolean isLaidOut(View view) {
            return ViewCompatKitKat.isLaidOut(view);
        }

        public boolean isAttachedToWindow(View view) {
            return ViewCompatKitKat.isAttachedToWindow(view);
        }
    }

    static class LollipopViewCompatImpl extends KitKatViewCompatImpl {
        LollipopViewCompatImpl() {
        }

        public String getTransitionName(View view) {
            return ViewCompatLollipop.getTransitionName(view);
        }

        public void requestApplyInsets(View view) {
            ViewCompatLollipop.requestApplyInsets(view);
        }

        public void setElevation(View view, float f) {
            ViewCompatLollipop.setElevation(view, f);
        }

        public float getElevation(View view) {
            return ViewCompatLollipop.getElevation(view);
        }

        public float getTranslationZ(View view) {
            return ViewCompatLollipop.getTranslationZ(view);
        }

        public void setOnApplyWindowInsetsListener(View view, OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
            if (onApplyWindowInsetsListener == null) {
                ViewCompatLollipop.setOnApplyWindowInsetsListener(view, null);
            } else {
                ViewCompatLollipop.setOnApplyWindowInsetsListener(view, new OnApplyWindowInsetsListenerBridge(this, onApplyWindowInsetsListener));
            }
        }

        public boolean isNestedScrollingEnabled(View view) {
            return ViewCompatLollipop.isNestedScrollingEnabled(view);
        }

        public void stopNestedScroll(View view) {
            ViewCompatLollipop.stopNestedScroll(view);
        }

        public ColorStateList getBackgroundTintList(View view) {
            return ViewCompatLollipop.getBackgroundTintList(view);
        }

        public void setBackgroundTintList(View view, ColorStateList colorStateList) {
            ViewCompatLollipop.setBackgroundTintList(view, colorStateList);
        }

        public void setBackgroundTintMode(View view, Mode mode) {
            ViewCompatLollipop.setBackgroundTintMode(view, mode);
        }

        public Mode getBackgroundTintMode(View view) {
            return ViewCompatLollipop.getBackgroundTintMode(view);
        }

        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            return WindowInsetsCompat.wrap(ViewCompatLollipop.onApplyWindowInsets(view, WindowInsetsCompat.unwrap(windowInsetsCompat)));
        }

        public WindowInsetsCompat dispatchApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            return WindowInsetsCompat.wrap(ViewCompatLollipop.dispatchApplyWindowInsets(view, WindowInsetsCompat.unwrap(windowInsetsCompat)));
        }

        public float getZ(View view) {
            return ViewCompatLollipop.getZ(view);
        }

        public void offsetLeftAndRight(View view, int i) {
            ViewCompatLollipop.offsetLeftAndRight(view, i);
        }

        public void offsetTopAndBottom(View view, int i) {
            ViewCompatLollipop.offsetTopAndBottom(view, i);
        }
    }

    static class MarshmallowViewCompatImpl extends LollipopViewCompatImpl {
        MarshmallowViewCompatImpl() {
        }

        public void setScrollIndicators(View view, int i, int i2) {
            ViewCompatMarshmallow.setScrollIndicators(view, i, i2);
        }

        public void offsetLeftAndRight(View view, int i) {
            ViewCompatMarshmallow.offsetLeftAndRight(view, i);
        }

        public void offsetTopAndBottom(View view, int i) {
            ViewCompatMarshmallow.offsetTopAndBottom(view, i);
        }
    }

    static class Api24ViewCompatImpl extends MarshmallowViewCompatImpl {
        Api24ViewCompatImpl() {
        }

        public void setPointerIcon(View view, PointerIconCompat pointerIconCompat) {
            ViewCompatApi24.setPointerIcon(view, pointerIconCompat != null ? pointerIconCompat.getPointerIcon() : null);
        }
    }

    static class Api26ViewCompatImpl extends Api24ViewCompatImpl {
        Api26ViewCompatImpl() {
        }

        public void setTooltip(View view, CharSequence charSequence) {
            ViewCompatApi26.setTooltip(view, charSequence);
        }
    }

    static {
        int i = VERSION.SDK_INT;
        if (BuildCompat.isAtLeastO()) {
            IMPL = new Api26ViewCompatImpl();
        } else if (i >= 24) {
            IMPL = new Api24ViewCompatImpl();
        } else if (i >= 23) {
            IMPL = new MarshmallowViewCompatImpl();
        } else if (i >= 21) {
            IMPL = new LollipopViewCompatImpl();
        } else if (i >= 19) {
            IMPL = new KitKatViewCompatImpl();
        } else if (i >= 18) {
            IMPL = new JbMr1ViewCompatImpl((byte) 0);
        } else if (i >= 17) {
            IMPL = new JbMr1ViewCompatImpl();
        } else if (i >= 16) {
            IMPL = new JBViewCompatImpl();
        } else if (i >= 15) {
            IMPL = new ICSMr1ViewCompatImpl();
        } else if (i >= 14) {
            IMPL = new ICSViewCompatImpl();
        } else if (i >= 11) {
            IMPL = new HCViewCompatImpl();
        } else {
            IMPL = new ViewCompatImpl();
        }
    }

    public static boolean canScrollHorizontally(View view, int i) {
        return IMPL.canScrollHorizontally(view, i);
    }

    public static boolean canScrollVertically(View view, int i) {
        return IMPL.canScrollVertically(view, i);
    }

    public static void setAccessibilityDelegate(View view, AccessibilityDelegateCompat accessibilityDelegateCompat) {
        IMPL.setAccessibilityDelegate(view, accessibilityDelegateCompat);
    }

    public static boolean hasAccessibilityDelegate(View view) {
        return IMPL.hasAccessibilityDelegate(view);
    }

    public static boolean hasTransientState(View view) {
        return IMPL.hasTransientState(view);
    }

    public static void setHasTransientState(View view, boolean z) {
        IMPL.setHasTransientState(view, z);
    }

    public static void postInvalidateOnAnimation(View view) {
        IMPL.postInvalidateOnAnimation(view);
    }

    public static void postInvalidateOnAnimation(View view, int i, int i2, int i3, int i4) {
        IMPL.postInvalidateOnAnimation(view, i, i2, i3, i4);
    }

    public static void postOnAnimation(View view, Runnable runnable) {
        IMPL.postOnAnimation(view, runnable);
    }

    public static void postOnAnimationDelayed(View view, Runnable runnable, long j) {
        IMPL.postOnAnimationDelayed(view, runnable, j);
    }

    public static int getImportantForAccessibility(View view) {
        return IMPL.getImportantForAccessibility(view);
    }

    public static void setImportantForAccessibility(View view, int i) {
        IMPL.setImportantForAccessibility(view, i);
    }

    public static float getAlpha(View view) {
        return IMPL.getAlpha(view);
    }

    public static void setLayerType(View view, int i, Paint paint) {
        IMPL.setLayerType(view, i, paint);
    }

    public static int getLayerType(View view) {
        return IMPL.getLayerType(view);
    }

    public static void setLayerPaint(View view, Paint paint) {
        IMPL.setLayerPaint(view, paint);
    }

    public static int getLayoutDirection(View view) {
        return IMPL.getLayoutDirection(view);
    }

    public static ViewParent getParentForAccessibility(View view) {
        return IMPL.getParentForAccessibility(view);
    }

    public static int resolveSizeAndState(int i, int i2, int i3) {
        return IMPL.resolveSizeAndState(i, i2, i3);
    }

    public static int getMeasuredWidthAndState(View view) {
        return IMPL.getMeasuredWidthAndState(view);
    }

    public static int getMeasuredState(View view) {
        return IMPL.getMeasuredState(view);
    }

    public static int combineMeasuredStates(int i, int i2) {
        return IMPL.combineMeasuredStates(i, i2);
    }

    public static void setAccessibilityLiveRegion(View view, int i) {
        IMPL.setAccessibilityLiveRegion(view, i);
    }

    public static int getPaddingStart(View view) {
        return IMPL.getPaddingStart(view);
    }

    public static int getPaddingEnd(View view) {
        return IMPL.getPaddingEnd(view);
    }

    public static void setPaddingRelative(View view, int i, int i2, int i3, int i4) {
        IMPL.setPaddingRelative(view, i, i2, i3, i4);
    }

    public static float getTranslationX(View view) {
        return IMPL.getTranslationX(view);
    }

    public static float getTranslationY(View view) {
        return IMPL.getTranslationY(view);
    }

    public static Matrix getMatrix(View view) {
        return IMPL.getMatrix(view);
    }

    public static int getMinimumWidth(View view) {
        return IMPL.getMinimumWidth(view);
    }

    public static int getMinimumHeight(View view) {
        return IMPL.getMinimumHeight(view);
    }

    public static ViewPropertyAnimatorCompat animate(View view) {
        return IMPL.animate(view);
    }

    public static void setTranslationX(View view, float f) {
        IMPL.setTranslationX(view, f);
    }

    public static void setTranslationY(View view, float f) {
        IMPL.setTranslationY(view, f);
    }

    public static void setAlpha(View view, float f) {
        IMPL.setAlpha(view, f);
    }

    public static void setScaleX(View view, float f) {
        IMPL.setScaleX(view, f);
    }

    public static void setScaleY(View view, float f) {
        IMPL.setScaleY(view, f);
    }

    public static void setPivotX(View view, float f) {
        IMPL.setPivotX(view, f);
    }

    public static void setPivotY(View view, float f) {
        IMPL.setPivotY(view, f);
    }

    public static float getScaleX(View view) {
        return IMPL.getScaleX(view);
    }

    public static void setElevation(View view, float f) {
        IMPL.setElevation(view, f);
    }

    public static float getElevation(View view) {
        return IMPL.getElevation(view);
    }

    public static String getTransitionName(View view) {
        return IMPL.getTransitionName(view);
    }

    public static int getWindowSystemUiVisibility(View view) {
        return IMPL.getWindowSystemUiVisibility(view);
    }

    public static void requestApplyInsets(View view) {
        IMPL.requestApplyInsets(view);
    }

    public static void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean z) {
        IMPL.setChildrenDrawingOrderEnabled(viewGroup, z);
    }

    public static boolean getFitsSystemWindows(View view) {
        return IMPL.getFitsSystemWindows(view);
    }

    public static void setFitsSystemWindows(View view, boolean z) {
        IMPL.setFitsSystemWindows(view, z);
    }

    public static void jumpDrawablesToCurrentState(View view) {
        IMPL.jumpDrawablesToCurrentState(view);
    }

    public static void setOnApplyWindowInsetsListener(View view, OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        IMPL.setOnApplyWindowInsetsListener(view, onApplyWindowInsetsListener);
    }

    public static WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
        return IMPL.onApplyWindowInsets(view, windowInsetsCompat);
    }

    public static WindowInsetsCompat dispatchApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
        return IMPL.dispatchApplyWindowInsets(view, windowInsetsCompat);
    }

    public static void setSaveFromParentEnabled(View view, boolean z) {
        IMPL.setSaveFromParentEnabled(view, z);
    }

    public static void setActivated(View view, boolean z) {
        IMPL.setActivated(view, z);
    }

    public static boolean hasOverlappingRendering(View view) {
        return IMPL.hasOverlappingRendering(view);
    }

    public static boolean isPaddingRelative(View view) {
        return IMPL.isPaddingRelative(view);
    }

    public static void setBackground(View view, Drawable drawable) {
        IMPL.setBackground(view, drawable);
    }

    public static ColorStateList getBackgroundTintList(View view) {
        return IMPL.getBackgroundTintList(view);
    }

    public static void setBackgroundTintList(View view, ColorStateList colorStateList) {
        IMPL.setBackgroundTintList(view, colorStateList);
    }

    public static Mode getBackgroundTintMode(View view) {
        return IMPL.getBackgroundTintMode(view);
    }

    public static void setBackgroundTintMode(View view, Mode mode) {
        IMPL.setBackgroundTintMode(view, mode);
    }

    public static boolean isNestedScrollingEnabled(View view) {
        return IMPL.isNestedScrollingEnabled(view);
    }

    public static void stopNestedScroll(View view) {
        IMPL.stopNestedScroll(view);
    }

    public static boolean isLaidOut(View view) {
        return IMPL.isLaidOut(view);
    }

    public static float getZ(View view) {
        return IMPL.getZ(view);
    }

    public static void offsetTopAndBottom(View view, int i) {
        IMPL.offsetTopAndBottom(view, i);
    }

    public static void offsetLeftAndRight(View view, int i) {
        IMPL.offsetLeftAndRight(view, i);
    }

    public static boolean isAttachedToWindow(View view) {
        return IMPL.isAttachedToWindow(view);
    }

    public static boolean hasOnClickListeners(View view) {
        return IMPL.hasOnClickListeners(view);
    }

    public static void setScrollIndicators(View view, int i, int i2) {
        IMPL.setScrollIndicators(view, i, i2);
    }

    public static void setPointerIcon(View view, PointerIconCompat pointerIconCompat) {
        IMPL.setPointerIcon(view, pointerIconCompat);
    }

    public static Display getDisplay(View view) {
        return IMPL.getDisplay(view);
    }

    public static void setTooltip(View view, CharSequence charSequence) {
        IMPL.setTooltip(view, charSequence);
    }

    protected ViewCompat() {
    }
}
