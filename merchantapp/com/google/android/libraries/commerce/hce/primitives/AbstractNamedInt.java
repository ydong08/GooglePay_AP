package com.google.android.libraries.commerce.hce.primitives;

public abstract class AbstractNamedInt extends AbstractNamed {
    private static final long serialVersionUID = 1;
    protected final int intValue;

    protected AbstractNamedInt(String str, int i) {
        super(str);
        this.intValue = i;
    }

    public int intValue() {
        return this.intValue;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Integer.valueOf(this.intValue).hashCode();
    }

    public String toString() {
        return String.format("%s '%X'", new Object[]{this.name, Integer.valueOf(this.intValue)});
    }
}
