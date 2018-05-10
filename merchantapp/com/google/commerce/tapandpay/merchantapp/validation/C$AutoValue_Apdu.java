package com.google.commerce.tapandpay.merchantapp.validation;

import java.util.List;

class C$AutoValue_Apdu extends Apdu {
    private final String name;
    private final String payload;
    private final String prefix;
    private final int prefixLength;
    private final List<Record> records;
    private final String suffix;
    private final int suffixLength;

    static final class Builder extends com.google.commerce.tapandpay.merchantapp.validation.Apdu.Builder {
        private String name;
        private String payload;
        private String prefix;
        private Integer prefixLength;
        private List<Record> records;
        private String suffix;
        private Integer suffixLength;

        Builder() {
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Apdu.Builder setName(String str) {
            this.name = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Apdu.Builder setPrefix(String str) {
            this.prefix = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Apdu.Builder setPrefixLength(int i) {
            this.prefixLength = Integer.valueOf(i);
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Apdu.Builder setSuffix(String str) {
            this.suffix = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Apdu.Builder setSuffixLength(int i) {
            this.suffixLength = Integer.valueOf(i);
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Apdu.Builder setPayload(String str) {
            this.payload = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Apdu.Builder setRecords(List<Record> list) {
            this.records = list;
            return this;
        }

        public Apdu build() {
            String str = "";
            if (this.prefixLength == null) {
                str = String.valueOf(str).concat(" prefixLength");
            }
            if (this.suffixLength == null) {
                str = String.valueOf(str).concat(" suffixLength");
            }
            if (this.records == null) {
                str = String.valueOf(str).concat(" records");
            }
            if (str.isEmpty()) {
                return new C$AutoValue_Apdu(this.name, this.prefix, this.prefixLength.intValue(), this.suffix, this.suffixLength.intValue(), this.payload, this.records, (byte) 0);
            }
            String str2 = "Missing required properties:";
            str = String.valueOf(str);
            throw new IllegalStateException(str.length() != 0 ? str2.concat(str) : new String(str2));
        }
    }

    C$AutoValue_Apdu(String str, String str2, int i, String str3, int i2, String str4, List<Record> list) {
        this.name = str;
        this.prefix = str2;
        this.prefixLength = i;
        this.suffix = str3;
        this.suffixLength = i2;
        this.payload = str4;
        if (list == null) {
            throw new NullPointerException("Null records");
        }
        this.records = list;
    }

    public String name() {
        return this.name;
    }

    public String prefix() {
        return this.prefix;
    }

    public int prefixLength() {
        return this.prefixLength;
    }

    public String suffix() {
        return this.suffix;
    }

    public int suffixLength() {
        return this.suffixLength;
    }

    public String payload() {
        return this.payload;
    }

    public List<Record> records() {
        return this.records;
    }

    public String toString() {
        String str = this.name;
        String str2 = this.prefix;
        int i = this.prefixLength;
        String str3 = this.suffix;
        int i2 = this.suffixLength;
        String str4 = this.payload;
        String valueOf = String.valueOf(this.records);
        return new StringBuilder(((((String.valueOf(str).length() + 101) + String.valueOf(str2).length()) + String.valueOf(str3).length()) + String.valueOf(str4).length()) + String.valueOf(valueOf).length()).append("Apdu{name=").append(str).append(", prefix=").append(str2).append(", prefixLength=").append(i).append(", suffix=").append(str3).append(", suffixLength=").append(i2).append(", payload=").append(str4).append(", records=").append(valueOf).append("}").toString();
    }

    C$AutoValue_Apdu(String str, String str2, int i, String str3, int i2, String str4, List<Record> list, byte b) {
        this(str, str2, i, str3, i2, str4, list);
    }
}
