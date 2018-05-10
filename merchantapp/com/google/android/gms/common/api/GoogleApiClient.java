package com.google.android.gms.common.api;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.util.ArrayMap;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.internal.zzg.zza;
import com.google.android.gms.internal.zzaci;
import com.google.android.gms.internal.zzack;
import com.google.android.gms.internal.zzsa;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class GoogleApiClient {
    private static final Set<GoogleApiClient> CV = Collections.newSetFromMap(new WeakHashMap());

    public static final class Builder {
        private final Set<Scope> CX = new HashSet();
        private final Set<Scope> CY = new HashSet();
        private int CZ;
        private View Da;
        private String Db;
        private final Map<Api<?>, zza> Dc = new ArrayMap();
        private final Map<Api<?>, ApiOptions> Dd = new ArrayMap();
        private int Df = -1;
        private GoogleApiAvailability Dh = GoogleApiAvailability.getInstance();
        private Api.zza<? extends zze, zzack> Di = zzaci.cc;
        private final ArrayList<ConnectionCallbacks> Dj = new ArrayList();
        private final ArrayList<OnConnectionFailedListener> Dk = new ArrayList();
        private Account P;
        private String cp;
        private final Context mContext;
        private Looper zzaig;

        public Builder(Context context) {
            this.mContext = context;
            this.zzaig = context.getMainLooper();
            this.cp = context.getPackageName();
            this.Db = context.getClass().getName();
        }

        public zzg zzasb() {
            zzack com_google_android_gms_internal_zzack = zzack.bgB;
            if (this.Dd.containsKey(zzaci.API)) {
                com_google_android_gms_internal_zzack = (zzack) this.Dd.get(zzaci.API);
            }
            return new zzg(this.P, this.CX, this.Dc, this.CZ, this.Da, this.cp, this.Db, com_google_android_gms_internal_zzack);
        }
    }

    public interface ConnectionCallbacks {
        void onConnected(Bundle bundle);

        void onConnectionSuspended(int i);
    }

    public interface OnConnectionFailedListener {
        void onConnectionFailed(ConnectionResult connectionResult);
    }

    public void zza(zzsa com_google_android_gms_internal_zzsa) {
        throw new UnsupportedOperationException();
    }

    public void zzb(zzsa com_google_android_gms_internal_zzsa) {
        throw new UnsupportedOperationException();
    }
}
