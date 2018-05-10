package com.google.commerce.tapandpay.merchantapp.validation;

import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Status;
import com.google.common.base.Strings;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BytePayloadHelper {
    private BytePayloadHelper() {
    }

    public static ValidationResults validatePrefix(String str, int i, byte[] bArr) {
        if (i > 0 && Strings.isNullOrEmpty(str)) {
            return ValidationResults.create(Status.INVALID, "Provided prefix length with no prefix validation condition.", new Object[0]);
        }
        if (bArr.length < i) {
            return ValidationResults.create(Status.FAILURE, "The prefix length (%s) is greater than the bytes length (%s). Bytes: %s", Integer.valueOf(i), Integer.valueOf(bArr.length), Hex.encodeUpper(bArr));
        }
        return validate("Prefix", str, Arrays.copyOf(bArr, i));
    }

    public static ValidationResults validateSuffix(String str, int i, byte[] bArr) {
        if (i > 0 && Strings.isNullOrEmpty(str)) {
            return ValidationResults.create(Status.INVALID, "Provided suffix length with no suffix validation condition.", new Object[0]);
        }
        if (bArr.length < i) {
            return ValidationResults.create(Status.FAILURE, "The suffix length (%s) is greater than the bytes length (%s). Bytes: %s", Integer.valueOf(i), Integer.valueOf(bArr.length), Hex.encodeUpper(bArr));
        }
        return validate("Suffix", str, Arrays.copyOfRange(bArr, bArr.length - i, bArr.length));
    }

    public static byte[] getPayload(int i, int i2, byte[] bArr) {
        return Arrays.copyOfRange(bArr, i, bArr.length - i2);
    }

    public static ValidationResults validateNdefId(String str, byte[] bArr) {
        return validate("NDEF record ID", str, bArr);
    }

    public static ValidationResults validateNdefTnf(String str, short s) {
        return validate("NDEF record TNF", str, ByteBuffer.allocate(2).putShort(s).array());
    }

    public static ValidationResults validateNdefType(String str, byte[] bArr) {
        return validate("NDEF record type", str, bArr);
    }

    public static ValidationResults validatePayload(String str, byte[] bArr) {
        return validate("Payload", str, bArr);
    }

    private static ValidationResults validate(String str, String str2, byte[] bArr) {
        if (Strings.isNullOrEmpty(str2)) {
            return ValidationResults.create(Status.SUCCESS, "%s has no validation condition.", str);
        }
        try {
            if (StringMatcher.matches(bArr, str2).getResult()) {
                return ValidationResults.create(Status.SUCCESS, "%s validation condition succeeded: %s. Bytes: %s", str, str2, Hex.encodeUpper(bArr));
            }
            return ValidationResults.create(Status.FAILURE, "%s condition failed %s check. Condition: %s. Bytes: %s", str, StringMatcher.matches(bArr, str2).getCheck().getName(), StringMatcher.matches(bArr, str2).getCondition(), Hex.encodeUpper(bArr));
        } catch (Throwable e) {
            return ValidationResults.create(Status.INVALID, e, "%s condition is invalid.", str);
        }
    }
}
