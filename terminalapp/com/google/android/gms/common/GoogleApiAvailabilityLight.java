package com.google.android.gms.common;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzo;
import com.google.android.gms.common.util.zzh;

public class GoogleApiAvailabilityLight {
    private static final GoogleApiAvailabilityLight Ce = new GoogleApiAvailabilityLight();
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;

    GoogleApiAvailabilityLight() {
    }

    public static GoogleApiAvailabilityLight getInstance() {
        return Ce;
    }

    private String zzn(Context context, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("gcore_");
        stringBuilder.append(GOOGLE_PLAY_SERVICES_VERSION_CODE);
        stringBuilder.append("-");
        if (!TextUtils.isEmpty(str)) {
            stringBuilder.append(str);
        }
        stringBuilder.append("-");
        if (context != null) {
            stringBuilder.append(context.getPackageName());
        }
        stringBuilder.append("-");
        if (context != null) {
            try {
                stringBuilder.append(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
            } catch (NameNotFoundException e) {
            }
        }
        return stringBuilder.toString();
    }

    public Intent getErrorResolutionIntent(Context context, int i, String str) {
        switch (i) {
            case 1:
            case 2:
                return zzo.zzak("com.google.android.gms", zzn(context, str));
            case 3:
                return zzo.zzgu("com.google.android.gms");
            case 42:
                return zzo.zzaxa();
            default:
                return null;
        }
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2) {
        return getErrorResolutionPendingIntent(context, i, i2, null);
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2, String str) {
        if (zzh.zzcm(context) && i == 2) {
            i = 42;
        }
        Intent errorResolutionIntent = getErrorResolutionIntent(context, i, str);
        return errorResolutionIntent == null ? null : PendingIntent.getActivity(context, i2, errorResolutionIntent, 268435456);
    }

    public int isGooglePlayServicesAvailable(Context context) {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtilLight.isGooglePlayServicesAvailable(context);
        return GooglePlayServicesUtilLight.isPlayServicesPossiblyUpdating(context, isGooglePlayServicesAvailable) ? 18 : isGooglePlayServicesAvailable;
    }

    public boolean isUserResolvableError(int i) {
        return GooglePlayServicesUtilLight.isUserRecoverableError(i);
    }

    public void zzbs(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        GooglePlayServicesUtilLight.zzbf(context);
    }

    public boolean zzm(Context context, String str) {
        return GooglePlayServicesUtilLight.zzm(context, str);
    }
}
