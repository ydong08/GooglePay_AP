package com.google.android.libraries.commerce.hce.terminal.smarttap;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Issuer;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Type;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.ndef.Format;
import com.google.android.libraries.commerce.hce.ndef.Primitive;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.io.Serializable;
import java.util.Arrays;

public abstract class TerminalServiceObject extends ServiceObject implements Serializable {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();

    public static abstract class Builder {
        private byte[] cvc;
        private Format cvcFormat;
        private byte[] deviceId;
        private Format deviceIdFormat;
        private byte[] expiration;
        private Format expirationFormat;
        private Issuer issuer;
        private Format issuerFormat;
        private byte[] issuerId;
        private byte[] pin;
        private Format pinFormat;
        private String preferedLanguage;
        private byte[] serviceId;
        private byte[] serviceNumberId;
        private Format serviceNumberIdFormat;
        private byte[] tapId;
        private Format tapIdFormat;
        private Type type;

        protected Builder setType(byte[] bArr) {
            if (Arrays.equals(bArr, SmartTap2Values.CUSTOMER_NDEF_TYPE)) {
                setType(Type.WALLET_CUSTOMER);
                setIssuer(Issuer.WALLET);
            } else if (Arrays.equals(bArr, SmartTap2Values.LOYALTY_NDEF_TYPE)) {
                setType(Type.LOYALTY);
            } else if (Arrays.equals(bArr, SmartTap2Values.OFFER_NDEF_TYPE)) {
                setType(Type.OFFER);
            } else if (Arrays.equals(bArr, SmartTap2Values.GIFT_CARD_NDEF_TYPE)) {
                setType(Type.GIFT_CARD);
            } else if (Arrays.equals(bArr, SmartTap2Values.PLC_NDEF_TYPE)) {
                setType(Type.CLOSED_LOOP_CARD);
            } else {
                TerminalServiceObject.LOG.w("Unrecognized service object NDEF type %s", SmartTap2Values.getNdefType(bArr));
            }
            return this;
        }

        protected Builder setIssuer(NdefRecord ndefRecord, short s) {
            Primitive fromServiceRecord = Primitive.fromServiceRecord(ndefRecord, s);
            setIssuerFormat(fromServiceRecord.getFormat());
            byte[] toBytes = fromServiceRecord.toBytes();
            setIssuer(Issuer.get(toBytes[0]));
            setIssuerId(Arrays.copyOfRange(toBytes, 1, toBytes.length));
            return this;
        }

        protected Builder setServiceNumber(NdefRecord ndefRecord, short s) {
            Primitive fromServiceRecord = Primitive.fromServiceRecord(ndefRecord, s);
            setServiceNumberIdFormat(fromServiceRecord.getFormat());
            setServiceNumberId(fromServiceRecord.toBytes());
            return this;
        }

        protected Builder setLanguage(NdefRecord ndefRecord, short s) {
            setPreferedLanguage(Primitive.fromServiceRecord(ndefRecord, s).toText().getText());
            return this;
        }

        protected Builder setTapId(NdefRecord ndefRecord, short s) {
            Primitive fromServiceRecord = Primitive.fromServiceRecord(ndefRecord, s);
            setTapIdFormat(fromServiceRecord.getFormat());
            setTapId(fromServiceRecord.toBytes());
            return this;
        }

        protected Builder setDeviceId(NdefRecord ndefRecord, short s) {
            Primitive fromServiceRecord = Primitive.fromServiceRecord(ndefRecord, s);
            setDeviceIdFormat(fromServiceRecord.getFormat());
            setDeviceId(fromServiceRecord.toBytes());
            return this;
        }

        protected Builder setPin(NdefRecord ndefRecord, short s) {
            Primitive fromServiceRecord = Primitive.fromServiceRecord(ndefRecord, s);
            setPinFormat(fromServiceRecord.getFormat());
            setPin(fromServiceRecord.toBytes());
            return this;
        }

        protected Builder setCvc(NdefRecord ndefRecord, short s) {
            Primitive fromServiceRecord = Primitive.fromServiceRecord(ndefRecord, s);
            setCvcFormat(fromServiceRecord.getFormat());
            setCvc(fromServiceRecord.toBytes());
            return this;
        }

