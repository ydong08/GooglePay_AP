package com.google.android.libraries.commerce.hce.terminal.smarttap;

import android.nfc.FormatException;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvInvalidLengthException;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvInvalidTagException;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvInvalidValueException;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.NfcMessageStatusException;
import com.google.android.libraries.commerce.hce.terminal.settings.SmartTapAid;
import java.io.IOException;

interface SmartTapHandler {
    boolean allowPayment();

    void executeSmartTap() throws BasicTlvInvalidLengthException, BasicTlvInvalidTagException, BasicTlvInvalidValueException, NfcMessageStatusException, IOException, FormatException;

    SmartTapAid getSmartTapAid();

    NfcMessage selectSmartTap() throws IOException, NfcMessageStatusException;

    boolean supportsSkippingSelect();

    boolean transmittedValuables();
}
