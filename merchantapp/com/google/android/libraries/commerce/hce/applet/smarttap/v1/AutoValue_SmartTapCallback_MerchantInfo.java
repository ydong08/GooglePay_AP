package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback.MerchantInfo;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Date;

final class AutoValue_SmartTapCallback_MerchantInfo extends MerchantInfo {
    private final ImmutableMap<MerchantCapability, Boolean> capabilities;
    private final long merchantId;
    private final ImmutableList<Long> secondaryLoyalties;
    private final String storeId;
    private final Date terminalTime;

    AutoValue_SmartTapCallback_MerchantInfo(long j, String str, ImmutableList<Long> immutableList, Date date, ImmutableMap<MerchantCapability, Boolean> immutableMap) {
        this.merchantId = j;
        if (str == null) {
            throw new NullPointerException("Null storeId");
        }
        this.storeId = str;
        if (immutableList == null) {
            throw new NullPointerException("Null secondaryLoyalties");
        }
        this.secondaryLoyalties = immutableList;
        if (date == null) {
            throw new NullPointerException("Null terminalTime");
        }
        this.terminalTime = date;
        if (immutableMap == null) {
            throw new NullPointerException("Null capabilities");
        }
        this.capabilities = immutableMap;
    }

    public long merchantId() {
        return this.merchantId;
    }

    public String storeId() {
        return this.storeId;
    }

    public ImmutableList<Long> secondaryLoyalties() {
        return this.secondaryLoyalties;
    }

    public Date terminalTime() {
        return this.terminalTime;
    }

    public ImmutableMap<MerchantCapability, Boolean> capabilities() {
        return this.capabilities;
    }

    public String toString() {
        long j = this.merchantId;
        String str = this.storeId;
        String valueOf = String.valueOf(this.secondaryLoyalties);
        String valueOf2 = String.valueOf(this.terminalTime);
        String valueOf3 = String.valueOf(this.capabilities);
        return new StringBuilder((((String.valueOf(str).length() + 106) + String.valueOf(valueOf).length()) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()).append("MerchantInfo{merchantId=").append(j).append(", storeId=").append(str).append(", secondaryLoyalties=").append(valueOf).append(", terminalTime=").append(valueOf2).append(", capabilities=").append(valueOf3).append("}").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MerchantInfo)) {
            return false;
        }
        MerchantInfo merchantInfo = (MerchantInfo) obj;
        if (this.merchantId == merchantInfo.merchantId() && this.storeId.equals(merchantInfo.storeId()) && this.secondaryLoyalties.equals(merchantInfo.secondaryLoyalties()) && this.terminalTime.equals(merchantInfo.terminalTime()) && this.capabilities.equals(merchantInfo.capabilities())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((((int) (((long) 1000003) ^ ((this.merchantId >>> 32) ^ this.merchantId))) * 1000003) ^ this.storeId.hashCode()) * 1000003) ^ this.secondaryLoyalties.hashCode()) * 1000003) ^ this.terminalTime.hashCode()) * 1000003) ^ this.capabilities.hashCode();
    }
}
