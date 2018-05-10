package com.google.android.libraries.commerce.hce.iso7816;

import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;

public class Aid extends ByteArrayWrapper {
    public static final Aid AMERICAN_EXPRESS_AID_PREFIX_CREDIT_OR_DEBIT = new Aid(Bytes.concat(AMERICAN_EXPRESS_RID.array(), new byte[]{(byte) 1, (byte) 16, (byte) 1}));
    public static final Aid AMERICAN_EXPRESS_RID = new Aid(new byte[]{(byte) -96, (byte) 0, (byte) 0, (byte) 0, (byte) 37});
    public static final Aid DISCOVER_AID_PREFIX = new Aid(Bytes.concat(DISCOVER_RID.array(), new byte[]{(byte) 16, (byte) 16}));
    public static final Aid DISCOVER_RID = new Aid(new byte[]{(byte) -96, (byte) 0, (byte) 0, (byte) 3, (byte) 36});
    public static final Aid MASTERCARD_AID_PREFIX_CREDIT_OR_DEBIT = new Aid(Bytes.concat(MASTERCARD_RID.array(), new byte[]{(byte) 16, (byte) 16}));
    public static final Aid MASTERCARD_RID = new Aid(new byte[]{(byte) -96, (byte) 0, (byte) 0, (byte) 0, (byte) 4});
    public static final Aid OSE = new Aid(new byte[]{(byte) 79, (byte) 83, (byte) 69, (byte) 46, (byte) 86, (byte) 65, (byte) 83, (byte) 46, (byte) 48, (byte) 49});
    public static final Aid PPSE_AID = new Aid(new byte[]{(byte) 50, (byte) 80, (byte) 65, (byte) 89, (byte) 46, (byte) 83, (byte) 89, (byte) 83, (byte) 46, (byte) 68, (byte) 68, (byte) 70, (byte) 48, (byte) 49});
    public static final Aid SMART_TAP_AID_V1_3 = new Aid(new byte[]{(byte) -96, (byte) 0, (byte) 0, (byte) 4, (byte) 118, (byte) -48, (byte) 0, (byte) 1, (byte) 1});
    public static final Aid SMART_TAP_AID_V2_0 = new Aid(new byte[]{(byte) -96, (byte) 0, (byte) 0, (byte) 4, (byte) 118, (byte) -48, (byte) 0, (byte) 1, (byte) 17});
    public static final Aid VISA_AID_PREFIX_CREDIT = new Aid(Bytes.concat(VISA_RID.array(), new byte[]{(byte) 3, (byte) 16, (byte) 16}));
    public static final Aid VISA_AID_PREFIX_DEBIT = new Aid(Bytes.concat(VISA_RID.array(), new byte[]{(byte) -104, (byte) 8, (byte) 64}));
    public static final Aid VISA_RID = new Aid(new byte[]{(byte) -96, (byte) 0, (byte) 0, (byte) 0});

    private Aid(byte[] bArr) {
        super(bArr);
    }

    public static Aid valueOf(byte[] bArr) throws IllegalArgumentException {
        int length = bArr.length;
        boolean z = length >= 5 && length <= 16;
        Preconditions.checkArgument(z, "Illegal length for AID: %s", length);
        return new Aid(bArr);
    }

    public byte[] getSelectCommand() {
        return getSelectCommand(this.bytes);
    }

    public static byte[] getSelectCommand(byte[] bArr) {
        r0 = new byte[4][];
        r0[1] = new byte[]{(byte) bArr.length};
        r0[2] = bArr;
        r0[3] = new byte[]{(byte) 0};
        return Bytes.concat(r0);
    }

    public String toString() {
        return Hex.encode(this.bytes);
    }
}
