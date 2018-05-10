package android.support.v4.view;

import android.view.View;
import java.lang.ref.WeakReference;

class ViewPropertyAnimatorCompat$BaseViewPropertyAnimatorCompatImpl$Starter implements Runnable {
    WeakReference<View> mViewRef;
    ViewPropertyAnimatorCompat mVpa;
    final /* synthetic */ ViewPropertyAnimatorCompatImpl this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCD1FEPKMATPFAPKMATQGE9NN0PBIEHSK2RJ9DLGN8RRI8DNMQS31EGI44OBJCLB6IPBNA1P6US35E9Q7IGBED5MM2T3FE91MURBGC5Q4IRBGDGTG____0;

    ViewPropertyAnimatorCompat$BaseViewPropertyAnimatorCompatImpl$Starter(ViewPropertyAnimatorCompatImpl viewPropertyAnimatorCompatImpl, ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
        this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCD1FEPKMATPFAPKMATQGE9NN0PBIEHSK2RJ9DLGN8RRI8DNMQS31EGI44OBJCLB6IPBNA1P6US35E9Q7IGBED5MM2T3FE91MURBGC5Q4IRBGDGTG____0 = viewPropertyAnimatorCompatImpl;
        this.mViewRef = new WeakReference(view);
        this.mVpa = viewPropertyAnimatorCompat;
    }

    public void run() {
        View view = (View) this.mViewRef.get();
        if (view != null) {
            this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCD1FEPKMATPFAPKMATQGE9NN0PBIEHSK2RJ9DLGN8RRI8DNMQS31EGI44OBJCLB6IPBNA1P6US35E9Q7IGBED5MM2T3FE91MURBGC5Q4IRBGDGTG____0.startAnimation(this.mVpa, view);
        }
    }
}
