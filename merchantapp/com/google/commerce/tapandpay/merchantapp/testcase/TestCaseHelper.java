package com.google.commerce.tapandpay.merchantapp.testcase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import com.google.android.apps.common.inject.annotation.ApplicationContext;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.sqlite.MerchantAppDbHelper;
import com.google.commerce.tapandpay.merchantapp.testcase.EncodingValue.Encoding;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase.Builder;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase.GetSmartTapDataResponse;
import com.google.commerce.tapandpay.merchantapp.validation.Schema;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.internal.tapandpay.v1.valuables.nano.CommonProto.SmartTap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.inject.Inject;

public class TestCaseHelper {
    private final Context context;
    private final SQLiteDatabase database;
    private final TestSuiteHelper testSuiteHelper;

    @Inject
    public TestCaseHelper(@ApplicationContext Context context, MerchantAppDbHelper merchantAppDbHelper, TestSuiteHelper testSuiteHelper) {
        this.database = merchantAppDbHelper.getWritableDatabase();
        this.context = context;
        this.testSuiteHelper = testSuiteHelper;
    }

    public long insertTestCase(TestCase testCase) {
        long insert = this.database.insert("test_case", null, getTestCaseContentValues(testCase));
        TestCase build = testCase.toBuilder().setId(insert).build();
        insertCustomStatuses(build);
        insertMaxCustomStatuses(build);
        insertNdefs(build);
        insertSmartTaps(build);
        return insert;
    }

    public void updateTestCase(TestCase testCase) {
        String[] strArr = new String[]{String.valueOf(testCase.id())};
        this.database.update("test_case", getTestCaseContentValues(testCase), "_id=?", strArr);
        deleteCustomStatuses(testCase.id());
        deleteMaxCustomStatuses(testCase.id());
        deleteNdefs(testCase.id());
        deleteSmartTaps(testCase.id());
        insertCustomStatuses(testCase);
        insertMaxCustomStatuses(testCase);
        insertNdefs(testCase);
        insertSmartTaps(testCase);
    }

    public void deleteTestCase(long j) {
        String[] strArr = new String[]{String.valueOf(j)};
        String str = "_id=?";
        TestCase readFullTestCase = readFullTestCase(j);
        if (!(readFullTestCase == null || readFullTestCase.tapVideoUri() == null)) {
            this.context.getContentResolver().delete(Uri.parse(readFullTestCase.tapVideoUri()), null, null);
        }
        this.database.delete("test_case", str, strArr);
    }

    public Cursor queryTestCaseSuite(long j) {
        String[] strArr = new String[]{String.valueOf(j)};
        return this.database.query("test_case", null, "test_suite_id=?", strArr, null, null, "_id ASC");
    }

    public List<TestCase> readTestCasesFromSuite(long j) {
        List<TestCase> arrayList = new ArrayList();
        Cursor queryTestCaseSuite = queryTestCaseSuite(j);
        while (queryTestCaseSuite.moveToNext()) {
            arrayList.add(readTestCaseFromCursor(queryTestCaseSuite));
        }
        queryTestCaseSuite.close();
        return arrayList;
    }

    public TestCase readFullTestCase(long j) {
        TestCase testCase = null;
        String[] strArr = new String[]{String.valueOf(j)};
        Cursor query = this.database.query("test_case", null, "_id=?", strArr, null, null, null);
        if (query.moveToFirst()) {
            testCase = readTestCaseFromCursor(query);
        }
        query.close();
        return testCase;
    }

