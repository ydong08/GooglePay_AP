package android.support.transition;

import android.view.ViewGroup;

class ViewGroupUtilsApi18 extends ViewGroupUtilsImpl {
    ViewGroupUtilsApi18() {
    }

    public ViewGroupOverlayImpl getOverlay(ViewGroup viewGroup) {
        return new ViewGroupOverlayApi18(viewGroup);
    }
}
