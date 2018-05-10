package android.support.v7.widget;

import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;

class ToolbarWidgetWrapper$2 extends ViewPropertyAnimatorListenerAdapter {
    private boolean mCanceled = false;
    final /* synthetic */ DecorToolbar this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNL8RRFDHH62SIND5I6EPBKATP62S3GCLP3M___0;
    final /* synthetic */ int val$visibility;

    ToolbarWidgetWrapper$2(DecorToolbar decorToolbar, int i) {
        this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNL8RRFDHH62SIND5I6EPBKATP62S3GCLP3M___0 = decorToolbar;
        this.val$visibility = i;
    }

    public void onAnimationStart(View view) {
        this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNL8RRFDHH62SIND5I6EPBKATP62S3GCLP3M___0.mToolbar.setVisibility(0);
    }

    public void onAnimationEnd(View view) {
        if (!this.mCanceled) {
            this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNL8RRFDHH62SIND5I6EPBKATP62S3GCLP3M___0.mToolbar.setVisibility(this.val$visibility);
        }
    }

    public void onAnimationCancel(View view) {
        this.mCanceled = true;
    }
}