    private TestCase readTestCaseFromCursor(Cursor cursor) {
        long j = getLong(cursor, "_id");
        String string = getString(cursor, "name");
        String string2 = getString(cursor, "expected_terminal_behavior");
        boolean bool = getBool(cursor, "support_generic_key_auth");
        boolean bool2 = getBool(cursor, "support_ecies");
        boolean bool3 = getBool(cursor, "override_transaction_mode");
        boolean bool4 = getBool(cursor, "payment_enabled");
        boolean bool5 = getBool(cursor, "payment_requested");
        boolean bool6 = getBool(cursor, "vas_enabled");
        boolean bool7 = getBool(cursor, "vas_requested");
        boolean bool8 = getBool(cursor, "use_ose");
        boolean bool9 = getBool(cursor, "ose_smarttap_1_3");
        int i = getInt(cursor, "ose_smarttap_1_3_priority");
        boolean bool10 = getBool(cursor, "ose_smarttap_2_0");
        int i2 = getInt(cursor, "ose_smarttap_2_0_priority");
        boolean bool11 = getBool(cursor, "ose_custom");
        byte[] blob = getBlob(cursor, "ose_custom_aid");
        int i3 = getInt(cursor, "ose_custom_priority");
        boolean bool12 = getBool(cursor, "support_smarttap_1_3");
        boolean bool13 = getBool(cursor, "support_smarttap_2_0");
        boolean bool14 = getBool(cursor, "allow_skipping_smart_tap_2_select");
        boolean bool15 = getBool(cursor, "include_master_nonce");
        boolean bool16 = getBool(cursor, "include_nonce_in_proprietary_data");
        byte[] blob2 = getBlob(cursor, "smarttap_min_version");
        byte[] blob3 = getBlob(cursor, "smarttap_max_version");
        String string3 = getString(cursor, "consumer_id");
        String string4 = getString(cursor, "tap_id");
        String string5 = getString(cursor, "device_id");
        boolean bool17 = getBool(cursor, "use_merchant_capabilities");
        boolean bool18 = getBool(cursor, "check_merchant_id");
        long j2 = getLong(cursor, "merchant_id");
        boolean bool19 = getBool(cursor, "require_encryption");
        boolean bool20 = getBool(cursor, "require_live_auth");
        long j3 = getLong(cursor, "test_suite_id");
        byte[] blob4 = getBlob(cursor, "merchant_public_key");
        String string6 = getString(cursor, "validation_schema");
        String string7 = getString(cursor, "tap_video_uri");
        boolean bool21 = getBool(cursor, "use_custom_ose_status_word");
        Builder id = TestCase.builder().setName(string).setExpectedTerminalBehavior(string2).setUseCustomOseStatusWord(bool21).setCustomOseStatusWord(getBlob(cursor, "custom_ose_status_word")).setCustomStatuses(readCustomStatuses(j)).setMaxCustomStatuses(readMaxCustomStatuses(j)).setAddedNdefs(readAddedNdefs(j)).setRemovedNdefs(readRemovedNdefs(j)).setSmartTaps(readSmartTaps(j)).setSupportGenericKeyAuth(bool).setSupportEcies(bool2).setOverrideTransactionMode(bool3).setPaymentEnabled(bool4).setPaymentRequested(bool5).setVasEnabled(bool6).setVasRequested(bool7).setOseEnabled(bool8).setOseSmartTap13(bool9).setOseSmartTap13Priority(i).setOseSmartTap20(bool10).setOseSmartTap20Priority(i2).setOseCustom(bool11).setOseCustomAid(Aid.valueOf(blob)).setOseCustomPriority(i3).setSupportSmartTap13(bool12).setSupportSmartTap20(bool13).setAllowSkippingSmartTap2Select(bool14).setIncludeMasterNonceInOseResponse(bool15).setIncludeNonceInProprietaryData(bool16).setSmartTapMinVersion(blob2).setSmartTapMaxVersion(blob3).setConsumerId(string3).setTapId(string4).setDeviceId(string5).setUseMerchantCapabilities(bool17).setCheckMerchantId(bool18).setMerchantId(j2).setRequireEncryption(bool19).setRequireLiveAuthentication(bool20).setTestSuiteId(j3).setMerchantPublicKey(blob4).setValidationSchema(Schema.fromString(string6)).setTapVideoUri(string7).setId(j);
        int columnIndex = cursor.getColumnIndex("get_data_response_status");
        if (!cursor.isNull(columnIndex)) {
            id.setGetDataResponseStatus(GetSmartTapDataResponse.valueOf(cursor.getString(columnIndex)));
        }
        columnIndex = cursor.getColumnIndex("stop_payment_command_type");
        if (!cursor.isNull(columnIndex)) {
            id.setStopPaymentsCommand(CommandType.valueOf(cursor.getString(columnIndex)));
        }
        return id.build();
    }

