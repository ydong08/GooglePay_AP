package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

public class SignInButtonConfig extends AbstractSafeParcelable {
    public static final Creator<SignInButtonConfig> CREATOR = new zzae();
    private final Scope[] Cx;
    private final int KL;
    private final int KM;
    final int mVersionCode;

    SignInButtonConfig(int i, int i2, int i3, Scope[] scopeArr) {
        this.mVersionCode = i;
        this.KL = i2;
        this.KM = i3;
        this.Cx = scopeArr;
    }

    public SignInButtonConfig(int i, int i2, Scope[] scopeArr) {
        this(1, i, i2, scopeArr);
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzae.zza(this, parcel, i);
    }

    public int zzaxk() {
        return this.KL;
    }

    public int zzaxl() {
        return this.KM;
    }

    public Scope[] zzaxm() {
        return this.Cx;
    }
}
