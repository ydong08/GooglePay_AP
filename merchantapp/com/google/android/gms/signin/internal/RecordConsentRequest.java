package com.google.android.gms.signin.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

public class RecordConsentRequest extends AbstractSafeParcelable {
    public static final Creator<RecordConsentRequest> CREATOR = new zzf();
    private final Account P;
    private final Scope[] bgK;
    private final String fO;
    final int mVersionCode;

    RecordConsentRequest(int i, Account account, Scope[] scopeArr, String str) {
        this.mVersionCode = i;
        this.P = account;
        this.bgK = scopeArr;
        this.fO = str;
    }

    public Account getAccount() {
        return this.P;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzf.zza(this, parcel, i);
    }

    public String zzaem() {
        return this.fO;
    }

    public Scope[] zzcng() {
        return this.bgK;
    }
}