    private static boolean getBool(Cursor cursor, String str) {
        return cursor.getInt(cursor.getColumnIndex(str)) != 0;
    }

    private static int getInt(Cursor cursor, String str) {
        return cursor.getInt(cursor.getColumnIndex(str));
    }

    private static long getLong(Cursor cursor, String str) {
        return cursor.getLong(cursor.getColumnIndex(str));
    }

    private static String getString(Cursor cursor, String str) {
        return cursor.getString(cursor.getColumnIndex(str));
    }

    private static byte[] getBlob(Cursor cursor, String str) {
        return cursor.getBlob(cursor.getColumnIndex(str));
    }

    private Map<Byte, byte[]> readCustomStatuses(long j) {
        String[] strArr = new String[]{String.valueOf(j)};
        Cursor query = this.database.query("custom_statuses", null, "test_case_id=?", strArr, null, null, null);
        ImmutableMap.Builder builder = ImmutableMap.builder();
        while (query.moveToNext()) {
            byte b = (byte) query.getInt(query.getColumnIndex("command"));
            builder.put(Byte.valueOf(b), query.getBlob(query.getColumnIndex("status")));
        }
        return builder.build();
    }

    private Map<Byte, Integer> readMaxCustomStatuses(long j) {
        String[] strArr = new String[]{String.valueOf(j)};
        Cursor query = this.database.query("max_custom_statuses", null, "test_case_id=?", strArr, null, null, null);
        ImmutableMap.Builder builder = ImmutableMap.builder();
        while (query.moveToNext()) {
            builder.put(Byte.valueOf((byte) query.getInt(query.getColumnIndex("command"))), Integer.valueOf(query.getInt(query.getColumnIndex("max_status_overrides"))));
        }
        return builder.build();
    }

    private Multimap<ByteArrayWrapper, NdefRecord> readAddedNdefs(long j) {
        String[] strArr = new String[]{String.valueOf(j)};
        Cursor query = this.database.query("added_ndefs", null, "test_case_id=?", strArr, null, null, null);
        ImmutableMultimap.Builder builder = ImmutableMultimap.builder();
        while (query.moveToNext()) {
            try {
                builder.put(new ByteArrayWrapper(query.getBlob(query.getColumnIndex("parent_id"))), new NdefMessage(query.getBlob(query.getColumnIndex("ndef_payload"))).getRecords()[0]);
            } catch (Throwable e) {
                throw new IllegalArgumentException(e);
            }
        }
        return builder.build();
    }

    private Set<ByteArrayWrapper> readRemovedNdefs(long j) {
        String[] strArr = new String[]{String.valueOf(j)};
        Cursor query = this.database.query("removed_ndefs", null, "test_case_id=?", strArr, null, null, null);
        ImmutableSet.Builder builder = ImmutableSet.builder();
        while (query.moveToNext()) {
            builder.add(new ByteArrayWrapper(query.getBlob(query.getColumnIndex("ndef_id"))));
        }
        return builder.build();
    }

    private List<SmartTap> readSmartTaps(long j) {
        String[] strArr = new String[]{String.valueOf(j)};
        Cursor query = this.database.query("smart_tap", null, "test_case_id=?", strArr, null, null, null);
        List<SmartTap> arrayList = new ArrayList();
        while (query.moveToNext()) {
            SmartTap smartTap = new SmartTap();
            smartTap.type = query.getInt(query.getColumnIndex("type"));
            smartTap.smarttapMerchantId = query.getLong(query.getColumnIndex("merchant_id"));
            smartTap.programId = query.getLong(query.getColumnIndex("issuer"));
            Encoding.valueOf(query.getString(query.getColumnIndex("valueencoding"))).setTapValue(smartTap, query.getString(query.getColumnIndex("value")));
            String string = query.getString(query.getColumnIndex("pin"));
            if (!Strings.isNullOrEmpty(string)) {
                Encoding.valueOf(query.getString(query.getColumnIndex("pinencoding"))).setTapPin(smartTap, string);
            }
            string = query.getString(query.getColumnIndex("cvc"));
            if (!Strings.isNullOrEmpty(string)) {
                Encoding.valueOf(query.getString(query.getColumnIndex("cvcencoding"))).setTapCvc(smartTap, string);
            }
            smartTap.expirationMonth = query.getInt(query.getColumnIndex("expmonth"));
            smartTap.expirationYear = query.getInt(query.getColumnIndex("expyear"));
            arrayList.add(smartTap);
        }
        query.close();
        return arrayList;
    }

