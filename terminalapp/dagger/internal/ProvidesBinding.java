package dagger.internal;

public abstract class ProvidesBinding<T> extends Binding<T> {
    protected final String methodName;
    protected final String moduleClass;

    public abstract T get();

    public ProvidesBinding(String str, boolean z, String str2, String str3) {
        super(str, null, z, new StringBuilder((str2.length() + str3.length()) + 3).append(str2).append('.').append(str3).append("()").toString());
        this.moduleClass = str2;
        this.methodName = str3;
    }

    public String toString() {
        String valueOf = String.valueOf(getClass().getName());
        String str = this.provideKey;
        String str2 = this.moduleClass;
        String str3 = this.methodName;
        return new StringBuilder((((String.valueOf(valueOf).length() + 17) + String.valueOf(str).length()) + String.valueOf(str2).length()) + String.valueOf(str3).length()).append(valueOf).append("[key=").append(str).append(" method=").append(str2).append(".").append(str3).append("()]").toString();
    }
}
