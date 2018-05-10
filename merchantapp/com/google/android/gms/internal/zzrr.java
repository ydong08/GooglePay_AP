package com.google.android.gms.internal;

import android.util.SparseArray;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.ResultStore;

public class zzrr extends ResultStore {
    private final SparseArray<ResultCallbacks<?>> GA = new SparseArray();
    private final SparseArray<PendingResult<?>> Gz = new SparseArray();
    private final Object zzaiw = new Object();

    public void remove(int i) {
        synchronized (this.zzaiw) {
            PendingResult pendingResult = (PendingResult) this.Gz.get(i);
            if (pendingResult != null) {
                this.Gz.remove(i);
                if (((ResultCallback) this.GA.get(i)) != null) {
                    pendingResult.setResultCallback(null);
                }
            }
        }
    }

    public void zzauq() {
        synchronized (this.zzaiw) {
            this.GA.clear();
            for (int i = 0; i < this.Gz.size(); i++) {
                ((PendingResult) this.Gz.valueAt(i)).setResultCallback(null);
            }
        }
    }

    public void zzy(Object obj) {
        synchronized (this.zzaiw) {
            for (int i = 0; i < this.Gz.size(); i++) {
                ((PendingResult) this.Gz.valueAt(i)).cancel();
            }
        }
        ResultStore.zzw(obj);
    }
}
