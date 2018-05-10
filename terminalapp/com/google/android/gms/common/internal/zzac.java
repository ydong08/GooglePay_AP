package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzac implements Creator<ResolveAccountRequest> {
    static void zza(ResolveAccountRequest resolveAccountRequest, Parcel parcel, int i) {
        int zzed = zzb.zzed(parcel);
        zzb.zzc(parcel, 1, resolveAccountRequest.mVersionCode);
        zzb.zza(parcel, 2, resolveAccountRequest.getAccount(), i, false);
        zzb.zzc(parcel, 3, resolveAccountRequest.getSessionId());
        zzb.zza(parcel, 4, resolveAccountRequest.zzaxf(), i, false);
        zzb.zzaj(parcel, zzed);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdx(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzix(i);
    }

    public ResolveAccountRequest zzdx(Parcel parcel) {
        GoogleSignInAccount googleSignInAccount = null;
        int i = 0;
        int zzec = zza.zzec(parcel);
        Account account = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzec) {
            int i3;
            Account account2;
            int zzg;
            GoogleSignInAccount googleSignInAccount2;
            int zzeb = zza.zzeb(parcel);
            GoogleSignInAccount googleSignInAccount3;
            switch (zza.zzjb(zzeb)) {
                case 1:
                    googleSignInAccount3 = googleSignInAccount;
                    i3 = i;
                    account2 = account;
                    zzg = zza.zzg(parcel, zzeb);
                    googleSignInAccount2 = googleSignInAccount3;
                    break;
                case 2:
                    zzg = i2;
                    int i4 = i;
                    account2 = (Account) zza.zza(parcel, zzeb, Account.CREATOR);
                    googleSignInAccount2 = googleSignInAccount;
                    i3 = i4;
                    break;
                case 3:
                    account2 = account;
                    zzg = i2;
                    googleSignInAccount3 = googleSignInAccount;
                    i3 = zza.zzg(parcel, zzeb);
                    googleSignInAccount2 = googleSignInAccount3;
                    break;
                case 4:
                    googleSignInAccount2 = (GoogleSignInAccount) zza.zza(parcel, zzeb, GoogleSignInAccount.CREATOR);
                    i3 = i;
                    account2 = account;
                    zzg = i2;
                    break;
                default:
                    zza.zzb(parcel, zzeb);
                    googleSignInAccount2 = googleSignInAccount;
                    i3 = i;
                    account2 = account;
                    zzg = i2;
                    break;
            }
            i2 = zzg;
            account = account2;
            i = i3;
            googleSignInAccount = googleSignInAccount2;
        }
        if (parcel.dataPosition() == zzec) {
            return new ResolveAccountRequest(i2, account, i, googleSignInAccount);
        }
        throw new zza.zza("Overread allowed size end=" + zzec, parcel);
    }

    public ResolveAccountRequest[] zzix(int i) {
        return new ResolveAccountRequest[i];
    }
}
