package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v7.app.AppCompatDelegate;
import java.lang.ref.WeakReference;

public class VectorEnabledTintResources extends Resources {
    private final WeakReference<Context> mContextRef;

    public static boolean shouldBeUsed() {
        return AppCompatDelegate.isCompatVectorFromResourcesEnabled() && VERSION.SDK_INT <= 20;
    }

    public VectorEnabledTintResources(Context context, Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.mContextRef = new WeakReference(context);
    }

    public Drawable getDrawable(int i) throws NotFoundException {
        Context context = (Context) this.mContextRef.get();
        if (context != null) {
            return AppCompatDrawableManager.get().onDrawableLoadedFromResources(context, this, i);
        }
        return super.getDrawable(i);
    }

    final Drawable superGetDrawable(int i) {
        return super.getDrawable(i);
    }
}
