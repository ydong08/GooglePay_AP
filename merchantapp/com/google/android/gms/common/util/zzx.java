package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import com.google.android.gms.common.GoogleSignatureVerifier;
import com.google.android.gms.internal.zztc;

public final class zzx {
    @TargetApi(19)
    public static boolean zza(Context context, int i, String str) {
        return zztc.zzcs(context).zzg(i, str);
    }

    public static boolean zzd(Context context, int i) {
        boolean z = false;
        if (!zza(context, i, "com.google.android.gms")) {
            return z;
        }
        try {
            return GoogleSignatureVerifier.getInstance(context).zza(context.getPackageManager(), context.getPackageManager().getPackageInfo("com.google.android.gms", 64));
        } catch (NameNotFoundException e) {
            if (!Log.isLoggable("UidVerifier", 3)) {
                return z;
            }
            Log.d("UidVerifier", "Package manager can't find google play services package, defaulting to false");
            return z;
        }
    }
}
