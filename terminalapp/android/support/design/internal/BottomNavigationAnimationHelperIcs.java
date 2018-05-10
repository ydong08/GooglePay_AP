package android.support.design.internal;

import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.ViewGroup;

class BottomNavigationAnimationHelperIcs extends BottomNavigationAnimationHelperBase {
    private final TransitionSet mSet = new AutoTransition();

    BottomNavigationAnimationHelperIcs() {
        this.mSet.setOrdering(0);
        this.mSet.setDuration(115);
        this.mSet.setInterpolator(new FastOutSlowInInterpolator());
        this.mSet.addTransition(new TextScale());
    }

    void beginDelayedTransition(ViewGroup viewGroup) {
        TransitionManager.beginDelayedTransition(viewGroup, this.mSet);
    }
}
