package com.google.commerce.tapandpay.merchantapp.smarttap.v1;

import com.google.android.libraries.commerce.hce.applet.smarttap.v1.GetSmartTapDataCommand;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.PostTransactionDataCommand;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.QualifierAnnotations.GetSmartTapData;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.QualifierAnnotations.PostTransactionData;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCommand;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class MerchantSmartTapCommandModule {
    @GetSmartTapData
    @Provides
    SmartTapCommand provideGetSmartTapDataCommand(GetSmartTapDataCommand getSmartTapDataCommand) {
        return getSmartTapDataCommand;
    }

    @PostTransactionData
    @Provides
    SmartTapCommand providePostTransactionDataCommand(PostTransactionDataCommand postTransactionDataCommand) {
        return postTransactionDataCommand;
    }
}
