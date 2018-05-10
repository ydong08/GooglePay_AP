package com.google.commerce.tapandpay.merchantapp.application;

import android.content.Context;
import android.nfc.cardemulation.CardEmulation;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapApplet;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2CryptoModule;
import com.google.commerce.tapandpay.merchantapp.hce.PayPassProcessor;
import com.google.commerce.tapandpay.merchantapp.hce.SmartTapV1Processor;
import com.google.commerce.tapandpay.merchantapp.hce.SmartTapV2Processor;
import com.google.commerce.tapandpay.merchantapp.paypass.PayPassMagStripe;
import com.google.commerce.tapandpay.merchantapp.smarttap.v1.MerchantSmartTapCallbackModule;
import com.google.commerce.tapandpay.merchantapp.smarttap.v1.MerchantSmartTapCommandModule;
import com.google.commerce.tapandpay.merchantapp.sqlite.MerchantAppDbHelper;
import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.Linker;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import java.util.Set;
import javax.inject.Provider;

public final class MerchantTapAndPayModule$$ModuleAdapter extends ModuleAdapter<MerchantTapAndPayModule> {
    private static final Class<?>[] INCLUDES = new Class[]{MerchantSmartTapCallbackModule.class, MerchantSmartTapCommandModule.class, com.google.commerce.tapandpay.merchantapp.smarttap.v2.MerchantSmartTapCallbackModule.class, com.google.commerce.tapandpay.merchantapp.smarttap.v2.MerchantSmartTapCommandModule.class, SmartTap2CryptoModule.class};
    private static final String[] INJECTS = new String[]{"members/com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapApplet", "members/com.google.commerce.tapandpay.merchantapp.hce.MerchantApduService", "members/com.google.commerce.tapandpay.merchantapp.hce.TestProcessor", "members/com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapApplet", "members/com.google.commerce.tapandpay.merchantapp.testcaseview.TestCaseActivity", "members/com.google.commerce.tapandpay.merchantapp.main.MainActivity", "members/com.google.commerce.tapandpay.merchantapp.settings.SettingsActivity", "members/com.google.commerce.tapandpay.merchantapp.resultview.UserResultActivity", "members/com.google.commerce.tapandpay.merchantapp.resultview.ExpectedResultActivity", "members/com.google.commerce.tapandpay.merchantapp.main.CommandReceiver", "members/com.google.commerce.tapandpay.merchantapp.resultview.ResultPageFragment", "members/com.google.commerce.tapandpay.merchantapp.resultview.SmartTapV2ViewInflater", "members/com.google.commerce.tapandpay.merchantapp.main.ShareTestSuiteTask", "members/com.google.commerce.tapandpay.merchantapp.resultview.ShareAllJsonTask", "members/com.google.commerce.tapandpay.merchantapp.main.DownloadTestCasesTask", "members/com.google.commerce.tapandpay.merchantapp.testcaseview.DownloadValidationSchemaTask", "members/com.google.commerce.tapandpay.merchantapp.testcaseview.DownloadVideoTask", "members/com.google.commerce.tapandpay.merchantapp.main.updatetasks.TestSuiteTask", "members/com.google.commerce.tapandpay.merchantapp.main.updatetasks.UiUpdateTask", "members/com.google.commerce.tapandpay.merchantapp.result.ReadResultsTask", "members/com.google.commerce.tapandpay.merchantapp.result.InsertResultTask"};
    private static final Class<?>[] STATIC_INJECTIONS = new Class[0];

    /* compiled from: MerchantTapAndPayModule$$ModuleAdapter */
    public static final class GetCardEmulationProvidesAdapter extends ProvidesBinding<CardEmulation> implements Provider<CardEmulation> {
        private Binding<Context> context;
        private final MerchantTapAndPayModule module;

        public GetCardEmulationProvidesAdapter(MerchantTapAndPayModule merchantTapAndPayModule) {
            super("android.nfc.cardemulation.CardEmulation", false, "com.google.commerce.tapandpay.merchantapp.application.MerchantTapAndPayModule", "getCardEmulation");
            this.module = merchantTapAndPayModule;
            setLibrary(false);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", MerchantTapAndPayModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public CardEmulation get() {
            return MerchantTapAndPayModule.getCardEmulation((Context) this.context.get());
        }
    }

