package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtilLight;

public class zza extends com.google.android.gms.common.internal.zzq.zza {
    int zzajy;

    public static Account zza(zzq com_google_android_gms_common_internal_zzq) {
        Account account = null;
        if (com_google_android_gms_common_internal_zzq != null) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                account = com_google_android_gms_common_internal_zzq.getAccount();
            } catch (RemoteException e) {
                Log.w("AccountAccessor", "Remote account accessor probably died");
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        return account;
    }

    public boolean equals(Object obj) {
        Account account = null;
        return this == obj ? true : !(obj instanceof zza) ? false : account.equals(account);
    }

    public Account getAccount() {
        int callingUid = Binder.getCallingUid();
        if (callingUid != this.zzajy) {
            if (GooglePlayServicesUtilLight.zzd(null, callingUid)) {
                this.zzajy = callingUid;
            } else {
                throw new SecurityException("Caller is not GooglePlayServices");
            }
        }
        return null;
    }
}
