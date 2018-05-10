package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.HceApplet;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.common.StatusWords;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2Encryptor;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier.AuthenticationState;
import com.google.android.libraries.commerce.hce.crypto.Version0EncryptionParameters;
import com.google.android.libraries.commerce.hce.crypto.Version1EncryptionParameters;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.Format;
import com.google.android.libraries.commerce.hce.ndef.NdefMessages;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Compressor;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Shorts;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Set;
import javax.inject.Inject;

public class SmartTapApplet implements HceApplet {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private static final byte[] SELECT_COMMAND = Aid.SMART_TAP_AID_V2_0.getSelectCommand();
    private final Multimap<ByteArrayWrapper, NdefRecord> addedNdefRecords;
    private AuthenticationState authenticationState = AuthenticationState.NOT_AUTHENTICATED;
    private final Compressor compressor;
    private byte[] encodedMerchantId;
    private final SmartTap2Encryptor encryptor;
    private final GetSmartTapDataCommand getSmartTapDataCommand;
    private byte[] handsetNonce = new byte[32];
    private Optional<Integer> keyVersionOptional;
    private Optional<Long> merchantIdOptional;
    private byte[] negotiateCommandBytes;
    private final NegotiateSmartTapCommand negotiateSmartTapCommand;
    private final PushSmartTapDataCommand pushSmartTapDataCommand;
    private final SecureRandom random;
    private final Set<ByteArrayWrapper> removedNdefRecords;
    private byte[] signature;
    private final SmartTapCallback smartTapCallback;
    private byte[] terminalEphemeralPublicKeyBytes;
    private byte[] terminalNonce;
    private Optional<Short> versionOptional;

    @Inject
    public SmartTapApplet(SmartTapCallback smartTapCallback, NegotiateSmartTapCommand negotiateSmartTapCommand, GetSmartTapDataCommand getSmartTapDataCommand, PushSmartTapDataCommand pushSmartTapDataCommand, SecureRandom secureRandom, SmartTap2Encryptor smartTap2Encryptor, Compressor compressor) {
        this.smartTapCallback = smartTapCallback;
        this.addedNdefRecords = smartTapCallback.getAddedNdefRecords();
        this.removedNdefRecords = smartTapCallback.getRemovedNdefRecords();
        this.negotiateSmartTapCommand = negotiateSmartTapCommand;
        this.getSmartTapDataCommand = getSmartTapDataCommand;
        this.pushSmartTapDataCommand = pushSmartTapDataCommand;
        this.random = secureRandom;
        this.encryptor = smartTap2Encryptor;
        this.compressor = compressor;
        reset();
    }

