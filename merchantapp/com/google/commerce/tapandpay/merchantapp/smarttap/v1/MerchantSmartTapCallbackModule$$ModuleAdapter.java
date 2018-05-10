package com.google.commerce.tapandpay.merchantapp.smarttap.v1;

import android.content.Context;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback;
import com.google.commerce.tapandpay.merchantapp.settings.Settings;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.Linker;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import java.util.Set;
import javax.inject.Provider;

public final class MerchantSmartTapCallbackModule$$ModuleAdapter extends ModuleAdapter<MerchantSmartTapCallbackModule> {
    private static final Class<?>[] INCLUDES = new Class[0];
    private static final String[] INJECTS = new String[]{"com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback"};
    private static final Class<?>[] STATIC_INJECTIONS = new Class[0];

    /* compiled from: MerchantSmartTapCallbackModule$$ModuleAdapter */
    public static final class GetSmartTapCallbackProvidesAdapter extends ProvidesBinding<SmartTapCallback> implements Provider<SmartTapCallback> {
        private Binding<Context> context;
        private Binding<TestCaseHelper> database;
        private final MerchantSmartTapCallbackModule module;
        private Binding<Settings> settings;

        public GetSmartTapCallbackProvidesAdapter(MerchantSmartTapCallbackModule merchantSmartTapCallbackModule) {
            super("com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback", false, "com.google.commerce.tapandpay.merchantapp.smarttap.v1.MerchantSmartTapCallbackModule", "getSmartTapCallback");
            this.module = merchantSmartTapCallbackModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", MerchantSmartTapCallbackModule.class, getClass().getClassLoader());
            this.database = linker.requestBinding("com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper", MerchantSmartTapCallbackModule.class, getClass().getClassLoader());
            this.settings = linker.requestBinding("com.google.commerce.tapandpay.merchantapp.settings.Settings", MerchantSmartTapCallbackModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
            set.add(this.database);
            set.add(this.settings);
        }

        public SmartTapCallback get() {
            return this.module.getSmartTapCallback((Context) this.context.get(), (TestCaseHelper) this.database.get(), (Settings) this.settings.get());
        }
    }

    public MerchantSmartTapCallbackModule$$ModuleAdapter() {
        super(MerchantSmartTapCallbackModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, false, true);
    }

    public MerchantSmartTapCallbackModule newModule() {
        return new MerchantSmartTapCallbackModule();
    }

    public void getBindings(BindingsGroup bindingsGroup, MerchantSmartTapCallbackModule merchantSmartTapCallbackModule) {
        bindingsGroup.contributeProvidesBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback", new GetSmartTapCallbackProvidesAdapter(merchantSmartTapCallbackModule));
    }
}
