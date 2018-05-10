package com.google.common.reflect;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

public class Invokable<T, R> extends Element implements GenericDeclaration {
    final Method method;

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public final Class<? super T> getDeclaringClass() {
        return super.getDeclaringClass();
    }

    public TypeToken<T> getOwnerType() {
        return TypeToken.of(getDeclaringClass());
    }

    public TypeVariable<?>[] getTypeParameters() {
        return this.method.getTypeParameters();
    }
}
