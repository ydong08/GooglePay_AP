package com.google.android.gms.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.zzc;
import com.google.android.gms.common.util.zza;
import java.util.Iterator;

public class zzqp extends zzql {
    private zzra CQ;
    private final zza<zzc<?>> Eu;

    public void onStop() {
        super.onStop();
        Iterator it = this.Eu.iterator();
        while (it.hasNext()) {
            ((zzc) it.next()).release();
        }
        this.Eu.clear();
        this.CQ.zza(this);
    }

    protected void zza(ConnectionResult connectionResult, int i) {
        this.CQ.zza(connectionResult, i);
    }

    protected void zzash() {
        this.CQ.zzash();
    }
}
