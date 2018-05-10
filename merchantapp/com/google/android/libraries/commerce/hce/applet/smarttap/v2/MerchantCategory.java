package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

public enum MerchantCategory {
    UNKNOWN(0, 0),
    AGRICULTURAL_SERVICES(1, 1499),
    CONTRACTED_SERVICES(1500, 2999),
    AIRLINES(3000, 3299),
    CAR_RENTAL(3300, 3499),
    LODGING(3500, 3999),
    TRANSPORTATION_SERVICES(4000, 4799),
    UTILITY_SERVICES(4800, 4999),
    RETAIL_OUTLET_SERVICES(5000, 5599),
    CLOTHING_STORES(5600, 5699),
    MISCELLANEOUS_STORES(5700, 7299),
    BUSINESS_SERVICES(7300, 7999),
    PROFESSIONAL_SERVICES_MEMBERSHIP_ORGANIZATIONS(8000, 8999),
    GOVERNMENT_SERVICES(9000, 9999);
    
    private static final Set<MerchantCategory> TRAVEL_AND_ENTERTAINMENT = null;
    private final int max;
    private final int min;

    static {
        TRAVEL_AND_ENTERTAINMENT = ImmutableSet.of(AIRLINES, CAR_RENTAL, LODGING);
    }

    private MerchantCategory(int i, int i2) {
        this.min = i;
        this.max = i2;
    }

    public static MerchantCategory get(int i) {
        for (MerchantCategory merchantCategory : values()) {
            if (i <= merchantCategory.max && merchantCategory.min <= i) {
                return merchantCategory;
            }
        }
        return UNKNOWN;
    }
}
