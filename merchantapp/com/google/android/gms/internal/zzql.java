package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;

public abstract class zzql extends zzrh implements OnCancelListener {
    protected boolean DK;
    private ConnectionResult DL;
    private int DM;
    private final Handler DN;
    protected final GoogleApiAvailability Dh;
    protected boolean mStarted;

    class zza implements Runnable {
        final /* synthetic */ zzql DO;

        private zza(zzql com_google_android_gms_internal_zzql) {
            this.DO = com_google_android_gms_internal_zzql;
        }

        public void run() {
            if (!this.DO.mStarted) {
                return;
            }
            if (this.DO.DL.hasResolution()) {
                this.DO.Gn.startActivityForResult(GoogleApiActivity.zzb(this.DO.getActivity(), this.DO.DL.getResolution(), this.DO.DM, false), 1);
            } else if (this.DO.Dh.isUserResolvableError(this.DO.DL.getErrorCode())) {
                this.DO.Dh.zza(this.DO.getActivity(), this.DO.Gn, this.DO.DL.getErrorCode(), 2, this.DO);
            } else if (this.DO.DL.getErrorCode() == 18) {
                this.DO.Dh.zza(this.DO.getActivity().getApplicationContext(), new com.google.android.gms.internal.zzrc.zza(this, this.DO.Dh.zza(this.DO.getActivity(), this.DO)));
            } else {
                this.DO.zza(this.DO.DL, this.DO.DM);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActivityResult(int r6, int r7, android.content.Intent r8) {
        /*
        r5 = this;
        r4 = 18;
        r2 = 13;
        r0 = 1;
        r1 = 0;
        switch(r6) {
            case 1: goto L_0x0027;
            case 2: goto L_0x0010;
            default: goto L_0x0009;
        };
    L_0x0009:
        r0 = r1;
    L_0x000a:
        if (r0 == 0) goto L_0x003d;
    L_0x000c:
        r5.zzasm();
    L_0x000f:
        return;
    L_0x0010:
        r2 = r5.Dh;
        r3 = r5.getActivity();
        r2 = r2.isGooglePlayServicesAvailable(r3);
        if (r2 != 0) goto L_0x0047;
    L_0x001c:
        r1 = r5.DL;
        r1 = r1.getErrorCode();
        if (r1 != r4) goto L_0x000a;
    L_0x0024:
        if (r2 != r4) goto L_0x000a;
    L_0x0026:
        goto L_0x000f;
    L_0x0027:
        r3 = -1;
        if (r7 == r3) goto L_0x000a;
    L_0x002a:
        if (r7 != 0) goto L_0x0009;
    L_0x002c:
        if (r8 == 0) goto L_0x0045;
    L_0x002e:
        r0 = "<<ResolutionFailureErrorDetail>>";
        r0 = r8.getIntExtra(r0, r2);
    L_0x0034:
        r2 = new com.google.android.gms.common.ConnectionResult;
        r3 = 0;
        r2.<init>(r0, r3);
        r5.DL = r2;
        goto L_0x0009;
    L_0x003d:
        r0 = r5.DL;
        r1 = r5.DM;
        r5.zza(r0, r1);
        goto L_0x000f;
    L_0x0045:
        r0 = r2;
        goto L_0x0034;
    L_0x0047:
        r0 = r1;
        goto L_0x001c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzql.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onCancel(DialogInterface dialogInterface) {
        zza(new ConnectionResult(13, null), this.DM);
        zzasm();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.DK = bundle.getBoolean("resolving_error", false);
            if (this.DK) {
                this.DM = bundle.getInt("failed_client_id", -1);
                this.DL = new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent) bundle.getParcelable("failed_resolution"));
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("resolving_error", this.DK);
        if (this.DK) {
            bundle.putInt("failed_client_id", this.DM);
            bundle.putInt("failed_status", this.DL.getErrorCode());
            bundle.putParcelable("failed_resolution", this.DL.getResolution());
        }
    }

    public void onStart() {
        super.onStart();
        this.mStarted = true;
    }

    public void onStop() {
        super.onStop();
        this.mStarted = false;
    }

    protected abstract void zza(ConnectionResult connectionResult, int i);

    protected abstract void zzash();

    protected void zzasm() {
        this.DM = -1;
        this.DK = false;
        this.DL = null;
        zzash();
    }

    public void zzb(ConnectionResult connectionResult, int i) {
        if (!this.DK) {
            this.DK = true;
            this.DM = i;
            this.DL = connectionResult;
            this.DN.post(new zza());
        }
    }
}
