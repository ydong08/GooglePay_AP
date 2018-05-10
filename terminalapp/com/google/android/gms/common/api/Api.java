package com.google.android.gms.common.api;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzd.zzf;
import com.google.android.gms.common.internal.zzq;
import java.util.Set;

public final class Api<O extends ApiOptions> {
    private final zza<?, O> CA;
    private final zzh<?, O> CB = null;
    private final zzc CC$9HHMUR9FCTNMUPRCCKNM2RJ4E9NMIP1FCTMN6BR3DTMMQRRE5TGN0Q9F85O6I93QF9J3M___0;
    private final zzc CD$9HHMUR9FCTNMUPRCCKNM2RJ4E9NMIP1FCTMN6BR3DTMMQRRE5TGN0Q9F85O6I93QF9KJM___0;
    private final String mName;

    public interface ApiOptions {

        public interface NotRequiredOptions extends ApiOptions {
        }

        public interface HasOptions extends ApiOptions, NotRequiredOptions {
        }
    }

    public static abstract class zzd<T extends zzb, O> {
    }

    public static abstract class zza<T extends zze, O> extends zzd<T, O> {
        public abstract T zza(Context context, Looper looper, com.google.android.gms.common.internal.zzg com_google_android_gms_common_internal_zzg, O o, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener);
    }

    public interface zzb {
    }

    public static class zzc<C extends zzb> {
        public zzc(byte b) {
            this();
        }
    }

    public interface zze extends zzb {
        void disconnect();

        boolean isConnected();

        boolean isConnecting();

        void zza(zzf com_google_android_gms_common_internal_zzd_zzf);

        void zza(zzq com_google_android_gms_common_internal_zzq, Set<Scope> set);

        boolean zzarp();

        IBinder zzarq();
    }

    public interface zzg<T extends IInterface> extends zzb {
        T zzbc(IBinder iBinder);

        String zzre();

        String zzrf();
    }

    public static abstract class zzh<T extends zzg, O> extends zzd<T, O> {
        public abstract int zzarr();

        public abstract T zzu(O o);
    }

    public <C extends zze> Api(String str, zza<C, O> com_google_android_gms_common_api_Api_zza_C__O, zzc com_google_android_gms_common_api_Api_zzc) {
        zzab.zzb((Object) com_google_android_gms_common_api_Api_zza_C__O, (Object) "Cannot construct an Api with a null ClientBuilder");
        zzab.zzb((Object) com_google_android_gms_common_api_Api_zzc, (Object) "Cannot construct an Api with a null ClientKey");
        this.mName = str;
        this.CA = com_google_android_gms_common_api_Api_zza_C__O;
        this.CC$9HHMUR9FCTNMUPRCCKNM2RJ4E9NMIP1FCTMN6BR3DTMMQRRE5TGN0Q9F85O6I93QF9J3M___0 = com_google_android_gms_common_api_Api_zzc;
        this.CD$9HHMUR9FCTNMUPRCCKNM2RJ4E9NMIP1FCTMN6BR3DTMMQRRE5TGN0Q9F85O6I93QF9KJM___0 = null;
    }

    public String getName() {
        return this.mName;
    }

    public zza<?, O> zzarl() {
        zzab.zza(this.CA != null, "This API was constructed with a SimpleClientBuilder. Use getSimpleClientBuilder");
        return this.CA;
    }

    public zzh<?, O> zzarm() {
        zzab.zza(false, "This API was constructed with a ClientBuilder. Use getClientBuilder");
        return null;
    }

    public zzc<?> zzarn() {
        if (this.CC$9HHMUR9FCTNMUPRCCKNM2RJ4E9NMIP1FCTMN6BR3DTMMQRRE5TGN0Q9F85O6I93QF9J3M___0 != null) {
            return this.CC$9HHMUR9FCTNMUPRCCKNM2RJ4E9NMIP1FCTMN6BR3DTMMQRRE5TGN0Q9F85O6I93QF9J3M___0;
        }
        throw new IllegalStateException("This API was constructed with null client keys. This should not be possible.");
    }

    public boolean zzaro() {
        return false;
    }
}
