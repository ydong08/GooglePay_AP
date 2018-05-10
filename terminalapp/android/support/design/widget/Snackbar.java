package android.support.design.widget;

import android.content.Context;
import android.util.AttributeSet;

public final class Snackbar extends BaseTransientBottomBar<Snackbar> {

    public static final class SnackbarLayout extends SnackbarBaseLayout {
        public SnackbarLayout(Context context) {
            super(context);
        }

        public SnackbarLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }
    }
}
