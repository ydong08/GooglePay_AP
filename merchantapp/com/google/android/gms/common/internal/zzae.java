package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzae implements Creator<SignInButtonConfig> {
    static void zza(SignInButtonConfig signInButtonConfig, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, signInButtonConfig.mVersionCode);
        zzb.zzc(parcel, 2, signInButtonConfig.zzaxk());
        zzb.zzc(parcel, 3, signInButtonConfig.zzaxl());
        zzb.zza(parcel, 4, signInButtonConfig.zzaxm(), i, false);
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdz(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zziz(i);
    }

    public SignInButtonConfig zzdz(Parcel parcel) {
        int i = 0;
        int zzec = zza.zzec(parcel);
        Scope[] scopeArr = null;
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
                    scopeArr = (Scope[]) zza.zzb(parcel, zzeb, Scope.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzeb);
                    break;
            }
        }
        if (parcel.dataPosition() == zzec) {
            return new SignInButtonConfig(i3, i2, i, scopeArr);
        }
        throw new zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public SignInButtonConfig[] zziz(int i) {
        return new SignInButtonConfig[i];
    }
}
