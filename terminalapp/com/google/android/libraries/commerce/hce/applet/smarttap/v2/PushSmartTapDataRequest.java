package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionRequest.DecodedRequest;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PushSmartTapDataRequest extends SessionRequest {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private List<NewService> newServices;
    private List<ServiceStatus> serviceStatuses;

    static class Builder extends com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionRequest.Builder {
        private List<NewService> newServices;
        private List<ServiceStatus> serviceStatuses;

        private Builder() {
            this.serviceStatuses = new ArrayList();
            this.newServices = new ArrayList();
        }

        public Builder addServiceStatus(ServiceStatus serviceStatus) {
            this.serviceStatuses.add(serviceStatus);
            return this;
        }

        public Builder addNewService(NewService newService) {
            this.newServices.add(newService);
            return this;
        }

        public PushSmartTapDataRequest build() throws SmartTapV2Exception {
            boolean z;
            boolean z2 = true;
            SmartTapV2Exception.check(this.version != null, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No NDEF version was set.", new Object[0]);
            if (this.sessionId != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No session ID was set.", new Object[0]);
            if (this.sequenceNumber == null) {
                z2 = false;
            }
            SmartTapV2Exception.check(z2, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No sequence number was set.", new Object[0]);
            return new PushSmartTapDataRequest(this.version.shortValue(), this.sessionId, this.sequenceNumber.byteValue(), this.serviceStatuses, this.newServices);
        }
    }

    private PushSmartTapDataRequest(short s, byte[] bArr, byte b, List<ServiceStatus> list, List<NewService> list2) {
        super(s, bArr, b);
        this.serviceStatuses = list;
        this.newServices = list2;
    }

    public List<ServiceStatus> getServiceStatuses() {
        return this.serviceStatuses;
    }

    public List<NewService> getNewServices() {
        return this.newServices;
    }

    public static PushSmartTapDataRequest parse(byte[] bArr) throws SmartTapV2Exception {
        Builder builder = new Builder();
        DecodedRequest decode = SessionRequest.decode(bArr, SmartTap2Values.PUSH_SERVICE_NDEF_TYPE);
        short version = decode.getVersion();
        builder.setVersion(version);
        for (NdefRecord ndefRecord : decode.getNdefRecords()) {
            byte[] type = NdefRecords.getType(ndefRecord, version);
            if (Arrays.equals(type, SmartTap2Values.SESSION_NDEF_TYPE)) {
                builder.setSession(ndefRecord, version);
            } else if (Arrays.equals(type, SmartTap2Values.SERVICE_STATUS_NDEF_TYPE)) {
                builder.addServiceStatus(ServiceStatus.parseNdef(ndefRecord, version));
            } else if (Arrays.equals(type, SmartTap2Values.NEW_SERVICE_NDEF_TYPE)) {
                builder.addNewService(NewService.parseNdef(ndefRecord, version));
            } else {
                LOG.w("Unrecognized nested ndef ID %s", SmartTap2Values.getNdefType(type));
            }
        }
        return builder.build();
    }
}
