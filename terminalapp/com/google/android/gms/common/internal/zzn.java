package com.google.android.gms.common.internal;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

final class zzn extends zzm implements Callback {
    private final HashMap<zza, zzb> Kp = new HashMap();
    private final com.google.android.gms.common.stats.zzb Kq;
    private final long Kr;
    private final Handler mHandler;
    private final Context zzare;

    static final class zza {
        private final String Ks;
        private final ComponentName kK = null;
        private final String mAction;

        public zza(String str, String str2) {
            this.mAction = zzab.zzgx(str);
            this.Ks = zzab.zzgx(str2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof zza)) {
                return false;
            }
            zza com_google_android_gms_common_internal_zzn_zza = (zza) obj;
            return zzaa.equal(this.mAction, com_google_android_gms_common_internal_zzn_zza.mAction) && zzaa.equal(this.kK, com_google_android_gms_common_internal_zzn_zza.kK);
        }

        public int hashCode() {
            return zzaa.hashCode(this.mAction, this.kK);
        }

        public String toString() {
            return this.mAction == null ? this.kK.flattenToString() : this.mAction;
        }

        public Intent zzawy() {
            return this.mAction != null ? new Intent(this.mAction).setPackage(this.Ks) : new Intent().setComponent(this.kK);
        }
    }

    final class zzb {
        private final zza Kt = new zza(this);
        private final Set<ServiceConnection> Ku = new HashSet();
        private boolean Kv;
        private final zza Kw;
        final /* synthetic */ zzn Kx;
        private ComponentName kK;
        private int mState = 2;
        private IBinder zzaka;

        public class zza implements ServiceConnection {
            final /* synthetic */ zzb Ky;

            public zza(zzb com_google_android_gms_common_internal_zzn_zzb) {
                this.Ky = com_google_android_gms_common_internal_zzn_zzb;
            }

            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                synchronized (this.Ky.Kx.Kp) {
                    this.Ky.zzaka = iBinder;
                    this.Ky.kK = componentName;
                    for (ServiceConnection onServiceConnected : this.Ky.Ku) {
                        onServiceConnected.onServiceConnected(componentName, iBinder);
                    }
                    this.Ky.mState = 1;
                }
            }

            public void onServiceDisconnected(ComponentName componentName) {
                synchronized (this.Ky.Kx.Kp) {
                    this.Ky.zzaka = null;
                    this.Ky.kK = componentName;
                    for (ServiceConnection onServiceDisconnected : this.Ky.Ku) {
                        onServiceDisconnected.onServiceDisconnected(componentName);
                    }
                    this.Ky.mState = 2;
                }
            }
        }

        public zzb(zzn com_google_android_gms_common_internal_zzn, zza com_google_android_gms_common_internal_zzn_zza) {
            this.Kx = com_google_android_gms_common_internal_zzn;
            this.Kw = com_google_android_gms_common_internal_zzn_zza;
        }

        public IBinder getBinder() {
            return this.zzaka;
        }

        public ComponentName getComponentName() {
            return this.kK;
        }

        public int getState() {
            return this.mState;
        }

        public boolean isBound() {
            return this.Kv;
        }

        public void zza(ServiceConnection serviceConnection, String str) {
            this.Kx.Kq.zza(this.Kx.zzare, serviceConnection, str, this.Kw.zzawy());
            this.Ku.add(serviceConnection);
        }

        public boolean zza(ServiceConnection serviceConnection) {
            return this.Ku.contains(serviceConnection);
        }

        public boolean zzawz() {
            return this.Ku.isEmpty();
        }

        public void zzb(ServiceConnection serviceConnection, String str) {
            this.Kx.Kq.zzb(this.Kx.zzare, serviceConnection);
            this.Ku.remove(serviceConnection);
        }

        @TargetApi(14)
        public void zzgs(String str) {
            this.mState = 3;
            this.Kv = this.Kx.Kq.zza(this.Kx.zzare, str, this.Kw.zzawy(), this.Kt, 129);
            if (!this.Kv) {
                this.mState = 2;
                try {
                    this.Kx.Kq.zza(this.Kx.zzare, this.Kt);
                } catch (IllegalArgumentException e) {
                }
            }
        }

        public void zzgt(String str) {
            this.Kx.Kq.zza(this.Kx.zzare, this.Kt);
            this.Kv = false;
            this.mState = 2;
        }
    }

    zzn(Context context) {
        this.zzare = context.getApplicationContext();
        this.mHandler = new Handler(context.getMainLooper(), this);
        this.Kq = com.google.android.gms.common.stats.zzb.zzaym();
        this.Kr = 5000;
    }

    private boolean zza(zza com_google_android_gms_common_internal_zzn_zza, ServiceConnection serviceConnection, String str) {
        boolean isBound;
        zzab.zzb((Object) serviceConnection, (Object) "ServiceConnection must not be null");
        synchronized (this.Kp) {
            zzb com_google_android_gms_common_internal_zzn_zzb = (zzb) this.Kp.get(com_google_android_gms_common_internal_zzn_zza);
            if (com_google_android_gms_common_internal_zzn_zzb != null) {
                this.mHandler.removeMessages(0, com_google_android_gms_common_internal_zzn_zzb);
                if (!com_google_android_gms_common_internal_zzn_zzb.zza(serviceConnection)) {
                    com_google_android_gms_common_internal_zzn_zzb.zza(serviceConnection, str);
                    switch (com_google_android_gms_common_internal_zzn_zzb.getState()) {
                        case 1:
                            serviceConnection.onServiceConnected(com_google_android_gms_common_internal_zzn_zzb.getComponentName(), com_google_android_gms_common_internal_zzn_zzb.getBinder());
                            break;
                        case 2:
                            com_google_android_gms_common_internal_zzn_zzb.zzgs(str);
                            break;
                        default:
                            break;
                    }
                }
                String valueOf = String.valueOf(com_google_android_gms_common_internal_zzn_zza);
                throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 81).append("Trying to bind a GmsServiceConnection that was already connected before.  config=").append(valueOf).toString());
            }
            com_google_android_gms_common_internal_zzn_zzb = new zzb(this, com_google_android_gms_common_internal_zzn_zza);
            com_google_android_gms_common_internal_zzn_zzb.zza(serviceConnection, str);
            com_google_android_gms_common_internal_zzn_zzb.zzgs(str);
            this.Kp.put(com_google_android_gms_common_internal_zzn_zza, com_google_android_gms_common_internal_zzn_zzb);
            isBound = com_google_android_gms_common_internal_zzn_zzb.isBound();
        }
        return isBound;
    }

    private void zzb(zza com_google_android_gms_common_internal_zzn_zza, ServiceConnection serviceConnection, String str) {
        zzab.zzb((Object) serviceConnection, (Object) "ServiceConnection must not be null");
        synchronized (this.Kp) {
            zzb com_google_android_gms_common_internal_zzn_zzb = (zzb) this.Kp.get(com_google_android_gms_common_internal_zzn_zza);
            String valueOf;
            if (com_google_android_gms_common_internal_zzn_zzb == null) {
                valueOf = String.valueOf(com_google_android_gms_common_internal_zzn_zza);
                throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 50).append("Nonexistent connection status for service config: ").append(valueOf).toString());
            } else if (com_google_android_gms_common_internal_zzn_zzb.zza(serviceConnection)) {
                com_google_android_gms_common_internal_zzn_zzb.zzb(serviceConnection, str);
                if (com_google_android_gms_common_internal_zzn_zzb.zzawz()) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, com_google_android_gms_common_internal_zzn_zzb), this.Kr);
                }
            } else {
                valueOf = String.valueOf(com_google_android_gms_common_internal_zzn_zza);
                throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 76).append("Trying to unbind a GmsServiceConnection  that was not bound before.  config=").append(valueOf).toString());
            }
        }
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 0:
                zzb com_google_android_gms_common_internal_zzn_zzb = (zzb) message.obj;
                synchronized (this.Kp) {
                    if (com_google_android_gms_common_internal_zzn_zzb.zzawz()) {
                        if (com_google_android_gms_common_internal_zzn_zzb.isBound()) {
                            com_google_android_gms_common_internal_zzn_zzb.zzgt("GmsClientSupervisor");
                        }
                        this.Kp.remove(com_google_android_gms_common_internal_zzn_zzb.Kw);
                    }
                }
                return true;
            default:
                return false;
        }
    }

    public boolean zza(String str, String str2, ServiceConnection serviceConnection, String str3) {
        return zza(new zza(str, str2), serviceConnection, str3);
    }

    public void zzb(String str, String str2, ServiceConnection serviceConnection, String str3) {
        zzb(new zza(str, str2), serviceConnection, str3);
    }
}
