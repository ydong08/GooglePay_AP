package dagger.internal;

public abstract class Loader {
    private final Memoizer<ClassLoader, Memoizer<String, Class<?>>> caches = new Memoizer<ClassLoader, Memoizer<String, Class<?>>>(this) {
        protected Memoizer<String, Class<?>> create(final ClassLoader classLoader) {
            return new Memoizer<String, Class<?>>(this) {
                protected Class<?> create(String str) {
                    try {
                        return classLoader.loadClass(str);
                    } catch (ClassNotFoundException e) {
                        return Void.class;
                    }
                }
            };
        }
    };

    public abstract Binding<?> getAtInjectBinding(String str, String str2, ClassLoader classLoader, boolean z);

    public abstract <T> ModuleAdapter<T> getModuleAdapter(Class<T> cls);

    protected Class<?> loadClass(ClassLoader classLoader, String str) {
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        return (Class) ((Memoizer) this.caches.get(classLoader)).get(str);
    }

    protected <T> T instantiate(String str, ClassLoader classLoader) {
        Throwable th;
        String str2;
        String valueOf;
        try {
            Class loadClass = loadClass(classLoader, str);
            if (loadClass == Void.class) {
                return null;
            }
            return loadClass.newInstance();
        } catch (Throwable e) {
            th = e;
            str2 = "Failed to initialize ";
            valueOf = String.valueOf(str);
            throw new RuntimeException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), th);
        } catch (Throwable e2) {
            th = e2;
            str2 = "Failed to initialize ";
            valueOf = String.valueOf(str);
            throw new RuntimeException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), th);
        }
    }
}
