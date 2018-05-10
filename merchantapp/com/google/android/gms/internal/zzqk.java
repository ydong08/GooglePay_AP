package com.google.android.gms.internal;

import android.os.DeadObjectException;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzab;
import java.util.concurrent.atomic.AtomicReference;

public class zzqk {

    public static abstract class zza<R extends Result, A extends zzb> extends zzqm<R> {
        private final zzc<A> DI;
        private AtomicReference<zzb> DJ;

        public void zza(zzb com_google_android_gms_internal_zzsb_zzb) {
            this.DJ.set(com_google_android_gms_internal_zzsb_zzb);
        }

        public final void zzah(Status status) {
            zzab.zzb(!status.isSuccess(), (Object) "Failed result must not be success");
            zzc(zzb(status));
        }

        public final zzc<A> zzarn() {
            return this.DI;
        }

        public void zzask() {
            setResultCallback(null);
        }

        public final void zzb(A a) throws DeadObjectException {
        }
    }
}
