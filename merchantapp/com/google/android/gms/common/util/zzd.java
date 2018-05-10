package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.google.android.gms.internal.zztc;

public class zzd {
    @TargetApi(12)
    public static boolean zzr(Context context, String str) {
        if (!zzr.zzaze()) {
            return false;
        }
        if ("com.google.android.gms".equals(str) && zzzz()) {
            return false;
        }
        try {
            return (zztc.zzcs(context).getApplicationInfo(str, 0).flags & 2097152) != 0;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static boolean zzzz() {
        return false;
    }
}
