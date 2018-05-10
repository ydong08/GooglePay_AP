package android.support.design.widget;

import android.graphics.drawable.Drawable;

class ShadowViewDelegate {
    final /* synthetic */ FloatingActionButton this$0;

    ShadowViewDelegate(FloatingActionButton floatingActionButton) {
        this.this$0 = floatingActionButton;
    }

    public float getRadius() {
        return ((float) this.this$0.getSizeDimension()) / 2.0f;
    }

    public void setShadowPadding(int i, int i2, int i3, int i4) {
        this.this$0.mShadowPadding.set(i, i2, i3, i4);
        this.this$0.setPadding(this.this$0.mImagePadding + i, this.this$0.mImagePadding + i2, this.this$0.mImagePadding + i3, this.this$0.mImagePadding + i4);
    }

    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
    }

    public boolean isCompatPaddingEnabled() {
        return this.this$0.mCompatPadding;
    }
}
