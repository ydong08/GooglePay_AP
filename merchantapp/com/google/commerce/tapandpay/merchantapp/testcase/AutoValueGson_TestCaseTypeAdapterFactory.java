package com.google.commerce.tapandpay.merchantapp.testcase;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

public final class AutoValueGson_TestCaseTypeAdapterFactory extends TestCaseTypeAdapterFactory {
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (TestCase.class.isAssignableFrom(typeToken.getRawType())) {
            return TestCase.typeAdapter(gson);
        }
        return null;
    }
}
