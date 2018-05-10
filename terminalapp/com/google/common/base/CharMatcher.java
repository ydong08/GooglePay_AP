package com.google.common.base;

import java.util.Arrays;

public abstract class CharMatcher implements Predicate<Character> {
    @Deprecated
    public static final CharMatcher ANY = any();
    @Deprecated
    public static final CharMatcher ASCII = ascii();
    @Deprecated
    public static final CharMatcher BREAKING_WHITESPACE = breakingWhitespace();
    @Deprecated
    public static final CharMatcher DIGIT = digit();
    @Deprecated
    public static final CharMatcher INVISIBLE = invisible();
    @Deprecated
    public static final CharMatcher JAVA_DIGIT = javaDigit();
    @Deprecated
    public static final CharMatcher JAVA_ISO_CONTROL = javaIsoControl();
    @Deprecated
    public static final CharMatcher JAVA_LETTER = javaLetter();
    @Deprecated
    public static final CharMatcher JAVA_LETTER_OR_DIGIT = javaLetterOrDigit();
    @Deprecated
    public static final CharMatcher JAVA_LOWER_CASE = javaLowerCase();
    @Deprecated
    public static final CharMatcher JAVA_UPPER_CASE = javaUpperCase();
    @Deprecated
    public static final CharMatcher NONE = none();
    @Deprecated
    public static final CharMatcher SINGLE_WIDTH = singleWidth();
    @Deprecated
    public static final CharMatcher WHITESPACE = whitespace();

    static abstract class FastMatcher extends CharMatcher {
        FastMatcher() {
        }
    }

    static abstract class NamedFastMatcher extends FastMatcher {
        private final String description;

        NamedFastMatcher(String str) {
            this.description = (String) Preconditions.checkNotNull(str);
        }

        public final String toString() {
            return this.description;
        }
    }

    static final class Any extends NamedFastMatcher {
        static final Any INSTANCE = new Any();

        private Any() {
            super("CharMatcher.any()");
        }

        public boolean matches(char c) {
            return true;
        }
    }

    static final class Ascii extends NamedFastMatcher {
        static final Ascii INSTANCE = new Ascii();

        Ascii() {
            super("CharMatcher.ascii()");
        }

        public boolean matches(char c) {
            return c <= '';
        }
    }

    static final class BreakingWhitespace extends CharMatcher {
        static final CharMatcher INSTANCE = new BreakingWhitespace();

        private BreakingWhitespace() {
        }

        public boolean matches(char c) {
            switch (c) {
                case '\t':
                case '\n':
                case '\u000b':
                case '\f':
                case '\r':
                case ' ':
                case '':
                case ' ':
                case ' ':
                case ' ':
                case ' ':
                case '　':
                    return true;
                case ' ':
                    return false;
                default:
                    if (c < ' ' || c > ' ') {
                        return false;
                    }
                    return true;
            }
        }

        public String toString() {
            return "CharMatcher.breakingWhitespace()";
        }
    }

    static class RangesMatcher extends CharMatcher {
        private final String description;
        private final char[] rangeEnds;
        private final char[] rangeStarts;

        RangesMatcher(String str, char[] cArr, char[] cArr2) {
            boolean z;
            this.description = str;
            this.rangeStarts = cArr;
            this.rangeEnds = cArr2;
            if (cArr.length == cArr2.length) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z);
            for (int i = 0; i < cArr.length; i++) {
                boolean z2;
                if (cArr[i] <= cArr2[i]) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                Preconditions.checkArgument(z2);
                if (i + 1 < cArr.length) {
                    if (cArr2[i] < cArr[i + 1]) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    Preconditions.checkArgument(z2);
                }
            }
        }

        public boolean matches(char c) {
            int binarySearch = Arrays.binarySearch(this.rangeStarts, c);
            if (binarySearch >= 0) {
                return true;
            }
            binarySearch = (binarySearch ^ -1) - 1;
            if (binarySearch < 0 || c > this.rangeEnds[binarySearch]) {
                return false;
            }
            return true;
        }

