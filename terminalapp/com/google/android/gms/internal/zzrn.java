package com.google.android.gms.internal;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class zzrn {
    private final Set<zzrm<?>> lM = Collections.newSetFromMap(new WeakHashMap());

    public void release() {
        for (zzrm clear : this.lM) {
            clear.clear();
        }
        this.lM.clear();
    }
}
