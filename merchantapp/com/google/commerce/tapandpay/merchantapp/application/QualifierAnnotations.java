package com.google.commerce.tapandpay.merchantapp.application;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;

public class QualifierAnnotations {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CurrentTimeMillis {
    }
}
