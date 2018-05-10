package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

public class ResolveAccountRequest extends AbstractSafeParcelable {
    public static final Creator<ResolveAccountRequest> CREATOR = new zzac();
    private final int KI;
    private final GoogleSignInAccount KJ;
    private final Account P;
    final int mVersionCode;

    ResolveAccountRequest(int i, Account account, int i2, GoogleSignInAccount googleSignInAccount) {
        this.mVersionCode = i;
        this.P = account;
        this.KI = i2;
        this.KJ = googleSignInAccount;
    }

    public Account getAccount() {
        return this.P;
    }

    public int getSessionId() {
        return this.KI;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzac.zza(this, parcel, i);
    }

    public GoogleSignInAccount zzaxf() {
        return this.KJ;
    }
}
