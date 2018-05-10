package com.google.commerce.tapandpay.merchantapp.smarttap.v1;

import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback.RedeemableEntity.Type;
import java.util.Arrays;

final class AutoValue_MerchantSmartTapCallback_MerchantRedeemableEntity extends MerchantRedeemableEntity {
    private final Long programId;
    private final Type type;
    private final byte[] value;

    AutoValue_MerchantSmartTapCallback_MerchantRedeemableEntity(Type type, Long l, byte[] bArr) {
        if (type == null) {
            throw new NullPointerException("Null type");
        }
        this.type = type;
        this.programId = l;
        if (bArr == null) {
            throw new NullPointerException("Null value");
        }
        this.value = bArr;
    }

    public Type type() {
        return this.type;
    }

    public Long programId() {
        return this.programId;
    }

    public byte[] value() {
        return this.value;
    }

    public String toString() {
        String valueOf = String.valueOf(this.type);
        String valueOf2 = String.valueOf(this.programId);
        String valueOf3 = String.valueOf(Arrays.toString(this.value));
        return new StringBuilder(((String.valueOf(valueOf).length() + 51) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()).append("MerchantRedeemableEntity{type=").append(valueOf).append(", programId=").append(valueOf2).append(", value=").append(valueOf3).append("}").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MerchantRedeemableEntity)) {
            return false;
        }
        boolean z;
        MerchantRedeemableEntity merchantRedeemableEntity = (MerchantRedeemableEntity) obj;
        if (this.type.equals(merchantRedeemableEntity.type()) && (this.programId != null ? this.programId.equals(merchantRedeemableEntity.programId()) : merchantRedeemableEntity.programId() == null)) {
            if (Arrays.equals(this.value, merchantRedeemableEntity instanceof AutoValue_MerchantSmartTapCallback_MerchantRedeemableEntity ? ((AutoValue_MerchantSmartTapCallback_MerchantRedeemableEntity) merchantRedeemableEntity).value : merchantRedeemableEntity.value())) {
                z = true;
                return z;
            }
        }
        z = false;
        return z;
    }

    public int hashCode() {
        return (((this.programId == null ? 0 : this.programId.hashCode()) ^ ((this.type.hashCode() ^ 1000003) * 1000003)) * 1000003) ^ Arrays.hashCode(this.value);
    }
}
