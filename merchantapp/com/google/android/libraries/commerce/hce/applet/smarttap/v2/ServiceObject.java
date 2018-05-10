package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.ndef.Format;
import com.google.android.libraries.commerce.hce.ndef.NdefMessages;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.ndef.Text;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Bytes;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class ServiceObject {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();

    public enum Issuer {
        UNSPECIFIED((byte) 0),
        MERCHANT((byte) 1),
        WALLET((byte) 2),
        MANUFACTURER((byte) 3);
        
        private final byte value;

        private Issuer(byte b) {
            this.value = b;
        }

        public byte value() {
            return this.value;
        }

        public static Issuer get(byte b) {
            for (Issuer issuer : values()) {
                if (b == issuer.value()) {
                    return issuer;
                }
            }
            throw new RuntimeException("Byte " + b + " was not a backing value for Valuable.Issuer.");
        }
    }

    public static class Request {
        private Format format;
        private Issuer issuer;
        private Type type;

        public Request(Type type, Issuer issuer, Format format) {
            this.type = type;
            this.issuer = issuer;
            this.format = format;
        }

        public Type getType() {
            return this.type;
        }

        public Issuer getIssuer() {
            return this.issuer;
        }

        public Format getFormat() {
            return this.format;
        }

        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }
            Request request = (Request) obj;
            if (Objects.equals(getType(), request.getType()) && Objects.equals(getIssuer(), request.getIssuer()) && Objects.equals(getFormat(), request.getFormat())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return com.google.common.base.Objects.hashCode(getType(), getIssuer(), getFormat());
        }
    }

    public enum Type {
        ALL((byte) 0),
        ALL_EXCEPT_PPSE((byte) 1),
        PPSE((byte) 2),
        LOYALTY((byte) 3),
        OFFER((byte) 4),
        GIFT_CARD((byte) 5),
        CLOSED_LOOP_CARD((byte) 6),
        CLOUD_WALLET((byte) 16),
        MOBILE_MARKETING((byte) 17),
        WALLET_CUSTOMER((byte) 64);
        
        private static final Map<ByteArrayWrapper, Type> SERVICE_TYPES = null;
        private final byte value;

        static {
            SERVICE_TYPES = ImmutableMap.builder().put(new ByteArrayWrapper(SmartTap2Values.CUSTOMER_NDEF_TYPE), WALLET_CUSTOMER).put(new ByteArrayWrapper(SmartTap2Values.LOYALTY_NDEF_TYPE), LOYALTY).put(new ByteArrayWrapper(SmartTap2Values.OFFER_NDEF_TYPE), OFFER).put(new ByteArrayWrapper(SmartTap2Values.GIFT_CARD_NDEF_TYPE), GIFT_CARD).put(new ByteArrayWrapper(SmartTap2Values.PLC_NDEF_TYPE), CLOSED_LOOP_CARD).build();
        }

        private Type(byte b) {
            this.value = b;
        }

        public byte value() {
            return this.value;
        }

        public static Type get(byte b) {
            for (Type type : values()) {
                if (b == type.value()) {
                    return type;
                }
            }
            throw new RuntimeException("Byte " + b + " was not a backing value for Valuable.Type.");
        }

        public static Type get(byte[] bArr) {
            return (Type) SERVICE_TYPES.get(new ByteArrayWrapper(bArr));
        }

        public String toString() {
            return String.format("%s (0x%s)", new Object[]{super.toString(), Hex.encodeUpper(this.value)});
        }
    }

    public abstract byte[] serviceId();

    public abstract byte[] serviceNumberId();

    public abstract Type type();

    public byte[] issuerId() {
        throw new UnsupportedOperationException("Issuer id has not been set");
    }

    public Issuer issuer() {
        return Issuer.UNSPECIFIED;
    }

    public Format issuerFormat() {
        return Format.UNSPECIFIED;
    }

    public Format serviceNumberIdFormat() {
        return Format.UNSPECIFIED;
    }

    public String preferedLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public byte[] deviceId() {
        throw new UnsupportedOperationException("Device id has not been set");
    }

    public Format deviceIdFormat() {
        return Format.UNSPECIFIED;
    }

    public byte[] tapId() {
        throw new UnsupportedOperationException("Tap id has not been set");
    }

    public Format tapIdFormat() {
        return Format.UNSPECIFIED;
    }

    public byte[] pin() {
        throw new UnsupportedOperationException("Pin has not been set");
    }

    public Format pinFormat() {
        return Format.UNSPECIFIED;
    }

    public byte[] expiration() {
        throw new UnsupportedOperationException("Expiration has not been set");
    }

    public Format expirationFormat() {
        return Format.UNSPECIFIED;
    }

    public byte[] cvc() {
        throw new UnsupportedOperationException("CVC has not been set");
    }

    public Format cvcFormat() {
        return Format.UNSPECIFIED;
    }

    public boolean unlockRequired() {
        return false;
    }

    public Optional<NdefRecord> toNdef(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, short s) {
        if (getServiceObjectNdef(multimap, set, s).isPresent()) {
            NdefRecord serviceIdNdef = getServiceIdNdef(s);
            byte[] serviceValueNdefType = SmartTap2Values.getServiceValueNdefType(s);
            return Optional.of(NdefRecords.compose(serviceValueNdefType, NdefMessages.compose((Multimap) multimap, (Set) set, serviceValueNdefType, s, serviceIdNdef, (NdefRecord) r0.get()).toByteArray(), s));
        }
        LOG.w("Could not construct service object record.", new Object[0]);
        return Optional.absent();
    }

    public Optional<NdefRecord> getServiceObjectNdef(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, short s) {
        Object obj = null;
        switch (type()) {
            case PPSE:
                obj = composePpseNdef();
                break;
            case LOYALTY:
                obj = composeLoyaltyNdef(multimap, set, s);
                break;
            case OFFER:
                obj = composeOfferNdef(multimap, set, s);
                break;
            case GIFT_CARD:
                obj = composeGiftCardNdef(multimap, set, s);
                break;
            case CLOSED_LOOP_CARD:
                obj = composeClosedLoopCardNdef(multimap, set, s);
                break;
            case WALLET_CUSTOMER:
                obj = composeWalletCustomerNdef(multimap, set, s);
                break;
            case ALL:
            case ALL_EXCEPT_PPSE:
            case CLOUD_WALLET:
            case MOBILE_MARKETING:
                LOG.w("Invalid service object type %s", type());
                break;
            default:
                LOG.w("Unsupported service object type %s", type());
                break;
        }
        return Optional.fromNullable(obj);
    }

    private NdefRecord composePpseNdef() {
        Preconditions.checkArgument(type() == Type.PPSE);
        return null;
    }

    private NdefRecord composeLoyaltyNdef(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, short s) {
        NdefRecord issuerNdef;
        Preconditions.checkArgument(type() == Type.LOYALTY);
        if (s == (short) 0) {
            issuerNdef = getIssuerNdef(s);
        } else {
            issuerNdef = getServiceIdNdef(s);
        }
        return NdefRecords.compose(SmartTap2Values.LOYALTY_NDEF_TYPE, NdefMessages.compose((Multimap) multimap, (Set) set, SmartTap2Values.LOYALTY_NDEF_TYPE, s, issuerNdef, getServiceNumberNdef(s)).toByteArray(), s);
    }

    private NdefRecord composeOfferNdef(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, short s) {
        NdefRecord issuerNdef;
        Preconditions.checkArgument(type() == Type.OFFER);
        if (s == (short) 0) {
            issuerNdef = getIssuerNdef(s);
        } else {
            issuerNdef = getServiceIdNdef(s);
        }
        return NdefRecords.compose(SmartTap2Values.OFFER_NDEF_TYPE, NdefMessages.compose((Multimap) multimap, (Set) set, SmartTap2Values.OFFER_NDEF_TYPE, s, issuerNdef, getServiceNumberNdef(s)).toByteArray(), s);
    }

    private NdefRecord composeGiftCardNdef(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, short s) {
        NdefRecord issuerNdef;
        Preconditions.checkArgument(type() == Type.GIFT_CARD);
        if (s == (short) 0) {
            issuerNdef = getIssuerNdef(s);
        } else {
            issuerNdef = getServiceIdNdef(s);
        }
        NdefRecord createPrimitive = NdefRecords.createPrimitive(SmartTap2Values.PIN_NDEF_TYPE, pinFormat(), pin(), s);
        return NdefRecords.compose(SmartTap2Values.GIFT_CARD_NDEF_TYPE, NdefMessages.compose((Multimap) multimap, (Set) set, SmartTap2Values.GIFT_CARD_NDEF_TYPE, s, issuerNdef, getServiceNumberNdef(s), createPrimitive).toByteArray(), s);
    }

    private NdefRecord composeClosedLoopCardNdef(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, short s) {
        NdefRecord issuerNdef;
        Preconditions.checkArgument(type() == Type.CLOSED_LOOP_CARD);
        if (s == (short) 0) {
            issuerNdef = getIssuerNdef(s);
        } else {
            issuerNdef = getServiceIdNdef(s);
        }
        NdefRecord createPrimitive = NdefRecords.createPrimitive(SmartTap2Values.EXPIRATION_NDEF_TYPE, expirationFormat(), expiration(), s);
        NdefRecord createPrimitive2 = NdefRecords.createPrimitive(SmartTap2Values.CVC_NDEF_TYPE, cvcFormat(), cvc(), s);
        return NdefRecords.compose(SmartTap2Values.PLC_NDEF_TYPE, NdefMessages.compose((Multimap) multimap, (Set) set, SmartTap2Values.PLC_NDEF_TYPE, s, issuerNdef, getServiceNumberNdef(s), createPrimitive, createPrimitive2).toByteArray(), s);
    }

    private NdefRecord composeWalletCustomerNdef(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, short s) {
        Preconditions.checkArgument(type() == Type.WALLET_CUSTOMER);
        NdefRecord createPrimitive = NdefRecords.createPrimitive(SmartTap2Values.CUSTOMER_ID_NDEF_TYPE, serviceNumberIdFormat(), serviceNumberId(), s);
        Optional absent = Optional.absent();
        String preferedLanguage = preferedLanguage();
        if (!Strings.isNullOrEmpty(preferedLanguage)) {
            absent = Optional.of(NdefRecords.createPrimitive(SmartTap2Values.CUSTOMER_LANGUAGE_NDEF_TYPE, Text.LANGUAGE_FORMAT, preferedLanguage.getBytes(Text.LANGUAGE_CHARSET), s));
        }
        Optional absent2 = Optional.absent();
        byte[] tapId = tapId();
        if (tapId != null && tapId.length > 0) {
            absent2 = Optional.of(NdefRecords.createPrimitive(SmartTap2Values.CUSTOMER_TAP_NDEF_TYPE, tapIdFormat(), tapId, s));
        }
        Optional absent3 = Optional.absent();
        byte[] deviceId = deviceId();
        if (deviceId != null && deviceId.length > 0) {
            absent3 = Optional.of(NdefRecords.createPrimitive(SmartTap2Values.CUSTOMER_DEVICE_NDEF_TYPE, deviceIdFormat(), deviceId, s));
        }
        return NdefRecords.compose(SmartTap2Values.CUSTOMER_NDEF_TYPE, ((NdefMessage) NdefMessages.fromOptionals(multimap, set, SmartTap2Values.CUSTOMER_NDEF_TYPE, s, Optional.of(createPrimitive), absent, absent2, absent3).get()).toByteArray(), s);
    }

    private NdefRecord getServiceIdNdef(short s) {
        return NdefRecords.createPrimitive(SmartTap2Values.getServiceIdNdefType(s), Format.BINARY, serviceId(), s);
    }

    public NdefRecord getIssuerNdef(short s) {
        r0 = new byte[2][];
        r0[0] = new byte[]{issuer().value()};
        r0[1] = issuerId();
        return NdefRecords.createPrimitive(SmartTap2Values.ISSUER_NDEF_TYPE, issuerFormat(), Bytes.concat(r0), s);
    }

    private NdefRecord getServiceNumberNdef(short s) {
        return NdefRecords.createPrimitive(SmartTap2Values.SERVICE_NUMBER_NDEF_TYPE, serviceNumberIdFormat(), serviceNumberId(), s);
    }

    public String toString() {
        String valueOf = String.valueOf(type().name());
        String valueOf2 = String.valueOf(Hex.encodeUpper(serviceId()));
        return new StringBuilder((String.valueOf(valueOf).length() + 2) + String.valueOf(valueOf2).length()).append(valueOf).append(": ").append(valueOf2).toString();
    }
}
