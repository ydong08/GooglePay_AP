package com.google.android.gms.internal;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class zzry extends Fragment {
    private zzrr Gs = new zzrr();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void onDestroy() {
        super.onDestroy();
        this.Gs.zzy(getActivity());
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.Gs.zzauq();
    }
}
