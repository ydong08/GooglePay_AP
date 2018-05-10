package com.google.commerce.tapandpay.merchantapp.smarttap.v2;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefRecord;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.apps.common.inject.annotation.ApplicationContext;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.MerchantInfo;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NewService;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Request;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Type;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceStatus;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapCallback;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier.AuthenticationState;
import com.google.android.libraries.commerce.hce.crypto.ValuablesCryptoException;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Bcd;
import com.google.commerce.tapandpay.merchantapp.settings.Settings;
import com.google.commerce.tapandpay.merchantapp.smarttap.MerchantSmartTapCallbackHelper;
import com.google.commerce.tapandpay.merchantapp.testcase.EncodingValue;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.internal.tapandpay.v1.valuables.nano.CommonProto.SmartTap;
import java.math.BigInteger;
import java.security.interfaces.ECPublicKey;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

public class MerchantSmartTapCallback implements SmartTapCallback {
    private final Context context;
    private final SmartTap2ECKeyManager keyManager;
    private final Settings settings;
    private final TestCaseHelper testCaseHelper;

    @Inject
    public MerchantSmartTapCallback(@ApplicationContext Context context, TestCaseHelper testCaseHelper, Settings settings, SmartTap2ECKeyManager smartTap2ECKeyManager) {
        this.context = context;
        this.testCaseHelper = testCaseHelper;
        this.settings = settings;
        this.keyManager = smartTap2ECKeyManager;
    }

    public byte[] getMinVersion() {
        TestCase testCase = getTestCase();
        return testCase == null ? null : testCase.smartTapMinVersion();
    }

    public byte[] getMaxVersion() {
        TestCase testCase = getTestCase();
        return testCase == null ? null : testCase.smartTapMaxVersion();
    }

    public ECPublicKey getLongTermPublicKey(long j, int i) {
        ECPublicKey eCPublicKey = null;
        TestCase testCase = getTestCase();
        if (testCase != null && (!testCase.checkMerchantId() || j == testCase.merchantId())) {
            byte[] merchantPublicKey = testCase.merchantPublicKey();
            if (merchantPublicKey != null) {
                try {
                    eCPublicKey = this.keyManager.decodeCompressedPublicKey(merchantPublicKey);
                } catch (ValuablesCryptoException e) {
                }
            }
        }
        return eCPublicKey;
    }

