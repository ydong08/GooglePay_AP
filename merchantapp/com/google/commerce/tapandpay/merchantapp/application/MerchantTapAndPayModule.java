package com.google.commerce.tapandpay.merchantapp.application;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.cardemulation.CardEmulation;
import com.google.android.apps.common.inject.annotation.ApplicationContext;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapApplet;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2CryptoModule;
import com.google.commerce.tapandpay.merchantapp.application.QualifierAnnotations.CurrentTimeMillis;
import com.google.commerce.tapandpay.merchantapp.hce.MerchantApduService;
import com.google.commerce.tapandpay.merchantapp.hce.PayPassProcessor;
import com.google.commerce.tapandpay.merchantapp.hce.SmartTapV1Processor;
import com.google.commerce.tapandpay.merchantapp.hce.SmartTapV2Processor;
import com.google.commerce.tapandpay.merchantapp.hce.TestProcessor;
import com.google.commerce.tapandpay.merchantapp.main.CommandReceiver;
import com.google.commerce.tapandpay.merchantapp.main.DownloadTestCasesTask;
import com.google.commerce.tapandpay.merchantapp.main.MainActivity;
import com.google.commerce.tapandpay.merchantapp.main.ShareTestSuiteTask;
import com.google.commerce.tapandpay.merchantapp.main.updatetasks.TestSuiteTask;
import com.google.commerce.tapandpay.merchantapp.main.updatetasks.UiUpdateTask;
import com.google.commerce.tapandpay.merchantapp.paypass.PayPassMagStripe;
import com.google.commerce.tapandpay.merchantapp.result.InsertResultTask;
import com.google.commerce.tapandpay.merchantapp.result.ReadResultsTask;
import com.google.commerce.tapandpay.merchantapp.resultview.ExpectedResultActivity;
import com.google.commerce.tapandpay.merchantapp.resultview.ResultPageFragment;
import com.google.commerce.tapandpay.merchantapp.resultview.ShareAllJsonTask;
import com.google.commerce.tapandpay.merchantapp.resultview.SmartTapV2ViewInflater;
import com.google.commerce.tapandpay.merchantapp.resultview.UserResultActivity;
import com.google.commerce.tapandpay.merchantapp.settings.SettingsActivity;
import com.google.commerce.tapandpay.merchantapp.smarttap.v1.MerchantSmartTapCallbackModule;
import com.google.commerce.tapandpay.merchantapp.smarttap.v1.MerchantSmartTapCommandModule;
import com.google.commerce.tapandpay.merchantapp.smarttap.v2.MerchantValuableConverter;
import com.google.commerce.tapandpay.merchantapp.sqlite.MerchantAppDbHelper;
import com.google.commerce.tapandpay.merchantapp.testcaseview.DownloadValidationSchemaTask;
import com.google.commerce.tapandpay.merchantapp.testcaseview.DownloadVideoTask;
import com.google.commerce.tapandpay.merchantapp.testcaseview.TestCaseActivity;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(complete = true, includes = {MerchantSmartTapCallbackModule.class, MerchantSmartTapCommandModule.class, com.google.commerce.tapandpay.merchantapp.smarttap.v2.MerchantSmartTapCallbackModule.class, com.google.commerce.tapandpay.merchantapp.smarttap.v2.MerchantSmartTapCommandModule.class, SmartTap2CryptoModule.class}, injects = {SmartTapApplet.class, MerchantApduService.class, TestProcessor.class, com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapApplet.class, TestCaseActivity.class, MainActivity.class, SettingsActivity.class, UserResultActivity.class, ExpectedResultActivity.class, CommandReceiver.class, ResultPageFragment.class, SmartTapV2ViewInflater.class, ShareTestSuiteTask.class, ShareAllJsonTask.class, DownloadTestCasesTask.class, DownloadValidationSchemaTask.class, DownloadVideoTask.class, TestSuiteTask.class, UiUpdateTask.class, ReadResultsTask.class, InsertResultTask.class}, library = false)
public class MerchantTapAndPayModule {
    @Singleton
    @Provides
    public static MerchantAppDbHelper getDbHelper(@ApplicationContext Context context) {
        return new MerchantAppDbHelper(context);
    }

    @Provides
    public static PayPassMagStripe getPayPassMagStripe() {
        return new PayPassMagStripe();
    }

    @CurrentTimeMillis
    @Provides
    public static Long getCurrentTimeMillis() {
        return Long.valueOf(System.currentTimeMillis());
    }

    @Provides
    public static CardEmulation getCardEmulation(@ApplicationContext Context context) {
        NfcAdapter defaultAdapter = NfcAdapter.getDefaultAdapter(context);
        if (defaultAdapter == null) {
            return null;
        }
        return CardEmulation.getInstance(defaultAdapter);
    }

    @Provides
    public static SmartTapV2Processor getSmartTapV2Processor(com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapApplet smartTapApplet) {
        return new SmartTapV2Processor(smartTapApplet);
    }

    @Provides
    public static SmartTapV1Processor getSmartTapV1Processor(SmartTapApplet smartTapApplet) {
        return new SmartTapV1Processor(smartTapApplet);
    }

    @Provides
    public static PayPassProcessor getPayPassProcessor(PayPassMagStripe payPassMagStripe) {
        return new PayPassProcessor(payPassMagStripe);
    }

    @Provides
    public static ServiceObjectConverter getServiceObjectConverter() {
        return new MerchantValuableConverter();
    }
}
