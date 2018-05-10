package com.google.commerce.tapandpay.merchantapp.smarttap.v2;

import com.google.gson.TypeAdapterFactory;

public abstract class MerchantValuableTypeAdapterFactory implements TypeAdapterFactory {
    public static TypeAdapterFactory create() {
        return new AutoValueGson_MerchantValuableTypeAdapterFactory();
    }
}
