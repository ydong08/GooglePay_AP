package android.support.transition;

import android.view.ViewGroup;

class ViewGroupUtilsImpl {
    ViewGroupUtilsImpl() {
    }

    public ViewGroupOverlayImpl getOverlay(ViewGroup viewGroup) {
        return ViewGroupOverlayApi14.createFrom(viewGroup);
    }

    public void suppressLayout(ViewGroup viewGroup, boolean z) {
    }
}