        protected Builder setExpiration(NdefRecord ndefRecord, short s) {
            Primitive fromServiceRecord = Primitive.fromServiceRecord(ndefRecord, s);
            setExpirationFormat(fromServiceRecord.getFormat());
            setExpiration(fromServiceRecord.toBytes());
            return this;
        }

        public TerminalServiceObject build() {
            return autoBuild();
        }

        Builder(byte b) {
            this();
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder setServiceId(byte[] bArr) {
            this.serviceId = bArr;
            return this;
        }

        public Builder setServiceNumberId(byte[] bArr) {
            this.serviceNumberId = bArr;
            return this;
        }

        public Builder setServiceNumberIdFormat(Format format) {
            this.serviceNumberIdFormat = format;
            return this;
        }

        public Builder setDeviceId(byte[] bArr) {
            this.deviceId = bArr;
            return this;
        }

        public Builder setDeviceIdFormat(Format format) {
            this.deviceIdFormat = format;
            return this;
        }

        public Builder setIssuerId(byte[] bArr) {
            this.issuerId = bArr;
            return this;
        }

        public Builder setIssuer(Issuer issuer) {
            this.issuer = issuer;
            return this;
        }

        public Builder setIssuerFormat(Format format) {
            this.issuerFormat = format;
            return this;
        }

        public Builder setTapId(byte[] bArr) {
            this.tapId = bArr;
            return this;
        }

        public Builder setTapIdFormat(Format format) {
            this.tapIdFormat = format;
            return this;
        }

        public Builder setPin(byte[] bArr) {
            this.pin = bArr;
            return this;
        }

        public Builder setPinFormat(Format format) {
            this.pinFormat = format;
            return this;
        }

        public Builder setCvc(byte[] bArr) {
            this.cvc = bArr;
            return this;
        }

        public Builder setCvcFormat(Format format) {
            this.cvcFormat = format;
            return this;
        }

        public Builder setExpiration(byte[] bArr) {
            this.expiration = bArr;
            return this;
        }

        public Builder setExpirationFormat(Format format) {
            this.expirationFormat = format;
            return this;
        }

        public Builder setPreferedLanguage(String str) {
            this.preferedLanguage = str;
            return this;
        }

        public TerminalServiceObject autoBuild() {
            String str = "";
            if (this.type == null) {
                str = String.valueOf(str).concat(" type");
            }
            if (this.serviceNumberId == null) {
                str = String.valueOf(str).concat(" serviceNumberId");
            }
            if (this.serviceNumberIdFormat == null) {
                str = String.valueOf(str).concat(" serviceNumberIdFormat");
            }
            if (str.isEmpty()) {
                return new AutoValue_TerminalServiceObject(this.type, this.serviceId, this.serviceNumberId, this.serviceNumberIdFormat, this.deviceId, this.deviceIdFormat, this.issuerId, this.issuer, this.issuerFormat, this.tapId, this.tapIdFormat, this.pin, this.pinFormat, this.cvc, this.cvcFormat, this.expiration, this.expirationFormat, this.preferedLanguage);
            }
            String str2 = "Missing required properties:";
            str = String.valueOf(str);
            throw new IllegalStateException(str.length() != 0 ? str2.concat(str) : new String(str2));
        }
    }

    public abstract byte[] cvc();

    public abstract Format cvcFormat();

    public abstract byte[] deviceId();

    public abstract Format deviceIdFormat();

    public abstract byte[] expiration();

    public abstract Format expirationFormat();

    public abstract Issuer issuer();

    public abstract Format issuerFormat();

    public abstract byte[] issuerId();

    public abstract byte[] pin();

    public abstract Format pinFormat();

    public abstract String preferedLanguage();

    public abstract byte[] serviceId();

    public abstract byte[] serviceNumberId();

    public abstract Format serviceNumberIdFormat();

    public abstract byte[] tapId();

    public abstract Format tapIdFormat();

    public abstract Type type();

    public static Builder builder() {
        return new Builder((byte) 0);
    }
}
