package com.google.commerce.tapandpay.merchantapp.testcase;

import com.google.commerce.tapandpay.merchantapp.testcase.EncodingValue.Encoding;

final class AutoValue_EncodingValue extends EncodingValue {
    private final Encoding encoding;
    private final String value;

    AutoValue_EncodingValue(String str, Encoding encoding) {
        if (str == null) {
            throw new NullPointerException("Null value");
        }
        this.value = str;
        if (encoding == null) {
            throw new NullPointerException("Null encoding");
        }
        this.encoding = encoding;
    }

    public String getValue() {
        return this.value;
    }

    public Encoding getEncoding() {
        return this.encoding;
    }

    public String toString() {
        String str = this.value;
        String valueOf = String.valueOf(this.encoding);
        return new StringBuilder((String.valueOf(str).length() + 32) + String.valueOf(valueOf).length()).append("EncodingValue{value=").append(str).append(", encoding=").append(valueOf).append("}").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EncodingValue)) {
            return false;
        }
        EncodingValue encodingValue = (EncodingValue) obj;
        if (this.value.equals(encodingValue.getValue()) && this.encoding.equals(encodingValue.getEncoding())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((this.value.hashCode() ^ 1000003) * 1000003) ^ this.encoding.hashCode();
    }
}
