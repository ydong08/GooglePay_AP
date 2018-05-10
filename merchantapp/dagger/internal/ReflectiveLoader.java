package dagger.internal;

import dagger.internal.loaders.ReflectiveAtInjectBinding;
import dagger.internal.loaders.ReflectiveModuleAdapter;

@Deprecated
public final class ReflectiveLoader extends Loader {
    public <T> ModuleAdapter<T> getModuleAdapter(Class<T> cls) {
        return ReflectiveModuleAdapter.create(cls);
    }

    public Binding<?> getAtInjectBinding(String str, String str2, ClassLoader classLoader, boolean z) {
        Class loadClass = loadClass(classLoader, str2);
        if (loadClass.equals(Void.class)) {
            throw new IllegalStateException(String.format("Could not load class %s needed for binding %s", new Object[]{str2, str}));
        } else if (loadClass.isInterface()) {
            return null;
        } else {
            return ReflectiveAtInjectBinding.createFactory(loadClass).create(z);
        }
    }
}
