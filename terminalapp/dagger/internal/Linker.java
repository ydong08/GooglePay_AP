package dagger.internal;

import dagger.internal.Binding.InvalidBindingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

public final class Linker {
    private static final Object UNINITIALIZED = new Object();
    private boolean attachSuccess = true;
    private final Linker base;
    private final Map<String, Binding<?>> bindings = new HashMap();
    private final ErrorHandler errorHandler;
    private final List<String> errors = new ArrayList();
    private volatile Map<String, Binding<?>> linkedBindings = null;
    private final Loader plugin;
    private final Queue<Binding<?>> toLink = new ArrayQueue();

    static class DeferredBinding extends Binding<Object> {
        final ClassLoader classLoader;
        final String deferredKey;
        final boolean mustHaveInjections;

        private DeferredBinding(String str, ClassLoader classLoader, Object obj, boolean z) {
            super(null, null, false, obj);
            this.deferredKey = str;
            this.classLoader = classLoader;
            this.mustHaveInjections = z;
        }

        public void injectMembers(Object obj) {
            throw new UnsupportedOperationException("Deferred bindings must resolve first.");
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            throw new UnsupportedOperationException("Deferred bindings must resolve first.");
        }

        public String toString() {
            String str = this.deferredKey;
            return new StringBuilder(String.valueOf(str).length() + 29).append("DeferredBinding[deferredKey=").append(str).append("]").toString();
        }
    }

    public interface ErrorHandler {
        public static final ErrorHandler NULL = new ErrorHandler() {
            public void handleErrors(List<String> list) {
            }
        };

        void handleErrors(List<String> list);
    }

    static class SingletonBinding<T> extends Binding<T> {
        private final Binding<T> binding;
        private volatile Object onlyInstance;

        private SingletonBinding(Binding<T> binding) {
            super(binding.provideKey, binding.membersKey, true, binding.requiredBy);
            this.onlyInstance = Linker.UNINITIALIZED;
            this.binding = binding;
        }

        public void attach(Linker linker) {
            this.binding.attach(linker);
        }

        public void injectMembers(T t) {
            this.binding.injectMembers(t);
        }

        public T get() {
            if (this.onlyInstance == Linker.UNINITIALIZED) {
                synchronized (this) {
                    if (this.onlyInstance == Linker.UNINITIALIZED) {
                        this.onlyInstance = this.binding.get();
                    }
                }
            }
            return this.onlyInstance;
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            this.binding.getDependencies(set, set2);
        }

        public boolean isCycleFree() {
            return this.binding.isCycleFree();
        }

        public boolean isLinked() {
            return this.binding.isLinked();
        }

        public boolean isVisiting() {
            return this.binding.isVisiting();
        }

        public boolean library() {
            return this.binding.library();
        }

        public boolean dependedOn() {
            return this.binding.dependedOn();
        }

        public void setCycleFree(boolean z) {
            this.binding.setCycleFree(z);
        }

        public void setVisiting(boolean z) {
            this.binding.setVisiting(z);
        }

        public void setLibrary(boolean z) {
            this.binding.setLibrary(true);
        }

        public void setDependedOn(boolean z) {
            this.binding.setDependedOn(z);
        }

        protected boolean isSingleton() {
            return true;
        }

        protected void setLinked() {
            this.binding.setLinked();
        }

        public String toString() {
            String str = "@Singleton/";
            String valueOf = String.valueOf(this.binding.toString());
            return valueOf.length() != 0 ? str.concat(valueOf) : new String(str);
        }
    }

    public Linker(Linker linker, Loader loader, ErrorHandler errorHandler) {
        if (loader == null) {
            throw new NullPointerException("plugin");
        } else if (errorHandler == null) {
            throw new NullPointerException("errorHandler");
        } else {
            this.base = linker;
            this.plugin = loader;
            this.errorHandler = errorHandler;
        }
    }

