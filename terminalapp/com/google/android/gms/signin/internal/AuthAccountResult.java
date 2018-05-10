package com.google.android.gms.signin.internal;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

public class AuthAccountResult extends AbstractSafeParcelable implements Result {
    public static final Creator<AuthAccountResult> CREATOR = new zza();
    private int bgG;
    private Intent bgH;
    final int mVersionCode;

    public AuthAccountResult() {
        this(0, null);
    }

    AuthAccountResult(int i, int i2, Intent intent) {
        this.mVersionCode = i;
        this.bgG = i2;
        this.bgH = intent;
    }

    public AuthAccountResult(int i, Intent intent) {
        this(2, i, intent);
    }

    public Status getStatus() {
        return this.bgG == 0 ? Status.Dq : Status.Du;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zza.zza(this, parcel, i);
    }

    public int zzcne() {
        return this.bgG;
    }

    public Intent zzcnf() {
        return this.bgH;
    }
}
