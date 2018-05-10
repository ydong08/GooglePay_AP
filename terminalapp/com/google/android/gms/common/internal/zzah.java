package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api.zzg;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class zzah<T extends IInterface> extends zzk<T> {
    private final zzg<T> KO;

    public zzah(Context context, Looper looper, int i, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, zzg com_google_android_gms_common_internal_zzg, zzg<T> com_google_android_gms_common_api_Api_zzg_T) {
        super(context, looper, i, com_google_android_gms_common_internal_zzg, connectionCallbacks, onConnectionFailedListener);
        this.KO = com_google_android_gms_common_api_Api_zzg_T;
    }

    public zzg<T> zzaxn() {
        return this.KO;
    }

    protected T zzbc(IBinder iBinder) {
        return this.KO.zzbc(iBinder);
    }

    protected String zzre() {
        return this.KO.zzre();
    }

    protected String zzrf() {
        return this.KO.zzrf();
    }
}
