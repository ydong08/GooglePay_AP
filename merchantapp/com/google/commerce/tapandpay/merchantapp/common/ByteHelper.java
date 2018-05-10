package com.google.commerce.tapandpay.merchantapp.common;

import android.content.Context;
import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetAdditionalSmartTapDataRequest;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetSmartTapDataRequest;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetSmartTapDataResponse;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NegotiateSmartTapRequest;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NegotiateSmartTapResponse;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.PushSmartTapDataRequest;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionResponse;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapV2Exception;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvInvalidLengthException;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvInvalidTagException;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.util.Arrays;
import java.util.List;

public class ByteHelper {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();

    public static class ByteHelperException extends Exception {
        public ByteHelperException(String str) {
            super(str);
        }

        public ByteHelperException(String str, Throwable th) {
            super(str, th);
        }
    }

    private ByteHelper() {
    }

    public static PushSmartTapDataRequest pushSmartTapDataRequest(byte[] bArr) throws ByteHelperException {
        try {
            return PushSmartTapDataRequest.parse(bArr);
        } catch (Throwable e) {
            LOG.d(e, "Failed to decode Push SmartTap Data request for %s", Hex.encode(bArr));
            throw new ByteHelperException(e.getMessage(), e);
        }
    }

    public static NegotiateSmartTapRequest negotiateSmartTapRequest(byte[] bArr) throws ByteHelperException {
        try {
            return NegotiateSmartTapRequest.parse(bArr);
        } catch (Throwable e) {
            LOG.d(e, "Failed to decode Negotiate SmartTap request for %s", Hex.encode(bArr));
            throw new ByteHelperException(e.getMessage(), e);
        }
    }

    public static NegotiateSmartTapResponse negotiateSmartTapResponse(byte[] bArr, short s) throws ByteHelperException {
        try {
            return NegotiateSmartTapResponse.parse(bArr, s);
        } catch (Throwable e) {
            LOG.d(e, "Failed to decode Negotiate SmartTap response for %s", Hex.encode(bArr));
            throw new ByteHelperException(e.getMessage(), e);
        }
    }

    public static GetSmartTapDataRequest getSmartTapDataRequest(byte[] bArr) throws ByteHelperException {
        try {
            return GetSmartTapDataRequest.parse(bArr);
        } catch (Throwable e) {
            LOG.d(e, "Failed to decode Get SmartTap Data request for %s", Hex.encode(bArr));
            throw new ByteHelperException(e.getMessage(), e);
        }
    }

    public static GetAdditionalSmartTapDataRequest getAdditionalSmartTapDataRequest(byte[] bArr) throws ByteHelperException {
        try {
            return GetAdditionalSmartTapDataRequest.parse(bArr);
        } catch (Throwable e) {
            LOG.d(e, "Failed to decode Get Additional SmartTap Data request for %s", Hex.encode(bArr));
            throw new ByteHelperException(e.getMessage(), e);
        }
    }

    public static GetSmartTapDataResponse getSmartTapDataResponse(byte[] bArr, ServiceObjectConverter serviceObjectConverter, short s) throws ByteHelperException {
        try {
            return GetSmartTapDataResponse.parse(bArr, serviceObjectConverter, s);
        } catch (Throwable e) {
            LOG.d(e, "Failed to decode Get SmartTap Data response for %s", Hex.encode(bArr));
            throw new ByteHelperException(e.getMessage(), e);
        }
    }

    public static SessionResponse sessionResponse(byte[] bArr, byte[] bArr2, short s) throws ByteHelperException {
        try {
            return SessionResponse.parse(bArr, bArr2, s);
        } catch (Throwable e) {
            LOG.d(e, "Failed to decode SmartTap response for %s", Hex.encode(bArr));
            throw new ByteHelperException(e.getMessage(), e);
        }
    }

    public static byte[] getHandsetNonce(byte[] bArr) throws ByteHelperException {
        if (bArr.length >= 6) {
            try {
                NdefRecord ndefRecord = SmartTapV2Exception.tryParseNdefMessage(Arrays.copyOfRange(bArr, 4, bArr.length - 2), "Select").getRecords()[0];
                if (Arrays.equals(ndefRecord.getId(), SmartTap2Values.HANDSET_NONCE_NDEF_TYPE)) {
                    return Arrays.copyOfRange(ndefRecord.getPayload(), 1, ndefRecord.getPayload().length);
                }
            } catch (Throwable e) {
                LOG.d(e, "Failed to decode Select SmartTap v2 response for %s", Hex.encode(bArr));
                throw new ByteHelperException(e.getMessage(), e);
            }
        }
        throw new ByteHelperException("Missing handset nonce");
    }

    public static String getHexString(Context context, byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return context.getString(R.string.no_value);
        }
        return Hex.encode(bArr);
    }

    public static List<BasicTlv> getCommandTlvs(byte[] bArr) throws ByteHelperException {
        try {
            return BasicTlv.getDecodedInstances(bArr, 5, bArr.length - 1);
        } catch (Throwable e) {
            LOG.d(e, "Failed to decode command tlvs for %s", Hex.encode(bArr));
            throw new ByteHelperException(e.getMessage(), e);
        }
    }

    public static List<BasicTlv> getResponseTlvs(byte[] bArr) throws ByteHelperException {
        try {
            return BasicTlv.getDecodedInstances(bArr, 0, (bArr.length - 1) - 2);
        } catch (Throwable e) {
            LOG.d(e, "Failed to decode response tlvs for %s", Hex.encode(bArr));
            throw new ByteHelperException(e.getMessage(), e);
        }
    }

    public static String getTlvStringValue(Context context, BasicTlv basicTlv) {
        try {
            return Hex.encode(basicTlv.getValue());
        } catch (BasicTlvInvalidTagException e) {
            return context.getString(R.string.invalid_tag);
        } catch (BasicTlvInvalidLengthException e2) {
            return context.getString(R.string.invalid_length);
        }
    }
}
