package com.google.android.libraries.commerce.hce.ndef;

import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import java.util.Arrays;
import java.util.Locale;

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

    public static NdefRecord createText(byte[] bArr, String str, short s) {
        return createText(bArr, null, str, s);
    }

    public static NdefRecord createText(byte[] bArr, String str, String str2, short s) {
        if (str2 == null) {
            throw new NullPointerException("text is null");
        }
        byte[] bytes;
        if (str == null) {
            bytes = Locale.getDefault().getLanguage().getBytes(Text.LANGUAGE_CHARSET);
        } else {
            bytes = str.getBytes(Text.LANGUAGE_CHARSET);
        }
        try {
            return createText(bArr, Format.get(Text.DEFAULT_TEXT_CHARSET), bytes, str2.getBytes(Text.DEFAULT_TEXT_CHARSET), s);
        } catch (Throwable e) {
            LOG.e(e, "Should not happen! Internally used format is not supported.", new Object[0]);
            throw new IllegalStateException("Should not happen! Most likely internally used format is not supported.", e);
        }
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

    public static NdefRecord createUri(byte[] bArr, Uri uri) {
        return new NdefRecord((short) 1, NdefRecord.RTD_URI, bArr, NdefRecord.createUri(uri).getPayload());
    }

    public static NdefRecord createPrimitive(byte[] bArr, Integer num, short s) {
        return createPrimitive(bArr, Format.BINARY, Ints.toByteArray(num.intValue()), s);
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

    public static ImmutableMultimap<ByteArrayWrapper, NdefRecord> extractRecords(NdefRecord ndefRecord, short s) throws FormatException {
        return extractRecords(ndefRecord, 0, s);
    }

    public static ImmutableMultimap<ByteArrayWrapper, NdefRecord> extractRecords(NdefRecord ndefRecord, int i, short s) throws FormatException {
        byte[] bytes = getBytes(ndefRecord, i, ndefRecord.getPayload().length - i);
        if (bytes.length == 0) {
            return ImmutableMultimap.of();
        }
        NdefMessage ndefMessage = new NdefMessage(bytes);
        Builder builder = ImmutableSetMultimap.builder();
        for (NdefRecord ndefRecord2 : ndefMessage.getRecords()) {
            builder.put(new ByteArrayWrapper(getType(ndefRecord2, s)), ndefRecord2);
        }
        return builder.build();
    }
}
