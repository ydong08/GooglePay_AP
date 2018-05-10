package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class SmartTapApplet$$InjectAdapter extends Binding<SmartTapApplet> implements Provider<SmartTapApplet> {
    private Binding<Provider<SmartTapCommand>> getSmartTapDataCommandProvider;
    private Binding<Provider<SmartTapCommand>> postTransactionDataCommandProvider;

    public SmartTapApplet$$InjectAdapter() {
        super("com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapApplet", "members/com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapApplet", false, SmartTapApplet.class);
    }

    public void attach(Linker linker) {
        this.getSmartTapDataCommandProvider = linker.requestBinding("@com.google.android.libraries.commerce.hce.applet.smarttap.v1.QualifierAnnotations$GetSmartTapData()/javax.inject.Provider<com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCommand>", SmartTapApplet.class, getClass().getClassLoader());
        this.postTransactionDataCommandProvider = linker.requestBinding("@com.google.android.libraries.commerce.hce.applet.smarttap.v1.QualifierAnnotations$PostTransactionData()/javax.inject.Provider<com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCommand>", SmartTapApplet.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set.add(this.getSmartTapDataCommandProvider);
        set.add(this.postTransactionDataCommandProvider);
    }

    public SmartTapApplet get() {
        return new SmartTapApplet((Provider) this.getSmartTapDataCommandProvider.get(), (Provider) this.postTransactionDataCommandProvider.get());
    }
}
