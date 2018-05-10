package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

public class SignInResponse extends AbstractSafeParcelable {
    public static final Creator<SignInResponse> CREATOR = new zzi();
    private final ConnectionResult CE;
    private final ResolveAccountResponse bgO;
    final int mVersionCode;

    SignInResponse(int i, ConnectionResult connectionResult, ResolveAccountResponse resolveAccountResponse) {
        this.mVersionCode = i;
        this.CE = connectionResult;
        this.bgO = resolveAccountResponse;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzi.zza(this, parcel, i);
    }

    public ConnectionResult zzaxh() {
        return this.CE;
    }

    public ResolveAccountResponse zzcnj() {
        return this.bgO;
    }
}
