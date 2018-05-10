package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class ConnectionEvent extends StatsEvent {
    public static final Creator<ConnectionEvent> CREATOR = new zza();
    private final long Ml;
    private int Mm;
    private final String Mn;
    private final String Mo;
    private final String Mp;
    private final String Mq;
    private final String Mr;
    private final String Ms;
    private final long Mt;
    private final long Mu;
    private long Mv;
    final int mVersionCode;

    ConnectionEvent(int i, long j, int i2, String str, String str2, String str3, String str4, String str5, String str6, long j2, long j3) {
        this.mVersionCode = i;
        this.Ml = j;
        this.Mm = i2;
        this.Mn = str;
        this.Mo = str2;
        this.Mp = str3;
        this.Mq = str4;
        this.Mv = -1;
        this.Mr = str5;
        this.Ms = str6;
        this.Mt = j2;
        this.Mu = j3;
    }

    public ConnectionEvent(long j, int i, String str, String str2, String str3, String str4, String str5, String str6, long j2, long j3) {
        this(1, j, i, str, str2, str3, str4, str5, str6, j2, j3);
    }

    public long getDurationMillis() {
        return this.Mv;
    }

    public int getEventType() {
        return this.Mm;
    }

    public long getTimeMillis() {
        return this.Ml;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zza.zza(this, parcel, i);
    }

    public String zzayd() {
        return this.Mn;
    }

    public String zzaye() {
        return this.Mo;
    }

    public String zzayf() {
        return this.Mp;
    }

    public String zzayg() {
        return this.Mq;
    }

    public String zzayh() {
        return this.Mr;
    }

    public String zzayi() {
        return this.Ms;
    }

    public long zzayj() {
        return this.Mu;
    }

    public long zzayk() {
        return this.Mt;
    }

    public String zzayl() {
        String valueOf = String.valueOf("\t");
        String valueOf2 = String.valueOf(zzayd());
        String valueOf3 = String.valueOf(zzaye());
        String valueOf4 = String.valueOf("\t");
        String valueOf5 = String.valueOf(zzayf());
        String valueOf6 = String.valueOf(zzayg());
        String valueOf7 = String.valueOf("\t");
        String str = this.Mr == null ? "" : this.Mr;
        String valueOf8 = String.valueOf("\t");
        return new StringBuilder(((((((((String.valueOf(valueOf).length() + 22) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()) + String.valueOf(valueOf4).length()) + String.valueOf(valueOf5).length()) + String.valueOf(valueOf6).length()) + String.valueOf(valueOf7).length()) + String.valueOf(str).length()) + String.valueOf(valueOf8).length()).append(valueOf).append(valueOf2).append("/").append(valueOf3).append(valueOf4).append(valueOf5).append("/").append(valueOf6).append(valueOf7).append(str).append(valueOf8).append(zzayj()).toString();
    }
}
