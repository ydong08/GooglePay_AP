package com.google.android.libraries.commerce.hce.applet.smarttap.ose;

import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.common.base.Optional;

final class AutoValue_SmartTap2ProprietaryData extends SmartTap2ProprietaryData {
    private final Optional<Short> maxVersion;
    private final Optional<Short> minVersion;
    private final Optional<ByteArrayWrapper> mobileDeviceNonce;
    private final boolean supportsSkippingSelect;

    AutoValue_SmartTap2ProprietaryData(Optional<Short> optional, Optional<Short> optional2, Optional<ByteArrayWrapper> optional3, boolean z) {
        if (optional == null) {
            throw new NullPointerException("Null minVersion");
        }
        this.minVersion = optional;
        if (optional2 == null) {
            throw new NullPointerException("Null maxVersion");
        }
        this.maxVersion = optional2;
        if (optional3 == null) {
            throw new NullPointerException("Null mobileDeviceNonce");
        }
        this.mobileDeviceNonce = optional3;
        this.supportsSkippingSelect = z;
    }

    public Optional<Short> minVersion() {
        return this.minVersion;
    }

    public Optional<Short> maxVersion() {
        return this.maxVersion;
    }

    public Optional<ByteArrayWrapper> mobileDeviceNonce() {
        return this.mobileDeviceNonce;
    }

    public boolean supportsSkippingSelect() {
        return this.supportsSkippingSelect;
    }

    public String toString() {
        String valueOf = String.valueOf(this.minVersion);
        String valueOf2 = String.valueOf(this.maxVersion);
        String valueOf3 = String.valueOf(this.mobileDeviceNonce);
        return new StringBuilder(((String.valueOf(valueOf).length() + 100) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()).append("SmartTap2ProprietaryData{minVersion=").append(valueOf).append(", maxVersion=").append(valueOf2).append(", mobileDeviceNonce=").append(valueOf3).append(", supportsSkippingSelect=").append(this.supportsSkippingSelect).append("}").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SmartTap2ProprietaryData)) {
            return false;
        }
        SmartTap2ProprietaryData smartTap2ProprietaryData = (SmartTap2ProprietaryData) obj;
        if (this.minVersion.equals(smartTap2ProprietaryData.minVersion()) && this.maxVersion.equals(smartTap2ProprietaryData.maxVersion()) && this.mobileDeviceNonce.equals(smartTap2ProprietaryData.mobileDeviceNonce()) && this.supportsSkippingSelect == smartTap2ProprietaryData.supportsSkippingSelect()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.supportsSkippingSelect ? 1231 : 1237) ^ ((((((this.minVersion.hashCode() ^ 1000003) * 1000003) ^ this.maxVersion.hashCode()) * 1000003) ^ this.mobileDeviceNonce.hashCode()) * 1000003);
    }
}
