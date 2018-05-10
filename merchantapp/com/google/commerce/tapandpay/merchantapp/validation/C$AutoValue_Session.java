package com.google.commerce.tapandpay.merchantapp.validation;

import java.util.ArrayList;

class C$AutoValue_Session extends Session {
    private final ArrayList<Apdu> apdus;
    private final String name;

    static final class Builder extends com.google.commerce.tapandpay.merchantapp.validation.Session.Builder {
        private ArrayList<Apdu> apdus;
        private String name;

        Builder() {
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Session.Builder setName(String str) {
            this.name = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.Session.Builder setApdus(ArrayList<Apdu> arrayList) {
            this.apdus = arrayList;
            return this;
        }

        public Session build() {
            String str = "";
            if (this.name == null) {
                str = String.valueOf(str).concat(" name");
            }
            if (this.apdus == null) {
                str = String.valueOf(str).concat(" apdus");
            }
            if (str.isEmpty()) {
                return new C$AutoValue_Session(this.name, this.apdus, (byte) 0);
            }
            String str2 = "Missing required properties:";
            str = String.valueOf(str);
            throw new IllegalStateException(str.length() != 0 ? str2.concat(str) : new String(str2));
        }
    }

    C$AutoValue_Session(String str, ArrayList<Apdu> arrayList) {
        if (str == null) {
            throw new NullPointerException("Null name");
        }
        this.name = str;
        if (arrayList == null) {
            throw new NullPointerException("Null apdus");
        }
        this.apdus = arrayList;
    }

    public String name() {
        return this.name;
    }

    public ArrayList<Apdu> apdus() {
        return this.apdus;
    }

    public String toString() {
        String str = this.name;
        String valueOf = String.valueOf(this.apdus);
        return new StringBuilder((String.valueOf(str).length() + 22) + String.valueOf(valueOf).length()).append("Session{name=").append(str).append(", apdus=").append(valueOf).append("}").toString();
    }

    C$AutoValue_Session(String str, ArrayList<Apdu> arrayList, byte b) {
        this(str, arrayList);
    }
}
