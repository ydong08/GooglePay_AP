package com.google.android.gms.common;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.base.R;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzh;
import com.google.android.gms.common.util.zzr;

public final class GooglePlayServicesUtil extends GooglePlayServicesUtilLight {
    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;

    private GooglePlayServicesUtil() {
    }

    public static Resources getRemoteResource(Context context) {
        return GooglePlayServicesUtilLight.getRemoteResource(context);
    }

    static void zza(int i, Context context, PendingIntent pendingIntent) {
        zza(i, context, null, pendingIntent);
    }

    @TargetApi(20)
    private static void zza(int i, Context context, String str, PendingIntent pendingIntent) {
        Notification build;
        int i2;
        Resources resources = context.getResources();
        String zzbx = GooglePlayServicesUtilLight.zzbx(context);
        CharSequence zzf = zzh.zzf(context, i);
        if (zzf == null) {
            zzf = resources.getString(R.string.common_google_play_services_notification_ticker);
        }
        CharSequence zzc = zzh.zzc(context, i, zzbx);
        if (com.google.android.gms.common.util.zzh.zzcm(context)) {
            zzab.zzbn(zzr.zzazh());
            build = new Builder(context).setSmallIcon(R.drawable.common_ic_googleplayservices).setPriority(2).setAutoCancel(true).setStyle(new BigTextStyle().bigText(new StringBuilder((String.valueOf(zzf).length() + 1) + String.valueOf(zzc).length()).append(zzf).append(" ").append(zzc).toString())).addAction(R.drawable.common_full_open_on_phone, resources.getString(R.string.common_open_on_phone), pendingIntent).build();
        } else {
            CharSequence string = resources.getString(R.string.common_google_play_services_notification_ticker);
            if (zzr.zzazd()) {
                Notification build2;
                Builder autoCancel = new Builder(context).setSmallIcon(17301642).setContentTitle(zzf).setContentText(zzc).setContentIntent(pendingIntent).setTicker(string).setAutoCancel(true);
                if (zzr.zzazl()) {
                    autoCancel.setLocalOnly(true);
                }
                if (zzr.zzazh()) {
                    autoCancel.setStyle(new BigTextStyle().bigText(zzc));
                    build2 = autoCancel.build();
                } else {
                    build2 = autoCancel.getNotification();
                }
                if (VERSION.SDK_INT == 19) {
                    build2.extras.putBoolean("android.support.localOnly", true);
                }
                build = build2;
            } else {
                build = new NotificationCompat.Builder(context).setSmallIcon(17301642).setTicker(string).setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentIntent(pendingIntent).setContentTitle(zzf).setContentText(zzc).build();
            }
        }
        if (GooglePlayServicesUtilLight.zzhs(i)) {
            Cu.set(false);
            i2 = 10436;
        } else {
            i2 = 39789;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (str != null) {
            notificationManager.notify(str, i2, build);
        } else {
            notificationManager.notify(i2, build);
        }
    }
}
