package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.zzq.zza;

public class ResolveAccountResponse extends AbstractSafeParcelable {
    public static final Creator<ResolveAccountResponse> CREATOR = new zzad();
    private ConnectionResult CE;
    private boolean EM;
    IBinder IN;
    private boolean KK;
    final int mVersionCode;

    ResolveAccountResponse(int i, IBinder iBinder, ConnectionResult connectionResult, boolean z, boolean z2) {
        this.mVersionCode = i;
        this.IN = iBinder;
        this.CE = connectionResult;
        this.EM = z;
        this.KK = z2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ResolveAccountResponse)) {
            return false;
        }
        ResolveAccountResponse resolveAccountResponse = (ResolveAccountResponse) obj;
        return this.CE.equals(resolveAccountResponse.CE) && zzaxg().equals(resolveAccountResponse.zzaxg());
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzad.zza(this, parcel, i);
    }

    public zzq zzaxg() {
        return zza.zzgw(this.IN);
    }

    public ConnectionResult zzaxh() {
        return this.CE;
    }

    public boolean zzaxi() {
        return this.EM;
    }

    public boolean zzaxj() {
        return this.KK;
    }
}