    /* compiled from: MerchantTapAndPayModule$$ModuleAdapter */
    public static final class GetCurrentTimeMillisProvidesAdapter extends ProvidesBinding<Long> implements Provider<Long> {
        private final MerchantTapAndPayModule module;

        public GetCurrentTimeMillisProvidesAdapter(MerchantTapAndPayModule merchantTapAndPayModule) {
            super("@com.google.commerce.tapandpay.merchantapp.application.QualifierAnnotations$CurrentTimeMillis()/java.lang.Long", false, "com.google.commerce.tapandpay.merchantapp.application.MerchantTapAndPayModule", "getCurrentTimeMillis");
            this.module = merchantTapAndPayModule;
            setLibrary(false);
        }

        public Long get() {
            return MerchantTapAndPayModule.getCurrentTimeMillis();
        }
    }

    /* compiled from: MerchantTapAndPayModule$$ModuleAdapter */
    public static final class GetDbHelperProvidesAdapter extends ProvidesBinding<MerchantAppDbHelper> implements Provider<MerchantAppDbHelper> {
        private Binding<Context> context;
        private final MerchantTapAndPayModule module;

        public GetDbHelperProvidesAdapter(MerchantTapAndPayModule merchantTapAndPayModule) {
            super("com.google.commerce.tapandpay.merchantapp.sqlite.MerchantAppDbHelper", true, "com.google.commerce.tapandpay.merchantapp.application.MerchantTapAndPayModule", "getDbHelper");
            this.module = merchantTapAndPayModule;
            setLibrary(false);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", MerchantTapAndPayModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public MerchantAppDbHelper get() {
            return MerchantTapAndPayModule.getDbHelper((Context) this.context.get());
        }
    }

    /* compiled from: MerchantTapAndPayModule$$ModuleAdapter */
    public static final class GetPayPassMagStripeProvidesAdapter extends ProvidesBinding<PayPassMagStripe> implements Provider<PayPassMagStripe> {
        private final MerchantTapAndPayModule module;

        public GetPayPassMagStripeProvidesAdapter(MerchantTapAndPayModule merchantTapAndPayModule) {
            super("com.google.commerce.tapandpay.merchantapp.paypass.PayPassMagStripe", false, "com.google.commerce.tapandpay.merchantapp.application.MerchantTapAndPayModule", "getPayPassMagStripe");
            this.module = merchantTapAndPayModule;
            setLibrary(false);
        }

        public PayPassMagStripe get() {
            return MerchantTapAndPayModule.getPayPassMagStripe();
        }
    }

    /* compiled from: MerchantTapAndPayModule$$ModuleAdapter */
    public static final class GetPayPassProcessorProvidesAdapter extends ProvidesBinding<PayPassProcessor> implements Provider<PayPassProcessor> {
        private final MerchantTapAndPayModule module;
        private Binding<PayPassMagStripe> payPassMagStripe;

        public GetPayPassProcessorProvidesAdapter(MerchantTapAndPayModule merchantTapAndPayModule) {
            super("com.google.commerce.tapandpay.merchantapp.hce.PayPassProcessor", false, "com.google.commerce.tapandpay.merchantapp.application.MerchantTapAndPayModule", "getPayPassProcessor");
            this.module = merchantTapAndPayModule;
            setLibrary(false);
        }

        public void attach(Linker linker) {
            this.payPassMagStripe = linker.requestBinding("com.google.commerce.tapandpay.merchantapp.paypass.PayPassMagStripe", MerchantTapAndPayModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.payPassMagStripe);
        }

        public PayPassProcessor get() {
            return MerchantTapAndPayModule.getPayPassProcessor((PayPassMagStripe) this.payPassMagStripe.get());
        }
    }

    /* compiled from: MerchantTapAndPayModule$$ModuleAdapter */
    public static final class GetServiceObjectConverterProvidesAdapter extends ProvidesBinding<ServiceObjectConverter> implements Provider<ServiceObjectConverter> {
        private final MerchantTapAndPayModule module;

        public GetServiceObjectConverterProvidesAdapter(MerchantTapAndPayModule merchantTapAndPayModule) {
            super("com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter", false, "com.google.commerce.tapandpay.merchantapp.application.MerchantTapAndPayModule", "getServiceObjectConverter");
            this.module = merchantTapAndPayModule;
            setLibrary(false);
        }

        public ServiceObjectConverter get() {
            return MerchantTapAndPayModule.getServiceObjectConverter();
        }
    }

