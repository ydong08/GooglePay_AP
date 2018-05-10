package com.google.android.libraries.commerce.hce.ndef;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.util.Bcd;
import com.google.android.libraries.commerce.hce.util.ByteArrays;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Primitive {
    public static final Charset LANGUAGE_CODE_CHARSET = StandardCharsets.US_ASCII;
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final Format format;
    private Optional<Integer> intOptional;
    private final boolean isWellKnownText;
    private Optional<Long> longOptional;
    private final byte[] payload;
    private Optional<Text> textOptional;

    public Primitive(NdefRecord ndefRecord) {
        this(ndefRecord, false);
    }

    public static Primitive fromServiceRecord(NdefRecord ndefRecord, short s) {
        return new Primitive(ndefRecord, s == (short) 0);
    }

    private Primitive(NdefRecord ndefRecord, boolean z) {
        this.intOptional = null;
        this.longOptional = null;
        this.textOptional = null;
        Preconditions.checkArgument(ndefRecord.getPayload().length > 0);
        if (ndefRecord.getTnf() == (short) 4) {
            this.format = Format.get(NdefRecords.getByte(ndefRecord));
            byte[] allBytes = NdefRecords.getAllBytes(ndefRecord, 1);
            if (this.format.isText() && z) {
                r3 = new byte[2][];
                r3[0] = new byte[]{(byte) 0};
                r3[1] = allBytes;
                this.payload = Bytes.concat(r3);
            } else {
                this.payload = allBytes;
            }
            this.isWellKnownText = false;
        } else if (ndefRecord.getTnf() == (short) 1 && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            this.isWellKnownText = true;
            this.payload = ndefRecord.getPayload();
            this.format = Format.get((this.payload[0] & -128) == 0 ? StandardCharsets.UTF_8 : StandardCharsets.UTF_16);
        } else {
            String valueOf = String.valueOf(ndefRecord);
            throw new RuntimeException(new StringBuilder(String.valueOf(valueOf).length() + 46).append("NdefRecord was not a valid primitive. Record: ").append(valueOf).toString());
        }
    }

    public Format getFormat() {
        return this.format;
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public byte[] toBytes() {
        switch (this.format) {
            case ASCII:
            case UTF_8:
            case UTF_16:
                int i = this.payload[0];
                if (this.isWellKnownText) {
                    i &= 63;
                }
                return Arrays.copyOfRange(this.payload, i + 1, this.payload.length);
            case BINARY:
            case BCD:
                return this.payload;
            default:
                String valueOf = String.valueOf(this.format);
                throw new RuntimeException(new StringBuilder(String.valueOf(valueOf).length() + 43).append("Primitive contains unexpected format type: ").append(valueOf).toString());
        }
    }

    public Integer toInt() {
        if (this.intOptional != null) {
            return (Integer) this.intOptional.orNull();
        }
        switch (this.format) {
            case ASCII:
            case UTF_8:
            case UTF_16:
                if (toText() == null) {
                    this.intOptional = Optional.absent();
                    return null;
                }
                try {
                    this.intOptional = Optional.of(Integer.valueOf(Integer.parseInt(toText().getText())));
                    return (Integer) this.intOptional.get();
                } catch (Throwable e) {
                    LOG.e(e, "Failed to parse Integer from text: %s", r2);
                    this.intOptional = Optional.absent();
                    return null;
                }
            case BINARY:
                byte[] updatePayloadLength = ByteArrays.updatePayloadLength(this.payload, 4);
                this.intOptional = updatePayloadLength == null ? Optional.absent() : Optional.of(Integer.valueOf(Ints.fromByteArray(updatePayloadLength)));
                return (Integer) this.intOptional.orNull();
            case BCD:
                try {
                    this.intOptional = Optional.of(Integer.valueOf((int) Bcd.decode(ByteArrays.updatePayloadLength(this.payload, 4))));
                    return (Integer) this.intOptional.get();
                } catch (Throwable e2) {
                    LOG.e(e2, "Failed to parse Integer from BCD: %s", Hex.encode(this.payload));
                    this.intOptional = Optional.absent();
                    return null;
                }
            default:
                LOG.e("Unsupported converstion from %s format to Integer", this.format);
                this.intOptional = Optional.absent();
                return null;
        }
    }

    public Long toLong() {
        if (this.longOptional != null) {
            return (Long) this.longOptional.orNull();
        }
        switch (this.format) {
            case ASCII:
            case UTF_8:
            case UTF_16:
                if (toText() == null) {
                    this.longOptional = Optional.absent();
                    return null;
                }
                try {
                    this.longOptional = Optional.of(Long.valueOf(Long.parseLong(toText().getText())));
                    return (Long) this.longOptional.get();
                } catch (Throwable e) {
                    LOG.e(e, "Failed to parse Long from text: %s", r2);
                    this.longOptional = Optional.absent();
                    return null;
                }
            case BINARY:
                byte[] updatePayloadLength = ByteArrays.updatePayloadLength(this.payload, 8);
                this.longOptional = updatePayloadLength == null ? Optional.absent() : Optional.of(Long.valueOf(Longs.fromByteArray(updatePayloadLength)));
                return (Long) this.longOptional.orNull();
            case BCD:
                try {
                    this.longOptional = Optional.of(Long.valueOf(Bcd.decode(ByteArrays.updatePayloadLength(this.payload, 8))));
                    return (Long) this.longOptional.get();
                } catch (Throwable e2) {
                    LOG.e(e2, "Failed to parse Long from BCD: %s", Hex.encode(this.payload));
                    this.longOptional = Optional.absent();
                    return null;
                }
            default:
                LOG.e("Unsupported converstion from %s format to Long", this.format);
                this.longOptional = Optional.absent();
                return null;
        }
    }

    public Text toText() {
        if (this.textOptional != null) {
            return (Text) this.textOptional.orNull();
        }
        if (this.isWellKnownText) {
            int i = this.payload[0] & 63;
            if (i > this.payload.length - 1) {
                LOG.e("Invalid payload length %d in Text NDEF record.", Integer.valueOf(i));
                this.textOptional = Optional.absent();
                return null;
            }
            String str = new String(this.payload, 1, i, LANGUAGE_CODE_CHARSET);
            Charset charset = this.format.getCharset();
            this.textOptional = Optional.of(new Text(charset, str, new String(Arrays.copyOfRange(this.payload, i + 1, this.payload.length), charset)));
            return (Text) this.textOptional.get();
        }
        switch (this.format) {
            case ASCII:
            case UTF_8:
            case UTF_16:
                Charset charset2 = this.format.getCharset();
                byte b = this.payload[0];
                if (b > this.payload.length - 1) {
                    LOG.e("Invalid payload length %d in Text primitive", Byte.valueOf(b));
                    this.textOptional = Optional.absent();
                    return null;
                }
                this.textOptional = Optional.of(new Text(charset2, new String(this.payload, 1, b, LANGUAGE_CODE_CHARSET), new String(this.payload, b + 1, (this.payload.length - b) - 1, charset2)));
                return (Text) this.textOptional.get();
            default:
                LOG.e("Unsupported conversion from %s format to Text", this.format);
                this.textOptional = Optional.absent();
                return null;
        }
    }
}
