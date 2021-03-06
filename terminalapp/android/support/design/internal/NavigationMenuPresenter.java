package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.R;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuPresenter.Callback;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;

public class NavigationMenuPresenter implements MenuPresenter {
    NavigationMenuAdapter mAdapter;
    private Callback mCallback;
    LinearLayout mHeaderLayout;
    ColorStateList mIconTintList;
    private int mId;
    Drawable mItemBackground;
    LayoutInflater mLayoutInflater;
    MenuBuilder mMenu;
    private NavigationMenuView mMenuView;
    final OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View view) {
            NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView) view;
            NavigationMenuPresenter.this.setUpdateSuspended(true);
            MenuItemImpl itemData = navigationMenuItemView.getItemData();
            boolean performItemAction = NavigationMenuPresenter.this.mMenu.performItemAction(itemData, NavigationMenuPresenter.this, 0);
            if (itemData != null && itemData.isCheckable() && performItemAction) {
                NavigationMenuPresenter.this.mAdapter.setCheckedItem(itemData);
            }
            NavigationMenuPresenter.this.setUpdateSuspended(false);
            NavigationMenuPresenter.this.updateMenuView(false);
        }
    };
    int mPaddingSeparator;
    private int mPaddingTopDefault;
    int mTextAppearance;
    boolean mTextAppearanceSet;
    ColorStateList mTextColor;

    static abstract class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    static class HeaderViewHolder extends ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    class NavigationMenuAdapter extends Adapter<ViewHolder> {
        private MenuItemImpl mCheckedItem;
        private final ArrayList<NavigationMenuItem> mItems = new ArrayList();
        private boolean mUpdateSuspended;

        NavigationMenuAdapter() {
            prepareMenuItems();
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public int getItemCount() {
            return this.mItems.size();
        }

        public int getItemViewType(int i) {
            NavigationMenuItem navigationMenuItem = (NavigationMenuItem) this.mItems.get(i);
            if (navigationMenuItem instanceof NavigationMenuSeparatorItem) {
                return 2;
            }
            if (navigationMenuItem instanceof NavigationMenuHeaderItem) {
                return 3;
            }
            if (!(navigationMenuItem instanceof NavigationMenuTextItem)) {
                throw new RuntimeException("Unknown item type.");
            } else if (((NavigationMenuTextItem) navigationMenuItem).getMenuItem().hasSubMenu()) {
                return 1;
            } else {
                return 0;
            }
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            switch (i) {
                case 0:
                    return new NormalViewHolder(NavigationMenuPresenter.this.mLayoutInflater, viewGroup, NavigationMenuPresenter.this.mOnClickListener);
                case 1:
                    return new SubheaderViewHolder(NavigationMenuPresenter.this.mLayoutInflater, viewGroup);
                case 2:
                    return new SeparatorViewHolder(NavigationMenuPresenter.this.mLayoutInflater, viewGroup);
                case 3:
                    return new HeaderViewHolder(NavigationMenuPresenter.this.mHeaderLayout);
                default:
                    return null;
            }
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            switch (getItemViewType(i)) {
                case 0:
                    NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView) viewHolder.itemView;
                    navigationMenuItemView.setIconTintList(NavigationMenuPresenter.this.mIconTintList);
                    if (NavigationMenuPresenter.this.mTextAppearanceSet) {
                        navigationMenuItemView.setTextAppearance(NavigationMenuPresenter.this.mTextAppearance);
                    }
                    if (NavigationMenuPresenter.this.mTextColor != null) {
                        navigationMenuItemView.setTextColor(NavigationMenuPresenter.this.mTextColor);
                    }
                    ViewCompat.setBackground(navigationMenuItemView, NavigationMenuPresenter.this.mItemBackground != null ? NavigationMenuPresenter.this.mItemBackground.getConstantState().newDrawable() : null);
                    NavigationMenuTextItem navigationMenuTextItem = (NavigationMenuTextItem) this.mItems.get(i);
                    navigationMenuItemView.setNeedsEmptyIcon(navigationMenuTextItem.needsEmptyIcon);
                    navigationMenuItemView.initialize(navigationMenuTextItem.getMenuItem(), 0);
                    return;
                case 1:
                    ((TextView) viewHolder.itemView).setText(((NavigationMenuTextItem) this.mItems.get(i)).getMenuItem().getTitle());
                    return;
                case 2:
                    NavigationMenuSeparatorItem navigationMenuSeparatorItem = (NavigationMenuSeparatorItem) this.mItems.get(i);
                    viewHolder.itemView.setPadding(0, navigationMenuSeparatorItem.getPaddingTop(), 0, navigationMenuSeparatorItem.getPaddingBottom());
                    return;
                default:
                    return;
            }
        }

        public void onViewRecycled(ViewHolder viewHolder) {
            if (viewHolder instanceof NormalViewHolder) {
                ((NavigationMenuItemView) viewHolder.itemView).recycle();
            }
        }

        public void update() {
            prepareMenuItems();
            notifyDataSetChanged();
        }

        private void prepareMenuItems() {
            if (!this.mUpdateSuspended) {
                this.mUpdateSuspended = true;
                this.mItems.clear();
                this.mItems.add(new NavigationMenuHeaderItem());
                int i = -1;
                int i2 = 0;
                boolean z = false;
                int size = NavigationMenuPresenter.this.mMenu.getVisibleItems().size();
                int i3 = 0;
                while (i3 < size) {
                    int i4;
                    MenuItemImpl menuItemImpl = (MenuItemImpl) NavigationMenuPresenter.this.mMenu.getVisibleItems().get(i3);
                    if (menuItemImpl.isChecked()) {
                        setCheckedItem(menuItemImpl);
                    }
                    if (menuItemImpl.isCheckable()) {
                        menuItemImpl.setExclusiveCheckable(false);
                    }
                    int i5;
                    if (menuItemImpl.hasSubMenu()) {
                        SubMenu subMenu = menuItemImpl.getSubMenu();
                        if (subMenu.hasVisibleItems()) {
                            if (i3 != 0) {
                                this.mItems.add(new NavigationMenuSeparatorItem(NavigationMenuPresenter.this.mPaddingSeparator, 0));
                            }
                            this.mItems.add(new NavigationMenuTextItem(menuItemImpl));
                            Object obj = null;
                            int size2 = this.mItems.size();
                            int size3 = subMenu.size();
                            for (i5 = 0; i5 < size3; i5++) {
                                MenuItemImpl menuItemImpl2 = (MenuItemImpl) subMenu.getItem(i5);
                                if (menuItemImpl2.isVisible()) {
                                    if (obj == null && menuItemImpl2.getIcon() != null) {
                                        obj = 1;
                                    }
                                    if (menuItemImpl2.isCheckable()) {
                                        menuItemImpl2.setExclusiveCheckable(false);
                                    }
                                    if (menuItemImpl.isChecked()) {
                                        setCheckedItem(menuItemImpl);
                                    }
                                    this.mItems.add(new NavigationMenuTextItem(menuItemImpl2));
                                }
                            }
                            if (obj != null) {
                                appendTransparentIconIfMissing(size2, this.mItems.size());
                            }
                        }
                        i4 = i;
                    } else {
                        int size4;
                        boolean z2;
                        i5 = menuItemImpl.getGroupId();
                        if (i5 != i) {
                            size4 = this.mItems.size();
                            z2 = menuItemImpl.getIcon() != null;
                            if (i3 != 0) {
                                size4++;
                                this.mItems.add(new NavigationMenuSeparatorItem(NavigationMenuPresenter.this.mPaddingSeparator, NavigationMenuPresenter.this.mPaddingSeparator));
                            }
                        } else if (z || menuItemImpl.getIcon() == null) {
                            z2 = z;
                            size4 = i2;
                        } else {
                            z2 = true;
                            appendTransparentIconIfMissing(i2, this.mItems.size());
                            size4 = i2;
                        }
                        NavigationMenuTextItem navigationMenuTextItem = new NavigationMenuTextItem(menuItemImpl);
                        navigationMenuTextItem.needsEmptyIcon = z2;
                        this.mItems.add(navigationMenuTextItem);
                        z = z2;
                        i2 = size4;
                        i4 = i5;
                    }
                    i3++;
                    i = i4;
                }
                this.mUpdateSuspended = false;
            }
        }

        private void appendTransparentIconIfMissing(int i, int i2) {
            while (i < i2) {
                ((NavigationMenuTextItem) this.mItems.get(i)).needsEmptyIcon = true;
                i++;
            }
        }

        public void setCheckedItem(MenuItemImpl menuItemImpl) {
            if (this.mCheckedItem != menuItemImpl && menuItemImpl.isCheckable()) {
                if (this.mCheckedItem != null) {
                    this.mCheckedItem.setChecked(false);
                }
                this.mCheckedItem = menuItemImpl;
                menuItemImpl.setChecked(true);
            }
        }

        public Bundle createInstanceState() {
            Bundle bundle = new Bundle();
            if (this.mCheckedItem != null) {
                bundle.putInt("android:menu:checked", this.mCheckedItem.getItemId());
            }
            SparseArray sparseArray = new SparseArray();
            Iterator it = this.mItems.iterator();
            while (it.hasNext()) {
                NavigationMenuItem navigationMenuItem = (NavigationMenuItem) it.next();
                if (navigationMenuItem instanceof NavigationMenuTextItem) {
                    MenuItemImpl menuItem = ((NavigationMenuTextItem) navigationMenuItem).getMenuItem();
                    View actionView = menuItem != null ? menuItem.getActionView() : null;
                    if (actionView != null) {
                        SparseArray parcelableSparseArray = new ParcelableSparseArray();
                        actionView.saveHierarchyState(parcelableSparseArray);
                        sparseArray.put(menuItem.getItemId(), parcelableSparseArray);
                    }
                }
            }
            bundle.putSparseParcelableArray("android:menu:action_views", sparseArray);
            return bundle;
        }

        public void restoreInstanceState(Bundle bundle) {
            NavigationMenuItem navigationMenuItem;
            int i = bundle.getInt("android:menu:checked", 0);
            if (i != 0) {
                this.mUpdateSuspended = true;
                Iterator it = this.mItems.iterator();
                while (it.hasNext()) {
                    navigationMenuItem = (NavigationMenuItem) it.next();
                    if (navigationMenuItem instanceof NavigationMenuTextItem) {
                        MenuItemImpl menuItem = ((NavigationMenuTextItem) navigationMenuItem).getMenuItem();
                        if (menuItem != null && menuItem.getItemId() == i) {
                            setCheckedItem(menuItem);
                            break;
                        }
                    }
                }
                this.mUpdateSuspended = false;
                prepareMenuItems();
            }
            SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:action_views");
            Iterator it2 = this.mItems.iterator();
            while (it2.hasNext()) {
                navigationMenuItem = (NavigationMenuItem) it2.next();
                if (navigationMenuItem instanceof NavigationMenuTextItem) {
                    MenuItemImpl menuItem2 = ((NavigationMenuTextItem) navigationMenuItem).getMenuItem();
                    View actionView = menuItem2 != null ? menuItem2.getActionView() : null;
                    if (actionView != null) {
                        actionView.restoreHierarchyState((SparseArray) sparseParcelableArray.get(menuItem2.getItemId()));
                    }
                }
            }
        }

        public void setUpdateSuspended(boolean z) {
            this.mUpdateSuspended = z;
        }
    }

    interface NavigationMenuItem {
    }

    static class NavigationMenuHeaderItem implements NavigationMenuItem {
        NavigationMenuHeaderItem() {
        }
    }

    static class NavigationMenuSeparatorItem implements NavigationMenuItem {
        private final int mPaddingBottom;
        private final int mPaddingTop;

        public NavigationMenuSeparatorItem(int i, int i2) {
            this.mPaddingTop = i;
            this.mPaddingBottom = i2;
        }

        public int getPaddingTop() {
            return this.mPaddingTop;
        }

        public int getPaddingBottom() {
            return this.mPaddingBottom;
        }
    }

    static class NavigationMenuTextItem implements NavigationMenuItem {
        private final MenuItemImpl mMenuItem;
        boolean needsEmptyIcon;

        NavigationMenuTextItem(MenuItemImpl menuItemImpl) {
            this.mMenuItem = menuItemImpl;
        }

        public MenuItemImpl getMenuItem() {
            return this.mMenuItem;
        }
    }

    static class NormalViewHolder extends ViewHolder {
        public NormalViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, OnClickListener onClickListener) {
            super(layoutInflater.inflate(R.layout.design_navigation_item, viewGroup, false));
            this.itemView.setOnClickListener(onClickListener);
        }
    }

    static class SeparatorViewHolder extends ViewHolder {
        public SeparatorViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.design_navigation_item_separator, viewGroup, false));
        }
    }

    static class SubheaderViewHolder extends ViewHolder {
        public SubheaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.design_navigation_item_subheader, viewGroup, false));
        }
    }

    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mMenu = menuBuilder;
        this.mPaddingSeparator = context.getResources().getDimensionPixelOffset(R.dimen.design_navigation_separator_vertical_padding);
    }

    public MenuView getMenuView(ViewGroup viewGroup) {
        if (this.mMenuView == null) {
            this.mMenuView = (NavigationMenuView) this.mLayoutInflater.inflate(R.layout.design_navigation_menu, viewGroup, false);
            if (this.mAdapter == null) {
                this.mAdapter = new NavigationMenuAdapter();
            }
            this.mHeaderLayout = (LinearLayout) this.mLayoutInflater.inflate(R.layout.design_navigation_item_header, this.mMenuView, false);
            this.mMenuView.setAdapter(this.mAdapter);
        }
        return this.mMenuView;
    }

    public void updateMenuView(boolean z) {
        if (this.mAdapter != null) {
            this.mAdapter.update();
        }
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        return false;
    }

    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        if (this.mCallback != null) {
            this.mCallback.onCloseMenu(menuBuilder, z);
        }
    }

    public boolean flagActionItems() {
        return false;
    }

    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int i) {
        this.mId = i;
    }

    public Parcelable onSaveInstanceState() {
        if (VERSION.SDK_INT < 11) {
            return null;
        }
        Parcelable bundle = new Bundle();
        if (this.mMenuView != null) {
            SparseArray sparseArray = new SparseArray();
            this.mMenuView.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("android:menu:list", sparseArray);
        }
        if (this.mAdapter != null) {
            bundle.putBundle("android:menu:adapter", this.mAdapter.createInstanceState());
        }
        if (this.mHeaderLayout == null) {
            return bundle;
        }
        sparseArray = new SparseArray();
        this.mHeaderLayout.saveHierarchyState(sparseArray);
        bundle.putSparseParcelableArray("android:menu:header", sparseArray);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:list");
            if (sparseParcelableArray != null) {
                this.mMenuView.restoreHierarchyState(sparseParcelableArray);
            }
            Bundle bundle2 = bundle.getBundle("android:menu:adapter");
            if (bundle2 != null) {
                this.mAdapter.restoreInstanceState(bundle2);
            }
            sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:header");
            if (sparseParcelableArray != null) {
                this.mHeaderLayout.restoreHierarchyState(sparseParcelableArray);
            }
        }
    }

    public void setCheckedItem(MenuItemImpl menuItemImpl) {
        this.mAdapter.setCheckedItem(menuItemImpl);
    }

    public View inflateHeaderView(int i) {
        View inflate = this.mLayoutInflater.inflate(i, this.mHeaderLayout, false);
        addHeaderView(inflate);
        return inflate;
    }

    public void addHeaderView(View view) {
        this.mHeaderLayout.addView(view);
        this.mMenuView.setPadding(0, 0, 0, this.mMenuView.getPaddingBottom());
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.mIconTintList = colorStateList;
        updateMenuView(false);
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.mTextColor = colorStateList;
        updateMenuView(false);
    }

    public void setItemTextAppearance(int i) {
        this.mTextAppearance = i;
        this.mTextAppearanceSet = true;
        updateMenuView(false);
    }

    public void setItemBackground(Drawable drawable) {
        this.mItemBackground = drawable;
        updateMenuView(false);
    }

    public void setUpdateSuspended(boolean z) {
        if (this.mAdapter != null) {
            this.mAdapter.setUpdateSuspended(z);
        }
    }

    public void dispatchApplyWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
        if (this.mPaddingTopDefault != systemWindowInsetTop) {
            this.mPaddingTopDefault = systemWindowInsetTop;
            if (this.mHeaderLayout.getChildCount() == 0) {
                this.mMenuView.setPadding(0, this.mPaddingTopDefault, 0, this.mMenuView.getPaddingBottom());
            }
        }
        ViewCompat.dispatchApplyWindowInsets(this.mHeaderLayout, windowInsetsCompat);
    }
}
