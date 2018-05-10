package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzd implements Creator<Scope> {
    static void zza(Scope scope, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, scope.mVersionCode);
        zzb.zza(parcel, 2, scope.zzase(), false);
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdo(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhu(i);
    }

    public Scope zzdo(Parcel parcel) {
        int zzec = zza.zzec(parcel);
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < zzec) {
            int zzeb = zza.zzeb(parcel);
            switch (zza.zzjb(zzeb)) {
                case 1:
                    i = zza.zzg(parcel, zzeb);
                    break;
                case 2:
                    str = zza.zzq(parcel, zzeb);
                    break;
                default:
                    zza.zzb(parcel, zzeb);
                    break;
            }
        }
        if (parcel.dataPosition() == zzec) {
            return new Scope(i, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public Scope[] zzhu(int i) {
        return new Scope[i];
    }
}
