package android.support.v4.view;

import android.view.MenuItem;

class MenuItemCompatIcs {
    MenuItemCompatIcs() {
    }

    public static boolean expandActionView(MenuItem menuItem) {
        return menuItem.expandActionView();
    }

    public static boolean isActionViewExpanded(MenuItem menuItem) {
        return menuItem.isActionViewExpanded();
    }
}
