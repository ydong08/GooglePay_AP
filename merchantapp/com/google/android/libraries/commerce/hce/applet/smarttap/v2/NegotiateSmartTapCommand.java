package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.common.StatusWords;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier.AuthenticationState;
import com.google.android.libraries.commerce.hce.crypto.ValuablesCryptoException;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefMessages;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Bcd;
import com.google.android.libraries.commerce.hce.util.ByteArrays;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Multimap;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;
import java.util.Set;
import javax.inject.Inject;

public class NegotiateSmartTapCommand {
    private static final ImmutableMap<AuthenticationState, Code> AUTH_STATE_TO_RESPONSE_CODE = new Builder().put(AuthenticationState.LIVE_AUTH, Code.SUCCESS).put(AuthenticationState.PRESIGNED_AUTH, Code.SUCCESS_PRESIGNED_AUTH).put(AuthenticationState.BAD_SIGNATURE, Code.AUTH_FAILED).put(AuthenticationState.UNKNOWN, Code.AUTH_FAILED).put(AuthenticationState.NO_KEY, Code.AUTH_FAILED).put(AuthenticationState.BAD_KEY, Code.CRYPTO_FAILURE).build();
    private final Multimap<ByteArrayWrapper, NdefRecord> addedNdefRecords;
    private AuthenticationState authenticationState = AuthenticationState.UNKNOWN;
    private byte[] encodedMerchantId;
    private boolean encryptionNegotiated = false;
    private byte[] handsetNonce;
    private int keyVersion = 0;
    private long merchantId = 0;
    private final Set<ByteArrayWrapper> removedNdefRecords;
    private byte[] signature;
    private final SmartTap2MerchantVerifier signatureVerifier;
    private final SmartTapCallback smartTapCallback;
    private byte[] terminalEphemeralPublicKey;
    private byte[] terminalNonce;
    private short version;

    @Inject
    public NegotiateSmartTapCommand(SmartTapCallback smartTapCallback, SmartTap2MerchantVerifier smartTap2MerchantVerifier) {
        this.smartTapCallback = smartTapCallback;
        this.addedNdefRecords = smartTapCallback.getAddedNdefRecords();
        this.removedNdefRecords = smartTapCallback.getRemovedNdefRecords();
        this.signatureVerifier = smartTap2MerchantVerifier;
    }

    SmartTapResponse process(byte[] bArr, byte[] bArr2, byte[] bArr3) throws SmartTapV2Exception {
        byte[] bArr4;
        NegotiateSmartTapRequest parse = NegotiateSmartTapRequest.parse(bArr);
        this.handsetNonce = bArr2;
        this.version = parse.getVersion();
        this.merchantId = parse.getMerchantId();
        if (this.version == (short) 0) {
            this.encodedMerchantId = Bcd.encodeWithPadding(this.merchantId, 4);
        } else {
            boolean z;
            byte[] encodedMerchantId = parse.getEncodedMerchantId();
            int length = encodedMerchantId.length;
            if (length > 4) {
                this.encodedMerchantId = Arrays.copyOfRange(encodedMerchantId, length - 4, length);
            } else {
                this.encodedMerchantId = ByteArrays.updatePayloadLength(encodedMerchantId, 4);
            }
            if (this.encodedMerchantId != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.INVALID_CRYPTO_INPUT, "Unable to parse merchant ID", new Object[0]);
        }
        this.terminalEphemeralPublicKey = parse.getEphemeralPublicKey();
        if (parse.getShouldLiveAuthenticate()) {
            bArr4 = bArr2;
        } else {
            bArr4 = new byte[32];
        }
        this.terminalNonce = parse.getTerminalNonce();
        this.encryptionNegotiated = true;
        this.signature = parse.getSignature();
        this.keyVersion = parse.getLongTermKeyVersion();
        ECPublicKey longTermPublicKey = this.smartTapCallback.getLongTermPublicKey(this.merchantId, this.keyVersion);
        if (longTermPublicKey != null) {
            try {
                this.authenticationState = this.signatureVerifier.verifySignature(longTermPublicKey, this.signature, this.terminalNonce, bArr4, this.encodedMerchantId, this.terminalEphemeralPublicKey);
            } catch (ValuablesCryptoException e) {
                this.authenticationState = AuthenticationState.BAD_KEY;
            }
        } else {
            this.authenticationState = AuthenticationState.NO_KEY;
        }
        StatusWord statusWord = StatusWords.get((Code) AUTH_STATE_TO_RESPONSE_CODE.get(this.authenticationState), this.version);
        NdefRecord composeNdef = new Session(parse.getSessionId(), (byte) (parse.getSequenceNumber() + 1)).composeNdef(this.version);
        NdefRecord compose = NdefRecords.compose(SmartTap2Values.HANDSET_EPHEMERAL_PUBLIC_KEY_NDEF_TYPE, bArr3, this.version);
        composeNdef = NdefRecords.compose(SmartTap2Values.NEGOTIATE_RESPONSE_NDEF_TYPE, NdefMessages.compose(this.addedNdefRecords, this.removedNdefRecords, SmartTap2Values.NEGOTIATE_RESPONSE_NDEF_TYPE, this.version, composeNdef, compose).toByteArray(), this.version);
        return SmartTapResponse.create(83, ResponseApdu.fromDataAndStatusWord(NdefMessages.compose(this.addedNdefRecords, this.removedNdefRecords, SmartTap2Values.NEGOTIATE_NDEF_FLAG, this.version, composeNdef).toByteArray(), statusWord), this.authenticationState);
    }

    public short getVersion() {
        return this.version;
    }

    public long getMerchantId() {
        return this.merchantId;
    }

    public boolean getEncryptionNegotiated() {
        return this.encryptionNegotiated;
    }

    public byte[] getTerminalEphemeralPublicKey() {
        return this.terminalEphemeralPublicKey;
    }

    public byte[] getTerminalNonce() {
        return this.terminalNonce;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public byte[] getEncodedMerchantId() {
        return this.encodedMerchantId;
    }

    public int getKeyVersion() {
        return this.keyVersion;
    }

    public AuthenticationState getAuthenticationState() {
        return this.authenticationState;
    }
}
