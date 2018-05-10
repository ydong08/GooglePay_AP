package com.google.android.gms.dynamic;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.internal.zzab;

public abstract class zzg<T> {
    private final String aaW;
    private T aaX;

    public static class zza extends Exception {
        public zza(String str) {
            super(str);
        }

        public zza(String str, Throwable th) {
            super(str, th);
        }
    }

    protected zzg(String str) {
        this.aaW = str;
    }

    protected final T zzct(Context context) throws zza {
        if (this.aaX == null) {
            zzab.zzae(context);
            Context remoteContext = GooglePlayServicesUtilLight.getRemoteContext(context);
            if (remoteContext == null) {
                throw new zza("Could not get remote context.");
            }
            try {
                this.aaX = zzd((IBinder) remoteContext.getClassLoader().loadClass(this.aaW).newInstance());
            } catch (Throwable e) {
                throw new zza("Could not load creator class.", e);
            } catch (Throwable e2) {
                throw new zza("Could not instantiate creator.", e2);
            } catch (Throwable e22) {
                throw new zza("Could not access creator.", e22);
            }
        }
        return this.aaX;
    }

    protected abstract T zzd(IBinder iBinder);
}
