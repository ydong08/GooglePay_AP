package com.google.android.gms.common.api;

import android.content.Context;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.internal.zzqh;
import com.google.android.gms.internal.zzra;
import com.google.android.gms.internal.zzrn;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class zzc<O extends ApiOptions> {
    private final zzrn CN;
    private final O CO;
    private final zzqh<O> CP;
    private final zzra CQ;
    private final AtomicBoolean CS;
    private final AtomicInteger CT;
    private final Context mContext;
    private final int mId;
    private final Api<O> zM;

    public Context getApplicationContext() {
        return this.mContext;
    }

    public void release() {
        boolean z = true;
        if (!this.CS.getAndSet(true)) {
            this.CN.release();
            zzra com_google_android_gms_internal_zzra = this.CQ;
            int i = this.mId;
            if (this.CT.get() <= 0) {
                z = false;
            }
            com_google_android_gms_internal_zzra.zzf(i, z);
        }
    }

    public Api<O> zzaru() {
        return this.zM;
    }

    public O zzarv() {
        return this.CO;
    }

    public zzqh<O> zzarw() {
        return this.CP;
    }
}
