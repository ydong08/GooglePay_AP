package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.Process;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.Api.zzh;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzah;
import com.google.android.gms.common.internal.zzd.zzf;
import com.google.android.gms.common.internal.zzg;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class zzra implements Callback {
    private static zzra FH;
    private static final Object zzank = new Object();
    private final GoogleApiAvailability Dh;
    private long FG;
    private int FI;
    private final SparseArray<zzc<?>> FK;
    private final Map<zzqh<?>, zzc<?>> FL;
    private zzqp FM;
    private final Set<zzqh<?>> FN;
    private final ReferenceQueue<com.google.android.gms.common.api.zzc<?>> FO;
    private final SparseArray<zza> FP;
    private zzb FQ;
    private long Ff;
    private long Fg;
    private final Context mContext;
    private final Handler mHandler;

    final class zza extends PhantomReference<com.google.android.gms.common.api.zzc<?>> {
        private final int Dx;
        final /* synthetic */ zzra FR;

        public zza(zzra com_google_android_gms_internal_zzra, com.google.android.gms.common.api.zzc com_google_android_gms_common_api_zzc, int i, ReferenceQueue<com.google.android.gms.common.api.zzc<?>> referenceQueue) {
            this.FR = com_google_android_gms_internal_zzra;
            super(com_google_android_gms_common_api_zzc, referenceQueue);
            this.Dx = i;
        }

        public void zzatz() {
            this.FR.mHandler.sendMessage(this.FR.mHandler.obtainMessage(2, this.Dx, 2));
        }
    }

    static final class zzb extends Thread {
        private final ReferenceQueue<com.google.android.gms.common.api.zzc<?>> FO;
        private final SparseArray<zza> FP;
        private final AtomicBoolean FS = new AtomicBoolean();

        public zzb(ReferenceQueue<com.google.android.gms.common.api.zzc<?>> referenceQueue, SparseArray<zza> sparseArray) {
            super("GoogleApiCleanup");
            this.FO = referenceQueue;
            this.FP = sparseArray;
        }

        public void run() {
            this.FS.set(true);
            Process.setThreadPriority(10);
            while (this.FS.get()) {
                try {
                    zza com_google_android_gms_internal_zzra_zza = (zza) this.FO.remove();
                    this.FP.remove(com_google_android_gms_internal_zzra_zza.Dx);
                    com_google_android_gms_internal_zzra_zza.zzatz();
                } catch (InterruptedException e) {
                } finally {
                    this.FS.set(false);
                }
            }
        }
    }

    class zzc<O extends ApiOptions> implements ConnectionCallbacks, OnConnectionFailedListener {
        private final zzqh<O> CP;
        final /* synthetic */ zzra FR;
        private final Queue<zzqg> FT = new LinkedList();
        private final zze FU;
        private final com.google.android.gms.common.api.Api.zzb FV;
        private final SparseArray<zzsb> FW = new SparseArray();
        private final Set<zzqj> FX = new HashSet();
        private final SparseArray<Map<Object, com.google.android.gms.internal.zzqk.zza>> FY = new SparseArray();
        private ConnectionResult FZ = null;
        private boolean Fe;

        public zzc(zzra com_google_android_gms_internal_zzra, com.google.android.gms.common.api.zzc<O> com_google_android_gms_common_api_zzc_O) {
            this.FR = com_google_android_gms_internal_zzra;
            this.FU = zzc((com.google.android.gms.common.api.zzc) com_google_android_gms_common_api_zzc_O);
            if (this.FU instanceof zzah) {
                this.FV = ((zzah) this.FU).zzaxn();
            } else {
                this.FV = this.FU;
            }
            this.CP = com_google_android_gms_common_api_zzc_O.zzarw();
        }

        private void connect() {
            if (!this.FU.isConnected() && !this.FU.isConnecting()) {
                if (this.FU.zzarp() && this.FR.FI != 0) {
                    this.FR.FI = this.FR.Dh.isGooglePlayServicesAvailable(this.FR.mContext);
                    if (this.FR.FI != 0) {
                        onConnectionFailed(new ConnectionResult(this.FR.FI, null));
                        return;
                    }
                }
                this.FU.zza(new zzd(this.FR, this.FU, this.CP));
            }
        }

        private void resume() {
            if (this.Fe) {
                connect();
            }
        }

        private void zzaj(Status status) {
            for (zzqg zzaf : this.FT) {
                zzaf.zzaf(status);
            }
            this.FT.clear();
        }

        private void zzatn() {
            if (this.Fe) {
                zzaud();
                zzaj(this.FR.Dh.isGooglePlayServicesAvailable(this.FR.mContext) == 18 ? new Status(8, "Connection timed out while waiting for Google Play services update to complete.") : new Status(8, "API failed to connect while resuming due to an unknown error."));
                this.FU.disconnect();
            }
        }

        private void zzaud() {
            if (this.Fe) {
                this.FR.mHandler.removeMessages(9, this.CP);
                this.FR.mHandler.removeMessages(8, this.CP);
                this.Fe = false;
            }
        }

        private void zzaue() {
            this.FR.mHandler.removeMessages(10, this.CP);
            this.FR.mHandler.sendMessageDelayed(this.FR.mHandler.obtainMessage(10, this.CP), this.FR.FG);
        }

        private void zzauf() {
            if (this.FU.isConnected() && this.FY.size() == 0) {
                for (int i = 0; i < this.FW.size(); i++) {
                    if (((zzsb) this.FW.get(this.FW.keyAt(i))).zzauw()) {
                        zzaue();
                        return;
                    }
                }
                this.FU.disconnect();
            }
        }

        private zze zzc(com.google.android.gms.common.api.zzc com_google_android_gms_common_api_zzc) {
            Api zzaru = com_google_android_gms_common_api_zzc.zzaru();
            if (!zzaru.zzaro()) {
                return com_google_android_gms_common_api_zzc.zzaru().zzarl().zza(com_google_android_gms_common_api_zzc.getApplicationContext(), this.FR.mHandler.getLooper(), zzg.zzcf(com_google_android_gms_common_api_zzc.getApplicationContext()), com_google_android_gms_common_api_zzc.zzarv(), this, this);
            }
            zzh zzarm = zzaru.zzarm();
            return new zzah(com_google_android_gms_common_api_zzc.getApplicationContext(), this.FR.mHandler.getLooper(), zzarm.zzarr(), this, this, zzg.zzcf(com_google_android_gms_common_api_zzc.getApplicationContext()), zzarm.zzu(com_google_android_gms_common_api_zzc.zzarv()));
        }

        private void zzc(zzqg com_google_android_gms_internal_zzqg) {
            com_google_android_gms_internal_zzqg.zza(this.FW);
            Map map;
            if (com_google_android_gms_internal_zzqg.bH == 3) {
                try {
                    Map map2;
                    map = (Map) this.FY.get(com_google_android_gms_internal_zzqg.Dx);
                    if (map == null) {
                        ArrayMap arrayMap = new ArrayMap(1);
                        this.FY.put(com_google_android_gms_internal_zzqg.Dx, arrayMap);
                        map2 = arrayMap;
                    } else {
                        map2 = map;
                    }
                    com.google.android.gms.internal.zzqk.zza com_google_android_gms_internal_zzqk_zza = ((com.google.android.gms.internal.zzqg.zza) com_google_android_gms_internal_zzqg).Dy;
                    map2.put(((zzrl) com_google_android_gms_internal_zzqk_zza).zzaup(), com_google_android_gms_internal_zzqk_zza);
                } catch (ClassCastException e) {
                    throw new IllegalStateException("Listener registration methods must implement ListenerApiMethod");
                }
            } else if (com_google_android_gms_internal_zzqg.bH == 4) {
                try {
                    map = (Map) this.FY.get(com_google_android_gms_internal_zzqg.Dx);
                    zzrl com_google_android_gms_internal_zzrl = (zzrl) ((com.google.android.gms.internal.zzqg.zza) com_google_android_gms_internal_zzqg).Dy;
                    if (map != null) {
                        map.remove(com_google_android_gms_internal_zzrl.zzaup());
                    } else {
                        Log.w("GoogleApiManager", "Received call to unregister a listener without a matching registration call.");
                    }
                } catch (ClassCastException e2) {
                    throw new IllegalStateException("Listener unregistration methods must implement ListenerApiMethod");
                }
            }
            try {
                com_google_android_gms_internal_zzqg.zzb(this.FV);
            } catch (DeadObjectException e3) {
                this.FU.disconnect();
                onConnectionSuspended(1);
            }
        }

        private void zzj(ConnectionResult connectionResult) {
            for (zzqj zza : this.FX) {
                zza.zza(this.CP, connectionResult);
            }
            this.FX.clear();
        }

        boolean isConnected() {
            return this.FU.isConnected();
        }

        public void onConnected(Bundle bundle) {
            zzaub();
            zzj(ConnectionResult.Ca);
            zzaud();
            for (int i = 0; i < this.FY.size(); i++) {
                for (com.google.android.gms.internal.zzqk.zza zzb : ((Map) this.FY.get(this.FY.keyAt(i))).values()) {
                    try {
                        zzb.zzb(this.FV);
                    } catch (DeadObjectException e) {
                        this.FU.disconnect();
                        onConnectionSuspended(1);
                    }
                }
            }
            zzaua();
            zzaue();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onConnectionFailed(com.google.android.gms.common.ConnectionResult r6) {
            /*
            r5 = this;
            r5.zzaub();
            r0 = r5.FR;
            r1 = -1;
            r0.FI = r1;
            r5.zzj(r6);
            r0 = r5.FW;
            r1 = 0;
            r0 = r0.keyAt(r1);
            r1 = r5.FT;
            r1 = r1.isEmpty();
            if (r1 == 0) goto L_0x001e;
        L_0x001b:
            r5.FZ = r6;
        L_0x001d:
            return;
        L_0x001e:
            r1 = com.google.android.gms.internal.zzra.zzank;
            monitor-enter(r1);
            r2 = r5.FR;	 Catch:{ all -> 0x0044 }
            r2 = r2.FM;	 Catch:{ all -> 0x0044 }
            if (r2 == 0) goto L_0x0047;
        L_0x002b:
            r2 = r5.FR;	 Catch:{ all -> 0x0044 }
            r2 = r2.FN;	 Catch:{ all -> 0x0044 }
            r3 = r5.CP;	 Catch:{ all -> 0x0044 }
            r2 = r2.contains(r3);	 Catch:{ all -> 0x0044 }
            if (r2 == 0) goto L_0x0047;
        L_0x0039:
            r2 = r5.FR;	 Catch:{ all -> 0x0044 }
            r2 = r2.FM;	 Catch:{ all -> 0x0044 }
            r2.zzb(r6, r0);	 Catch:{ all -> 0x0044 }
            monitor-exit(r1);	 Catch:{ all -> 0x0044 }
            goto L_0x001d;
        L_0x0044:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0044 }
            throw r0;
        L_0x0047:
            monitor-exit(r1);	 Catch:{ all -> 0x0044 }
            r1 = r5.FR;
            r0 = r1.zzc(r6, r0);
            if (r0 != 0) goto L_0x001d;
        L_0x0050:
            r0 = r6.getErrorCode();
            r1 = 18;
            if (r0 != r1) goto L_0x005b;
        L_0x0058:
            r0 = 1;
            r5.Fe = r0;
        L_0x005b:
            r0 = r5.Fe;
            if (r0 == 0) goto L_0x007d;
        L_0x005f:
            r0 = r5.FR;
            r0 = r0.mHandler;
            r1 = r5.FR;
            r1 = r1.mHandler;
            r2 = 8;
            r3 = r5.CP;
            r1 = android.os.Message.obtain(r1, r2, r3);
            r2 = r5.FR;
            r2 = r2.Fg;
            r0.sendMessageDelayed(r1, r2);
            goto L_0x001d;
        L_0x007d:
            r0 = new com.google.android.gms.common.api.Status;
            r1 = 17;
            r2 = r5.CP;
            r2 = r2.zzasg();
            r2 = java.lang.String.valueOf(r2);
            r3 = new java.lang.StringBuilder;
            r4 = java.lang.String.valueOf(r2);
            r4 = r4.length();
            r4 = r4 + 38;
            r3.<init>(r4);
            r4 = "API: ";
            r3 = r3.append(r4);
            r2 = r3.append(r2);
            r3 = " is not available on this device.";
            r2 = r2.append(r3);
            r2 = r2.toString();
            r0.<init>(r1, r2);
            r5.zzaj(r0);
            goto L_0x001d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzra.zzc.onConnectionFailed(com.google.android.gms.common.ConnectionResult):void");
        }

        public void onConnectionSuspended(int i) {
            zzaub();
            this.Fe = true;
            this.FR.mHandler.sendMessageDelayed(Message.obtain(this.FR.mHandler, 8, this.CP), this.FR.Fg);
            this.FR.mHandler.sendMessageDelayed(Message.obtain(this.FR.mHandler, 9, this.CP), this.FR.Ff);
            this.FR.FI = -1;
        }

        public void zzaua() {
            while (this.FU.isConnected() && !this.FT.isEmpty()) {
                zzc((zzqg) this.FT.remove());
            }
        }

        public void zzaub() {
            this.FZ = null;
        }

        ConnectionResult zzauc() {
            return this.FZ;
        }

        public void zzb(zzqg com_google_android_gms_internal_zzqg) {
            if (this.FU.isConnected()) {
                zzc(com_google_android_gms_internal_zzqg);
                zzaue();
                return;
            }
            this.FT.add(com_google_android_gms_internal_zzqg);
            if (this.FZ == null || !this.FZ.hasResolution()) {
                connect();
            } else {
                onConnectionFailed(this.FZ);
            }
        }

        public void zzb(zzqj com_google_android_gms_internal_zzqj) {
            this.FX.add(com_google_android_gms_internal_zzqj);
        }

        public void zzh(int i, boolean z) {
            Iterator it = this.FT.iterator();
            while (it.hasNext()) {
                zzqg com_google_android_gms_internal_zzqg = (zzqg) it.next();
                if (com_google_android_gms_internal_zzqg.Dx == i && com_google_android_gms_internal_zzqg.bH != 1 && com_google_android_gms_internal_zzqg.cancel()) {
                    it.remove();
                }
            }
            ((zzsb) this.FW.get(i)).release();
            this.FY.delete(i);
            if (!z) {
                this.FW.remove(i);
                this.FR.FP.remove(i);
                if (this.FW.size() == 0 && this.FT.isEmpty()) {
                    zzaud();
                    this.FU.disconnect();
                    this.FR.FL.remove(this.CP);
                    synchronized (zzra.zzank) {
                        this.FR.FN.remove(this.CP);
                    }
                }
            }
        }

        public void zzic(int i) {
            this.FW.put(i, new zzsb(this.CP.zzarn(), this.FU));
        }

        public void zzid(int i) {
            ((zzsb) this.FW.get(i)).zza(new zzc(this, i));
        }
    }

    class zzd implements zzf {
        private final zzqh<?> CP;
        final /* synthetic */ zzra FR;
        private final zze FU;

        public zzd(zzra com_google_android_gms_internal_zzra, zze com_google_android_gms_common_api_Api_zze, zzqh<?> com_google_android_gms_internal_zzqh_) {
            this.FR = com_google_android_gms_internal_zzra;
            this.FU = com_google_android_gms_common_api_Api_zze;
            this.CP = com_google_android_gms_internal_zzqh_;
        }

        public void zzh(ConnectionResult connectionResult) {
            if (connectionResult.isSuccess()) {
                this.FU.zza(null, Collections.emptySet());
            } else {
                ((zzc) this.FR.FL.get(this.CP)).onConnectionFailed(connectionResult);
            }
        }
    }

    private void zza(com.google.android.gms.common.api.zzc<?> com_google_android_gms_common_api_zzc_, int i) {
        zzqh zzarw = com_google_android_gms_common_api_zzc_.zzarw();
        if (!this.FL.containsKey(zzarw)) {
            this.FL.put(zzarw, new zzc(this, com_google_android_gms_common_api_zzc_));
        }
        zzc com_google_android_gms_internal_zzra_zzc = (zzc) this.FL.get(zzarw);
        com_google_android_gms_internal_zzra_zzc.zzic(i);
        this.FK.put(i, com_google_android_gms_internal_zzra_zzc);
        com_google_android_gms_internal_zzra_zzc.connect();
        this.FP.put(i, new zza(this, com_google_android_gms_common_api_zzc_, i, this.FO));
        if (this.FQ == null || !this.FQ.FS.get()) {
            this.FQ = new zzb(this.FO, this.FP);
            this.FQ.start();
        }
    }

    private void zza(zzqg com_google_android_gms_internal_zzqg) {
        ((zzc) this.FK.get(com_google_android_gms_internal_zzqg.Dx)).zzb(com_google_android_gms_internal_zzqg);
    }

    public static zzra zzatw() {
        zzra com_google_android_gms_internal_zzra;
        synchronized (zzank) {
            com_google_android_gms_internal_zzra = FH;
        }
        return com_google_android_gms_internal_zzra;
    }

    private void zzatx() {
        for (zzc com_google_android_gms_internal_zzra_zzc : this.FL.values()) {
            com_google_android_gms_internal_zzra_zzc.zzaub();
            com_google_android_gms_internal_zzra_zzc.connect();
        }
    }

    private void zzg(int i, boolean z) {
        zzc com_google_android_gms_internal_zzra_zzc = (zzc) this.FK.get(i);
        if (com_google_android_gms_internal_zzra_zzc != null) {
            if (!z) {
                this.FK.delete(i);
            }
            com_google_android_gms_internal_zzra_zzc.zzh(i, z);
            return;
        }
        Log.wtf("GoogleApiManager", "onRelease received for unknown instance: " + i, new Exception());
    }

    private void zzib(int i) {
        zzc com_google_android_gms_internal_zzra_zzc = (zzc) this.FK.get(i);
        if (com_google_android_gms_internal_zzra_zzc != null) {
            this.FK.delete(i);
            com_google_android_gms_internal_zzra_zzc.zzid(i);
            return;
        }
        Log.wtf("GoogleApiManager", "onCleanupLeakInternal received for unknown instance: " + i, new Exception());
    }

    public boolean handleMessage(Message message) {
        boolean z = false;
        switch (message.what) {
            case 1:
                zza((zzqj) message.obj);
                break;
            case 2:
                zzib(message.arg1);
                break;
            case 3:
                zzatx();
                break;
            case 4:
                zza((zzqg) message.obj);
                break;
            case 5:
                if (this.FK.get(message.arg1) != null) {
                    ((zzc) this.FK.get(message.arg1)).zzaj(new Status(17, "Error resolution was canceled by the user."));
                    break;
                }
                break;
            case 6:
                zza((com.google.android.gms.common.api.zzc) message.obj, message.arg1);
                break;
            case 7:
                int i = message.arg1;
                if (message.arg2 == 1) {
                    z = true;
                }
                zzg(i, z);
                break;
            case 8:
                if (this.FL.containsKey(message.obj)) {
                    ((zzc) this.FL.get(message.obj)).resume();
                    break;
                }
                break;
            case 9:
                if (this.FL.containsKey(message.obj)) {
                    ((zzc) this.FL.get(message.obj)).zzatn();
                    break;
                }
                break;
            case 10:
                if (this.FL.containsKey(message.obj)) {
                    ((zzc) this.FL.get(message.obj)).zzauf();
                    break;
                }
                break;
            default:
                Log.w("GoogleApiManager", "Unknown message id: " + message.what);
                return false;
        }
        return true;
    }

    public void zza(ConnectionResult connectionResult, int i) {
        if (!zzc(connectionResult, i)) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(5, i, 0));
        }
    }

    public void zza(zzqj com_google_android_gms_internal_zzqj) {
        for (zzqh com_google_android_gms_internal_zzqh : com_google_android_gms_internal_zzqj.zzasj()) {
            zzc com_google_android_gms_internal_zzra_zzc = (zzc) this.FL.get(com_google_android_gms_internal_zzqh);
            if (com_google_android_gms_internal_zzra_zzc == null) {
                com_google_android_gms_internal_zzqj.cancel();
                return;
            } else if (com_google_android_gms_internal_zzra_zzc.isConnected()) {
                com_google_android_gms_internal_zzqj.zza(com_google_android_gms_internal_zzqh, ConnectionResult.Ca);
            } else if (com_google_android_gms_internal_zzra_zzc.zzauc() != null) {
                com_google_android_gms_internal_zzqj.zza(com_google_android_gms_internal_zzqh, com_google_android_gms_internal_zzra_zzc.zzauc());
            } else {
                com_google_android_gms_internal_zzra_zzc.zzb(com_google_android_gms_internal_zzqj);
            }
        }
    }

    public void zza(zzqp com_google_android_gms_internal_zzqp) {
        synchronized (zzank) {
            if (com_google_android_gms_internal_zzqp == this.FM) {
                this.FM = null;
                this.FN.clear();
            }
        }
    }

    public void zzash() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3));
    }

    boolean zzc(ConnectionResult connectionResult, int i) {
        if (!connectionResult.hasResolution() && !this.Dh.isUserResolvableError(connectionResult.getErrorCode())) {
            return false;
        }
        this.Dh.zza(this.mContext, connectionResult, i);
        return true;
    }

    public void zzf(int i, boolean z) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(7, i, z ? 1 : 2));
    }
}
