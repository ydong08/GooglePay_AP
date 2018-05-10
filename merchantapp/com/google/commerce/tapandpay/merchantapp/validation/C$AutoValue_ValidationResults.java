package com.google.commerce.tapandpay.merchantapp.validation;

import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Status;
import java.util.ArrayList;

abstract class C$AutoValue_ValidationResults extends ValidationResults {
    private final String message;
    private final ArrayList<ValidationResults> nestedResults;
    private final Status status;
    private final Throwable throwable;

    static final class Builder extends com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder {
        private String message;
        private ArrayList<ValidationResults> nestedResults;
        private Status status;
        private Throwable throwable;

        Builder() {
        }

        public com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Status status() {
            if (this.status != null) {
                return this.status;
            }
            throw new IllegalStateException("Property \"status\" has not been set");
        }

        public com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder setMessage(String str) {
            this.message = str;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder setThrowable(Throwable th) {
            this.throwable = th;
            return this;
        }

        public com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder setNestedResults(ArrayList<ValidationResults> arrayList) {
            this.nestedResults = arrayList;
            return this;
        }

        public ArrayList<ValidationResults> nestedResults() {
            if (this.nestedResults != null) {
                return this.nestedResults;
            }
            throw new IllegalStateException("Property \"nestedResults\" has not been set");
        }

        public ValidationResults build() {
            String str = "";
            if (this.status == null) {
                str = String.valueOf(str).concat(" status");
            }
            if (this.nestedResults == null) {
                str = String.valueOf(str).concat(" nestedResults");
            }
            if (str.isEmpty()) {
                return new AutoValue_ValidationResults(this.status, this.message, this.throwable, this.nestedResults);
            }
            String str2 = "Missing required properties:";
            str = String.valueOf(str);
            throw new IllegalStateException(str.length() != 0 ? str2.concat(str) : new String(str2));
        }
    }

    C$AutoValue_ValidationResults(Status status, String str, Throwable th, ArrayList<ValidationResults> arrayList) {
        if (status == null) {
            throw new NullPointerException("Null status");
        }
        this.status = status;
        this.message = str;
        this.throwable = th;
        if (arrayList == null) {
            throw new NullPointerException("Null nestedResults");
        }
        this.nestedResults = arrayList;
    }

    public Status status() {
        return this.status;
    }

    public String message() {
        return this.message;
    }

    public Throwable throwable() {
        return this.throwable;
    }

    public ArrayList<ValidationResults> nestedResults() {
        return this.nestedResults;
    }
}
