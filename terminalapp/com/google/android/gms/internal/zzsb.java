package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultStore;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class zzsb {
    private static final com.google.android.gms.internal.zzqk.zza<?, ?>[] GM = new com.google.android.gms.internal.zzqk.zza[0];
    private final Map<com.google.android.gms.common.api.Api.zzc<?>, zze> Fj = new ArrayMap();
    final Set<com.google.android.gms.internal.zzqk.zza<?, ?>> GN = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap()));
    private final zzb GO = new zzb(this) {
        final /* synthetic */ zzsb GR;

        {
            this.GR = r1;
        }
    };
    private ResultStore GP;
    private zzc GQ = null;

    interface zzb {
    }

    static class zza implements DeathRecipient, zzb {
        private final WeakReference<com.google.android.gms.internal.zzqk.zza<?, ?>> GS;
        private final WeakReference<ResultStore> GT;
        private final WeakReference<IBinder> GU;

        private zza(com.google.android.gms.internal.zzqk.zza<?, ?> com_google_android_gms_internal_zzqk_zza___, ResultStore resultStore, IBinder iBinder) {
            this.GT = new WeakReference(resultStore);
            this.GS = new WeakReference(com_google_android_gms_internal_zzqk_zza___);
            this.GU = new WeakReference(iBinder);
        }

        private void zzatz() {
            com.google.android.gms.internal.zzqk.zza com_google_android_gms_internal_zzqk_zza = (com.google.android.gms.internal.zzqk.zza) this.GS.get();
            ResultStore resultStore = (ResultStore) this.GT.get();
            if (!(resultStore == null || com_google_android_gms_internal_zzqk_zza == null)) {
                resultStore.remove(com_google_android_gms_internal_zzqk_zza.zzasd().intValue());
            }
            IBinder iBinder = (IBinder) this.GU.get();
            if (this.GU != null) {
                iBinder.unlinkToDeath(this, 0);
            }
        }

        public void binderDied() {
            zzatz();
        }
    }

    interface zzc {
        final /* synthetic */ int Ga;
        final /* synthetic */ zzc Gb;

        zzc(zzc com_google_android_gms_internal_zzra_zzc, int i) {
            this.Gb = com_google_android_gms_internal_zzra_zzc;
            this.Ga = i;
        }

        void zzaug() {
            if (this.Gb.FT.isEmpty()) {
                this.Gb.zzh(this.Ga, false);
            }
        }
    }

    public zzsb(com.google.android.gms.common.api.Api.zzc<?> com_google_android_gms_common_api_Api_zzc_, zze com_google_android_gms_common_api_Api_zze) {
        this.Fj.put(com_google_android_gms_common_api_Api_zzc_, com_google_android_gms_common_api_Api_zze);
    }

    private static void zza(com.google.android.gms.internal.zzqk.zza<?, ?> com_google_android_gms_internal_zzqk_zza___, ResultStore resultStore, IBinder iBinder) {
        if (com_google_android_gms_internal_zzqk_zza___.isReady()) {
            com_google_android_gms_internal_zzqk_zza___.zza(new zza(com_google_android_gms_internal_zzqk_zza___, resultStore, iBinder));
        } else if (iBinder == null || !iBinder.isBinderAlive()) {
            com_google_android_gms_internal_zzqk_zza___.zza(null);
            com_google_android_gms_internal_zzqk_zza___.cancel();
            resultStore.remove(com_google_android_gms_internal_zzqk_zza___.zzasd().intValue());
        } else {
            Object com_google_android_gms_internal_zzsb_zza = new zza(com_google_android_gms_internal_zzqk_zza___, resultStore, iBinder);
            com_google_android_gms_internal_zzqk_zza___.zza(com_google_android_gms_internal_zzsb_zza);
            try {
                iBinder.linkToDeath(com_google_android_gms_internal_zzsb_zza, 0);
            } catch (RemoteException e) {
                com_google_android_gms_internal_zzqk_zza___.cancel();
                resultStore.remove(com_google_android_gms_internal_zzqk_zza___.zzasd().intValue());
            }
        }
    }

    public void release() {
        for (com.google.android.gms.internal.zzqk.zza com_google_android_gms_internal_zzqk_zza : (com.google.android.gms.internal.zzqk.zza[]) this.GN.toArray(GM)) {
            com_google_android_gms_internal_zzqk_zza.zza(null);
            if (com_google_android_gms_internal_zzqk_zza.zzasd() != null) {
                com_google_android_gms_internal_zzqk_zza.zzask();
                zza(com_google_android_gms_internal_zzqk_zza, this.GP, ((zze) this.Fj.get(com_google_android_gms_internal_zzqk_zza.zzarn())).zzarq());
                this.GN.remove(com_google_android_gms_internal_zzqk_zza);
            } else if (com_google_android_gms_internal_zzqk_zza.zzaso()) {
                this.GN.remove(com_google_android_gms_internal_zzqk_zza);
            }
        }
    }

    public void zza(zzc com_google_android_gms_internal_zzsb_zzc) {
        if (this.GN.isEmpty()) {
            com_google_android_gms_internal_zzsb_zzc.zzaug();
        }
        this.GQ = com_google_android_gms_internal_zzsb_zzc;
    }

    public boolean zzauw() {
        for (com.google.android.gms.internal.zzqk.zza isReady : (com.google.android.gms.internal.zzqk.zza[]) this.GN.toArray(GM)) {
            if (!isReady.isReady()) {
                return true;
            }
        }
        return false;
    }

    <A extends com.google.android.gms.common.api.Api.zzb> void zzg(com.google.android.gms.internal.zzqk.zza<? extends Result, A> com_google_android_gms_internal_zzqk_zza__extends_com_google_android_gms_common_api_Result__A) {
        this.GN.add(com_google_android_gms_internal_zzqk_zza__extends_com_google_android_gms_common_api_Result__A);
        com_google_android_gms_internal_zzqk_zza__extends_com_google_android_gms_common_api_Result__A.zza(this.GO);
    }
}
