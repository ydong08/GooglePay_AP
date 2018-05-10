package com.google.commerce.tapandpay.merchantapp.testcase;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase.GetSmartTapDataResponse;
import com.google.commerce.tapandpay.merchantapp.validation.Schema;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.internal.tapandpay.v1.valuables.nano.CommonProto.SmartTap;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class AutoValue_TestCase extends C$AutoValue_TestCase {

    public static final class GsonTypeAdapter extends TypeAdapter<TestCase> {
        private final TypeAdapter<Multimap<ByteArrayWrapper, NdefRecord>> addedNdefsAdapter;
        private final TypeAdapter<Boolean> allowSkippingSmartTap2SelectAdapter;
        private final TypeAdapter<Boolean> checkMerchantIdAdapter;
        private final TypeAdapter<String> consumerIdAdapter;
        private final TypeAdapter<byte[]> customOseStatusWordAdapter;
        private final TypeAdapter<Map<Byte, byte[]>> customStatusesAdapter;
        private final TypeAdapter<String> deviceIdAdapter;
        private final TypeAdapter<String> expectedTerminalBehaviorAdapter;
        private final TypeAdapter<GetSmartTapDataResponse> getDataResponseStatusAdapter;
        private final TypeAdapter<Long> idAdapter;
        private final TypeAdapter<Boolean> includeMasterNonceInOseResponseAdapter;
        private final TypeAdapter<Boolean> includeNonceInProprietaryDataAdapter;
        private final TypeAdapter<Map<Byte, Integer>> maxCustomStatusesAdapter;
        private final TypeAdapter<Long> merchantIdAdapter;
        private final TypeAdapter<byte[]> merchantPublicKeyAdapter;
        private final TypeAdapter<String> nameAdapter;
        private final TypeAdapter<Boolean> oseCustomAdapter;
        private final TypeAdapter<Aid> oseCustomAidAdapter;
        private final TypeAdapter<Integer> oseCustomPriorityAdapter;
        private final TypeAdapter<Boolean> oseEnabledAdapter;
        private final TypeAdapter<Boolean> oseSmartTap13Adapter;
        private final TypeAdapter<Integer> oseSmartTap13PriorityAdapter;
        private final TypeAdapter<Boolean> oseSmartTap20Adapter;
        private final TypeAdapter<Integer> oseSmartTap20PriorityAdapter;
        private final TypeAdapter<Boolean> overrideTransactionModeAdapter;
        private final TypeAdapter<Boolean> paymentEnabledAdapter;
        private final TypeAdapter<Boolean> paymentRequestedAdapter;
        private final TypeAdapter<Set<ByteArrayWrapper>> removedNdefsAdapter;
        private final TypeAdapter<Boolean> requireEncryptionAdapter;
        private final TypeAdapter<Boolean> requireLiveAuthenticationAdapter;
        private final TypeAdapter<byte[]> smartTapMaxVersionAdapter;
        private final TypeAdapter<byte[]> smartTapMinVersionAdapter;
        private final TypeAdapter<List<SmartTap>> smartTapsAdapter;
        private final TypeAdapter<CommandType> stopPaymentsCommandAdapter;
        private final TypeAdapter<Boolean> supportEciesAdapter;
        private final TypeAdapter<Boolean> supportGenericKeyAuthAdapter;
        private final TypeAdapter<Boolean> supportSmartTap13Adapter;
        private final TypeAdapter<Boolean> supportSmartTap20Adapter;
        private final TypeAdapter<String> tapIdAdapter;
        private final TypeAdapter<String> tapVideoUriAdapter;
        private final TypeAdapter<Long> testSuiteIdAdapter;
        private final TypeAdapter<Boolean> useCustomOseStatusWordAdapter;
        private final TypeAdapter<Boolean> useMerchantCapabilitiesAdapter;
        private final TypeAdapter<Schema> validationSchemaAdapter;
        private final TypeAdapter<Boolean> vasEnabledAdapter;
        private final TypeAdapter<Boolean> vasRequestedAdapter;

        public GsonTypeAdapter(Gson gson) {
            this.nameAdapter = gson.getAdapter(String.class);
            this.expectedTerminalBehaviorAdapter = gson.getAdapter(String.class);
            this.supportGenericKeyAuthAdapter = gson.getAdapter(Boolean.class);
            this.supportEciesAdapter = gson.getAdapter(Boolean.class);
            this.overrideTransactionModeAdapter = gson.getAdapter(Boolean.class);
            this.paymentEnabledAdapter = gson.getAdapter(Boolean.class);
            this.paymentRequestedAdapter = gson.getAdapter(Boolean.class);
            this.vasEnabledAdapter = gson.getAdapter(Boolean.class);
            this.vasRequestedAdapter = gson.getAdapter(Boolean.class);
            this.oseEnabledAdapter = gson.getAdapter(Boolean.class);
            this.oseSmartTap13Adapter = gson.getAdapter(Boolean.class);
            this.oseSmartTap13PriorityAdapter = gson.getAdapter(Integer.class);
            this.oseSmartTap20Adapter = gson.getAdapter(Boolean.class);
            this.oseSmartTap20PriorityAdapter = gson.getAdapter(Integer.class);
            this.oseCustomAdapter = gson.getAdapter(Boolean.class);
            this.oseCustomAidAdapter = gson.getAdapter(Aid.class);
            this.oseCustomPriorityAdapter = gson.getAdapter(Integer.class);
            this.supportSmartTap13Adapter = gson.getAdapter(Boolean.class);
            this.supportSmartTap20Adapter = gson.getAdapter(Boolean.class);
            this.allowSkippingSmartTap2SelectAdapter = gson.getAdapter(Boolean.class);
            this.includeMasterNonceInOseResponseAdapter = gson.getAdapter(Boolean.class);
            this.includeNonceInProprietaryDataAdapter = gson.getAdapter(Boolean.class);
            this.smartTapMinVersionAdapter = gson.getAdapter(byte[].class);
            this.smartTapMaxVersionAdapter = gson.getAdapter(byte[].class);
            this.consumerIdAdapter = gson.getAdapter(String.class);
            this.tapIdAdapter = gson.getAdapter(String.class);
            this.deviceIdAdapter = gson.getAdapter(String.class);
            this.merchantIdAdapter = gson.getAdapter(Long.class);
            this.useMerchantCapabilitiesAdapter = gson.getAdapter(Boolean.class);
            this.checkMerchantIdAdapter = gson.getAdapter(Boolean.class);
            this.requireEncryptionAdapter = gson.getAdapter(Boolean.class);
            this.requireLiveAuthenticationAdapter = gson.getAdapter(Boolean.class);
            this.getDataResponseStatusAdapter = gson.getAdapter(GetSmartTapDataResponse.class);
            this.stopPaymentsCommandAdapter = gson.getAdapter(CommandType.class);
            this.validationSchemaAdapter = gson.getAdapter(Schema.class);
            this.tapVideoUriAdapter = gson.getAdapter(String.class);
            this.merchantPublicKeyAdapter = gson.getAdapter(byte[].class);
            this.useCustomOseStatusWordAdapter = gson.getAdapter(Boolean.class);
            this.customOseStatusWordAdapter = gson.getAdapter(byte[].class);
            this.customStatusesAdapter = gson.getAdapter(new TypeToken<Map<Byte, byte[]>>(this) {
            });
            this.maxCustomStatusesAdapter = gson.getAdapter(new TypeToken<Map<Byte, Integer>>(this) {
            });
            this.addedNdefsAdapter = gson.getAdapter(new TypeToken<Multimap<ByteArrayWrapper, NdefRecord>>(this) {
            });
            this.removedNdefsAdapter = gson.getAdapter(new TypeToken<Set<ByteArrayWrapper>>(this) {
            });
            this.smartTapsAdapter = gson.getAdapter(new TypeToken<List<SmartTap>>(this) {
            });
            this.testSuiteIdAdapter = gson.getAdapter(Long.class);
            this.idAdapter = gson.getAdapter(Long.class);
        }

        public void write(JsonWriter jsonWriter, TestCase testCase) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name("name");
            this.nameAdapter.write(jsonWriter, testCase.name());
            if (testCase.expectedTerminalBehavior() != null) {
                jsonWriter.name("expectedTerminalBehavior");
                this.expectedTerminalBehaviorAdapter.write(jsonWriter, testCase.expectedTerminalBehavior());
            }
            jsonWriter.name("supportGenericKeyAuth");
            this.supportGenericKeyAuthAdapter.write(jsonWriter, Boolean.valueOf(testCase.supportGenericKeyAuth()));
            jsonWriter.name("supportEcies");
            this.supportEciesAdapter.write(jsonWriter, Boolean.valueOf(testCase.supportEcies()));
            jsonWriter.name("overrideTransactionMode");
            this.overrideTransactionModeAdapter.write(jsonWriter, Boolean.valueOf(testCase.overrideTransactionMode()));
            jsonWriter.name("paymentEnabled");
            this.paymentEnabledAdapter.write(jsonWriter, Boolean.valueOf(testCase.paymentEnabled()));
            jsonWriter.name("paymentRequested");
            this.paymentRequestedAdapter.write(jsonWriter, Boolean.valueOf(testCase.paymentRequested()));
            jsonWriter.name("vasEnabled");
            this.vasEnabledAdapter.write(jsonWriter, Boolean.valueOf(testCase.vasEnabled()));
            jsonWriter.name("vasRequested");
            this.vasRequestedAdapter.write(jsonWriter, Boolean.valueOf(testCase.vasRequested()));
            jsonWriter.name("oseEnabled");
            this.oseEnabledAdapter.write(jsonWriter, Boolean.valueOf(testCase.oseEnabled()));
            jsonWriter.name("oseSmartTap13");
            this.oseSmartTap13Adapter.write(jsonWriter, Boolean.valueOf(testCase.oseSmartTap13()));
            jsonWriter.name("oseSmartTap13Priority");
            this.oseSmartTap13PriorityAdapter.write(jsonWriter, Integer.valueOf(testCase.oseSmartTap13Priority()));
            jsonWriter.name("oseSmartTap20");
            this.oseSmartTap20Adapter.write(jsonWriter, Boolean.valueOf(testCase.oseSmartTap20()));
            jsonWriter.name("oseSmartTap20Priority");
            this.oseSmartTap20PriorityAdapter.write(jsonWriter, Integer.valueOf(testCase.oseSmartTap20Priority()));
            jsonWriter.name("oseCustom");
            this.oseCustomAdapter.write(jsonWriter, Boolean.valueOf(testCase.oseCustom()));
            jsonWriter.name("oseCustomAid");
            this.oseCustomAidAdapter.write(jsonWriter, testCase.oseCustomAid());
            jsonWriter.name("oseCustomPriority");
            this.oseCustomPriorityAdapter.write(jsonWriter, Integer.valueOf(testCase.oseCustomPriority()));
            jsonWriter.name("supportSmartTap13");
            this.supportSmartTap13Adapter.write(jsonWriter, Boolean.valueOf(testCase.supportSmartTap13()));
            jsonWriter.name("supportSmartTap20");
            this.supportSmartTap20Adapter.write(jsonWriter, Boolean.valueOf(testCase.supportSmartTap20()));
            jsonWriter.name("allowSkippingSmartTap2Select");
            this.allowSkippingSmartTap2SelectAdapter.write(jsonWriter, Boolean.valueOf(testCase.allowSkippingSmartTap2Select()));
            jsonWriter.name("includeMasterNonceInOseResponse");
            this.includeMasterNonceInOseResponseAdapter.write(jsonWriter, Boolean.valueOf(testCase.includeMasterNonceInOseResponse()));
            jsonWriter.name("includeNonceInProprietaryData");
            this.includeNonceInProprietaryDataAdapter.write(jsonWriter, Boolean.valueOf(testCase.includeNonceInProprietaryData()));
            jsonWriter.name("smartTapMinVersion");
            this.smartTapMinVersionAdapter.write(jsonWriter, testCase.smartTapMinVersion());
            jsonWriter.name("smartTapMaxVersion");
            this.smartTapMaxVersionAdapter.write(jsonWriter, testCase.smartTapMaxVersion());
            if (testCase.consumerId() != null) {
                jsonWriter.name("consumerId");
                this.consumerIdAdapter.write(jsonWriter, testCase.consumerId());
            }
            if (testCase.tapId() != null) {
                jsonWriter.name("tapId");
                this.tapIdAdapter.write(jsonWriter, testCase.tapId());
            }
            if (testCase.deviceId() != null) {
                jsonWriter.name("deviceId");
                this.deviceIdAdapter.write(jsonWriter, testCase.deviceId());
            }
            jsonWriter.name("merchantId");
            this.merchantIdAdapter.write(jsonWriter, Long.valueOf(testCase.merchantId()));
            jsonWriter.name("useMerchantCapabilities");
            this.useMerchantCapabilitiesAdapter.write(jsonWriter, Boolean.valueOf(testCase.useMerchantCapabilities()));
            jsonWriter.name("checkMerchantId");
            this.checkMerchantIdAdapter.write(jsonWriter, Boolean.valueOf(testCase.checkMerchantId()));
            jsonWriter.name("requireEncryption");
            this.requireEncryptionAdapter.write(jsonWriter, Boolean.valueOf(testCase.requireEncryption()));
            jsonWriter.name("requireLiveAuthentication");
            this.requireLiveAuthenticationAdapter.write(jsonWriter, Boolean.valueOf(testCase.requireLiveAuthentication()));
            if (testCase.getDataResponseStatus() != null) {
                jsonWriter.name("getDataResponseStatus");
                this.getDataResponseStatusAdapter.write(jsonWriter, testCase.getDataResponseStatus());
            }
            if (testCase.stopPaymentsCommand() != null) {
                jsonWriter.name("stopPaymentsCommand");
                this.stopPaymentsCommandAdapter.write(jsonWriter, testCase.stopPaymentsCommand());
            }
            if (testCase.validationSchema() != null) {
                jsonWriter.name("validationSchema");
                this.validationSchemaAdapter.write(jsonWriter, testCase.validationSchema());
            }
            if (testCase.tapVideoUri() != null) {
                jsonWriter.name("tapVideoUri");
                this.tapVideoUriAdapter.write(jsonWriter, testCase.tapVideoUri());
            }
            if (testCase.merchantPublicKey() != null) {
                jsonWriter.name("merchantPublicKey");
                this.merchantPublicKeyAdapter.write(jsonWriter, testCase.merchantPublicKey());
            }
            jsonWriter.name("useCustomOseStatusWord");
            this.useCustomOseStatusWordAdapter.write(jsonWriter, Boolean.valueOf(testCase.useCustomOseStatusWord()));
            if (testCase.customOseStatusWord() != null) {
                jsonWriter.name("customOseStatusWord");
                this.customOseStatusWordAdapter.write(jsonWriter, testCase.customOseStatusWord());
            }
            jsonWriter.name("customStatuses");
            this.customStatusesAdapter.write(jsonWriter, testCase.customStatuses());
            jsonWriter.name("maxCustomStatuses");
            this.maxCustomStatusesAdapter.write(jsonWriter, testCase.maxCustomStatuses());
            jsonWriter.name("addedNdefs");
            this.addedNdefsAdapter.write(jsonWriter, testCase.addedNdefs());
            jsonWriter.name("removedNdefs");
            this.removedNdefsAdapter.write(jsonWriter, testCase.removedNdefs());
            jsonWriter.name("smartTaps");
            this.smartTapsAdapter.write(jsonWriter, testCase.smartTaps());
            jsonWriter.name("testSuiteId");
            this.testSuiteIdAdapter.write(jsonWriter, Long.valueOf(testCase.testSuiteId()));
            jsonWriter.name("id");
            this.idAdapter.write(jsonWriter, Long.valueOf(testCase.id()));
            jsonWriter.endObject();
        }

        public TestCase read(JsonReader jsonReader) throws IOException {
            jsonReader.beginObject();
            String str = null;
            String str2 = null;
            boolean z = false;
            boolean z2 = false;
            boolean z3 = false;
            boolean z4 = false;
            boolean z5 = false;
            boolean z6 = false;
            boolean z7 = false;
            boolean z8 = false;
            boolean z9 = false;
            int i = 0;
            boolean z10 = false;
            int i2 = 0;
            boolean z11 = false;
            Aid aid = null;
            int i3 = 0;
            boolean z12 = false;
            boolean z13 = false;
            boolean z14 = false;
            boolean z15 = false;
            boolean z16 = false;
            byte[] bArr = null;
            byte[] bArr2 = null;
            String str3 = null;
            String str4 = null;
            String str5 = null;
            long j = 0;
            boolean z17 = false;
            boolean z18 = false;
            boolean z19 = false;
            boolean z20 = false;
            GetSmartTapDataResponse getSmartTapDataResponse = null;
            CommandType commandType = null;
            Schema schema = null;
            String str6 = null;
            byte[] bArr3 = null;
            boolean z21 = false;
            byte[] bArr4 = null;
            Map emptyMap = Collections.emptyMap();
            Map emptyMap2 = Collections.emptyMap();
            Multimap multimap = null;
            Set emptySet = Collections.emptySet();
            List emptyList = Collections.emptyList();
            long j2 = 0;
            long j3 = 0;
            while (jsonReader.hasNext()) {
                String nextName = jsonReader.nextName();
                if (jsonReader.peek() != JsonToken.NULL) {
                    Object obj = -1;
                    switch (nextName.hashCode()) {
                        case -2051751680:
                            if (nextName.equals("oseEnabled")) {
                                obj = 9;
                                break;
                            }
                            break;
                        case -1736897130:
                            if (nextName.equals("oseCustomPriority")) {
                                obj = 16;
                                break;
                            }
                            break;
                        case -1669798564:
                            if (nextName.equals("addedNdefs")) {
                                obj = 41;
                                break;
                            }
                            break;
                        case -1653494898:
                            if (nextName.equals("smartTapMaxVersion")) {
                                obj = 23;
                                break;
                            }
                            break;
                        case -1642832104:
                            if (nextName.equals("includeMasterNonceInOseResponse")) {
                                obj = 20;
                                break;
                            }
                            break;
                        case -1568999702:
                            if (nextName.equals("supportEcies")) {
                                obj = 3;
                                break;
                            }
                            break;
                        case -1501260111:
                            if (nextName.equals("customStatuses")) {
                                obj = 39;
                                break;
                            }
                            break;
                        case -1469933923:
                            if (nextName.equals("oseSmartTap13")) {
                                obj = 10;
                                break;
                            }
                            break;
                        case -1469933895:
                            if (nextName.equals("oseSmartTap20")) {
                                obj = 12;
                                break;
                            }
                            break;
                        case -1444072807:
                            if (nextName.equals("smartTaps")) {
                                obj = 43;
                                break;
                            }
                            break;
                        case -1243586359:
                            if (nextName.equals("requireLiveAuthentication")) {
                                obj = 31;
                                break;
                            }
                            break;
                        case -1137836998:
                            if (nextName.equals("allowSkippingSmartTap2Select")) {
                                obj = 19;
                                break;
                            }
                            break;
                        case -820560405:
                            if (nextName.equals("supportSmartTap13")) {
                                obj = 17;
                                break;
                            }
                            break;
                        case -820560377:
                            if (nextName.equals("supportSmartTap20")) {
                                obj = 18;
                                break;
                            }
                            break;
                        case -747328067:
                            if (nextName.equals("includeNonceInProprietaryData")) {
                                obj = 21;
                                break;
                            }
                            break;
                        case -402241028:
                            if (nextName.equals("removedNdefs")) {
                                obj = 42;
                                break;
                            }
                            break;
                        case -366660920:
                            if (nextName.equals("requireEncryption")) {
                                obj = 30;
                                break;
                            }
                            break;
                        case -339245215:
                            if (nextName.equals("oseSmartTap13Priority")) {
                                obj = 11;
                                break;
                            }
                            break;
                        case -258572029:
                            if (nextName.equals("merchantId")) {
                                obj = 27;
                                break;
                            }
                            break;
                        case -166238287:
                            if (nextName.equals("consumerId")) {
                                obj = 24;
                                break;
                            }
                            break;
                        case -166078724:
                            if (nextName.equals("stopPaymentsCommand")) {
                                obj = 33;
                                break;
                            }
                            break;
                        case -64960946:
                            if (nextName.equals("merchantPublicKey")) {
                                obj = 36;
                                break;
                            }
                            break;
                        case 3355:
                            if (nextName.equals("id")) {
                                obj = 45;
                                break;
                            }
                            break;
                        case 3373707:
                            if (nextName.equals("name")) {
                                obj = null;
                                break;
                            }
                            break;
                        case 110128158:
                            if (nextName.equals("tapId")) {
                                obj = 25;
                                break;
                            }
                            break;
                        case 302246965:
                            if (nextName.equals("overrideTransactionMode")) {
                                obj = 4;
                                break;
                            }
                            break;
                        case 304318406:
                            if (nextName.equals("expectedTerminalBehavior")) {
                                obj = 1;
                                break;
                            }
                            break;
                        case 402502405:
                            if (nextName.equals("useCustomOseStatusWord")) {
                                obj = 37;
                                break;
                            }
                            break;
                        case 591637373:
                            if (nextName.equals("oseSmartTap20Priority")) {
                                obj = 13;
                                break;
                            }
                            break;
                        case 769684404:
                            if (nextName.equals("tapVideoUri")) {
                                obj = 35;
                                break;
                            }
                            break;
                        case 818504416:
                            if (nextName.equals("smartTapMinVersion")) {
                                obj = 22;
                                break;
                            }
                            break;
                        case 945264683:
                            if (nextName.equals("checkMerchantId")) {
                                obj = 29;
                                break;
                            }
                            break;
                        case 1109191185:
                            if (nextName.equals("deviceId")) {
                                obj = 26;
                                break;
                            }
                            break;
                        case 1184613388:
                            if (nextName.equals("customOseStatusWord")) {
                                obj = 38;
                                break;
                            }
                            break;
                        case 1269047762:
                            if (nextName.equals("oseCustom")) {
                                obj = 14;
                                break;
                            }
                            break;
                        case 1414954278:
                            if (nextName.equals("vasRequested")) {
                                obj = 8;
                                break;
                            }
                            break;
                        case 1513998645:
                            if (nextName.equals("maxCustomStatuses")) {
                                obj = 40;
                                break;
                            }
                            break;
                        case 1523954117:
                            if (nextName.equals("useMerchantCapabilities")) {
                                obj = 28;
                                break;
                            }
                            break;
                        case 1627614216:
                            if (nextName.equals("paymentRequested")) {
                                obj = 6;
                                break;
                            }
                            break;
                        case 1689697883:
                            if (nextName.equals("paymentEnabled")) {
                                obj = 5;
                                break;
                            }
                            break;
                        case 1715911807:
                            if (nextName.equals("supportGenericKeyAuth")) {
                                obj = 2;
                                break;
                            }
                            break;
                        case 1760127866:
                            if (nextName.equals("validationSchema")) {
                                obj = 34;
                                break;
                            }
                            break;
                        case 1899804170:
                            if (nextName.equals("oseCustomAid")) {
                                obj = 15;
                                break;
                            }
                            break;
                        case 1966571257:
                            if (nextName.equals("vasEnabled")) {
                                obj = 7;
                                break;
                            }
                            break;
                        case 2029537235:
                            if (nextName.equals("getDataResponseStatus")) {
                                obj = 32;
                                break;
                            }
                            break;
                        case 2041244993:
                            if (nextName.equals("testSuiteId")) {
                                obj = 44;
                                break;
                            }
                            break;
                    }
                    switch (obj) {
                        case null:
                            str = (String) this.nameAdapter.read(jsonReader);
                            break;
                        case 1:
                            str2 = (String) this.expectedTerminalBehaviorAdapter.read(jsonReader);
                            break;
                        case 2:
                            z = ((Boolean) this.supportGenericKeyAuthAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 3:
                            z2 = ((Boolean) this.supportEciesAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 4:
                            z3 = ((Boolean) this.overrideTransactionModeAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 5:
                            z4 = ((Boolean) this.paymentEnabledAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 6:
                            z5 = ((Boolean) this.paymentRequestedAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 7:
                            z6 = ((Boolean) this.vasEnabledAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 8:
                            z7 = ((Boolean) this.vasRequestedAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 9:
                            z8 = ((Boolean) this.oseEnabledAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 10:
                            z9 = ((Boolean) this.oseSmartTap13Adapter.read(jsonReader)).booleanValue();
                            break;
                        case 11:
                            i = ((Integer) this.oseSmartTap13PriorityAdapter.read(jsonReader)).intValue();
                            break;
                        case 12:
                            z10 = ((Boolean) this.oseSmartTap20Adapter.read(jsonReader)).booleanValue();
                            break;
                        case 13:
                            i2 = ((Integer) this.oseSmartTap20PriorityAdapter.read(jsonReader)).intValue();
                            break;
                        case 14:
                            z11 = ((Boolean) this.oseCustomAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 15:
                            aid = (Aid) this.oseCustomAidAdapter.read(jsonReader);
                            break;
                        case 16:
                            i3 = ((Integer) this.oseCustomPriorityAdapter.read(jsonReader)).intValue();
                            break;
                        case 17:
                            z12 = ((Boolean) this.supportSmartTap13Adapter.read(jsonReader)).booleanValue();
                            break;
                        case 18:
                            z13 = ((Boolean) this.supportSmartTap20Adapter.read(jsonReader)).booleanValue();
                            break;
                        case 19:
                            z14 = ((Boolean) this.allowSkippingSmartTap2SelectAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 20:
                            z15 = ((Boolean) this.includeMasterNonceInOseResponseAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 21:
                            z16 = ((Boolean) this.includeNonceInProprietaryDataAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 22:
                            bArr = (byte[]) this.smartTapMinVersionAdapter.read(jsonReader);
                            break;
                        case 23:
                            bArr2 = (byte[]) this.smartTapMaxVersionAdapter.read(jsonReader);
                            break;
                        case 24:
                            str3 = (String) this.consumerIdAdapter.read(jsonReader);
                            break;
                        case 25:
                            str4 = (String) this.tapIdAdapter.read(jsonReader);
                            break;
                        case 26:
                            str5 = (String) this.deviceIdAdapter.read(jsonReader);
                            break;
                        case 27:
                            j = ((Long) this.merchantIdAdapter.read(jsonReader)).longValue();
                            break;
                        case 28:
                            z17 = ((Boolean) this.useMerchantCapabilitiesAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 29:
                            z18 = ((Boolean) this.checkMerchantIdAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 30:
                            z19 = ((Boolean) this.requireEncryptionAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 31:
                            z20 = ((Boolean) this.requireLiveAuthenticationAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 32:
                            getSmartTapDataResponse = (GetSmartTapDataResponse) this.getDataResponseStatusAdapter.read(jsonReader);
                            break;
                        case 33:
                            commandType = (CommandType) this.stopPaymentsCommandAdapter.read(jsonReader);
                            break;
                        case 34:
                            schema = (Schema) this.validationSchemaAdapter.read(jsonReader);
                            break;
                        case 35:
                            str6 = (String) this.tapVideoUriAdapter.read(jsonReader);
                            break;
                        case 36:
                            bArr3 = (byte[]) this.merchantPublicKeyAdapter.read(jsonReader);
                            break;
                        case 37:
                            z21 = ((Boolean) this.useCustomOseStatusWordAdapter.read(jsonReader)).booleanValue();
                            break;
                        case 38:
                            bArr4 = (byte[]) this.customOseStatusWordAdapter.read(jsonReader);
                            break;
                        case 39:
                            emptyMap = (Map) this.customStatusesAdapter.read(jsonReader);
                            break;
                        case 40:
                            emptyMap2 = (Map) this.maxCustomStatusesAdapter.read(jsonReader);
                            break;
                        case 41:
                            multimap = (Multimap) this.addedNdefsAdapter.read(jsonReader);
                            break;
                        case 42:
                            emptySet = (Set) this.removedNdefsAdapter.read(jsonReader);
                            break;
                        case 43:
                            emptyList = (List) this.smartTapsAdapter.read(jsonReader);
                            break;
                        case 44:
                            j2 = ((Long) this.testSuiteIdAdapter.read(jsonReader)).longValue();
                            break;
                        case 45:
                            j3 = ((Long) this.idAdapter.read(jsonReader)).longValue();
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                jsonReader.skipValue();
            }
            jsonReader.endObject();
            return new AutoValue_TestCase(str, str2, z, z2, z3, z4, z5, z6, z7, z8, z9, i, z10, i2, z11, aid, i3, z12, z13, z14, z15, z16, bArr, bArr2, str3, str4, str5, j, z17, z18, z19, z20, getSmartTapDataResponse, commandType, schema, str6, bArr3, z21, bArr4, emptyMap, emptyMap2, multimap, emptySet, emptyList, j2, j3);
        }
    }

    AutoValue_TestCase(String str, String str2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9, int i, boolean z10, int i2, boolean z11, Aid aid, int i3, boolean z12, boolean z13, boolean z14, boolean z15, boolean z16, byte[] bArr, byte[] bArr2, String str3, String str4, String str5, long j, boolean z17, boolean z18, boolean z19, boolean z20, GetSmartTapDataResponse getSmartTapDataResponse, CommandType commandType, Schema schema, String str6, byte[] bArr3, boolean z21, byte[] bArr4, Map<Byte, byte[]> map, Map<Byte, Integer> map2, Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, List<SmartTap> list, long j2, long j3) {
        super(str, str2, z, z2, z3, z4, z5, z6, z7, z8, z9, i, z10, i2, z11, aid, i3, z12, z13, z14, z15, z16, bArr, bArr2, str3, str4, str5, j, z17, z18, z19, z20, getSmartTapDataResponse, commandType, schema, str6, bArr3, z21, bArr4, map, map2, multimap, set, list, j2, j3);
    }
}
