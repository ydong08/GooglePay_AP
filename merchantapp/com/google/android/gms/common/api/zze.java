package com.google.android.gms.common.api;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zze implements Creator<Status> {
    static void zza(Status status, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, status.getStatusCode());
        zzb.zza(parcel, 2, status.getStatusMessage(), false);
        zzb.zza(parcel, 3, status.getPendingIntent(), i, false);
        zzb.zzc(parcel, 1000, status.getVersionCode());
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdp(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhv(i);
    }

    public Status zzdp(Parcel parcel) {
        PendingIntent pendingIntent = null;
        int i = 0;
        int zzec = zza.zzec(parcel);
        String str = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzec) {
            int zzeb = zza.zzeb(parcel);
            switch (zza.zzjb(zzeb)) {
                case 1:
                    i = zza.zzg(parcel, zzeb);
                    break;
                case 2:
                    str = zza.zzq(parcel, zzeb);
                    break;
                case 3:
                    pendingIntent = (PendingIntent) zza.zza(parcel, zzeb, PendingIntent.CREATOR);
                    break;
                case 1000:
                    i2 = zza.zzg(parcel, zzeb);
                    break;
                default:
                    zza.zzb(parcel, zzeb);
                    break;
            }
        }
        if (parcel.dataPosition() == zzec) {
            return new Status(i2, i, str, pendingIntent);
        }
        throw new zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public Status[] zzhv(int i) {
        return new Status[i];
    }
}
