package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.ServiceConnection;

public abstract class zzm {
    private static final Object Kn = new Object();
    private static zzm Ko;

    public static zzm zzcg(Context context) {
        synchronized (Kn) {
            if (Ko == null) {
                Ko = new zzn(context.getApplicationContext());
            }
        }
        return Ko;
    }

    public abstract boolean zza(String str, String str2, ServiceConnection serviceConnection, String str3);

    public abstract void zzb(String str, String str2, ServiceConnection serviceConnection, String str3);
}
