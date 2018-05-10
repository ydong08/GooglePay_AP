package com.google.android.gms.common.stats;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Debug;
import android.os.Parcelable;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.gms.common.stats.zzc.zza;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.common.util.zzs;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class zzb {
    private static final Object Kn = new Object();
    private static Integer MC;
    private static zzb Mw;
    private final List<String> MA;
    private zze MB;
    private zze MD;
    private final List<String> Mx;
    private final List<String> My;
    private final List<String> Mz;

    private zzb() {
        if (getLogLevel() == zzd.LOG_LEVEL_OFF) {
            this.Mx = Collections.EMPTY_LIST;
            this.My = Collections.EMPTY_LIST;
            this.Mz = Collections.EMPTY_LIST;
            this.MA = Collections.EMPTY_LIST;
            return;
        }
        String str = (String) zza.MI.get();
        this.Mx = str == null ? Collections.EMPTY_LIST : Arrays.asList(str.split(","));
        str = (String) zza.MJ.get();
        this.My = str == null ? Collections.EMPTY_LIST : Arrays.asList(str.split(","));
        str = (String) zza.MK.get();
        this.Mz = str == null ? Collections.EMPTY_LIST : Arrays.asList(str.split(","));
        str = (String) zza.ML.get();
        this.MA = str == null ? Collections.EMPTY_LIST : Arrays.asList(str.split(","));
        this.MB = new zze(1024, ((Long) zza.MM.get()).longValue());
        this.MD = new zze(1024, ((Long) zza.MM.get()).longValue());
    }

    private static int getLogLevel() {
        if (MC == null) {
            try {
                MC = Integer.valueOf(zzd.zzzz() ? ((Integer) zza.MH.get()).intValue() : zzd.LOG_LEVEL_OFF);
            } catch (SecurityException e) {
                MC = Integer.valueOf(zzd.LOG_LEVEL_OFF);
            }
        }
        return MC.intValue();
    }

    private static String zza(StackTraceElement[] stackTraceElementArr, int i) {
        if (i + 4 >= stackTraceElementArr.length) {
            return "<bottom of call stack>";
        }
        StackTraceElement stackTraceElement = stackTraceElementArr[i + 4];
        String valueOf = String.valueOf(stackTraceElement.getClassName());
        String valueOf2 = String.valueOf(stackTraceElement.getMethodName());
        return new StringBuilder((String.valueOf(valueOf).length() + 13) + String.valueOf(valueOf2).length()).append(valueOf).append(".").append(valueOf2).append(":").append(stackTraceElement.getLineNumber()).toString();
    }

    private void zza(Context context, String str, int i, String str2, String str3, String str4, String str5) {
        Parcelable connectionEvent;
        long currentTimeMillis = System.currentTimeMillis();
        String str6 = null;
        if (!((getLogLevel() & zzd.MR) == 0 || i == 13)) {
            str6 = zzx(3, 5);
        }
        long j = 0;
        if ((getLogLevel() & zzd.MT) != 0) {
            j = Debug.getNativeHeapAllocatedSize();
        }
        if (i == 1 || i == 4 || i == 14) {
            connectionEvent = new ConnectionEvent(currentTimeMillis, i, null, null, null, null, str6, str, SystemClock.elapsedRealtime(), j);
        } else {
            connectionEvent = new ConnectionEvent(currentTimeMillis, i, str2, str3, str4, str5, str6, str, SystemClock.elapsedRealtime(), j);
        }
        context.startService(new Intent().setComponent(zzd.MN).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", connectionEvent));
    }

    private void zza(Context context, String str, String str2, Intent intent, int i) {
        String str3 = null;
        if (zzayn() && this.MB != null) {
            String str4;
            String str5;
            if (i != 4 && i != 1) {
                ServiceInfo zzd = zzd(context, intent);
                if (zzd == null) {
                    Log.w("ConnectionTracker", String.format("Client %s made an invalid request %s", new Object[]{str2, intent.toUri(0)}));
                    return;
                }
                str4 = zzd.processName;
                str5 = zzd.name;
                str3 = zzs.zzazq();
                if (zzb(str3, str2, str4, str5)) {
                    this.MB.zzgz(str);
                } else {
                    return;
                }
            } else if (this.MB.zzha(str)) {
                str5 = null;
                str4 = null;
            } else {
                return;
            }
            zza(context, str, i, str3, str2, str4, str5);
        }
    }

    public static zzb zzaym() {
        synchronized (Kn) {
            if (Mw == null) {
                Mw = new zzb();
            }
        }
        return Mw;
    }

    private boolean zzayn() {
        return false;
    }

    private String zzb(ServiceConnection serviceConnection) {
        return String.valueOf((((long) Process.myPid()) << 32) | ((long) System.identityHashCode(serviceConnection)));
    }

    private boolean zzb(String str, String str2, String str3, String str4) {
        return (this.Mx.contains(str) || this.My.contains(str2) || this.Mz.contains(str3) || this.MA.contains(str4) || (str3.equals(str) && (getLogLevel() & zzd.MS) != 0)) ? false : true;
    }

    private boolean zzc(Context context, Intent intent) {
        ComponentName component = intent.getComponent();
        return component == null ? false : zzd.zzr(context, component.getPackageName());
    }

    private static ServiceInfo zzd(Context context, Intent intent) {
        List<ResolveInfo> queryIntentServices = context.getPackageManager().queryIntentServices(intent, 128);
        if (queryIntentServices == null || queryIntentServices.size() == 0) {
            Log.w("ConnectionTracker", String.format("There are no handler of this intent: %s\n Stack trace: %s", new Object[]{intent.toUri(0), zzx(3, 20)}));
            return null;
        } else if (queryIntentServices.size() <= 1) {
            return ((ResolveInfo) queryIntentServices.get(0)).serviceInfo;
        } else {
            Log.w("ConnectionTracker", String.format("Multiple handlers found for this intent: %s\n Stack trace: %s", new Object[]{intent.toUri(0), zzx(3, 20)}));
            for (ResolveInfo resolveInfo : queryIntentServices) {
                Log.w("ConnectionTracker", resolveInfo.serviceInfo.name);
            }
            return null;
        }
    }

    private static String zzx(int i, int i2) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuffer stringBuffer = new StringBuffer();
        int i3 = i2 + i;
        while (i < i3) {
            stringBuffer.append(zza(stackTrace, i)).append(" ");
            i++;
        }
        return stringBuffer.toString();
    }

    @SuppressLint({"UntrackedBindService"})
    public void zza(Context context, ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
        zza(context, zzb(serviceConnection), null, null, 1);
    }

    public void zza(Context context, ServiceConnection serviceConnection, String str, Intent intent) {
        zza(context, zzb(serviceConnection), str, intent, 3);
    }

    @SuppressLint({"UntrackedBindService"})
    public boolean zza(Context context, String str, Intent intent, ServiceConnection serviceConnection, int i) {
        if (zzc(context, intent)) {
            Log.w("ConnectionTracker", "Attempted to bind to a service in a STOPPED package.");
            return false;
        }
        boolean bindService = context.bindService(intent, serviceConnection, i);
        if (bindService) {
            zza(context, zzb(serviceConnection), str, intent, 2);
        }
        return bindService;
    }

    public void zzb(Context context, ServiceConnection serviceConnection) {
        zza(context, zzb(serviceConnection), null, null, 4);
    }
}
