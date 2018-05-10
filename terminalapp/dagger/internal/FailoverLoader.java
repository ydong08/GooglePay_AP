package dagger.internal;

import dagger.internal.loaders.ReflectiveAtInjectBinding;
import dagger.internal.loaders.ReflectiveAtInjectBinding.Factory;
import java.lang.reflect.Constructor;

public final class FailoverLoader extends Loader {
    private final Memoizer<AtInjectBindingKey, AtInjectBindingInfo> atInjectBindings = new Memoizer<AtInjectBindingKey, AtInjectBindingInfo>() {
        protected AtInjectBindingInfo create(AtInjectBindingKey atInjectBindingKey) {
            return FailoverLoader.this.getAtInjectBindingInfo(atInjectBindingKey.classLoader, atInjectBindingKey.className);
        }
    };
    private final Memoizer<Class<?>, ModuleAdapter<?>> loadedAdapters = new Memoizer<Class<?>, ModuleAdapter<?>>() {
        protected ModuleAdapter<?> create(Class<?> cls) {
            ModuleAdapter<?> moduleAdapter = (ModuleAdapter) FailoverLoader.this.instantiate(cls.getName().concat("$$ModuleAdapter"), cls.getClassLoader());
            if (moduleAdapter != null) {
                return moduleAdapter;
            }
            String valueOf = String.valueOf(cls);
            throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 100).append("Module adapter for ").append(valueOf).append(" could not be loaded. Please ensure that code generation was run for this module.").toString());
        }
    };

    static final class AtInjectBindingInfo {
        private final Constructor<Binding<?>> adapterConstructor;
        private final Factory<?> reflectiveBindingFactory;

        AtInjectBindingInfo(Constructor<Binding<?>> constructor, Factory<?> factory) {
            this.adapterConstructor = constructor;
            this.reflectiveBindingFactory = factory;
        }
    }

    static final class AtInjectBindingKey {
        private final ClassLoader classLoader;
        private final String className;

        AtInjectBindingKey(ClassLoader classLoader, String str) {
            this.classLoader = classLoader;
            this.className = str;
        }

        public int hashCode() {
            return this.className.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof AtInjectBindingKey)) {
                return false;
            }
            AtInjectBindingKey atInjectBindingKey = (AtInjectBindingKey) obj;
            if (this.classLoader == atInjectBindingKey.classLoader && this.className.equals(atInjectBindingKey.className)) {
                return true;
            }
            return false;
        }
    }

    public <T> ModuleAdapter<T> getModuleAdapter(Class<T> cls) {
        return (ModuleAdapter) this.loadedAdapters.get(cls);
    }

    public Binding<?> getAtInjectBinding(String str, String str2, ClassLoader classLoader, boolean z) {
        Throwable th;
        String str3;
        String valueOf;
        AtInjectBindingInfo atInjectBindingInfo = (AtInjectBindingInfo) this.atInjectBindings.get(new AtInjectBindingKey(classLoader, str2));
        if (atInjectBindingInfo.adapterConstructor != null) {
            try {
                return (Binding) atInjectBindingInfo.adapterConstructor.newInstance(new Object[0]);
            } catch (Throwable e) {
                th = e;
                str3 = "Could not create an instance of the inject adapter for class ";
                valueOf = String.valueOf(str2);
                throw new IllegalStateException(valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), th);
            } catch (Throwable e2) {
                th = e2;
                str3 = "Could not create an instance of the inject adapter for class ";
                valueOf = String.valueOf(str2);
                throw new IllegalStateException(valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), th);
            } catch (Throwable e22) {
                th = e22;
                str3 = "Could not create an instance of the inject adapter for class ";
                valueOf = String.valueOf(str2);
                throw new IllegalStateException(valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), th);
            } catch (Throwable e222) {
                th = e222;
                str3 = "Could not create an instance of the inject adapter for class ";
                valueOf = String.valueOf(str2);
                throw new IllegalStateException(valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), th);
            }
        } else if (atInjectBindingInfo.reflectiveBindingFactory != null) {
            return atInjectBindingInfo.reflectiveBindingFactory.create(z);
        } else {
            return null;
        }
    }

    private AtInjectBindingInfo getAtInjectBindingInfo(ClassLoader classLoader, String str) {
        Class loadClass = loadClass(classLoader, str.concat("$$InjectAdapter"));
        if (loadClass.equals(Void.class)) {
            loadClass = loadClass(classLoader, str);
            if (loadClass.equals(Void.class)) {
                String str2 = "Could not load class ";
                String valueOf = String.valueOf(str);
                throw new IllegalStateException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            } else if (loadClass.isInterface()) {
                return new AtInjectBindingInfo(null, null);
            } else {
                return new AtInjectBindingInfo(null, ReflectiveAtInjectBinding.createFactory(loadClass));
            }
        }
        try {
            return new AtInjectBindingInfo(loadClass.getConstructor(new Class[0]), null);
        } catch (NoSuchMethodException e) {
            str2 = "Couldn't find default constructor in the generated inject adapter for class ";
            valueOf = String.valueOf(str);
            throw new IllegalStateException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        }
    }
}
