package com.google.commerce.tapandpay.merchantapp.validation;

import com.google.gson.TypeAdapterFactory;

public abstract class ValidationAutoValueTypeAdapterFactory implements TypeAdapterFactory {
    public static TypeAdapterFactory create() {
        return new AutoValueGson_ValidationAutoValueTypeAdapterFactory();
    }
}
