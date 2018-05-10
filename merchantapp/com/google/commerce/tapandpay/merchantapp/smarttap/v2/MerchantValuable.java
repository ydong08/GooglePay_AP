package com.google.commerce.tapandpay.merchantapp.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Issuer;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Type;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.ndef.Format;
import com.google.android.libraries.commerce.hce.ndef.Primitive;
import com.google.android.libraries.commerce.hce.util.Bcd;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.merchantapp.smarttap.v2.AutoValue_MerchantValuable.GsonTypeAdapter;
import com.google.commerce.tapandpay.merchantapp.testcase.EncodingValue;
import com.google.commerce.tapandpay.merchantapp.testcase.EncodingValue.Encoding;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.io.PrintStream;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;

public abstract class MerchantValuable extends ServiceObject implements Serializable {
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

        protected Builder generateNewServiceId() {
            byte[] bArr = new byte[8];
            new SecureRandom().nextBytes(bArr);
            setServiceId(bArr);
            return this;
        }

        protected Builder prepareIssuer(Long l) {
            return prepareIssuer(l, Issuer.MERCHANT);
        }

        protected Builder prepareIssuer(Long l, Issuer issuer) {
            if (l == null) {
                l = Long.valueOf(0);
            }
            setIssuerFormat(Format.BCD);
            setIssuer(issuer);
            setIssuerId(Bcd.encode(l.longValue()));
            return this;
        }

        protected Builder prepareExpiration(int i, int i2) {
            if (i2 < 99) {
                i2 += 2000;
            }
            setExpiration(Bcd.encode((long) ((i * 10000) + i2)));
            setExpirationFormat(Format.BCD);
            return this;
        }

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
                MerchantValuable.LOG.w("Unrecognized service object NDEF type %s", SmartTap2Values.getNdefType(bArr));
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
            System.out.println(">>> TNF: " + ndefRecord.getTnf());
            PrintStream printStream = System.out;
            String str = ">>> TYPE: ";
            String valueOf = String.valueOf(Hex.encodeUpper(ndefRecord.getType()));
            printStream.println(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            printStream = System.out;
            str = ">>> ID: ";
            valueOf = String.valueOf(Hex.encodeUpper(ndefRecord.getId()));
            printStream.println(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            printStream = System.out;
            str = ">>> PAYLOAD: ";
            valueOf = String.valueOf(Hex.encodeUpper(ndefRecord.getPayload()));
            printStream.println(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            System.out.println(">>> VERSION: " + s);
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

        protected MerchantValuable build() {
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

        public MerchantValuable autoBuild() {
            String str = "";
            if (this.type == null) {
                str = String.valueOf(str).concat(" type");
            }
            if (this.serviceId == null) {
                str = String.valueOf(str).concat(" serviceId");
            }
            if (this.serviceNumberId == null) {
                str = String.valueOf(str).concat(" serviceNumberId");
            }
            if (this.serviceNumberIdFormat == null) {
                str = String.valueOf(str).concat(" serviceNumberIdFormat");
            }
            if (str.isEmpty()) {
                return new AutoValue_MerchantValuable(this.type, this.serviceId, this.serviceNumberId, this.serviceNumberIdFormat, this.deviceId, this.deviceIdFormat, this.issuerId, this.issuer, this.issuerFormat, this.tapId, this.tapIdFormat, this.pin, this.pinFormat, this.cvc, this.cvcFormat, this.expiration, this.expirationFormat, this.preferedLanguage);
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

    public static MerchantValuable newCustomer(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return builder().generateNewServiceId().setType(Type.WALLET_CUSTOMER).setIssuer(Issuer.WALLET).setIssuerId(SmartTap2Values.GOOGLE_ISSUER_ID).setIssuerFormat(Format.BINARY).setServiceNumberId(bArr).setServiceNumberIdFormat(Format.BINARY).setPreferedLanguage(Locale.getDefault().getLanguage()).setTapId(bArr2).setTapIdFormat(Format.BINARY).setDeviceId(bArr3).setDeviceIdFormat(Format.BINARY).build();
    }

    public static MerchantValuable newLoyaltyProgram(Long l, EncodingValue encodingValue) {
        return builder().generateNewServiceId().setType(Type.LOYALTY).prepareIssuer(l).setServiceNumberId(encodingValue.getByteValue()).setServiceNumberIdFormat(getFormat(encodingValue.getEncoding())).build();
    }

    public static MerchantValuable newOffer(Long l, EncodingValue encodingValue) {
        return builder().generateNewServiceId().setType(Type.OFFER).prepareIssuer(l).setServiceNumberId(encodingValue.getByteValue()).setServiceNumberIdFormat(getFormat(encodingValue.getEncoding())).build();
    }

    public static MerchantValuable newGiftCard(Long l, EncodingValue encodingValue, EncodingValue encodingValue2) {
        return builder().generateNewServiceId().setType(Type.GIFT_CARD).prepareIssuer(l).setServiceNumberId(encodingValue.getByteValue()).setServiceNumberIdFormat(getFormat(encodingValue.getEncoding())).setPin(encodingValue2.getByteValue()).setPinFormat(getFormat(encodingValue2.getEncoding())).build();
    }

    public static MerchantValuable newPlc(Long l, EncodingValue encodingValue, EncodingValue encodingValue2, int i, int i2) {
        return builder().generateNewServiceId().setType(Type.CLOSED_LOOP_CARD).prepareIssuer(l).setServiceNumberId(encodingValue.getByteValue()).setServiceNumberIdFormat(getFormat(encodingValue.getEncoding())).setCvc(encodingValue2.getByteValue()).setCvcFormat(getFormat(encodingValue2.getEncoding())).prepareExpiration(i, i2).build();
    }

    private static Format getFormat(Encoding encoding) {
        switch (encoding) {
            case ASCII:
                return Format.ASCII;
            case BCD:
                return Format.BCD;
            case Binary:
                return Format.BINARY;
            default:
                LOG.w("Unknown encoding format: %s", encoding);
                return Format.BINARY;
        }
    }

    public static TypeAdapter<MerchantValuable> typeAdapter(Gson gson) {
        return new GsonTypeAdapter(gson);
    }
}
