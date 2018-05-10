package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class PushSmartTapDataCommand$$InjectAdapter extends Binding<PushSmartTapDataCommand> implements Provider<PushSmartTapDataCommand> {
    private Binding<SmartTapCallback> smartTapCallback;

    public PushSmartTapDataCommand$$InjectAdapter() {
        super("com.google.android.libraries.commerce.hce.applet.smarttap.v2.PushSmartTapDataCommand", "members/com.google.android.libraries.commerce.hce.applet.smarttap.v2.PushSmartTapDataCommand", false, PushSmartTapDataCommand.class);
    }

    public void attach(Linker linker) {
        this.smartTapCallback = linker.requestBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapCallback", PushSmartTapDataCommand.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set.add(this.smartTapCallback);
    }

    public PushSmartTapDataCommand get() {
        return new PushSmartTapDataCommand((SmartTapCallback) this.smartTapCallback.get());
    }
}