    private ContentValues getTestCaseContentValues(TestCase testCase) {
        String str;
        String str2 = null;
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", testCase.name());
        contentValues.put("expected_terminal_behavior", testCase.expectedTerminalBehavior());
        contentValues.put("support_generic_key_auth", Boolean.valueOf(testCase.supportGenericKeyAuth()));
        contentValues.put("support_ecies", Boolean.valueOf(testCase.supportEcies()));
        contentValues.put("override_transaction_mode", Boolean.valueOf(testCase.overrideTransactionMode()));
        contentValues.put("payment_enabled", Boolean.valueOf(testCase.paymentEnabled()));
        contentValues.put("payment_requested", Boolean.valueOf(testCase.paymentRequested()));
        contentValues.put("vas_enabled", Boolean.valueOf(testCase.vasEnabled()));
        contentValues.put("vas_requested", Boolean.valueOf(testCase.vasRequested()));
        contentValues.put("use_ose", Boolean.valueOf(testCase.oseEnabled()));
        contentValues.put("ose_smarttap_1_3", Boolean.valueOf(testCase.oseSmartTap13()));
        contentValues.put("ose_smarttap_1_3_priority", Integer.valueOf(testCase.oseSmartTap13Priority()));
        contentValues.put("ose_smarttap_2_0", Boolean.valueOf(testCase.oseSmartTap20()));
        contentValues.put("ose_smarttap_2_0_priority", Integer.valueOf(testCase.oseSmartTap20Priority()));
        contentValues.put("ose_custom", Boolean.valueOf(testCase.oseCustom()));
        contentValues.put("ose_custom_aid", testCase.oseCustomAid().array());
        contentValues.put("ose_custom_priority", Integer.valueOf(testCase.oseCustomPriority()));
        contentValues.put("support_smarttap_1_3", Boolean.valueOf(testCase.supportSmartTap13()));
        contentValues.put("support_smarttap_2_0", Boolean.valueOf(testCase.supportSmartTap20()));
        contentValues.put("allow_skipping_smart_tap_2_select", Boolean.valueOf(testCase.allowSkippingSmartTap2Select()));
        contentValues.put("include_master_nonce", Boolean.valueOf(testCase.includeMasterNonceInOseResponse()));
        contentValues.put("include_nonce_in_proprietary_data", Boolean.valueOf(testCase.includeNonceInProprietaryData()));
        contentValues.put("smarttap_min_version", testCase.smartTapMinVersion());
        contentValues.put("smarttap_max_version", testCase.smartTapMaxVersion());
        contentValues.put("consumer_id", testCase.consumerId());
        contentValues.put("tap_id", testCase.tapId());
        contentValues.put("device_id", testCase.deviceId());
        contentValues.put("description", testCase.getDescription());
        contentValues.put("use_merchant_capabilities", Boolean.valueOf(testCase.useMerchantCapabilities()));
        contentValues.put("test_suite_id", Long.valueOf(testCase.testSuiteId()));
        contentValues.put("merchant_public_key", testCase.merchantPublicKey());
        contentValues.put("check_merchant_id", Boolean.valueOf(testCase.checkMerchantId()));
        contentValues.put("merchant_id", Long.valueOf(testCase.merchantId()));
        contentValues.put("require_encryption", Boolean.valueOf(testCase.requireEncryption()));
        contentValues.put("require_live_auth", Boolean.valueOf(testCase.requireLiveAuthentication()));
        contentValues.put("tap_video_uri", testCase.tapVideoUri());
        contentValues.put("use_custom_ose_status_word", Boolean.valueOf(testCase.useCustomOseStatusWord()));
        contentValues.put("custom_ose_status_word", testCase.customOseStatusWord());
        GetSmartTapDataResponse dataResponseStatus = testCase.getDataResponseStatus();
        if (dataResponseStatus == null) {
            str = null;
        } else {
            str = dataResponseStatus.toString();
        }
        contentValues.put("get_data_response_status", str);
        CommandType stopPaymentsCommand = testCase.stopPaymentsCommand();
        contentValues.put("stop_payment_command_type", stopPaymentsCommand == null ? null : stopPaymentsCommand.toString());
        if (testCase.validationSchema() != null) {
            str2 = testCase.validationSchema().toString();
        }
        contentValues.put("validation_schema", str2);
        return contentValues;
    }

