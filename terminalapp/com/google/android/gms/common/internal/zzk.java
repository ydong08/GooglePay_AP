package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzd.zzb;
import com.google.android.gms.common.internal.zzd.zzc;
import java.util.Set;

public abstract class zzk<T extends IInterface> extends zzd<T> implements zze {
    private final zzg EO;
    private final Account P;
    private final Set<Scope> fQ;

    protected zzk(Context context, Looper looper, int i, zzg com_google_android_gms_common_internal_zzg, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, zzm.zzcg(context), GoogleApiAvailability.getInstance(), i, com_google_android_gms_common_internal_zzg, (ConnectionCallbacks) zzab.zzae(connectionCallbacks), (OnConnectionFailedListener) zzab.zzae(onConnectionFailedListener));
    }

    protected zzk(Context context, Looper looper, zzm com_google_android_gms_common_internal_zzm, GoogleApiAvailability googleApiAvailability, int i, zzg com_google_android_gms_common_internal_zzg, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, com_google_android_gms_common_internal_zzm, googleApiAvailability, i, zza(connectionCallbacks), zza(onConnectionFailedListener), com_google_android_gms_common_internal_zzg.zzawn());
        this.EO = com_google_android_gms_common_internal_zzg;
        this.P = com_google_android_gms_common_internal_zzg.getAccount();
        this.fQ = zzb(com_google_android_gms_common_internal_zzg.zzawk());
    }

    private static zzb zza(ConnectionCallbacks connectionCallbacks) {
        return connectionCallbacks == null ? null : new zzb(connectionCallbacks);
    }

    private static zzc zza(OnConnectionFailedListener onConnectionFailedListener) {
        return onConnectionFailedListener == null ? null : new zzc(onConnectionFailedListener);
    }

    private Set<Scope> zzb(Set<Scope> set) {
        Set<Scope> zzc = zzc(set);
        for (Scope contains : zzc) {
            if (!set.contains(contains)) {
                throw new IllegalStateException("Expanding scopes is not permitted, use implied scopes instead");
            }
        }
        return zzc;
    }

    public final Account getAccount() {
        return this.P;
    }

    protected Set<Scope> zzc(Set<Scope> set) {
        return set;
    }
}
