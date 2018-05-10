package com.google.android.gms.common.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.internal.zzri;

public abstract class zzi implements OnClickListener {

    class AnonymousClass1 extends zzi {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ Intent val$intent;
        final /* synthetic */ int val$requestCode;

        AnonymousClass1(Intent intent, Activity activity, int i) {
            this.val$intent = intent;
            this.val$activity = activity;
            this.val$requestCode = i;
        }

        public void zzawr() {
            if (this.val$intent != null) {
                this.val$activity.startActivityForResult(this.val$intent, this.val$requestCode);
            }
        }
    }

    class AnonymousClass3 extends zzi {
        final /* synthetic */ zzri JR;
        final /* synthetic */ Intent val$intent;
        final /* synthetic */ int val$requestCode;

        AnonymousClass3(Intent intent, zzri com_google_android_gms_internal_zzri, int i) {
            this.val$intent = intent;
            this.JR = com_google_android_gms_internal_zzri;
            this.val$requestCode = i;
        }

        @TargetApi(11)
        public void zzawr() {
            if (this.val$intent != null) {
                this.JR.startActivityForResult(this.val$intent, this.val$requestCode);
            }
        }
    }

    public static zzi zza(Activity activity, Intent intent, int i) {
        return new AnonymousClass1(intent, activity, i);
    }

    public static zzi zza(zzri com_google_android_gms_internal_zzri, Intent intent, int i) {
        return new AnonymousClass3(intent, com_google_android_gms_internal_zzri, i);
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        try {
            zzawr();
            dialogInterface.dismiss();
        } catch (Throwable e) {
            Log.e("DialogRedirect", "Can't redirect to app settings for Google Play services", e);
        }
    }

    public abstract void zzawr();
}
