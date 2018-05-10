package dagger.internal.loaders;

import dagger.internal.Binding;
import dagger.internal.Binding.InvalidBindingException;
import dagger.internal.Keys;
import dagger.internal.Linker;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

public final class ReflectiveAtInjectBinding<T> extends Binding<T> {
    private final Constructor<T> constructor;
    private final Binding<?>[] fieldBindings;
    private final Field[] fields;
    private final String[] keys;
    private final ClassLoader loader;
    private final Binding<?>[] parameterBindings;
    private final Class<?> supertype;
    private Binding<? super T> supertypeBinding;

    public static class Factory<T> {
        private final Constructor<T> constructor;
        private final Field[] fields;
        private final String[] keys;
        private final String membersKey;
        private final int parameterCount;
        private final String provideKey;
        private final boolean singleton;
        private final Class<?> supertype;
        private final Class<?> type;

        private Factory(String str, String str2, boolean z, Class<?> cls, Field[] fieldArr, Constructor<T> constructor, int i, Class<?> cls2, String[] strArr) {
            this.provideKey = str;
            this.membersKey = str2;
            this.singleton = z;
            this.type = cls;
            this.fields = fieldArr;
            this.constructor = constructor;
            this.parameterCount = i;
            this.supertype = cls2;
            this.keys = strArr;
        }

        public ReflectiveAtInjectBinding<T> create(boolean z) {
            if (!z || this.constructor != null || this.fields.length != 0) {
                return new ReflectiveAtInjectBinding(this.provideKey, this.membersKey, this.singleton, this.type, this.fields, this.constructor, this.parameterCount, this.supertype, this.keys);
            }
            throw new InvalidBindingException(this.type.getName(), "has no injectable members. Do you want to add an injectable constructor?");
        }
    }

    private ReflectiveAtInjectBinding(String str, String str2, boolean z, Class<?> cls, Field[] fieldArr, Constructor<T> constructor, int i, Class<?> cls2, String[] strArr) {
        super(str, str2, z, cls);
        this.constructor = constructor;
        this.fields = fieldArr;
        this.supertype = cls2;
        this.keys = strArr;
        this.parameterBindings = new Binding[i];
        this.fieldBindings = new Binding[fieldArr.length];
        this.loader = cls.getClassLoader();
    }

    public void attach(Linker linker) {
        int i;
        int i2 = 0;
        for (i = 0; i < this.fields.length; i++) {
            if (this.fieldBindings[i] == null) {
                this.fieldBindings[i] = linker.requestBinding(this.keys[i2], this.fields[i], this.loader);
            }
            i2++;
        }
        if (this.constructor != null) {
            for (i = 0; i < this.parameterBindings.length; i++) {
                if (this.parameterBindings[i] == null) {
                    this.parameterBindings[i] = linker.requestBinding(this.keys[i2], this.constructor, this.loader);
                }
                i2++;
            }
        }
        if (this.supertype != null && this.supertypeBinding == null) {
            this.supertypeBinding = linker.requestBinding(this.keys[i2], this.membersKey, this.loader, false, true);
        }
    }

