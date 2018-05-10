package com.google.commerce.tapandpay.merchantapp.smarttap.v2;

import android.content.Context;
import com.google.android.apps.common.inject.annotation.ApplicationContext;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapCallback;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager;
import com.google.commerce.tapandpay.merchantapp.settings.Settings;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, injects = {SmartTapCallback.class}, library = true)
public class MerchantSmartTapCallbackModule {
    @Provides
    SmartTapCallback getSmartTapCallback(@ApplicationContext Context context, TestCaseHelper testCaseHelper, Settings settings, SmartTap2ECKeyManager smartTap2ECKeyManager) {
        return new MerchantSmartTapCallback(context, testCaseHelper, settings, smartTap2ECKeyManager);
    }
}
