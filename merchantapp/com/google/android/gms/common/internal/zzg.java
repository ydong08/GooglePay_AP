package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.view.View;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.zzack;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class zzg {
    private final Set<Scope> CX;
    private final int CZ;
    private final View Da;
    private final String Db;
    private final Set<Scope> JL;
    private final Map<Api<?>, zza> JM;
    private final zzack JN;
    private Integer JO;
    private final Account P;
    private final String cp;

    public static final class zza {
        public final Set<Scope> fQ;
    }

    public zzg(Account account, Set<Scope> set, Map<Api<?>, zza> map, int i, View view, String str, String str2, zzack com_google_android_gms_internal_zzack) {
        Map map2;
        this.P = account;
        this.CX = set == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(set);
        if (map == null) {
            map2 = Collections.EMPTY_MAP;
        }
        this.JM = map2;
        this.Da = view;
        this.CZ = i;
        this.cp = str;
        this.Db = str2;
        this.JN = com_google_android_gms_internal_zzack;
        Set hashSet = new HashSet(this.CX);
        for (zza com_google_android_gms_common_internal_zzg_zza : this.JM.values()) {
            hashSet.addAll(com_google_android_gms_common_internal_zzg_zza.fQ);
        }
        this.JL = Collections.unmodifiableSet(hashSet);
    }

    public static zzg zzcf(Context context) {
        return new Builder(context).zzasb();
    }

    public Account getAccount() {
        return this.P;
    }

    public Set<Scope> zzawk() {
        return this.JL;
    }

    public String zzawm() {
        return this.cp;
    }

    public String zzawn() {
        return this.Db;
    }

    public zzack zzawp() {
        return this.JN;
    }

    public Integer zzawq() {
        return this.JO;
    }
}
