package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.common.collect.Multimap;
import java.util.Set;

public interface SmartTapCallback {
    Multimap<ByteArrayWrapper, NdefRecord> getAddedNdefRecords();

    Set<ByteArrayWrapper> getRemovedNdefRecords();
}
