package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzad implements Creator<ResolveAccountResponse> {
    static void zza(ResolveAccountResponse resolveAccountResponse, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, resolveAccountResponse.mVersionCode);
        zzb.zza(parcel, 2, resolveAccountResponse.IN, false);
        zzb.zza(parcel, 3, resolveAccountResponse.zzaxh(), i, false);
        zzb.zza(parcel, 4, resolveAccountResponse.zzaxi());
        zzb.zza(parcel, 5, resolveAccountResponse.zzaxj());
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdy(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zziy(i);
    }

    public ResolveAccountResponse zzdy(Parcel parcel) {
        ConnectionResult connectionResult = null;
        boolean z = false;
        int zzec = zza.zzec(parcel);
        boolean z2 = false;
        IBinder iBinder = null;
        int i = 0;
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
                    connectionResult = (ConnectionResult) zza.zza(parcel, zzeb, ConnectionResult.CREATOR);
                    break;
                case 4:
                    z2 = zza.zzc(parcel, zzeb);
                    break;
                case 5:
                    z = zza.zzc(parcel, zzeb);
                    break;
                default:
                    zza.zzb(parcel, zzeb);
                    break;
            }
        }
        if (parcel.dataPosition() == zzec) {
            return new ResolveAccountResponse(i, iBinder, connectionResult, z2, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public ResolveAccountResponse[] zziy(int i) {
        return new ResolveAccountResponse[i];
    }
}
