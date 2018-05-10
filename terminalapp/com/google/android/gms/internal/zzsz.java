package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzab;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class zzsz implements ThreadFactory {
    private final String NG;
    private final AtomicInteger NH;
    private final ThreadFactory NI;
    private final int mPriority;

    public zzsz(String str) {
        this(str, 0);
    }

    public zzsz(String str, int i) {
        this.NH = new AtomicInteger();
        this.NI = Executors.defaultThreadFactory();
        this.NG = (String) zzab.zzb((Object) str, (Object) "Name must not be null");
        this.mPriority = i;
    }

    public Thread newThread(Runnable runnable) {
        Thread newThread = this.NI.newThread(new zzta(runnable, this.mPriority));
        String str = this.NG;
        newThread.setName(new StringBuilder(String.valueOf(str).length() + 13).append(str).append("[").append(this.NH.getAndIncrement()).append("]").toString());
        return newThread;
    }
}