    public SmartTapResponse processCommand(byte[] bArr, boolean z) throws SmartTapV2Exception {
        LOG.d("SmartTap v2.0 command: %s.", Hex.encode(bArr));
        byte b = bArr[0];
        byte b2 = bArr[1];
        if (b == (byte) 0 && b2 == (byte) -92) {
            if (Arrays.equals(SELECT_COMMAND, bArr)) {
                return getSelectSmartTapResponse(b2);
            }
            return SmartTapResponse.create(b2, ResponseApdu.fromStatusWord(Iso7816StatusWord.FILE_NOT_FOUND));
        } else if (b != (byte) -112) {
            return SmartTapResponse.create(b2, ResponseApdu.fromStatusWord(Iso7816StatusWord.CLA_NOT_SUPPORTED));
        } else {
            SmartTapResponse moreData;
            switch (b2) {
                case (byte) -64:
                    try {
                        moreData = this.getSmartTapDataCommand.getMoreData(bArr);
                        if (this.getSmartTapDataCommand.getVersion() != (short) 0) {
                            return moreData;
                        }
                        setOrConfirmVersion(this.getSmartTapDataCommand.getVersion());
                        return moreData;
                    } catch (Throwable e) {
                        throw new SmartTapV2Exception(Status.UNKNOWN, Code.CRYPTO_FAILURE, "Unable to encrypt payload for GetMoreData", e);
                    }
                case (byte) 80:
                    try {
                        moreData = this.getSmartTapDataCommand.processCommand(bArr, this.encryptor, this.compressor, this.authenticationState, z);
                        setOrConfirmVersion(this.getSmartTapDataCommand.getVersion());
                        setOrConfirmMerchant(Long.valueOf(this.getSmartTapDataCommand.getMerchantInfo().getMerchantId()));
                        return moreData;
                    } catch (Throwable e2) {
                        throw new SmartTapV2Exception(Status.UNKNOWN, Code.CRYPTO_FAILURE, "Unable to encrypt payload for GetData", e2);
                    }
                case (byte) 82:
                    moreData = this.pushSmartTapDataCommand.process(bArr, this.merchantIdOptional);
                    setOrConfirmVersion(this.pushSmartTapDataCommand.getVersion());
                    return moreData;
                case (byte) 83:
                    SmartTapResponse process = this.negotiateSmartTapCommand.process(bArr, this.handsetNonce, this.encryptor.getEphemeralPublicKey());
                    setOrConfirmVersion(this.negotiateSmartTapCommand.getVersion());
                    setOrConfirmMerchant(Long.valueOf(this.negotiateSmartTapCommand.getMerchantId()));
                    this.terminalEphemeralPublicKeyBytes = this.negotiateSmartTapCommand.getTerminalEphemeralPublicKey();
                    this.terminalNonce = this.negotiateSmartTapCommand.getTerminalNonce();
                    this.keyVersionOptional = Optional.of(Integer.valueOf(this.negotiateSmartTapCommand.getKeyVersion()));
                    this.encodedMerchantId = this.negotiateSmartTapCommand.getEncodedMerchantId();
                    this.authenticationState = this.negotiateSmartTapCommand.getAuthenticationState();
                    if (this.negotiateSmartTapCommand.getEncryptionNegotiated()) {
                        try {
                            short shortValue = ((Short) getVersion().get()).shortValue();
                            if (shortValue == (short) 0) {
                                this.negotiateCommandBytes = bArr;
                                this.encryptor.setCryptoParams(new Version0EncryptionParameters(this.terminalEphemeralPublicKeyBytes, this.handsetNonce, this.negotiateCommandBytes));
                            } else if (shortValue >= (short) 1) {
                                this.signature = this.negotiateSmartTapCommand.getSignature();
                                this.encryptor.setCryptoParams(new Version1EncryptionParameters(this.terminalEphemeralPublicKeyBytes, this.handsetNonce, this.terminalNonce, this.encodedMerchantId, this.terminalEphemeralPublicKeyBytes, this.signature));
                            }
                        } catch (Throwable e22) {
                            throw new SmartTapV2Exception(Status.UNKNOWN, Code.CRYPTO_FAILURE, "Unable to generate shared key", e22);
                        }
                    }
                    return process;
                default:
                    LOG.w("Unknown INS value: %s", Byte.valueOf(b2));
                    if (this.versionOptional.isPresent()) {
                        return SmartTapResponse.create(b2, ResponseApdu.fromStatusWord(StatusWords.get(Code.UNKNOWN_TERMINAL_COMMAND, ((Short) this.versionOptional.get()).shortValue())));
                    }
                    return SmartTapResponse.create(b2, ResponseApdu.fromStatusWord(Iso7816StatusWord.INS_NOT_SUPPORTED));
            }
        }
    }

    public Optional<Short> getVersion() {
        return this.versionOptional;
    }

    public byte[] getHandsetNonce() {
        return Arrays.copyOf(this.handsetNonce, this.handsetNonce.length);
    }

    public void reset() {
        this.versionOptional = Optional.absent();
        this.merchantIdOptional = Optional.absent();
        this.keyVersionOptional = Optional.absent();
        this.random.nextBytes(this.handsetNonce);
        this.encryptor.reset();
    }

