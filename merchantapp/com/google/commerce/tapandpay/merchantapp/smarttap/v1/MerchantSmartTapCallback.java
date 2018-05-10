package com.google.commerce.tapandpay.merchantapp.smarttap.v1;

import android.content.Context;
import android.preference.PreferenceManager;
import com.google.android.apps.common.inject.annotation.ApplicationContext;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.MerchantCapability;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback.MerchantInfo;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback.RedeemableEntity;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback.RedeemableEntity.Type;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.merchantapp.settings.Settings;
import com.google.commerce.tapandpay.merchantapp.smarttap.MerchantSmartTapCallbackHelper;
import com.google.commerce.tapandpay.merchantapp.testcase.EncodingValue;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import com.google.internal.tapandpay.v1.valuables.nano.CommonProto.SmartTap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;

public class MerchantSmartTapCallback implements SmartTapCallback {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final Context context;
    private final Settings settings;
    private final TestCaseHelper testCaseHelper;

    static abstract class MerchantRedeemableEntity implements RedeemableEntity {
        public abstract byte[] value();

        MerchantRedeemableEntity() {
        }

        static MerchantRedeemableEntity create(Type type, Long l, byte[] bArr) {
            return new AutoValue_MerchantSmartTapCallback_MerchantRedeemableEntity(type, l, bArr);
        }
    }

    @Inject
    public MerchantSmartTapCallback(@ApplicationContext Context context, TestCaseHelper testCaseHelper, Settings settings) {
        this.context = context;
        this.testCaseHelper = testCaseHelper;
        this.settings = settings;
    }

    public byte[] getConsumerId(long j) {
        return MerchantSmartTapCallbackHelper.getConsumerId(this.context, this.testCaseHelper, j);
    }

    public Set<RedeemableEntity> getRedemptionInfos(MerchantInfo merchantInfo) {
        Set<RedeemableEntity> hashSet = new HashSet();
        TestCase readFullTestCase = this.testCaseHelper.readFullTestCase(PreferenceManager.getDefaultSharedPreferences(this.context).getLong("active_testcase", -1));
        if (readFullTestCase != null) {
            Map capabilities = merchantInfo.capabilities();
            boolean sendLoyalty = this.settings.getSendLoyalty();
            boolean sendOffer = this.settings.getSendOffer();
            if (readFullTestCase.useMerchantCapabilities()) {
                int i;
                int i2 = (sendLoyalty && capabilities.containsKey(MerchantCapability.LOYALTY_SUPPORT) && ((Boolean) capabilities.get(MerchantCapability.LOYALTY_SUPPORT)).booleanValue()) ? 1 : 0;
                if (sendOffer && capabilities.containsKey(MerchantCapability.OFFERS_SUPPORT) && ((Boolean) capabilities.get(MerchantCapability.OFFERS_SUPPORT)).booleanValue()) {
                    i = 1;
                } else {
                    i = 0;
                }
                int i3 = i;
                int i4 = i2;
            } else {
                boolean z = sendOffer;
                sendOffer = sendLoyalty;
            }
            for (SmartTap smartTap : readFullTestCase.smartTaps()) {
                if (smartTap.type == 3 || smartTap.type == 4) {
                    FormattingLogger formattingLogger = LOG;
                    String str = "Gift cards and plc are not transmitted in SmartTap v1.3. Ignoring valuable with ID: ";
                    String valueOf = String.valueOf(EncodingValue.getValueEncoding(smartTap).getValue());
                    formattingLogger.w(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), new Object[0]);
                } else {
                    Long valueOf2;
                    EncodingValue valueEncoding = EncodingValue.getValueEncoding(smartTap);
                    Type type = getType(smartTap);
                    if (smartTap.programId > 0) {
                        valueOf2 = Long.valueOf(smartTap.programId);
                    } else {
                        valueOf2 = null;
                    }
                    if (type == Type.LOYALTY && r3 != 0) {
                        hashSet.add(MerchantRedeemableEntity.create(type, Long.valueOf(valueOf2 == null ? merchantInfo.merchantId() : valueOf2.longValue()), valueEncoding.getByteValueWithFormat()));
                    } else if (type == Type.OFFER && r1 != 0) {
                        hashSet.add(MerchantRedeemableEntity.create(type, valueOf2, valueEncoding.getByteValueWithFormat()));
                    }
                }
            }
        }
        return hashSet;
    }

    private static Type getType(SmartTap smartTap) {
        switch (smartTap.type) {
            case 0:
                throw new IllegalArgumentException("No valid type was specified.");
            case 1:
                return Type.LOYALTY;
            case 2:
                return Type.OFFER;
            case 3:
            case 4:
                throw new IllegalArgumentException("Gift cards and PLC are not supported in SmartTap v1.3.");
            default:
                throw new IllegalArgumentException("Unsupported type was specified.");
        }
    }
}
