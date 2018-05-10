package com.google.android.gms.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.internal.zzaa;

public final class zzqh<O extends ApiOptions> {
    private final O CO;
    private final Api<O> zM;

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzqh)) {
            return false;
        }
        zzqh com_google_android_gms_internal_zzqh = (zzqh) obj;
        return zzaa.equal(this.zM, com_google_android_gms_internal_zzqh.zM) && zzaa.equal(this.CO, com_google_android_gms_internal_zzqh.CO);
    }

    public int hashCode() {
        return zzaa.hashCode(this.zM, this.CO);
    }

    public zzc<?> zzarn() {
        return this.zM.zzarn();
    }

    public String zzasg() {
        return this.zM.getName();
    }
}
