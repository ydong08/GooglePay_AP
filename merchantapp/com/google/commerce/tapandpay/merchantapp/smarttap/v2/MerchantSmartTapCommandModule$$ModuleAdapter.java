package com.google.commerce.tapandpay.merchantapp.smarttap.v2;

import dagger.internal.ModuleAdapter;

public final class MerchantSmartTapCommandModule$$ModuleAdapter extends ModuleAdapter<MerchantSmartTapCommandModule> {
    private static final Class<?>[] INCLUDES = new Class[0];
    private static final String[] INJECTS = new String[]{"members/com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetSmartTapDataCommand", "members/com.google.android.libraries.commerce.hce.applet.smarttap.v2.NegotiateSmartTapCommand", "members/com.google.android.libraries.commerce.hce.applet.smarttap.v2.PushSmartTapDataCommand"};
    private static final Class<?>[] STATIC_INJECTIONS = new Class[0];

    public MerchantSmartTapCommandModule$$ModuleAdapter() {
        super(MerchantSmartTapCommandModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, false, true);
    }

    public MerchantSmartTapCommandModule newModule() {
        return new MerchantSmartTapCommandModule();
    }
}
