package com.google.android.gms.common.api;

import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.internal.zzqh;

public class zza extends zzb {
    private final ConnectionResult CE;

    public zza(Status status, ArrayMap<zzqh<?>, ConnectionResult> arrayMap) {
        super(status, arrayMap);
        this.CE = (ConnectionResult) arrayMap.get(arrayMap.keyAt(0));
    }
}
