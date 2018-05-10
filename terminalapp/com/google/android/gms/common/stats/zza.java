package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zza implements Creator<ConnectionEvent> {
    static void zza(ConnectionEvent connectionEvent, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, connectionEvent.mVersionCode);
        zzb.zza(parcel, 2, connectionEvent.getTimeMillis());
        zzb.zza(parcel, 4, connectionEvent.zzayd(), false);
        zzb.zza(parcel, 5, connectionEvent.zzaye(), false);
        zzb.zza(parcel, 6, connectionEvent.zzayf(), false);
        zzb.zza(parcel, 7, connectionEvent.zzayg(), false);
        zzb.zza(parcel, 8, connectionEvent.zzayh(), false);
        zzb.zza(parcel, 10, connectionEvent.zzayk());
        zzb.zza(parcel, 11, connectionEvent.zzayj());
        zzb.zzc(parcel, 12, connectionEvent.getEventType());
        zzb.zza(parcel, 13, connectionEvent.zzayi(), false);
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzel(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzjm(i);
    }

    public ConnectionEvent zzel(Parcel parcel) {
        int zzec = com.google.android.gms.common.internal.safeparcel.zza.zzec(parcel);
        int i = 0;
        long j = 0;
        int i2 = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        long j2 = 0;
        long j3 = 0;
        while (parcel.dataPosition() < zzec) {
            int zzeb = com.google.android.gms.common.internal.safeparcel.zza.zzeb(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzjb(zzeb)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzeb);
                    break;
                case 2:
                    j = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, zzeb);
                    break;
                case 4:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzeb);
                    break;
                case 5:
                    str2 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzeb);
                    break;
                case 6:
                    str3 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzeb);
                    break;
                case 7:
                    str4 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzeb);
                    break;
                case 8:
                    str5 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzeb);
                    break;
                case 10:
                    j2 = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, zzeb);
                    break;
                case 11:
                    j3 = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, zzeb);
                    break;
                case 12:
                    i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzeb);
                    break;
                case 13:
                    str6 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzeb);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzeb);
                    break;
            }
        }
        if (parcel.dataPosition() == zzec) {
            return new ConnectionEvent(i, j, i2, str, str2, str3, str4, str5, str6, j2, j3);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public ConnectionEvent[] zzjm(int i) {
        return new ConnectionEvent[i];
    }
}
