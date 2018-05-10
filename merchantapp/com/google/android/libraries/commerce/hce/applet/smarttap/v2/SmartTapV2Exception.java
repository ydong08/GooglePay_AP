package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefMessage;
import com.google.android.libraries.commerce.hce.applet.smarttap.SmartTapException;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;

public class SmartTapV2Exception extends SmartTapException {
    private final Code responseCode;
    private final Status status;

    public SmartTapV2Exception(Status status, Code code, String str) {
        super(str);
        this.status = status;
        this.responseCode = code;
    }

    public SmartTapV2Exception(Status status, Code code, String str, Throwable th) {
        super(str, th);
        this.status = status;
        this.responseCode = code;
    }

    public static NdefMessage tryParseNdefMessage(byte[] bArr, String str) throws SmartTapV2Exception {
        try {
            return new NdefMessage(bArr);
        } catch (Throwable e) {
            throw new SmartTapV2Exception(Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, String.format("Failed to parse NDEF message from %s.", new Object[]{str}), e);
        }
    }

    public static void check(boolean z, Status status, Code code, String str, Object... objArr) throws SmartTapV2Exception {
        if (!z) {
            throw new SmartTapV2Exception(status, code, String.format(str, objArr));
        }
    }

    public Code getResponseCode() {
        return this.responseCode;
    }
}
