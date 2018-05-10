package com.google.commerce.tapandpay.merchantapp.smarttap.v2;

import com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetSmartTapDataCommand;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NegotiateSmartTapCommand;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.PushSmartTapDataCommand;
import dagger.Module;

@Module(complete = false, injects = {GetSmartTapDataCommand.class, NegotiateSmartTapCommand.class, PushSmartTapDataCommand.class}, library = true)
public class MerchantSmartTapCommandModule {
}
