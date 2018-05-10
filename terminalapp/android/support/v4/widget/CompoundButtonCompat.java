package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.widget.CompoundButton;

public final class CompoundButtonCompat {
    private static final CompoundButtonCompatImpl IMPL;

    interface CompoundButtonCompatImpl {
        CompoundButtonCompatImpl() {
        }

        void setButtonTintList(CompoundButton compoundButton, ColorStateList colorStateList) {
            CompoundButtonCompatGingerbread.setButtonTintList(compoundButton, colorStateList);
        }

        void setButtonTintMode(CompoundButton compoundButton, Mode mode) {
            CompoundButtonCompatGingerbread.setButtonTintMode(compoundButton, mode);
        }

        Drawable getButtonDrawable(CompoundButton compoundButton) {
            return CompoundButtonCompatGingerbread.getButtonDrawable(compoundButton);
        }
    }

    static class LollipopCompoundButtonImpl extends CompoundButtonCompatImpl {
        LollipopCompoundButtonImpl() {
        }

        public void setButtonTintList(CompoundButton compoundButton, ColorStateList colorStateList) {
            CompoundButtonCompatLollipop.setButtonTintList(compoundButton, colorStateList);
        }

        public void setButtonTintMode(CompoundButton compoundButton, Mode mode) {
            CompoundButtonCompatLollipop.setButtonTintMode(compoundButton, mode);
        }
    }

    static class Api23CompoundButtonImpl extends LollipopCompoundButtonImpl {
        Api23CompoundButtonImpl() {
        }

        public Drawable getButtonDrawable(CompoundButton compoundButton) {
            return CompoundButtonCompatApi23.getButtonDrawable(compoundButton);
        }
    }

    static {
        int i = VERSION.SDK_INT;
        if (i >= 23) {
            IMPL = new Api23CompoundButtonImpl();
        } else if (i >= 21) {
            IMPL = new LollipopCompoundButtonImpl();
        } else {
            IMPL = new CompoundButtonCompatImpl();
        }
    }

    private CompoundButtonCompat() {
    }

    public static void setButtonTintList(CompoundButton compoundButton, ColorStateList colorStateList) {
        IMPL.setButtonTintList(compoundButton, colorStateList);
    }

    public static void setButtonTintMode(CompoundButton compoundButton, Mode mode) {
        IMPL.setButtonTintMode(compoundButton, mode);
    }

    public static Drawable getButtonDrawable(CompoundButton compoundButton) {
        return IMPL.getButtonDrawable(compoundButton);
    }
}
