package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.Format;
import com.google.android.libraries.commerce.hce.ndef.NdefMessages;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.ndef.Primitive;
import com.google.android.libraries.commerce.hce.ndef.Text;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Bytes;
import java.util.Arrays;
import java.util.Set;

public class ServiceStatus {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final Optional<Text> description;
    private final byte[] id;
    private final Operation operation;
    private final byte[] payload;
    private final Optional<Text> title;
    private final Usage usage;

    static class Builder {
        private Text description;
        private byte[] id;
        private Operation operation;
        private byte[] payload;
        private Text title;
        private Usage usage;

        private Builder() {
            this.usage = Usage.UNKNOWN;
            this.operation = Operation.UNKNOWN;
            this.payload = new byte[0];
        }

        public Builder setId(byte[] bArr) throws SmartTapV2Exception {
            boolean z;
            if (bArr != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Cannot set null service ID.", new Object[0]);
            this.id = bArr;
            return this;
        }

        public Builder setUsage(Usage usage) {
            this.usage = usage;
            return this;
        }

        public Builder setTitle(NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
            Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.SERVICE_USAGE_TITLE_NDEF_TYPE));
            return setTitle(new Primitive(ndefRecord).toText());
        }

        public Builder setTitle(Text text) throws SmartTapV2Exception {
            boolean z;
            if (text != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Unable to extract title from NDEF record for service usage.", new Object[0]);
            this.title = text;
            return this;
        }

        public Builder setDescription(NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
            Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.SERVICE_USAGE_DESCRIPTION_NDEF_TYPE));
            return setDescription(new Primitive(ndefRecord).toText());
        }

        public Builder setDescription(Text text) throws SmartTapV2Exception {
            boolean z;
            if (text != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Unable to extract description from NDEF record for service usage.", new Object[0]);
            this.description = text;
            return this;
        }

        public Builder setOperation(Operation operation) {
            this.operation = operation;
            return this;
        }

        public Builder setPayload(byte[] bArr) throws SmartTapV2Exception {
            boolean z;
            if (this.id != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Cannot set null operation payload.", new Object[0]);
            this.payload = bArr;
            return this;
        }

        public ServiceStatus build() throws SmartTapV2Exception {
            boolean z;
            if (this.id != null) {
                z = true;
            } else {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "No service ID was set.", new Object[0]);
            return new ServiceStatus(this.id, this.usage, this.title, this.description, this.operation, this.payload);
        }
    }

    public enum Operation {
        UNKNOWN((byte) -1),
        NO_OP((byte) 0),
        REMOVE_SERVICE((byte) 1),
        SET_BALANCE((byte) 2),
        ADD_BALANCE((byte) 3),
        SUBTRACT_BALANCE((byte) 4),
        FREE((byte) 5);
        
        private final byte value;

        private Operation(byte b) {
            this.value = b;
        }

        public byte value() {
            return this.value;
        }

        public static Operation get(byte b) {
            for (Operation operation : values()) {
                if (b == operation.value()) {
                    return operation;
                }
            }
            throw new RuntimeException("Byte " + b + " was not a backing value for ServiceStatus.Operation.");
        }
    }

    public enum Usage {
        UNKNOWN((byte) -1),
        UNSPECIFIED((byte) 0),
        SUCCESS((byte) 1),
        INVALID_FORMAT((byte) 2),
        INVALID_VALUE((byte) 3);
        
        private final byte value;

        private Usage(byte b) {
            this.value = b;
        }

        public byte value() {
            return this.value;
        }

        public static Usage get(byte b) {
            for (Usage usage : values()) {
                if (b == usage.value()) {
                    return usage;
                }
            }
            throw new RuntimeException("Byte " + b + " was not a backing value for ServiceStatus.Usage.");
        }
    }

    public ServiceStatus(byte[] bArr, Usage usage, String str, String str2, Operation operation, byte[] bArr2) {
        this(bArr, usage, Strings.isNullOrEmpty(str) ? null : new Text(str), Strings.isNullOrEmpty(str2) ? null : new Text(str2), operation, bArr2);
    }

    public ServiceStatus(byte[] bArr, Usage usage, Text text, Text text2, Operation operation, byte[] bArr2) {
        this.id = bArr;
        this.usage = usage;
        this.title = Optional.fromNullable(text);
        this.description = Optional.fromNullable(text2);
        this.operation = operation;
        this.payload = bArr2;
    }

    public byte[] getId() {
        return this.id;
    }

    public Usage getUsage() {
        return this.usage;
    }

    public Optional<Text> getTitle() {
        return this.title;
    }

    public Optional<Text> getDescription() {
        return this.description;
    }

    public Operation getOperation() {
        return this.operation;
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public NdefRecord toNdef(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, short s) {
        Optional of;
        Optional of2;
        NdefRecord createPrimitive = NdefRecords.createPrimitive(SmartTap2Values.getServiceIdNdefType(s), Format.BINARY, this.id, s);
        if (this.title.isPresent()) {
            of = Optional.of(NdefRecords.createText(SmartTap2Values.SERVICE_USAGE_TITLE_NDEF_TYPE, ((Text) this.title.get()).getLanguageCode(), ((Text) this.title.get()).getText(), s));
        } else {
            of = Optional.absent();
        }
        if (this.description.isPresent()) {
            of2 = Optional.of(NdefRecords.createText(SmartTap2Values.SERVICE_USAGE_DESCRIPTION_NDEF_TYPE, ((Text) this.description.get()).getLanguageCode(), ((Text) this.description.get()).getText(), s));
        } else {
            of2 = Optional.absent();
        }
        of2 = NdefMessages.fromOptionals(multimap, set, SmartTap2Values.SERVICE_USAGE_NDEF_TYPE, s, of, of2);
        if (this.usage == Usage.UNKNOWN) {
            of2 = Optional.absent();
        } else {
            r1 = new byte[2][];
            r1[0] = new byte[]{this.usage.value()};
            r1[1] = of2.isPresent() ? ((NdefMessage) of2.get()).toByteArray() : new byte[0];
            of2 = Optional.of(NdefRecords.compose(SmartTap2Values.SERVICE_USAGE_NDEF_TYPE, Bytes.concat(r1), s));
        }
        if (this.operation == Operation.UNKNOWN) {
            of = Optional.absent();
        } else {
            byte[] bArr = SmartTap2Values.SERVICE_UPDATE_NDEF_TYPE;
            r4 = new byte[2][];
            r4[0] = new byte[]{this.operation.value()};
            r4[1] = this.payload == null ? new byte[0] : this.payload;
            of = Optional.of(NdefRecords.compose(bArr, Bytes.concat(r4), s));
        }
        return NdefRecords.compose(SmartTap2Values.SERVICE_STATUS_NDEF_TYPE, ((NdefMessage) NdefMessages.fromOptionals(multimap, set, SmartTap2Values.SERVICE_STATUS_NDEF_TYPE, s, Optional.of(createPrimitive), of2, of).get()).toByteArray(), s);
    }

    public String toString() {
        return String.format("Service ID: %s Usage: %s Operation: %s Payload: %s", new Object[]{Hex.encodeUpper(this.id), this.usage, this.operation, Hex.encodeUpper(this.payload)});
    }

    public static ServiceStatus parseNdef(NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
        Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.SERVICE_STATUS_NDEF_TYPE));
        Builder builder = new Builder();
        for (NdefRecord ndefRecord2 : SmartTapV2Exception.tryParseNdefMessage(ndefRecord.getPayload(), "service status record").getRecords()) {
            byte[] type = NdefRecords.getType(ndefRecord2, s);
            if (Arrays.equals(type, SmartTap2Values.getServiceIdNdefType(s))) {
                setId(builder, ndefRecord2, s);
            } else if (Arrays.equals(type, SmartTap2Values.SERVICE_USAGE_NDEF_TYPE)) {
                setUsage(builder, ndefRecord2, s);
            } else if (Arrays.equals(type, SmartTap2Values.SERVICE_UPDATE_NDEF_TYPE)) {
                setUpdate(builder, ndefRecord2, s);
            } else {
                LOG.w("Unrecognized nested NDEF type %s inside of service status.", SmartTap2Values.getNdefType(type));
            }
        }
        return builder.build();
    }

    private static void setId(Builder builder, NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
        Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.getServiceIdNdefType(s)));
        builder.setId(new Primitive(ndefRecord).getPayload());
    }

    private static void setUsage(Builder builder, NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
        Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.SERVICE_USAGE_NDEF_TYPE));
        byte[] payload = ndefRecord.getPayload();
        SmartTapV2Exception.check(payload.length >= 1, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Usage record has no status.", new Object[0]);
        builder.setUsage(Usage.get(payload[0]));
        if (payload.length > 1) {
            for (NdefRecord ndefRecord2 : SmartTapV2Exception.tryParseNdefMessage(NdefRecords.getAllBytes(ndefRecord, 1), "nested usage record").getRecords()) {
                byte[] type = NdefRecords.getType(ndefRecord2, s);
                if (Arrays.equals(type, SmartTap2Values.SERVICE_USAGE_TITLE_NDEF_TYPE)) {
                    builder.setTitle(ndefRecord2, s);
                } else if (Arrays.equals(type, SmartTap2Values.SERVICE_USAGE_DESCRIPTION_NDEF_TYPE)) {
                    builder.setDescription(ndefRecord2, s);
                } else if (ndefRecord2.getTnf() == (short) 1 && Arrays.equals(ndefRecord2.getType(), NdefRecord.RTD_TEXT)) {
                    type = ndefRecord2.getId();
                    if (Arrays.equals(type, SmartTap2Values.SERVICE_USAGE_TITLE_NDEF_TYPE)) {
                        builder.setTitle(new Primitive(ndefRecord2).toText());
                    } else if (Arrays.equals(type, SmartTap2Values.SERVICE_USAGE_DESCRIPTION_NDEF_TYPE)) {
                        builder.setDescription(new Primitive(ndefRecord2).toText());
                    } else {
                        LOG.w("Unrecognized NDEF ID %s inside of service usage TEXT record.", SmartTap2Values.getNdefType(type));
                    }
                } else {
                    LOG.w("Unrecognized nested NDEF type %s inside of service usage.", SmartTap2Values.getNdefType(type));
                }
            }
        }
    }

    private static void setUpdate(Builder builder, NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
        Preconditions.checkArgument(Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.SERVICE_UPDATE_NDEF_TYPE));
        byte[] payload = ndefRecord.getPayload();
        SmartTapV2Exception.check(payload.length >= 1, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Update record has no operation.", new Object[0]);
        builder.setOperation(Operation.get(payload[0]));
        if (payload.length > 1) {
            builder.setPayload(NdefRecords.getAllBytes(ndefRecord, 1));
        }
    }
}
