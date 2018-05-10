package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class NegotiateSmartTapCommand$$InjectAdapter extends Binding<NegotiateSmartTapCommand> implements Provider<NegotiateSmartTapCommand> {
    private Binding<SmartTapCallback> smartTapCallback;
    private Binding<SmartTap2MerchantVerifier> verifier;

    public NegotiateSmartTapCommand$$InjectAdapter() {
        super("com.google.android.libraries.commerce.hce.applet.smarttap.v2.NegotiateSmartTapCommand", "members/com.google.android.libraries.commerce.hce.applet.smarttap.v2.NegotiateSmartTapCommand", false, NegotiateSmartTapCommand.class);
    }

    public void attach(Linker linker) {
        this.smartTapCallback = linker.requestBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapCallback", NegotiateSmartTapCommand.class, getClass().getClassLoader());
        this.verifier = linker.requestBinding("com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantVerifier", NegotiateSmartTapCommand.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set.add(this.smartTapCallback);
        set.add(this.verifier);
    }

    public NegotiateSmartTapCommand get() {
        return new NegotiateSmartTapCommand((SmartTapCallback) this.smartTapCallback.get(), (SmartTap2MerchantVerifier) this.verifier.get());
    }
}
