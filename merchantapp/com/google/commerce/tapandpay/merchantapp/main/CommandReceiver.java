package com.google.commerce.tapandpay.merchantapp.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.merchantapp.result.Result;
import com.google.commerce.tapandpay.merchantapp.result.ResultHelper;
import com.google.commerce.tapandpay.merchantapp.settings.Settings;
import com.google.commerce.tapandpay.merchantapp.sharing.JsonConverter;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import com.google.commerce.tapandpay.merchantapp.testcase.TestSuiteHelper;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import java.util.List;
import javax.inject.Inject;

public class CommandReceiver extends BroadcastReceiver {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    @Inject
    JsonConverter jsonConverter;
    @Inject
    ResultHelper resultHelper;
    @Inject
    Settings settings;
    @Inject
    TestCaseHelper testCaseHelper;
    @Inject
    TestSuiteHelper testSuiteHelper;

    public void onReceive(Context context, Intent intent) {
        if (this.testCaseHelper == null) {
            ((InjectedApplication) context.getApplicationContext()).inject(this);
        }
        if (intent.getAction().equals("com.google.commerce.tapandpay.merchantapp.LOAD_TEST_CASES")) {
            loadTestCases(context, intent);
        } else if (intent.getAction().equals("com.google.commerce.tapandpay.merchantapp.LOAD_WITH_EXPECTED")) {
            loadTestsAndExpectedResults(context, intent);
        } else if (intent.getAction().equals("com.google.commerce.tapandpay.merchantapp.DELETE_TEST_SUITES")) {
            deleteTestSuites(context);
        } else if (intent.getAction().equals("com.google.commerce.tapandpay.merchantapp.AUTO_ADVANCE")) {
            setAutoAdvancing(intent);
        } else if (intent.getAction().equals("com.google.commerce.tapandpay.merchantapp.SEND_LOYALTY")) {
            setSendLoyalty(intent);
        } else if (intent.getAction().equals("com.google.commerce.tapandpay.merchantapp.SEND_OFFER")) {
            setSendOffer(intent);
        } else if (intent.getAction().equals("com.google.commerce.tapandpay.merchantapp.SEND_GIFT_CARD")) {
            setSendGiftCard(intent);
        } else if (intent.getAction().equals("com.google.commerce.tapandpay.merchantapp.SEND_PLC")) {
            setSendPlc(intent);
        } else if (intent.getAction().equals("com.google.commerce.tapandpay.merchantapp.RESET_ACTIVE")) {
            resetActiveTestCase(context, intent);
        } else if (intent.getAction().equals("com.google.commerce.tapandpay.merchantapp.SET_ACTIVE")) {
            setActiveTestCase(context, intent);
        } else if (intent.getAction().equals("com.google.commerce.tapandpay.merchantapp.SET_MERCHANT_PUBLIC_KEY")) {
            setMerchantPublicKey(intent);
        }
    }

