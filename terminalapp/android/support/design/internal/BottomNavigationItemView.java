package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView.ItemView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class BottomNavigationItemView extends FrameLayout implements ItemView {
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private final int mDefaultMargin;
    private ImageView mIcon;
    private ColorStateList mIconTint;
    private MenuItemImpl mItemData;
    private int mItemPosition;
    private final TextView mLargeLabel;
    private final float mScaleDownFactor;
    private final float mScaleUpFactor;
    private final int mShiftAmount;
    private boolean mShiftingMode;
    private final TextView mSmallLabel;

    public BottomNavigationItemView(Context context) {
        this(context, null);
    }

    public BottomNavigationItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BottomNavigationItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mItemPosition = -1;
        Resources resources = getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_text_size);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_text_size);
        this.mDefaultMargin = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_margin);
        this.mShiftAmount = dimensionPixelSize - dimensionPixelSize2;
        this.mScaleUpFactor = (((float) dimensionPixelSize2) * 1.0f) / ((float) dimensionPixelSize);
        this.mScaleDownFactor = (((float) dimensionPixelSize) * 1.0f) / ((float) dimensionPixelSize2);
        LayoutInflater.from(context).inflate(R.layout.design_bottom_navigation_item, this, true);
        setBackgroundResource(R.drawable.design_bottom_navigation_item_background);
        this.mIcon = (ImageView) findViewById(R.id.icon);
        this.mSmallLabel = (TextView) findViewById(R.id.smallLabel);
        this.mLargeLabel = (TextView) findViewById(R.id.largeLabel);
    }

    public void initialize(MenuItemImpl menuItemImpl, int i) {
        this.mItemData = menuItemImpl;
        setCheckable(menuItemImpl.isCheckable());
        setChecked(menuItemImpl.isChecked());
        setEnabled(menuItemImpl.isEnabled());
        setIcon(menuItemImpl.getIcon());
        setTitle(menuItemImpl.getTitle());
        setId(menuItemImpl.getItemId());
    }

    public void setItemPosition(int i) {
        this.mItemPosition = i;
    }

    public int getItemPosition() {
        return this.mItemPosition;
    }

    public void setShiftingMode(boolean z) {
        this.mShiftingMode = z;
    }

    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void setTitle(CharSequence charSequence) {
        this.mSmallLabel.setText(charSequence);
        this.mLargeLabel.setText(charSequence);
    }

    public void setCheckable(boolean z) {
        refreshDrawableState();
    }

    public void setChecked(boolean z) {
        ViewCompat.setPivotX(this.mLargeLabel, (float) (this.mLargeLabel.getWidth() / 2));
        ViewCompat.setPivotY(this.mLargeLabel, (float) this.mLargeLabel.getBaseline());
        ViewCompat.setPivotX(this.mSmallLabel, (float) (this.mSmallLabel.getWidth() / 2));
        ViewCompat.setPivotY(this.mSmallLabel, (float) this.mSmallLabel.getBaseline());
        LayoutParams layoutParams;
        if (this.mShiftingMode) {
            if (z) {
                layoutParams = (LayoutParams) this.mIcon.getLayoutParams();
                layoutParams.gravity = 49;
                layoutParams.topMargin = this.mDefaultMargin;
                this.mIcon.setLayoutParams(layoutParams);
                this.mLargeLabel.setVisibility(0);
                ViewCompat.setScaleX(this.mLargeLabel, 1.0f);
                ViewCompat.setScaleY(this.mLargeLabel, 1.0f);
            } else {
                layoutParams = (LayoutParams) this.mIcon.getLayoutParams();
                layoutParams.gravity = 17;
                layoutParams.topMargin = this.mDefaultMargin;
                this.mIcon.setLayoutParams(layoutParams);
                this.mLargeLabel.setVisibility(4);
                ViewCompat.setScaleX(this.mLargeLabel, 0.5f);
                ViewCompat.setScaleY(this.mLargeLabel, 0.5f);
            }
            this.mSmallLabel.setVisibility(4);
        } else if (z) {
            layoutParams = (LayoutParams) this.mIcon.getLayoutParams();
            layoutParams.gravity = 49;
            layoutParams.topMargin = this.mDefaultMargin + this.mShiftAmount;
            this.mIcon.setLayoutParams(layoutParams);
            this.mLargeLabel.setVisibility(0);
            this.mSmallLabel.setVisibility(4);
            ViewCompat.setScaleX(this.mLargeLabel, 1.0f);
            ViewCompat.setScaleY(this.mLargeLabel, 1.0f);
            ViewCompat.setScaleX(this.mSmallLabel, this.mScaleUpFactor);
            ViewCompat.setScaleY(this.mSmallLabel, this.mScaleUpFactor);
        } else {
            layoutParams = (LayoutParams) this.mIcon.getLayoutParams();
            layoutParams.gravity = 49;
            layoutParams.topMargin = this.mDefaultMargin;
            this.mIcon.setLayoutParams(layoutParams);
            this.mLargeLabel.setVisibility(4);
            this.mSmallLabel.setVisibility(0);
            ViewCompat.setScaleX(this.mLargeLabel, this.mScaleDownFactor);
            ViewCompat.setScaleY(this.mLargeLabel, this.mScaleDownFactor);
            ViewCompat.setScaleX(this.mSmallLabel, 1.0f);
            ViewCompat.setScaleY(this.mSmallLabel, 1.0f);
        }
        refreshDrawableState();
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mSmallLabel.setEnabled(z);
        this.mLargeLabel.setEnabled(z);
        this.mIcon.setEnabled(z);
        if (z) {
            ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(getContext(), 1002));
        } else {
            ViewCompat.setPointerIcon(this, null);
        }
    }

    public int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
        if (this.mItemData != null && this.mItemData.isCheckable() && this.mItemData.isChecked()) {
            mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    public void setShortcut(boolean z, char c) {
    }

    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            ConstantState constantState = drawable.getConstantState();
            if (constantState != null) {
                drawable = constantState.newDrawable();
            }
            drawable = DrawableCompat.wrap(drawable).mutate();
            DrawableCompat.setTintList(drawable, this.mIconTint);
        }
        this.mIcon.setImageDrawable(drawable);
    }

    public boolean prefersCondensedTitle() {
        return false;
    }

    public void setIconTintList(ColorStateList colorStateList) {
        this.mIconTint = colorStateList;
        if (this.mItemData != null) {
            setIcon(this.mItemData.getIcon());
        }
    }

    public void setTextColor(ColorStateList colorStateList) {
        this.mSmallLabel.setTextColor(colorStateList);
        this.mLargeLabel.setTextColor(colorStateList);
    }

    public void setItemBackground(int i) {
        Drawable drawable;
        if (i == 0) {
            drawable = null;
        } else {
            drawable = ContextCompat.getDrawable(getContext(), i);
        }
        ViewCompat.setBackground(this, drawable);
    }
}
