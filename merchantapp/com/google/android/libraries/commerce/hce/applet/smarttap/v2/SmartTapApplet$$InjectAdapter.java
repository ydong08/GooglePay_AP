package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import com.google.android.libraries.commerce.hce.crypto.SmartTap2Encryptor;
import com.google.android.libraries.commerce.hce.util.Compressor;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.security.SecureRandom;
import java.util.Set;
import javax.inject.Provider;

public final class SmartTapApplet$$InjectAdapter extends Binding<SmartTapApplet> implements Provider<SmartTapApplet> {
    private Binding<Compressor> compressor;
    private Binding<SmartTap2Encryptor> encryptor;
    private Binding<GetSmartTapDataCommand> getSmartTapDataCommand;
    private Binding<NegotiateSmartTapCommand> negotiateSmartTapCommand;
    private Binding<PushSmartTapDataCommand> pushSmartTapDataCommand;
    private Binding<SecureRandom> random;
    private Binding<SmartTapCallback> smartTapCallback;

    public SmartTapApplet$$InjectAdapter() {
        super("com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapApplet", "members/com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapApplet", false, SmartTapApplet.class);
    }

    public void attach(Linker linker) {
        this.smartTapCallback = linker.requestBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapCallback", SmartTapApplet.class, getClass().getClassLoader());
        this.negotiateSmartTapCommand = linker.requestBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v2.NegotiateSmartTapCommand", SmartTapApplet.class, getClass().getClassLoader());
        this.getSmartTapDataCommand = linker.requestBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetSmartTapDataCommand", SmartTapApplet.class, getClass().getClassLoader());
        this.pushSmartTapDataCommand = linker.requestBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v2.PushSmartTapDataCommand", SmartTapApplet.class, getClass().getClassLoader());
        this.random = linker.requestBinding("java.security.SecureRandom", SmartTapApplet.class, getClass().getClassLoader());
        this.encryptor = linker.requestBinding("com.google.android.libraries.commerce.hce.crypto.SmartTap2Encryptor", SmartTapApplet.class, getClass().getClassLoader());
        this.compressor = linker.requestBinding("com.google.android.libraries.commerce.hce.util.Compressor", SmartTapApplet.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set.add(this.smartTapCallback);
        set.add(this.negotiateSmartTapCommand);
        set.add(this.getSmartTapDataCommand);
        set.add(this.pushSmartTapDataCommand);
        set.add(this.random);
        set.add(this.encryptor);
        set.add(this.compressor);
    }

    public SmartTapApplet get() {
        return new SmartTapApplet((SmartTapCallback) this.smartTapCallback.get(), (NegotiateSmartTapCommand) this.negotiateSmartTapCommand.get(), (GetSmartTapDataCommand) this.getSmartTapDataCommand.get(), (PushSmartTapDataCommand) this.pushSmartTapDataCommand.get(), (SecureRandom) this.random.get(), (SmartTap2Encryptor) this.encryptor.get(), (Compressor) this.compressor.get());
    }
}
