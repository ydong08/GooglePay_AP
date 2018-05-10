package dagger.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class SetBinding<T> extends Binding<Set<T>> {
    private final List<Binding<?>> contributors;
    private final SetBinding<T> parent;

    public static <T> void add(BindingsGroup bindingsGroup, String str, Binding<?> binding) {
        prepareSetBinding(bindingsGroup, str, binding).contributors.add(Linker.scope(binding));
    }

    private static <T> SetBinding<T> prepareSetBinding(BindingsGroup bindingsGroup, String str, Binding<?> binding) {
        Binding binding2 = bindingsGroup.get(str);
        if (binding2 instanceof SetBinding) {
            SetBinding<T> setBinding = (SetBinding) binding2;
            boolean z = setBinding.library() && binding.library();
            setBinding.setLibrary(z);
            return setBinding;
        } else if (binding2 != null) {
            String valueOf = String.valueOf(binding2);
            String valueOf2 = String.valueOf(binding);
            throw new IllegalArgumentException(new StringBuilder((String.valueOf(valueOf).length() + 20) + String.valueOf(valueOf2).length()).append("Duplicate:\n    ").append(valueOf).append("\n    ").append(valueOf2).toString());
        } else {
            SetBinding setBinding2 = new SetBinding(str, binding.requiredBy);
            setBinding2.setLibrary(binding.library());
            bindingsGroup.contributeSetBinding(str, setBinding2);
            return (SetBinding) bindingsGroup.get(str);
        }
    }

    public SetBinding(String str, Object obj) {
        super(str, null, false, obj);
        this.parent = null;
        this.contributors = new ArrayList();
    }

    public SetBinding(SetBinding<T> setBinding) {
        super(setBinding.provideKey, null, false, setBinding.requiredBy);
        this.parent = setBinding;
        setLibrary(setBinding.library());
        setDependedOn(setBinding.dependedOn());
        this.contributors = new ArrayList();
    }

    public void attach(Linker linker) {
        for (Binding attach : this.contributors) {
            attach.attach(linker);
        }
    }

    public Set<T> get() {
        Collection arrayList = new ArrayList();
        for (SetBinding setBinding = this; setBinding != null; setBinding = setBinding.parent) {
            int size = setBinding.contributors.size();
            for (int i = 0; i < size; i++) {
                Binding binding = (Binding) setBinding.contributors.get(i);
                Object obj = binding.get();
                if (binding.provideKey.equals(this.provideKey)) {
                    arrayList.addAll((Set) obj);
                } else {
                    arrayList.add(obj);
                }
            }
        }
        return Collections.unmodifiableSet(new LinkedHashSet(arrayList));
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        while (this != null) {
            set.addAll(this.contributors);
            this = this.parent;
        }
    }

    public void injectMembers(Set<T> set) {
        throw new UnsupportedOperationException("Cannot inject members on a contributed Set<T>.");
    }

    public String toString() {
        Object obj = 1;
        StringBuilder stringBuilder = new StringBuilder("SetBinding[");
        while (this != null) {
            int size = this.contributors.size();
            Object obj2 = obj;
            int i = 0;
            while (i < size) {
                if (obj2 == null) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(this.contributors.get(i));
                i++;
                obj2 = null;
            }
            this = this.parent;
            obj = obj2;
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
