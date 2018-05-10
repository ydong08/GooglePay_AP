package com.google.android.libraries.commerce.hce.common;

import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;

public class ResponseApdus {
    private ResponseApdus() {
    }

    public static ResponseApdu get(Code code, short s) {
        return ResponseApdu.fromStatusWord(StatusWords.get(code, s));
    }
}
