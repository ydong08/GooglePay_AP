package com.google.commerce.tapandpay.merchantapp.testcase;

import com.google.gson.TypeAdapterFactory;

public abstract class TestCaseTypeAdapterFactory implements TypeAdapterFactory {
    public static TypeAdapterFactory create() {
        return new AutoValueGson_TestCaseTypeAdapterFactory();
    }
}
