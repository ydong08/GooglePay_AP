package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.zzq.zza;
import java.util.Collection;

public class GetServiceRequest extends AbstractSafeParcelable {
    public static final Creator<GetServiceRequest> CREATOR = new zzj();
    final int JW;
    int JX;
    String JY;
    IBinder JZ;
    Scope[] Ka;
    Bundle Kb;
    Account Kc;
    long Kd;
    final int version;

    public GetServiceRequest(int i) {
        this.version = 3;
        this.JX = GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        this.JW = i;
    }

    GetServiceRequest(int i, int i2, int i3, String str, IBinder iBinder, Scope[] scopeArr, Bundle bundle, Account account, long j) {
        this.version = i;
        this.JW = i2;
        this.JX = i3;
        this.JY = str;
        if (i < 2) {
            this.Kc = zzgv(iBinder);
        } else {
            this.JZ = iBinder;
            this.Kc = account;
        }
        this.Ka = scopeArr;
        this.Kb = bundle;
        this.Kd = j;
    }

    private Account zzgv(IBinder iBinder) {
        return iBinder != null ? zza.zza(zza.zzgw(iBinder)) : null;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzj.zza(this, parcel, i);
    }

    public GetServiceRequest zzb(zzq com_google_android_gms_common_internal_zzq) {
        if (com_google_android_gms_common_internal_zzq != null) {
            this.JZ = com_google_android_gms_common_internal_zzq.asBinder();
        }
        return this;
    }

    public GetServiceRequest zzc(Account account) {
        this.Kc = account;
        return this;
    }

    public GetServiceRequest zzf(Collection<Scope> collection) {
        this.Ka = (Scope[]) collection.toArray(new Scope[collection.size()]);
        return this;
    }

    public GetServiceRequest zzgr(String str) {
        this.JY = str;
        return this;
    }

    public GetServiceRequest zzt(Bundle bundle) {
        this.Kb = bundle;
        return this;
    }
}
