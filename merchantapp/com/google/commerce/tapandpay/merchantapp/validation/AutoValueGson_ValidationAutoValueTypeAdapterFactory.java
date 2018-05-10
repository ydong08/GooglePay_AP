package com.google.commerce.tapandpay.merchantapp.validation;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

public final class AutoValueGson_ValidationAutoValueTypeAdapterFactory extends ValidationAutoValueTypeAdapterFactory {
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class rawType = typeToken.getRawType();
        if (Apdu.class.isAssignableFrom(rawType)) {
            return Apdu.typeAdapter(gson);
        }
        if (Record.class.isAssignableFrom(rawType)) {
            return Record.typeAdapter(gson);
        }
        if (Schema.class.isAssignableFrom(rawType)) {
            return Schema.typeAdapter(gson);
        }
        if (Session.class.isAssignableFrom(rawType)) {
            return Session.typeAdapter(gson);
        }
        if (ValidationResults.class.isAssignableFrom(rawType)) {
            return ValidationResults.typeAdapter(gson);
        }
        return null;
    }
}
