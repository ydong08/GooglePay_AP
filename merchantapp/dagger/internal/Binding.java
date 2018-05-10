package dagger.internal;

import dagger.MembersInjector;
import java.util.Set;
import javax.inject.Provider;

public abstract class Binding<T> implements MembersInjector<T>, Provider<T> {
    private static final int CYCLE_FREE = 8;
    private static final int DEPENDED_ON = 16;
    protected static final boolean IS_SINGLETON = true;
    private static final int LIBRARY = 32;
    private static final int LINKED = 2;
    protected static final boolean NOT_SINGLETON = false;
    private static final int SINGLETON = 1;
    public static final Binding<Object> UNRESOLVED = new AnonymousClass1(null, null, NOT_SINGLETON, null);
    private static final int VISITING = 4;
    private int bits;
    public final String membersKey;
    public final String provideKey;
    public final Object requiredBy;

    class AnonymousClass1 extends Binding<Object> {
        AnonymousClass1(String str, String str2, boolean z, Object obj) {
            super(str, str2, z, obj);
        }

        public Object get() {
            throw new AssertionError("Unresolved binding should never be called to inject.");
        }

        public void injectMembers(Object obj) {
            throw new AssertionError("Unresolved binding should never be called to inject.");
        }
    }

    public static class InvalidBindingException extends RuntimeException {
        public final String type;

        public InvalidBindingException(String str, String str2) {
            super(str2);
            this.type = str;
        }
    }

    protected Binding(String str, String str2, boolean z, Object obj) {
        if (z && str == null) {
            throw new InvalidBindingException(Keys.getClassName(str2), "is exclusively members injected and therefore cannot be scoped");
        }
        this.provideKey = str;
        this.membersKey = str2;
        this.requiredBy = obj;
        this.bits = z ? 1 : 0;
    }

    public void attach(Linker linker) {
    }

    public void injectMembers(T t) {
    }

    public T get() {
        String str = "No injectable constructor on ";
        String valueOf = String.valueOf(getClass().getName());
        throw new UnsupportedOperationException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
    }

    void setLinked() {
        this.bits |= 2;
    }

    public boolean isLinked() {
        return (this.bits & 2) != 0 ? IS_SINGLETON : NOT_SINGLETON;
    }

    boolean isSingleton() {
        return (this.bits & 1) != 0 ? IS_SINGLETON : NOT_SINGLETON;
    }

    public boolean isVisiting() {
        return (this.bits & 4) != 0 ? IS_SINGLETON : NOT_SINGLETON;
    }

    public void setVisiting(boolean z) {
        this.bits = z ? this.bits | 4 : this.bits & -5;
    }

    public boolean isCycleFree() {
        return (this.bits & 8) != 0 ? IS_SINGLETON : NOT_SINGLETON;
    }

    public void setCycleFree(boolean z) {
        this.bits = z ? this.bits | 8 : this.bits & -9;
    }

    public void setLibrary(boolean z) {
        this.bits = z ? this.bits | 32 : this.bits & -33;
    }

    public boolean library() {
        return (this.bits & 32) != 0 ? IS_SINGLETON : NOT_SINGLETON;
    }

    public void setDependedOn(boolean z) {
        this.bits = z ? this.bits | 16 : this.bits & -17;
    }

    public boolean dependedOn() {
        return (this.bits & 16) != 0 ? IS_SINGLETON : NOT_SINGLETON;
    }

    public String toString() {
        String valueOf = String.valueOf(getClass().getSimpleName());
        String str = this.provideKey;
        String str2 = this.membersKey;
        return new StringBuilder(((String.valueOf(valueOf).length() + 30) + String.valueOf(str).length()) + String.valueOf(str2).length()).append(valueOf).append("[provideKey=\"").append(str).append("\", memberskey=\"").append(str2).append("\"]").toString();
    }
}
