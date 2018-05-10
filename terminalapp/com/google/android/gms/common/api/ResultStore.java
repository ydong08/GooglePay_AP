package com.google.android.gms.common.api;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class ResultStore {
    private static final Map<Object, ResultStore> Do = new WeakHashMap();
    private static final Object zzank = new Object();

    protected static void zzw(Object obj) {
        synchronized (zzank) {
            Do.remove(obj);
        }
    }

    public abstract void remove(int i);
}
