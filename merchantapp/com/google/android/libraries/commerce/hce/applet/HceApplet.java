package com.google.android.libraries.commerce.hce.applet;

import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;

public interface HceApplet {

    public interface AppletProcessCommandResponse {
        ResponseApdu getResponseApdu();
    }

    AppletProcessCommandResponse processCommand(byte[] bArr, boolean z) throws InvalidCommandException;
}
