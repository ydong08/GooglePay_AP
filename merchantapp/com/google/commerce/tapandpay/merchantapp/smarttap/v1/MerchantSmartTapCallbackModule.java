package com.google.commerce.tapandpay.merchantapp.smarttap.v1;

import android.content.Context;
import com.google.android.apps.common.inject.annotation.ApplicationContext;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback;
import com.google.commerce.tapandpay.merchantapp.settings.Settings;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, injects = {SmartTapCallback.class}, library = true)
public class MerchantSmartTapCallbackModule {
    @Provides
    SmartTapCallback getSmartTapCallback(@ApplicationContext Context context, TestCaseHelper testCaseHelper, Settings settings) {
        return new MerchantSmartTapCallback(context, testCaseHelper, settings);
    }
}
