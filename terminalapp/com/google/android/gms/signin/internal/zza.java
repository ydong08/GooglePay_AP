package com.google.android.gms.signin.internal;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zza implements Creator<AuthAccountResult> {
    static void zza(AuthAccountResult authAccountResult, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, authAccountResult.mVersionCode);
        zzb.zzc(parcel, 2, authAccountResult.zzcne());
        zzb.zza(parcel, 3, authAccountResult.zzcnf(), i, false);
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzaak(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaju(i);
    }

    public AuthAccountResult zzaak(Parcel parcel) {
        int i = 0;
        int zzec = com.google.android.gms.common.internal.safeparcel.zza.zzec(parcel);
        Intent intent = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzec) {
            int zzeb = com.google.android.gms.common.internal.safeparcel.zza.zzeb(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzjb(zzeb)) {
                case 1:
                    i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzeb);
                    break;
                case 2:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzeb);
                    break;
                case 3:
                    intent = (Intent) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzeb, Intent.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzeb);
                    break;
            }
        }
        if (parcel.dataPosition() == zzec) {
            return new AuthAccountResult(i2, i, intent);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public AuthAccountResult[] zzaju(int i) {
        return new AuthAccountResult[i];
    }
}
