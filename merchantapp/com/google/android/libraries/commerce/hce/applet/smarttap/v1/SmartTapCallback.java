package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SmartTapCallback {

    public static abstract class MerchantInfo {
        public abstract ImmutableMap<MerchantCapability, Boolean> capabilities();

        public abstract long merchantId();

        public abstract ImmutableList<Long> secondaryLoyalties();

        public abstract String storeId();

        public abstract Date terminalTime();

        public static MerchantInfo create(long j, String str, List<Long> list, Date date, Map<MerchantCapability, Boolean> map) {
            return new AutoValue_SmartTapCallback_MerchantInfo(j, str, ImmutableList.copyOf((Collection) list), new Date(date.getTime()), ImmutableMap.copyOf((Map) map));
        }
    }

    public interface RedeemableEntity {

        public enum Type {
            LOYALTY,
            OFFER
        }

        Long programId();

        Type type();

        byte[] value();
    }

    byte[] getConsumerId(long j);

    Set<RedeemableEntity> getRedemptionInfos(MerchantInfo merchantInfo);
}
