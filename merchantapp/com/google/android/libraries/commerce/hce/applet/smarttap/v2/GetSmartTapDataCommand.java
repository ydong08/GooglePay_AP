package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.common.ResponseApdus;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2Encryptor;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier.AuthenticationState;
import com.google.android.libraries.commerce.hce.crypto.ValuablesCryptoException;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Compressor;
import com.google.common.collect.Multimap;
import java.util.Set;
import javax.inject.Inject;

public class GetSmartTapDataCommand {
    private final Multimap<ByteArrayWrapper, NdefRecord> addedNdefRecords;
    private GetData getData;
    private MerchantInfo merchantInfo;
    private final Set<ByteArrayWrapper> removedNdefRecords;
    private byte sequenceNumber;
    private byte[] sessionId;
    private final SmartTapCallback smartTapCallback;
    private boolean tryCompression;
    private boolean useEncryption = false;
    private short version;

    @Inject
    public GetSmartTapDataCommand(SmartTapCallback smartTapCallback) {
        this.smartTapCallback = smartTapCallback;
        this.addedNdefRecords = smartTapCallback.getAddedNdefRecords();
        this.removedNdefRecords = smartTapCallback.getRemovedNdefRecords();
    }

    public SmartTapResponse processCommand(byte[] bArr, SmartTap2Encryptor smartTap2Encryptor, Compressor compressor, AuthenticationState authenticationState, boolean z) throws SmartTapV2Exception, ValuablesCryptoException {
        GetSmartTapDataRequest parse = GetSmartTapDataRequest.parse(bArr);
        this.useEncryption = smartTap2Encryptor.isInitialized();
        this.tryCompression = parse.getSupportsZlib();
        this.sessionId = parse.getSessionId();
        this.sequenceNumber = (byte) (parse.getSequenceNumber() + 1);
        this.version = parse.getVersion();
        this.merchantInfo = parse.getMerchantInfo();
        Set<ServiceObject> serviceObjects = this.smartTapCallback.getServiceObjects(this.merchantInfo, this.useEncryption, authenticationState);
        boolean z2 = false;
        for (ServiceObject unlockRequired : serviceObjects) {
            if (unlockRequired.unlockRequired()) {
                z2 = true;
                break;
            }
        }
        if (this.version == (short) 0) {
            this.getData = new GetDataVersion0(this.addedNdefRecords, this.removedNdefRecords, this.sessionId, this.version, authenticationState, this.useEncryption, smartTap2Encryptor, serviceObjects, z2);
        } else {
            this.getData = new GetDataVersion1(this.addedNdefRecords, this.removedNdefRecords, this.sessionId, this.sequenceNumber, this.version, authenticationState, this.useEncryption, this.tryCompression, smartTap2Encryptor, compressor, serviceObjects, z2, z);
        }
        return this.getData.getDataResponse(this.sequenceNumber);
    }

    SmartTapResponse getMoreData(byte[] bArr) throws SmartTapV2Exception, ValuablesCryptoException {
        GetAdditionalSmartTapDataRequest parse = GetAdditionalSmartTapDataRequest.parse(bArr);
        this.version = parse.getVersion();
        if (this.version == (short) 0) {
            this.sequenceNumber = (byte) (parse.getSequenceNumber() + 1);
        }
        if (this.getData == null || !this.getData.hasMoreData()) {
            return SmartTapResponse.create(-64, ResponseApdus.get(Code.REQUEST_MORE_NOT_APPLICABLE, this.version));
        }
        return this.getData.getMoreDataResponse(this.sequenceNumber);
    }

    public short getVersion() {
        return this.version;
    }

    public MerchantInfo getMerchantInfo() {
        return this.merchantInfo;
    }
}
