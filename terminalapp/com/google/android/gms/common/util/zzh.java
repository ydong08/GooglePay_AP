package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

public final class zzh {
    private static Boolean Nl;
    private static Boolean Nm;
    private static Boolean Nn;

    public static boolean zzb(Resources resources) {
        boolean z = false;
        if (resources == null) {
            return false;
        }
        if (Nl == null) {
            boolean z2 = (resources.getConfiguration().screenLayout & 15) > 3;
            if ((zzr.zzazd() && z2) || zzc(resources)) {
                z = true;
            }
            Nl = Boolean.valueOf(z);
        }
        return Nl.booleanValue();
    }

    @TargetApi(13)
    private static boolean zzc(Resources resources) {
        if (Nm == null) {
            Configuration configuration = resources.getConfiguration();
            boolean z = zzr.zzazf() && (configuration.screenLayout & 15) <= 3 && configuration.smallestScreenWidthDp >= 600;
            Nm = Boolean.valueOf(z);
        }
        return Nm.booleanValue();
    }

    @TargetApi(20)
    public static boolean zzcm(Context context) {
        if (Nn == null) {
            boolean z = zzr.zzazl() && context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
            Nn = Boolean.valueOf(z);
        }
        return Nn.booleanValue();
    }
}
