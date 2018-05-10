package com.google.android.libraries.commerce.hce.crypto;

import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.common.base.Preconditions;

public class SmartTap2ConscryptInstaller {
    private final Context context;
    private boolean installed = false;

    public SmartTap2ConscryptInstaller(Context context) {
        Preconditions.checkNotNull(context, "Context cannot be null");
        this.context = context;
    }

    public synchronized void installConscryptIfNeeded() {
        Throwable e;
        if (!this.installed) {
            try {
                ProviderInstaller.installIfNeeded(this.context);
                this.installed = true;
            } catch (GooglePlayServicesRepairableException e2) {
                e = e2;
                throw new IllegalStateException("Google Play Services Conscrypt implementation not available.", e);
            } catch (GooglePlayServicesNotAvailableException e3) {
                e = e3;
                throw new IllegalStateException("Google Play Services Conscrypt implementation not available.", e);
            }
        }
    }
}
