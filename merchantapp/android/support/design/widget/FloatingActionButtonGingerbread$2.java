package android.support.design.widget;

import android.view.animation.Animation;

class FloatingActionButtonGingerbread$2 extends AnimationListenerAdapter {
    final /* synthetic */ FloatingActionButtonImpl this$0$9HGMSP3IDTKM8BRJELO70RRIEGNM8PBJD5JMSBRND5I6EPBK5T36ORR1EHKMSPQ1CDQ6IRRE89QN8T3FDP3MIRJ7CLP64SJ5C5I3M___0;
    final /* synthetic */ InternalVisibilityChangedListener val$listener;

    FloatingActionButtonGingerbread$2(FloatingActionButtonImpl floatingActionButtonImpl, InternalVisibilityChangedListener internalVisibilityChangedListener) {
        this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNM8PBJD5JMSBRND5I6EPBK5T36ORR1EHKMSPQ1CDQ6IRRE89QN8T3FDP3MIRJ7CLP64SJ5C5I3M___0 = floatingActionButtonImpl;
        this.val$listener = internalVisibilityChangedListener;
    }

    public void onAnimationEnd(Animation animation) {
        this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNM8PBJD5JMSBRND5I6EPBK5T36ORR1EHKMSPQ1CDQ6IRRE89QN8T3FDP3MIRJ7CLP64SJ5C5I3M___0.mAnimState = 0;
    }
}
