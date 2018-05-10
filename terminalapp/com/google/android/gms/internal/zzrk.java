package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Bundle;

@TargetApi(11)
public class zzrk extends Fragment {
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
