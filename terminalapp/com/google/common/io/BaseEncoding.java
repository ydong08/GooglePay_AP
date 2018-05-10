package com.google.common.io;

import com.google.common.base.CharMatcher;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.Arrays;

public abstract class BaseEncoding {
    private static final BaseEncoding BASE16 = new Base16Encoding("base16()", "0123456789ABCDEF");
    private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", Character.valueOf('='));
    private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", Character.valueOf('='));
    private static final BaseEncoding BASE64 = new Base64Encoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", Character.valueOf('='));
    private static final BaseEncoding BASE64_URL = new Base64Encoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", Character.valueOf('='));

    static final class Alphabet extends CharMatcher {
        final int bitsPerChar;
        final int bytesPerChunk;
        private final char[] chars;
        final int charsPerChunk;
        private final byte[] decodabet;
        final int mask;
        private final String name;
        private final boolean[] validPadding;

        Alphabet(String str, char[] cArr) {
            int i = 0;
            this.name = (String) Preconditions.checkNotNull(str);
            this.chars = (char[]) Preconditions.checkNotNull(cArr);
            try {
                this.bitsPerChar = IntMath.log2(cArr.length, RoundingMode.UNNECESSARY);
                int min = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
                try {
                    this.charsPerChunk = 8 / min;
                    this.bytesPerChunk = this.bitsPerChar / min;
                    this.mask = cArr.length - 1;
                    byte[] bArr = new byte[128];
                    Arrays.fill(bArr, (byte) -1);
                    for (min = 0; min < cArr.length; min++) {
                        boolean z;
                        char c = cArr[min];
                        Preconditions.checkArgument(CharMatcher.ascii().matches(c), "Non-ASCII character: %s", c);
                        if (bArr[c] == (byte) -1) {
                            z = true;
                        } else {
                            z = false;
                        }
                        Preconditions.checkArgument(z, "Duplicate character: %s", c);
                        bArr[c] = (byte) min;
                    }
                    this.decodabet = bArr;
                    boolean[] zArr = new boolean[this.charsPerChunk];
                    while (i < this.bytesPerChunk) {
                        zArr[IntMath.divide(i * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
                        i++;
                    }
                    this.validPadding = zArr;
                } catch (Throwable e) {
                    Throwable th = e;
                    String str2 = "Illegal alphabet ";
                    String valueOf = String.valueOf(new String(cArr));
                    throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), th);
                }
            } catch (Throwable e2) {
                throw new IllegalArgumentException("Illegal alphabet length " + cArr.length, e2);
            }
        }

        char encode(int i) {
            return this.chars[i];
        }

        public boolean matches(char c) {
            return CharMatcher.ascii().matches(c) && this.decodabet[c] != (byte) -1;
        }

