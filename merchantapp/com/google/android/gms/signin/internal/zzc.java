package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;

public class zzc implements Creator<CheckServerAuthResult> {
    static void zza(CheckServerAuthResult checkServerAuthResult, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, checkServerAuthResult.mVersionCode);
        zzb.zza(parcel, 2, checkServerAuthResult.bgI);
        zzb.zzc(parcel, 3, checkServerAuthResult.bgJ, false);
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzaal(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzajv(i);
    }

    public CheckServerAuthResult zzaal(Parcel parcel) {
        boolean z = false;
        int zzec = zza.zzec(parcel);
        List list = null;
        int i = 0;
        while (parcel.dataPosition() < zzec) {
            int zzeb = zza.zzeb(parcel);
            switch (zza.zzjb(zzeb)) {
                case 1:
                    i = zza.zzg(parcel, zzeb);
                    break;
                case 2:
                    z = zza.zzc(parcel, zzeb);
                    break;
                case 3:
                    list = zza.zzc(parcel, zzeb, Scope.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzeb);
                    break;
            }
        }
        if (parcel.dataPosition() == zzec) {
            return new CheckServerAuthResult(i, z, list);
        }
        throw new zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public CheckServerAuthResult[] zzajv(int i) {
        return new CheckServerAuthResult[i];
    }
}
