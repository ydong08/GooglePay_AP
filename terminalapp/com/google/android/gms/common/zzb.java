package com.google.android.gms.common;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;

public class zzb implements Creator<ConnectionResult> {
    static void zza(ConnectionResult connectionResult, Parcel parcel, int i) {
        int zzed = com.google.android.gms.common.internal.safeparcel.zzb.zzed(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, connectionResult.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, connectionResult.getErrorCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, connectionResult.getResolution(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, connectionResult.getErrorMessage(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdn(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhr(i);
    }

    public ConnectionResult zzdn(Parcel parcel) {
        String str = null;
        int i = 0;
        int zzec = zza.zzec(parcel);
        PendingIntent pendingIntent = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzec) {
            PendingIntent pendingIntent2;
            int i3;
            String str2;
            int zzeb = zza.zzeb(parcel);
            String str3;
            switch (zza.zzjb(zzeb)) {
                case 1:
                    str3 = str;
                    pendingIntent2 = pendingIntent;
                    i3 = i;
                    i = zza.zzg(parcel, zzeb);
                    str2 = str3;
                    break;
                case 2:
                    i = i2;
                    PendingIntent pendingIntent3 = pendingIntent;
                    i3 = zza.zzg(parcel, zzeb);
                    str2 = str;
                    pendingIntent2 = pendingIntent3;
                    break;
                case 3:
                    i3 = i;
                    i = i2;
                    str3 = str;
                    pendingIntent2 = (PendingIntent) zza.zza(parcel, zzeb, PendingIntent.CREATOR);
                    str2 = str3;
                    break;
                case 4:
                    str2 = zza.zzq(parcel, zzeb);
                    pendingIntent2 = pendingIntent;
                    i3 = i;
                    i = i2;
                    break;
                default:
                    zza.zzb(parcel, zzeb);
                    str2 = str;
                    pendingIntent2 = pendingIntent;
                    i3 = i;
                    i = i2;
                    break;
            }
            i2 = i;
            i = i3;
            pendingIntent = pendingIntent2;
            str = str2;
        }
        if (parcel.dataPosition() == zzec) {
            return new ConnectionResult(i2, i, pendingIntent, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public ConnectionResult[] zzhr(int i) {
        return new ConnectionResult[i];
    }
}
