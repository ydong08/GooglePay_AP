package com.google.android.gms.common.stats;

import android.os.SystemClock;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

public class zze {
    private final long MV;
    private final int MW;
    private final SimpleArrayMap<String, Long> MX;

    public zze() {
        this.MV = 60000;
        this.MW = 10;
        this.MX = new SimpleArrayMap(10);
    }

    public zze(int i, long j) {
        this.MV = j;
        this.MW = i;
        this.MX = new SimpleArrayMap();
    }

    private void zzh(long j, long j2) {
        for (int size = this.MX.size() - 1; size >= 0; size--) {
            if (j2 - ((Long) this.MX.valueAt(size)).longValue() > j) {
                this.MX.removeAt(size);
            }
        }
    }

    public Long zzgz(String str) {
        Long l;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.MV;
        synchronized (this) {
            while (this.MX.size() >= this.MW) {
                zzh(j, elapsedRealtime);
                j /= 2;
                Log.w("ConnectionTracker", "The max capacity " + this.MW + " is not enough. Current durationThreshold is: " + j);
            }
            l = (Long) this.MX.put(str, Long.valueOf(elapsedRealtime));
        }
        return l;
    }

    public boolean zzha(String str) {
        boolean z;
        synchronized (this) {
            z = this.MX.remove(str) != null;
        }
        return z;
    }
}
