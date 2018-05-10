package com.google.commerce.tapandpay.merchantapp.validation;

import com.google.android.libraries.commerce.hce.util.ByteArrays;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringMatcher {
    private static final ImmutableMap<String, Check> CHECKS_MAP;
    private static final ImmutableMap<String, Tnf> TNFS_MAP;

    public enum Check {
        LITERAL("LITERAL"),
        INTEGER("INTEGER"),
        ASCII("ASCII"),
        TNF("TNF"),
        REGEX("REGEX"),
        STARTS_WITH("STARTS_WITH"),
        ENDS_WITH("ENDS_WITH"),
        IS_EMPTY("IS_EMPTY"),
        LENGTH("LENGTH"),
        MIN_LENGTH("MIN_LENGTH"),
        MAX_LENGTH("MAX_LENGTH"),
        AND("AND"),
        OR("OR");
        
        private final String name;

        private Check(String str) {
            this.name = str;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class CheckResult {
        Check check;
        String condition;
        boolean result;

        CheckResult(boolean z, Check check, String str) {
            this.result = z;
            this.check = check;
            this.condition = str;
        }

        public boolean getResult() {
            return this.result;
        }

        public Check getCheck() {
            return this.check;
        }

        public String getCondition() {
            return this.condition;
        }

        public String toString() {
            String str;
            String valueOf;
            if (this.result) {
                str = "SUCCESS: ";
                valueOf = String.valueOf(this.condition);
                return valueOf.length() != 0 ? str.concat(valueOf) : new String(str);
            } else {
                str = "FAILURE: ";
                valueOf = String.valueOf(this.condition);
                return valueOf.length() != 0 ? str.concat(valueOf) : new String(str);
            }
        }
    }

    enum State {
        PARSING_OPERATION,
        WALKING_NESTED_CONDITION,
        FINISHED_PARSING_OPERATION,
        EXPECTING_PARSING_OPERATION
    }

    enum Tnf {
        EXTERNAL("EXTERNAL", (short) 4),
        WELL_KNOWN("WELL_KNOWN", (short) 1);
        
        private final String name;
        private final short value;

        private Tnf(String str, short s) {
            this.name = str;
            this.value = s;
        }

        public String getName() {
            return this.name;
        }

        public short getValue() {
            return this.value;
        }
    }

    static {
        int i = 0;
        Builder builder = ImmutableMap.builder();
        for (Check check : Check.values()) {
            builder.put(check.getName(), check);
        }
        CHECKS_MAP = builder.build();
        Builder builder2 = ImmutableMap.builder();
        Tnf[] values = Tnf.values();
        int length = values.length;
        while (i < length) {
            Tnf tnf = values[i];
            builder2.put(tnf.getName(), tnf);
            i++;
        }
        TNFS_MAP = builder2.build();
    }

    private StringMatcher() {
    }

    public static CheckResult matches(byte[] bArr, String str) throws IllegalArgumentException {
        if (Strings.isNullOrEmpty(str)) {
            throw new IllegalArgumentException("Attempting to validate against null or empty conditional.");
        }
        try {
            return recursiveMatches(bArr, str);
        } catch (Throwable e) {
            String str2 = "Failed to parse conditional: ";
            String valueOf = String.valueOf(str);
            throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), e);
        }
    }

    private static CheckResult recursiveMatches(byte[] bArr, String str) throws IllegalArgumentException {
        boolean z = true;
        boolean z2 = false;
        int indexOf = str.indexOf(40);
        int lastIndexOf = str.lastIndexOf(41);
        if (indexOf < 0 && lastIndexOf < 0) {
            return new CheckResult(literalMatches(bArr, str), Check.LITERAL, str);
        }
        String str2;
        String valueOf;
        if (indexOf < 0) {
            str2 = "Expected matching opening parenthesis in condition: ";
            valueOf = String.valueOf(str);
            throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        } else if (lastIndexOf < 0) {
            str2 = "Expected matching closing parenthesis in condition: ";
            valueOf = String.valueOf(str);
            throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        } else if (lastIndexOf != str.length() - 1) {
            str2 = "Trailing characters after closing parenthesis in condition: ";
            valueOf = String.valueOf(str);
            throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        } else {
            valueOf = str.substring(0, indexOf).toUpperCase();
            if (CHECKS_MAP.containsKey(valueOf)) {
                Check check = (Check) CHECKS_MAP.get(valueOf);
                String substring = str.substring(indexOf + 1, lastIndexOf);
                String encode;
                switch (check) {
                    case LITERAL:
                        z = literalMatches(bArr, substring);
                        break;
                    case INTEGER:
                        z = integerMatches(bArr, substring);
                        break;
                    case ASCII:
                        z = asciiMatches(bArr, substring);
                        break;
                    case TNF:
                        z = tnfMatches(bArr, substring);
                        break;
                    case REGEX:
                        Pattern compile = Pattern.compile(substring);
                        if (compile.matcher(Hex.encode(bArr)).matches() || compile.matcher(Hex.encodeUpper(bArr)).matches()) {
                            z2 = true;
                        }
                        z = z2;
                        break;
                    case STARTS_WITH:
                        if (bArr.length * 2 >= substring.length()) {
                            z = substring.equalsIgnoreCase(Hex.encode(bArr).substring(0, substring.length()));
                            break;
                        }
                        z = false;
                        break;
                    case ENDS_WITH:
                        if (bArr.length * 2 >= substring.length()) {
                            encode = Hex.encode(bArr);
                            z = substring.equalsIgnoreCase(encode.substring(encode.length() - substring.length()));
                            break;
                        }
                        z = false;
                        break;
                    case IS_EMPTY:
                        if (Strings.isNullOrEmpty(substring)) {
                            if (bArr.length != 0) {
                                z = false;
                                break;
                            }
                        }
                        str2 = "Unexpected contents inside of IS_EMPTY conditional: ";
                        valueOf = String.valueOf(substring);
                        throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                        break;
                    case LENGTH:
                        if (bArr.length != getInt(substring)) {
                            z = false;
                            break;
                        }
                        break;
                    case MIN_LENGTH:
                        if (bArr.length < getInt(substring)) {
                            z = false;
                            break;
                        }
                        break;
                    case MAX_LENGTH:
                        if (bArr.length > getInt(substring)) {
                            z = false;
                            break;
                        }
                        break;
                    case AND:
                        if (!Strings.isNullOrEmpty(substring)) {
                            for (String encode2 : getNestedConditions(substring)) {
                                CheckResult recursiveMatches = recursiveMatches(bArr, encode2);
                                if (!recursiveMatches.getResult()) {
                                    return recursiveMatches;
                                }
                            }
                            break;
                        }
                        throw new IllegalArgumentException("No conditions inside of AND condition.");
                    case OR:
                        if (!Strings.isNullOrEmpty(substring)) {
                            z = false;
                            for (String encode22 : getNestedConditions(substring)) {
                                z = recursiveMatches(bArr, encode22).getResult() | z;
                            }
                            break;
                        }
                        throw new IllegalArgumentException("No conditions inside of OR condition.");
                    default:
                        str2 = "Unsupported operator in condition: ";
                        valueOf = String.valueOf(str);
                        if (valueOf.length() != 0) {
                            valueOf = str2.concat(valueOf);
                        } else {
                            valueOf = new String(str2);
                        }
                        throw new IllegalArgumentException(valueOf);
                }
                return new CheckResult(z, check, substring);
            }
            throw new IllegalArgumentException(new StringBuilder((String.valueOf(valueOf).length() + 37) + String.valueOf(str).length()).append("Unrecognized operator ").append(valueOf).append(" in condition: ").append(str).toString());
        }
    }

    private static boolean literalMatches(byte[] bArr, String str) {
        return str.toUpperCase().equalsIgnoreCase(Hex.encode(bArr));
    }

    private static boolean integerMatches(byte[] bArr, String str) {
        byte[] updatePayloadLength = ByteArrays.updatePayloadLength(bArr, 4);
        if (updatePayloadLength != null && Ints.fromByteArray(updatePayloadLength) == getInt(str)) {
            return true;
        }
        return false;
    }

    private static boolean asciiMatches(byte[] bArr, String str) {
        return str.equals(new String(bArr, StandardCharsets.US_ASCII));
    }

    private static boolean tnfMatches(byte[] bArr, String str) {
        String toUpperCase = str.toUpperCase();
        if (TNFS_MAP.containsKey(toUpperCase)) {
            Tnf tnf = (Tnf) TNFS_MAP.get(toUpperCase);
            byte[] updatePayloadLength = ByteArrays.updatePayloadLength(bArr, 2);
            if (updatePayloadLength == null) {
                return false;
            }
            return Shorts.fromByteArray(updatePayloadLength) == tnf.getValue();
        }
        String str2 = "Unrecognized TNF type ";
        toUpperCase = String.valueOf(str);
        throw new IllegalArgumentException(toUpperCase.length() != 0 ? str2.concat(toUpperCase) : new String(str2));
    }

    private static int getInt(String str) throws IllegalArgumentException {
        try {
            int parseInt = Integer.parseInt(str);
            if (parseInt >= 0) {
                return parseInt;
            }
            throw new IllegalArgumentException("Unexpected integer value less than 0: " + parseInt);
        } catch (Throwable e) {
            Throwable th = e;
            String str2 = "Expected integer in conditional: ";
            String valueOf = String.valueOf(str);
            throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), th);
        }
    }

    private static List<String> getNestedConditions(String str) throws IllegalArgumentException {
        if (Strings.isNullOrEmpty(str)) {
            throw new IllegalArgumentException("Invalid conditional syntax, no nested conditions found.");
        }
        List arrayList = new ArrayList();
        State state = State.PARSING_OPERATION;
        StringBuilder stringBuilder = new StringBuilder();
        String trim = str.trim();
        StringBuilder stringBuilder2 = stringBuilder;
        State state2 = state;
        int i = 0;
        for (int i2 = 0; i2 < trim.length(); i2++) {
            char charAt = trim.charAt(i2);
            switch (state2) {
                case PARSING_OPERATION:
                    if (charAt == '(') {
                        stringBuilder2.append(charAt);
                        i++;
                        state2 = State.WALKING_NESTED_CONDITION;
                        break;
                    } else if (Character.isWhitespace(charAt)) {
                        arrayList.add(stringBuilder2.toString());
                        stringBuilder2 = new StringBuilder();
                        state2 = State.FINISHED_PARSING_OPERATION;
                        break;
                    } else if (charAt == ',') {
                        arrayList.add(stringBuilder2.toString());
                        stringBuilder2 = new StringBuilder();
                        state2 = State.EXPECTING_PARSING_OPERATION;
                        break;
                    } else if (Character.isLetterOrDigit(charAt) || charAt == '_') {
                        stringBuilder2.append(charAt);
                        break;
                    } else {
                        throw new IllegalArgumentException(String.format("Invalid conditional syntax at %s:%s in %s", new Object[]{Integer.valueOf(i2), Character.valueOf(charAt), trim}));
                    }
                case WALKING_NESTED_CONDITION:
                    stringBuilder2.append(charAt);
                    if (charAt == '(') {
                        i++;
                        break;
                    } else if (charAt == ')') {
                        i--;
                        if (i != 0) {
                            break;
                        }
                        arrayList.add(stringBuilder2.toString());
                        stringBuilder2 = new StringBuilder();
                        state2 = State.FINISHED_PARSING_OPERATION;
                        break;
                    } else if (!(Character.isLetterOrDigit(charAt) || charAt == '_' || charAt == ',' || Character.isWhitespace(charAt))) {
                        throw new IllegalArgumentException(String.format("Invalid conditional syntax at %s:%s in %s", new Object[]{Integer.valueOf(i2), Character.valueOf(charAt), trim}));
                    }
                case FINISHED_PARSING_OPERATION:
                    if (Character.isWhitespace(charAt)) {
                        continue;
                    } else if (charAt == ',') {
                        state2 = State.EXPECTING_PARSING_OPERATION;
                        break;
                    } else {
                        throw new IllegalArgumentException(String.format("Invalid conditional syntax at %s:%s in %s", new Object[]{Integer.valueOf(i2), Character.valueOf(charAt), trim}));
                    }
                case EXPECTING_PARSING_OPERATION:
                    if (Character.isWhitespace(charAt)) {
                        continue;
                    } else if (Character.isLetterOrDigit(charAt)) {
                        stringBuilder2.append(charAt);
                        state2 = State.PARSING_OPERATION;
                        break;
                    } else {
                        throw new IllegalArgumentException(String.format("Invalid conditional syntax at %s:%s in %s", new Object[]{Integer.valueOf(i2), Character.valueOf(charAt), trim}));
                    }
                default:
                    throw new RuntimeException("Should not get here!");
            }
        }
        if (state2 == State.PARSING_OPERATION && stringBuilder2.length() > 0) {
            arrayList.add(stringBuilder2.toString());
        } else if (state2 != State.FINISHED_PARSING_OPERATION) {
            String str2 = "Invalid conditional syntax, found trailing characters: ";
            String valueOf = String.valueOf(trim);
            throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        }
        if (arrayList.size() >= 2) {
            return arrayList;
        }
        str2 = "Invalid conditional syntax, expected at least 2 nested conditionals: ";
        valueOf = String.valueOf(trim);
        if (valueOf.length() != 0) {
            valueOf = str2.concat(valueOf);
        } else {
            valueOf = new String(str2);
        }
        throw new IllegalArgumentException(valueOf);
    }
}
