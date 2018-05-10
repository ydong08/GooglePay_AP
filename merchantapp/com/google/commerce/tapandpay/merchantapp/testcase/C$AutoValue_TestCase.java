package com.google.commerce.tapandpay.merchantapp.testcase;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase.Builder;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase.GetSmartTapDataResponse;
import com.google.commerce.tapandpay.merchantapp.validation.Schema;
import com.google.common.collect.Multimap;
import com.google.internal.tapandpay.v1.valuables.nano.CommonProto.SmartTap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract class C$AutoValue_TestCase extends TestCase {
    private final Multimap<ByteArrayWrapper, NdefRecord> addedNdefs;
    private final boolean allowSkippingSmartTap2Select;
    private final boolean checkMerchantId;
    private final String consumerId;
    private final byte[] customOseStatusWord;
    private final Map<Byte, byte[]> customStatuses;
    private final String deviceId;
    private final String expectedTerminalBehavior;
    private final GetSmartTapDataResponse getDataResponseStatus;
    private final long id;
    private final boolean includeMasterNonceInOseResponse;
    private final boolean includeNonceInProprietaryData;
    private final Map<Byte, Integer> maxCustomStatuses;
    private final long merchantId;
    private final byte[] merchantPublicKey;
    private final String name;
    private final boolean oseCustom;
    private final Aid oseCustomAid;
    private final int oseCustomPriority;
    private final boolean oseEnabled;
    private final boolean oseSmartTap13;
    private final int oseSmartTap13Priority;
    private final boolean oseSmartTap20;
    private final int oseSmartTap20Priority;
    private final boolean overrideTransactionMode;
    private final boolean paymentEnabled;
    private final boolean paymentRequested;
    private final Set<ByteArrayWrapper> removedNdefs;
    private final boolean requireEncryption;
    private final boolean requireLiveAuthentication;
    private final byte[] smartTapMaxVersion;
    private final byte[] smartTapMinVersion;
    private final List<SmartTap> smartTaps;
    private final CommandType stopPaymentsCommand;
    private final boolean supportEcies;
    private final boolean supportGenericKeyAuth;
    private final boolean supportSmartTap13;
    private final boolean supportSmartTap20;
    private final String tapId;
    private final String tapVideoUri;
    private final long testSuiteId;
    private final boolean useCustomOseStatusWord;
    private final boolean useMerchantCapabilities;
    private final Schema validationSchema;
    private final boolean vasEnabled;
    private final boolean vasRequested;

    C$AutoValue_TestCase(String str, String str2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9, int i, boolean z10, int i2, boolean z11, Aid aid, int i3, boolean z12, boolean z13, boolean z14, boolean z15, boolean z16, byte[] bArr, byte[] bArr2, String str3, String str4, String str5, long j, boolean z17, boolean z18, boolean z19, boolean z20, GetSmartTapDataResponse getSmartTapDataResponse, CommandType commandType, Schema schema, String str6, byte[] bArr3, boolean z21, byte[] bArr4, Map<Byte, byte[]> map, Map<Byte, Integer> map2, Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, List<SmartTap> list, long j2, long j3) {
        if (str == null) {
            throw new NullPointerException("Null name");
        }
        this.name = str;
        this.expectedTerminalBehavior = str2;
        this.supportGenericKeyAuth = z;
        this.supportEcies = z2;
        this.overrideTransactionMode = z3;
        this.paymentEnabled = z4;
        this.paymentRequested = z5;
        this.vasEnabled = z6;
        this.vasRequested = z7;
        this.oseEnabled = z8;
        this.oseSmartTap13 = z9;
        this.oseSmartTap13Priority = i;
        this.oseSmartTap20 = z10;
        this.oseSmartTap20Priority = i2;
        this.oseCustom = z11;
        if (aid == null) {
            throw new NullPointerException("Null oseCustomAid");
        }
        this.oseCustomAid = aid;
        this.oseCustomPriority = i3;
        this.supportSmartTap13 = z12;
        this.supportSmartTap20 = z13;
        this.allowSkippingSmartTap2Select = z14;
        this.includeMasterNonceInOseResponse = z15;
        this.includeNonceInProprietaryData = z16;
        if (bArr == null) {
            throw new NullPointerException("Null smartTapMinVersion");
        }
        this.smartTapMinVersion = bArr;
        if (bArr2 == null) {
            throw new NullPointerException("Null smartTapMaxVersion");
        }
        this.smartTapMaxVersion = bArr2;
        this.consumerId = str3;
        this.tapId = str4;
        this.deviceId = str5;
        this.merchantId = j;
        this.useMerchantCapabilities = z17;
        this.checkMerchantId = z18;
        this.requireEncryption = z19;
        this.requireLiveAuthentication = z20;
        this.getDataResponseStatus = getSmartTapDataResponse;
        this.stopPaymentsCommand = commandType;
        this.validationSchema = schema;
        this.tapVideoUri = str6;
        this.merchantPublicKey = bArr3;
        this.useCustomOseStatusWord = z21;
        this.customOseStatusWord = bArr4;
        if (map == null) {
            throw new NullPointerException("Null customStatuses");
        }
        this.customStatuses = map;
        if (map2 == null) {
            throw new NullPointerException("Null maxCustomStatuses");
        }
        this.maxCustomStatuses = map2;
        if (multimap == null) {
            throw new NullPointerException("Null addedNdefs");
        }
        this.addedNdefs = multimap;
        if (set == null) {
            throw new NullPointerException("Null removedNdefs");
        }
        this.removedNdefs = set;
        if (list == null) {
            throw new NullPointerException("Null smartTaps");
        }
        this.smartTaps = list;
        this.testSuiteId = j2;
        this.id = j3;
    }

    public String name() {
        return this.name;
    }

    public String expectedTerminalBehavior() {
        return this.expectedTerminalBehavior;
    }

    public boolean supportGenericKeyAuth() {
        return this.supportGenericKeyAuth;
    }

    public boolean supportEcies() {
        return this.supportEcies;
    }

    public boolean overrideTransactionMode() {
        return this.overrideTransactionMode;
    }

    public boolean paymentEnabled() {
        return this.paymentEnabled;
    }

    public boolean paymentRequested() {
        return this.paymentRequested;
    }

    public boolean vasEnabled() {
        return this.vasEnabled;
    }

    public boolean vasRequested() {
        return this.vasRequested;
    }

    public boolean oseEnabled() {
        return this.oseEnabled;
    }

    public boolean oseSmartTap13() {
        return this.oseSmartTap13;
    }

    public int oseSmartTap13Priority() {
        return this.oseSmartTap13Priority;
    }

    public boolean oseSmartTap20() {
        return this.oseSmartTap20;
    }

    public int oseSmartTap20Priority() {
        return this.oseSmartTap20Priority;
    }

    public boolean oseCustom() {
        return this.oseCustom;
    }

    public Aid oseCustomAid() {
        return this.oseCustomAid;
    }

    public int oseCustomPriority() {
        return this.oseCustomPriority;
    }

    public boolean supportSmartTap13() {
        return this.supportSmartTap13;
    }

    public boolean supportSmartTap20() {
        return this.supportSmartTap20;
    }

    public boolean allowSkippingSmartTap2Select() {
        return this.allowSkippingSmartTap2Select;
    }

    public boolean includeMasterNonceInOseResponse() {
        return this.includeMasterNonceInOseResponse;
    }

    public boolean includeNonceInProprietaryData() {
        return this.includeNonceInProprietaryData;
    }

    public byte[] smartTapMinVersion() {
        return this.smartTapMinVersion;
    }

    public byte[] smartTapMaxVersion() {
        return this.smartTapMaxVersion;
    }

    public String consumerId() {
        return this.consumerId;
    }

    public String tapId() {
        return this.tapId;
    }

    public String deviceId() {
        return this.deviceId;
    }

    public long merchantId() {
        return this.merchantId;
    }

    public boolean useMerchantCapabilities() {
        return this.useMerchantCapabilities;
    }

    public boolean checkMerchantId() {
        return this.checkMerchantId;
    }

    public boolean requireEncryption() {
        return this.requireEncryption;
    }

    public boolean requireLiveAuthentication() {
        return this.requireLiveAuthentication;
    }

    public GetSmartTapDataResponse getDataResponseStatus() {
        return this.getDataResponseStatus;
    }

    public CommandType stopPaymentsCommand() {
        return this.stopPaymentsCommand;
    }

    public Schema validationSchema() {
        return this.validationSchema;
    }

    public String tapVideoUri() {
        return this.tapVideoUri;
    }

    public byte[] merchantPublicKey() {
        return this.merchantPublicKey;
    }

    public boolean useCustomOseStatusWord() {
        return this.useCustomOseStatusWord;
    }

    public byte[] customOseStatusWord() {
        return this.customOseStatusWord;
    }

    public Map<Byte, byte[]> customStatuses() {
        return this.customStatuses;
    }

    public Map<Byte, Integer> maxCustomStatuses() {
        return this.maxCustomStatuses;
    }

    public Multimap<ByteArrayWrapper, NdefRecord> addedNdefs() {
        return this.addedNdefs;
    }

    public Set<ByteArrayWrapper> removedNdefs() {
        return this.removedNdefs;
    }

    public List<SmartTap> smartTaps() {
        return this.smartTaps;
    }

    public long testSuiteId() {
        return this.testSuiteId;
    }

    public long id() {
        return this.id;
    }

    public String toString() {
        String str = this.name;
        String str2 = this.expectedTerminalBehavior;
        boolean z = this.supportGenericKeyAuth;
        boolean z2 = this.supportEcies;
        boolean z3 = this.overrideTransactionMode;
        boolean z4 = this.paymentEnabled;
        boolean z5 = this.paymentRequested;
        boolean z6 = this.vasEnabled;
        boolean z7 = this.vasRequested;
        boolean z8 = this.oseEnabled;
        boolean z9 = this.oseSmartTap13;
        int i = this.oseSmartTap13Priority;
        boolean z10 = this.oseSmartTap20;
        int i2 = this.oseSmartTap20Priority;
        boolean z11 = this.oseCustom;
        String valueOf = String.valueOf(this.oseCustomAid);
        int i3 = this.oseCustomPriority;
        boolean z12 = this.supportSmartTap13;
        boolean z13 = this.supportSmartTap20;
        boolean z14 = this.allowSkippingSmartTap2Select;
        boolean z15 = this.includeMasterNonceInOseResponse;
        boolean z16 = this.includeNonceInProprietaryData;
        String valueOf2 = String.valueOf(Arrays.toString(this.smartTapMinVersion));
        String valueOf3 = String.valueOf(Arrays.toString(this.smartTapMaxVersion));
        String str3 = this.consumerId;
        String str4 = this.tapId;
        String str5 = this.deviceId;
        long j = this.merchantId;
        boolean z17 = this.useMerchantCapabilities;
        boolean z18 = this.checkMerchantId;
        boolean z19 = this.requireEncryption;
        boolean z20 = this.requireLiveAuthentication;
        String valueOf4 = String.valueOf(this.getDataResponseStatus);
        String valueOf5 = String.valueOf(this.stopPaymentsCommand);
        String valueOf6 = String.valueOf(this.validationSchema);
        String str6 = this.tapVideoUri;
        String valueOf7 = String.valueOf(Arrays.toString(this.merchantPublicKey));
        boolean z21 = this.useCustomOseStatusWord;
        String valueOf8 = String.valueOf(Arrays.toString(this.customOseStatusWord));
        String valueOf9 = String.valueOf(this.customStatuses);
        String valueOf10 = String.valueOf(this.maxCustomStatuses);
        String valueOf11 = String.valueOf(this.addedNdefs);
        String valueOf12 = String.valueOf(this.removedNdefs);
        String valueOf13 = String.valueOf(this.smartTaps);
        long j2 = this.testSuiteId;
        return new StringBuilder(((((((((((((((((((String.valueOf(str).length() + 1067) + String.valueOf(str2).length()) + String.valueOf(valueOf).length()) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()) + String.valueOf(str3).length()) + String.valueOf(str4).length()) + String.valueOf(str5).length()) + String.valueOf(valueOf4).length()) + String.valueOf(valueOf5).length()) + String.valueOf(valueOf6).length()) + String.valueOf(str6).length()) + String.valueOf(valueOf7).length()) + String.valueOf(valueOf8).length()) + String.valueOf(valueOf9).length()) + String.valueOf(valueOf10).length()) + String.valueOf(valueOf11).length()) + String.valueOf(valueOf12).length()) + String.valueOf(valueOf13).length()).append("TestCase{name=").append(str).append(", expectedTerminalBehavior=").append(str2).append(", supportGenericKeyAuth=").append(z).append(", supportEcies=").append(z2).append(", overrideTransactionMode=").append(z3).append(", paymentEnabled=").append(z4).append(", paymentRequested=").append(z5).append(", vasEnabled=").append(z6).append(", vasRequested=").append(z7).append(", oseEnabled=").append(z8).append(", oseSmartTap13=").append(z9).append(", oseSmartTap13Priority=").append(i).append(", oseSmartTap20=").append(z10).append(", oseSmartTap20Priority=").append(i2).append(", oseCustom=").append(z11).append(", oseCustomAid=").append(valueOf).append(", oseCustomPriority=").append(i3).append(", supportSmartTap13=").append(z12).append(", supportSmartTap20=").append(z13).append(", allowSkippingSmartTap2Select=").append(z14).append(", includeMasterNonceInOseResponse=").append(z15).append(", includeNonceInProprietaryData=").append(z16).append(", smartTapMinVersion=").append(valueOf2).append(", smartTapMaxVersion=").append(valueOf3).append(", consumerId=").append(str3).append(", tapId=").append(str4).append(", deviceId=").append(str5).append(", merchantId=").append(j).append(", useMerchantCapabilities=").append(z17).append(", checkMerchantId=").append(z18).append(", requireEncryption=").append(z19).append(", requireLiveAuthentication=").append(z20).append(", getDataResponseStatus=").append(valueOf4).append(", stopPaymentsCommand=").append(valueOf5).append(", validationSchema=").append(valueOf6).append(", tapVideoUri=").append(str6).append(", merchantPublicKey=").append(valueOf7).append(", useCustomOseStatusWord=").append(z21).append(", customOseStatusWord=").append(valueOf8).append(", customStatuses=").append(valueOf9).append(", maxCustomStatuses=").append(valueOf10).append(", addedNdefs=").append(valueOf11).append(", removedNdefs=").append(valueOf12).append(", smartTaps=").append(valueOf13).append(", testSuiteId=").append(j2).append(", id=").append(this.id).append("}").toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r9) {
        /*
        r8 = this;
        r1 = 1;
        r2 = 0;
        if (r9 != r8) goto L_0x0005;
    L_0x0004:
        return r1;
    L_0x0005:
        r0 = r9 instanceof com.google.commerce.tapandpay.merchantapp.testcase.TestCase;
        if (r0 == 0) goto L_0x025d;
    L_0x0009:
        r9 = (com.google.commerce.tapandpay.merchantapp.testcase.TestCase) r9;
        r0 = r8.name;
        r3 = r9.name();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x0017:
        r0 = r8.expectedTerminalBehavior;
        if (r0 != 0) goto L_0x01d5;
    L_0x001b:
        r0 = r9.expectedTerminalBehavior();
        if (r0 != 0) goto L_0x01e1;
    L_0x0021:
        r0 = r8.supportGenericKeyAuth;
        r3 = r9.supportGenericKeyAuth();
        if (r0 != r3) goto L_0x01e1;
    L_0x0029:
        r0 = r8.supportEcies;
        r3 = r9.supportEcies();
        if (r0 != r3) goto L_0x01e1;
    L_0x0031:
        r0 = r8.overrideTransactionMode;
        r3 = r9.overrideTransactionMode();
        if (r0 != r3) goto L_0x01e1;
    L_0x0039:
        r0 = r8.paymentEnabled;
        r3 = r9.paymentEnabled();
        if (r0 != r3) goto L_0x01e1;
    L_0x0041:
        r0 = r8.paymentRequested;
        r3 = r9.paymentRequested();
        if (r0 != r3) goto L_0x01e1;
    L_0x0049:
        r0 = r8.vasEnabled;
        r3 = r9.vasEnabled();
        if (r0 != r3) goto L_0x01e1;
    L_0x0051:
        r0 = r8.vasRequested;
        r3 = r9.vasRequested();
        if (r0 != r3) goto L_0x01e1;
    L_0x0059:
        r0 = r8.oseEnabled;
        r3 = r9.oseEnabled();
        if (r0 != r3) goto L_0x01e1;
    L_0x0061:
        r0 = r8.oseSmartTap13;
        r3 = r9.oseSmartTap13();
        if (r0 != r3) goto L_0x01e1;
    L_0x0069:
        r0 = r8.oseSmartTap13Priority;
        r3 = r9.oseSmartTap13Priority();
        if (r0 != r3) goto L_0x01e1;
    L_0x0071:
        r0 = r8.oseSmartTap20;
        r3 = r9.oseSmartTap20();
        if (r0 != r3) goto L_0x01e1;
    L_0x0079:
        r0 = r8.oseSmartTap20Priority;
        r3 = r9.oseSmartTap20Priority();
        if (r0 != r3) goto L_0x01e1;
    L_0x0081:
        r0 = r8.oseCustom;
        r3 = r9.oseCustom();
        if (r0 != r3) goto L_0x01e1;
    L_0x0089:
        r0 = r8.oseCustomAid;
        r3 = r9.oseCustomAid();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x0095:
        r0 = r8.oseCustomPriority;
        r3 = r9.oseCustomPriority();
        if (r0 != r3) goto L_0x01e1;
    L_0x009d:
        r0 = r8.supportSmartTap13;
        r3 = r9.supportSmartTap13();
        if (r0 != r3) goto L_0x01e1;
    L_0x00a5:
        r0 = r8.supportSmartTap20;
        r3 = r9.supportSmartTap20();
        if (r0 != r3) goto L_0x01e1;
    L_0x00ad:
        r0 = r8.allowSkippingSmartTap2Select;
        r3 = r9.allowSkippingSmartTap2Select();
        if (r0 != r3) goto L_0x01e1;
    L_0x00b5:
        r0 = r8.includeMasterNonceInOseResponse;
        r3 = r9.includeMasterNonceInOseResponse();
        if (r0 != r3) goto L_0x01e1;
    L_0x00bd:
        r0 = r8.includeNonceInProprietaryData;
        r3 = r9.includeNonceInProprietaryData();
        if (r0 != r3) goto L_0x01e1;
    L_0x00c5:
        r3 = r8.smartTapMinVersion;
        r0 = r9 instanceof com.google.commerce.tapandpay.merchantapp.testcase.C$AutoValue_TestCase;
        if (r0 == 0) goto L_0x01e3;
    L_0x00cb:
        r0 = r9;
        r0 = (com.google.commerce.tapandpay.merchantapp.testcase.C$AutoValue_TestCase) r0;
        r0 = r0.smartTapMinVersion;
    L_0x00d0:
        r0 = java.util.Arrays.equals(r3, r0);
        if (r0 == 0) goto L_0x01e1;
    L_0x00d6:
        r3 = r8.smartTapMaxVersion;
        r0 = r9 instanceof com.google.commerce.tapandpay.merchantapp.testcase.C$AutoValue_TestCase;
        if (r0 == 0) goto L_0x01e9;
    L_0x00dc:
        r0 = r9;
        r0 = (com.google.commerce.tapandpay.merchantapp.testcase.C$AutoValue_TestCase) r0;
        r0 = r0.smartTapMaxVersion;
    L_0x00e1:
        r0 = java.util.Arrays.equals(r3, r0);
        if (r0 == 0) goto L_0x01e1;
    L_0x00e7:
        r0 = r8.consumerId;
        if (r0 != 0) goto L_0x01ef;
    L_0x00eb:
        r0 = r9.consumerId();
        if (r0 != 0) goto L_0x01e1;
    L_0x00f1:
        r0 = r8.tapId;
        if (r0 != 0) goto L_0x01fd;
    L_0x00f5:
        r0 = r9.tapId();
        if (r0 != 0) goto L_0x01e1;
    L_0x00fb:
        r0 = r8.deviceId;
        if (r0 != 0) goto L_0x020b;
    L_0x00ff:
        r0 = r9.deviceId();
        if (r0 != 0) goto L_0x01e1;
    L_0x0105:
        r4 = r8.merchantId;
        r6 = r9.merchantId();
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 != 0) goto L_0x01e1;
    L_0x010f:
        r0 = r8.useMerchantCapabilities;
        r3 = r9.useMerchantCapabilities();
        if (r0 != r3) goto L_0x01e1;
    L_0x0117:
        r0 = r8.checkMerchantId;
        r3 = r9.checkMerchantId();
        if (r0 != r3) goto L_0x01e1;
    L_0x011f:
        r0 = r8.requireEncryption;
        r3 = r9.requireEncryption();
        if (r0 != r3) goto L_0x01e1;
    L_0x0127:
        r0 = r8.requireLiveAuthentication;
        r3 = r9.requireLiveAuthentication();
        if (r0 != r3) goto L_0x01e1;
    L_0x012f:
        r0 = r8.getDataResponseStatus;
        if (r0 != 0) goto L_0x0219;
    L_0x0133:
        r0 = r9.getDataResponseStatus();
        if (r0 != 0) goto L_0x01e1;
    L_0x0139:
        r0 = r8.stopPaymentsCommand;
        if (r0 != 0) goto L_0x0227;
    L_0x013d:
        r0 = r9.stopPaymentsCommand();
        if (r0 != 0) goto L_0x01e1;
    L_0x0143:
        r0 = r8.validationSchema;
        if (r0 != 0) goto L_0x0235;
    L_0x0147:
        r0 = r9.validationSchema();
        if (r0 != 0) goto L_0x01e1;
    L_0x014d:
        r0 = r8.tapVideoUri;
        if (r0 != 0) goto L_0x0243;
    L_0x0151:
        r0 = r9.tapVideoUri();
        if (r0 != 0) goto L_0x01e1;
    L_0x0157:
        r3 = r8.merchantPublicKey;
        r0 = r9 instanceof com.google.commerce.tapandpay.merchantapp.testcase.C$AutoValue_TestCase;
        if (r0 == 0) goto L_0x0251;
    L_0x015d:
        r0 = r9;
        r0 = (com.google.commerce.tapandpay.merchantapp.testcase.C$AutoValue_TestCase) r0;
        r0 = r0.merchantPublicKey;
    L_0x0162:
        r0 = java.util.Arrays.equals(r3, r0);
        if (r0 == 0) goto L_0x01e1;
    L_0x0168:
        r0 = r8.useCustomOseStatusWord;
        r3 = r9.useCustomOseStatusWord();
        if (r0 != r3) goto L_0x01e1;
    L_0x0170:
        r3 = r8.customOseStatusWord;
        r0 = r9 instanceof com.google.commerce.tapandpay.merchantapp.testcase.C$AutoValue_TestCase;
        if (r0 == 0) goto L_0x0257;
    L_0x0176:
        r0 = r9;
        r0 = (com.google.commerce.tapandpay.merchantapp.testcase.C$AutoValue_TestCase) r0;
        r0 = r0.customOseStatusWord;
    L_0x017b:
        r0 = java.util.Arrays.equals(r3, r0);
        if (r0 == 0) goto L_0x01e1;
    L_0x0181:
        r0 = r8.customStatuses;
        r3 = r9.customStatuses();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x018d:
        r0 = r8.maxCustomStatuses;
        r3 = r9.maxCustomStatuses();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x0199:
        r0 = r8.addedNdefs;
        r3 = r9.addedNdefs();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x01a5:
        r0 = r8.removedNdefs;
        r3 = r9.removedNdefs();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x01b1:
        r0 = r8.smartTaps;
        r3 = r9.smartTaps();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x01bd:
        r4 = r8.testSuiteId;
        r6 = r9.testSuiteId();
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 != 0) goto L_0x01e1;
    L_0x01c7:
        r4 = r8.id;
        r6 = r9.id();
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 != 0) goto L_0x01e1;
    L_0x01d1:
        r0 = r1;
    L_0x01d2:
        r1 = r0;
        goto L_0x0004;
    L_0x01d5:
        r0 = r8.expectedTerminalBehavior;
        r3 = r9.expectedTerminalBehavior();
        r0 = r0.equals(r3);
        if (r0 != 0) goto L_0x0021;
    L_0x01e1:
        r0 = r2;
        goto L_0x01d2;
    L_0x01e3:
        r0 = r9.smartTapMinVersion();
        goto L_0x00d0;
    L_0x01e9:
        r0 = r9.smartTapMaxVersion();
        goto L_0x00e1;
    L_0x01ef:
        r0 = r8.consumerId;
        r3 = r9.consumerId();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x01fb:
        goto L_0x00f1;
    L_0x01fd:
        r0 = r8.tapId;
        r3 = r9.tapId();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x0209:
        goto L_0x00fb;
    L_0x020b:
        r0 = r8.deviceId;
        r3 = r9.deviceId();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x0217:
        goto L_0x0105;
    L_0x0219:
        r0 = r8.getDataResponseStatus;
        r3 = r9.getDataResponseStatus();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x0225:
        goto L_0x0139;
    L_0x0227:
        r0 = r8.stopPaymentsCommand;
        r3 = r9.stopPaymentsCommand();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x0233:
        goto L_0x0143;
    L_0x0235:
        r0 = r8.validationSchema;
        r3 = r9.validationSchema();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x0241:
        goto L_0x014d;
    L_0x0243:
        r0 = r8.tapVideoUri;
        r3 = r9.tapVideoUri();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x01e1;
    L_0x024f:
        goto L_0x0157;
    L_0x0251:
        r0 = r9.merchantPublicKey();
        goto L_0x0162;
    L_0x0257:
        r0 = r9.customOseStatusWord();
        goto L_0x017b;
    L_0x025d:
        r1 = r2;
        goto L_0x0004;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.commerce.tapandpay.merchantapp.testcase.$AutoValue_TestCase.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        int i3 = 1231;
        int hashCode = ((this.expectedTerminalBehavior == null ? 0 : this.expectedTerminalBehavior.hashCode()) ^ ((this.name.hashCode() ^ 1000003) * 1000003)) * 1000003;
        if (this.supportGenericKeyAuth) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.supportEcies) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.overrideTransactionMode) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.paymentEnabled) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.paymentRequested) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.vasEnabled) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.vasRequested) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.oseEnabled) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.oseSmartTap13) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (((i ^ hashCode) * 1000003) ^ this.oseSmartTap13Priority) * 1000003;
        if (this.oseSmartTap20) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (((i ^ hashCode) * 1000003) ^ this.oseSmartTap20Priority) * 1000003;
        if (this.oseCustom) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (((((i ^ hashCode) * 1000003) ^ this.oseCustomAid.hashCode()) * 1000003) ^ this.oseCustomPriority) * 1000003;
        if (this.supportSmartTap13) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.supportSmartTap20) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.allowSkippingSmartTap2Select) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.includeMasterNonceInOseResponse) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.includeNonceInProprietaryData) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = ((int) (((long) (((this.deviceId == null ? 0 : this.deviceId.hashCode()) ^ (((this.tapId == null ? 0 : this.tapId.hashCode()) ^ (((this.consumerId == null ? 0 : this.consumerId.hashCode()) ^ ((((((i ^ hashCode) * 1000003) ^ Arrays.hashCode(this.smartTapMinVersion)) * 1000003) ^ Arrays.hashCode(this.smartTapMaxVersion)) * 1000003)) * 1000003)) * 1000003)) * 1000003)) ^ ((this.merchantId >>> 32) ^ this.merchantId))) * 1000003;
        if (this.useMerchantCapabilities) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.checkMerchantId) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.requireEncryption) {
            i = 1231;
        } else {
            i = 1237;
        }
        hashCode = (i ^ hashCode) * 1000003;
        if (this.requireLiveAuthentication) {
            i = 1231;
        } else {
            i = 1237;
        }
        i = ((this.validationSchema == null ? 0 : this.validationSchema.hashCode()) ^ (((this.stopPaymentsCommand == null ? 0 : this.stopPaymentsCommand.hashCode()) ^ (((this.getDataResponseStatus == null ? 0 : this.getDataResponseStatus.hashCode()) ^ ((i ^ hashCode) * 1000003)) * 1000003)) * 1000003)) * 1000003;
        if (this.tapVideoUri != null) {
            i2 = this.tapVideoUri.hashCode();
        }
        i = (((i ^ i2) * 1000003) ^ Arrays.hashCode(this.merchantPublicKey)) * 1000003;
        if (!this.useCustomOseStatusWord) {
            i3 = 1237;
        }
        return (int) (((long) (((int) (((long) ((((((((((((((i ^ i3) * 1000003) ^ Arrays.hashCode(this.customOseStatusWord)) * 1000003) ^ this.customStatuses.hashCode()) * 1000003) ^ this.maxCustomStatuses.hashCode()) * 1000003) ^ this.addedNdefs.hashCode()) * 1000003) ^ this.removedNdefs.hashCode()) * 1000003) ^ this.smartTaps.hashCode()) * 1000003)) ^ ((this.testSuiteId >>> 32) ^ this.testSuiteId))) * 1000003)) ^ ((this.id >>> 32) ^ this.id));
    }

    public Builder toBuilder() {
        return new Builder();
    }
}