    private ContentValues getCustomStatusValues(Entry<Byte, byte[]> entry, long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("command", Byte.valueOf(((Byte) entry.getKey()).byteValue()));
        contentValues.put("status", (byte[]) entry.getValue());
        contentValues.put("test_case_id", Long.valueOf(j));
        return contentValues;
    }

    private ContentValues getMaxCustomStatusValues(Entry<Byte, Integer> entry, long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("command", Byte.valueOf(((Byte) entry.getKey()).byteValue()));
        contentValues.put("max_status_overrides", (Integer) entry.getValue());
        contentValues.put("test_case_id", Long.valueOf(j));
        return contentValues;
    }

    private ContentValues getAddedNdefValues(Entry<ByteArrayWrapper, NdefRecord> entry, long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("parent_id", ((ByteArrayWrapper) entry.getKey()).array());
        contentValues.put("ndef_payload", new NdefMessage((NdefRecord) entry.getValue(), new NdefRecord[0]).toByteArray());
        contentValues.put("test_case_id", Long.valueOf(j));
        return contentValues;
    }

    private ContentValues getRemovedNdefValues(ByteArrayWrapper byteArrayWrapper, long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ndef_id", byteArrayWrapper.array());
        contentValues.put("test_case_id", Long.valueOf(j));
        return contentValues;
    }

    private ContentValues getSmartTapValues(SmartTap smartTap, long j) {
        ContentValues contentValues = new ContentValues();
        EncodingValue valueEncoding = EncodingValue.getValueEncoding(smartTap);
        EncodingValue pinEncoding = EncodingValue.getPinEncoding(smartTap);
        EncodingValue cvcEncoding = EncodingValue.getCvcEncoding(smartTap);
        contentValues.put("test_case_id", Long.valueOf(j));
        contentValues.put("type", Integer.valueOf(smartTap.type));
        contentValues.put("merchant_id", Long.valueOf(smartTap.smarttapMerchantId));
        contentValues.put("issuer", Long.valueOf(smartTap.programId));
        contentValues.put("value", valueEncoding.getValue());
        contentValues.put("valueencoding", valueEncoding.getEncoding().toString());
        contentValues.put("pin", pinEncoding.getValue());
        contentValues.put("pinencoding", pinEncoding.getEncoding().toString());
        contentValues.put("cvc", cvcEncoding.getValue());
        contentValues.put("cvcencoding", cvcEncoding.getEncoding().toString());
        contentValues.put("expmonth", Integer.valueOf(smartTap.expirationMonth));
        contentValues.put("expyear", Integer.valueOf(smartTap.expirationYear));
        return contentValues;
    }

    private void insertCustomStatuses(TestCase testCase) {
        for (Entry customStatusValues : testCase.customStatuses().entrySet()) {
            this.database.insert("custom_statuses", null, getCustomStatusValues(customStatusValues, testCase.id()));
        }
    }

    private void insertMaxCustomStatuses(TestCase testCase) {
        for (Entry maxCustomStatusValues : testCase.maxCustomStatuses().entrySet()) {
            this.database.insert("max_custom_statuses", null, getMaxCustomStatusValues(maxCustomStatusValues, testCase.id()));
        }
    }

    private void insertNdefs(TestCase testCase) {
        for (Entry addedNdefValues : testCase.addedNdefs().entries()) {
            this.database.insert("added_ndefs", null, getAddedNdefValues(addedNdefValues, testCase.id()));
        }
        for (ByteArrayWrapper removedNdefValues : testCase.removedNdefs()) {
            this.database.insert("removed_ndefs", null, getRemovedNdefValues(removedNdefValues, testCase.id()));
        }
    }

