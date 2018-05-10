package android.support.v4.view;

import android.content.Context;
import android.support.v4.os.BuildCompat;

public final class PointerIconCompat {
    static final PointerIconCompatImpl IMPL;
    private Object mPointerIcon;

    interface PointerIconCompatImpl {
        PointerIconCompatImpl() {
        }

        Object getSystemIcon(Context context, int i) {
            return null;
        }
    }

    static class Api24PointerIconCompatImpl extends PointerIconCompatImpl {
        Api24PointerIconCompatImpl() {
        }

        public Object getSystemIcon(Context context, int i) {
            return PointerIconCompatApi24.getSystemIcon(context, i);
        }
    }

    private PointerIconCompat(Object obj) {
        this.mPointerIcon = obj;
    }

    public Object getPointerIcon() {
        return this.mPointerIcon;
    }

    static {
        if (BuildCompat.isAtLeastN()) {
            IMPL = new Api24PointerIconCompatImpl();
        } else {
            IMPL = new PointerIconCompatImpl();
        }
    }

    public static PointerIconCompat getSystemIcon(Context context, int i) {
        return new PointerIconCompat(IMPL.getSystemIcon(context, i));
    }
}