    /* compiled from: MerchantTapAndPayModule$$ModuleAdapter */
    public static final class GetSmartTapV1ProcessorProvidesAdapter extends ProvidesBinding<SmartTapV1Processor> implements Provider<SmartTapV1Processor> {
        private final MerchantTapAndPayModule module;
        private Binding<SmartTapApplet> smartTapApplet;

        public GetSmartTapV1ProcessorProvidesAdapter(MerchantTapAndPayModule merchantTapAndPayModule) {
            super("com.google.commerce.tapandpay.merchantapp.hce.SmartTapV1Processor", false, "com.google.commerce.tapandpay.merchantapp.application.MerchantTapAndPayModule", "getSmartTapV1Processor");
            this.module = merchantTapAndPayModule;
            setLibrary(false);
        }

        public void attach(Linker linker) {
            this.smartTapApplet = linker.requestBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapApplet", MerchantTapAndPayModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.smartTapApplet);
        }

        public SmartTapV1Processor get() {
            return MerchantTapAndPayModule.getSmartTapV1Processor((SmartTapApplet) this.smartTapApplet.get());
        }
    }

    /* compiled from: MerchantTapAndPayModule$$ModuleAdapter */
    public static final class GetSmartTapV2ProcessorProvidesAdapter extends ProvidesBinding<SmartTapV2Processor> implements Provider<SmartTapV2Processor> {
        private final MerchantTapAndPayModule module;
        private Binding<com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapApplet> smartTapApplet;

        public GetSmartTapV2ProcessorProvidesAdapter(MerchantTapAndPayModule merchantTapAndPayModule) {
            super("com.google.commerce.tapandpay.merchantapp.hce.SmartTapV2Processor", false, "com.google.commerce.tapandpay.merchantapp.application.MerchantTapAndPayModule", "getSmartTapV2Processor");
            this.module = merchantTapAndPayModule;
            setLibrary(false);
        }

        public void attach(Linker linker) {
            this.smartTapApplet = linker.requestBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapApplet", MerchantTapAndPayModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.smartTapApplet);
        }

        public SmartTapV2Processor get() {
            return MerchantTapAndPayModule.getSmartTapV2Processor((com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapApplet) this.smartTapApplet.get());
        }
    }

    public MerchantTapAndPayModule$$ModuleAdapter() {
        super(MerchantTapAndPayModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, true, false);
    }

    public MerchantTapAndPayModule newModule() {
        return new MerchantTapAndPayModule();
    }

    public void getBindings(BindingsGroup bindingsGroup, MerchantTapAndPayModule merchantTapAndPayModule) {
        bindingsGroup.contributeProvidesBinding("com.google.commerce.tapandpay.merchantapp.sqlite.MerchantAppDbHelper", new GetDbHelperProvidesAdapter(merchantTapAndPayModule));
        bindingsGroup.contributeProvidesBinding("com.google.commerce.tapandpay.merchantapp.paypass.PayPassMagStripe", new GetPayPassMagStripeProvidesAdapter(merchantTapAndPayModule));
        bindingsGroup.contributeProvidesBinding("@com.google.commerce.tapandpay.merchantapp.application.QualifierAnnotations$CurrentTimeMillis()/java.lang.Long", new GetCurrentTimeMillisProvidesAdapter(merchantTapAndPayModule));
        bindingsGroup.contributeProvidesBinding("android.nfc.cardemulation.CardEmulation", new GetCardEmulationProvidesAdapter(merchantTapAndPayModule));
        bindingsGroup.contributeProvidesBinding("com.google.commerce.tapandpay.merchantapp.hce.SmartTapV2Processor", new GetSmartTapV2ProcessorProvidesAdapter(merchantTapAndPayModule));
        bindingsGroup.contributeProvidesBinding("com.google.commerce.tapandpay.merchantapp.hce.SmartTapV1Processor", new GetSmartTapV1ProcessorProvidesAdapter(merchantTapAndPayModule));
        bindingsGroup.contributeProvidesBinding("com.google.commerce.tapandpay.merchantapp.hce.PayPassProcessor", new GetPayPassProcessorProvidesAdapter(merchantTapAndPayModule));
        bindingsGroup.contributeProvidesBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter", new GetServiceObjectConverterProvidesAdapter(merchantTapAndPayModule));
    }
}
