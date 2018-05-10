package com.google.common.reflect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public abstract class TypeToken<T> extends TypeCapture<T> implements Serializable {
    private final Type runtimeType;

    class AnonymousClass1 extends Invokable {
        final /* synthetic */ TypeToken this$0;

        public TypeToken<T> getOwnerType() {
            return this.this$0;
        }

        public String toString() {
            String valueOf = String.valueOf(getOwnerType());
            String valueOf2 = String.valueOf(super.toString());
            return new StringBuilder((String.valueOf(valueOf).length() + 1) + String.valueOf(valueOf2).length()).append(valueOf).append(".").append(valueOf2).toString();
        }
    }

    static final class SimpleTypeToken<T> extends TypeToken<T> {
        private static final long serialVersionUID = 0;

        SimpleTypeToken(Type type) {
            super(type);
        }
    }

    protected TypeToken() {
        this.runtimeType = capture();
        Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
    }

    private TypeToken(Type type) {
        this.runtimeType = (Type) Preconditions.checkNotNull(type);
    }

    public static <T> TypeToken<T> of(Class<T> cls) {
        return new SimpleTypeToken(cls);
    }

    public static TypeToken<?> of(Type type) {
        return new SimpleTypeToken(type);
    }

    public final Type getType() {
        return this.runtimeType;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof TypeToken)) {
            return false;
        }
        return this.runtimeType.equals(((TypeToken) obj).runtimeType);
    }

    public int hashCode() {
        return this.runtimeType.hashCode();
    }

    public String toString() {
        return Types.toString(this.runtimeType);
    }

    protected Object writeReplace() {
        return of(new TypeResolver().resolveType(this.runtimeType));
    }
}
