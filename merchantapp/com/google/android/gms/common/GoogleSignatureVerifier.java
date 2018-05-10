package com.google.android.gms.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.android.gms.common.internal.zzab;

public class GoogleSignatureVerifier {
    private static GoogleSignatureVerifier Cw;
    private final Context mContext;

    private GoogleSignatureVerifier(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static GoogleSignatureVerifier getInstance(Context context) {
        zzab.zzae(context);
        synchronized (GoogleSignatureVerifier.class) {
            if (Cw == null) {
                zzc.init(context);
                Cw = new GoogleSignatureVerifier(context);
            }
        }
        return Cw;
    }

    zza zza(PackageInfo packageInfo, zza... com_google_android_gms_common_zzc_zzaArr) {
        int i = 0;
        if (packageInfo.signatures == null) {
            return null;
        }
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
            return null;
        }
        zzb com_google_android_gms_common_zzc_zzb = new zzb(packageInfo.signatures[0].toByteArray());
        while (i < com_google_android_gms_common_zzc_zzaArr.length) {
            if (com_google_android_gms_common_zzc_zzaArr[i].equals(com_google_android_gms_common_zzc_zzb)) {
                return com_google_android_gms_common_zzc_zzaArr[i];
            }
            i++;
        }
        return null;
    }

    public boolean zza(PackageInfo packageInfo, boolean z) {
        if (!(packageInfo == null || packageInfo.signatures == null)) {
            zza zza;
            if (z) {
                zza = zza(packageInfo, zzd.Cn);
            } else {
                zza = zza(packageInfo, zzd.Cn[0]);
            }
            if (zza != null) {
                return true;
            }
        }
        return false;
    }

    public boolean zza(PackageManager packageManager, PackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        if (zza(packageInfo, false)) {
            return true;
        }
        if (!zza(packageInfo, true)) {
            return false;
        }
        if (GooglePlayServicesUtilLight.honorsDebugCertificates(this.mContext)) {
            return true;
        }
        Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
        return false;
    }
}
