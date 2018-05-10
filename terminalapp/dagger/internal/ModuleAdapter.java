package dagger.internal;

public abstract class ModuleAdapter<T> {
    public final boolean complete;
    public final Class<?>[] includes;
    public final String[] injectableTypes;
    public final boolean library;
    public final Class<T> moduleClass;
    public final boolean overrides;
    public final Class<?>[] staticInjections;

    protected ModuleAdapter(Class<T> cls, String[] strArr, Class<?>[] clsArr, boolean z, Class<?>[] clsArr2, boolean z2, boolean z3) {
        this.moduleClass = cls;
        this.injectableTypes = strArr;
        this.staticInjections = clsArr;
        this.overrides = z;
        this.includes = clsArr2;
        this.complete = z2;
        this.library = z3;
    }

    public void getBindings(BindingsGroup bindingsGroup, T t) {
    }

    protected T newModule() {
        String str = "No no-args constructor on ";
        String valueOf = String.valueOf(getClass().getName());
        throw new UnsupportedOperationException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ModuleAdapter)) {
            return false;
        }
        return this.moduleClass.equals(((ModuleAdapter) obj).moduleClass);
    }

    public final int hashCode() {
        return this.moduleClass.hashCode();
    }
}