    private void loadTestsAndExpectedResults(Context context, Intent intent) {
        if (Strings.isNullOrEmpty(intent.getStringExtra("load_test_cases")) || Strings.isNullOrEmpty(intent.getStringExtra("expected_results"))) {
            LOG.w("Both test cases and results must be specified nonempty strings", new Object[0]);
            return;
        }
        try {
            List fromJsonArray = TestCase.fromJsonArray(intent.getStringExtra("load_test_cases"));
            List fromJsonArray2 = this.jsonConverter.fromJsonArray(intent.getStringExtra("expected_results"));
            if (fromJsonArray.size() != fromJsonArray2.size()) {
                LOG.e("Test case and result json arrays were mismatched in size", new Object[0]);
                return;
            }
            long loadTestSuite = loadTestSuite(intent);
            if (loadTestSuite >= 0) {
                for (int i = 0; i < fromJsonArray.size(); i++) {
                    this.resultHelper.insertExpectedResult((Result) fromJsonArray2.get(i), this.testCaseHelper.insertTestCase(((TestCase) fromJsonArray.get(i)).toBuilder().setTestSuiteId(loadTestSuite).build()));
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.google.commerce.tapandpay.merchantapp.UPDATE_CURSORS"));
            }
        } catch (Throwable e) {
            LOG.e(e, "Illegal format for json arrays", new Object[0]);
        }
    }

    private void loadTestCases(Context context, Intent intent) {
        if (Strings.isNullOrEmpty(intent.getStringExtra("load_test_cases"))) {
            LOG.e("No test cases were specified", new Object[0]);
            return;
        }
        try {
            List<TestCase> fromJsonArray = TestCase.fromJsonArray(intent.getStringExtra("load_test_cases"));
            long loadTestSuite = loadTestSuite(intent);
            if (loadTestSuite >= 0) {
                for (TestCase toBuilder : fromJsonArray) {
                    this.testCaseHelper.insertTestCase(toBuilder.toBuilder().setTestSuiteId(loadTestSuite).build());
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.google.commerce.tapandpay.merchantapp.UPDATE_CURSORS"));
            }
        } catch (Throwable e) {
            LOG.e(e, "Illegal format for test case json array", new Object[0]);
        }
    }

    private long loadTestSuite(Intent intent) {
        if (Strings.isNullOrEmpty(intent.getStringExtra("test_suite_name"))) {
            LOG.w("Test suite name must be specified and nonempty", new Object[0]);
            return -1;
        }
        long insertTestSuite = this.testSuiteHelper.insertTestSuite(intent.getStringExtra("test_suite_name"));
        if (insertTestSuite >= 0) {
            return insertTestSuite;
        }
        LOG.e("A test suite with that name already exists or a problem with the database occurred", new Object[0]);
        return insertTestSuite;
    }

    private void deleteTestSuites(Context context) {
        this.testSuiteHelper.deleteAllTestSuites();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong("active_testsuite", this.testSuiteHelper.insertTestSuite(context.getString(R.string.default_test_suite_name))).commit();
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.google.commerce.tapandpay.merchantapp.UPDATE_CURSORS"));
    }

    private void setAutoAdvancing(Intent intent) {
        if (intent.hasExtra("auto_advance")) {
            this.settings.setAutoAdvance(intent.getBooleanExtra("auto_advance", false));
        } else {
            LOG.e("No argument was specified for auto advance setting", new Object[0]);
        }
    }

    private void setSendLoyalty(Intent intent) {
        if (intent.hasExtra("send_loyalty")) {
            this.settings.setSendLoyalty(intent.getBooleanExtra("send_loyalty", true));
        } else {
            LOG.e("No argument was specified for sending loyalty", new Object[0]);
        }
    }

    private void setSendOffer(Intent intent) {
        if (intent.hasExtra("send_offer")) {
            this.settings.setSendOffer(intent.getBooleanExtra("send_offer", true));
        } else {
            LOG.e("No argument was specified for sending offer", new Object[0]);
        }
    }

    private void setSendGiftCard(Intent intent) {
        if (intent.hasExtra("send_gift_card")) {
            this.settings.setSendGiftCard(intent.getBooleanExtra("send_gift_card", true));
        } else {
            LOG.e("No argument was specified for sending gift card", new Object[0]);
        }
    }

    private void setSendPlc(Intent intent) {
        if (intent.hasExtra("send_plc")) {
            this.settings.setSendPlc(intent.getBooleanExtra("send_plc", true));
        } else {
            LOG.e("No argument was specified for sending plc", new Object[0]);
        }
    }

    private void resetActiveTestCase(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra("test_suite_name");
        if (Strings.isNullOrEmpty(stringExtra)) {
            LOG.w("Test suite name must be specified and nonempty", new Object[0]);
            return;
        }
        Optional firstIdInTestSuite = this.testCaseHelper.getFirstIdInTestSuite(stringExtra);
        if (firstIdInTestSuite.isPresent()) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putLong("active_testcase", ((Long) firstIdInTestSuite.get()).longValue()).commit();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.google.commerce.tapandpay.merchantapp.UPDATE_CURSORS"));
            return;
        }
        LOG.e("There was no test case to activate", new Object[0]);
    }

    private void setActiveTestCase(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra("test_suite_name");
        String stringExtra2 = intent.getStringExtra("test_case_name_prefix");
        if (Strings.isNullOrEmpty(stringExtra)) {
            LOG.w("Test suite name must be specified and nonempty", new Object[0]);
        } else if (Strings.isNullOrEmpty(stringExtra2)) {
            LOG.w("Test case name prefix must be specified and nonempty", new Object[0]);
        } else {
            Optional testCaseInTestSuite = this.testCaseHelper.getTestCaseInTestSuite(stringExtra, stringExtra2);
            if (testCaseInTestSuite.isPresent()) {
                PreferenceManager.getDefaultSharedPreferences(context).edit().putLong("active_testcase", ((Long) testCaseInTestSuite.get()).longValue()).commit();
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.google.commerce.tapandpay.merchantapp.UPDATE_CURSORS"));
                return;
            }
            LOG.e("There was no test case with prefix %s in test suite %s to activate", stringExtra2, stringExtra);
        }
    }

    private void setMerchantPublicKey(Intent intent) {
        long longExtra = intent.getLongExtra("test_case_id", -1);
        String stringExtra = intent.getStringExtra("merchant_public_key");
        TestCase readFullTestCase = this.testCaseHelper.readFullTestCase(longExtra);
        if (Strings.isNullOrEmpty(stringExtra)) {
            LOG.w("Public key must be specified and nonempty", new Object[0]);
        } else if (readFullTestCase == null) {
            LOG.w("Valid test case id must be specified", new Object[0]);
        } else {
            try {
                this.testCaseHelper.updateTestCase(readFullTestCase.toBuilder().setMerchantPublicKey(Hex.decode(stringExtra.replaceAll("\\s+", ""))).build());
            } catch (Throwable e) {
                LOG.e(e, "Invalid hex string was given", new Object[0]);
            }
        }
    }
}
