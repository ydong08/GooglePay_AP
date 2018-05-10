package com.google.android.libraries.commerce.hce.terminal.nfc;

import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.Action;

public interface ApduParser {
    NfcMessage parse(Action action, byte[] bArr, short s);
}
