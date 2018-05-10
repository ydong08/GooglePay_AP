package com.google.commerce.tapandpay.merchantapp.hce;

import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapApplet;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import javax.inject.Inject;

public class SmartTapV2Processor extends SmartTapCommandProcessor {
    private final SmartTapApplet smartTap2Applet;

    @Inject
    public SmartTapV2Processor(SmartTapApplet smartTapApplet) {
        super(smartTapApplet);
        this.smartTap2Applet = smartTapApplet;
    }

    public ResponseApdu processCommand(TestProcessor testProcessor, byte[] bArr) {
        return super.processCommand(testProcessor, bArr);
    }

    public byte[] getHandsetNonce() {
        return this.smartTap2Applet.getHandsetNonce();
    }
}
