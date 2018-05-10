package android.support.design.widget;

import android.view.animation.Animation;

class FloatingActionButtonGingerbread$1 extends AnimationListenerAdapter {
    final /* synthetic */ FloatingActionButtonImpl this$0$9HGMSP3IDTKM8BRJELO70RRIEGNM8PBJD5JMSBRND5I6EPBK5T36ORR1EHKMSPQ1CDQ6IRRE89QN8T3FDP3MIRJ7CLP64SJ5C5I3M___0;
    final /* synthetic */ boolean val$fromUser;
    final /* synthetic */ InternalVisibilityChangedListener val$listener;

    FloatingActionButtonGingerbread$1(FloatingActionButtonImpl floatingActionButtonImpl, boolean z, InternalVisibilityChangedListener internalVisibilityChangedListener) {
        this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNM8PBJD5JMSBRND5I6EPBK5T36ORR1EHKMSPQ1CDQ6IRRE89QN8T3FDP3MIRJ7CLP64SJ5C5I3M___0 = floatingActionButtonImpl;
        this.val$fromUser = z;
        this.val$listener = internalVisibilityChangedListener;
    }

    public void onAnimationEnd(Animation animation) {
        this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNM8PBJD5JMSBRND5I6EPBK5T36ORR1EHKMSPQ1CDQ6IRRE89QN8T3FDP3MIRJ7CLP64SJ5C5I3M___0.mAnimState = 0;
        this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNM8PBJD5JMSBRND5I6EPBK5T36ORR1EHKMSPQ1CDQ6IRRE89QN8T3FDP3MIRJ7CLP64SJ5C5I3M___0.mView.internalSetVisibility(this.val$fromUser ? 8 : 4, this.val$fromUser);
    }
}
