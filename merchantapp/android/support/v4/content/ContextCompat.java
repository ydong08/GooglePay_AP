package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.TypedValue;
import java.io.File;

public class ContextCompat {
    private static final Object sLock = new Object();
    private static TypedValue sTempValue;

    protected ContextCompat() {
    }

    public static boolean startActivities(Context context, Intent[] intentArr, Bundle bundle) {
        int i = VERSION.SDK_INT;
        if (i >= 16) {
            ContextCompatJellybean.startActivities(context, intentArr, bundle);
            return true;
        } else if (i < 11) {
            return false;
        } else {
            ContextCompatHoneycomb.startActivities(context, intentArr);
            return true;
        }
    }

    public static File[] getExternalFilesDirs(Context context, String str) {
        if (VERSION.SDK_INT >= 19) {
            return ContextCompatKitKat.getExternalFilesDirs(context, str);
        }
        return new File[]{context.getExternalFilesDir(str)};
    }

    public static File[] getExternalCacheDirs(Context context) {
        if (VERSION.SDK_INT >= 19) {
            return ContextCompatKitKat.getExternalCacheDirs(context);
        }
        return new File[]{context.getExternalCacheDir()};
    }

    public static final Drawable getDrawable(Context context, int i) {
        int i2 = VERSION.SDK_INT;
        if (i2 >= 21) {
            return ContextCompatApi21.getDrawable(context, i);
        }
        if (i2 >= 16) {
            return context.getResources().getDrawable(i);
        }
        synchronized (sLock) {
            if (sTempValue == null) {
                sTempValue = new TypedValue();
            }
            context.getResources().getValue(i, sTempValue, true);
            i2 = sTempValue.resourceId;
        }
        return context.getResources().getDrawable(i2);
    }

    public static final ColorStateList getColorStateList(Context context, int i) {
        if (VERSION.SDK_INT >= 23) {
            return ContextCompatApi23.getColorStateList(context, i);
        }
        return context.getResources().getColorStateList(i);
    }

    public static final int getColor(Context context, int i) {
        if (VERSION.SDK_INT >= 23) {
            return ContextCompatApi23.getColor(context, i);
        }
        return context.getResources().getColor(i);
    }
}
