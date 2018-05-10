package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier.AuthenticationState;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Multimap;
import java.util.Set;
import javax.inject.Inject;

public class NegotiateSmartTapCommand {
    private static final ImmutableMap<AuthenticationState, Code> AUTH_STATE_TO_RESPONSE_CODE = new Builder().put(AuthenticationState.LIVE_AUTH, Code.SUCCESS).put(AuthenticationState.PRESIGNED_AUTH, Code.SUCCESS_PRESIGNED_AUTH).put(AuthenticationState.BAD_SIGNATURE, Code.AUTH_FAILED).put(AuthenticationState.UNKNOWN, Code.AUTH_FAILED).put(AuthenticationState.NO_KEY, Code.AUTH_FAILED).put(AuthenticationState.BAD_KEY, Code.CRYPTO_FAILURE).build();
    private final Multimap<ByteArrayWrapper, NdefRecord> addedNdefRecords;
    private AuthenticationState authenticationState = AuthenticationState.UNKNOWN;
    private boolean encryptionNegotiated = false;
    private int keyVersion = 0;
    private long merchantId = 0;
    private final Set<ByteArrayWrapper> removedNdefRecords;
    private final SmartTap2MerchantVerifier signatureVerifier;
    private final SmartTapCallback smartTapCallback;

    @Inject
    public NegotiateSmartTapCommand(SmartTapCallback smartTapCallback, SmartTap2MerchantVerifier smartTap2MerchantVerifier) {
        this.smartTapCallback = smartTapCallback;
        this.addedNdefRecords = smartTapCallback.getAddedNdefRecords();
        this.removedNdefRecords = smartTapCallback.getRemovedNdefRecords();
        this.signatureVerifier = smartTap2MerchantVerifier;
    }
}
