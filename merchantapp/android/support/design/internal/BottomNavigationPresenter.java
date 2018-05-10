package android.support.design.internal;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuPresenter.Callback;
import android.support.v7.view.menu.SubMenuBuilder;

public class BottomNavigationPresenter implements MenuPresenter {
    private MenuBuilder mMenu;
    private BottomNavigationMenuView mMenuView;
    private boolean mUpdateSuspended = false;

    public void setBottomNavigationMenuView(BottomNavigationMenuView bottomNavigationMenuView) {
        this.mMenuView = bottomNavigationMenuView;
    }

    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        this.mMenuView.initialize(this.mMenu);
        this.mMenu = menuBuilder;
    }

    public void updateMenuView(boolean z) {
        if (!this.mUpdateSuspended) {
            if (z) {
                this.mMenuView.buildMenuView();
            } else {
                this.mMenuView.updateMenuView();
            }
        }
    }

    public void setCallback(Callback callback) {
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        return false;
    }

    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
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
        return -1;
    }

    public Parcelable onSaveInstanceState() {
        return null;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
    }

    public void setUpdateSuspended(boolean z) {
        this.mUpdateSuspended = z;
    }
}
