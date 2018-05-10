package com.google.commerce.tapandpay.merchantapp.validation;

import java.util.ArrayList;

class C$AutoValue_Schema extends Schema {
    private final String name;
    private final ArrayList<Session> sessions;
    private final Type type;

    static final class Builder extends com.google.commerce.tapandpay.merchantapp.validation.Schema.Builder {
        private String name;
        private ArrayList<Session> sessions;
        private Type type;

        Builder() {
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Schema.Builder setName(String str) {
            this.name = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Schema.Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Schema.Builder setSessions(ArrayList<Session> arrayList) {
            this.sessions = arrayList;
            return this;
        }

        public Schema build() {
            String str = "";
            if (this.name == null) {
                str = String.valueOf(str).concat(" name");
            }
            if (this.type == null) {
                str = String.valueOf(str).concat(" type");
            }
            if (this.sessions == null) {
                str = String.valueOf(str).concat(" sessions");
            }
            if (str.isEmpty()) {
                return new C$AutoValue_Schema(this.name, this.type, this.sessions, (byte) 0);
            }
            String str2 = "Missing required properties:";
            str = String.valueOf(str);
            throw new IllegalStateException(str.length() != 0 ? str2.concat(str) : new String(str2));
        }
    }

    C$AutoValue_Schema(String str, Type type, ArrayList<Session> arrayList) {
        if (str == null) {
            throw new NullPointerException("Null name");
        }
        this.name = str;
        if (type == null) {
            throw new NullPointerException("Null type");
        }
        this.type = type;
        if (arrayList == null) {
            throw new NullPointerException("Null sessions");
        }
        this.sessions = arrayList;
    }

    public String name() {
        return this.name;
    }

    public Type type() {
        return this.type;
    }

    public ArrayList<Session> sessions() {
        return this.sessions;
    }

    C$AutoValue_Schema(String str, Type type, ArrayList<Session> arrayList, byte b) {
        this(str, type, arrayList);
    }
}
