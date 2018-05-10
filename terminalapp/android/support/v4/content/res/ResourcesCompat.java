package android.support.v4.content.res;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;

public final class ResourcesCompat {
    public static Drawable getDrawable(Resources resources, int i, Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 21) {
            return ResourcesCompatApi21.getDrawable(resources, i, theme);
        }
        return resources.getDrawable(i);
    }

    private ResourcesCompat() {
    }
}