    private SmartTapResponse getSelectSmartTapResponse(byte b) {
        boolean z;
        byte[] minVersion = this.smartTapCallback.getMinVersion();
        byte[] maxVersion = this.smartTapCallback.getMaxVersion();
        Preconditions.checkArgument(minVersion.length == 2);
        if (maxVersion.length == 2) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z);
        short fromByteArray = Shorts.fromByteArray(minVersion);
        short fromByteArray2 = Shorts.fromByteArray(maxVersion);
        if (fromByteArray <= fromByteArray2) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z);
        if (fromByteArray < SmartTap2Values.SMARTTAP_MIN_VERSION_SHORT) {
            LOG.w("Specified smarttap min version %s is less than library supported min version %s.", Hex.encodeUpper(minVersion), Hex.encodeUpper(SmartTap2Values.SMARTTAP_MIN_VERSION));
        }
        if (fromByteArray2 > SmartTap2Values.SMARTTAP_MAX_VERSION_SHORT) {
            LOG.w("Specified smarttap max version %s is greater than library supported max version %s.", Hex.encodeUpper(maxVersion), Hex.encodeUpper(SmartTap2Values.SMARTTAP_MAX_VERSION));
        }
        r0 = new byte[2][];
        r0[0] = new byte[]{Format.BINARY.value()};
        r0[1] = this.handsetNonce;
        NdefRecord ndefRecord = new NdefRecord((short) 4, SmartTap2Values.HANDSET_NONCE_NDEF_TYPE, SmartTap2Values.HANDSET_NONCE_NDEF_TYPE, Bytes.concat(r0));
        NdefMessage compose = NdefMessages.compose(this.addedNdefRecords, this.removedNdefRecords, SmartTap2Values.SELECT_NDEF_FLAG, SmartTap2Values.SMARTTAP_MAX_VERSION_SHORT, ndefRecord);
        return SmartTapResponse.create(b, ResponseApdu.fromDataAndStatusWord(Bytes.concat(minVersion, maxVersion, compose.toByteArray()), Iso7816StatusWord.NO_ERROR));
    }

    private void setOrConfirmVersion(short s) throws SmartTapV2Exception {
        if (!this.versionOptional.isPresent()) {
            byte[] minVersion = this.smartTapCallback.getMinVersion();
            byte[] maxVersion = this.smartTapCallback.getMaxVersion();
            short fromByteArray = Shorts.fromByteArray(minVersion);
            short fromByteArray2 = Shorts.fromByteArray(maxVersion);
            if (fromByteArray > s || fromByteArray2 < s) {
                throw new SmartTapV2Exception(Status.UNSUPPORTED_VERSION, Code.VERSION_NOT_SUPPORTED, String.format("Requested version is not supported. Min version: %s. Max version: %s. Requested version: %s.", new Object[]{Short.valueOf(fromByteArray), Short.valueOf(fromByteArray2), Short.valueOf(s)}));
            }
            this.versionOptional = Optional.of(Short.valueOf(s));
        } else if (!((Short) this.versionOptional.get()).equals(Short.valueOf(s))) {
            throw new SmartTapV2Exception(Status.UNSUPPORTED_VERSION, Code.VERSION_NOT_SUPPORTED, String.format("Provided two different protocol versions. Version 1: %s Version 2: %s.", new Object[]{this.versionOptional.get(), Short.valueOf(s)}));
        }
    }

    private void setOrConfirmMerchant(Long l) throws SmartTapV2Exception {
        if (!this.merchantIdOptional.isPresent()) {
            this.merchantIdOptional = Optional.of(l);
        } else if (!((Long) this.merchantIdOptional.get()).equals(l)) {
            Status status = Status.UNSUPPORTED_MERCHANT;
            Code code = Code.TOO_MANY_REQUESTS;
            String valueOf = String.valueOf(this.merchantIdOptional.get());
            String valueOf2 = String.valueOf(l);
            throw new SmartTapV2Exception(status, code, new StringBuilder((String.valueOf(valueOf).length() + 50) + String.valueOf(valueOf2).length()).append("Provided two different merchant IDs. ID 1: ").append(valueOf).append(" ID 2: ").append(valueOf2).toString());
        }
    }
}
