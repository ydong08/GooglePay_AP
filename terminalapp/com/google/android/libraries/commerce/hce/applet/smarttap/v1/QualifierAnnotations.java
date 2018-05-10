package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;

public final class QualifierAnnotations {

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface GetSmartTapData {
    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PostTransactionData {
    }

    private QualifierAnnotations() {
    }
}
