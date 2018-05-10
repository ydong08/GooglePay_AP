package com.google.android.libraries.commerce.hce.primitives;

import com.google.common.base.Preconditions;
import java.io.Serializable;

public abstract class AbstractNamed implements Serializable {
    private static final long serialVersionUID = 1;
    protected final String name;

    protected AbstractNamed(String str) {
        this.name = (String) Preconditions.checkNotNull(str);
    }

    public String toString() {
        return this.name;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public final boolean equals(Object obj) {
        return this == obj;
    }
}
