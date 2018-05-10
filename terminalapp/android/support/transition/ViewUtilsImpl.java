package android.support.transition;

import android.view.View;

class ViewUtilsImpl {
    ViewUtilsImpl() {
    }

    public ViewOverlayImpl getOverlay(View view) {
        return ViewOverlayApi14.createFrom(view);
    }

    public WindowIdImpl getWindowId(View view) {
        return new WindowIdApi14(view.getWindowToken());
    }
}