    private void insertSmartTaps(TestCase testCase) {
        for (SmartTap smartTapValues : testCase.smartTaps()) {
            this.database.insert("smart_tap", null, getSmartTapValues(smartTapValues, testCase.id()));
        }
    }

    private void deleteCustomStatuses(long j) {
        String[] strArr = new String[]{String.valueOf(j)};
        this.database.delete("custom_statuses", "test_case_id=?", strArr);
    }

    private void deleteMaxCustomStatuses(long j) {
        String[] strArr = new String[]{String.valueOf(j)};
        this.database.delete("max_custom_statuses", "test_case_id=?", strArr);
    }

    private void deleteNdefs(long j) {
        String[] strArr = new String[]{String.valueOf(j)};
        this.database.delete("added_ndefs", "test_case_id=?", strArr);
        this.database.delete("removed_ndefs", "test_case_id=?", strArr);
    }

    private void deleteSmartTaps(long j) {
        String[] strArr = new String[]{String.valueOf(j)};
        this.database.delete("smart_tap", "test_case_id=?", strArr);
    }

    public Optional<Long> getFirstIdInTestSuite(String str) {
        Optional testSuiteId = this.testSuiteHelper.getTestSuiteId(str);
        Optional<Long> absent = Optional.absent();
        if (!testSuiteId.isPresent()) {
            return absent;
        }
        Optional<Long> of;
        String[] strArr = new String[]{"_id"};
        String[] strArr2 = new String[]{String.valueOf(testSuiteId.get())};
        Cursor query = this.database.query("test_case", strArr, "test_suite_id=?", strArr2, null, null, "_id ASC", "1");
        if (query.moveToFirst()) {
            of = Optional.of(Long.valueOf(query.getLong(query.getColumnIndex("_id"))));
        } else {
            of = absent;
        }
        query.close();
        return of;
    }

    public Optional<Long> getTestCaseInTestSuite(String str, String str2) {
        Optional testSuiteId = this.testSuiteHelper.getTestSuiteId(str);
        Optional<Long> absent = Optional.absent();
        if (!testSuiteId.isPresent()) {
            return absent;
        }
        Optional<Long> of;
        String[] strArr = new String[]{"_id"};
        String[] strArr2 = new String[]{String.valueOf(testSuiteId.get()), new StringBuilder(String.valueOf(str2).length() + 1).append(str2).append("%").toString()};
        Cursor query = this.database.query("test_case", strArr, "test_suite_id=? AND name LIKE ?", strArr2, null, null, "_id ASC", "1");
        if (query.moveToFirst()) {
            of = Optional.of(Long.valueOf(query.getLong(query.getColumnIndex("_id"))));
        } else {
            of = absent;
        }
        query.close();
        return of;
    }

    public Optional<Long> getNextValidId(long j) {
        Optional testSuiteId = getTestSuiteId(j);
        Optional<Long> absent = Optional.absent();
        if (!testSuiteId.isPresent()) {
            return absent;
        }
        Optional<Long> of;
        String[] strArr = new String[]{"_id"};
        String[] strArr2 = new String[]{String.valueOf(j), String.valueOf(testSuiteId.get())};
        Cursor query = this.database.query("test_case", strArr, "_id>? AND test_suite_id=?", strArr2, null, null, "_id ASC", "1");
        if (query.moveToFirst()) {
            of = Optional.of(Long.valueOf(query.getLong(query.getColumnIndex("_id"))));
        } else {
            of = absent;
        }
        query.close();
        return of;
    }

    private Optional<Long> getTestSuiteId(long j) {
        String[] strArr = new String[]{"test_suite_id"};
        String[] strArr2 = new String[]{String.valueOf(j)};
        Cursor query = this.database.query("test_case", strArr, "_id=?", strArr2, null, null, null, "1");
        Optional<Long> absent = Optional.absent();
        if (query.moveToFirst()) {
            absent = Optional.of(Long.valueOf(query.getLong(query.getColumnIndex("test_suite_id"))));
        }
        query.close();
        return absent;
    }
}
