package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ResolveAccountRequest;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzh implements Creator<SignInRequest> {
    static void zza(SignInRequest signInRequest, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, signInRequest.mVersionCode);
        zzb.zza(parcel, 2, signInRequest.zzcni(), i, false);
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzaan(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzajy(i);
    }

    public SignInRequest zzaan(Parcel parcel) {
        int zzec = zza.zzec(parcel);
        int i = 0;
        ResolveAccountRequest resolveAccountRequest = null;
        while (parcel.dataPosition() < zzec) {
            int zzeb = zza.zzeb(parcel);
            switch (zza.zzjb(zzeb)) {
                case 1:
                    i = zza.zzg(parcel, zzeb);
                    break;
                case 2:
                    resolveAccountRequest = (ResolveAccountRequest) zza.zza(parcel, zzeb, ResolveAccountRequest.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzeb);
                    break;
            }
        }
        if (parcel.dataPosition() == zzec) {
            return new SignInRequest(i, resolveAccountRequest);
        }
        throw new zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public SignInRequest[] zzajy(int i) {
        return new SignInRequest[i];
    }
}
