package com.google.android.gms.common.stats;

import com.google.android.gms.internal.zzse;

public final class zzc {
    public static zzse<Integer> MF = zzse.zza("gms:common:stats:max_num_of_events", Integer.valueOf(100));
    public static zzse<Integer> MG = zzse.zza("gms:common:stats:max_chunk_size", Integer.valueOf(100));

    public static final class zza {
        public static zzse<Integer> MH = zzse.zza("gms:common:stats:connections:level", Integer.valueOf(zzd.LOG_LEVEL_OFF));
        public static zzse<String> MI = zzse.zzai("gms:common:stats:connections:ignored_calling_processes", "");
        public static zzse<String> MJ = zzse.zzai("gms:common:stats:connections:ignored_calling_services", "");
        public static zzse<String> MK = zzse.zzai("gms:common:stats:connections:ignored_target_processes", "");
        public static zzse<String> ML = zzse.zzai("gms:common:stats:connections:ignored_target_services", "com.google.android.gms.auth.GetToken");
        public static zzse<Long> MM = zzse.zza("gms:common:stats:connections:time_out_duration", Long.valueOf(600000));
    }
}
