package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.ndef.Format;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.collect.ImmutableMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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

    public String toString() {
        String valueOf = String.valueOf(type().name());
        String valueOf2 = String.valueOf(Hex.encodeUpper(serviceId()));
        return new StringBuilder((String.valueOf(valueOf).length() + 2) + String.valueOf(valueOf2).length()).append(valueOf).append(": ").append(valueOf2).toString();
    }
}
