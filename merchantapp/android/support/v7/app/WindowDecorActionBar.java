package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.ActionBarContainer;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback;
import android.support.v7.widget.DecorToolbar;
import android.support.v7.widget.ScrollingTabContainerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class WindowDecorActionBar extends ActionBar implements ActionBarVisibilityCallback {
    private static final boolean ALLOW_SHOW_HIDE_ANIMATIONS = (VERSION.SDK_INT >= 14);
    private static final Interpolator sHideInterpolator = new AccelerateInterpolator();
    private static final Interpolator sShowInterpolator = new DecelerateInterpolator();
    ActionModeImpl mActionMode;
    private Activity mActivity;
    ActionBarContainer mContainerView;
    boolean mContentAnimations = true;
    View mContentView;
    Context mContext;
    ActionBarContextView mContextView;
    private int mCurWindowVisibility = 0;
    ViewPropertyAnimatorCompatSet mCurrentShowAnim;
    DecorToolbar mDecorToolbar;
    ActionMode mDeferredDestroyActionMode;
    Callback mDeferredModeDestroyCallback;
    private Dialog mDialog;
    private boolean mDisplayHomeAsUpSet;
    private boolean mHasEmbeddedTabs;
    boolean mHiddenByApp;
    boolean mHiddenBySystem;
    final ViewPropertyAnimatorListener mHideListener = new ViewPropertyAnimatorListenerAdapter() {
        public void onAnimationEnd(View view) {
            if (WindowDecorActionBar.this.mContentAnimations && WindowDecorActionBar.this.mContentView != null) {
                ViewCompat.setTranslationY(WindowDecorActionBar.this.mContentView, 0.0f);
                ViewCompat.setTranslationY(WindowDecorActionBar.this.mContainerView, 0.0f);
            }
            WindowDecorActionBar.this.mContainerView.setVisibility(8);
            WindowDecorActionBar.this.mContainerView.setTransitioning(false);
            WindowDecorActionBar.this.mCurrentShowAnim = null;
            WindowDecorActionBar.this.completeDeferredDestroyActionMode();
            if (WindowDecorActionBar.this.mOverlayLayout != null) {
                ViewCompat.requestApplyInsets(WindowDecorActionBar.this.mOverlayLayout);
            }
        }
    };
    boolean mHideOnContentScroll;
    private boolean mLastMenuVisibility;
    private ArrayList<Object> mMenuVisibilityListeners = new ArrayList();
    private boolean mNowShowing = true;
    ActionBarOverlayLayout mOverlayLayout;
    private int mSavedTabPosition = -1;
    private boolean mShowHideAnimationEnabled;
    final ViewPropertyAnimatorListener mShowListener = new ViewPropertyAnimatorListenerAdapter() {
        public void onAnimationEnd(View view) {
            WindowDecorActionBar.this.mCurrentShowAnim = null;
            WindowDecorActionBar.this.mContainerView.requestLayout();
        }
    };
    private boolean mShowingForMode;
    ScrollingTabContainerView mTabScrollView;
    private ArrayList<Object> mTabs = new ArrayList();
    private Context mThemedContext;
    final ViewPropertyAnimatorUpdateListener mUpdateListener = new ViewPropertyAnimatorUpdateListener() {
        public void onAnimationUpdate(View view) {
            ((View) WindowDecorActionBar.this.mContainerView.getParent()).invalidate();
        }
    };

    public class ActionModeImpl extends ActionMode implements MenuBuilder.Callback {
        private final Context mActionModeContext;
        private Callback mCallback;
        private WeakReference<View> mCustomView;
        private final MenuBuilder mMenu;

        public ActionModeImpl(Context context, Callback callback) {
            this.mActionModeContext = context;
            this.mCallback = callback;
            this.mMenu = new MenuBuilder(context).setDefaultShowAsAction(1);
            this.mMenu.setCallback(this);
        }

        public MenuInflater getMenuInflater() {
            return new SupportMenuInflater(this.mActionModeContext);
        }

        public Menu getMenu() {
            return this.mMenu;
        }

        public void finish() {
            if (WindowDecorActionBar.this.mActionMode == this) {
                if (WindowDecorActionBar.checkShowingFlags(WindowDecorActionBar.this.mHiddenByApp, WindowDecorActionBar.this.mHiddenBySystem, false)) {
                    this.mCallback.onDestroyActionMode(this);
                } else {
                    WindowDecorActionBar.this.mDeferredDestroyActionMode = this;
                    WindowDecorActionBar.this.mDeferredModeDestroyCallback = this.mCallback;
                }
                this.mCallback = null;
                WindowDecorActionBar.this.animateToMode(false);
                WindowDecorActionBar.this.mContextView.closeMode();
                WindowDecorActionBar.this.mDecorToolbar.getViewGroup().sendAccessibilityEvent(32);
                WindowDecorActionBar.this.mOverlayLayout.setHideOnContentScrollEnabled(WindowDecorActionBar.this.mHideOnContentScroll);
                WindowDecorActionBar.this.mActionMode = null;
            }
        }

        public void invalidate() {
            if (WindowDecorActionBar.this.mActionMode == this) {
                this.mMenu.stopDispatchingItemsChanged();
                try {
                    this.mCallback.onPrepareActionMode(this, this.mMenu);
                } finally {
                    this.mMenu.startDispatchingItemsChanged();
                }
            }
        }

        public boolean dispatchOnCreate() {
            this.mMenu.stopDispatchingItemsChanged();
            try {
                boolean onCreateActionMode = this.mCallback.onCreateActionMode(this, this.mMenu);
                return onCreateActionMode;
            } finally {
                this.mMenu.startDispatchingItemsChanged();
            }
        }

        public void setCustomView(View view) {
            WindowDecorActionBar.this.mContextView.setCustomView(view);
            this.mCustomView = new WeakReference(view);
        }

        public void setSubtitle(CharSequence charSequence) {
            WindowDecorActionBar.this.mContextView.setSubtitle(charSequence);
        }

        public void setTitle(CharSequence charSequence) {
            WindowDecorActionBar.this.mContextView.setTitle(charSequence);
        }

        public void setTitle(int i) {
            setTitle(WindowDecorActionBar.this.mContext.getResources().getString(i));
        }

        public void setSubtitle(int i) {
            setSubtitle(WindowDecorActionBar.this.mContext.getResources().getString(i));
        }

        public CharSequence getTitle() {
            return WindowDecorActionBar.this.mContextView.getTitle();
        }

        public CharSequence getSubtitle() {
            return WindowDecorActionBar.this.mContextView.getSubtitle();
        }

        public void setTitleOptionalHint(boolean z) {
            super.setTitleOptionalHint(z);
            WindowDecorActionBar.this.mContextView.setTitleOptional(z);
        }

        public boolean isTitleOptional() {
            return WindowDecorActionBar.this.mContextView.isTitleOptional();
        }

        public View getCustomView() {
            return this.mCustomView != null ? (View) this.mCustomView.get() : null;
        }

        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            if (this.mCallback != null) {
                return this.mCallback.onActionItemClicked(this, menuItem);
            }
            return false;
        }

        public void onMenuModeChange(MenuBuilder menuBuilder) {
            if (this.mCallback != null) {
                invalidate();
                WindowDecorActionBar.this.mContextView.showOverflowMenu();
            }
        }
    }

    public WindowDecorActionBar(Activity activity, boolean z) {
        this.mActivity = activity;
        View decorView = activity.getWindow().getDecorView();
        init(decorView);
        if (!z) {
            this.mContentView = decorView.findViewById(16908290);
        }
    }

    public WindowDecorActionBar(Dialog dialog) {
        this.mDialog = dialog;
        init(dialog.getWindow().getDecorView());
    }

    private void init(View view) {
        this.mOverlayLayout = (ActionBarOverlayLayout) view.findViewById(R.id.decor_content_parent);
        if (this.mOverlayLayout != null) {
            this.mOverlayLayout.setActionBarVisibilityCallback(this);
        }
        this.mDecorToolbar = getDecorToolbar(view.findViewById(R.id.action_bar));
        this.mContextView = (ActionBarContextView) view.findViewById(R.id.action_context_bar);
        this.mContainerView = (ActionBarContainer) view.findViewById(R.id.action_bar_container);
        if (this.mDecorToolbar == null || this.mContextView == null || this.mContainerView == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used " + "with a compatible window decor layout");
        }
        this.mContext = this.mDecorToolbar.getContext();
        if ((this.mDecorToolbar.getDisplayOptions() & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(this.mContext);
        actionBarPolicy.enableHomeButtonByDefault();
        setHasEmbeddedTabs(actionBarPolicy.hasEmbeddedTabs());
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        if (obtainStyledAttributes.getBoolean(R.styleable.ActionBar_hideOnContentScroll, false)) {
            setHideOnContentScrollEnabled(true);
        }
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.ActionBar_elevation, 0);
        if (dimensionPixelSize != 0) {
            setElevation((float) dimensionPixelSize);
        }
        obtainStyledAttributes.recycle();
    }

    private DecorToolbar getDecorToolbar(View view) {
        if (view instanceof DecorToolbar) {
            return (DecorToolbar) view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getWrapper();
        }
        throw new IllegalStateException(new StringBuilder().append("Can't make a decor toolbar out of ").append(view).toString() != null ? view.getClass().getSimpleName() : "null");
    }

    public void setElevation(float f) {
        ViewCompat.setElevation(this.mContainerView, f);
    }

    public void onConfigurationChanged(Configuration configuration) {
        setHasEmbeddedTabs(ActionBarPolicy.get(this.mContext).hasEmbeddedTabs());
    }

    private void setHasEmbeddedTabs(boolean z) {
        boolean z2;
        boolean z3;
        boolean z4 = true;
        this.mHasEmbeddedTabs = z;
        if (this.mHasEmbeddedTabs) {
            this.mContainerView.setTabContainer(null);
            this.mDecorToolbar.setEmbeddedTabView(this.mTabScrollView);
        } else {
            this.mDecorToolbar.setEmbeddedTabView(null);
            this.mContainerView.setTabContainer(this.mTabScrollView);
        }
        if (getNavigationMode() == 2) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (this.mTabScrollView != null) {
            if (z2) {
                this.mTabScrollView.setVisibility(0);
                if (this.mOverlayLayout != null) {
                    ViewCompat.requestApplyInsets(this.mOverlayLayout);
                }
            } else {
                this.mTabScrollView.setVisibility(8);
            }
        }
        DecorToolbar decorToolbar = this.mDecorToolbar;
        if (this.mHasEmbeddedTabs || !z2) {
            z3 = false;
        } else {
            z3 = true;
        }
        decorToolbar.setCollapsible(z3);
        ActionBarOverlayLayout actionBarOverlayLayout = this.mOverlayLayout;
        if (this.mHasEmbeddedTabs || !z2) {
            z4 = false;
        }
        actionBarOverlayLayout.setHasNonEmbeddedTabs(z4);
    }

    void completeDeferredDestroyActionMode() {
        if (this.mDeferredModeDestroyCallback != null) {
            this.mDeferredModeDestroyCallback.onDestroyActionMode(this.mDeferredDestroyActionMode);
            this.mDeferredDestroyActionMode = null;
            this.mDeferredModeDestroyCallback = null;
        }
    }

    public void onWindowVisibilityChanged(int i) {
        this.mCurWindowVisibility = i;
    }

    public void setShowHideAnimationEnabled(boolean z) {
        this.mShowHideAnimationEnabled = z;
        if (!z && this.mCurrentShowAnim != null) {
            this.mCurrentShowAnim.cancel();
        }
    }

    public void dispatchMenuVisibilityChanged(boolean z) {
        if (z != this.mLastMenuVisibility) {
            this.mLastMenuVisibility = z;
            int size = this.mMenuVisibilityListeners.size();
            for (int i = 0; i < size; i++) {
                this.mMenuVisibilityListeners.get(i);
            }
        }
    }

    public void setDisplayHomeAsUpEnabled(boolean z) {
        setDisplayOptions(z ? 4 : 0, 4);
    }

    public void setWindowTitle(CharSequence charSequence) {
        this.mDecorToolbar.setWindowTitle(charSequence);
    }

    public boolean requestFocus() {
        ViewGroup viewGroup = this.mDecorToolbar.getViewGroup();
        if (viewGroup == null || viewGroup.hasFocus()) {
            return false;
        }
        viewGroup.requestFocus();
        return true;
    }

    public void setDisplayOptions(int i, int i2) {
        int displayOptions = this.mDecorToolbar.getDisplayOptions();
        if ((i2 & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mDecorToolbar.setDisplayOptions((displayOptions & (i2 ^ -1)) | (i & i2));
    }

    public int getNavigationMode() {
        return this.mDecorToolbar.getNavigationMode();
    }

    public int getDisplayOptions() {
        return this.mDecorToolbar.getDisplayOptions();
    }

    public ActionMode startActionMode(Callback callback) {
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        this.mOverlayLayout.setHideOnContentScrollEnabled(false);
        this.mContextView.killMode();
        ActionMode actionModeImpl = new ActionModeImpl(this.mContextView.getContext(), callback);
        if (!actionModeImpl.dispatchOnCreate()) {
            return null;
        }
        this.mActionMode = actionModeImpl;
        actionModeImpl.invalidate();
        this.mContextView.initForMode(actionModeImpl);
        animateToMode(true);
        this.mContextView.sendAccessibilityEvent(32);
        return actionModeImpl;
    }

    public int getHeight() {
        return this.mContainerView.getHeight();
    }

    public void enableContentAnimations(boolean z) {
        this.mContentAnimations = z;
    }

    private void showForActionMode() {
        if (!this.mShowingForMode) {
            this.mShowingForMode = true;
            if (this.mOverlayLayout != null) {
                this.mOverlayLayout.setShowingForActionMode(true);
            }
            updateVisibility(false);
        }
    }

    public void showForSystem() {
        if (this.mHiddenBySystem) {
            this.mHiddenBySystem = false;
            updateVisibility(true);
        }
    }

    private void hideForActionMode() {
        if (this.mShowingForMode) {
            this.mShowingForMode = false;
            if (this.mOverlayLayout != null) {
                this.mOverlayLayout.setShowingForActionMode(false);
            }
            updateVisibility(false);
        }
    }

    public void hideForSystem() {
        if (!this.mHiddenBySystem) {
            this.mHiddenBySystem = true;
            updateVisibility(true);
        }
    }

    public void setHideOnContentScrollEnabled(boolean z) {
        if (!z || this.mOverlayLayout.isInOverlayMode()) {
            this.mHideOnContentScroll = z;
            this.mOverlayLayout.setHideOnContentScrollEnabled(z);
            return;
        }
        throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
    }

    public int getHideOffset() {
        return this.mOverlayLayout.getActionBarHideOffset();
    }

    static boolean checkShowingFlags(boolean z, boolean z2, boolean z3) {
        if (z3) {
            return true;
        }
        if (z || z2) {
            return false;
        }
        return true;
    }

    private void updateVisibility(boolean z) {
        if (checkShowingFlags(this.mHiddenByApp, this.mHiddenBySystem, this.mShowingForMode)) {
            if (!this.mNowShowing) {
                this.mNowShowing = true;
                doShow(z);
            }
        } else if (this.mNowShowing) {
            this.mNowShowing = false;
            doHide(z);
        }
    }

    public void doShow(boolean z) {
        if (this.mCurrentShowAnim != null) {
            this.mCurrentShowAnim.cancel();
        }
        this.mContainerView.setVisibility(0);
        if (this.mCurWindowVisibility == 0 && ALLOW_SHOW_HIDE_ANIMATIONS && (this.mShowHideAnimationEnabled || z)) {
            ViewCompat.setTranslationY(this.mContainerView, 0.0f);
            float f = (float) (-this.mContainerView.getHeight());
            if (z) {
                int[] iArr = new int[]{0, 0};
                this.mContainerView.getLocationInWindow(iArr);
                f -= (float) iArr[1];
            }
            ViewCompat.setTranslationY(this.mContainerView, f);
            ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = new ViewPropertyAnimatorCompatSet();
            ViewPropertyAnimatorCompat translationY = ViewCompat.animate(this.mContainerView).translationY(0.0f);
            translationY.setUpdateListener(this.mUpdateListener);
            viewPropertyAnimatorCompatSet.play(translationY);
            if (this.mContentAnimations && this.mContentView != null) {
                ViewCompat.setTranslationY(this.mContentView, f);
                viewPropertyAnimatorCompatSet.play(ViewCompat.animate(this.mContentView).translationY(0.0f));
            }
            viewPropertyAnimatorCompatSet.setInterpolator(sShowInterpolator);
            viewPropertyAnimatorCompatSet.setDuration(250);
            viewPropertyAnimatorCompatSet.setListener(this.mShowListener);
            this.mCurrentShowAnim = viewPropertyAnimatorCompatSet;
            viewPropertyAnimatorCompatSet.start();
        } else {
            ViewCompat.setAlpha(this.mContainerView, 1.0f);
            ViewCompat.setTranslationY(this.mContainerView, 0.0f);
            if (this.mContentAnimations && this.mContentView != null) {
                ViewCompat.setTranslationY(this.mContentView, 0.0f);
            }
            this.mShowListener.onAnimationEnd(null);
        }
        if (this.mOverlayLayout != null) {
            ViewCompat.requestApplyInsets(this.mOverlayLayout);
        }
    }

    public void doHide(boolean z) {
        if (this.mCurrentShowAnim != null) {
            this.mCurrentShowAnim.cancel();
        }
        if (this.mCurWindowVisibility == 0 && ALLOW_SHOW_HIDE_ANIMATIONS && (this.mShowHideAnimationEnabled || z)) {
            ViewCompat.setAlpha(this.mContainerView, 1.0f);
            this.mContainerView.setTransitioning(true);
            ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = new ViewPropertyAnimatorCompatSet();
            float f = (float) (-this.mContainerView.getHeight());
            if (z) {
                int[] iArr = new int[]{0, 0};
                this.mContainerView.getLocationInWindow(iArr);
                f -= (float) iArr[1];
            }
            ViewPropertyAnimatorCompat translationY = ViewCompat.animate(this.mContainerView).translationY(f);
            translationY.setUpdateListener(this.mUpdateListener);
            viewPropertyAnimatorCompatSet.play(translationY);
            if (this.mContentAnimations && this.mContentView != null) {
                viewPropertyAnimatorCompatSet.play(ViewCompat.animate(this.mContentView).translationY(f));
            }
            viewPropertyAnimatorCompatSet.setInterpolator(sHideInterpolator);
            viewPropertyAnimatorCompatSet.setDuration(250);
            viewPropertyAnimatorCompatSet.setListener(this.mHideListener);
            this.mCurrentShowAnim = viewPropertyAnimatorCompatSet;
            viewPropertyAnimatorCompatSet.start();
            return;
        }
        this.mHideListener.onAnimationEnd(null);
    }

    public boolean isShowing() {
        int height = getHeight();
        return this.mNowShowing && (height == 0 || getHideOffset() < height);
    }

    public void animateToMode(boolean z) {
        if (z) {
            showForActionMode();
        } else {
            hideForActionMode();
        }
        if (shouldAnimateContextView()) {
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat;
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2;
            if (z) {
                viewPropertyAnimatorCompat = this.mDecorToolbar.setupAnimatorToVisibility(4, 100);
                viewPropertyAnimatorCompat2 = this.mContextView.setupAnimatorToVisibility(0, 200);
            } else {
                viewPropertyAnimatorCompat2 = this.mDecorToolbar.setupAnimatorToVisibility(0, 200);
                viewPropertyAnimatorCompat = this.mContextView.setupAnimatorToVisibility(8, 100);
            }
            ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = new ViewPropertyAnimatorCompatSet();
            viewPropertyAnimatorCompatSet.playSequentially(viewPropertyAnimatorCompat, viewPropertyAnimatorCompat2);
            viewPropertyAnimatorCompatSet.start();
        } else if (z) {
            this.mDecorToolbar.setVisibility(4);
            this.mContextView.setVisibility(0);
        } else {
            this.mDecorToolbar.setVisibility(0);
            this.mContextView.setVisibility(8);
        }
    }

    private boolean shouldAnimateContextView() {
        return ViewCompat.isLaidOut(this.mContainerView);
    }

    public Context getThemedContext() {
        if (this.mThemedContext == null) {
            TypedValue typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
            int i = typedValue.resourceId;
            if (i != 0) {
                this.mThemedContext = new ContextThemeWrapper(this.mContext, i);
            } else {
                this.mThemedContext = this.mContext;
            }
        }
        return this.mThemedContext;
    }

    public void setHomeAsUpIndicator(Drawable drawable) {
        this.mDecorToolbar.setNavigationIcon(drawable);
    }

    public void setHomeAsUpIndicator(int i) {
        this.mDecorToolbar.setNavigationIcon(i);
    }

    public void setHomeActionContentDescription(int i) {
        this.mDecorToolbar.setNavigationContentDescription(i);
    }

    public void onContentScrollStarted() {
        if (this.mCurrentShowAnim != null) {
            this.mCurrentShowAnim.cancel();
            this.mCurrentShowAnim = null;
        }
    }

    public boolean collapseActionView() {
        if (this.mDecorToolbar == null || !this.mDecorToolbar.hasExpandedActionView()) {
            return false;
        }
        this.mDecorToolbar.collapseActionView();
        return true;
    }

    public void setDefaultDisplayHomeAsUpEnabled(boolean z) {
        if (!this.mDisplayHomeAsUpSet) {
            setDisplayHomeAsUpEnabled(z);
        }
    }
}
