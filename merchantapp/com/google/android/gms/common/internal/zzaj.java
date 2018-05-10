package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzaj implements Creator<ValidateAccountRequest> {
    static void zza(ValidateAccountRequest validateAccountRequest, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, validateAccountRequest.mVersionCode);
        zzb.zzc(parcel, 2, validateAccountRequest.zzaxo());
        zzb.zza(parcel, 3, validateAccountRequest.IN, false);
        zzb.zza(parcel, 4, validateAccountRequest.zzaxm(), i, false);
        zzb.zza(parcel, 5, validateAccountRequest.zzaxp(), false);
        zzb.zza(parcel, 6, validateAccountRequest.getCallingPackage(), false);
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzea(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzja(i);
    }

    public ValidateAccountRequest zzea(Parcel parcel) {
        int i = 0;
        String str = null;
        int zzec = zza.zzec(parcel);
        Bundle bundle = null;
        Scope[] scopeArr = null;
        IBinder iBinder = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzec) {
            int zzeb = zza.zzeb(parcel);
            switch (zza.zzjb(zzeb)) {
                case 1:
                    i2 = zza.zzg(parcel, zzeb);
                    break;
                case 2:
                    i = zza.zzg(parcel, zzeb);
                    break;
                case 3:
                    iBinder = zza.zzr(parcel, zzeb);
                    break;
                case 4:
                    scopeArr = (Scope[]) zza.zzb(parcel, zzeb, Scope.CREATOR);
                    break;
                case 5:
                    bundle = zza.zzs(parcel, zzeb);
                    break;
                case 6:
                    str = zza.zzq(parcel, zzeb);
                    break;
                default:
                    zza.zzb(parcel, zzeb);
                    break;
            }
        }
        if (parcel.dataPosition() == zzec) {
            return new ValidateAccountRequest(i2, i, iBinder, scopeArr, bundle, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public ValidateAccountRequest[] zzja(int i) {
        return new ValidateAccountRequest[i];
    }
}
