package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.signin.internal.zzg;

public final class zzaci {
    public static final Api<zzack> API = new Api("SignIn.API", cc, cb$9HHMUR9FCTNMUPRCCKNM2RJ4E9NMIP1FCTMN6BR3DTMMQRRE5TGN0Q9F85O6I93QF9J3M___0);
    public static final Api<zza> QR = new Api("SignIn.INTERNAL_API", bgA, bgz$9HHMUR9FCTNMUPRCCKNM2RJ4E9NMIP1FCTMN6BR3DTMMQRRE5TGN0Q9F85O6I93QF9J3M___0);
    static final com.google.android.gms.common.api.Api.zza<zzg, zza> bgA = new com.google.android.gms.common.api.Api.zza<zzg, zza>() {
        public zzg zza(Context context, Looper looper, com.google.android.gms.common.internal.zzg com_google_android_gms_common_internal_zzg, zza com_google_android_gms_internal_zzaci_zza, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzg(context, looper, false, com_google_android_gms_common_internal_zzg, com_google_android_gms_internal_zzaci_zza.zzcmx(), connectionCallbacks, onConnectionFailedListener);
        }
    };
    public static final zzc bgz$9HHMUR9FCTNMUPRCCKNM2RJ4E9NMIP1FCTMN6BR3DTMMQRRE5TGN0Q9F85O6I93QF9J3M___0 = new zzc((byte) 0);
    public static final zzc cb$9HHMUR9FCTNMUPRCCKNM2RJ4E9NMIP1FCTMN6BR3DTMMQRRE5TGN0Q9F85O6I93QF9J3M___0 = new zzc((byte) 0);
    public static final com.google.android.gms.common.api.Api.zza<zzg, zzack> cc = new com.google.android.gms.common.api.Api.zza<zzg, zzack>() {
        public zzg zza(Context context, Looper looper, com.google.android.gms.common.internal.zzg com_google_android_gms_common_internal_zzg, zzack com_google_android_gms_internal_zzack, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzg(context, looper, true, com_google_android_gms_common_internal_zzg, com_google_android_gms_internal_zzack == null ? zzack.bgB : com_google_android_gms_internal_zzack, connectionCallbacks, onConnectionFailedListener);
        }
    };
    public static final Scope fH = new Scope("profile");
    public static final Scope fI = new Scope("email");

    public static class zza implements HasOptions {
        public Bundle zzcmx() {
            return null;
        }
    }
}
