package com.google.commerce.tapandpay.merchantapp.testcaseview;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import com.google.android.libraries.commerce.hce.util.Hex;

public class Validator {
    private Validator() {
    }

    public static boolean hex(Context context, TextInputLayout textInputLayout) {
        return hex(context, textInputLayout, -1, -1);
    }

    public static boolean hex(Context context, TextInputLayout textInputLayout, int i) {
        return hex(context, textInputLayout, i, i);
    }

    public static boolean hex(Context context, TextInputLayout textInputLayout, int i, int i2) {
        String obj = textInputLayout.getEditText().getText().toString();
        int i3 = i * 2;
        int i4 = i2 * 2;
        if (obj == null || (i3 > 0 && obj.length() < i3)) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(context.getString(R.string.too_short_error, new Object[]{Integer.valueOf(i3)}));
            return false;
        } else if (i4 <= 0 || obj.length() <= i4) {
            try {
                Hex.decode(obj);
                textInputLayout.setErrorEnabled(false);
                textInputLayout.setError(null);
                return true;
            } catch (IllegalArgumentException e) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(context.getString(R.string.hex_error));
                return false;
            }
        } else {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(context.getString(R.string.too_long_error, new Object[]{Integer.valueOf(i4)}));
            return false;
        }
    }
}