        public String toString() {
            return this.name;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Alphabet)) {
                return false;
            }
            return Arrays.equals(this.chars, ((Alphabet) obj).chars);
        }

        public int hashCode() {
            return Arrays.hashCode(this.chars);
        }
    }

    static class StandardBaseEncoding extends BaseEncoding {
        final Alphabet alphabet;
        final Character paddingChar;

        StandardBaseEncoding(String str, String str2, Character ch) {
            this(new Alphabet(str, str2.toCharArray()), ch);
        }

        StandardBaseEncoding(Alphabet alphabet, Character ch) {
            this.alphabet = (Alphabet) Preconditions.checkNotNull(alphabet);
            boolean z = ch == null || !alphabet.matches(ch.charValue());
            Preconditions.checkArgument(z, "Padding character %s was already in alphabet", (Object) ch);
            this.paddingChar = ch;
        }

        int maxEncodedSize(int i) {
            return this.alphabet.charsPerChunk * IntMath.divide(i, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
        }

        void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            Preconditions.checkNotNull(appendable);
            Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
            int i3 = 0;
            while (i3 < i2) {
                encodeChunkTo(appendable, bArr, i + i3, Math.min(this.alphabet.bytesPerChunk, i2 - i3));
                i3 += this.alphabet.bytesPerChunk;
            }
        }

        void encodeChunkTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            boolean z;
            int i3;
            Preconditions.checkNotNull(appendable);
            Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
            if (i2 <= this.alphabet.bytesPerChunk) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z);
            long j = 0;
            for (i3 = 0; i3 < i2; i3++) {
                j = (j | ((long) (bArr[i + i3] & 255))) << 8;
            }
            int i4 = ((i2 + 1) * 8) - this.alphabet.bitsPerChar;
            i3 = 0;
            while (i3 < i2 * 8) {
                appendable.append(this.alphabet.encode(((int) (j >>> (i4 - i3))) & this.alphabet.mask));
                i3 += this.alphabet.bitsPerChar;
            }
            if (this.paddingChar != null) {
                while (i3 < this.alphabet.bytesPerChunk * 8) {
                    appendable.append(this.paddingChar.charValue());
                    i3 += this.alphabet.bitsPerChar;
                }
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("BaseEncoding.");
            stringBuilder.append(this.alphabet.toString());
            if (8 % this.alphabet.bitsPerChar != 0) {
                if (this.paddingChar == null) {
                    stringBuilder.append(".omitPadding()");
                } else {
                    stringBuilder.append(".withPadChar('").append(this.paddingChar).append("')");
                }
            }
            return stringBuilder.toString();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof StandardBaseEncoding)) {
                return false;
            }
            StandardBaseEncoding standardBaseEncoding = (StandardBaseEncoding) obj;
            if (this.alphabet.equals(standardBaseEncoding.alphabet) && Objects.equal(this.paddingChar, standardBaseEncoding.paddingChar)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.alphabet.hashCode() ^ Objects.hashCode(this.paddingChar);
        }
    }

    static final class Base16Encoding extends StandardBaseEncoding {
        final char[] encoding;

        Base16Encoding(String str, String str2) {
            this(new Alphabet(str, str2.toCharArray()));
        }

        private Base16Encoding(Alphabet alphabet) {
            boolean z;
            int i = 0;
            super(alphabet, null);
            this.encoding = new char[512];
            if (alphabet.chars.length == 16) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z);
            while (i < 256) {
                this.encoding[i] = alphabet.encode(i >>> 4);
                this.encoding[i | 256] = alphabet.encode(i & 15);
                i++;
            }
        }

        void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            Preconditions.checkNotNull(appendable);
            Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
            for (int i3 = 0; i3 < i2; i3++) {
                int i4 = bArr[i + i3] & 255;
                appendable.append(this.encoding[i4]);
                appendable.append(this.encoding[i4 | 256]);
            }
        }
    }

    static final class Base64Encoding extends StandardBaseEncoding {
        Base64Encoding(String str, String str2, Character ch) {
            this(new Alphabet(str, str2.toCharArray()), ch);
        }

        private Base64Encoding(Alphabet alphabet, Character ch) {
            super(alphabet, ch);
            Preconditions.checkArgument(alphabet.chars.length == 64);
        }

        void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            Preconditions.checkNotNull(appendable);
            Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
            int i3 = i;
            for (int i4 = i2; i4 >= 3; i4 -= 3) {
                int i5 = i3 + 1;
                int i6 = i5 + 1;
                i5 = ((bArr[i5] & 255) << 8) | ((bArr[i3] & 255) << 16);
                i3 = i6 + 1;
                i5 |= bArr[i6] & 255;
                appendable.append(this.alphabet.encode(i5 >>> 18));
                appendable.append(this.alphabet.encode((i5 >>> 12) & 63));
                appendable.append(this.alphabet.encode((i5 >>> 6) & 63));
                appendable.append(this.alphabet.encode(i5 & 63));
            }
            if (i3 < i + i2) {
                encodeChunkTo(appendable, bArr, i3, (i + i2) - i3);
            }
        }
    }

    abstract void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException;

    abstract int maxEncodedSize(int i);

    BaseEncoding() {
    }

    public String encode(byte[] bArr) {
        return encode(bArr, 0, bArr.length);
    }

    public final String encode(byte[] bArr, int i, int i2) {
        Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
        Appendable stringBuilder = new StringBuilder(maxEncodedSize(i2));
        try {
            encodeTo(stringBuilder, bArr, i, i2);
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static BaseEncoding base16() {
        return BASE16;
    }
}
