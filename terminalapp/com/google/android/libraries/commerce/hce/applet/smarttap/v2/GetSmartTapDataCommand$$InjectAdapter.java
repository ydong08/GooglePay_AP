package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class GetSmartTapDataCommand$$InjectAdapter extends Binding<GetSmartTapDataCommand> implements Provider<GetSmartTapDataCommand> {
    private Binding<SmartTapCallback> smartTapCallback;

    public GetSmartTapDataCommand$$InjectAdapter() {
        super("com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetSmartTapDataCommand", "members/com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetSmartTapDataCommand", false, GetSmartTapDataCommand.class);
    }

    public void attach(Linker linker) {
        this.smartTapCallback = linker.requestBinding("com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapCallback", GetSmartTapDataCommand.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set.add(this.smartTapCallback);
    }

    public GetSmartTapDataCommand get() {
        return new GetSmartTapDataCommand((SmartTapCallback) this.smartTapCallback.get());
    }
}
