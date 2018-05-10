package com.google.commerce.tapandpay.merchantapp.testcase;

import android.content.Context;
import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.json.Json;
import com.google.commerce.tapandpay.merchantapp.testcase.AutoValue_TestCase.GsonTypeAdapter;
import com.google.commerce.tapandpay.merchantapp.validation.Schema;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.internal.tapandpay.v1.valuables.nano.CommonProto.SmartTap;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class TestCase implements Serializable {
    private static final Gson GSON = Json.getGsonBuilder().registerTypeAdapterFactory(TestCaseTypeAdapterFactory.create()).registerTypeHierarchyAdapter(Multimap.class, new MultimapAdapter()).enableComplexMapKeySerialization().create();
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();

    public static abstract class Builder {
        private Multimap<ByteArrayWrapper, NdefRecord> addedNdefs;
        private Boolean allowSkippingSmartTap2Select;
        private Boolean checkMerchantId;
        private String consumerId;
        private byte[] customOseStatusWord;
        private Map<Byte, byte[]> customStatuses;
        private String deviceId;
        private String expectedTerminalBehavior;
        private GetSmartTapDataResponse getDataResponseStatus;
        private Long id;
        private Boolean includeMasterNonceInOseResponse;
        private Boolean includeNonceInProprietaryData;
        private Map<Byte, Integer> maxCustomStatuses;
        private Long merchantId;
        private byte[] merchantPublicKey;
        private String name;
        private Boolean oseCustom;
        private Aid oseCustomAid;
        private Integer oseCustomPriority;
        private Boolean oseEnabled;
        private Boolean oseSmartTap13;
        private Integer oseSmartTap13Priority;
        private Boolean oseSmartTap20;
        private Integer oseSmartTap20Priority;
        private Boolean overrideTransactionMode;
        private Boolean paymentEnabled;
        private Boolean paymentRequested;
        private Set<ByteArrayWrapper> removedNdefs;
        private Boolean requireEncryption;
        private Boolean requireLiveAuthentication;
        private byte[] smartTapMaxVersion;
        private byte[] smartTapMinVersion;
        private List<SmartTap> smartTaps;
        private CommandType stopPaymentsCommand;
        private Boolean supportEcies;
        private Boolean supportGenericKeyAuth;
        private Boolean supportSmartTap13;
        private Boolean supportSmartTap20;
        private String tapId;
        private String tapVideoUri;
        private Long testSuiteId;
        private Boolean useCustomOseStatusWord;
        private Boolean useMerchantCapabilities;
        private Schema validationSchema;
        private Boolean vasEnabled;
        private Boolean vasRequested;

        Builder(byte b) {
            this();
        }

        public Builder setName(String str) {
            this.name = str;
            return this;
        }

        public Builder setExpectedTerminalBehavior(String str) {
            this.expectedTerminalBehavior = str;
            return this;
        }

        public Builder setSupportGenericKeyAuth(boolean z) {
            this.supportGenericKeyAuth = Boolean.valueOf(z);
            return this;
        }

        public Builder setSupportEcies(boolean z) {
            this.supportEcies = Boolean.valueOf(z);
            return this;
        }

        public Builder setOverrideTransactionMode(boolean z) {
            this.overrideTransactionMode = Boolean.valueOf(z);
            return this;
        }

        public Builder setPaymentEnabled(boolean z) {
            this.paymentEnabled = Boolean.valueOf(z);
            return this;
        }

        public Builder setPaymentRequested(boolean z) {
            this.paymentRequested = Boolean.valueOf(z);
            return this;
        }

        public Builder setVasEnabled(boolean z) {
            this.vasEnabled = Boolean.valueOf(z);
            return this;
        }

        public Builder setVasRequested(boolean z) {
            this.vasRequested = Boolean.valueOf(z);
            return this;
        }

        public Builder setOseEnabled(boolean z) {
            this.oseEnabled = Boolean.valueOf(z);
            return this;
        }

        public Builder setOseSmartTap13(boolean z) {
            this.oseSmartTap13 = Boolean.valueOf(z);
            return this;
        }

        public Builder setOseSmartTap13Priority(int i) {
            this.oseSmartTap13Priority = Integer.valueOf(i);
            return this;
        }

        public Builder setOseSmartTap20(boolean z) {
            this.oseSmartTap20 = Boolean.valueOf(z);
            return this;
        }

        public Builder setOseSmartTap20Priority(int i) {
            this.oseSmartTap20Priority = Integer.valueOf(i);
            return this;
        }

        public Builder setOseCustom(boolean z) {
            this.oseCustom = Boolean.valueOf(z);
            return this;
        }

        public Builder setOseCustomAid(Aid aid) {
            this.oseCustomAid = aid;
            return this;
        }

        public Builder setOseCustomPriority(int i) {
            this.oseCustomPriority = Integer.valueOf(i);
            return this;
        }

        public Builder setSupportSmartTap13(boolean z) {
            this.supportSmartTap13 = Boolean.valueOf(z);
            return this;
        }

        public Builder setSupportSmartTap20(boolean z) {
            this.supportSmartTap20 = Boolean.valueOf(z);
            return this;
        }

        public Builder setAllowSkippingSmartTap2Select(boolean z) {
            this.allowSkippingSmartTap2Select = Boolean.valueOf(z);
            return this;
        }

        public Builder setIncludeMasterNonceInOseResponse(boolean z) {
            this.includeMasterNonceInOseResponse = Boolean.valueOf(z);
            return this;
        }

        public Builder setIncludeNonceInProprietaryData(boolean z) {
            this.includeNonceInProprietaryData = Boolean.valueOf(z);
            return this;
        }

        public Builder setSmartTapMinVersion(byte[] bArr) {
            this.smartTapMinVersion = bArr;
            return this;
        }

        public Builder setSmartTapMaxVersion(byte[] bArr) {
            this.smartTapMaxVersion = bArr;
            return this;
        }

        public Builder setConsumerId(String str) {
            this.consumerId = str;
            return this;
        }

        public Builder setTapId(String str) {
            this.tapId = str;
            return this;
        }

        public Builder setDeviceId(String str) {
            this.deviceId = str;
            return this;
        }

        public Builder setMerchantId(long j) {
            this.merchantId = Long.valueOf(j);
            return this;
        }

        public Builder setUseMerchantCapabilities(boolean z) {
            this.useMerchantCapabilities = Boolean.valueOf(z);
            return this;
        }

        public Builder setCheckMerchantId(boolean z) {
            this.checkMerchantId = Boolean.valueOf(z);
            return this;
        }

        public Builder setRequireEncryption(boolean z) {
            this.requireEncryption = Boolean.valueOf(z);
            return this;
        }

        public Builder setRequireLiveAuthentication(boolean z) {
            this.requireLiveAuthentication = Boolean.valueOf(z);
            return this;
        }

        public Builder setGetDataResponseStatus(GetSmartTapDataResponse getSmartTapDataResponse) {
            this.getDataResponseStatus = getSmartTapDataResponse;
            return this;
        }

        public Builder setStopPaymentsCommand(CommandType commandType) {
            this.stopPaymentsCommand = commandType;
            return this;
        }

        public Builder setValidationSchema(Schema schema) {
            this.validationSchema = schema;
            return this;
        }

        public Builder setTapVideoUri(String str) {
            this.tapVideoUri = str;
            return this;
        }

        public Builder setMerchantPublicKey(byte[] bArr) {
            this.merchantPublicKey = bArr;
            return this;
        }

        public Builder setUseCustomOseStatusWord(boolean z) {
            this.useCustomOseStatusWord = Boolean.valueOf(z);
            return this;
        }

        public Builder setCustomOseStatusWord(byte[] bArr) {
            this.customOseStatusWord = bArr;
            return this;
        }

        public Builder setCustomStatuses(Map<Byte, byte[]> map) {
            this.customStatuses = map;
            return this;
        }

        public Builder setMaxCustomStatuses(Map<Byte, Integer> map) {
            this.maxCustomStatuses = map;
            return this;
        }

        public Builder setAddedNdefs(Multimap<ByteArrayWrapper, NdefRecord> multimap) {
            this.addedNdefs = multimap;
            return this;
        }

        public Builder setRemovedNdefs(Set<ByteArrayWrapper> set) {
            this.removedNdefs = set;
            return this;
        }

        public Builder setSmartTaps(List<SmartTap> list) {
            this.smartTaps = list;
            return this;
        }

        public Builder setTestSuiteId(long j) {
            this.testSuiteId = Long.valueOf(j);
            return this;
        }

        public Builder setId(long j) {
            this.id = Long.valueOf(j);
            return this;
        }

        public TestCase build() {
            String str = "";
            if (this.name == null) {
                str = String.valueOf(str).concat(" name");
            }
            if (this.supportGenericKeyAuth == null) {
                str = String.valueOf(str).concat(" supportGenericKeyAuth");
            }
            if (this.supportEcies == null) {
                str = String.valueOf(str).concat(" supportEcies");
            }
            if (this.overrideTransactionMode == null) {
                str = String.valueOf(str).concat(" overrideTransactionMode");
            }
            if (this.paymentEnabled == null) {
                str = String.valueOf(str).concat(" paymentEnabled");
            }
            if (this.paymentRequested == null) {
                str = String.valueOf(str).concat(" paymentRequested");
            }
            if (this.vasEnabled == null) {
                str = String.valueOf(str).concat(" vasEnabled");
            }
            if (this.vasRequested == null) {
                str = String.valueOf(str).concat(" vasRequested");
            }
            if (this.oseEnabled == null) {
                str = String.valueOf(str).concat(" oseEnabled");
            }
            if (this.oseSmartTap13 == null) {
                str = String.valueOf(str).concat(" oseSmartTap13");
            }
            if (this.oseSmartTap13Priority == null) {
                str = String.valueOf(str).concat(" oseSmartTap13Priority");
            }
            if (this.oseSmartTap20 == null) {
                str = String.valueOf(str).concat(" oseSmartTap20");
            }
            if (this.oseSmartTap20Priority == null) {
                str = String.valueOf(str).concat(" oseSmartTap20Priority");
            }
            if (this.oseCustom == null) {
                str = String.valueOf(str).concat(" oseCustom");
            }
            if (this.oseCustomAid == null) {
                str = String.valueOf(str).concat(" oseCustomAid");
            }
            if (this.oseCustomPriority == null) {
                str = String.valueOf(str).concat(" oseCustomPriority");
            }
            if (this.supportSmartTap13 == null) {
                str = String.valueOf(str).concat(" supportSmartTap13");
            }
            if (this.supportSmartTap20 == null) {
                str = String.valueOf(str).concat(" supportSmartTap20");
            }
            if (this.allowSkippingSmartTap2Select == null) {
                str = String.valueOf(str).concat(" allowSkippingSmartTap2Select");
            }
            if (this.includeMasterNonceInOseResponse == null) {
                str = String.valueOf(str).concat(" includeMasterNonceInOseResponse");
            }
            if (this.includeNonceInProprietaryData == null) {
                str = String.valueOf(str).concat(" includeNonceInProprietaryData");
            }
            if (this.smartTapMinVersion == null) {
                str = String.valueOf(str).concat(" smartTapMinVersion");
            }
            if (this.smartTapMaxVersion == null) {
                str = String.valueOf(str).concat(" smartTapMaxVersion");
            }
            if (this.merchantId == null) {
                str = String.valueOf(str).concat(" merchantId");
            }
            if (this.useMerchantCapabilities == null) {
                str = String.valueOf(str).concat(" useMerchantCapabilities");
            }
            if (this.checkMerchantId == null) {
                str = String.valueOf(str).concat(" checkMerchantId");
            }
            if (this.requireEncryption == null) {
                str = String.valueOf(str).concat(" requireEncryption");
            }
            if (this.requireLiveAuthentication == null) {
                str = String.valueOf(str).concat(" requireLiveAuthentication");
            }
            if (this.useCustomOseStatusWord == null) {
                str = String.valueOf(str).concat(" useCustomOseStatusWord");
            }
            if (this.customStatuses == null) {
                str = String.valueOf(str).concat(" customStatuses");
            }
            if (this.maxCustomStatuses == null) {
                str = String.valueOf(str).concat(" maxCustomStatuses");
            }
            if (this.addedNdefs == null) {
                str = String.valueOf(str).concat(" addedNdefs");
            }
            if (this.removedNdefs == null) {
                str = String.valueOf(str).concat(" removedNdefs");
            }
            if (this.smartTaps == null) {
                str = String.valueOf(str).concat(" smartTaps");
            }
            if (this.testSuiteId == null) {
                str = String.valueOf(str).concat(" testSuiteId");
            }
            if (this.id == null) {
                str = String.valueOf(str).concat(" id");
            }
            if (str.isEmpty()) {
                return new AutoValue_TestCase(this.name, this.expectedTerminalBehavior, this.supportGenericKeyAuth.booleanValue(), this.supportEcies.booleanValue(), this.overrideTransactionMode.booleanValue(), this.paymentEnabled.booleanValue(), this.paymentRequested.booleanValue(), this.vasEnabled.booleanValue(), this.vasRequested.booleanValue(), this.oseEnabled.booleanValue(), this.oseSmartTap13.booleanValue(), this.oseSmartTap13Priority.intValue(), this.oseSmartTap20.booleanValue(), this.oseSmartTap20Priority.intValue(), this.oseCustom.booleanValue(), this.oseCustomAid, this.oseCustomPriority.intValue(), this.supportSmartTap13.booleanValue(), this.supportSmartTap20.booleanValue(), this.allowSkippingSmartTap2Select.booleanValue(), this.includeMasterNonceInOseResponse.booleanValue(), this.includeNonceInProprietaryData.booleanValue(), this.smartTapMinVersion, this.smartTapMaxVersion, this.consumerId, this.tapId, this.deviceId, this.merchantId.longValue(), this.useMerchantCapabilities.booleanValue(), this.checkMerchantId.booleanValue(), this.requireEncryption.booleanValue(), this.requireLiveAuthentication.booleanValue(), this.getDataResponseStatus, this.stopPaymentsCommand, this.validationSchema, this.tapVideoUri, this.merchantPublicKey, this.useCustomOseStatusWord.booleanValue(), this.customOseStatusWord, this.customStatuses, this.maxCustomStatuses, this.addedNdefs, this.removedNdefs, this.smartTaps, this.testSuiteId.longValue(), this.id.longValue());
            }
            String str2 = "Missing required properties:";
            str = String.valueOf(str);
            throw new IllegalStateException(str.length() != 0 ? str2.concat(str) : new String(str2));
        }

        private Builder(TestCase testCase) {
            this();
            this.name = testCase.name();
            this.expectedTerminalBehavior = testCase.expectedTerminalBehavior();
            this.supportGenericKeyAuth = Boolean.valueOf(testCase.supportGenericKeyAuth());
            this.supportEcies = Boolean.valueOf(testCase.supportEcies());
            this.overrideTransactionMode = Boolean.valueOf(testCase.overrideTransactionMode());
            this.paymentEnabled = Boolean.valueOf(testCase.paymentEnabled());
            this.paymentRequested = Boolean.valueOf(testCase.paymentRequested());
            this.vasEnabled = Boolean.valueOf(testCase.vasEnabled());
            this.vasRequested = Boolean.valueOf(testCase.vasRequested());
            this.oseEnabled = Boolean.valueOf(testCase.oseEnabled());
            this.oseSmartTap13 = Boolean.valueOf(testCase.oseSmartTap13());
            this.oseSmartTap13Priority = Integer.valueOf(testCase.oseSmartTap13Priority());
            this.oseSmartTap20 = Boolean.valueOf(testCase.oseSmartTap20());
            this.oseSmartTap20Priority = Integer.valueOf(testCase.oseSmartTap20Priority());
            this.oseCustom = Boolean.valueOf(testCase.oseCustom());
            this.oseCustomAid = testCase.oseCustomAid();
            this.oseCustomPriority = Integer.valueOf(testCase.oseCustomPriority());
            this.supportSmartTap13 = Boolean.valueOf(testCase.supportSmartTap13());
            this.supportSmartTap20 = Boolean.valueOf(testCase.supportSmartTap20());
            this.allowSkippingSmartTap2Select = Boolean.valueOf(testCase.allowSkippingSmartTap2Select());
            this.includeMasterNonceInOseResponse = Boolean.valueOf(testCase.includeMasterNonceInOseResponse());
            this.includeNonceInProprietaryData = Boolean.valueOf(testCase.includeNonceInProprietaryData());
            this.smartTapMinVersion = testCase.smartTapMinVersion();
            this.smartTapMaxVersion = testCase.smartTapMaxVersion();
            this.consumerId = testCase.consumerId();
            this.tapId = testCase.tapId();
            this.deviceId = testCase.deviceId();
            this.merchantId = Long.valueOf(testCase.merchantId());
            this.useMerchantCapabilities = Boolean.valueOf(testCase.useMerchantCapabilities());
            this.checkMerchantId = Boolean.valueOf(testCase.checkMerchantId());
            this.requireEncryption = Boolean.valueOf(testCase.requireEncryption());
            this.requireLiveAuthentication = Boolean.valueOf(testCase.requireLiveAuthentication());
            this.getDataResponseStatus = testCase.getDataResponseStatus();
            this.stopPaymentsCommand = testCase.stopPaymentsCommand();
            this.validationSchema = testCase.validationSchema();
            this.tapVideoUri = testCase.tapVideoUri();
            this.merchantPublicKey = testCase.merchantPublicKey();
            this.useCustomOseStatusWord = Boolean.valueOf(testCase.useCustomOseStatusWord());
            this.customOseStatusWord = testCase.customOseStatusWord();
            this.customStatuses = testCase.customStatuses();
            this.maxCustomStatuses = testCase.maxCustomStatuses();
            this.addedNdefs = testCase.addedNdefs();
            this.removedNdefs = testCase.removedNdefs();
            this.smartTaps = testCase.smartTaps();
            this.testSuiteId = Long.valueOf(testCase.testSuiteId());
            this.id = Long.valueOf(testCase.id());
        }
    }

    public enum GetSmartTapDataResponse {
        DO_NOT_RESPOND,
        WRONG_DATA(Iso7816StatusWord.WRONG_DATA),
        CONDITIONS_NOT_SATISFIED(Iso7816StatusWord.CONDITIONS_NOT_SATISFIED);
        
        private final Optional<StatusWord> statusWord;

        private GetSmartTapDataResponse(StatusWord statusWord) {
            this.statusWord = Optional.of(statusWord);
        }

        public String getMessage(Context context) {
            if (this.statusWord.isPresent()) {
                return ((StatusWord) this.statusWord.get()).getCode().getMessage();
            }
            return context.getString(R.string.get_smarttap_data_no_response);
        }

        public ResponseApdu getResponseApdu() {
            if (this.statusWord.isPresent()) {
                return ResponseApdu.fromStatusWord((StatusWord) this.statusWord.get());
            }
            return null;
        }
    }

    static class MultimapAdapter implements JsonDeserializer<Multimap<ByteArrayWrapper, NdefRecord>>, JsonSerializer<Multimap<ByteArrayWrapper, NdefRecord>> {
        private static final Type MAP_TYPE = new TypeToken<Map<ByteArrayWrapper, Collection<NdefRecord>>>() {
        }.getType();

        private MultimapAdapter() {
        }

        public JsonElement serialize(Multimap<ByteArrayWrapper, NdefRecord> multimap, Type type, JsonSerializationContext jsonSerializationContext) {
            return jsonSerializationContext.serialize(multimap.asMap());
        }

        public Multimap<ByteArrayWrapper, NdefRecord> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Map map = (Map) jsonDeserializationContext.deserialize(jsonElement, MAP_TYPE);
            com.google.common.collect.ImmutableMultimap.Builder builder = ImmutableMultimap.builder();
            for (Entry entry : map.entrySet()) {
                for (NdefRecord put : (Collection) entry.getValue()) {
                    builder.put((ByteArrayWrapper) entry.getKey(), put);
                }
            }
            return builder.build();
        }
    }

    public abstract Multimap<ByteArrayWrapper, NdefRecord> addedNdefs();

    public abstract boolean allowSkippingSmartTap2Select();

    public abstract boolean checkMerchantId();

    public abstract String consumerId();

    public abstract byte[] customOseStatusWord();

    public abstract Map<Byte, byte[]> customStatuses();

    public abstract String deviceId();

    public abstract String expectedTerminalBehavior();

    public abstract GetSmartTapDataResponse getDataResponseStatus();

    public abstract long id();

    public abstract boolean includeMasterNonceInOseResponse();

    public abstract boolean includeNonceInProprietaryData();

    public abstract Map<Byte, Integer> maxCustomStatuses();

    public abstract long merchantId();

    public abstract byte[] merchantPublicKey();

    public abstract String name();

    public abstract boolean oseCustom();

    public abstract Aid oseCustomAid();

    public abstract int oseCustomPriority();

    public abstract boolean oseEnabled();

    public abstract boolean oseSmartTap13();

    public abstract int oseSmartTap13Priority();

    public abstract boolean oseSmartTap20();

    public abstract int oseSmartTap20Priority();

    public abstract boolean overrideTransactionMode();

    public abstract boolean paymentEnabled();

    public abstract boolean paymentRequested();

    public abstract Set<ByteArrayWrapper> removedNdefs();

    public abstract boolean requireEncryption();

    public abstract boolean requireLiveAuthentication();

    public abstract byte[] smartTapMaxVersion();

    public abstract byte[] smartTapMinVersion();

    public abstract List<SmartTap> smartTaps();

    public abstract CommandType stopPaymentsCommand();

    public abstract boolean supportEcies();

    public abstract boolean supportGenericKeyAuth();

    public abstract boolean supportSmartTap13();

    public abstract boolean supportSmartTap20();

    public abstract String tapId();

    public abstract String tapVideoUri();

    public abstract long testSuiteId();

    public abstract Builder toBuilder();

    public abstract boolean useCustomOseStatusWord();

    public abstract boolean useMerchantCapabilities();

    public abstract Schema validationSchema();

    public abstract boolean vasEnabled();

    public abstract boolean vasRequested();

    public static Builder builder() {
        return new Builder((byte) 0).setExpectedTerminalBehavior("").setSupportGenericKeyAuth(true).setSupportEcies(true).setOverrideTransactionMode(false).setPaymentEnabled(true).setPaymentRequested(true).setVasEnabled(true).setVasRequested(true).setOseEnabled(true).setOseSmartTap13(true).setOseSmartTap13Priority(2).setOseSmartTap20(true).setOseSmartTap20Priority(1).setOseCustom(false).setOseCustomAid(Aid.valueOf(new byte[]{(byte) 17, (byte) 34, (byte) 51, (byte) 68, (byte) 85})).setOseCustomPriority(3).setSupportSmartTap13(true).setSupportSmartTap20(true).setAllowSkippingSmartTap2Select(true).setIncludeMasterNonceInOseResponse(true).setIncludeNonceInProprietaryData(false).setSmartTapMinVersion(SmartTap2Values.SMARTTAP_MIN_VERSION).setSmartTapMaxVersion(SmartTap2Values.SMARTTAP_MAX_VERSION).setUseCustomOseStatusWord(false).setCustomOseStatusWord(new byte[0]).setCustomStatuses(ImmutableMap.of()).setMaxCustomStatuses(ImmutableMap.of()).setAddedNdefs(ImmutableMultimap.of()).setRemovedNdefs(ImmutableSet.of()).setSmartTaps(new ArrayList()).setCheckMerchantId(false).setMerchantId(-1).setRequireEncryption(false).setRequireLiveAuthentication(false).setTestSuiteId(-1).setId(-1);
    }

    public static String toJson(List<TestCase> list) {
        return GSON.toJson((Object) list);
    }

    public static List<TestCase> fromJsonArray(String str) {
        TestCase[] testCaseArr = (TestCase[]) GSON.fromJson(str, TestCase[].class);
        List arrayList = new ArrayList(testCaseArr.length);
        for (Object obj : testCaseArr) {
            if (obj != null) {
                arrayList.add(obj);
            } else {
                LOG.w("Ignoring null test case in deserialized json TestCase array.", new Object[0]);
            }
        }
        return arrayList;
    }

    public String getDescription() {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        for (SmartTap smartTap : smartTaps()) {
            int i5;
            switch (smartTap.type) {
                case 1:
                    i5 = i;
                    i = i2;
                    i2 = i3;
                    i3 = i4 + 1;
                    break;
                case 2:
                    i5 = i;
                    i = i2;
                    i2 = i3 + 1;
                    i3 = i4;
                    break;
                case 3:
                    i5 = i;
                    i = i2 + 1;
                    i2 = i3;
                    i3 = i4;
                    break;
                case 4:
                    i5 = i + 1;
                    i = i2;
                    i2 = i3;
                    i3 = i4;
                    break;
                default:
                    LOG.w("Unknown valuable type: " + smartTap.type, new Object[0]);
                    i5 = i;
                    i = i2;
                    i2 = i3;
                    i3 = i4;
                    break;
            }
            i4 = i3;
            i3 = i2;
            i2 = i;
            i = i5;
        }
        return i4 + "x loyalty, " + i3 + "x offers, " + i2 + "x gift cards, " + i + "x plc";
    }

    public static TypeAdapter<TestCase> typeAdapter(Gson gson) {
        return new GsonTypeAdapter(gson);
    }
}
