package dagger.internal.loaders;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.Keys;
import dagger.internal.Linker;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import dagger.internal.SetBinding;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import javax.inject.Provider;
import javax.inject.Singleton;

@Deprecated
public class ReflectiveModuleAdapter<M> extends ModuleAdapter<M> {

    static final class ReflectiveProvidesBinding<T> extends ProvidesBinding<T> {
        private final Object instance;
        private final Method method;
        private Binding<?>[] parameters;

        public ReflectiveProvidesBinding(Method method, String str, String str2, Object obj, boolean z) {
            super(str, method.isAnnotationPresent(Singleton.class), str2, method.getName());
            this.method = method;
            this.instance = obj;
            method.setAccessible(true);
            setLibrary(z);
        }

        public void attach(Linker linker) {
            Type[] genericParameterTypes = this.method.getGenericParameterTypes();
            Annotation[][] parameterAnnotations = this.method.getParameterAnnotations();
            this.parameters = new Binding[genericParameterTypes.length];
            for (int i = 0; i < this.parameters.length; i++) {
                Type type = genericParameterTypes[i];
                Annotation[] annotationArr = parameterAnnotations[i];
                String valueOf = String.valueOf(this.method);
                this.parameters[i] = linker.requestBinding(Keys.get(type, annotationArr, new StringBuilder(String.valueOf(valueOf).length() + 22).append(valueOf).append(" parameter ").append(i).toString()), this.method, this.instance.getClass().getClassLoader());
            }
        }

        public T get() {
            Throwable cause;
            Object[] objArr = new Object[this.parameters.length];
            for (int i = 0; i < this.parameters.length; i++) {
                objArr[i] = this.parameters[i].get();
            }
            try {
                return this.method.invoke(this.instance, objArr);
            } catch (InvocationTargetException e) {
                RuntimeException runtimeException;
                cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    runtimeException = (RuntimeException) cause;
                } else {
                    runtimeException = new RuntimeException(cause);
                }
                throw runtimeException;
            } catch (Throwable cause2) {
                throw new RuntimeException(cause2);
            }
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            for (Object add : this.parameters) {
                set.add(add);
            }
        }

        public void injectMembers(T t) {
            throw new AssertionError("Provides method bindings are not MembersInjectors");
        }
    }

    public ReflectiveModuleAdapter(Class<M> cls, Module module) {
        super(cls, injectableTypesToKeys(module.injects()), module.staticInjections(), module.overrides(), module.includes(), module.complete(), module.library());
    }

    private static String[] injectableTypesToKeys(Class<?>[] clsArr) {
        String[] strArr = new String[clsArr.length];
        for (int i = 0; i < clsArr.length; i++) {
            String str;
            Object obj = clsArr[i];
            if (obj.isInterface()) {
                str = Keys.get(obj);
            } else {
                str = Keys.getMembersKey(obj);
            }
            strArr[i] = str;
        }
        return strArr;
    }

    public void getBindings(BindingsGroup bindingsGroup, M m) {
        for (Class cls = this.moduleClass; !cls.equals(Object.class); cls = cls.getSuperclass()) {
            for (Method method : cls.getDeclaredMethods()) {
                Provides provides = (Provides) method.getAnnotation(Provides.class);
                if (provides != null) {
                    Object rawType;
                    Type genericReturnType = method.getGenericReturnType();
                    if (genericReturnType instanceof ParameterizedType) {
                        rawType = ((ParameterizedType) genericReturnType).getRawType();
                    } else {
                        Type type = genericReturnType;
                    }
                    String valueOf;
                    String valueOf2;
                    if (Provider.class.equals(rawType)) {
                        valueOf = String.valueOf(cls.getName());
                        valueOf2 = String.valueOf(method.getName());
                        throw new IllegalStateException(new StringBuilder((String.valueOf(valueOf).length() + 53) + String.valueOf(valueOf2).length()).append("@Provides method must not return Provider directly: ").append(valueOf).append(".").append(valueOf2).toString());
                    } else if (Lazy.class.equals(rawType)) {
                        valueOf = String.valueOf(cls.getName());
                        valueOf2 = String.valueOf(method.getName());
                        throw new IllegalStateException(new StringBuilder((String.valueOf(valueOf).length() + 49) + String.valueOf(valueOf2).length()).append("@Provides method must not return Lazy directly: ").append(valueOf).append(".").append(valueOf2).toString());
                    } else {
                        String str = Keys.get(genericReturnType, method.getAnnotations(), method);
                        switch (provides.type()) {
                            case UNIQUE:
                                handleBindings(bindingsGroup, m, method, str, this.library);
                                break;
                            case SET:
                                handleSetBindings(bindingsGroup, m, method, Keys.getSetKey(method.getGenericReturnType(), method.getAnnotations(), method), str, this.library);
                                break;
                            case SET_VALUES:
                                handleSetBindings(bindingsGroup, m, method, str, str, this.library);
                                break;
                            default:
                                String valueOf3 = String.valueOf(provides.type());
                                throw new AssertionError(new StringBuilder(String.valueOf(valueOf3).length() + 23).append("Unknown @Provides type ").append(valueOf3).toString());
                        }
                    }
                }
            }
        }
    }

    private void handleBindings(BindingsGroup bindingsGroup, M m, Method method, String str, boolean z) {
        bindingsGroup.contributeProvidesBinding(str, new ReflectiveProvidesBinding(method, str, this.moduleClass.getName(), m, z));
    }

    private void handleSetBindings(BindingsGroup bindingsGroup, M m, Method method, String str, String str2, boolean z) {
        SetBinding.add(bindingsGroup, str, new ReflectiveProvidesBinding(method, str2, this.moduleClass.getName(), m, z));
    }

    public M newModule() {
        try {
            Constructor declaredConstructor = this.moduleClass.getDeclaredConstructor(new Class[0]);
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance(new Object[0]);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e.getCause());
        } catch (Throwable e2) {
            String valueOf = String.valueOf(this.moduleClass.getName());
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(valueOf).length() + 165).append("Could not construct ").append(valueOf).append(" as it lacks an accessible no-args constructor. This module must be passed in as an instance, or an accessible no-args constructor must be added.").toString(), e2);
        } catch (Throwable e22) {
            Throwable th = e22;
            String str = "Failed to construct ";
            String valueOf2 = String.valueOf(this.moduleClass.getName());
            throw new IllegalArgumentException(valueOf2.length() != 0 ? str.concat(valueOf2) : new String(str), th);
        } catch (IllegalAccessException e3) {
            throw new AssertionError();
        }
    }

    public String toString() {
        String valueOf = String.valueOf(this.moduleClass.getName());
        return new StringBuilder(String.valueOf(valueOf).length() + 25).append("ReflectiveModuleAdapter[").append(valueOf).append("]").toString();
    }

    public static <M> ModuleAdapter<M> create(Class<M> cls) {
        Module module = (Module) cls.getAnnotation(Module.class);
        String str;
        String valueOf;
        if (module == null) {
            str = "No @Module on ";
            valueOf = String.valueOf(cls.getName());
            throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        } else if (cls.getSuperclass().equals(Object.class)) {
            return new ReflectiveModuleAdapter(cls, module);
        } else {
            str = "Modules must not extend from other classes: ";
            valueOf = String.valueOf(cls.getName());
            throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        }
    }
}
