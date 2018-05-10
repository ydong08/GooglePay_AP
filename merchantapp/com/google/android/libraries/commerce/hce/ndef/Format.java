package com.google.android.libraries.commerce.hce.ndef;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap.Builder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public enum Format {
    UNSPECIFIED((byte) 0),
    ASCII((byte) 1),
    UTF_8((byte) 2),
    UTF_16((byte) 3),
    BINARY((byte) 4),
    BCD((byte) 5);
    
    public static final BiMap<Format, Charset> CHARSET_MAP = null;
    private final byte value;

    static {
        CHARSET_MAP = new Builder().put(ASCII, StandardCharsets.US_ASCII).put(UTF_8, StandardCharsets.UTF_8).put(UTF_16, StandardCharsets.UTF_16).build();
    }

    private Format(byte b) {
        this.value = b;
    }

    public byte value() {
        return this.value;
    }

    public boolean isText() {
        return this == ASCII || this == UTF_8 || this == UTF_16;
    }

    public static Format get(byte b) {
        for (Format format : values()) {
            if (b == format.value()) {
                return format;
            }
        }
        throw new RuntimeException("Byte " + b + " was not a backing value for NdefRecords.Format.");
    }

    public static Format get(Charset charset) {
        if (CHARSET_MAP.inverse().containsKey(charset)) {
            return (Format) CHARSET_MAP.inverse().get(charset);
        }
        String valueOf = String.valueOf(charset);
        throw new RuntimeException(new StringBuilder(String.valueOf(valueOf).length() + 49).append("Charset ").append(valueOf).append(" in not supported for NdefRecords.Format.").toString());
    }

    public Charset getCharset() {
        if (CHARSET_MAP.containsKey(this)) {
            return (Charset) CHARSET_MAP.get(this);
        }
        String valueOf = String.valueOf(this);
        throw new RuntimeException(new StringBuilder(String.valueOf(valueOf).length() + 30).append(valueOf).append(" does not represent a Charset.").toString());
    }
}
