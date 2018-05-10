package com.google.android.libraries.commerce.hce.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;

public class SmartTap2MerchantVerifier {
    private final Signature signatureVerifier;

    public enum AuthenticationState {
        UNKNOWN,
        NOT_AUTHENTICATED,
        BAD_KEY,
        NO_KEY,
        LIVE_AUTH,
        PRESIGNED_AUTH,
        BAD_SIGNATURE
    }

    public SmartTap2MerchantVerifier() {
        Throwable e;
        String str;
        String valueOf;
        try {
            this.signatureVerifier = Signature.getInstance("SHA256withECDSA", "AndroidOpenSSL");
        } catch (NoSuchAlgorithmException e2) {
            e = e2;
            str = "Unable to initialize crypto ";
            valueOf = String.valueOf(e.toString());
            throw new IllegalStateException(valueOf.length() == 0 ? str.concat(valueOf) : new String(str), e);
        } catch (NoSuchProviderException e3) {
            e = e3;
            str = "Unable to initialize crypto ";
            valueOf = String.valueOf(e.toString());
            if (valueOf.length() == 0) {
            }
            throw new IllegalStateException(valueOf.length() == 0 ? str.concat(valueOf) : new String(str), e);
        }
    }
}
