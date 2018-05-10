package dagger.internal;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class BindingsGroup {
    private final Map<String, Binding<?>> bindings = new LinkedHashMap();

    public abstract Binding<?> contributeSetBinding(String str, SetBinding<?> setBinding);

    public Binding<?> contributeProvidesBinding(String str, ProvidesBinding<?> providesBinding) {
        return put(str, providesBinding);
    }

    protected Binding<?> put(String str, Binding<?> binding) {
        Binding binding2 = (Binding) this.bindings.put(str, binding);
        if (binding2 == null) {
            return null;
        }
        this.bindings.put(str, binding2);
        String valueOf = String.valueOf(binding2);
        String valueOf2 = String.valueOf(binding);
        throw new IllegalArgumentException(new StringBuilder((String.valueOf(valueOf).length() + 20) + String.valueOf(valueOf2).length()).append("Duplicate:\n    ").append(valueOf).append("\n    ").append(valueOf2).toString());
    }

    public Binding<?> get(String str) {
        return (Binding) this.bindings.get(str);
    }

    public final Set<Entry<String, Binding<?>>> entrySet() {
        return this.bindings.entrySet();
    }

    public String toString() {
        String valueOf = String.valueOf(getClass().getSimpleName());
        String valueOf2 = String.valueOf(this.bindings.toString());
        return valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
    }
}
