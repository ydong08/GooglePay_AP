package com.google.android.gms.common.api;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.zzaa;

public final class Status extends AbstractSafeParcelable implements Result, ReflectedParcelable {
    public static final Creator<Status> CREATOR = new zze();
    public static final Status Dq = new Status(0);
    public static final Status Dr = new Status(14);
    public static final Status Ds = new Status(8);
    public static final Status Dt = new Status(15);
    public static final Status Du = new Status(16);
    public static final Status Dv = new Status(17);
    public static final Status Dw = new Status(18);
    private final String Cb;
    private final PendingIntent mPendingIntent;
    private final int mVersionCode;
    private final int yj;

    public Status(int i) {
        this(i, null);
    }

    Status(int i, int i2, String str, PendingIntent pendingIntent) {
        this.mVersionCode = i;
        this.yj = i2;
        this.Cb = str;
        this.mPendingIntent = pendingIntent;
    }

    public Status(int i, String str) {
        this(1, i, str, null);
    }

    private String zzasf() {
        return this.Cb != null ? this.Cb : CommonStatusCodes.getStatusCodeString(this.yj);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Status)) {
            return false;
        }
        Status status = (Status) obj;
        return this.mVersionCode == status.mVersionCode && this.yj == status.yj && zzaa.equal(this.Cb, status.Cb) && zzaa.equal(this.mPendingIntent, status.mPendingIntent);
    }

    PendingIntent getPendingIntent() {
        return this.mPendingIntent;
    }

    public Status getStatus() {
        return this;
    }

    public int getStatusCode() {
        return this.yj;
    }

    public String getStatusMessage() {
        return this.Cb;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        return zzaa.hashCode(Integer.valueOf(this.mVersionCode), Integer.valueOf(this.yj), this.Cb, this.mPendingIntent);
    }

    public boolean isSuccess() {
        return this.yj <= 0;
    }

    public String toString() {
        return zzaa.zzad(this).zzh("statusCode", zzasf()).zzh("resolution", this.mPendingIntent).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zze.zza(this, parcel, i);
    }
}
