package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.common.StatusWords;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2Encryptor;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier.AuthenticationState;
import com.google.android.libraries.commerce.hce.crypto.ValuablesCryptoException;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefMessages;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GetDataVersion0 implements GetData {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final Multimap<ByteArrayWrapper, NdefRecord> addedNdefRecords;
    private final AuthenticationState authenticationState;
    private final SmartTap2Encryptor encryptor;
    private List<ProcessedServiceObject> remainingServices;
    private final Set<ByteArrayWrapper> removedNdefRecords;
    private final byte[] sessionId;
    private final boolean unlockRequired;
    private final boolean useEncryption;
    private final short version;

    static class ProcessedServiceObject {
        private NdefRecord record;
        private ServiceObject serviceObject;
        private int size;

        ProcessedServiceObject(ServiceObject serviceObject, NdefRecord ndefRecord, int i) {
            this.serviceObject = serviceObject;
            this.record = ndefRecord;
            this.size = i;
        }

        ServiceObject serviceObject() {
            return this.serviceObject;
        }

        NdefRecord record() {
            return this.record;
        }

        int size() {
            return this.size;
        }

        public String toString() {
            return this.serviceObject.toString();
        }
    }

    public GetDataVersion0(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, byte[] bArr, short s, AuthenticationState authenticationState, boolean z, SmartTap2Encryptor smartTap2Encryptor, Set<ServiceObject> set2, boolean z2) {
        this.addedNdefRecords = multimap;
        this.removedNdefRecords = set;
        this.sessionId = bArr;
        this.version = s;
        this.authenticationState = authenticationState;
        this.useEncryption = z;
        this.encryptor = smartTap2Encryptor;
        this.unlockRequired = z2;
        initializeRemainingServices(set2);
    }

    public SmartTapResponse getDataResponse(byte b) throws ValuablesCryptoException {
        return getSmartTapResponse(b, 80);
    }

    public SmartTapResponse getMoreDataResponse(byte b) throws ValuablesCryptoException {
        return getSmartTapResponse(b, -64);
    }

    public boolean hasMoreData() {
        return !this.remainingServices.isEmpty();
    }

    private void initializeRemainingServices(Set<ServiceObject> set) {
        this.remainingServices = new ArrayList();
        for (ServiceObject serviceObject : set) {
            Optional toNdef = serviceObject.toNdef(this.addedNdefRecords, this.removedNdefRecords, this.version);
            if (toNdef.isPresent()) {
                NdefRecord ndefRecord = (NdefRecord) toNdef.get();
                this.remainingServices.add(new ProcessedServiceObject(serviceObject, ndefRecord, getServiceObjectNdefLength(serviceObject, ndefRecord.getPayload().length)));
            } else {
                LOG.w("Skipping current service object since NDEF record composition failed.", new Object[0]);
            }
        }
        Collections.sort(this.remainingServices, new Comparator<ProcessedServiceObject>(this) {
            public int compare(ProcessedServiceObject processedServiceObject, ProcessedServiceObject processedServiceObject2) {
                return Integer.compare(processedServiceObject2.size(), processedServiceObject.size());
            }
        });
    }

    private NdefRecord createEncryptedNdefRecord(Collection<NdefRecord> collection) throws ValuablesCryptoException {
        return NdefRecords.compose(SmartTap2Values.ENCRYPTED_SERVICE_VALUE_NDEF_TYPE, this.encryptor.encryptMessage(NdefMessages.compose(this.addedNdefRecords, this.removedNdefRecords, SmartTap2Values.ENCRYPTED_SERVICE_VALUE_NDEF_TYPE, this.version, (Collection) collection).toByteArray()), this.version);
    }

    private SmartTapResponse getSmartTapResponse(byte b, int i) throws ValuablesCryptoException {
        NdefRecord composeNdef = new Session(this.sessionId, b).composeNdef(this.version);
        Collection arrayList = new ArrayList();
        arrayList.add(composeNdef);
        int length = (composeNdef.getPayload().length + SmartTap2Values.SESSION_V0_NDEF_HEADER_SIZE) + SmartTap2Values.SERVICE_RESPONSE_V0_NDEF_HEADER_SIZE;
        if (this.useEncryption) {
            length += SmartTap2Values.ENCRYPTION_V0_OVERHEAD;
        }
        Collection arrayList2 = new ArrayList();
        Set hashSet = new HashSet();
        Iterator it = this.remainingServices.iterator();
        int i2 = length;
        while (it.hasNext()) {
            ProcessedServiceObject processedServiceObject = (ProcessedServiceObject) it.next();
            if (processedServiceObject.size() + i2 <= 255) {
                arrayList2.add(processedServiceObject.record());
                hashSet.add(processedServiceObject.serviceObject());
                i2 += processedServiceObject.size();
                it.remove();
                length = i2;
            } else {
                if (arrayList2.isEmpty()) {
                    LOG.w("Skipped current service record since it does not fit in single APDU. Service Object: %s. Size: %s", processedServiceObject.serviceObject(), Integer.valueOf(processedServiceObject.size()));
                    it.remove();
                }
                length = i2;
            }
            i2 = length;
        }
        if (!arrayList2.isEmpty()) {
            if (this.useEncryption) {
                arrayList.add(createEncryptedNdefRecord(arrayList2));
            } else {
                arrayList.addAll(arrayList2);
            }
        }
        StatusWord statusWord = StatusWords.get(Code.SUCCESS, (short) 0);
        if (!this.remainingServices.isEmpty()) {
            statusWord = StatusWords.get(Code.SUCCESS_MORE_PAYLOAD, (short) 0);
        }
        NdefRecord compose = NdefRecords.compose(SmartTap2Values.SERVICE_RESPONSE_NDEF_TYPE, NdefMessages.compose(this.addedNdefRecords, this.removedNdefRecords, SmartTap2Values.SERVICE_RESPONSE_NDEF_TYPE, this.version, arrayList).toByteArray(), this.version);
        return SmartTapResponse.create(i, ResponseApdu.fromDataAndStatusWord(NdefMessages.compose(this.addedNdefRecords, this.removedNdefRecords, SmartTap2Values.GET_DATA_NDEF_FLAG, this.version, compose).toByteArray(), statusWord), hashSet, this.authenticationState, this.useEncryption, this.unlockRequired);
    }

    private int getServiceObjectNdefLength(ServiceObject serviceObject, int i) {
        if (i > 255) {
            return Integer.MAX_VALUE;
        }
        switch (serviceObject.type()) {
            case LOYALTY:
                return SmartTap2Values.LOYALTY_V0_NDEF_HEADER_SIZE + i;
            case OFFER:
                return SmartTap2Values.OFFER_V0_NDEF_HEADER_SIZE + i;
            case GIFT_CARD:
                return SmartTap2Values.GIFT_CARD_V0_NDEF_HEADER_SIZE + i;
            case WALLET_CUSTOMER:
                return SmartTap2Values.CUSTOMER_V0_NDEF_HEADER_SIZE + i;
            case CLOSED_LOOP_CARD:
                return SmartTap2Values.PLC_V0_NDEF_HEADER_SIZE + i;
            default:
                throw new IllegalArgumentException("Invalid service object type");
        }
    }
}
