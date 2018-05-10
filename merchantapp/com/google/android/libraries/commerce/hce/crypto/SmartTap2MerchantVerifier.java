package com.google.android.libraries.commerce.hce.crypto;

import com.google.common.base.Preconditions;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;

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

    public AuthenticationState verifySignature(ECPublicKey eCPublicKey, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5) throws ValuablesCryptoException {
        Throwable e;
        String str;
        String valueOf;
        boolean equals = Arrays.equals(bArr3, new byte[32]);
        Preconditions.checkArgument(bArr2.length == 32, String.format("terminalNonce must be %d bytes, is %d", new Object[]{Integer.valueOf(32), Integer.valueOf(bArr2.length)}));
        Preconditions.checkArgument(bArr3.length == 32, String.format("handsetNonce must be %d bytes, is %d", new Object[]{Integer.valueOf(32), Integer.valueOf(bArr3.length)}));
        Preconditions.checkArgument(bArr4.length == 4, String.format("merchantId must be %d bytes, is %d", new Object[]{Integer.valueOf(4), Integer.valueOf(bArr4.length)}));
        Preconditions.checkArgument(bArr5.length == 33, String.format("ephemeralPublicKey must be %d bytes, is %d", new Object[]{Integer.valueOf(33), Integer.valueOf(bArr5.length)}));
        try {
            this.signatureVerifier.initVerify(eCPublicKey);
            this.signatureVerifier.update(bArr2);
            this.signatureVerifier.update(bArr3);
            this.signatureVerifier.update(bArr4);
            this.signatureVerifier.update(bArr5);
            if (this.signatureVerifier.verify(bArr)) {
                return equals ? AuthenticationState.PRESIGNED_AUTH : AuthenticationState.LIVE_AUTH;
            } else {
                return AuthenticationState.BAD_SIGNATURE;
            }
        } catch (SignatureException e2) {
            e = e2;
            str = "Unable to verify signature";
            valueOf = String.valueOf(e.toString());
            throw new ValuablesCryptoException(valueOf.length() == 0 ? str.concat(valueOf) : new String(str), e);
        } catch (InvalidKeyException e3) {
            e = e3;
            str = "Unable to verify signature";
            valueOf = String.valueOf(e.toString());
            if (valueOf.length() == 0) {
            }
            throw new ValuablesCryptoException(valueOf.length() == 0 ? str.concat(valueOf) : new String(str), e);
        }
    }
}
