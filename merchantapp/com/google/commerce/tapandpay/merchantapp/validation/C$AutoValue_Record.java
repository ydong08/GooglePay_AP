package com.google.commerce.tapandpay.merchantapp.validation;

import java.util.List;

class C$AutoValue_Record extends Record {
    private final String id;
    private final String name;
    private final String payload;
    private final String prefix;
    private final int prefixLength;
    private final List<Record> records;
    private final String suffix;
    private final int suffixLength;
    private final String tnf;
    private final String type;

    static final class Builder extends com.google.commerce.tapandpay.merchantapp.validation.Record.Builder {
        private String id;
        private String name;
        private String payload;
        private String prefix;
        private Integer prefixLength;
        private List<Record> records;
        private String suffix;
        private Integer suffixLength;
        private String tnf;
        private String type;

        Builder() {
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Record.Builder setName(String str) {
            this.name = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Record.Builder setId(String str) {
            this.id = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Record.Builder setTnf(String str) {
            this.tnf = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Record.Builder setType(String str) {
            this.type = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Record.Builder setPrefix(String str) {
            this.prefix = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Record.Builder setPrefixLength(int i) {
            this.prefixLength = Integer.valueOf(i);
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Record.Builder setSuffix(String str) {
            this.suffix = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Record.Builder setSuffixLength(int i) {
            this.suffixLength = Integer.valueOf(i);
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Record.Builder setPayload(String str) {
            this.payload = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Record.Builder setRecords(List<Record> list) {
            this.records = list;
            return this;
        }

        public Record build() {
            String str = "";
            if (this.name == null) {
                str = String.valueOf(str).concat(" name");
            }
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
                return new C$AutoValue_Record(this.name, this.id, this.tnf, this.type, this.prefix, this.prefixLength.intValue(), this.suffix, this.suffixLength.intValue(), this.payload, this.records, (byte) 0);
            }
            String str2 = "Missing required properties:";
            str = String.valueOf(str);
            throw new IllegalStateException(str.length() != 0 ? str2.concat(str) : new String(str2));
        }
    }

    C$AutoValue_Record(String str, String str2, String str3, String str4, String str5, int i, String str6, int i2, String str7, List<Record> list) {
        if (str == null) {
            throw new NullPointerException("Null name");
        }
        this.name = str;
        this.id = str2;
        this.tnf = str3;
        this.type = str4;
        this.prefix = str5;
        this.prefixLength = i;
        this.suffix = str6;
        this.suffixLength = i2;
        this.payload = str7;
        if (list == null) {
            throw new NullPointerException("Null records");
        }
        this.records = list;
    }

    public String name() {
        return this.name;
    }

    public String id() {
        return this.id;
    }

    public String tnf() {
        return this.tnf;
    }

    public String type() {
        return this.type;
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
        String str2 = this.id;
        String str3 = this.tnf;
        String str4 = this.type;
        String str5 = this.prefix;
        int i = this.prefixLength;
        String str6 = this.suffix;
        int i2 = this.suffixLength;
        String str7 = this.payload;
        String valueOf = String.valueOf(this.records);
        return new StringBuilder((((((((String.valueOf(str).length() + 121) + String.valueOf(str2).length()) + String.valueOf(str3).length()) + String.valueOf(str4).length()) + String.valueOf(str5).length()) + String.valueOf(str6).length()) + String.valueOf(str7).length()) + String.valueOf(valueOf).length()).append("Record{name=").append(str).append(", id=").append(str2).append(", tnf=").append(str3).append(", type=").append(str4).append(", prefix=").append(str5).append(", prefixLength=").append(i).append(", suffix=").append(str6).append(", suffixLength=").append(i2).append(", payload=").append(str7).append(", records=").append(valueOf).append("}").toString();
    }

    C$AutoValue_Record(String str, String str2, String str3, String str4, String str5, int i, String str6, int i2, String str7, List<Record> list, byte b) {
        this(str, str2, str3, str4, str5, i, str6, i2, str7, list);
    }
}
