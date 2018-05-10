package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.common.StatusWords;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GetSmartTapDataResponse extends SessionResponse {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private List<RecordBundle> recordBundles;
    private Set<ServiceObject> serviceObjects;

    static class Builder extends com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionResponse.Builder {
        private com.google.common.collect.ImmutableList.Builder<RecordBundle> recordBundlesBuilder;
        private com.google.common.collect.ImmutableSet.Builder<ServiceObject> serviceObjectsBuilder;

        private Builder() {
            this.serviceObjectsBuilder = ImmutableSet.builder();
            this.recordBundlesBuilder = ImmutableList.builder();
        }

        public Builder addServiceObject(ServiceObject serviceObject) {
            if (serviceObject != null) {
                this.serviceObjectsBuilder.add((Object) serviceObject);
            }
            return this;
        }

        public Builder addRecordBundle(RecordBundle recordBundle) {
            if (recordBundle != null) {
                this.recordBundlesBuilder.add((Object) recordBundle);
            }
            return this;
        }

        public GetSmartTapDataResponse build() throws SmartTapV2Exception {
            boolean z;
            boolean z2 = true;
            SmartTapV2Exception.check(this.sessionId != null, Status.NDEF_FORMAT_ERROR, Code.EXECUTION_FAILURE, "No session ID was set.", new Object[0]);
            if (this.sequenceNumber != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.EXECUTION_FAILURE, "No sequence number was set.", new Object[0]);
            if (this.statusWord == null) {
                z2 = false;
            }
            SmartTapV2Exception.check(z2, Status.NDEF_FORMAT_ERROR, Code.EXECUTION_FAILURE, "No status word was set.", new Object[0]);
            return new GetSmartTapDataResponse(this.sessionId, this.sequenceNumber.byteValue(), this.statusWord, this.serviceObjectsBuilder.build(), this.recordBundlesBuilder.build());
        }
    }

    public static class RecordBundle {
        private final boolean isCompressed;
        private final boolean isEncrypted;
        private final byte[] payload;
        private final byte statusBitmap;

        public RecordBundle(NdefRecord ndefRecord) {
            this(NdefRecords.getByte(ndefRecord), NdefRecords.getAllBytes(ndefRecord, 1));
        }

        public RecordBundle(byte b, byte[] bArr) {
            boolean z;
            boolean z2 = true;
            this.statusBitmap = b;
            if ((b & 1) != 0) {
                z = true;
            } else {
                z = false;
            }
            this.isEncrypted = z;
            if ((b & 2) == 0) {
                z2 = false;
            }
            this.isCompressed = z2;
            this.payload = bArr;
        }

        public boolean isEncrypted() {
            return this.isEncrypted;
        }

        public boolean isCompressed() {
            return this.isCompressed;
        }

        public byte[] getPayload() {
            return this.payload;
        }
    }

    private GetSmartTapDataResponse(byte[] bArr, byte b, StatusWord statusWord, Set<ServiceObject> set, List<RecordBundle> list) {
        super(bArr, b, statusWord);
        this.serviceObjects = set;
        this.recordBundles = list;
    }

    public Set<ServiceObject> getServiceObjects() {
        return this.serviceObjects;
    }

    public List<RecordBundle> getRecordBundles() {
        return this.recordBundles;
    }

    public static GetSmartTapDataResponse parse(byte[] bArr, ServiceObjectConverter serviceObjectConverter, short s) throws SmartTapV2Exception {
        Builder builder = new Builder();
        for (NdefRecord ndefRecord : SessionResponse.decode(bArr, SmartTap2Values.SERVICE_RESPONSE_NDEF_TYPE, s)) {
            byte[] type = NdefRecords.getType(ndefRecord, s);
            if (Arrays.equals(type, SmartTap2Values.SESSION_NDEF_TYPE)) {
                builder.setSession(ndefRecord, s);
            } else if (Arrays.equals(type, SmartTap2Values.getServiceValueNdefType(s))) {
                for (ServiceObject addServiceObject : serviceObjectConverter.convert(ndefRecord, s)) {
                    builder.addServiceObject(addServiceObject);
                }
            } else if (!Arrays.equals(type, SmartTap2Values.RECORD_BUNDLE_NDEF_TYPE)) {
                LOG.w("Unrecognized nested NDEF type %s", SmartTap2Values.getNdefType(type));
            } else if (ndefRecord.getPayload().length > 0) {
                builder.addRecordBundle(new RecordBundle(ndefRecord));
            } else {
                LOG.w("Skipping record bundle with empty payload.", new Object[0]);
            }
        }
        builder.setStatusWord(StatusWords.get(Arrays.copyOfRange(bArr, bArr.length - 2, bArr.length), s));
        return builder.build();
    }
}
