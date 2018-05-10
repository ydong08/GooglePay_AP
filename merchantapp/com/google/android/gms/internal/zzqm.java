package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzr;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

public abstract class zzqm<R extends Result> extends PendingResult<R> {
    static final ThreadLocal<Boolean> DR = new ThreadLocal<Boolean>() {
        protected /* synthetic */ Object initialValue() {
            return zzasr();
        }

        protected Boolean zzasr() {
            return Boolean.valueOf(false);
        }
    };
    private final Object DS = new Object();
    protected final zza<R> DT = new zza(Looper.getMainLooper());
    protected final WeakReference<GoogleApiClient> DU = new WeakReference(null);
    private final ArrayList<Object> DV = new ArrayList();
    private ResultCallback<? super R> DW;
    private zzb DX;
    private volatile boolean DY;
    private boolean DZ;
    private R Dm;
    private zzr Ea;
    private Integer Eb;
    private volatile zzsa<R> Ec;
    private boolean Ed = false;
    private boolean zzaj;
    private final CountDownLatch zzalx = new CountDownLatch(1);

    public static class zza<R extends Result> extends Handler {
        public zza() {
            this(Looper.getMainLooper());
        }

        public zza(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Pair pair = (Pair) message.obj;
                    zzb((ResultCallback) pair.first, (Result) pair.second);
                    return;
                case 2:
                    ((zzqm) message.obj).zzai(Status.Dt);
                    return;
                default:
                    Log.wtf("BasePendingResult", "Don't know how to handle message: " + message.what, new Exception());
                    return;
            }
        }

        public void zza(ResultCallback<? super R> resultCallback, R r) {
            sendMessage(obtainMessage(1, new Pair(resultCallback, r)));
        }

        public void zzass() {
            removeMessages(2);
        }

