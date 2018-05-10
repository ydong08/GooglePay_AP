package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionRequest.Builder;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionRequest.DecodedRequest;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.util.Arrays;

public class GetAdditionalSmartTapDataRequest extends SessionRequest {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();

    private GetAdditionalSmartTapDataRequest(short s, byte[] bArr, byte b) {
        super(s, bArr, b);
    }

    public static GetAdditionalSmartTapDataRequest parse(byte[] bArr) throws SmartTapV2Exception {
        boolean z = true;
        Builder builder = new Builder();
        DecodedRequest decode = SessionRequest.decode(bArr, SmartTap2Values.ADDITIONAL_SERVICE_NDEF_TYPE);
        short version = decode.getVersion();
        if (version == (short) 0) {
            builder.setVersion(version);
            for (NdefRecord ndefRecord : decode.getNdefRecords()) {
                if (Arrays.equals(NdefRecords.getType(ndefRecord, version), SmartTap2Values.SESSION_NDEF_TYPE)) {
                    builder.setSession(ndefRecord, version);
                } else {
                    LOG.w("Unrecognized nested NDEF type %s", SmartTap2Values.getNdefType(NdefRecords.getType(ndefRecord, version)));
                }
            }
        } else {
            if (decode.getNdefRecords() != null) {
                z = false;
            }
            SmartTapV2Exception.check(z, Status.NDEF_FORMAT_ERROR, Code.PARSING_FAILURE, "Got unexpected data in GetAdditionalData version 2.1+", new Object[0]);
            builder.setVersion(version);
        }
        return builder.build();
    }
}
