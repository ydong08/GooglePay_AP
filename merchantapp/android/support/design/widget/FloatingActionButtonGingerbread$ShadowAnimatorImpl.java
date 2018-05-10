package android.support.design.widget;

abstract class FloatingActionButtonGingerbread$ShadowAnimatorImpl extends AnimatorListener implements AnimatorUpdateListener {
    private float mShadowSizeEnd;
    private float mShadowSizeStart;
    private boolean mValidValues;
    final /* synthetic */ FloatingActionButtonImpl this$0$9HGMSP3IDTKM8BRJELO70RRIEGNM8PBJD5JMSBRND5I6EPBK5T36ORR1EHKMSPQ1CDQ6IRRE89QN8T3FDP3MIRJ7CLP64SJ5C5I3M___0;

    protected abstract float getTargetShadowSize();

    private FloatingActionButtonGingerbread$ShadowAnimatorImpl(FloatingActionButtonImpl floatingActionButtonImpl) {
        this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNM8PBJD5JMSBRND5I6EPBK5T36ORR1EHKMSPQ1CDQ6IRRE89QN8T3FDP3MIRJ7CLP64SJ5C5I3M___0 = floatingActionButtonImpl;
    }

    public void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat) {
        if (!this.mValidValues) {
            this.mShadowSizeStart = this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNM8PBJD5JMSBRND5I6EPBK5T36ORR1EHKMSPQ1CDQ6IRRE89QN8T3FDP3MIRJ7CLP64SJ5C5I3M___0.mShadowDrawable.getShadowSize();
            this.mShadowSizeEnd = getTargetShadowSize();
            this.mValidValues = true;
        }
        this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNM8PBJD5JMSBRND5I6EPBK5T36ORR1EHKMSPQ1CDQ6IRRE89QN8T3FDP3MIRJ7CLP64SJ5C5I3M___0.mShadowDrawable.setShadowSize(this.mShadowSizeStart + ((this.mShadowSizeEnd - this.mShadowSizeStart) * valueAnimatorCompat.getAnimatedFraction()));
    }

    public void onAnimationEnd(ValueAnimatorCompat valueAnimatorCompat) {
        this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNM8PBJD5JMSBRND5I6EPBK5T36ORR1EHKMSPQ1CDQ6IRRE89QN8T3FDP3MIRJ7CLP64SJ5C5I3M___0.mShadowDrawable.setShadowSize(this.mShadowSizeEnd);
        this.mValidValues = false;
    }
}
