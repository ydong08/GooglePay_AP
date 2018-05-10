package com.google.commerce.tapandpay.terminalapp.application;

import com.google.android.apps.common.inject.InjectedApplication;
import java.util.ArrayList;
import java.util.List;

public class TerminalTapAndPayApplication extends InjectedApplication {
    public void onCreate() {
        super.initGraph();
        super.onCreate();
    }

    protected List<Object> getModules() {
        List<Object> arrayList = new ArrayList();
        arrayList.add(TerminalTapAndPayModule.class);
        return arrayList;
    }
}
