package com.google.commerce.tapandpay.merchantapp.paypass;

import com.google.android.libraries.commerce.hce.util.Hex;

public class PayPassConstants {
    public static final byte[] EXPECTED_UDOL = Hex.decode("9F6A 049F7E 019F02 065F2A 029F1A 02".replace(" ", ""));

    private PayPassConstants() {
    }
}
