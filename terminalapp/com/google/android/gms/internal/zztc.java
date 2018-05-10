package com.google.android.gms.internal;

import android.content.Context;

public class zztc {
    private static zztc NK = new zztc();
    private zztb NJ = null;

    public static zztb zzcs(Context context) {
        return NK.zzcr(context);
    }

    public synchronized zztb zzcr(Context context) {
        if (this.NJ == null) {
            if (context.getApplicationContext() != null) {
                context = context.getApplicationContext();
            }
            this.NJ = new zztb(context);
        }
        return this.NJ;
    }
}
