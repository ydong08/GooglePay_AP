package com.google.android.gms.common.stats;

import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

public abstract class StatsEvent extends AbstractSafeParcelable implements ReflectedParcelable {
    public abstract long getDurationMillis();

    public abstract int getEventType();

    public abstract long getTimeMillis();

    public String toString() {
        long timeMillis = getTimeMillis();
        String valueOf = String.valueOf("\t");
        int eventType = getEventType();
        String valueOf2 = String.valueOf("\t");
        long durationMillis = getDurationMillis();
        String valueOf3 = String.valueOf(zzayl());
        return new StringBuilder(((String.valueOf(valueOf).length() + 51) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()).append(timeMillis).append(valueOf).append(eventType).append(valueOf2).append(durationMillis).append(valueOf3).toString();
    }

    public abstract String zzayl();
}
