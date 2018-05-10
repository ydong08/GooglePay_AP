package com.google.commerce.tapandpay.merchantapp.hce;

import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;

interface CommandProcessor {
    ResponseApdu processCommand(TestProcessor testProcessor, byte[] bArr);
}
