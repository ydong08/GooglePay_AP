package com.google.android.libraries.commerce.hce.common;

import com.google.common.primitives.Shorts;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SmartTap2Values {
    public static final byte[] ADDITIONAL_SERVICE_NDEF_TYPE = "asr".getBytes(TYPE_CHARSET);
    private static final byte[] AGGREGATED_SERVICE_NDEF_TYPE = "asv".getBytes(TYPE_CHARSET);
    public static final byte[] COLLECTOR_ID_NDEF_TYPE = "cld".getBytes(TYPE_CHARSET);
    public static final byte[] CRYPTO_PARAMS_NDEF_TYPE = "cpr".getBytes(TYPE_CHARSET);
    public static final byte[] CUSTOMER_DEVICE_NDEF_TYPE = "cud".getBytes(TYPE_CHARSET);
    public static final byte[] CUSTOMER_ID_NDEF_TYPE = "cid".getBytes(TYPE_CHARSET);
    public static final byte[] CUSTOMER_LANGUAGE_NDEF_TYPE = "cpl".getBytes(TYPE_CHARSET);
    public static final byte[] CUSTOMER_NDEF_TYPE = "cus".getBytes(TYPE_CHARSET);
    public static final byte[] CUSTOMER_TAP_NDEF_TYPE = "cut".getBytes(TYPE_CHARSET);
    public static final int CUSTOMER_V0_NDEF_HEADER_SIZE = (CUSTOMER_NDEF_TYPE.length + 4);
    public static final byte[] CVC_NDEF_TYPE = "c1".getBytes(TYPE_CHARSET);
    public static final byte[] ENCRYPTED_SERVICE_VALUE_NDEF_TYPE = "enc".getBytes(TYPE_CHARSET);
    public static final int ENCRYPTION_V0_OVERHEAD = (ENCRYPTED_SERVICE_VALUE_NDEF_TYPE.length + 24);
    public static final byte[] EXPIRATION_NDEF_TYPE = "ex".getBytes(TYPE_CHARSET);
    public static final byte[] GET_ADDITIONAL_DATA_NDEF_FLAG = "ADDF".getBytes(TYPE_CHARSET);
    public static final byte[] GET_DATA_NDEF_FLAG = "GETF".getBytes(TYPE_CHARSET);
    public static final byte[] GIFT_CARD_NDEF_TYPE = "gc".getBytes(TYPE_CHARSET);
    public static final int GIFT_CARD_V0_NDEF_HEADER_SIZE = (GIFT_CARD_NDEF_TYPE.length + 4);
    public static final byte[] GOOGLE_ISSUER_ID = new byte[]{(byte) 113, (byte) 121, (byte) 121, (byte) 113};
    public static final byte[] HANDSET_EPHEMERAL_PUBLIC_KEY_NDEF_TYPE = "dpk".getBytes(TYPE_CHARSET);
    public static final byte[] HANDSET_NONCE_NDEF_TYPE = "mdn".getBytes(TYPE_CHARSET);
    public static final byte[] ISSUER_NDEF_TYPE = "i".getBytes(TYPE_CHARSET);
    public static final byte[] LOCATION_ID_NDEF_TYPE = "lid".getBytes(TYPE_CHARSET);
    public static final byte[] LOYALTY_NDEF_TYPE = "ly".getBytes(TYPE_CHARSET);
    public static final int LOYALTY_V0_NDEF_HEADER_SIZE = (LOYALTY_NDEF_TYPE.length + 4);
    public static final byte[] MERCHANT_CATEGORY_NDEF_TYPE = "mcr".getBytes(TYPE_CHARSET);
    public static final byte[] MERCHANT_ID_V0_NDEF_TYPE = "mid".getBytes(TYPE_CHARSET);
    public static final byte[] MERCHANT_NAME_NDEF_TYPE = "mnr".getBytes(TYPE_CHARSET);
    public static final byte[] MERCHANT_NDEF_TYPE = "mer".getBytes(TYPE_CHARSET);
    public static final byte[] NEGOTIATE_NDEF_FLAG = "NEGF".getBytes(TYPE_CHARSET);
    public static final byte[] NEGOTIATE_NDEF_TYPE = "ngr".getBytes(TYPE_CHARSET);
    public static final byte[] NEGOTIATE_RESPONSE_NDEF_TYPE = "nrs".getBytes(TYPE_CHARSET);
    public static final byte[] NEW_SERVICE_NDEF_TYPE = "nsr".getBytes(TYPE_CHARSET);
    public static final byte[] NEW_SERVICE_TITLE_NDEF_TYPE = "nst".getBytes(TYPE_CHARSET);
    public static final byte[] NEW_SERVICE_URI_NDEF_TYPE = "nsu".getBytes(TYPE_CHARSET);
    public static final int NEW_SERVICE_V0_NDEF_HEADER_SIZE = (NEW_SERVICE_NDEF_TYPE.length + 4);
    private static final byte[] OBJECT_ID_NDEF_TYPE = "oid".getBytes(TYPE_CHARSET);
    public static final byte[] OFFER_NDEF_TYPE = "of".getBytes(TYPE_CHARSET);
    public static final int OFFER_V0_NDEF_HEADER_SIZE = (OFFER_NDEF_TYPE.length + 4);
    public static final byte[] PIN_NDEF_TYPE = "p".getBytes(TYPE_CHARSET);
    public static final byte[] PLC_NDEF_TYPE = "pl".getBytes(TYPE_CHARSET);
    public static final int PLC_V0_NDEF_HEADER_SIZE = (PLC_NDEF_TYPE.length + 4);
    public static final byte[] POS_CAPABILITIES_NDEF_TYPE = "pcr".getBytes(TYPE_CHARSET);
    public static final int POS_CAPABILITIES_V0_NDEF_HEADER_SIZE = (POS_CAPABILITIES_NDEF_TYPE.length + 4);
    public static final int POS_CAPABILITIES_V1_NDEF_HEADER_SIZE = (POS_CAPABILITIES_NDEF_TYPE.length + 3);
    public static final byte[] PUSH_NDEF_FLAG = "PSHF".getBytes(TYPE_CHARSET);
    public static final byte[] PUSH_SERVICE_NDEF_TYPE = "spr".getBytes(TYPE_CHARSET);
    public static final byte[] PUSH_SERVICE_RESPONSE_NDEF_TYPE = "psr".getBytes(TYPE_CHARSET);
    public static final int PUSH_SERVICE_V0_NDEF_HEADER_SIZE = (PUSH_SERVICE_NDEF_TYPE.length + 4);
    public static final int PUSH_SERVICE_V1_NDEF_HEADER_SIZE = (PUSH_SERVICE_NDEF_TYPE.length + 3);
    public static final byte[] RECORD_BUNDLE_NDEF_TYPE = "reb".getBytes(TYPE_CHARSET);
    public static final byte[] SELECT_NDEF_FLAG = "SELF".getBytes(TYPE_CHARSET);
    private static final byte[] SERVICE_ID_NDEF_TYPE = "sid".getBytes(TYPE_CHARSET);
    public static final byte[] SERVICE_LIST_NDEF_TYPE = "slr".getBytes(TYPE_CHARSET);
    public static final byte[] SERVICE_NUMBER_NDEF_TYPE = "n".getBytes(TYPE_CHARSET);
    public static final byte[] SERVICE_REQUEST_NDEF_TYPE = "srq".getBytes(TYPE_CHARSET);
    public static final byte[] SERVICE_RESPONSE_NDEF_TYPE = "srs".getBytes(TYPE_CHARSET);
    public static final int SERVICE_RESPONSE_V0_NDEF_HEADER_SIZE = (SERVICE_RESPONSE_NDEF_TYPE.length + 4);
    public static final int SERVICE_RESPONSE_V1_NDEF_HEADER_SIZE = (SERVICE_RESPONSE_NDEF_TYPE.length + 3);
    public static final byte[] SERVICE_STATUS_NDEF_TYPE = "ssr".getBytes(TYPE_CHARSET);
    public static final int SERVICE_STATUS_V0_NDEF_HEADER_SIZE = (SERVICE_STATUS_NDEF_TYPE.length + 4);
    public static final int SERVICE_STATUS_V1_NDEF_HEADER_SIZE = (SERVICE_STATUS_NDEF_TYPE.length + 3);
    public static final byte[] SERVICE_TYPE_REQUEST_NDEF_TYPE = "str".getBytes(TYPE_CHARSET);
    public static final byte[] SERVICE_UPDATE_NDEF_TYPE = "sup".getBytes(TYPE_CHARSET);
    public static final byte[] SERVICE_USAGE_DESCRIPTION_NDEF_TYPE = "sud".getBytes(TYPE_CHARSET);
    public static final byte[] SERVICE_USAGE_NDEF_TYPE = "sug".getBytes(TYPE_CHARSET);
    public static final byte[] SERVICE_USAGE_TITLE_NDEF_TYPE = "sut".getBytes(TYPE_CHARSET);
    private static final byte[] SERVICE_VALUE_NDEF_TYPE = "srv".getBytes(TYPE_CHARSET);
    public static final byte[] SESSION_NDEF_TYPE = "ses".getBytes(TYPE_CHARSET);
    public static final int SESSION_V0_NDEF_HEADER_SIZE = (SESSION_NDEF_TYPE.length + 4);
    public static final int SESSION_V1_NDEF_HEADER_SIZE = (SESSION_NDEF_TYPE.length + 3);
    public static final byte[] SIGNATURE_NDEF_TYPE = "sig".getBytes(TYPE_CHARSET);
    public static final byte[] SMARTTAP_MAX_VERSION = new byte[]{(byte) 0, (byte) 1};
    public static final short SMARTTAP_MAX_VERSION_SHORT = Shorts.fromByteArray(SMARTTAP_MAX_VERSION);
    public static final byte[] SMARTTAP_MIN_VERSION = new byte[]{(byte) 0, (byte) 0};
    public static final short SMARTTAP_MIN_VERSION_SHORT = Shorts.fromByteArray(SMARTTAP_MIN_VERSION);
    public static final byte[] TERMINAL_ID_NDEF_TYPE = "tid".getBytes(TYPE_CHARSET);
    public static final Charset TYPE_CHARSET = StandardCharsets.UTF_8;

    public static byte[] getServiceValueNdefType(short s) {
        return s == (short) 0 ? SERVICE_VALUE_NDEF_TYPE : AGGREGATED_SERVICE_NDEF_TYPE;
    }

    public static byte[] getServiceIdNdefType(short s) {
        return s == (short) 0 ? SERVICE_ID_NDEF_TYPE : OBJECT_ID_NDEF_TYPE;
    }

    private SmartTap2Values() {
    }

    public static String getNdefType(byte[] bArr) {
        return new String(bArr, TYPE_CHARSET);
    }
}
