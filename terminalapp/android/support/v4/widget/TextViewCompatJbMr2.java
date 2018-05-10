package android.support.v4.widget;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

class TextViewCompatJbMr2 {
    TextViewCompatJbMr2() {
    }

    public static void setCompoundDrawablesRelative(TextView textView, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        textView.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
    }

    public static Drawable[] getCompoundDrawablesRelative(TextView textView) {
        return textView.getCompoundDrawablesRelative();
    }
}
