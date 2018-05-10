package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier.AuthenticationState;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import java.security.interfaces.ECPublicKey;
import java.util.List;
import java.util.Set;

public interface SmartTapCallback {
    Multimap<ByteArrayWrapper, NdefRecord> getAddedNdefRecords();

    ECPublicKey getLongTermPublicKey(long j, int i);

    byte[] getMaxVersion();

    byte[] getMinVersion();

    Set<ByteArrayWrapper> getRemovedNdefRecords();

    Set<ServiceObject> getServiceObjects(MerchantInfo merchantInfo, boolean z, AuthenticationState authenticationState);

    Code processPushBackData(List<ServiceStatus> list, List<NewService> list2, Optional<Long> optional);
}
