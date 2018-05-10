package com.google.commerce.tapandpay.merchantapp.application;

import com.google.android.apps.common.inject.InjectedApplication;
import java.util.ArrayList;
import java.util.List;

public class MerchantTapAndPayApplication extends InjectedApplication {
    public void onCreate() {
        super.initGraph();
        super.onCreate();
    }

    protected List<Object> getModules() {
        List<Object> arrayList = new ArrayList();
        arrayList.add(MerchantTapAndPayModule.class);
        return arrayList;
    }
}
