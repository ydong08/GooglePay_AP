package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import java.util.List;

public interface ServiceObjectConverter {
    List<ServiceObject> convert(NdefRecord ndefRecord, short s) throws SmartTapV2Exception;
}
