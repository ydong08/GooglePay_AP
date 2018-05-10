package android.support.v4.app;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;

public final class BundleCompat {
    private BundleCompat() {
    }

    public static IBinder getBinder(Bundle bundle, String str) {
        if (VERSION.SDK_INT >= 18) {
            return BundleCompatJellybeanMR2.getBinder(bundle, str);
        }
        return BundleCompatGingerbread.getBinder(bundle, str);
    }
}
