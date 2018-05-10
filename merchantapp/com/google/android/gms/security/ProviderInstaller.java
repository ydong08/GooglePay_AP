package com.google.android.gms.security;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.internal.zzab;
import java.lang.reflect.Method;

public class ProviderInstaller {
    private static final GoogleApiAvailabilityLight bgw = GoogleApiAvailabilityLight.getInstance();
    private static Method bgx = null;
    private static final Object zzank = new Object();

    public static void installIfNeeded(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        zzab.zzb((Object) context, (Object) "Context must not be null");
        bgw.zzbs(context);
        Context remoteContext = GooglePlayServicesUtilLight.getRemoteContext(context);
        if (remoteContext == null) {
            Log.e("ProviderInstaller", "Failed to get remote context");
            throw new GooglePlayServicesNotAvailableException(8);
        }
        synchronized (zzank) {
            try {
                if (bgx == null) {
                    zzdw(remoteContext);
                }
                bgx.invoke(null, new Object[]{remoteContext});
            } catch (Exception e) {
                String str = "ProviderInstaller";
                String str2 = "Failed to install provider: ";
                String valueOf = String.valueOf(e.getMessage());
                Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                throw new GooglePlayServicesNotAvailableException(8);
            }
        }
    }

    private static void zzdw(Context context) throws ClassNotFoundException, NoSuchMethodException {
        bgx = context.getClassLoader().loadClass("com.google.android.gms.common.security.ProviderInstallerImpl").getMethod("insertProvider", new Class[]{Context.class});
    }
}
