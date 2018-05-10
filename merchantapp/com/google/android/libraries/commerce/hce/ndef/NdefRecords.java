package com.google.android.libraries.commerce.hce.ndef;

import android.nfc.FormatException;
import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.primitives.Bytes;
import java.util.Arrays;

public class NdefRecords {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    public static final byte[] TYPE_PRIMITIVE = "P".getBytes(SmartTap2Values.TYPE_CHARSET);

    private NdefRecords() {
    }

    public static byte[] getType(NdefRecord ndefRecord, short s) {
        if (s == (short) 0) {
            return ndefRecord.getId();
        }
        return ndefRecord.getType();
    }

    public static NdefRecord compose(byte[] bArr, byte[] bArr2, short s) {
        if (s == (short) 0) {
            return new NdefRecord((short) 4, null, bArr, bArr2);
        }
        return new NdefRecord((short) 4, bArr, null, bArr2);
    }

    public static NdefRecord createText(byte[] bArr, Format format, byte[] bArr2, byte[] bArr3, short s) throws FormatException {
        int i = 128;
        if (bArr3 == null) {
            throw new NullPointerException("textBytes is null");
        } else if (bArr2 == null) {
            throw new NullPointerException("languageBytes is null");
        } else if (bArr2.length >= 128) {
            throw new IllegalArgumentException("language code is too long, must be < 256 bytes.");
        } else if (s == (short) 0) {
            r0 = new byte[3][];
            r0[0] = new byte[]{(byte) bArr2.length};
            r0[1] = bArr2;
            r0[2] = bArr3;
            return createPrimitive(bArr, format, Bytes.concat(r0), s);
        } else {
            if (format == Format.ASCII || format == Format.UTF_8) {
                i = 0;
            } else if (format != Format.UTF_16) {
                String valueOf = String.valueOf(format.name());
                throw new FormatException(new StringBuilder(String.valueOf(valueOf).length() + 75).append("Unexpected format for text NdefRecord: ").append(valueOf).append(". Only UTF 8 or UTF 16 are expected.").toString());
            }
            r2 = new byte[3][];
            r2[0] = new byte[]{(byte) ((char) (i + bArr2.length))};
            r2[1] = bArr2;
            r2[2] = bArr3;
            return new NdefRecord((short) 1, NdefRecord.RTD_TEXT, bArr, Bytes.concat(r2));
        }
    }

    public static NdefRecord createPrimitive(byte[] bArr, Format format, byte[] bArr2, short s) {
        r0 = new byte[2][];
        r0[0] = new byte[]{format.value()};
        r0[1] = bArr2;
        byte[] concat = Bytes.concat(r0);
        if (s == (short) 0) {
            return new NdefRecord((short) 4, TYPE_PRIMITIVE, bArr, concat);
        }
        if (!format.isText()) {
            return compose(bArr, concat, s);
        }
        try {
            return createText(bArr, format, new byte[0], bArr2, s);
        } catch (Throwable e) {
            LOG.e(e, "Should not happen! Internally used format is not supported.", new Object[0]);
            throw new IllegalStateException("Should not happen! Internally used format is not supported.", e);
        }
    }

    public static byte getByte(NdefRecord ndefRecord) {
        return getByte(ndefRecord, 0);
    }

    public static byte getByte(NdefRecord ndefRecord, int i) {
        return ndefRecord.getPayload()[i];
    }

    public static byte[] getBytes(NdefRecord ndefRecord, int i) {
        return getBytes(ndefRecord, 0, i);
    }

    public static byte[] getAllBytes(NdefRecord ndefRecord, int i) {
        return getBytes(ndefRecord, i, ndefRecord.getPayload().length - i);
    }

    public static byte[] getBytes(NdefRecord ndefRecord, int i, int i2) {
        return Arrays.copyOfRange(ndefRecord.getPayload(), i, i + i2);
    }
}
