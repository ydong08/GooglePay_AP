package android.support.v4.widget;

import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.widget.TextView;

public final class TextViewCompat {
    static final TextViewCompatImpl IMPL;

    interface TextViewCompatImpl {
        TextViewCompatImpl() {
        }

        void setCompoundDrawablesRelative(TextView textView, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
            textView.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        }

        int getMaxLines(TextView textView) {
            return TextViewCompatGingerbread.getMaxLines(textView);
        }

        void setTextAppearance(TextView textView, int i) {
            TextViewCompatGingerbread.setTextAppearance(textView, i);
        }

        Drawable[] getCompoundDrawablesRelative(TextView textView) {
            return TextViewCompatGingerbread.getCompoundDrawablesRelative(textView);
        }
    }

    static class JbTextViewCompatImpl extends TextViewCompatImpl {
        JbTextViewCompatImpl() {
        }

        public int getMaxLines(TextView textView) {
            return TextViewCompatJb.getMaxLines(textView);
        }
    }

    static class JbMr1TextViewCompatImpl extends JbTextViewCompatImpl {
        JbMr1TextViewCompatImpl() {
        }

        public void setCompoundDrawablesRelative(TextView textView, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
            TextViewCompatJbMr1.setCompoundDrawablesRelative(textView, drawable, drawable2, drawable3, drawable4);
        }

        public Drawable[] getCompoundDrawablesRelative(TextView textView) {
            return TextViewCompatJbMr1.getCompoundDrawablesRelative(textView);
        }
    }

    static class JbMr2TextViewCompatImpl extends JbMr1TextViewCompatImpl {
        JbMr2TextViewCompatImpl() {
        }

        public void setCompoundDrawablesRelative(TextView textView, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
            TextViewCompatJbMr2.setCompoundDrawablesRelative(textView, drawable, drawable2, drawable3, drawable4);
        }

        public Drawable[] getCompoundDrawablesRelative(TextView textView) {
            return TextViewCompatJbMr2.getCompoundDrawablesRelative(textView);
        }
    }

    static class Api23TextViewCompatImpl extends JbMr2TextViewCompatImpl {
        Api23TextViewCompatImpl() {
        }

        public void setTextAppearance(TextView textView, int i) {
            TextViewCompatApi23.setTextAppearance(textView, i);
        }
    }

    private TextViewCompat() {
    }

    static {
        int i = VERSION.SDK_INT;
        if (i >= 23) {
            IMPL = new Api23TextViewCompatImpl();
        } else if (i >= 18) {
            IMPL = new JbMr2TextViewCompatImpl();
        } else if (i >= 17) {
            IMPL = new JbMr1TextViewCompatImpl();
        } else if (i >= 16) {
            IMPL = new JbTextViewCompatImpl();
        } else {
            IMPL = new TextViewCompatImpl();
        }
    }

    public static void setCompoundDrawablesRelative(TextView textView, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        IMPL.setCompoundDrawablesRelative(textView, drawable, drawable2, drawable3, drawable4);
    }

    public static int getMaxLines(TextView textView) {
        return IMPL.getMaxLines(textView);
    }

    public static void setTextAppearance(TextView textView, int i) {
        IMPL.setTextAppearance(textView, i);
    }

    public static Drawable[] getCompoundDrawablesRelative(TextView textView) {
        return IMPL.getCompoundDrawablesRelative(textView);
    }
}
