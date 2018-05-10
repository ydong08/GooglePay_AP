package com.google.android.libraries.commerce.hce.terminal.smarttap;

import com.google.android.libraries.commerce.hce.iso7816.Aid;

public class SmartTapValues {
    static final byte[] GET_REMAINING_DATA_PREFIX = new byte[]{(byte) -112, (byte) -64, (byte) 0, (byte) 0};
    static final byte[] GET_SMARTTAP_DATA_PREFIX = new byte[]{(byte) -112, (byte) 80, (byte) 0, (byte) 0};
    static final byte[] NEGOTIATE_SMARTTAP_DATA_PREFIX = new byte[]{(byte) -112, (byte) 83, (byte) 0, (byte) 0};
    static final byte[] PUSH_DATA_PREFIX = new byte[]{(byte) -112, (byte) 82, (byte) 0, (byte) 0};
    static final byte[] SELECT_AID_PREFIX = new byte[]{(byte) 0, (byte) -92, (byte) 4, (byte) 0, (byte) 9};
    public static final Aid SMART_TAP_AID_V1_3_OLD = Aid.valueOf(new byte[]{(byte) -96, (byte) 0, (byte) 0, (byte) 4, (byte) -123, (byte) 16, (byte) 1, (byte) 1, (byte) 1});
    static final byte[] SMART_TAP_V1_TLV_BYTES = new byte[]{(byte) 1, (byte) 0};
    static final byte[] SMART_TAP_V2_TLV_BYTES = new byte[]{(byte) 1, (byte) 1};

    private SmartTapValues() {
    }
}
