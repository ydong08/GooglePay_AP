package com.google.android.gms.common.api;

import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.internal.zzqh;

public class zzb implements Result {
    private final ArrayMap<zzqh<?>, ConnectionResult> CF;
    private final Status cq;

    public zzb(Status status, ArrayMap<zzqh<?>, ConnectionResult> arrayMap) {
        this.cq = status;
        this.CF = arrayMap;
    }

    public Status getStatus() {
        return this.cq;
    }
}
