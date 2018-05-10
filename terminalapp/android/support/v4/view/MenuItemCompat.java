package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.v4.internal.view.SupportMenuItem;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public final class MenuItemCompat {
    static final MenuVersionImpl IMPL;

    interface MenuVersionImpl {
        boolean expandActionView(MenuItem menuItem);

        View getActionView(MenuItem menuItem);

        boolean isActionViewExpanded(MenuItem menuItem);

        MenuItem setActionView(MenuItem menuItem, int i);

        MenuItem setActionView(MenuItem menuItem, View view);

        void setShowAsAction(MenuItem menuItem, int i);
    }

    static class BaseMenuVersionImpl implements MenuVersionImpl {
        BaseMenuVersionImpl() {
        }

        public void setShowAsAction(MenuItem menuItem, int i) {
        }

        public MenuItem setActionView(MenuItem menuItem, View view) {
            return menuItem;
        }

        public MenuItem setActionView(MenuItem menuItem, int i) {
            return menuItem;
        }

        public View getActionView(MenuItem menuItem) {
            return null;
        }

        public boolean expandActionView(MenuItem menuItem) {
            return false;
        }

        public boolean isActionViewExpanded(MenuItem menuItem) {
            return false;
        }
    }

    static class HoneycombMenuVersionImpl implements MenuVersionImpl {
        HoneycombMenuVersionImpl() {
        }

        public void setShowAsAction(MenuItem menuItem, int i) {
            MenuItemCompatHoneycomb.setShowAsAction(menuItem, i);
        }

        public MenuItem setActionView(MenuItem menuItem, View view) {
            return MenuItemCompatHoneycomb.setActionView(menuItem, view);
        }

        public MenuItem setActionView(MenuItem menuItem, int i) {
            return MenuItemCompatHoneycomb.setActionView(menuItem, i);
        }

        public View getActionView(MenuItem menuItem) {
            return MenuItemCompatHoneycomb.getActionView(menuItem);
        }

        public boolean expandActionView(MenuItem menuItem) {
            return false;
        }

        public boolean isActionViewExpanded(MenuItem menuItem) {
            return false;
        }
    }

    static class IcsMenuVersionImpl extends HoneycombMenuVersionImpl {
        IcsMenuVersionImpl() {
        }

        public boolean expandActionView(MenuItem menuItem) {
            return MenuItemCompatIcs.expandActionView(menuItem);
        }

        public boolean isActionViewExpanded(MenuItem menuItem) {
            return MenuItemCompatIcs.isActionViewExpanded(menuItem);
        }
    }

    public interface OnActionExpandListener {
        boolean onMenuItemActionCollapse(MenuItem menuItem);

        boolean onMenuItemActionExpand(MenuItem menuItem);
    }

    static {
        if (VERSION.SDK_INT >= 14) {
            IMPL = new IcsMenuVersionImpl();
        } else if (VERSION.SDK_INT >= 11) {
            IMPL = new HoneycombMenuVersionImpl();
        } else {
            IMPL = new BaseMenuVersionImpl();
        }
    }

    public static void setShowAsAction(MenuItem menuItem, int i) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem) menuItem).setShowAsAction(i);
        } else {
            IMPL.setShowAsAction(menuItem, i);
        }
    }

    public static MenuItem setActionView(MenuItem menuItem, View view) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem).setActionView(view);
        }
        return IMPL.setActionView(menuItem, view);
    }

    public static MenuItem setActionView(MenuItem menuItem, int i) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem).setActionView(i);
        }
        return IMPL.setActionView(menuItem, i);
    }

    public static View getActionView(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem).getActionView();
        }
        return IMPL.getActionView(menuItem);
    }

    public static MenuItem setActionProvider(MenuItem menuItem, ActionProvider actionProvider) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem).setSupportActionProvider(actionProvider);
        }
        Log.w("MenuItemCompat", "setActionProvider: item does not implement SupportMenuItem; ignoring");
        return menuItem;
    }

    public static boolean expandActionView(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem).expandActionView();
        }
        return IMPL.expandActionView(menuItem);
    }

    public static boolean isActionViewExpanded(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem).isActionViewExpanded();
        }
        return IMPL.isActionViewExpanded(menuItem);
    }

    private MenuItemCompat() {
    }
}
