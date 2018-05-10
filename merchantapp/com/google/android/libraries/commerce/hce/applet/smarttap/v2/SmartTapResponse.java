package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import com.google.android.libraries.commerce.hce.applet.HceApplet.AppletProcessCommandResponse;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier.AuthenticationState;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.common.collect.ImmutableSet;
import java.util.Set;

public abstract class SmartTapResponse implements AppletProcessCommandResponse {
    public abstract AuthenticationState authenticationState();

    public abstract boolean encrypted();

    public abstract int instruction();

    public abstract ResponseApdu responseApdu();

    public abstract Set<? extends ServiceObject> serviceObjectsInResponse();

    public abstract boolean unlockRequired();

    public static SmartTapResponse create(int i, ResponseApdu responseApdu) {
        return create(i, responseApdu, AuthenticationState.NOT_AUTHENTICATED);
    }

    public static SmartTapResponse create(int i, ResponseApdu responseApdu, AuthenticationState authenticationState) {
        return create(i, responseApdu, null, authenticationState, false, false);
    }

    public static SmartTapResponse create(int i, ResponseApdu responseApdu, Set<? extends ServiceObject> set, AuthenticationState authenticationState, boolean z, boolean z2) {
        Set set2;
        if (set != null) {
            set2 = set;
        } else {
            set2 = ImmutableSet.of();
        }
        return new AutoValue_SmartTapResponse(i, responseApdu, set2, authenticationState, z, z2);
    }

    public ResponseApdu getResponseApdu() {
        return responseApdu();
    }
}
