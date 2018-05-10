package com.google.commerce.tapandpay.merchantapp.common;

import android.content.Context;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.iso7816.CommandApduException;
import com.google.commerce.tapandpay.merchantapp.paypass.PayPassCommandApduIns;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CommandType {
    SELECT_OSE,
    SELECT_SMARTTAP_V1_3,
    SELECT_SMARTTAP_V2_0,
    NEGOTIATE_SMARTTAP,
    GET_SMARTTAP_DATA,
    GET_ADDITIONAL_SMARTTAP_DATA,
    PUSH_SMARTTAP_DATA,
    SELECT_PPSE,
    SELECT_PAYPASS,
    GET_PROCESSING_OPTIONS,
    READ_RECORD,
    COMPUTE_CRYPTOGRAPHIC_CHECKSUM,
    ERROR;
    
    private static final Map<CommandType, Integer> COMMAND_TYPE_STRING_INT = null;
    public static final ImmutableList<CommandType> PAYMENT_TYPES = null;
    private static final ImmutableList<CommandType> SMARTTAP_TYPES = null;

    static {
        COMMAND_TYPE_STRING_INT = ImmutableMap.builder().put(SELECT_OSE, Integer.valueOf(R.string.select_ose)).put(SELECT_SMARTTAP_V1_3, Integer.valueOf(R.string.select_smarttap_v1_3)).put(SELECT_SMARTTAP_V2_0, Integer.valueOf(R.string.select_smarttap_v2_0)).put(NEGOTIATE_SMARTTAP, Integer.valueOf(R.string.negotiate_smarttap)).put(GET_SMARTTAP_DATA, Integer.valueOf(R.string.get_smarttap_data)).put(GET_ADDITIONAL_SMARTTAP_DATA, Integer.valueOf(R.string.get_additional_smarttap_data)).put(PUSH_SMARTTAP_DATA, Integer.valueOf(R.string.push_smarttap_data)).put(SELECT_PPSE, Integer.valueOf(R.string.select_ppse)).put(SELECT_PAYPASS, Integer.valueOf(R.string.select_paypass)).put(GET_PROCESSING_OPTIONS, Integer.valueOf(R.string.get_processing_options)).put(READ_RECORD, Integer.valueOf(R.string.read_record)).put(COMPUTE_CRYPTOGRAPHIC_CHECKSUM, Integer.valueOf(R.string.compute_cryptographic_checksum)).put(ERROR, Integer.valueOf(R.string.error_type)).build();
        PAYMENT_TYPES = ImmutableList.of(SELECT_PPSE, SELECT_PAYPASS, GET_PROCESSING_OPTIONS, READ_RECORD, COMPUTE_CRYPTOGRAPHIC_CHECKSUM);
        SMARTTAP_TYPES = ImmutableList.of(SELECT_SMARTTAP_V1_3, SELECT_SMARTTAP_V2_0, NEGOTIATE_SMARTTAP, GET_SMARTTAP_DATA, GET_ADDITIONAL_SMARTTAP_DATA, PUSH_SMARTTAP_DATA);
    }

    public int titleResId() {
        return ((Integer) COMMAND_TYPE_STRING_INT.get(this)).intValue();
    }

    public static CommandType fromTitle(String str, Context context) {
        Matcher matcher = Pattern.compile("(.+)\\s(command|response)\\sapdu").matcher(str);
        if (matcher.find()) {
            str = matcher.group(1);
        }
        for (Entry entry : COMMAND_TYPE_STRING_INT.entrySet()) {
            if (context.getString(((Integer) entry.getValue()).intValue()).equals(str)) {
                return (CommandType) entry.getKey();
            }
        }
        return null;
    }

    public String commandTitle(Context context) {
        return context.getString(R.string.command_apdu, new Object[]{context.getString(titleResId())});
    }

    public String responseTitle(Context context, boolean z) {
        if (z) {
            return context.getString(R.string.response_apdu, new Object[]{context.getString(R.string.error_type)});
        }
        return context.getString(R.string.response_apdu, new Object[]{context.getString(titleResId())});
    }

    public boolean isPayment() {
        return PAYMENT_TYPES.contains(this);
    }

    public boolean isSmartTap() {
        return SMARTTAP_TYPES.contains(this);
    }

    public static CommandType commandToType(byte[] bArr) {
        if (Ints.fromByteArray(bArr) == 10748928) {
            return selectType(bArr);
        }
        byte b = bArr[0];
        byte b2 = bArr[1];
        if (b == (byte) -112) {
            if (b2 == (byte) 83) {
                return NEGOTIATE_SMARTTAP;
            }
            if (b2 == (byte) 80) {
                return GET_SMARTTAP_DATA;
            }
            if (b2 == (byte) -64) {
                return GET_ADDITIONAL_SMARTTAP_DATA;
            }
            if (b2 == (byte) 82) {
                return PUSH_SMARTTAP_DATA;
            }
        }
        try {
            switch (PayPassCommandApduIns.fromByteArray(bArr).intValue()) {
                case 2:
                    return GET_PROCESSING_OPTIONS;
                case 3:
                    return READ_RECORD;
                case 4:
                    return COMPUTE_CRYPTOGRAPHIC_CHECKSUM;
                default:
                    return ERROR;
            }
        } catch (CommandApduException e) {
            return ERROR;
        }
    }

    public static CommandType selectType(byte[] bArr) {
        Aid valueOf = Aid.valueOf(Arrays.copyOfRange(bArr, 5, bArr[4] + 5));
        if (valueOf.equals(Aid.OSE)) {
            return SELECT_OSE;
        }
        if (valueOf.equals(Aid.SMART_TAP_AID_V1_3)) {
            return SELECT_SMARTTAP_V1_3;
        }
        if (valueOf.equals(Aid.SMART_TAP_AID_V2_0)) {
            return SELECT_SMARTTAP_V2_0;
        }
        if (valueOf.equals(Aid.MASTERCARD_AID_PREFIX_CREDIT_OR_DEBIT)) {
            return SELECT_PAYPASS;
        }
        if (valueOf.equals(Aid.PPSE_AID)) {
            return SELECT_PPSE;
        }
        return ERROR;
    }
}
