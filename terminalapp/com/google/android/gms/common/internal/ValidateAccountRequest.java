package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

@Deprecated
public class ValidateAccountRequest extends AbstractSafeParcelable {
    public static final Creator<ValidateAccountRequest> CREATOR = new zzaj();
    private final Scope[] Cx;
    final IBinder IN;
    private final int KR;
    private final Bundle KS;
    private final String KT;
    final int mVersionCode;

    ValidateAccountRequest(int i, int i2, IBinder iBinder, Scope[] scopeArr, Bundle bundle, String str) {
        this.mVersionCode = i;
        this.KR = i2;
        this.IN = iBinder;
        this.Cx = scopeArr;
        this.KS = bundle;
        this.KT = str;
    }

    public String getCallingPackage() {
        return this.KT;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzaj.zza(this, parcel, i);
    }

    public Scope[] zzaxm() {
        return this.Cx;
    }

    public int zzaxo() {
        return this.KR;
    }

    public Bundle zzaxp() {
        return this.KS;
    }
}
