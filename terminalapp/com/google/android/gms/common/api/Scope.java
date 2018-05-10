package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.zzab;

public final class Scope extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<Scope> CREATOR = new zzd();
    private final String Dp;
    final int mVersionCode;

    Scope(int i, String str) {
        zzab.zzi(str, "scopeUri must not be null or empty");
        this.mVersionCode = i;
        this.Dp = str;
    }

    public Scope(String str) {
        this(1, str);
    }

    public boolean equals(Object obj) {
        return this == obj ? true : !(obj instanceof Scope) ? false : this.Dp.equals(((Scope) obj).Dp);
    }

    public int hashCode() {
        return this.Dp.hashCode();
    }

    public String toString() {
        return this.Dp;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzd.zza(this, parcel, i);
    }

    public String zzase() {
        return this.Dp;
    }
}
