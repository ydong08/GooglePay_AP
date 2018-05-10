package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzi implements Creator<SignInResponse> {
    static void zza(SignInResponse signInResponse, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, signInResponse.mVersionCode);
        zzb.zza(parcel, 2, signInResponse.zzaxh(), i, false);
        zzb.zza(parcel, 3, signInResponse.zzcnj(), i, false);
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzaao(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzajz(i);
    }

    public SignInResponse zzaao(Parcel parcel) {
        ResolveAccountResponse resolveAccountResponse = null;
        int zzec = zza.zzec(parcel);
        int i = 0;
        ConnectionResult connectionResult = null;
        while (parcel.dataPosition() < zzec) {
            ConnectionResult connectionResult2;
            int zzg;
            ResolveAccountResponse resolveAccountResponse2;
            int zzeb = zza.zzeb(parcel);
            switch (zza.zzjb(zzeb)) {
                case 1:
                    ResolveAccountResponse resolveAccountResponse3 = resolveAccountResponse;
                    connectionResult2 = connectionResult;
                    zzg = zza.zzg(parcel, zzeb);
                    resolveAccountResponse2 = resolveAccountResponse3;
                    break;
                case 2:
                    zzg = i;
                    ConnectionResult connectionResult3 = (ConnectionResult) zza.zza(parcel, zzeb, ConnectionResult.CREATOR);
                    resolveAccountResponse2 = resolveAccountResponse;
                    connectionResult2 = connectionResult3;
                    break;
                case 3:
                    resolveAccountResponse2 = (ResolveAccountResponse) zza.zza(parcel, zzeb, ResolveAccountResponse.CREATOR);
                    connectionResult2 = connectionResult;
                    zzg = i;
                    break;
                default:
                    zza.zzb(parcel, zzeb);
                    resolveAccountResponse2 = resolveAccountResponse;
                    connectionResult2 = connectionResult;
                    zzg = i;
                    break;
            }
            i = zzg;
            connectionResult = connectionResult2;
            resolveAccountResponse = resolveAccountResponse2;
        }
        if (parcel.dataPosition() == zzec) {
            return new SignInResponse(i, connectionResult, resolveAccountResponse);
        }
        throw new zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public SignInResponse[] zzajz(int i) {
        return new SignInResponse[i];
    }
}
