package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import java.util.List;

public class CheckServerAuthResult extends AbstractSafeParcelable {
    public static final Creator<CheckServerAuthResult> CREATOR = new zzc();
    final boolean bgI;
    final List<Scope> bgJ;
    final int mVersionCode;

    CheckServerAuthResult(int i, boolean z, List<Scope> list) {
        this.mVersionCode = i;
        this.bgI = z;
        this.bgJ = list;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }
}