        public String toString() {
            return this.description;
        }
    }

    static final class Digit extends RangesMatcher {
        static final Digit INSTANCE = new Digit();

        private static char[] zeroes() {
            return "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".toCharArray();
        }

        private static char[] nines() {
            char[] cArr = new char["0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".length()];
            for (int i = 0; i < "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".length(); i++) {
                cArr[i] = (char) ("0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".charAt(i) + 9);
            }
            return cArr;
        }

        private Digit() {
            super("CharMatcher.digit()", zeroes(), nines());
        }
    }

    static final class Invisible extends RangesMatcher {
        static final Invisible INSTANCE = new Invisible();

        private Invisible() {
            super("CharMatcher.invisible()", "\u0000­؀؜۝܏ ᠎   ⁦⁧⁨⁩⁪　?﻿￹￺".toCharArray(), "  ­؄؜۝܏ ᠎‏ ⁤⁦⁧⁨⁩⁯　﻿￹￻".toCharArray());
        }
    }

    static final class JavaDigit extends CharMatcher {
        static final JavaDigit INSTANCE = new JavaDigit();

        private JavaDigit() {
        }

        public boolean matches(char c) {
            return Character.isDigit(c);
        }

        public String toString() {
            return "CharMatcher.javaDigit()";
        }
    }

    static final class JavaIsoControl extends NamedFastMatcher {
        static final JavaIsoControl INSTANCE = new JavaIsoControl();

        private JavaIsoControl() {
            super("CharMatcher.javaIsoControl()");
        }

        public boolean matches(char c) {
            return c <= '\u001f' || (c >= '' && c <= '');
        }
    }

    static final class JavaLetter extends CharMatcher {
        static final JavaLetter INSTANCE = new JavaLetter();

        private JavaLetter() {
        }

        public boolean matches(char c) {
            return Character.isLetter(c);
        }

        public String toString() {
            return "CharMatcher.javaLetter()";
        }
    }

    static final class JavaLetterOrDigit extends CharMatcher {
        static final JavaLetterOrDigit INSTANCE = new JavaLetterOrDigit();

        private JavaLetterOrDigit() {
        }

        public boolean matches(char c) {
            return Character.isLetterOrDigit(c);
        }

        public String toString() {
            return "CharMatcher.javaLetterOrDigit()";
        }
    }

    static final class JavaLowerCase extends CharMatcher {
        static final JavaLowerCase INSTANCE = new JavaLowerCase();

        private JavaLowerCase() {
        }

        public boolean matches(char c) {
            return Character.isLowerCase(c);
        }

        public String toString() {
            return "CharMatcher.javaLowerCase()";
        }
    }

    static final class JavaUpperCase extends CharMatcher {
        static final JavaUpperCase INSTANCE = new JavaUpperCase();

        private JavaUpperCase() {
        }

        public boolean matches(char c) {
            return Character.isUpperCase(c);
        }

        public String toString() {
            return "CharMatcher.javaUpperCase()";
        }
    }

    static final class None extends NamedFastMatcher {
        static final None INSTANCE = new None();

        private None() {
            super("CharMatcher.none()");
        }

        public boolean matches(char c) {
            return false;
        }
    }

    static final class SingleWidth extends RangesMatcher {
        static final SingleWidth INSTANCE = new SingleWidth();

        private SingleWidth() {
            super("CharMatcher.singleWidth()", "\u0000־א׳؀ݐ฀Ḁ℀ﭐﹰ｡".toCharArray(), "ӹ־ת״ۿݿ๿₯℺﷿﻿ￜ".toCharArray());
        }
    }

    static final class Whitespace extends NamedFastMatcher {
        static final Whitespace INSTANCE = new Whitespace();
        static final int SHIFT = Integer.numberOfLeadingZeros(" 　\r   　 \u000b　   　 \t     \f 　 　　 \n 　".length() - 1);

        Whitespace() {
            super("CharMatcher.whitespace()");
        }

        public boolean matches(char c) {
            return " 　\r   　 \u000b　   　 \t     \f 　 　　 \n 　".charAt((1682554634 * c) >>> SHIFT) == c;
        }
    }

    public abstract boolean matches(char c);

    public static CharMatcher any() {
        return Any.INSTANCE;
    }

    public static CharMatcher none() {
        return None.INSTANCE;
    }

    public static CharMatcher whitespace() {
        return Whitespace.INSTANCE;
    }

    public static CharMatcher breakingWhitespace() {
        return BreakingWhitespace.INSTANCE;
    }

    public static CharMatcher ascii() {
        return Ascii.INSTANCE;
    }

    public static CharMatcher digit() {
        return Digit.INSTANCE;
    }

    public static CharMatcher javaDigit() {
        return JavaDigit.INSTANCE;
    }

    public static CharMatcher javaLetter() {
        return JavaLetter.INSTANCE;
    }

    public static CharMatcher javaLetterOrDigit() {
        return JavaLetterOrDigit.INSTANCE;
    }

    public static CharMatcher javaUpperCase() {
        return JavaUpperCase.INSTANCE;
    }

    public static CharMatcher javaLowerCase() {
        return JavaLowerCase.INSTANCE;
    }

    public static CharMatcher javaIsoControl() {
        return JavaIsoControl.INSTANCE;
    }

    public static CharMatcher invisible() {
        return Invisible.INSTANCE;
    }

    public static CharMatcher singleWidth() {
        return SingleWidth.INSTANCE;
    }

    protected CharMatcher() {
    }

    @Deprecated
    public boolean apply(Character ch) {
        return matches(ch.charValue());
    }

    public String toString() {
        return super.toString();
    }
}
