package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public final class zzrx extends Fragment implements zzri {
    private static WeakHashMap<FragmentActivity, WeakReference<zzrx>> Go = new WeakHashMap();
    private Map<String, zzrh> Gp = new ArrayMap();
    private Bundle kC;
    private int zzblo = 0;

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        Iterator it = this.Gp.values().iterator();
        while (it.hasNext()) {
            it.next();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        for (zzrh onActivityResult : this.Gp.values()) {
            onActivityResult.onActivityResult(i, i2, intent);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzblo = 1;
        this.kC = bundle;
        for (Entry entry : this.Gp.entrySet()) {
            ((zzrh) entry.getValue()).onCreate(bundle != null ? bundle.getBundle((String) entry.getKey()) : null);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            for (Entry entry : this.Gp.entrySet()) {
                Bundle bundle2 = new Bundle();
                ((zzrh) entry.getValue()).onSaveInstanceState(bundle2);
                bundle.putBundle((String) entry.getKey(), bundle2);
            }
        }
    }

    public void onStart() {
        super.onStop();
        this.zzblo = 2;
        for (zzrh onStart : this.Gp.values()) {
            onStart.onStart();
        }
    }

    public void onStop() {
        super.onStop();
        this.zzblo = 3;
        for (zzrh onStop : this.Gp.values()) {
            onStop.onStop();
        }
    }

    public /* synthetic */ Activity zzaun() {
        return zzaur();
    }

    public FragmentActivity zzaur() {
        return getActivity();
    }
}
