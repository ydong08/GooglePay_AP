package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Transition implements Cloneable {
    private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators = new ThreadLocal();
    private ArrayList<Animator> mAnimators = new ArrayList();
    boolean mCanRemoveViews = false;
    private ArrayList<Animator> mCurrentAnimators = new ArrayList();
    long mDuration = -1;
    private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
    private boolean mEnded = false;
    private TimeInterpolator mInterpolator = null;
    private ArrayList<TransitionListener> mListeners = null;
    private String mName = getClass().getName();
    private int mNumInstances = 0;
    TransitionSet mParent = null;
    private boolean mPaused = false;
    private ViewGroup mSceneRoot = null;
    private long mStartDelay = -1;
    private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
    private ArrayList<View> mTargetChildExcludes = null;
    private ArrayList<View> mTargetExcludes = null;
    private ArrayList<Integer> mTargetIdChildExcludes = null;
    private ArrayList<Integer> mTargetIdExcludes = null;
    ArrayList<Integer> mTargetIds = new ArrayList();
    private ArrayList<Class> mTargetTypeChildExcludes = null;
    private ArrayList<Class> mTargetTypeExcludes = null;
    ArrayList<View> mTargets = new ArrayList();

    public interface TransitionListener {
        void onTransitionStart(Transition transition) {
        }

        void onTransitionEnd(Transition transition) {
        }

        void onTransitionPause(Transition transition) {
        }

        void onTransitionResume(Transition transition) {
        }
    }

    static class AnimationInfo {
        String mName;
        TransitionValues mValues;
        View mView;
        WindowIdImpl mWindowId;

        AnimationInfo(View view, String str, WindowIdImpl windowIdImpl, TransitionValues transitionValues) {
            this.mView = view;
            this.mName = str;
            this.mValues = transitionValues;
            this.mWindowId = windowIdImpl;
        }
    }

    public abstract void captureEndValues(TransitionValues transitionValues);

    public abstract void captureStartValues(TransitionValues transitionValues);

    public Transition setDuration(long j) {
        this.mDuration = j;
        return this;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public Transition setInterpolator(TimeInterpolator timeInterpolator) {
        this.mInterpolator = timeInterpolator;
        return this;
    }

    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    public String[] getTransitionProperties() {
        return null;
    }

    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    protected void createAnimators(ViewGroup viewGroup, TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2) {
        int i;
        int i2;
        ArrayMap arrayMap = new ArrayMap(transitionValuesMaps2.mViewValues);
        SparseArray sparseArray = new SparseArray(transitionValuesMaps2.mIdValues.size());
        for (i = 0; i < transitionValuesMaps2.mIdValues.size(); i++) {
            sparseArray.put(transitionValuesMaps2.mIdValues.keyAt(i), transitionValuesMaps2.mIdValues.valueAt(i));
        }
        LongSparseArray longSparseArray = new LongSparseArray(transitionValuesMaps2.mItemIdValues.size());
        for (i = 0; i < transitionValuesMaps2.mItemIdValues.size(); i++) {
            longSparseArray.put(transitionValuesMaps2.mItemIdValues.keyAt(i), transitionValuesMaps2.mItemIdValues.valueAt(i));
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (View view : transitionValuesMaps.mViewValues.keySet()) {
            Object obj = null;
            if (view.getParent() instanceof ListView) {
                obj = 1;
            }
            if (obj == null) {
                Object obj2;
                int id = view.getId();
                if (transitionValuesMaps.mViewValues.get(view) != null) {
                    obj2 = (TransitionValues) transitionValuesMaps.mViewValues.get(view);
                } else {
                    TransitionValues transitionValues = (TransitionValues) transitionValuesMaps.mIdValues.get(id);
                }
                if (transitionValuesMaps2.mViewValues.get(view) != null) {
                    obj = (TransitionValues) transitionValuesMaps2.mViewValues.get(view);
                    arrayMap.remove(view);
                } else if (id != -1) {
                    TransitionValues transitionValues2 = (TransitionValues) transitionValuesMaps2.mIdValues.get(id);
                    View view2 = null;
                    for (View view3 : arrayMap.keySet()) {
                        View view32;
                        if (view32.getId() != id) {
                            view32 = view2;
                        }
                        view2 = view32;
                    }
                    if (view2 != null) {
                        arrayMap.remove(view2);
                    }
                } else {
                    obj = null;
                }
                sparseArray.remove(id);
                if (isValidTarget(view, (long) id)) {
                    arrayList.add(obj2);
                    arrayList2.add(obj);
                }
            } else {
                ListView listView = (ListView) view.getParent();
                if (listView.getAdapter().hasStableIds()) {
                    long itemIdAtPosition = listView.getItemIdAtPosition(listView.getPositionForView(view));
                    TransitionValues transitionValues3 = (TransitionValues) transitionValuesMaps.mItemIdValues.get(itemIdAtPosition);
                    longSparseArray.remove(itemIdAtPosition);
                    arrayList.add(transitionValues3);
                    arrayList2.add(null);
                }
            }
        }
        int size = transitionValuesMaps.mItemIdValues.size();
        for (i2 = 0; i2 < size; i2++) {
            long keyAt = transitionValuesMaps.mItemIdValues.keyAt(i2);
            if (isValidTarget(null, keyAt)) {
                transitionValues3 = (TransitionValues) transitionValuesMaps.mItemIdValues.get(keyAt);
                transitionValues2 = (TransitionValues) transitionValuesMaps2.mItemIdValues.get(keyAt);
                longSparseArray.remove(keyAt);
                arrayList.add(transitionValues3);
                arrayList2.add(transitionValues2);
            }
        }
        for (View view4 : arrayMap.keySet()) {
            size = view4.getId();
            if (isValidTarget(view4, (long) size)) {
                if (transitionValuesMaps.mViewValues.get(view4) != null) {
                    obj = (TransitionValues) transitionValuesMaps.mViewValues.get(view4);
                } else {
                    transitionValues2 = (TransitionValues) transitionValuesMaps.mIdValues.get(size);
                }
                transitionValues3 = (TransitionValues) arrayMap.get(view4);
                sparseArray.remove(size);
                arrayList.add(obj);
                arrayList2.add(transitionValues3);
            }
        }
        size = sparseArray.size();
        for (i2 = 0; i2 < size; i2++) {
            int keyAt2 = sparseArray.keyAt(i2);
            if (isValidTarget(null, (long) keyAt2)) {
                transitionValues3 = (TransitionValues) transitionValuesMaps.mIdValues.get(keyAt2);
                transitionValues2 = (TransitionValues) sparseArray.get(keyAt2);
                arrayList.add(transitionValues3);
                arrayList2.add(transitionValues2);
            }
        }
        size = longSparseArray.size();
        for (i2 = 0; i2 < size; i2++) {
            long keyAt3 = longSparseArray.keyAt(i2);
            transitionValues2 = (TransitionValues) longSparseArray.get(keyAt3);
            arrayList.add((TransitionValues) transitionValuesMaps.mItemIdValues.get(keyAt3));
            arrayList2.add(transitionValues2);
        }
        ArrayMap runningAnimators = getRunningAnimators();
        for (i2 = 0; i2 < arrayList.size(); i2++) {
            transitionValues3 = (TransitionValues) arrayList.get(i2);
            transitionValues2 = (TransitionValues) arrayList2.get(i2);
            if (!(transitionValues3 == null && transitionValues2 == null) && (transitionValues3 == null || !transitionValues3.equals(transitionValues2))) {
                Object createAnimator = createAnimator(viewGroup, transitionValues3, transitionValues2);
                if (createAnimator != null) {
                    View view5;
                    if (transitionValues2 != null) {
                        View view6 = transitionValues2.view;
                        String[] transitionProperties = getTransitionProperties();
                        if (view6 == null || transitionProperties == null || transitionProperties.length <= 0) {
                            transitionValues3 = null;
                            obj = createAnimator;
                        } else {
                            transitionValues = new TransitionValues();
                            transitionValues.view = view6;
                            transitionValues3 = (TransitionValues) transitionValuesMaps2.mViewValues.get(view6);
                            if (transitionValues3 != null) {
                                for (keyAt2 = 0; keyAt2 < transitionProperties.length; keyAt2++) {
                                    transitionValues.values.put(transitionProperties[keyAt2], transitionValues3.values.get(transitionProperties[keyAt2]));
                                }
                            }
                            int size2 = runningAnimators.size();
                            for (keyAt2 = 0; keyAt2 < size2; keyAt2++) {
                                AnimationInfo animationInfo = (AnimationInfo) runningAnimators.get((Animator) runningAnimators.keyAt(keyAt2));
                                if (animationInfo.mValues != null && animationInfo.mView == view6 && (((animationInfo.mName == null && getName() == null) || animationInfo.mName.equals(getName())) && animationInfo.mValues.equals(transitionValues))) {
                                    obj = null;
                                    transitionValues3 = transitionValues;
                                    break;
                                }
                            }
                            transitionValues3 = transitionValues;
                            obj = createAnimator;
                        }
                        createAnimator = obj;
                        view5 = view6;
                    } else {
                        view5 = transitionValues3.view;
                        transitionValues3 = null;
                    }
                    if (createAnimator != null) {
                        runningAnimators.put(createAnimator, new AnimationInfo(view5, getName(), ViewUtils.getWindowId(viewGroup), transitionValues3));
                        this.mAnimators.add(createAnimator);
                    }
                }
            }
        }
    }

    boolean isValidTarget(View view, long j) {
        if (this.mTargetIdExcludes != null && this.mTargetIdExcludes.contains(Integer.valueOf((int) j))) {
            return false;
        }
        if (this.mTargetExcludes != null && this.mTargetExcludes.contains(view)) {
            return false;
        }
        int i;
        if (!(this.mTargetTypeExcludes == null || view == null)) {
            int size = this.mTargetTypeExcludes.size();
            for (i = 0; i < size; i++) {
                if (((Class) this.mTargetTypeExcludes.get(i)).isInstance(view)) {
                    return false;
                }
            }
        }
        if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0) {
            return true;
        }
        if (this.mTargetIds.size() > 0) {
            for (i = 0; i < this.mTargetIds.size(); i++) {
                if (((long) ((Integer) this.mTargetIds.get(i)).intValue()) == j) {
                    return true;
                }
            }
        }
        if (view == null || this.mTargets.size() <= 0) {
            return false;
        }
        for (int i2 = 0; i2 < this.mTargets.size(); i2++) {
            if (this.mTargets.get(i2) == view) {
                return true;
            }
        }
        return false;
    }

    private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
        ArrayMap<Animator, AnimationInfo> arrayMap = (ArrayMap) sRunningAnimators.get();
        if (arrayMap != null) {
            return arrayMap;
        }
        arrayMap = new ArrayMap();
        sRunningAnimators.set(arrayMap);
        return arrayMap;
    }

    protected void runAnimators() {
        start();
        ArrayMap runningAnimators = getRunningAnimators();
        Iterator it = this.mAnimators.iterator();
        while (it.hasNext()) {
            Animator animator = (Animator) it.next();
            if (runningAnimators.containsKey(animator)) {
                start();
                runAnimator(animator, runningAnimators);
            }
        }
        this.mAnimators.clear();
        end();
    }

    private void runAnimator(Animator animator, final ArrayMap<Animator, AnimationInfo> arrayMap) {
        if (animator != null) {
            animator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animator) {
                    Transition.this.mCurrentAnimators.add(animator);
                }

                public void onAnimationEnd(Animator animator) {
                    arrayMap.remove(animator);
                    Transition.this.mCurrentAnimators.remove(animator);
                }
            });
            animate(animator);
        }
    }

    void captureValues(ViewGroup viewGroup, boolean z) {
        int i = 0;
        clearValues(z);
        if (this.mTargetIds.size() > 0 || this.mTargets.size() > 0) {
            if (this.mTargetIds.size() > 0) {
                for (int i2 = 0; i2 < this.mTargetIds.size(); i2++) {
                    int intValue = ((Integer) this.mTargetIds.get(i2)).intValue();
                    View findViewById = viewGroup.findViewById(intValue);
                    if (findViewById != null) {
                        TransitionValues transitionValues = new TransitionValues();
                        transitionValues.view = findViewById;
                        if (z) {
                            captureStartValues(transitionValues);
                        } else {
                            captureEndValues(transitionValues);
                        }
                        if (z) {
                            this.mStartValues.mViewValues.put(findViewById, transitionValues);
                            if (intValue >= 0) {
                                this.mStartValues.mIdValues.put(intValue, transitionValues);
                            }
                        } else {
                            this.mEndValues.mViewValues.put(findViewById, transitionValues);
                            if (intValue >= 0) {
                                this.mEndValues.mIdValues.put(intValue, transitionValues);
                            }
                        }
                    }
                }
            }
            if (this.mTargets.size() > 0) {
                while (i < this.mTargets.size()) {
                    View view = (View) this.mTargets.get(i);
                    if (view != null) {
                        TransitionValues transitionValues2 = new TransitionValues();
                        transitionValues2.view = view;
                        if (z) {
                            captureStartValues(transitionValues2);
                        } else {
                            captureEndValues(transitionValues2);
                        }
                        if (z) {
                            this.mStartValues.mViewValues.put(view, transitionValues2);
                        } else {
                            this.mEndValues.mViewValues.put(view, transitionValues2);
                        }
                    }
                    i++;
                }
                return;
            }
            return;
        }
        captureHierarchy(viewGroup, z);
    }

    void clearValues(boolean z) {
        if (z) {
            this.mStartValues.mViewValues.clear();
            this.mStartValues.mIdValues.clear();
            this.mStartValues.mItemIdValues.clear();
            return;
        }
        this.mEndValues.mViewValues.clear();
        this.mEndValues.mIdValues.clear();
        this.mEndValues.mItemIdValues.clear();
    }

    private void captureHierarchy(View view, boolean z) {
        if (view != null) {
            boolean z2;
            if (view.getParent() instanceof ListView) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!z2 || ((ListView) view.getParent()).getAdapter().hasStableIds()) {
                int i;
                long j;
                if (z2) {
                    ListView listView = (ListView) view.getParent();
                    long itemIdAtPosition = listView.getItemIdAtPosition(listView.getPositionForView(view));
                    ViewCompat.setHasTransientState(view, true);
                    i = -1;
                    j = itemIdAtPosition;
                } else {
                    i = view.getId();
                    j = -1;
                }
                if (this.mTargetIdExcludes != null && this.mTargetIdExcludes.contains(Integer.valueOf(i))) {
                    return;
                }
                if (this.mTargetExcludes == null || !this.mTargetExcludes.contains(view)) {
                    if (!(this.mTargetTypeExcludes == null || view == null)) {
                        int size = this.mTargetTypeExcludes.size();
                        int i2 = 0;
                        while (i2 < size) {
                            if (!((Class) this.mTargetTypeExcludes.get(i2)).isInstance(view)) {
                                i2++;
                            } else {
                                return;
                            }
                        }
                    }
                    TransitionValues transitionValues = new TransitionValues();
                    transitionValues.view = view;
                    if (z) {
                        captureStartValues(transitionValues);
                    } else {
                        captureEndValues(transitionValues);
                    }
                    if (z) {
                        if (z2) {
                            this.mStartValues.mItemIdValues.put(j, transitionValues);
                        } else {
                            this.mStartValues.mViewValues.put(view, transitionValues);
                            if (i >= 0) {
                                this.mStartValues.mIdValues.put(i, transitionValues);
                            }
                        }
                    } else if (z2) {
                        this.mEndValues.mItemIdValues.put(j, transitionValues);
                    } else {
                        this.mEndValues.mViewValues.put(view, transitionValues);
                        if (i >= 0) {
                            this.mEndValues.mIdValues.put(i, transitionValues);
                        }
                    }
                    if (!(view instanceof ViewGroup)) {
                        return;
                    }
                    if (this.mTargetIdChildExcludes != null && this.mTargetIdChildExcludes.contains(Integer.valueOf(i))) {
                        return;
                    }
                    if (this.mTargetChildExcludes == null || !this.mTargetChildExcludes.contains(view)) {
                        if (!(this.mTargetTypeChildExcludes == null || view == null)) {
                            int size2 = this.mTargetTypeChildExcludes.size();
                            i = 0;
                            while (i < size2) {
                                if (!((Class) this.mTargetTypeChildExcludes.get(i)).isInstance(view)) {
                                    i++;
                                } else {
                                    return;
                                }
                            }
                        }
                        ViewGroup viewGroup = (ViewGroup) view;
                        for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
                            captureHierarchy(viewGroup.getChildAt(i3), z);
                        }
                    }
                }
            }
        }
    }

    public void pause(View view) {
        if (!this.mEnded) {
            ArrayMap runningAnimators = getRunningAnimators();
            int size = runningAnimators.size();
            WindowIdImpl windowId = ViewUtils.getWindowId(view);
            for (int i = size - 1; i >= 0; i--) {
                AnimationInfo animationInfo = (AnimationInfo) runningAnimators.valueAt(i);
                if (animationInfo.mView != null && windowId.equals(animationInfo.mWindowId)) {
                    ((Animator) runningAnimators.keyAt(i)).cancel();
                }
            }
            if (this.mListeners != null && this.mListeners.size() > 0) {
                ArrayList arrayList = (ArrayList) this.mListeners.clone();
                int size2 = arrayList.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    ((TransitionListener) arrayList.get(i2)).onTransitionPause(this);
                }
            }
            this.mPaused = true;
        }
    }

    public void resume(View view) {
        if (this.mPaused) {
            if (!this.mEnded) {
                ArrayMap runningAnimators = getRunningAnimators();
                int size = runningAnimators.size();
                WindowIdImpl windowId = ViewUtils.getWindowId(view);
                for (int i = size - 1; i >= 0; i--) {
                    AnimationInfo animationInfo = (AnimationInfo) runningAnimators.valueAt(i);
                    if (animationInfo.mView != null && windowId.equals(animationInfo.mWindowId)) {
                        ((Animator) runningAnimators.keyAt(i)).end();
                    }
                }
                if (this.mListeners != null && this.mListeners.size() > 0) {
                    ArrayList arrayList = (ArrayList) this.mListeners.clone();
                    int size2 = arrayList.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        ((TransitionListener) arrayList.get(i2)).onTransitionResume(this);
                    }
                }
            }
            this.mPaused = false;
        }
    }

    void playTransition(ViewGroup viewGroup) {
        ArrayMap runningAnimators = getRunningAnimators();
        for (int size = runningAnimators.size() - 1; size >= 0; size--) {
            Animator animator = (Animator) runningAnimators.keyAt(size);
            if (animator != null) {
                AnimationInfo animationInfo = (AnimationInfo) runningAnimators.get(animator);
                if (!(animationInfo == null || animationInfo.mView == null || animationInfo.mView.getContext() != viewGroup.getContext())) {
                    TransitionValues transitionValues;
                    Object obj;
                    TransitionValues transitionValues2 = animationInfo.mValues;
                    View view = animationInfo.mView;
                    TransitionValues transitionValues3 = this.mEndValues.mViewValues != null ? (TransitionValues) this.mEndValues.mViewValues.get(view) : null;
                    if (transitionValues3 == null) {
                        transitionValues = (TransitionValues) this.mEndValues.mIdValues.get(view.getId());
                    } else {
                        transitionValues = transitionValues3;
                    }
                    if (!(transitionValues2 == null || transitionValues == null)) {
                        for (String str : transitionValues2.values.keySet()) {
                            Object obj2 = transitionValues2.values.get(str);
                            obj = transitionValues.values.get(str);
                            if (obj2 != null && obj != null && !obj2.equals(obj)) {
                                obj = 1;
                                break;
                            }
                        }
                    }
                    obj = null;
                    if (obj != null) {
                        if (animator.isRunning() || animator.isStarted()) {
                            animator.cancel();
                        } else {
                            runningAnimators.remove(animator);
                        }
                    }
                }
            }
        }
        createAnimators(viewGroup, this.mStartValues, this.mEndValues);
        runAnimators();
    }

    protected void animate(Animator animator) {
        if (animator == null) {
            end();
            return;
        }
        if (getDuration() >= 0) {
            animator.setDuration(getDuration());
        }
        if (getStartDelay() >= 0) {
            animator.setStartDelay(getStartDelay());
        }
        if (getInterpolator() != null) {
            animator.setInterpolator(getInterpolator());
        }
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                Transition.this.end();
                animator.removeListener(this);
            }
        });
        animator.start();
    }

    protected void start() {
        if (this.mNumInstances == 0) {
            if (this.mListeners != null && this.mListeners.size() > 0) {
                ArrayList arrayList = (ArrayList) this.mListeners.clone();
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    ((TransitionListener) arrayList.get(i)).onTransitionStart(this);
                }
            }
            this.mEnded = false;
        }
        this.mNumInstances++;
    }

    protected void end() {
        this.mNumInstances--;
        if (this.mNumInstances == 0) {
            int i;
            View view;
            if (this.mListeners != null && this.mListeners.size() > 0) {
                ArrayList arrayList = (ArrayList) this.mListeners.clone();
                int size = arrayList.size();
                for (int i2 = 0; i2 < size; i2++) {
                    ((TransitionListener) arrayList.get(i2)).onTransitionEnd(this);
                }
            }
            for (i = 0; i < this.mStartValues.mItemIdValues.size(); i++) {
                view = ((TransitionValues) this.mStartValues.mItemIdValues.valueAt(i)).view;
                if (ViewCompat.hasTransientState(view)) {
                    ViewCompat.setHasTransientState(view, false);
                }
            }
            for (i = 0; i < this.mEndValues.mItemIdValues.size(); i++) {
                view = ((TransitionValues) this.mEndValues.mItemIdValues.valueAt(i)).view;
                if (ViewCompat.hasTransientState(view)) {
                    ViewCompat.setHasTransientState(view, false);
                }
            }
            this.mEnded = true;
        }
    }

    public Transition addListener(TransitionListener transitionListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(transitionListener);
        return this;
    }

    public Transition removeListener(TransitionListener transitionListener) {
        if (this.mListeners != null) {
            this.mListeners.remove(transitionListener);
            if (this.mListeners.size() == 0) {
                this.mListeners = null;
            }
        }
        return this;
    }

    public String toString() {
        return toString("");
    }

    public Transition clone() {
        try {
            Transition transition = (Transition) super.clone();
            transition.mAnimators = new ArrayList();
            transition.mStartValues = new TransitionValuesMaps();
            transition.mEndValues = new TransitionValuesMaps();
            return transition;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public String getName() {
        return this.mName;
    }

    String toString(String str) {
        int i = 0;
        String str2 = str + getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + ": ";
        if (this.mDuration != -1) {
            str2 = str2 + "dur(" + this.mDuration + ") ";
        }
        if (this.mStartDelay != -1) {
            str2 = str2 + "dly(" + this.mStartDelay + ") ";
        }
        if (this.mInterpolator != null) {
            str2 = str2 + "interp(" + this.mInterpolator + ") ";
        }
        if (this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0) {
            return str2;
        }
        String str3;
        str2 = str2 + "tgts(";
        if (this.mTargetIds.size() > 0) {
            str3 = str2;
            for (int i2 = 0; i2 < this.mTargetIds.size(); i2++) {
                if (i2 > 0) {
                    str3 = str3 + ", ";
                }
                str3 = str3 + this.mTargetIds.get(i2);
            }
        } else {
            str3 = str2;
        }
        if (this.mTargets.size() > 0) {
            while (i < this.mTargets.size()) {
                if (i > 0) {
                    str3 = str3 + ", ";
                }
                str3 = str3 + this.mTargets.get(i);
                i++;
            }
        }
        return str3 + ")";
    }
}
