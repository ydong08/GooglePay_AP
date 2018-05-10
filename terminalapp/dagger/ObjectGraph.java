package dagger;

import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.FailoverLoader;
import dagger.internal.Keys;
import dagger.internal.Linker;
import dagger.internal.Loader;
import dagger.internal.ModuleAdapter;
import dagger.internal.Modules;
import dagger.internal.Modules.ModuleWithAdapter;
import dagger.internal.SetBinding;
import dagger.internal.StaticInjection;
import dagger.internal.ThrowingErrorHandler;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class ObjectGraph {

    static final class ComponentObjectGraph extends ObjectGraph {
        private final Object component;
        private final MethodIndex methodIndex;

        static final class MethodIndex {
            static final Map<Class<?>, WeakReference<MethodIndex>> methodIndexes = Collections.synchronizedMap(new WeakHashMap());
            final Map<Class<?>, Method> membersInjectionMethodIndex;
            final Map<Class<?>, Method> provisionMethodIndex;
        }

        public <T> T inject(T t) {
            if (t == null) {
                throw new NullPointerException();
            }
            Method method = (Method) this.methodIndex.membersInjectionMethodIndex.get(t.getClass());
            if (method == null) {
                throw new IllegalArgumentException(String.format("No injection methods for %s were found on %s; only %s", new Object[]{t.getClass().getName(), this.component.getClass().getName(), this.methodIndex.membersInjectionMethodIndex.keySet()}));
            }
            try {
                method.invoke(this.component, new Object[]{t});
                return t;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } catch (Throwable e2) {
                throw new RuntimeException(InvocationTargetException.class.getCanonicalName(), e2);
            }
        }

        public String toString() {
            StringBuilder append = new StringBuilder("ComponentObjectGraph(").append(this.component.getClass().getCanonicalName()).append("){provisionMethods=[");
            Iterator it = this.methodIndex.provisionMethodIndex.values().iterator();
            if (it.hasNext()) {
                appendProvisionMethod(append, (Method) it.next());
            }
            while (it.hasNext()) {
                append.append(", ");
                appendProvisionMethod(append, (Method) it.next());
            }
            append.append("], membersInjectionMethods=[");
            it = this.methodIndex.membersInjectionMethodIndex.values().iterator();
            if (it.hasNext()) {
                appendMembersInjectionMethod(append, (Method) it.next());
            }
            while (it.hasNext()) {
                append.append(", ");
                appendMembersInjectionMethod(append, (Method) it.next());
            }
            return append.append("]}").toString();
        }

        private StringBuilder appendProvisionMethod(StringBuilder stringBuilder, Method method) {
            return stringBuilder.append(method.getName()).append("->").append(method.getReturnType().getCanonicalName());
        }

        private StringBuilder appendMembersInjectionMethod(StringBuilder stringBuilder, Method method) {
            return stringBuilder.append(method.getParameterTypes()[0].getCanonicalName()).append("->").append(method.getName());
        }
    }

    static class DaggerObjectGraph extends ObjectGraph {
        private final DaggerObjectGraph base;
        private final Map<String, Class<?>> injectableTypes;
        private final Linker linker;
        private final Loader plugin;
        private final List<SetBinding<?>> setBindings;
        private final Map<Class<?>, StaticInjection> staticInjections;

        DaggerObjectGraph(DaggerObjectGraph daggerObjectGraph, Linker linker, Loader loader, Map<Class<?>, StaticInjection> map, Map<String, Class<?>> map2, List<SetBinding<?>> list) {
            this.base = daggerObjectGraph;
            this.linker = (Linker) checkNotNull(linker, "linker");
            this.plugin = (Loader) checkNotNull(loader, "plugin");
            this.staticInjections = (Map) checkNotNull(map, "staticInjections");
            this.injectableTypes = (Map) checkNotNull(map2, "injectableTypes");
            this.setBindings = (List) checkNotNull(list, "setBindings");
        }

        private static <T> T checkNotNull(T t, String str) {
            if (t != null) {
                return t;
            }
            throw new NullPointerException(str);
        }

        private static ObjectGraph makeGraph(DaggerObjectGraph daggerObjectGraph, Loader loader, Object... objArr) {
            BindingsGroup standardBindings;
            Map linkedHashMap = new LinkedHashMap();
            Map linkedHashMap2 = new LinkedHashMap();
            if (daggerObjectGraph == null) {
                standardBindings = new StandardBindings();
            } else {
                standardBindings = new StandardBindings(daggerObjectGraph.setBindings);
            }
            BindingsGroup overridesBindings = new OverridesBindings();
            ArrayList loadModules = Modules.loadModules(loader, objArr);
            int size = loadModules.size();
            int i = 0;
            while (i < size) {
                ModuleWithAdapter moduleWithAdapter = (ModuleWithAdapter) loadModules.get(i);
                ModuleAdapter moduleAdapter = moduleWithAdapter.getModuleAdapter();
                for (Object put : moduleAdapter.injectableTypes) {
                    linkedHashMap.put(put, moduleAdapter.moduleClass);
                }
                for (Object put2 : moduleAdapter.staticInjections) {
                    linkedHashMap2.put(put2, null);
                }
                try {
                    moduleAdapter.getBindings(moduleAdapter.overrides ? overridesBindings : standardBindings, moduleWithAdapter.getModule());
                    i++;
                } catch (Throwable e) {
                    String valueOf = String.valueOf(moduleAdapter.moduleClass.getSimpleName());
                    String valueOf2 = String.valueOf(e.getMessage());
                    throw new IllegalArgumentException(new StringBuilder((String.valueOf(valueOf).length() + 2) + String.valueOf(valueOf2).length()).append(valueOf).append(": ").append(valueOf2).toString(), e);
                }
            }
            Linker linker = new Linker(daggerObjectGraph != null ? daggerObjectGraph.linker : null, loader, new ThrowingErrorHandler());
            linker.installBindings(standardBindings);
            linker.installBindings(overridesBindings);
            return new DaggerObjectGraph(daggerObjectGraph, linker, loader, linkedHashMap2, linkedHashMap, standardBindings.setBindings);
        }

        public <T> T inject(T t) {
            String membersKey = Keys.getMembersKey(t.getClass());
            getInjectableTypeBinding(t.getClass().getClassLoader(), membersKey, membersKey).injectMembers(t);
            return t;
        }

        private Binding<?> getInjectableTypeBinding(ClassLoader classLoader, String str, String str2) {
            Class moduleClassDeclaringInjects = getModuleClassDeclaringInjects(str);
            if (moduleClassDeclaringInjects == null) {
                throw new IllegalArgumentException(new StringBuilder(String.valueOf(str).length() + 101).append("No inject registered for ").append(str).append(". You must explicitly add it to the 'injects' option in one of your modules.").toString());
            }
            Binding<?> requestBinding;
            synchronized (this.linker) {
                requestBinding = this.linker.requestBinding(str2, moduleClassDeclaringInjects, classLoader, false, true);
                if (requestBinding == null || !requestBinding.isLinked()) {
                    this.linker.linkRequested();
                    requestBinding = this.linker.requestBinding(str2, moduleClassDeclaringInjects, classLoader, false, true);
                }
            }
            return requestBinding;
        }

        private Class<?> getModuleClassDeclaringInjects(String str) {
            Class<?> cls = null;
            while (this != null) {
                cls = (Class) this.injectableTypes.get(str);
                if (cls != null) {
                    break;
                }
                this = this.base;
            }
            return cls;
        }
    }

    static final class OverridesBindings extends BindingsGroup {
        OverridesBindings() {
        }

        public Binding<?> contributeSetBinding(String str, SetBinding<?> setBinding) {
            throw new IllegalArgumentException("Module overrides cannot contribute set bindings.");
        }
    }

    static final class StandardBindings extends BindingsGroup {
        private final List<SetBinding<?>> setBindings;

        public StandardBindings() {
            this.setBindings = new ArrayList();
        }

        public StandardBindings(List<SetBinding<?>> list) {
            this.setBindings = new ArrayList(list.size());
            for (SetBinding setBinding : list) {
                Binding setBinding2 = new SetBinding(setBinding);
                this.setBindings.add(setBinding2);
                put(setBinding2.provideKey, setBinding2);
            }
        }

        public Binding<?> contributeSetBinding(String str, SetBinding<?> setBinding) {
            this.setBindings.add(setBinding);
            return super.put(str, setBinding);
        }
    }

    public abstract <T> T inject(T t);

    ObjectGraph() {
    }

    public static ObjectGraph create(Object... objArr) {
        return DaggerObjectGraph.makeGraph(null, new FailoverLoader(), objArr);
    }
}