    public void installBindings(BindingsGroup bindingsGroup) {
        if (this.linkedBindings != null) {
            throw new IllegalStateException("Cannot install further bindings after calling linkAll().");
        }
        for (Entry entry : bindingsGroup.entrySet()) {
            this.bindings.put(entry.getKey(), scope((Binding) entry.getValue()));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void linkRequested() {
        /*
        r6 = this;
        r6.assertLockHeld();
    L_0x0003:
        r0 = r6.toLink;
        r0 = r0.poll();
        r0 = (dagger.internal.Binding) r0;
        if (r0 == 0) goto L_0x0179;
    L_0x000d:
        r1 = r0 instanceof dagger.internal.Linker.DeferredBinding;
        if (r1 == 0) goto L_0x0163;
    L_0x0011:
        r1 = r0;
        r1 = (dagger.internal.Linker.DeferredBinding) r1;
        r2 = r1.deferredKey;
        r3 = r1.mustHaveInjections;
        r4 = r6.bindings;
        r4 = r4.containsKey(r2);
        if (r4 != 0) goto L_0x0003;
    L_0x0020:
        r4 = r0.requiredBy;	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r1 = r1.classLoader;	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r1 = r6.createBinding(r2, r4, r1, r3);	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r3 = r0.library();	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r1.setLibrary(r3);	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r3 = r0.dependedOn();	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r1.setDependedOn(r3);	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r3 = r1.provideKey;	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r3 = r2.equals(r3);	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        if (r3 != 0) goto L_0x0107;
    L_0x003e:
        r3 = r1.membersKey;	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r3 = r2.equals(r3);	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        if (r3 != 0) goto L_0x0107;
    L_0x0046:
        r3 = new java.lang.IllegalStateException;	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r4 = "Unable to create binding for ";
        r1 = java.lang.String.valueOf(r2);	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r5 = r1.length();	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        if (r5 == 0) goto L_0x00b6;
    L_0x0054:
        r1 = r4.concat(r1);	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
    L_0x0058:
        r3.<init>(r1);	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        throw r3;	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
    L_0x005c:
        r1 = move-exception;
        r3 = r1.type;
        r1 = r1.getMessage();
        r1 = java.lang.String.valueOf(r1);
        r0 = r0.requiredBy;
        r0 = java.lang.String.valueOf(r0);
        r4 = java.lang.String.valueOf(r3);
        r4 = r4.length();
        r4 = r4 + 14;
        r5 = java.lang.String.valueOf(r1);
        r5 = r5.length();
        r4 = r4 + r5;
        r5 = java.lang.String.valueOf(r0);
        r5 = r5.length();
        r4 = r4 + r5;
        r5 = new java.lang.StringBuilder;
        r5.<init>(r4);
        r3 = r5.append(r3);
        r4 = " ";
        r3 = r3.append(r4);
        r1 = r3.append(r1);
        r3 = " required by ";
        r1 = r1.append(r3);
        r0 = r1.append(r0);
        r0 = r0.toString();
        r6.addError(r0);
        r0 = r6.bindings;
        r1 = dagger.internal.Binding.UNRESOLVED;
        r0.put(r2, r1);
        goto L_0x0003;
    L_0x00b6:
        r1 = new java.lang.String;	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r1.<init>(r4);	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        goto L_0x0058;
    L_0x00bc:
        r1 = move-exception;
        r1 = r1.getMessage();
        r1 = java.lang.String.valueOf(r1);
        r0 = r0.requiredBy;
        r0 = java.lang.String.valueOf(r0);
        r3 = java.lang.String.valueOf(r1);
        r3 = r3.length();
        r3 = r3 + 26;
        r4 = java.lang.String.valueOf(r0);
        r4 = r4.length();
        r3 = r3 + r4;
        r4 = new java.lang.StringBuilder;
        r4.<init>(r3);
        r3 = "Unsupported: ";
        r3 = r4.append(r3);
        r1 = r3.append(r1);
        r3 = " required by ";
        r1 = r1.append(r3);
        r0 = r1.append(r0);
        r0 = r0.toString();
        r6.addError(r0);
        r0 = r6.bindings;
        r1 = dagger.internal.Binding.UNRESOLVED;
        r0.put(r2, r1);
        goto L_0x0003;
    L_0x0107:
        r1 = scope(r1);	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r3 = r6.toLink;	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r3.add(r1);	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        r6.putBinding(r1);	 Catch:{ InvalidBindingException -> 0x005c, UnsupportedOperationException -> 0x00bc, IllegalArgumentException -> 0x0115, RuntimeException -> 0x015a, Exception -> 0x015c }
        goto L_0x0003;
    L_0x0115:
        r1 = move-exception;
        r1 = r1.getMessage();
        r1 = java.lang.String.valueOf(r1);
        r0 = r0.requiredBy;
        r0 = java.lang.String.valueOf(r0);
        r3 = java.lang.String.valueOf(r1);
        r3 = r3.length();
        r3 = r3 + 13;
        r4 = java.lang.String.valueOf(r0);
        r4 = r4.length();
        r3 = r3 + r4;
        r4 = new java.lang.StringBuilder;
        r4.<init>(r3);
        r1 = r4.append(r1);
        r3 = " required by ";
        r1 = r1.append(r3);
        r0 = r1.append(r0);
        r0 = r0.toString();
        r6.addError(r0);
        r0 = r6.bindings;
        r1 = dagger.internal.Binding.UNRESOLVED;
        r0.put(r2, r1);
        goto L_0x0003;
    L_0x015a:
        r0 = move-exception;
        throw r0;
    L_0x015c:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r1.<init>(r0);
        throw r1;
    L_0x0163:
        r1 = 1;
        r6.attachSuccess = r1;
        r0.attach(r6);
        r1 = r6.attachSuccess;
        if (r1 == 0) goto L_0x0172;
    L_0x016d:
        r0.setLinked();
        goto L_0x0003;
    L_0x0172:
        r1 = r6.toLink;
        r1.add(r0);
        goto L_0x0003;
    L_0x0179:
        r0 = r6.errorHandler;	 Catch:{ all -> 0x0186 }
        r1 = r6.errors;	 Catch:{ all -> 0x0186 }
        r0.handleErrors(r1);	 Catch:{ all -> 0x0186 }
        r0 = r6.errors;
        r0.clear();
        return;
    L_0x0186:
        r0 = move-exception;
        r1 = r6.errors;
        r1.clear();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: dagger.internal.Linker.linkRequested():void");
    }

    private void assertLockHeld() {
        if (!Thread.holdsLock(this)) {
            throw new AssertionError();
        }
    }

    private Binding<?> createBinding(String str, Object obj, ClassLoader classLoader, boolean z) {
        String builtInBindingsKey = Keys.getBuiltInBindingsKey(str);
        if (builtInBindingsKey != null) {
            return new BuiltInBinding(str, obj, classLoader, builtInBindingsKey);
        }
        builtInBindingsKey = Keys.getLazyKey(str);
        if (builtInBindingsKey != null) {
            return new LazyBinding(str, obj, classLoader, builtInBindingsKey);
        }
        builtInBindingsKey = Keys.getClassName(str);
        if (builtInBindingsKey == null) {
            throw new InvalidBindingException(str, "is a generic class or an array and can only be bound with concrete type parameter(s) in a @Provides method.");
        } else if (Keys.isAnnotated(str)) {
            throw new InvalidBindingException(str, "is a @Qualifier-annotated type and must be bound by a @Provides method.");
        } else {
            Binding<?> atInjectBinding = this.plugin.getAtInjectBinding(str, builtInBindingsKey, classLoader, z);
            if (atInjectBinding != null) {
                return atInjectBinding;
            }
            String str2 = "could not be bound with key ";
            String valueOf = String.valueOf(str);
            throw new InvalidBindingException(builtInBindingsKey, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        }
    }

    public Binding<?> requestBinding(String str, Object obj, ClassLoader classLoader) {
        return requestBinding(str, obj, classLoader, true, true);
    }

    public Binding<?> requestBinding(String str, Object obj, ClassLoader classLoader, boolean z, boolean z2) {
        assertLockHeld();
        Binding<?> binding = null;
        for (Linker linker = this; linker != null; linker = linker.base) {
            binding = (Binding) linker.bindings.get(str);
            if (binding != null) {
                if (!(linker == this || binding.isLinked())) {
                    throw new AssertionError();
                }
                if (binding != null) {
                    Binding deferredBinding = new DeferredBinding(str, classLoader, obj, z);
                    deferredBinding.setLibrary(z2);
                    deferredBinding.setDependedOn(true);
                    this.toLink.add(deferredBinding);
                    this.attachSuccess = false;
                    return null;
                }
                if (!binding.isLinked()) {
                    this.toLink.add(binding);
                }
                binding.setLibrary(z2);
                binding.setDependedOn(true);
                return binding;
            }
        }
        if (binding != null) {
            if (binding.isLinked()) {
                this.toLink.add(binding);
            }
            binding.setLibrary(z2);
            binding.setDependedOn(true);
            return binding;
        }
        Binding deferredBinding2 = new DeferredBinding(str, classLoader, obj, z);
        deferredBinding2.setLibrary(z2);
        deferredBinding2.setDependedOn(true);
        this.toLink.add(deferredBinding2);
        this.attachSuccess = false;
        return null;
    }

    private <T> void putBinding(Binding<T> binding) {
        if (binding.provideKey != null) {
            putIfAbsent(this.bindings, binding.provideKey, binding);
        }
        if (binding.membersKey != null) {
            putIfAbsent(this.bindings, binding.membersKey, binding);
        }
    }

    static <T> Binding<T> scope(Binding<T> binding) {
        return (!binding.isSingleton() || (binding instanceof SingletonBinding)) ? binding : new SingletonBinding(binding);
    }

    private <K, V> void putIfAbsent(Map<K, V> map, K k, V v) {
        Object put = map.put(k, v);
        if (put != null) {
            map.put(k, put);
        }
    }

    private void addError(String str) {
        this.errors.add(str);
    }
}
