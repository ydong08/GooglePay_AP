package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2Encryptor;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier.AuthenticationState;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Compressor;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import java.security.SecureRandom;
import java.util.Set;
import javax.inject.Inject;

public class SmartTapApplet {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private static final byte[] SELECT_COMMAND = Aid.SMART_TAP_AID_V2_0.getSelectCommand();
    private final Multimap<ByteArrayWrapper, NdefRecord> addedNdefRecords;
    private AuthenticationState authenticationState = AuthenticationState.NOT_AUTHENTICATED;
    private final Compressor compressor;
    private final SmartTap2Encryptor encryptor;
    private final GetSmartTapDataCommand getSmartTapDataCommand;
    private byte[] handsetNonce = new byte[32];
    private Optional<Integer> keyVersionOptional;
    private Optional<Long> merchantIdOptional;
    private final NegotiateSmartTapCommand negotiateSmartTapCommand;
    private final PushSmartTapDataCommand pushSmartTapDataCommand;
    private final SecureRandom random;
    private final Set<ByteArrayWrapper> removedNdefRecords;
    private final SmartTapCallback smartTapCallback;
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

    public void reset() {
        this.versionOptional = Optional.absent();
        this.merchantIdOptional = Optional.absent();
        this.keyVersionOptional = Optional.absent();
        this.random.nextBytes(this.handsetNonce);
        this.encryptor.reset();
    }
}
