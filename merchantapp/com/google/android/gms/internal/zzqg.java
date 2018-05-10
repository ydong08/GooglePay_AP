package com.google.android.gms.internal;

import android.os.DeadObjectException;
import android.util.SparseArray;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

public abstract class zzqg {
    public final int Dx;
    public final int bH;

    public static final class zza extends zzqg {
        public final com.google.android.gms.internal.zzqk.zza<? extends Result, zzb> Dy;

        public boolean cancel() {
            return this.Dy.zzaso();
        }

        public void zza(SparseArray<zzsb> sparseArray) {
            zzsb com_google_android_gms_internal_zzsb = (zzsb) sparseArray.get(this.Dx);
            if (com_google_android_gms_internal_zzsb != null) {
                com_google_android_gms_internal_zzsb.zzg(this.Dy);
            }
        }

        public void zzaf(Status status) {
            this.Dy.zzah(status);
        }

        public void zzb(zzb com_google_android_gms_common_api_Api_zzb) throws DeadObjectException {
            this.Dy.zzb(com_google_android_gms_common_api_Api_zzb);
        }
    }

    public boolean cancel() {
        return true;
    }

    public void zza(SparseArray<zzsb> sparseArray) {
    }

    public abstract void zzaf(Status status);

    public abstract void zzb(zzb com_google_android_gms_common_api_Api_zzb) throws DeadObjectException;
}
