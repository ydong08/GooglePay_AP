package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzj implements Creator<GetServiceRequest> {
    static void zza(GetServiceRequest getServiceRequest, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, getServiceRequest.version);
        zzb.zzc(parcel, 2, getServiceRequest.JW);
        zzb.zzc(parcel, 3, getServiceRequest.JX);
        zzb.zza(parcel, 4, getServiceRequest.JY, false);
        zzb.zza(parcel, 5, getServiceRequest.JZ, false);
        zzb.zza(parcel, 6, getServiceRequest.Ka, i, false);
        zzb.zza(parcel, 7, getServiceRequest.Kb, false);
        zzb.zza(parcel, 8, getServiceRequest.Kc, i, false);
        zzb.zza(parcel, 9, getServiceRequest.Kd);
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdw(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzit(i);
    }

    public GetServiceRequest zzdw(Parcel parcel) {
        int i = 0;
        Account account = null;
        int zzec = zza.zzec(parcel);
        long j = 0;
        Bundle bundle = null;
        Scope[] scopeArr = null;
        IBinder iBinder = null;
        String str = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzec) {
            int zzeb = zza.zzeb(parcel);
            switch (zza.zzjb(zzeb)) {
                case 1:
                    i3 = zza.zzg(parcel, zzeb);
                    break;
                case 2:
                    i2 = zza.zzg(parcel, zzeb);
                    break;
                case 3:
                    i = zza.zzg(parcel, zzeb);
                    break;
                case 4:
                    str = zza.zzq(parcel, zzeb);
                    break;
                case 5:
                    iBinder = zza.zzr(parcel, zzeb);
                    break;
                case 6:
                    scopeArr = (Scope[]) zza.zzb(parcel, zzeb, Scope.CREATOR);
                    break;
                case 7:
                    bundle = zza.zzs(parcel, zzeb);
                    break;
                case 8:
                    account = (Account) zza.zza(parcel, zzeb, Account.CREATOR);
                    break;
                case 9:
                    j = zza.zzi(parcel, zzeb);
                    break;
                default:
                    zza.zzb(parcel, zzeb);
                    break;
            }
        }
        if (parcel.dataPosition() == zzec) {
            return new GetServiceRequest(i3, i2, i, str, iBinder, scopeArr, bundle, account, j);
        }
        throw new zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public GetServiceRequest[] zzit(int i) {
        return new GetServiceRequest[i];
    }
}
