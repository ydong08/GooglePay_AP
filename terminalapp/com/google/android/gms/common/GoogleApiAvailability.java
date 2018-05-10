package com.google.android.gms.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import com.google.android.gms.base.R;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.internal.zzi;
import com.google.android.gms.common.util.zzh;
import com.google.android.gms.common.util.zzr;
import com.google.android.gms.internal.zzrc;
import com.google.android.gms.internal.zzrc.zza;
import com.google.android.gms.internal.zzri;

public class GoogleApiAvailability extends GoogleApiAvailabilityLight {
    private static final GoogleApiAvailability Cd = new GoogleApiAvailability();
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;

    GoogleApiAvailability() {
    }

    public static GoogleApiAvailability getInstance() {
        return Cd;
    }

    public Dialog getErrorDialog(Activity activity, int i, int i2, OnCancelListener onCancelListener) {
        return zza((Context) activity, i, zzi.zza(activity, getErrorResolutionIntent(activity, i, "d"), i2), onCancelListener);
    }

    public Intent getErrorResolutionIntent(Context context, int i, String str) {
        return super.getErrorResolutionIntent(context, i, str);
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2) {
        return super.getErrorResolutionPendingIntent(context, i, i2);
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2, String str) {
        return super.getErrorResolutionPendingIntent(context, i, i2, str);
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            return connectionResult.getResolution();
        }
        int errorCode = connectionResult.getErrorCode();
        if (zzh.zzcm(context) && errorCode == 2) {
            errorCode = 42;
        }
        return getErrorResolutionPendingIntent(context, errorCode, 0);
    }

    public int isGooglePlayServicesAvailable(Context context) {
        return super.isGooglePlayServicesAvailable(context);
    }

    public final boolean isUserResolvableError(int i) {
        return super.isUserResolvableError(i);
    }

    public boolean showErrorDialogFragment(Activity activity, int i, int i2, OnCancelListener onCancelListener) {
        Dialog errorDialog = getErrorDialog(activity, i, i2, onCancelListener);
        if (errorDialog == null) {
            return false;
        }
        zza(activity, errorDialog, "GooglePlayServicesErrorDialog", onCancelListener);
        return true;
    }

    public Dialog zza(Activity activity, OnCancelListener onCancelListener) {
        View progressBar = new ProgressBar(activity, null, 16842874);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(0);
        Builder builder = new Builder(activity);
        builder.setView(progressBar);
        String zzbx = GooglePlayServicesUtilLight.zzbx(activity);
        builder.setMessage(activity.getResources().getString(R.string.common_google_play_services_updating_text, new Object[]{zzbx}));
        builder.setTitle(R.string.common_google_play_services_updating_title);
        builder.setPositiveButton("", null);
        Dialog create = builder.create();
        zza(activity, create, "GooglePlayServicesUpdatingDialog", onCancelListener);
        return create;
    }

    @TargetApi(14)
    Dialog zza(Context context, int i, zzi com_google_android_gms_common_internal_zzi, OnCancelListener onCancelListener) {
        Builder builder = null;
        if (i == 0) {
            return null;
        }
        if (zzh.zzcm(context) && i == 2) {
            i = 42;
        }
        if (zzr.zzazg()) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(16843529, typedValue, true);
            if ("Theme.Dialog.Alert".equals(context.getResources().getResourceEntryName(typedValue.resourceId))) {
                builder = new Builder(context, 5);
            }
        }
        if (builder == null) {
            builder = new Builder(context);
        }
        builder.setMessage(com.google.android.gms.common.internal.zzh.zzb(context, i, GooglePlayServicesUtilLight.zzbx(context)));
        if (onCancelListener != null) {
            builder.setOnCancelListener(onCancelListener);
        }
        CharSequence zzg = com.google.android.gms.common.internal.zzh.zzg(context, i);
        if (zzg != null) {
            builder.setPositiveButton(zzg, com_google_android_gms_common_internal_zzi);
        }
        zzg = com.google.android.gms.common.internal.zzh.zze(context, i);
        if (zzg != null) {
            builder.setTitle(zzg);
        }
        return builder.create();
    }

    public zzrc zza(Context context, zza com_google_android_gms_internal_zzrc_zza) {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        BroadcastReceiver com_google_android_gms_internal_zzrc = new zzrc(com_google_android_gms_internal_zzrc_zza);
        context.registerReceiver(com_google_android_gms_internal_zzrc, intentFilter);
        com_google_android_gms_internal_zzrc.setContext(context);
        if (zzm(context, "com.google.android.gms")) {
            return com_google_android_gms_internal_zzrc;
        }
        com_google_android_gms_internal_zzrc_zza.zzasn();
        com_google_android_gms_internal_zzrc.unregister();
        return null;
    }

    @TargetApi(11)
    void zza(Activity activity, Dialog dialog, String str, OnCancelListener onCancelListener) {
        if (activity instanceof FragmentActivity) {
            SupportErrorDialogFragment.newInstance(dialog, onCancelListener).show(((FragmentActivity) activity).getSupportFragmentManager(), str);
        } else if (zzr.zzazd()) {
            ErrorDialogFragment.newInstance(dialog, onCancelListener).show(activity.getFragmentManager(), str);
        } else {
            throw new RuntimeException("This Activity does not support Fragments.");
        }
    }

    public void zza(Context context, ConnectionResult connectionResult, int i) {
        PendingIntent errorResolutionPendingIntent = getErrorResolutionPendingIntent(context, connectionResult);
        if (errorResolutionPendingIntent != null) {
            GooglePlayServicesUtil.zza(connectionResult.getErrorCode(), context, GoogleApiActivity.zza(context, errorResolutionPendingIntent, i));
        }
    }

    public boolean zza(Activity activity, zzri com_google_android_gms_internal_zzri, int i, int i2, OnCancelListener onCancelListener) {
        Dialog zza = zza((Context) activity, i, zzi.zza(com_google_android_gms_internal_zzri, getErrorResolutionIntent(activity, i, "d"), i2), onCancelListener);
        if (zza == null) {
            return false;
        }
        zza(activity, zza, "GooglePlayServicesErrorDialog", onCancelListener);
        return true;
    }
}
