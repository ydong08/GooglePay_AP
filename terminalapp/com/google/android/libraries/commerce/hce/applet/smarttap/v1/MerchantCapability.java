package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.util.Set;

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

    public static byte[] compose(Set<MerchantCapability> set) {
        byte[] bArr = new byte[2];
        for (MerchantCapability merchantCapability : set) {
            bArr[0] = (byte) ((1 << merchantCapability.bytePosition) | bArr[0]);
        }
        return bArr;
    }
}
