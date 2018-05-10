package com.google.android.gms.internal;

import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.zza;
import com.google.android.gms.common.api.zzb;
import java.util.Set;

public final class zzqj extends zzqm<zzb> {
    private int DG;
    private boolean DH;

    private void zza(ConnectionResult connectionResult) {
        ArrayMap arrayMap = null;
        for (int i = 0; i < arrayMap.size(); i++) {
            zza((zzqh) arrayMap.keyAt(i), connectionResult);
        }
    }

    public void zza(zzqh<?> com_google_android_gms_internal_zzqh_, ConnectionResult connectionResult) {
        synchronized (null) {
            ArrayMap arrayMap = null;
            try {
                arrayMap.put(com_google_android_gms_internal_zzqh_, connectionResult);
                this.DG--;
                boolean isSuccess = connectionResult.isSuccess();
                if (!isSuccess) {
                    this.DH = isSuccess;
                }
                if (this.DG == 0) {
                    Status status = this.DH ? new Status(13) : Status.Dq;
                    arrayMap = null;
                    zzc(arrayMap.size() == 1 ? new zza(status, null) : new zzb(status, null));
                }
            } finally {
            }
        }
    }

    protected zzb zzag(Status status) {
        zzb com_google_android_gms_common_api_zzb;
        synchronized (null) {
            try {
                zza(new ConnectionResult(8));
                ArrayMap arrayMap = null;
                if (arrayMap.size() != 1) {
                    com_google_android_gms_common_api_zzb = new zzb(status, null);
                }
            } finally {
            }
        }
        return com_google_android_gms_common_api_zzb;
    }

    public Set<zzqh<?>> zzasj() {
        ArrayMap arrayMap = null;
        return arrayMap.keySet();
    }

    protected /* synthetic */ Result zzb(Status status) {
        return zzag(status);
    }
}
