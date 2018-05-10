package com.google.commerce.tapandpay.terminalapp.application;

import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter;
import dagger.internal.BindingsGroup;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import javax.inject.Provider;

public final class TerminalTapAndPayModule$$ModuleAdapter extends ModuleAdapter<TerminalTapAndPayModule> {
    private static final Class<?>[] INCLUDES = new Class[0];
    private static final String[] INJECTS = new String[]{"members/com.google.commerce.tapandpay.terminalapp.main.MessageAdapter", "members/com.google.android.libraries.commerce.hce.terminal.smarttap.Version2"};
    private static final Class<?>[] STATIC_INJECTIONS = new Class[0];

    /* compiled from: TerminalTapAndPayModule$$ModuleAdapter */
    public static final class GetServiceObjectConverterProvidesAdapter extends ProvidesBinding<ServiceObjectConverter> implements Provider<ServiceObjectConverter> {
        private final TerminalTapAndPayModule module;

        public GetServiceObjectConverterProvidesAdapter(TerminalTapAndPayModule terminalTapAndPayModule) {
            super("com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter", false, "com.google.commerce.tapandpay.terminalapp.application.TerminalTapAndPayModule", "getServiceObjectConverter");
            this.module = terminalTapAndPayModule;
            setLibrary(false);
        }

        public ServiceObjectConverter get() {
            return TerminalTapAndPayModule.getServiceObjectConverter();
        }
    }

    public TerminalTapAndPayModule$$ModuleAdapter() {
        super(TerminalTapAndPayModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, true, false);
    }

    public TerminalTapAndPayModule newModule() {
        return new TerminalTapAndPayModule();
    }

    public void getBindings(BindingsGroup bindingsGroup, TerminalTapAndPayModule terminalTapAndPayModule) {
        bindingsGroup.contributeProvidesBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter", new GetServiceObjectConverterProvidesAdapter(terminalTapAndPayModule));
    }
}
