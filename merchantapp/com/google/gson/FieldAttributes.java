package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.lang.reflect.Field;

public final class FieldAttributes {
    private final Field field;

    public FieldAttributes(Field field) {
        C$Gson$Preconditions.checkNotNull(field);
        this.field = field;
    }
}
