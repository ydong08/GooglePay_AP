package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzc implements Creator<AuthAccountRequest> {
    static void zza(AuthAccountRequest authAccountRequest, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, authAccountRequest.mVersionCode);
        zzb.zza(parcel, 2, authAccountRequest.IN, false);
        zzb.zza(parcel, 3, authAccountRequest.Cx, i, false);
        zzb.zza(parcel, 4, authAccountRequest.IO, false);
        zzb.zza(parcel, 5, authAccountRequest.IP, false);
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdu(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zziq(i);
    }

    public AuthAccountRequest zzdu(Parcel parcel) {
        Integer num = null;
        int zzec = zza.zzec(parcel);
        int i = 0;
        Integer num2 = null;
        Scope[] scopeArr = null;
        IBinder iBinder = null;
        while (parcel.dataPosition() < zzec) {
            int zzeb = zza.zzeb(parcel);
            switch (zza.zzjb(zzeb)) {
                case 1:
                    i = zza.zzg(parcel, zzeb);
                    break;
                case 2:
                    iBinder = zza.zzr(parcel, zzeb);
                    break;
                case 3:
                    scopeArr = (Scope[]) zza.zzb(parcel, zzeb, Scope.CREATOR);
                    break;
                case 4:
                    num2 = zza.zzh(parcel, zzeb);
                    break;
                case 5:
                    num = zza.zzh(parcel, zzeb);
                    break;
                default:
                    zza.zzb(parcel, zzeb);
                    break;
            }
        }
        if (parcel.dataPosition() == zzec) {
            return new AuthAccountRequest(i, iBinder, scopeArr, num2, num);
        }
        throw new zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public AuthAccountRequest[] zziq(int i) {
        return new AuthAccountRequest[i];
    }
}
