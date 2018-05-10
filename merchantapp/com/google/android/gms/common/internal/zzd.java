package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class zzd<T extends IInterface> {
    public static final String[] GOOGLE_PLUS_REQUIRED_FEATURES = new String[]{"service_esmobile", "service_googleme"};
    private final GoogleApiAvailabilityLight EA;
    private int IQ;
    private long IR;
    private long IS;
    private int IT;
    private long IU;
    private final zzm IV;
    private final Object IW = new Object();
    private zzu IX;
    private zzf IY;
    private T IZ;
    private final ArrayList<zze<?>> Ja = new ArrayList();
    private zzh Jb;
    private int Jc = 1;
    private final zzb Jd;
    private final zzc Je;
    private final int Jf;
    private final String Jg;
    protected AtomicInteger Jh = new AtomicInteger(0);
    private final Context mContext;
    final Handler mHandler;
    private final Looper zzaig;
    private final Object zzaiw = new Object();

    public abstract class zze<TListener> {
        final /* synthetic */ zzd Jj;
        private boolean Jk = false;
        private TListener mListener;

        public zze(zzd com_google_android_gms_common_internal_zzd, TListener tListener) {
            this.Jj = com_google_android_gms_common_internal_zzd;
            this.mListener = tListener;
        }

        public void unregister() {
            zzawg();
            synchronized (this.Jj.Ja) {
                this.Jj.Ja.remove(this);
            }
        }

        protected abstract void zzab(TListener tListener);

        public void zzawf() {
            synchronized (this) {
                Object obj = this.mListener;
                if (this.Jk) {
                    String valueOf = String.valueOf(this);
                    Log.w("GmsClient", new StringBuilder(String.valueOf(valueOf).length() + 47).append("Callback proxy ").append(valueOf).append(" being reused. This is not safe.").toString());
                }
            }
            if (obj != null) {
                try {
                    zzab(obj);
                } catch (RuntimeException e) {
                    throw e;
                }
            }
            synchronized (this) {
                this.Jk = true;
            }
            unregister();
        }

        public void zzawg() {
            synchronized (this) {
                this.mListener = null;
            }
        }
    }

    abstract class zza extends zze<Boolean> {
        public final Bundle Ji;
        final /* synthetic */ zzd Jj;
        public final int statusCode;

        protected zza(zzd com_google_android_gms_common_internal_zzd, int i, Bundle bundle) {
            this.Jj = com_google_android_gms_common_internal_zzd;
            super(com_google_android_gms_common_internal_zzd, Boolean.valueOf(true));
            this.statusCode = i;
            this.Ji = bundle;
        }

        protected /* synthetic */ void zzab(Object obj) {
            zzd((Boolean) obj);
        }

        protected abstract boolean zzawd();

        protected void zzd(Boolean bool) {
            PendingIntent pendingIntent = null;
            if (bool == null) {
                this.Jj.zzb(1, null);
                return;
            }
            switch (this.statusCode) {
                case 0:
                    if (!zzawd()) {
                        this.Jj.zzb(1, null);
                        zzl(new ConnectionResult(8, null));
                        return;
                    }
                    return;
                case 10:
                    this.Jj.zzb(1, null);
                    throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
                default:
                    this.Jj.zzb(1, null);
                    if (this.Ji != null) {
                        pendingIntent = (PendingIntent) this.Ji.getParcelable("pendingIntent");
                    }
                    zzl(new ConnectionResult(this.statusCode, pendingIntent));
                    return;
            }
        }

        protected abstract void zzl(ConnectionResult connectionResult);
    }

    public interface zzb {
        final /* synthetic */ ConnectionCallbacks Ke;

        zzb(ConnectionCallbacks connectionCallbacks) {
            this.Ke = connectionCallbacks;
        }

        void onConnected(Bundle bundle) {
            this.Ke.onConnected(bundle);
        }

        void onConnectionSuspended(int i) {
            this.Ke.onConnectionSuspended(i);
        }
    }

    public interface zzc {
        final /* synthetic */ OnConnectionFailedListener Kf;

        zzc(OnConnectionFailedListener onConnectionFailedListener) {
            this.Kf = onConnectionFailedListener;
        }

        void onConnectionFailed(ConnectionResult connectionResult) {
            this.Kf.onConnectionFailed(connectionResult);
        }
    }

    final class zzd extends Handler {
        final /* synthetic */ zzd Jj;

        public zzd(zzd com_google_android_gms_common_internal_zzd, Looper looper) {
            this.Jj = com_google_android_gms_common_internal_zzd;
            super(looper);
        }

        private void zza(Message message) {
            ((zze) message.obj).unregister();
        }

        private boolean zzb(Message message) {
            return message.what == 2 || message.what == 1 || message.what == 5;
        }

        public void handleMessage(Message message) {
            PendingIntent pendingIntent = null;
            if (this.Jj.Jh.get() != message.arg1) {
                if (zzb(message)) {
                    zza(message);
                }
            } else if ((message.what == 1 || message.what == 5) && !this.Jj.isConnecting()) {
                zza(message);
            } else if (message.what == 3) {
                if (message.obj instanceof PendingIntent) {
                    pendingIntent = (PendingIntent) message.obj;
                }
                ConnectionResult connectionResult = new ConnectionResult(message.arg2, pendingIntent);
                this.Jj.IY.zzh(connectionResult);
                this.Jj.onConnectionFailed(connectionResult);
            } else if (message.what == 4) {
                this.Jj.zzb(4, null);
                if (this.Jj.Jd != null) {
                    this.Jj.Jd.onConnectionSuspended(message.arg2);
                }
                this.Jj.onConnectionSuspended(message.arg2);
                this.Jj.zza(4, 1, null);
            } else if (message.what == 2 && !this.Jj.isConnected()) {
                zza(message);
            } else if (zzb(message)) {
                ((zze) message.obj).zzawf();
            } else {
                Log.wtf("GmsClient", "Don't know how to handle message: " + message.what, new Exception());
            }
        }
    }

    public interface zzf {
        void zzh(ConnectionResult connectionResult);
    }

    public static final class zzg extends com.google.android.gms.common.internal.zzt.zza {
        private zzd Jl;
        private final int Jm;

        public zzg(zzd com_google_android_gms_common_internal_zzd, int i) {
            this.Jl = com_google_android_gms_common_internal_zzd;
            this.Jm = i;
        }

        private void zzawh() {
            this.Jl = null;
        }

        public void zza(int i, Bundle bundle) {
            Log.wtf("GmsClient", "received deprecated onAccountValidationComplete callback, ignoring", new Exception());
        }

        public void zza(int i, IBinder iBinder, Bundle bundle) {
            zzab.zzb(this.Jl, (Object) "onPostInitComplete can be called only once per call to getRemoteService");
            this.Jl.zza(i, iBinder, bundle, this.Jm);
            zzawh();
        }
    }

    public final class zzh implements ServiceConnection {
        final /* synthetic */ zzd Jj;
        private final int Jm;

        public zzh(zzd com_google_android_gms_common_internal_zzd, int i) {
            this.Jj = com_google_android_gms_common_internal_zzd;
            this.Jm = i;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            zzab.zzb((Object) iBinder, (Object) "Expecting a valid IBinder");
            synchronized (this.Jj.IW) {
                this.Jj.IX = com.google.android.gms.common.internal.zzu.zza.zzha(iBinder);
            }
            this.Jj.zza(0, null, this.Jm);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            synchronized (this.Jj.IW) {
                this.Jj.IX = null;
            }
            this.Jj.mHandler.sendMessage(this.Jj.mHandler.obtainMessage(4, this.Jm, 1));
        }
    }

    public final class zzj extends zza {
        final /* synthetic */ zzd Jj;
        public final IBinder Jn;

        public zzj(zzd com_google_android_gms_common_internal_zzd, int i, IBinder iBinder, Bundle bundle) {
            this.Jj = com_google_android_gms_common_internal_zzd;
            super(com_google_android_gms_common_internal_zzd, i, bundle);
            this.Jn = iBinder;
        }

        protected boolean zzawd() {
            try {
                String interfaceDescriptor = this.Jn.getInterfaceDescriptor();
                if (this.Jj.zzrf().equals(interfaceDescriptor)) {
                    IInterface zzbc = this.Jj.zzbc(this.Jn);
                    if (zzbc == null || !this.Jj.zza(2, 3, zzbc)) {
                        return false;
                    }
                    Bundle zzapv = this.Jj.zzapv();
                    if (this.Jj.Jd != null) {
                        this.Jj.Jd.onConnected(zzapv);
                    }
                    return true;
                }
                String valueOf = String.valueOf(this.Jj.zzrf());
                Log.e("GmsClient", new StringBuilder((String.valueOf(valueOf).length() + 34) + String.valueOf(interfaceDescriptor).length()).append("service descriptor mismatch: ").append(valueOf).append(" vs. ").append(interfaceDescriptor).toString());
                return false;
            } catch (RemoteException e) {
                Log.w("GmsClient", "service probably died");
                return false;
            }
        }

        protected void zzl(ConnectionResult connectionResult) {
            if (this.Jj.Je != null) {
                this.Jj.Je.onConnectionFailed(connectionResult);
            }
            this.Jj.onConnectionFailed(connectionResult);
        }
    }

    public final class zzk extends zza {
        final /* synthetic */ zzd Jj;

        public zzk(zzd com_google_android_gms_common_internal_zzd, int i, Bundle bundle) {
            this.Jj = com_google_android_gms_common_internal_zzd;
            super(com_google_android_gms_common_internal_zzd, i, bundle);
        }

        protected boolean zzawd() {
            this.Jj.IY.zzh(ConnectionResult.Ca);
            return true;
        }

        protected void zzl(ConnectionResult connectionResult) {
            this.Jj.IY.zzh(connectionResult);
            this.Jj.onConnectionFailed(connectionResult);
        }
    }

    protected zzd(Context context, Looper looper, zzm com_google_android_gms_common_internal_zzm, GoogleApiAvailabilityLight googleApiAvailabilityLight, int i, zzb com_google_android_gms_common_internal_zzd_zzb, zzc com_google_android_gms_common_internal_zzd_zzc, String str) {
        this.mContext = (Context) zzab.zzb((Object) context, (Object) "Context must not be null");
        this.zzaig = (Looper) zzab.zzb((Object) looper, (Object) "Looper must not be null");
        this.IV = (zzm) zzab.zzb((Object) com_google_android_gms_common_internal_zzm, (Object) "Supervisor must not be null");
        this.EA = (GoogleApiAvailabilityLight) zzab.zzb((Object) googleApiAvailabilityLight, (Object) "API availability must not be null");
        this.mHandler = new zzd(this, looper);
        this.Jf = i;
        this.Jd = com_google_android_gms_common_internal_zzd_zzb;
        this.Je = com_google_android_gms_common_internal_zzd_zzc;
        this.Jg = str;
    }

    private boolean zza(int i, int i2, T t) {
        boolean z;
        synchronized (this.zzaiw) {
            if (this.Jc != i) {
                z = false;
            } else {
                zzb(i2, t);
                z = true;
            }
        }
        return z;
    }

    private void zzavv() {
        if (this.Jb != null) {
            String valueOf = String.valueOf(zzre());
            String valueOf2 = String.valueOf(zzavt());
            Log.e("GmsClient", new StringBuilder((String.valueOf(valueOf).length() + 70) + String.valueOf(valueOf2).length()).append("Calling connect() while still connected, missing disconnect() for ").append(valueOf).append(" on ").append(valueOf2).toString());
            this.IV.zzb(zzre(), zzavt(), this.Jb, zzavu());
            this.Jh.incrementAndGet();
        }
        this.Jb = new zzh(this, this.Jh.get());
        if (!this.IV.zza(zzre(), zzavt(), this.Jb, zzavu())) {
            valueOf = String.valueOf(zzre());
            valueOf2 = String.valueOf(zzavt());
            Log.e("GmsClient", new StringBuilder((String.valueOf(valueOf).length() + 34) + String.valueOf(valueOf2).length()).append("unable to connect to service: ").append(valueOf).append(" on ").append(valueOf2).toString());
            zza(16, null, this.Jh.get());
        }
    }

    private void zzavw() {
        if (this.Jb != null) {
            this.IV.zzb(zzre(), zzavt(), this.Jb, zzavu());
            this.Jb = null;
        }
    }

    private void zzb(int i, T t) {
        boolean z = true;
        if ((i == 3) != (t != null)) {
            z = false;
        }
        zzab.zzbo(z);
        synchronized (this.zzaiw) {
            this.Jc = i;
            this.IZ = t;
            switch (i) {
                case 1:
                    zzavw();
                    break;
                case 2:
                    zzavv();
                    break;
                case 3:
                    zza((IInterface) t);
                    break;
            }
        }
    }

    public void disconnect() {
        this.Jh.incrementAndGet();
        synchronized (this.Ja) {
            int size = this.Ja.size();
            for (int i = 0; i < size; i++) {
                ((zze) this.Ja.get(i)).zzawg();
            }
            this.Ja.clear();
        }
        synchronized (this.IW) {
            this.IX = null;
        }
        zzb(1, null);
    }

    public Account getAccount() {
        return null;
    }

    public final Context getContext() {
        return this.mContext;
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.zzaiw) {
            z = this.Jc == 3;
        }
        return z;
    }

    public boolean isConnecting() {
        boolean z;
        synchronized (this.zzaiw) {
            z = this.Jc == 2;
        }
        return z;
    }

    protected void onConnectionFailed(ConnectionResult connectionResult) {
        this.IT = connectionResult.getErrorCode();
        this.IU = System.currentTimeMillis();
    }

    protected void onConnectionSuspended(int i) {
        this.IQ = i;
        this.IR = System.currentTimeMillis();
    }

    protected void zza(int i, Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(5, i2, -1, new zzk(this, i, bundle)));
    }

    protected void zza(int i, IBinder iBinder, Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, i2, -1, new zzj(this, i, iBinder, bundle)));
    }

    protected void zza(T t) {
        this.IS = System.currentTimeMillis();
    }

    public void zza(zzf com_google_android_gms_common_internal_zzd_zzf) {
        this.IY = (zzf) zzab.zzb((Object) com_google_android_gms_common_internal_zzd_zzf, (Object) "Connection progress callbacks cannot be null.");
        zzb(2, null);
    }

    public void zza(zzq com_google_android_gms_common_internal_zzq, Set<Scope> set) {
        try {
            GetServiceRequest zzt = new GetServiceRequest(this.Jf).zzgr(this.mContext.getPackageName()).zzt(zzadn());
            if (set != null) {
                zzt.zzf(set);
            }
            if (zzaec()) {
                zzt.zzc(zzavy()).zzb(com_google_android_gms_common_internal_zzq);
            } else if (zzawb()) {
                zzt.zzc(getAccount());
            }
            synchronized (this.IW) {
                if (this.IX != null) {
                    this.IX.zza(new zzg(this, this.Jh.get()), zzt);
                } else {
                    Log.w("GmsClient", "mServiceBroker is null, client disconnected");
                }
            }
        } catch (DeadObjectException e) {
            Log.w("GmsClient", "service died");
            zzir(1);
        } catch (Throwable e2) {
            Log.w("GmsClient", "Remote exception occurred", e2);
        }
    }

    protected Bundle zzadn() {
        return new Bundle();
    }

    public boolean zzaec() {
        return false;
    }

    public Bundle zzapv() {
        return null;
    }

    public boolean zzarp() {
        return true;
    }

    public IBinder zzarq() {
        IBinder iBinder;
        synchronized (this.IW) {
            if (this.IX == null) {
                iBinder = null;
            } else {
                iBinder = this.IX.asBinder();
            }
        }
        return iBinder;
    }

    protected String zzavt() {
        return "com.google.android.gms";
    }

    protected final String zzavu() {
        return this.Jg == null ? this.mContext.getClass().getName() : this.Jg;
    }

    public final Account zzavy() {
        return getAccount() != null ? getAccount() : new Account("<<default account>>", "com.google");
    }

    public boolean zzawb() {
        return false;
    }

    protected abstract T zzbc(IBinder iBinder);

    public void zzir(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, this.Jh.get(), i));
    }

    protected abstract String zzre();

    protected abstract String zzrf();
}
