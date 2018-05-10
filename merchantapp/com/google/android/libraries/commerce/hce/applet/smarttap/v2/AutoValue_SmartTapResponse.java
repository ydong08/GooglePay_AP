package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier.AuthenticationState;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import java.util.Set;

final class AutoValue_SmartTapResponse extends SmartTapResponse {
    private final AuthenticationState authenticationState;
    private final boolean encrypted;
    private final int instruction;
    private final ResponseApdu responseApdu;
    private final Set<? extends ServiceObject> serviceObjectsInResponse;
    private final boolean unlockRequired;

    AutoValue_SmartTapResponse(int i, ResponseApdu responseApdu, Set<? extends ServiceObject> set, AuthenticationState authenticationState, boolean z, boolean z2) {
        this.instruction = i;
        if (responseApdu == null) {
            throw new NullPointerException("Null responseApdu");
        }
        this.responseApdu = responseApdu;
        if (set == null) {
            throw new NullPointerException("Null serviceObjectsInResponse");
        }
        this.serviceObjectsInResponse = set;
        if (authenticationState == null) {
            throw new NullPointerException("Null authenticationState");
        }
        this.authenticationState = authenticationState;
        this.encrypted = z;
        this.unlockRequired = z2;
    }

    public int instruction() {
        return this.instruction;
    }

    public ResponseApdu responseApdu() {
        return this.responseApdu;
    }

    public Set<? extends ServiceObject> serviceObjectsInResponse() {
        return this.serviceObjectsInResponse;
    }

    public AuthenticationState authenticationState() {
        return this.authenticationState;
    }

    public boolean encrypted() {
        return this.encrypted;
    }

    public boolean unlockRequired() {
        return this.unlockRequired;
    }

    public String toString() {
        int i = this.instruction;
        String valueOf = String.valueOf(this.responseApdu);
        String valueOf2 = String.valueOf(this.serviceObjectsInResponse);
        String valueOf3 = String.valueOf(this.authenticationState);
        boolean z = this.encrypted;
        return new StringBuilder(((String.valueOf(valueOf).length() + 144) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()).append("SmartTapResponse{instruction=").append(i).append(", responseApdu=").append(valueOf).append(", serviceObjectsInResponse=").append(valueOf2).append(", authenticationState=").append(valueOf3).append(", encrypted=").append(z).append(", unlockRequired=").append(this.unlockRequired).append("}").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SmartTapResponse)) {
            return false;
        }
        SmartTapResponse smartTapResponse = (SmartTapResponse) obj;
        if (this.instruction == smartTapResponse.instruction() && this.responseApdu.equals(smartTapResponse.responseApdu()) && this.serviceObjectsInResponse.equals(smartTapResponse.serviceObjectsInResponse()) && this.authenticationState.equals(smartTapResponse.authenticationState()) && this.encrypted == smartTapResponse.encrypted() && this.unlockRequired == smartTapResponse.unlockRequired()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2 = 1231;
        int hashCode = (((((((this.instruction ^ 1000003) * 1000003) ^ this.responseApdu.hashCode()) * 1000003) ^ this.serviceObjectsInResponse.hashCode()) * 1000003) ^ this.authenticationState.hashCode()) * 1000003;
        if (this.encrypted) {
            i = 1231;
        } else {
            i = 1237;
        }
        i = (i ^ hashCode) * 1000003;
        if (!this.unlockRequired) {
            i2 = 1237;
        }
        return i ^ i2;
    }
}
