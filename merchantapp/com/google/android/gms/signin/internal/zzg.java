package com.google.android.gms.signin.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzk;
import com.google.android.gms.internal.zzack;
import com.google.android.gms.signin.internal.zze.zza;

public class zzg extends zzk<zze> implements zze {
    private final com.google.android.gms.common.internal.zzg EO;
    private Integer JO;
    private final boolean bgL;
    private final Bundle bgM;

    public zzg(Context context, Looper looper, boolean z, com.google.android.gms.common.internal.zzg com_google_android_gms_common_internal_zzg, Bundle bundle, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 44, com_google_android_gms_common_internal_zzg, connectionCallbacks, onConnectionFailedListener);
        this.bgL = z;
        this.EO = com_google_android_gms_common_internal_zzg;
        this.bgM = bundle;
        this.JO = com_google_android_gms_common_internal_zzg.zzawq();
    }

    public zzg(Context context, Looper looper, boolean z, com.google.android.gms.common.internal.zzg com_google_android_gms_common_internal_zzg, zzack com_google_android_gms_internal_zzack, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, z, com_google_android_gms_common_internal_zzg, zza(com_google_android_gms_common_internal_zzg), connectionCallbacks, onConnectionFailedListener);
    }

    public static Bundle zza(com.google.android.gms.common.internal.zzg com_google_android_gms_common_internal_zzg) {
        zzack zzawp = com_google_android_gms_common_internal_zzg.zzawp();
        Integer zzawq = com_google_android_gms_common_internal_zzg.zzawq();
        Bundle bundle = new Bundle();
        bundle.putParcelable("com.google.android.gms.signin.internal.clientRequestedAccount", com_google_android_gms_common_internal_zzg.getAccount());
        if (zzawq != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", zzawq.intValue());
        }
        if (zzawp != null) {
            bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", zzawp.zzcmz());
            bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", zzawp.zzaej());
            bundle.putString("com.google.android.gms.signin.internal.serverClientId", zzawp.zzaem());
            bundle.putBoolean("com.google.android.gms.signin.internal.usePromptModeForAuthCode", true);
            bundle.putBoolean("com.google.android.gms.signin.internal.forceCodeForRefreshToken", zzawp.zzael());
            bundle.putString("com.google.android.gms.signin.internal.hostedDomain", zzawp.zzaen());
            bundle.putBoolean("com.google.android.gms.signin.internal.waitForAccessTokenRefresh", zzawp.zzcna());
            if (zzawp.zzcnb() != null) {
                bundle.putLong("com.google.android.gms.signin.internal.authApiSignInModuleVersion", zzawp.zzcnb().longValue());
            }
            if (zzawp.zzcnc() != null) {
                bundle.putLong("com.google.android.gms.signin.internal.realClientLibraryVersion", zzawp.zzcnc().longValue());
            }
        }
        return bundle;
    }

    protected Bundle zzadn() {
        if (!getContext().getPackageName().equals(this.EO.zzawm())) {
            this.bgM.putString("com.google.android.gms.signin.internal.realClientPackageName", this.EO.zzawm());
        }
        return this.bgM;
    }

    public boolean zzaec() {
        return this.bgL;
    }

    protected /* synthetic */ IInterface zzbc(IBinder iBinder) {
        return zzqz(iBinder);
    }

    protected zze zzqz(IBinder iBinder) {
        return zza.zzqy(iBinder);
    }

    protected String zzre() {
        return "com.google.android.gms.signin.service.START";
    }

    protected String zzrf() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }
}
