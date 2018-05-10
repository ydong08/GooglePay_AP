package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Issuer;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Request;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Type;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionRequest.DecodedRequest;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.Format;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.ndef.Primitive;
import com.google.android.libraries.commerce.hce.ndef.Text;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Iterator;

public class GetSmartTapDataRequest extends SessionRequest {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final MerchantInfo merchantInfo;
    private final boolean supportsZlib;

    static class Builder extends com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionRequest.Builder {
        private Primitive locationId;
        private MerchantCategory merchantCategory;
        private Long merchantId;
        private Text merchantName;
        private com.google.common.collect.ImmutableSet.Builder<Request> serviceObjectRequestBuilder;
        private boolean supportsZlib;
        private Primitive terminalId;

        private Builder() {
            this.merchantCategory = MerchantCategory.UNKNOWN;
            this.serviceObjectRequestBuilder = ImmutableSet.builder();
        }

        public Builder setMerchantId(Primitive primitive) throws SmartTapV2Exception {
            boolean z = true;
            SmartTapV2Exception.check(primitive != null, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Cannot set null merchant ID.", new Object[0]);
            this.merchantId = primitive.toLong();
            if (this.merchantId == null) {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Failed to parse merchant ID.", new Object[0]);
            return this;
        }

        public Builder setLocationId(Primitive primitive) throws SmartTapV2Exception {
            boolean z;
            if (primitive != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Cannot set null location ID.", new Object[0]);
            this.locationId = primitive;
            return this;
        }

        public Builder setTerminalId(Primitive primitive) throws SmartTapV2Exception {
            boolean z;
            if (primitive != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Cannot set null store ID.", new Object[0]);
            this.terminalId = primitive;
            return this;
        }

        public Builder setMerchantName(Text text) throws SmartTapV2Exception {
            boolean z;
            if (text != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Merchant name primitive does not contain text.", new Object[0]);
            this.merchantName = text;
            return this;
        }

        public Builder setMerchantCategory(Primitive primitive) throws SmartTapV2Exception {
            boolean z = true;
            SmartTapV2Exception.check(primitive != null, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Cannot set null merchant category.", new Object[0]);
            Integer toInt = primitive.toInt();
            if (toInt == null) {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Failed to parse merchant category.", new Object[0]);
            this.merchantCategory = MerchantCategory.get(toInt.intValue());
            return this;
        }

        public Builder addRequestedService(Request request) {
            this.serviceObjectRequestBuilder.add((Object) request);
            return this;
        }

        public Builder setSupportsZlib(boolean z) {
            this.supportsZlib = z;
            return this;
        }

        public GetSmartTapDataRequest build() throws SmartTapV2Exception {
            boolean z;
            boolean z2 = true;
            SmartTapV2Exception.check(this.version != null, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No NDEF version was set.", new Object[0]);
            if (this.sessionId != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No session ID was set.", new Object[0]);
            if (this.sequenceNumber != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.INVALID_SEQUENCE_NUMBER, Code.PARSING_FAILURE, "No sequence number was set.", new Object[0]);
            if (this.merchantId == null) {
                z2 = false;
            }
            SmartTapV2Exception.check(z2, Status.MERCHANT_DATA_MISSING, Code.PARSING_FAILURE, "No merchant ID was set.", new Object[0]);
            return new GetSmartTapDataRequest(this.version.shortValue(), this.sessionId, this.sequenceNumber.byteValue(), new MerchantInfo(this.merchantId.longValue(), this.locationId, this.terminalId, this.merchantName, this.merchantCategory, getDedupedRequests()), this.supportsZlib);
        }

        private ImmutableSet<Request> getDedupedRequests() {
            com.google.common.collect.ImmutableSet.Builder builder = ImmutableSet.builder();
            Iterator it = this.serviceObjectRequestBuilder.build().iterator();
            while (it.hasNext()) {
                Object obj = (Request) it.next();
                if (obj.getType() == Type.ALL) {
                    for (Type type : Type.values()) {
                        if (!(type == Type.ALL || type == Type.ALL_EXCEPT_PPSE)) {
                            builder.add(new Request(type, obj.getIssuer(), obj.getFormat()));
                        }
                    }
                } else if (obj.getType() == Type.ALL_EXCEPT_PPSE) {
                    for (Type type2 : Type.values()) {
                        if (!(type2 == Type.ALL || type2 == Type.ALL_EXCEPT_PPSE || type2 == Type.PPSE)) {
                            builder.add(new Request(type2, obj.getIssuer(), obj.getFormat()));
                        }
                    }
                } else {
                    builder.add(obj);
                }
            }
            return builder.build();
        }
    }

    private GetSmartTapDataRequest(short s, byte[] bArr, byte b, MerchantInfo merchantInfo, boolean z) {
        super(s, bArr, b);
        this.merchantInfo = merchantInfo;
        this.supportsZlib = z;
    }

    public MerchantInfo getMerchantInfo() {
        return this.merchantInfo;
    }

    public static GetSmartTapDataRequest parse(byte[] bArr) throws SmartTapV2Exception {
        Builder builder = new Builder();
        DecodedRequest decode = SessionRequest.decode(bArr, SmartTap2Values.SERVICE_REQUEST_NDEF_TYPE);
        short version = decode.getVersion();
        builder.setVersion(version);
        for (NdefRecord ndefRecord : decode.getNdefRecords()) {
            byte[] type = NdefRecords.getType(ndefRecord, version);
            if (Arrays.equals(type, SmartTap2Values.SESSION_NDEF_TYPE)) {
                builder.setSession(ndefRecord, version);
            } else if (Arrays.equals(type, SmartTap2Values.MERCHANT_NDEF_TYPE)) {
                setMerchantInfo(builder, ndefRecord, version);
            } else if (Arrays.equals(type, SmartTap2Values.SERVICE_LIST_NDEF_TYPE)) {
                setServiceList(builder, ndefRecord, version);
            } else if (Arrays.equals(type, SmartTap2Values.POS_CAPABILITIES_NDEF_TYPE)) {
                setPosCapabilities(builder, ndefRecord, version);
            } else {
                LOG.w("Unrecognized nested NDEF type %s", SmartTap2Values.getNdefType(type));
            }
        }
        return builder.build();
    }

    private static void setMerchantInfo(Builder builder, NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
        Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.MERCHANT_NDEF_TYPE));
        byte[] payload = ndefRecord.getPayload();
        if (payload.length != 0) {
            for (NdefRecord ndefRecord2 : SmartTapV2Exception.tryParseNdefMessage(payload, "merchant info").getRecords()) {
                byte[] type = NdefRecords.getType(ndefRecord2, s);
                Primitive primitive = new Primitive(ndefRecord2);
                if (Arrays.equals(type, SmartTap2Values.MERCHANT_ID_V0_NDEF_TYPE) || Arrays.equals(type, SmartTap2Values.COLLECTOR_ID_NDEF_TYPE)) {
                    builder.setMerchantId(primitive);
                } else if (Arrays.equals(type, SmartTap2Values.LOCATION_ID_NDEF_TYPE)) {
                    builder.setLocationId(primitive);
                } else if (Arrays.equals(type, SmartTap2Values.TERMINAL_ID_NDEF_TYPE)) {
                    builder.setTerminalId(primitive);
                } else if (Arrays.equals(type, SmartTap2Values.MERCHANT_NAME_NDEF_TYPE)) {
                    builder.setMerchantName(primitive.toText());
                } else if (Arrays.equals(type, SmartTap2Values.MERCHANT_CATEGORY_NDEF_TYPE)) {
                    builder.setMerchantCategory(primitive);
                } else if (ndefRecord2.getTnf() == (short) 1 && Arrays.equals(ndefRecord2.getType(), NdefRecord.RTD_TEXT)) {
                    byte[] id = ndefRecord2.getId();
                    if (Arrays.equals(id, SmartTap2Values.LOCATION_ID_NDEF_TYPE)) {
                        builder.setLocationId(primitive);
                    } else if (Arrays.equals(id, SmartTap2Values.TERMINAL_ID_NDEF_TYPE)) {
                        builder.setTerminalId(primitive);
                    } else if (Arrays.equals(id, SmartTap2Values.MERCHANT_NAME_NDEF_TYPE)) {
                        builder.setMerchantName(primitive.toText());
                    } else {
                        LOG.w("Unrecognized NDEF ID %s inside of merchant TEXT record.", SmartTap2Values.getNdefType(id));
                    }
                } else {
                    LOG.w("Unrecognized nested merchant NDEF type %s inside of merchant info of service request.", SmartTap2Values.getNdefType(type));
                }
            }
        }
    }

    private static void setServiceList(Builder builder, NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
        Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.SERVICE_LIST_NDEF_TYPE));
        byte[] payload = ndefRecord.getPayload();
        if (payload.length != 0) {
            for (NdefRecord ndefRecord2 : SmartTapV2Exception.tryParseNdefMessage(payload, "service list").getRecords()) {
                SmartTapV2Exception.check(Arrays.equals(NdefRecords.getType(ndefRecord2, s), SmartTap2Values.SERVICE_TYPE_REQUEST_NDEF_TYPE), Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Service list contains records other than service type: %s", Hex.encodeUpper(NdefRecords.getType(ndefRecord2, s)));
                byte[] payload2 = ndefRecord2.getPayload();
                SmartTapV2Exception.check(payload2.length >= 1, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Service type record contains no type.", new Object[0]);
                Type type = Type.get(payload2[0]);
                Issuer issuer = Issuer.UNSPECIFIED;
                Format format = Format.UNSPECIFIED;
                if (s == (short) 0) {
                    if (payload2.length > 1) {
                        issuer = Issuer.get(payload2[1]);
                    }
                    if (payload2.length > 2) {
                        format = Format.get(payload2[2]);
                    }
                    SmartTapV2Exception.check(payload2.length <= 3, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Service type NDEF record has length longer than 3.", new Object[0]);
                } else {
                    SmartTapV2Exception.check(payload2.length == 1, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Service type NDEF record has length longer than 1.", new Object[0]);
                }
                builder.addRequestedService(new Request(type, issuer, format));
            }
        }
    }

    private static void setPosCapabilities(Builder builder, NdefRecord ndefRecord, short s) {
        Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.POS_CAPABILITIES_NDEF_TYPE));
        int length = ndefRecord.getPayload().length;
        byte[] payload = ndefRecord.getPayload();
        if (length > 0) {
            boolean z;
            if ((payload[0] & 64) != 0) {
                z = true;
            } else {
                z = false;
            }
            builder.setSupportsZlib(z);
        }
        if (length > 5) {
            LOG.w("Unknown extra payload at end of POS Capabilities record. Length: %s.", Integer.valueOf(length));
        }
    }
}
