package android.support.transition;

import android.animation.TimeInterpolator;
import android.support.transition.Transition.TransitionListener;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;

public class TransitionSet extends Transition {
    private int mCurrentListeners;
    private boolean mPlayTogether = true;
    private boolean mStarted = false;
    private ArrayList<Transition> mTransitions = new ArrayList();

    static class TransitionSetListener extends TransitionListener {
        TransitionSet mTransitionSet;

        TransitionSetListener(TransitionSet transitionSet) {
            this.mTransitionSet = transitionSet;
        }

        public void onTransitionStart(Transition transition) {
            if (!this.mTransitionSet.mStarted) {
                this.mTransitionSet.start();
                this.mTransitionSet.mStarted = true;
            }
        }

        public void onTransitionEnd(Transition transition) {
            TransitionSet.access$106(this.mTransitionSet);
            if (this.mTransitionSet.mCurrentListeners == 0) {
                this.mTransitionSet.mStarted = false;
                this.mTransitionSet.end();
            }
            transition.removeListener(this);
        }
    }

    static /* synthetic */ int access$106(TransitionSet transitionSet) {
        int i = transitionSet.mCurrentListeners - 1;
        transitionSet.mCurrentListeners = i;
        return i;
    }

    public TransitionSet setOrdering(int i) {
        switch (i) {
            case 0:
                this.mPlayTogether = true;
                break;
            case 1:
                this.mPlayTogether = false;
                break;
            default:
                throw new AndroidRuntimeException("Invalid parameter for TransitionSet ordering: " + i);
        }
        return this;
    }

    public TransitionSet addTransition(Transition transition) {
        this.mTransitions.add(transition);
        transition.mParent = this;
        if (this.mDuration >= 0) {
            transition.setDuration(this.mDuration);
        }
        return this;
    }

    public TransitionSet setDuration(long j) {
        super.setDuration(j);
        if (this.mDuration >= 0) {
            int size = this.mTransitions.size();
            for (int i = 0; i < size; i++) {
                ((Transition) this.mTransitions.get(i)).setDuration(j);
            }
        }
        return this;
    }

    public TransitionSet setInterpolator(TimeInterpolator timeInterpolator) {
        return (TransitionSet) super.setInterpolator(timeInterpolator);
    }

    public TransitionSet addListener(TransitionListener transitionListener) {
        return (TransitionSet) super.addListener(transitionListener);
    }

    public TransitionSet removeListener(TransitionListener transitionListener) {
        return (TransitionSet) super.removeListener(transitionListener);
    }

    private void setupStartEndListeners() {
        TransitionListener transitionSetListener = new TransitionSetListener(this);
        Iterator it = this.mTransitions.iterator();
        while (it.hasNext()) {
            ((Transition) it.next()).addListener(transitionSetListener);
        }
        this.mCurrentListeners = this.mTransitions.size();
    }

    protected void createAnimators(ViewGroup viewGroup, TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2) {
        Iterator it = this.mTransitions.iterator();
        while (it.hasNext()) {
            ((Transition) it.next()).createAnimators(viewGroup, transitionValuesMaps, transitionValuesMaps2);
        }
    }

    protected void runAnimators() {
        if (this.mTransitions.isEmpty()) {
            start();
            end();
            return;
        }
        setupStartEndListeners();
        if (this.mPlayTogether) {
            Iterator it = this.mTransitions.iterator();
            while (it.hasNext()) {
                ((Transition) it.next()).runAnimators();
            }
            return;
        }
        for (int i = 1; i < this.mTransitions.size(); i++) {
            final Transition transition = (Transition) this.mTransitions.get(i);
            ((Transition) this.mTransitions.get(i - 1)).addListener(new TransitionListener() {
                public void onTransitionEnd(Transition transition) {
                    transition.runAnimators();
                    transition.removeListener(this);
                }
            });
        }
        Transition transition2 = (Transition) this.mTransitions.get(0);
        if (transition2 != null) {
            transition2.runAnimators();
        }
    }

    public void captureStartValues(TransitionValues transitionValues) {
        int id = transitionValues.view.getId();
        if (isValidTarget(transitionValues.view, (long) id)) {
            Iterator it = this.mTransitions.iterator();
            while (it.hasNext()) {
                Transition transition = (Transition) it.next();
                if (transition.isValidTarget(transitionValues.view, (long) id)) {
                    transition.captureStartValues(transitionValues);
                }
            }
        }
    }

    public void captureEndValues(TransitionValues transitionValues) {
        int id = transitionValues.view.getId();
        if (isValidTarget(transitionValues.view, (long) id)) {
            Iterator it = this.mTransitions.iterator();
            while (it.hasNext()) {
                Transition transition = (Transition) it.next();
                if (transition.isValidTarget(transitionValues.view, (long) id)) {
                    transition.captureEndValues(transitionValues);
                }
            }
        }
    }

    public void pause(View view) {
        super.pause(view);
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            ((Transition) this.mTransitions.get(i)).pause(view);
        }
    }

    public void resume(View view) {
        super.resume(view);
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            ((Transition) this.mTransitions.get(i)).resume(view);
        }
    }

    String toString(String str) {
        String transition = super.toString(str);
        int i = 0;
        while (i < this.mTransitions.size()) {
            String str2 = transition + "\n" + ((Transition) this.mTransitions.get(i)).toString(str + "  ");
            i++;
            transition = str2;
        }
        return transition;
    }

    public Transition clone() {
        TransitionSet transitionSet = (TransitionSet) super.clone();
        transitionSet.mTransitions = new ArrayList();
        int size = this.mTransitions.size();
        for (int i = 0; i < size; i++) {
            transitionSet.addTransition(((Transition) this.mTransitions.get(i)).clone());
        }
        return transitionSet;
    }
}
