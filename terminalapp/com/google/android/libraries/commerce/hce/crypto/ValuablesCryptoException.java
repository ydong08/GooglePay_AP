package com.google.android.libraries.commerce.hce.crypto;

import java.security.GeneralSecurityException;

public class ValuablesCryptoException extends GeneralSecurityException {
    public ValuablesCryptoException(String str) {
        super(str);
    }

    public ValuablesCryptoException(String str, Throwable th) {
        super(str, th);
    }
}
