package com.google.commerce.tapandpay.merchantapp.smarttap.v2;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

public final class AutoValueGson_MerchantValuableTypeAdapterFactory extends MerchantValuableTypeAdapterFactory {
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (MerchantValuable.class.isAssignableFrom(typeToken.getRawType())) {
            return MerchantValuable.typeAdapter(gson);
        }
        return null;
    }
}
