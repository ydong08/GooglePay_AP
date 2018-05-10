package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefMessages;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.ndef.Primitive;
import com.google.android.libraries.commerce.hce.ndef.Text;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Bytes;
import java.util.Arrays;
import java.util.Set;

public class NewService {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final Text title;
    private final ServiceType type;
    private final Uri uri;

    static class Builder {
        private Text title;
        private ServiceType type;
        private Uri uri;

        private Builder() {
        }

        public Builder setType(ServiceType serviceType) {
            this.type = serviceType;
            return this;
        }

        public Builder setTitle(NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
            Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.NEW_SERVICE_TITLE_NDEF_TYPE));
            return setTitle(new Primitive(ndefRecord).toText());
        }

        public Builder setTitle(Text text) throws SmartTapV2Exception {
            boolean z;
            if (text != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Unable to extract title from NDEF record for new service.", new Object[0]);
            this.title = text;
            return this;
        }

        public Builder setUri(NdefRecord ndefRecord) throws SmartTapV2Exception {
            boolean z = true;
            Preconditions.checkArgument(Arrays.equals(ndefRecord.getId(), SmartTap2Values.NEW_SERVICE_URI_NDEF_TYPE));
            if (ndefRecord.getTnf() != (short) 1) {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "URI NDEF record does not have TNF value TNF_WELL_KNOWN.", new Object[0]);
            SmartTapV2Exception.check(Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_URI), Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "URI NDEF record does not have type RTD_URI.", new Object[0]);
            this.uri = ndefRecord.toUri();
            return this;
        }

        public NewService build() throws SmartTapV2Exception {
            boolean z;
            boolean z2 = true;
            if (this.title != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No title was set.", new Object[0]);
            if (this.uri == null) {
                z2 = false;
            }
            SmartTapV2Exception.check(z2, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No uri was set.", new Object[0]);
            return new NewService(this.type, this.title, this.uri);
        }
    }

    public enum ServiceType {
        UNSPECIFIED((byte) 0),
        VALUABLE((byte) 1),
        RECEIPT((byte) 2),
        SURVEY((byte) 3),
        GOODS((byte) 4),
        SIGNUP((byte) 5);
        
        private final byte value;

        private ServiceType(byte b) {
            this.value = b;
        }

        public byte value() {
            return this.value;
        }

        public static ServiceType get(byte b) {
            for (ServiceType serviceType : values()) {
                if (b == serviceType.value()) {
                    return serviceType;
                }
            }
            throw new RuntimeException("Byte " + b + " was not a backing value for NewService.ServiceType.");
        }
    }

    public NewService(ServiceType serviceType, String str, Uri uri) {
        this(serviceType, new Text(str), uri);
    }

    public NewService(ServiceType serviceType, Text text, Uri uri) {
        this.type = serviceType;
        this.title = text;
        this.uri = uri;
    }

    public ServiceType getServiceType() {
        return this.type;
    }

    public Text getTitle() {
        return this.title;
    }

    public Uri getUri() {
        return this.uri;
    }

    public NdefRecord toNdef(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, short s) {
        NdefRecord createText = NdefRecords.createText(SmartTap2Values.NEW_SERVICE_TITLE_NDEF_TYPE, this.title.getLanguageCode(), this.title.getText(), s);
        NdefRecord createUri = NdefRecords.createUri(SmartTap2Values.NEW_SERVICE_URI_NDEF_TYPE, this.uri);
        NdefMessage compose = NdefMessages.compose(multimap, set, SmartTap2Values.NEW_SERVICE_NDEF_TYPE, s, createText, createUri);
        byte[] bArr = SmartTap2Values.NEW_SERVICE_NDEF_TYPE;
        r2 = new byte[2][];
        r2[0] = new byte[]{this.type.value()};
        r2[1] = compose.toByteArray();
        return NdefRecords.compose(bArr, Bytes.concat(r2), s);
    }

    public String toString() {
        return String.format("New %s Service: %s URI: %s", new Object[]{this.type, this.title, this.uri});
    }

    public static NewService parseNdef(NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
        Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.NEW_SERVICE_NDEF_TYPE));
        Builder builder = new Builder();
        byte[] payload = ndefRecord.getPayload();
        if (payload.length < 1) {
            throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "New Service NDEF record is too small to contain a service type.");
        }
        byte b = payload[0];
        ServiceType serviceType = ServiceType.UNSPECIFIED;
        try {
            serviceType = ServiceType.get(b);
        } catch (RuntimeException e) {
            LOG.w("Unrecognized service type. Will revert to unspecified. Type code: %s", Hex.encodeUpper(b));
        }
        builder.setType(serviceType);
        payload = NdefRecords.getAllBytes(ndefRecord, 1);
        if (payload.length == 0) {
            throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "New Service NDEF record is too small to contain any records.");
        }
        for (NdefRecord ndefRecord2 : SmartTapV2Exception.tryParseNdefMessage(payload, "nested new service record").getRecords()) {
            if (Arrays.equals(NdefRecords.getType(ndefRecord2, s), SmartTap2Values.NEW_SERVICE_TITLE_NDEF_TYPE)) {
                builder.setTitle(ndefRecord2, s);
            } else if (ndefRecord2.getTnf() == (short) 1 && Arrays.equals(ndefRecord2.getType(), NdefRecord.RTD_TEXT)) {
                if (Arrays.equals(ndefRecord2.getId(), SmartTap2Values.NEW_SERVICE_TITLE_NDEF_TYPE)) {
                    builder.setTitle(new Primitive(ndefRecord2).toText());
                } else {
                    LOG.w("Unrecognized NDEF ID %s inside of new service TEXT record.", SmartTap2Values.getNdefType(ndefRecord2.getId()));
                }
            } else if (ndefRecord2.getTnf() == (short) 1 && Arrays.equals(ndefRecord2.getType(), NdefRecord.RTD_URI)) {
                if (Arrays.equals(ndefRecord2.getId(), SmartTap2Values.NEW_SERVICE_URI_NDEF_TYPE)) {
                    builder.setUri(ndefRecord2);
                } else {
                    LOG.w("Unrecognized nested NDEF ID %s inside of new service URI record.", SmartTap2Values.getNdefType(ndefRecord2.getId()));
                }
            } else {
                LOG.w("Unrecognized nested NDEF type %s inside of new service.", SmartTap2Values.getNdefType(r6));
            }
        }
        return builder.build();
    }
}
