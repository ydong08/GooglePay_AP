package com.google.android.gms.internal;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class zzrc extends BroadcastReceiver {
    private final zza Gd;
    protected Context mContext;

    public static abstract class zza {
        final /* synthetic */ Dialog DP;
        final /* synthetic */ zza DQ;

        zza(zza com_google_android_gms_internal_zzql_zza, Dialog dialog) {
            this.DQ = com_google_android_gms_internal_zzql_zza;
            this.DP = dialog;
            this();
        }

        public void zzasn() {
            this.DQ.DO.zzasm();
            if (this.DP.isShowing()) {
                this.DP.dismiss();
            }
        }
    }

    public zzrc(zza com_google_android_gms_internal_zzrc_zza) {
        this.Gd = com_google_android_gms_internal_zzrc_zza;
    }

    public void onReceive(Context context, Intent intent) {
        Uri data = intent.getData();
        Object obj = null;
        if (data != null) {
            obj = data.getSchemeSpecificPart();
        }
        if ("com.google.android.gms".equals(obj)) {
            this.Gd.zzasn();
            unregister();
        }
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public synchronized void unregister() {
        if (this.mContext != null) {
            this.mContext.unregisterReceiver(this);
        }
        this.mContext = null;
    }
}
