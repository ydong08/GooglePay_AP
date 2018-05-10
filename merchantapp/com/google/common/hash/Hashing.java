package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

public final class Hashing {
    private static final int GOOD_FAST_HASH_SEED = ((int) System.currentTimeMillis());

    public static HashFunction hmacSha256(Key key) {
        return new MacHashFunction("HmacSHA256", key, hmacToString("hmacSha256", key));
    }

    public static HashFunction hmacSha256(byte[] bArr) {
        return hmacSha256(new SecretKeySpec((byte[]) Preconditions.checkNotNull(bArr), "HmacSHA256"));
    }

    private static String hmacToString(String str, Key key) {
        return String.format("Hashing.%s(Key[algorithm=%s, format=%s])", new Object[]{str, key.getAlgorithm(), key.getFormat()});
    }

    private Hashing() {
    }
}
