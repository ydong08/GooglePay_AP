package com.google.android.gms.common.util;

public final class zzg implements Clock {
    private static zzg Nk;

    public static synchronized Clock zzazb() {
        Clock clock;
        synchronized (zzg.class) {
            if (Nk == null) {
                Nk = new zzg();
            }
            clock = Nk;
        }
        return clock;
    }
}
