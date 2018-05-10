package com.google.commerce.tapandpay.merchantapp.smarttap;

import android.content.Context;
import android.preference.PreferenceManager;
import com.google.android.libraries.commerce.hce.util.Bcd;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import com.google.common.base.Strings;

public class MerchantSmartTapCallbackHelper {
    private MerchantSmartTapCallbackHelper() {
    }

    public static byte[] getConsumerId(Context context, TestCaseHelper testCaseHelper, long j) {
        long j2 = PreferenceManager.getDefaultSharedPreferences(context).getLong("active_testcase", -1);
        if (j2 >= 0) {
            TestCase readFullTestCase = testCaseHelper.readFullTestCase(j2);
            if (!(readFullTestCase == null || Strings.isNullOrEmpty(readFullTestCase.consumerId()))) {
                return Bcd.encode(readFullTestCase.consumerId());
            }
        }
        return Bcd.encode(0);
    }
}