    public T get() {
        Throwable cause;
        if (this.constructor == null) {
            throw new UnsupportedOperationException();
        }
        Object[] objArr = new Object[this.parameterBindings.length];
        for (int i = 0; i < this.parameterBindings.length; i++) {
            objArr[i] = this.parameterBindings[i].get();
        }
        try {
            T newInstance = this.constructor.newInstance(objArr);
            injectMembers(newInstance);
            return newInstance;
        } catch (InvocationTargetException e) {
            RuntimeException runtimeException;
            cause = e.getCause();
            if (cause instanceof RuntimeException) {
                runtimeException = (RuntimeException) cause;
            } else {
                runtimeException = new RuntimeException(cause);
            }
            throw runtimeException;
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (Throwable cause2) {
            throw new RuntimeException(cause2);
        }
    }

    public void injectMembers(T t) {
        int i = 0;
        while (i < this.fields.length) {
            try {
                this.fields[i].set(t, this.fieldBindings[i].get());
                i++;
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
        if (this.supertypeBinding != null) {
            this.supertypeBinding.injectMembers(t);
        }
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        if (this.parameterBindings != null) {
            Collections.addAll(set, this.parameterBindings);
        }
        Collections.addAll(set2, this.fieldBindings);
        if (this.supertypeBinding != null) {
            set2.add(this.supertypeBinding);
        }
    }

    public String toString() {
        return this.provideKey != null ? this.provideKey : this.membersKey;
    }

    public static <T> Factory<T> createFactory(Class<T> cls) {
        String valueOf;
        int length;
        Constructor declaredConstructor;
        Type[] genericParameterTypes;
        Annotation[][] parameterAnnotations;
        int i;
        Class superclass;
        boolean isAnnotationPresent = cls.isAnnotationPresent(Singleton.class);
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        for (Class cls2 = cls; cls2 != Object.class; cls2 = cls2.getSuperclass()) {
            for (Field field : cls2.getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class) && !Modifier.isStatic(field.getModifiers())) {
                    if ((field.getModifiers() & 2) != 0) {
                        valueOf = String.valueOf(field);
                        throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 28).append("Can't inject private field: ").append(valueOf).toString());
                    }
                    field.setAccessible(true);
                    arrayList2.add(field);
                    arrayList.add(Keys.get(field.getGenericType(), field.getAnnotations(), field));
                }
            }
        }
        AnonymousClass1 anonymousClass1 = null;
        for (Constructor constructor : getConstructorsForType(cls)) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                if (anonymousClass1 != null) {
                    throw new InvalidBindingException(cls.getName(), "has too many injectable constructors");
                }
                Object obj = constructor;
            }
        }
        if (anonymousClass1 == null && !arrayList2.isEmpty()) {
            try {
                declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
            } catch (NoSuchMethodException e) {
            }
            if (declaredConstructor == null) {
                if ((declaredConstructor.getModifiers() & 2) == 0) {
                    valueOf = String.valueOf(declaredConstructor);
                    throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 34).append("Can't inject private constructor: ").append(valueOf).toString());
                }
                valueOf = Keys.get(cls);
                declaredConstructor.setAccessible(true);
                genericParameterTypes = declaredConstructor.getGenericParameterTypes();
                length = genericParameterTypes.length;
                if (length != 0) {
                    parameterAnnotations = declaredConstructor.getParameterAnnotations();
                    for (i = 0; i < genericParameterTypes.length; i++) {
                        arrayList.add(Keys.get(genericParameterTypes[i], parameterAnnotations[i], declaredConstructor));
                    }
                }
            } else if (isAnnotationPresent) {
                valueOf = null;
                length = 0;
            } else {
                String str = "No injectable constructor on @Singleton ";
                String valueOf2 = String.valueOf(cls.getName());
                throw new IllegalArgumentException(valueOf2.length() == 0 ? str.concat(valueOf2) : new String(str));
            }
            superclass = cls.getSuperclass();
            if (superclass != null) {
                if (Keys.isPlatformType(superclass.getName())) {
                    arrayList.add(Keys.getMembersKey(superclass));
                } else {
                    superclass = null;
                }
            }
            return new Factory(valueOf, Keys.getMembersKey(cls), isAnnotationPresent, cls, (Field[]) arrayList2.toArray(new Field[arrayList2.size()]), declaredConstructor, length, superclass, (String[]) arrayList.toArray(new String[arrayList.size()]));
        }
        Object obj2 = anonymousClass1;
        if (declaredConstructor == null) {
            if (isAnnotationPresent) {
                valueOf = null;
                length = 0;
            } else {
                String str2 = "No injectable constructor on @Singleton ";
                String valueOf22 = String.valueOf(cls.getName());
                if (valueOf22.length() == 0) {
                }
                throw new IllegalArgumentException(valueOf22.length() == 0 ? str2.concat(valueOf22) : new String(str2));
            }
        } else if ((declaredConstructor.getModifiers() & 2) == 0) {
            valueOf = Keys.get(cls);
            declaredConstructor.setAccessible(true);
            genericParameterTypes = declaredConstructor.getGenericParameterTypes();
            length = genericParameterTypes.length;
            if (length != 0) {
                parameterAnnotations = declaredConstructor.getParameterAnnotations();
                for (i = 0; i < genericParameterTypes.length; i++) {
                    arrayList.add(Keys.get(genericParameterTypes[i], parameterAnnotations[i], declaredConstructor));
                }
            }
        } else {
            valueOf = String.valueOf(declaredConstructor);
            throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 34).append("Can't inject private constructor: ").append(valueOf).toString());
        }
        superclass = cls.getSuperclass();
        if (superclass != null) {
            if (Keys.isPlatformType(superclass.getName())) {
                arrayList.add(Keys.getMembersKey(superclass));
            } else {
                superclass = null;
            }
        }
        return new Factory(valueOf, Keys.getMembersKey(cls), isAnnotationPresent, cls, (Field[]) arrayList2.toArray(new Field[arrayList2.size()]), declaredConstructor, length, superclass, (String[]) arrayList.toArray(new String[arrayList.size()]));
    }

    private static <T> Constructor<T>[] getConstructorsForType(Class<T> cls) {
        return cls.getDeclaredConstructors();
    }
}
