package com.google.android.libraries.commerce.hce.common;

import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;

public class StatusWords {
    private StatusWords() {
    }

    public static StatusWord get(Code code, short s) {
        if (s == (short) 0) {
            return Iso7816StatusWord.fromCode(code);
        }
        return new SmartTapStatusWord(code);
    }

    public static StatusWord get(byte[] bArr, short s) {
        if (s == (short) 0) {
            return Iso7816StatusWord.fromBytes(bArr);
        }
        return SmartTapStatusWord.fromBytes(bArr);
    }
}
