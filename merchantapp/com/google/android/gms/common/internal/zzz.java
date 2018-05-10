package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.internal.zztc;

public class zzz {
    private static String KF;
    private static int KG;
    private static Object zzank = new Object();
    private static boolean zzbyk;

    public static int zzci(Context context) {
        zzcj(context);
        return KG;
    }

    private static void zzcj(Context context) {
        synchronized (zzank) {
            if (zzbyk) {
                return;
            }
            zzbyk = true;
            try {
                Bundle bundle = zztc.zzcs(context).getApplicationInfo(context.getPackageName(), 128).metaData;
                if (bundle == null) {
                    return;
                }
                KF = bundle.getString("com.google.app.id");
                KG = bundle.getInt("com.google.android.gms.version");
            } catch (Throwable e) {
                Log.wtf("MetadataValueReader", "This should never happen.", e);
            }
        }
    }
}
