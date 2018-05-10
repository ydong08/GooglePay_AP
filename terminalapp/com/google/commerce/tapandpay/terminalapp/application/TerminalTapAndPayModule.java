package com.google.commerce.tapandpay.terminalapp.application;

import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter;
import com.google.android.libraries.commerce.hce.terminal.smarttap.TerminalServiceObjectConverter;
import com.google.android.libraries.commerce.hce.terminal.smarttap.Version2;
import com.google.commerce.tapandpay.terminalapp.main.MessageAdapter;
import dagger.Module;
import dagger.Provides;

@Module(complete = true, injects = {MessageAdapter.class, Version2.class}, library = false)
public class TerminalTapAndPayModule {
    @Provides
    public static ServiceObjectConverter getServiceObjectConverter() {
        return new TerminalServiceObjectConverter();
    }
}
