package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.dynamic.zzg.zza;

public final class zzaf extends zzg<zzx> {
    private static final zzaf KN = new zzaf();

    private zzaf() {
        super("com.google.android.gms.common.ui.SignInButtonCreatorImpl");
    }

    public static View zzb(Context context, int i, int i2, Scope[] scopeArr) throws zza {
        return KN.zzc(context, i, i2, scopeArr);
    }

    private View zzc(Context context, int i, int i2, Scope[] scopeArr) throws zza {
        try {
            SignInButtonConfig signInButtonConfig = new SignInButtonConfig(i, i2, scopeArr);
            return (View) zze.zzag(((zzx) zzct(context)).zza(zze.zzal(context), signInButtonConfig));
        } catch (Throwable e) {
            throw new zza("Could not get button with size " + i + " and color " + i2, e);
        }
    }

    public /* synthetic */ Object zzd(IBinder iBinder) {
        return zzhe(iBinder);
    }

    public zzx zzhe(IBinder iBinder) {
        return zzx.zza.zzhd(iBinder);
    }
}
