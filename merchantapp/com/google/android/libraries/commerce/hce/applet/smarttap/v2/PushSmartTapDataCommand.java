package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.common.StatusWords;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import java.util.Set;
import javax.inject.Inject;

public class PushSmartTapDataCommand {
    private final Multimap<ByteArrayWrapper, NdefRecord> addedNdefRecords;
    private final Set<ByteArrayWrapper> removedNdefRecords;
    private final SmartTapCallback smartTapCallback;
    private short version;

    @Inject
    public PushSmartTapDataCommand(SmartTapCallback smartTapCallback) {
        this.smartTapCallback = smartTapCallback;
        this.addedNdefRecords = smartTapCallback.getAddedNdefRecords();
        this.removedNdefRecords = smartTapCallback.getRemovedNdefRecords();
    }

    SmartTapResponse process(byte[] bArr, Optional<Long> optional) throws SmartTapV2Exception {
        PushSmartTapDataRequest parse = PushSmartTapDataRequest.parse(bArr);
        this.version = parse.getVersion();
        return SmartTapResponse.create(82, new SessionResponse(parse.getSessionId(), (byte) (parse.getSequenceNumber() + 1), StatusWords.get(this.smartTapCallback.processPushBackData(parse.getServiceStatuses(), parse.getNewServices(), optional), this.version)).composeSimpleResponse(this.addedNdefRecords, this.removedNdefRecords, SmartTap2Values.PUSH_NDEF_FLAG, SmartTap2Values.PUSH_SERVICE_RESPONSE_NDEF_TYPE, this.version));
    }

    public short getVersion() {
        return this.version;
    }
}