        protected void zzb(ResultCallback<? super R> resultCallback, R r) {
            try {
                resultCallback.onResult(r);
            } catch (RuntimeException e) {
                zzqm.zze(r);
                throw e;
            }
        }
    }

    final class zzb {
        final /* synthetic */ zzqm Ee;

        private zzb(zzqm com_google_android_gms_internal_zzqm) {
            this.Ee = com_google_android_gms_internal_zzqm;
        }

        protected void finalize() throws Throwable {
            zzqm.zze(this.Ee.Dm);
            super.finalize();
        }
    }

    @Deprecated
    zzqm() {
    }

    private R get() {
        R r;
        boolean z = true;
        synchronized (this.DS) {
            if (this.DY) {
                z = false;
            }
            zzab.zza(z, "Result has already been consumed.");
            zzab.zza(isReady(), "Result is not ready.");
            r = this.Dm;
            this.Dm = null;
            this.DW = null;
            this.DY = true;
        }
        zzasl();
        return r;
    }

    private void zzd(R r) {
        this.Dm = r;
        this.Ea = null;
        this.zzalx.countDown();
        this.Dm.getStatus();
        if (this.zzaj) {
            this.DW = null;
        } else if (this.DW != null) {
            this.DT.zzass();
            this.DT.zza(this.DW, get());
        } else if (this.Dm instanceof Releasable) {
            this.DX = new zzb();
        }
        Iterator it = this.DV.iterator();
        while (it.hasNext()) {
            it.next();
        }
        this.DV.clear();
    }

    public static void zze(Result result) {
        if (!(result instanceof Releasable)) {
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cancel() {
        /*
        r2 = this;
        r1 = r2.DS;
        monitor-enter(r1);
        r0 = r2.zzaj;	 Catch:{ all -> 0x0023 }
        if (r0 != 0) goto L_0x000b;
    L_0x0007:
        r0 = r2.DY;	 Catch:{ all -> 0x0023 }
        if (r0 == 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
    L_0x000c:
        return;
    L_0x000d:
        r0 = 0;
        if (r0 == 0) goto L_0x0010;
    L_0x0010:
        r0 = r2.Dm;	 Catch:{ all -> 0x0023 }
        zze(r0);	 Catch:{ all -> 0x0023 }
        r0 = 1;
        r2.zzaj = r0;	 Catch:{ all -> 0x0023 }
        r0 = com.google.android.gms.common.api.Status.Du;	 Catch:{ all -> 0x0023 }
        r0 = r2.zzb(r0);	 Catch:{ all -> 0x0023 }
        r2.zzd(r0);	 Catch:{ all -> 0x0023 }
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
        goto L_0x000c;
    L_0x0023:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzqm.cancel():void");
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this.DS) {
            z = this.zzaj;
        }
        return z;
    }

    public final boolean isReady() {
        return this.zzalx.getCount() == 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setResultCallback(com.google.android.gms.common.api.ResultCallback<? super R> r6) {
        /*
        r5 = this;
        r0 = 1;
        r1 = 0;
        r3 = r5.DS;
        monitor-enter(r3);
        if (r6 != 0) goto L_0x000c;
    L_0x0007:
        r0 = 0;
        r5.DW = r0;	 Catch:{ all -> 0x0027 }
        monitor-exit(r3);	 Catch:{ all -> 0x0027 }
    L_0x000b:
        return;
    L_0x000c:
        r2 = r5.DY;	 Catch:{ all -> 0x0027 }
        if (r2 != 0) goto L_0x002a;
    L_0x0010:
        r2 = r0;
    L_0x0011:
        r4 = "Result has already been consumed.";
        com.google.android.gms.common.internal.zzab.zza(r2, r4);	 Catch:{ all -> 0x0027 }
        r2 = r5.Ec;	 Catch:{ all -> 0x0027 }
        if (r2 != 0) goto L_0x002c;
    L_0x001a:
        r1 = "Cannot set callbacks if then() has been called.";
        com.google.android.gms.common.internal.zzab.zza(r0, r1);	 Catch:{ all -> 0x0027 }
        r0 = r5.isCanceled();	 Catch:{ all -> 0x0027 }
        if (r0 == 0) goto L_0x002e;
    L_0x0025:
        monitor-exit(r3);	 Catch:{ all -> 0x0027 }
        goto L_0x000b;
    L_0x0027:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0027 }
        throw r0;
    L_0x002a:
        r2 = r1;
        goto L_0x0011;
    L_0x002c:
        r0 = r1;
        goto L_0x001a;
    L_0x002e:
        r0 = r5.isReady();	 Catch:{ all -> 0x0027 }
        if (r0 == 0) goto L_0x003f;
    L_0x0034:
        r0 = r5.DT;	 Catch:{ all -> 0x0027 }
        r1 = r5.get();	 Catch:{ all -> 0x0027 }
        r0.zza(r6, r1);	 Catch:{ all -> 0x0027 }
    L_0x003d:
        monitor-exit(r3);	 Catch:{ all -> 0x0027 }
        goto L_0x000b;
    L_0x003f:
        r5.DW = r6;	 Catch:{ all -> 0x0027 }
        goto L_0x003d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzqm.setResultCallback(com.google.android.gms.common.api.ResultCallback):void");
    }

    public final void zzai(Status status) {
        synchronized (this.DS) {
            if (!isReady()) {
                zzc(zzb(status));
                this.DZ = true;
            }
        }
    }

    public Integer zzasd() {
        return this.Eb;
    }

    protected void zzasl() {
    }

    public boolean zzaso() {
        boolean isCanceled;
        synchronized (this.DS) {
            if (((GoogleApiClient) this.DU.get()) == null || !this.Ed) {
                cancel();
            }
            isCanceled = isCanceled();
        }
        return isCanceled;
    }

    boolean zzasq() {
        return false;
    }

    protected abstract R zzb(Status status);

    public final void zzc(R r) {
        boolean z = true;
        synchronized (this.DS) {
            if (this.DZ || this.zzaj || (isReady() && zzasq())) {
                zze(r);
                return;
            }
            zzab.zza(!isReady(), "Results have already been set");
            if (this.DY) {
                z = false;
            }
            zzab.zza(z, "Result has already been consumed");
            zzd(r);
        }
    }
}
