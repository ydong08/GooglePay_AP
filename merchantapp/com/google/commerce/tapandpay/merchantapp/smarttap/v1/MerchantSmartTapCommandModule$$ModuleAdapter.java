package com.google.commerce.tapandpay.merchantapp.smarttap.v1;

import com.google.android.libraries.commerce.hce.applet.smarttap.v1.GetSmartTapDataCommand;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.PostTransactionDataCommand;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCommand;
import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.Linker;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import java.util.Set;
import javax.inject.Provider;

public final class MerchantSmartTapCommandModule$$ModuleAdapter extends ModuleAdapter<MerchantSmartTapCommandModule> {
    private static final Class<?>[] INCLUDES = new Class[0];
    private static final String[] INJECTS = new String[0];
    private static final Class<?>[] STATIC_INJECTIONS = new Class[0];

    /* compiled from: MerchantSmartTapCommandModule$$ModuleAdapter */
    public static final class ProvideGetSmartTapDataCommandProvidesAdapter extends ProvidesBinding<SmartTapCommand> implements Provider<SmartTapCommand> {
        private Binding<GetSmartTapDataCommand> command;
        private final MerchantSmartTapCommandModule module;

        public ProvideGetSmartTapDataCommandProvidesAdapter(MerchantSmartTapCommandModule merchantSmartTapCommandModule) {
            super("@com.google.android.libraries.commerce.hce.applet.smarttap.v1.QualifierAnnotations$GetSmartTapData()/com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCommand", false, "com.google.commerce.tapandpay.merchantapp.smarttap.v1.MerchantSmartTapCommandModule", "provideGetSmartTapDataCommand");
            this.module = merchantSmartTapCommandModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.command = linker.requestBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v1.GetSmartTapDataCommand", MerchantSmartTapCommandModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.command);
        }

        public SmartTapCommand get() {
            return this.module.provideGetSmartTapDataCommand((GetSmartTapDataCommand) this.command.get());
        }
    }

    /* compiled from: MerchantSmartTapCommandModule$$ModuleAdapter */
    public static final class ProvidePostTransactionDataCommandProvidesAdapter extends ProvidesBinding<SmartTapCommand> implements Provider<SmartTapCommand> {
        private Binding<PostTransactionDataCommand> command;
        private final MerchantSmartTapCommandModule module;

        public ProvidePostTransactionDataCommandProvidesAdapter(MerchantSmartTapCommandModule merchantSmartTapCommandModule) {
            super("@com.google.android.libraries.commerce.hce.applet.smarttap.v1.QualifierAnnotations$PostTransactionData()/com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCommand", false, "com.google.commerce.tapandpay.merchantapp.smarttap.v1.MerchantSmartTapCommandModule", "providePostTransactionDataCommand");
            this.module = merchantSmartTapCommandModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.command = linker.requestBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v1.PostTransactionDataCommand", MerchantSmartTapCommandModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.command);
        }

        public SmartTapCommand get() {
            return this.module.providePostTransactionDataCommand((PostTransactionDataCommand) this.command.get());
        }
    }

    public MerchantSmartTapCommandModule$$ModuleAdapter() {
        super(MerchantSmartTapCommandModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, false, true);
    }

    public MerchantSmartTapCommandModule newModule() {
        return new MerchantSmartTapCommandModule();
    }

    public void getBindings(BindingsGroup bindingsGroup, MerchantSmartTapCommandModule merchantSmartTapCommandModule) {
        bindingsGroup.contributeProvidesBinding("@com.google.android.libraries.commerce.hce.applet.smarttap.v1.QualifierAnnotations$GetSmartTapData()/com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCommand", new ProvideGetSmartTapDataCommandProvidesAdapter(merchantSmartTapCommandModule));
        bindingsGroup.contributeProvidesBinding("@com.google.android.libraries.commerce.hce.applet.smarttap.v1.QualifierAnnotations$PostTransactionData()/com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCommand", new ProvidePostTransactionDataCommandProvidesAdapter(merchantSmartTapCommandModule));
    }
}
