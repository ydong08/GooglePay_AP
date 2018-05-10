package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback.RedeemableEntity;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCommand.Response;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.common.base.Objects;
import java.util.Set;

public class GetSmartTapDataResponse extends Response {
    private final long merchantId;
    private final Set<RedeemableEntity> transmittedCards;

    public GetSmartTapDataResponse(ResponseApdu responseApdu, Set<RedeemableEntity> set, long j) {
        super(responseApdu);
        this.transmittedCards = set;
        this.merchantId = j;
    }

    public Set<RedeemableEntity> getTransmittedCards() {
        return this.transmittedCards;
    }

    public long getMerchantId() {
        return this.merchantId;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GetSmartTapDataResponse)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        GetSmartTapDataResponse getSmartTapDataResponse = (GetSmartTapDataResponse) obj;
        if (Objects.equal(this.transmittedCards, getSmartTapDataResponse.transmittedCards) && Objects.equal(getResponseApdu(), getSmartTapDataResponse.getResponseApdu()) && Objects.equal(Long.valueOf(this.merchantId), Long.valueOf(getSmartTapDataResponse.merchantId))) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(super.hashCode()), this.transmittedCards, getResponseApdu(), Long.valueOf(this.merchantId));
    }
}
