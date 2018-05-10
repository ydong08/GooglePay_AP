package com.google.commerce.tapandpay.merchantapp.hce;

import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapApplet;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import javax.inject.Inject;

public class SmartTapV1Processor extends SmartTapCommandProcessor {
    @Inject
    public SmartTapV1Processor(SmartTapApplet smartTapApplet) {
        super(smartTapApplet);
    }

    public ResponseApdu processCommand(TestProcessor testProcessor, byte[] bArr) {
        return super.processCommand(testProcessor, bArr);
    }
}
