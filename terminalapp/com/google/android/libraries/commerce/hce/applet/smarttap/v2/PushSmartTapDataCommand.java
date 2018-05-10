package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.common.collect.Multimap;
import java.util.Set;
import javax.inject.Inject;

public class PushSmartTapDataCommand {
    private final Multimap<ByteArrayWrapper, NdefRecord> addedNdefRecords;
    private final Set<ByteArrayWrapper> removedNdefRecords;
    private final SmartTapCallback smartTapCallback;

    @Inject
    public PushSmartTapDataCommand(SmartTapCallback smartTapCallback) {
        this.smartTapCallback = smartTapCallback;
        this.addedNdefRecords = smartTapCallback.getAddedNdefRecords();
        this.removedNdefRecords = smartTapCallback.getRemovedNdefRecords();
    }
}
