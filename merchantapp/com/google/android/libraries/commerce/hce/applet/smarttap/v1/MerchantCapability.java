package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.util.EnumMap;

public enum MerchantCapability {
    LOYALTY_SUPPORT(7),
    SECONDARY_LOYALTY(6),
    OFFERS_SUPPORT(5),
    ADDITIONAL_OFFERS(4),
    CONTACTLESS_PAYMENT(3),
    ENTERPRISE_MERCHANT_ID(2),
    CLOUD_BASED_OFFERS(1),
    POST_TRANSACTION_DATA(0);
    
    private static final FormattingLogger LOG = null;
    private final int bytePosition;

    static {
        LOG = FormattingLoggers.newContextLogger();
    }

    private MerchantCapability(int i) {
        this.bytePosition = i;
    }

    public static EnumMap<MerchantCapability, Boolean> parse(byte[] bArr) {
        EnumMap<MerchantCapability, Boolean> enumMap = new EnumMap(MerchantCapability.class);
        if (bArr.length > 0) {
            byte b = bArr[0];
            for (Enum enumR : values()) {
                boolean z;
                if (((b >> enumR.bytePosition) & 1) == 1) {
                    z = true;
                } else {
                    z = false;
                }
                enumMap.put(enumR, Boolean.valueOf(z));
            }
        } else {
            LOG.i("No merchant capabilities were received", new Object[0]);
        }
        return enumMap;
    }
}
