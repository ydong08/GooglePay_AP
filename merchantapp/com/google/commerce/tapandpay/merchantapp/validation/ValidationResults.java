package com.google.commerce.tapandpay.merchantapp.validation;

import com.google.commerce.tapandpay.merchantapp.validation.AutoValue_ValidationResults.GsonTypeAdapter;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public abstract class ValidationResults implements Serializable {
    private static final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(ValidationAutoValueTypeAdapterFactory.create()).setPrettyPrinting().create();

    public static abstract class Builder {
        public abstract ValidationResults build();

        public abstract ArrayList<ValidationResults> nestedResults();

        public abstract Builder setMessage(String str);

        public abstract Builder setNestedResults(ArrayList<ValidationResults> arrayList);

        public abstract Builder setStatus(Status status);

        public abstract Builder setThrowable(Throwable th);

        public abstract Status status();

        public Builder addNestedResult(ValidationResults validationResults) {
            setStatus(status().and(validationResults.status()));
            nestedResults().add(validationResults);
            return this;
        }

        public static Builder builder() {
            return new Builder().setStatus(Status.UNKNOWN).setNestedResults(new ArrayList());
        }
    }

    public enum Status {
        UNKNOWN(R.string.unknown_schema_status),
        NO_SCHEMA(R.string.no_schema),
        SUCCESS(R.string.schema_success),
        FAILURE(R.string.schema_failure),
        INVALID(R.string.schema_invalid);
        
        private final int stringResId;

        private Status(int i) {
            this.stringResId = i;
        }

        public int stringResId() {
            return this.stringResId;
        }

        public Status and(Status status) {
            if (this == NO_SCHEMA) {
                return status;
            }
            if (status == NO_SCHEMA) {
                return this;
            }
            if (this == UNKNOWN || status == UNKNOWN) {
                return UNKNOWN;
            }
            if (this == INVALID || status == INVALID) {
                return INVALID;
            }
            if (this == FAILURE || status == FAILURE) {
                return FAILURE;
            }
            if (this == SUCCESS && status == SUCCESS) {
                return SUCCESS;
            }
            return UNKNOWN;
        }
    }

    public abstract String message();

    public abstract ArrayList<ValidationResults> nestedResults();

    public abstract Status status();

    public abstract Throwable throwable();

    public static ValidationResults create(Status status) {
        return Builder.builder().setStatus(status).build();
    }

    public static ValidationResults create(Status status, String str, Object... objArr) {
        return Builder.builder().setStatus(status).setMessage(String.format(str, objArr)).build();
    }

    public static ValidationResults create(Status status, Throwable th, String str, Object... objArr) {
        return Builder.builder().setStatus(status).setThrowable(th).setMessage(String.format(str, objArr)).build();
    }

    public static ValidationResults fromString(String str) throws JsonSyntaxException {
        if (Strings.isNullOrEmpty(str)) {
            return Builder.builder().setStatus(Status.UNKNOWN).build();
        }
        return (ValidationResults) GSON.fromJson(str, ValidationResults.class);
    }

    public String toJson() {
        return GSON.toJson((Object) this);
    }

    public String toString() {
        return toJson();
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        ValidationResults validationResults = (ValidationResults) obj;
        if (nestedResults() != null && validationResults.nestedResults() != null && Objects.equals(status(), validationResults.status()) && Objects.equals(message(), validationResults.message()) && throwablesEqual(throwable(), validationResults.throwable()) && Arrays.equals(nestedResults().toArray(new ValidationResults[0]), validationResults.nestedResults().toArray(new ValidationResults[0]))) {
            return true;
        }
        return false;
    }

    private static boolean throwablesEqual(Throwable th, Throwable th2) {
        if (th == null && th2 == null) {
            return true;
        }
        if (th == null || th2 == null) {
            return false;
        }
        return Objects.equals(th.toString(), th2.toString());
    }

    public int hashCode() {
        return com.google.common.base.Objects.hashCode(status(), message(), throwable(), nestedResults());
    }

    public static TypeAdapter<ValidationResults> typeAdapter(Gson gson) {
        return new GsonTypeAdapter(gson);
    }
}
