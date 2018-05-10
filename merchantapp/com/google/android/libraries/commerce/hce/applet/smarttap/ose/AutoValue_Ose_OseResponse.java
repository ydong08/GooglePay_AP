package com.google.android.libraries.commerce.hce.applet.smarttap.ose;

import com.google.android.libraries.commerce.hce.applet.smarttap.ose.Ose.OseResponse;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.common.base.Optional;
import java.util.List;

final class AutoValue_Ose_OseResponse extends OseResponse {
    private final List<AidInfo> aidInfos;
    private final Optional<ByteArrayWrapper> mobileDeviceNonce;
    private final Optional<TransactionalDetails> transactionalDetails;

    AutoValue_Ose_OseResponse(Optional<TransactionalDetails> optional, Optional<ByteArrayWrapper> optional2, List<AidInfo> list) {
        if (optional == null) {
            throw new NullPointerException("Null transactionalDetails");
        }
        this.transactionalDetails = optional;
        if (optional2 == null) {
            throw new NullPointerException("Null mobileDeviceNonce");
        }
        this.mobileDeviceNonce = optional2;
        if (list == null) {
            throw new NullPointerException("Null aidInfos");
        }
        this.aidInfos = list;
    }

    public Optional<TransactionalDetails> transactionalDetails() {
        return this.transactionalDetails;
    }

    public Optional<ByteArrayWrapper> mobileDeviceNonce() {
        return this.mobileDeviceNonce;
    }

    public List<AidInfo> aidInfos() {
        return this.aidInfos;
    }

    public String toString() {
        String valueOf = String.valueOf(this.transactionalDetails);
        String valueOf2 = String.valueOf(this.mobileDeviceNonce);
        String valueOf3 = String.valueOf(this.aidInfos);
        return new StringBuilder(((String.valueOf(valueOf).length() + 65) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()).append("OseResponse{transactionalDetails=").append(valueOf).append(", mobileDeviceNonce=").append(valueOf2).append(", aidInfos=").append(valueOf3).append("}").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OseResponse)) {
            return false;
        }
        OseResponse oseResponse = (OseResponse) obj;
        if (this.transactionalDetails.equals(oseResponse.transactionalDetails()) && this.mobileDeviceNonce.equals(oseResponse.mobileDeviceNonce()) && this.aidInfos.equals(oseResponse.aidInfos())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((((this.transactionalDetails.hashCode() ^ 1000003) * 1000003) ^ this.mobileDeviceNonce.hashCode()) * 1000003) ^ this.aidInfos.hashCode();
    }
}
