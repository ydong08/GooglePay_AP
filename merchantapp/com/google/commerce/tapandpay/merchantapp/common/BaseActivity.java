package com.google.commerce.tapandpay.merchantapp.common;

import android.content.ComponentName;
import android.nfc.cardemulation.CardEmulation;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.apps.common.inject.InjectedApplication;
import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    CardEmulation cardEmulation;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ((InjectedApplication) getApplication()).inject(this);
    }

    protected void onResume() {
        super.onResume();
        if (canSetPreferredCardEmulationService()) {
            this.cardEmulation.setPreferredService(this, new ComponentName(this, "com.google.commerce.tapandpay.merchantapp.hce.MerchantApduService"));
        }
    }

    protected void onPause() {
        if (canSetPreferredCardEmulationService()) {
            this.cardEmulation.unsetPreferredService(this);
        }
        super.onPause();
    }

    private boolean canSetPreferredCardEmulationService() {
        return VERSION.SDK_INT >= 21 && this.cardEmulation != null;
    }
}