    public Set<ServiceObject> getServiceObjects(MerchantInfo merchantInfo, boolean z, AuthenticationState authenticationState) {
        Object merchantValuables = getMerchantValuables(merchantInfo, z, authenticationState);
        if (z) {
            Intent intent = new Intent("add_valuables_action");
            intent.putExtra("merchant_valuables", merchantValuables);
            LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent);
        }
        return new HashSet(merchantValuables);
    }

    private HashSet<MerchantValuable> getMerchantValuables(MerchantInfo merchantInfo, boolean z, AuthenticationState authenticationState) {
        TestCase testCase = getTestCase();
        if (testCase == null) {
            return new HashSet();
        }
        if (testCase.checkMerchantId() && merchantInfo.getMerchantId() != testCase.merchantId()) {
            return new HashSet();
        }
        if (testCase.requireEncryption() && !z) {
            return new HashSet();
        }
        if (testCase.requireEncryption() && authenticationState != AuthenticationState.LIVE_AUTH && authenticationState != AuthenticationState.PRESIGNED_AUTH) {
            return new HashSet();
        }
        if (testCase.requireLiveAuthentication() && authenticationState != AuthenticationState.LIVE_AUTH) {
            return new HashSet();
        }
        HashSet<MerchantValuable> hashSet = new HashSet();
        for (Request request : merchantInfo.getRequestedServiceObjects()) {
            if (request.getType() == Type.WALLET_CUSTOMER) {
                hashSet.add(MerchantValuable.newCustomer(MerchantSmartTapCallbackHelper.getConsumerId(this.context, this.testCaseHelper, merchantInfo.getMerchantId()), getBinaryId(testCase.tapId()), getBcdId(testCase.deviceId())));
            } else {
                for (SmartTap smartTap : testCase.smartTaps()) {
                    if (includeValuable(request, getType(smartTap), this.settings)) {
                        hashSet.add(createValuable(smartTap, getProgramId(merchantInfo, smartTap)));
                    }
                }
            }
        }
        return hashSet;
    }

    public Code processPushBackData(List<ServiceStatus> list, List<NewService> list2, Optional<Long> optional) {
        return Code.SUCCESS;
    }

    public Multimap<ByteArrayWrapper, NdefRecord> getAddedNdefRecords() {
        TestCase testCase = getTestCase();
        if (testCase == null) {
            return ImmutableMultimap.of();
        }
        return testCase.addedNdefs();
    }

    public Set<ByteArrayWrapper> getRemovedNdefRecords() {
        TestCase testCase = getTestCase();
        if (testCase == null) {
            return ImmutableSet.of();
        }
        return testCase.removedNdefs();
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
                return Type.GIFT_CARD;
            case 4:
                return Type.CLOSED_LOOP_CARD;
            default:
                throw new IllegalArgumentException("Unsupported type was specified.");
        }
    }

    private static byte[] getBinaryId(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }
        return new BigInteger(str).toByteArray();
    }

    private static byte[] getBcdId(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }
        return Bcd.encode(str);
    }

    private static Long getProgramId(MerchantInfo merchantInfo, SmartTap smartTap) {
        if (smartTap.programId > 0) {
            return Long.valueOf(smartTap.programId);
        }
        return Long.valueOf(merchantInfo.getMerchantId());
    }

    private static boolean includeValuable(Request request, Type type, Settings settings) {
        boolean sendLoyalty = settings.getSendLoyalty();
        boolean sendOffer = settings.getSendOffer();
        boolean sendGiftCard = settings.getSendGiftCard();
        boolean sendPlc = settings.getSendPlc();
        switch (request.getType()) {
            case ALL:
            case ALL_EXCEPT_PPSE:
                if (type == Type.LOYALTY && !sendLoyalty) {
                    return false;
                }
                if (type == Type.OFFER && !sendOffer) {
                    return false;
                }
                if (type == Type.GIFT_CARD && !sendGiftCard) {
                    return false;
                }
                if (type != Type.CLOSED_LOOP_CARD || sendPlc) {
                    return true;
                }
                return false;
            case LOYALTY:
                if (type == Type.LOYALTY && sendLoyalty) {
                    return true;
                }
                return false;
            case OFFER:
                if (type == Type.OFFER && sendOffer) {
                    return true;
                }
                return false;
            case GIFT_CARD:
                if (type == Type.GIFT_CARD && sendGiftCard) {
                    return true;
                }
                return false;
            case CLOSED_LOOP_CARD:
                if (type == Type.CLOSED_LOOP_CARD && sendPlc) {
                    return true;
                }
                return false;
            case PPSE:
            case CLOUD_WALLET:
            case MOBILE_MARKETING:
            case WALLET_CUSTOMER:
                return false;
            default:
                String valueOf = String.valueOf(request.getType());
                throw new IllegalArgumentException(new StringBuilder(String.valueOf(valueOf).length() + 32).append("Unsupported type ").append(valueOf).append(" was requested.").toString());
        }
    }

    private static MerchantValuable createValuable(SmartTap smartTap, Long l) {
        EncodingValue valueEncoding = EncodingValue.getValueEncoding(smartTap);
        switch (smartTap.type) {
            case 1:
                return MerchantValuable.newLoyaltyProgram(l, valueEncoding);
            case 2:
                return MerchantValuable.newOffer(l, valueEncoding);
            case 3:
                return MerchantValuable.newGiftCard(l, valueEncoding, EncodingValue.getPinEncoding(smartTap));
            case 4:
                return MerchantValuable.newPlc(l, valueEncoding, EncodingValue.getCvcEncoding(smartTap), smartTap.expirationMonth, smartTap.expirationYear);
            default:
                throw new IllegalArgumentException("MerchantValuable requested from unsupported type.");
        }
    }

    private TestCase getTestCase() {
        return this.testCaseHelper.readFullTestCase(PreferenceManager.getDefaultSharedPreferences(this.context).getLong("active_testcase", -1));
    }
}
