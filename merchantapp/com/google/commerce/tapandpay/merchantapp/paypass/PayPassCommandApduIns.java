package com.google.commerce.tapandpay.merchantapp.paypass;

import com.google.android.libraries.commerce.hce.iso7816.CommandApduException;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.primitives.AbstractNamedInt;

public class PayPassCommandApduIns extends AbstractNamedInt {
    static final PayPassCommandApduIns CCC = new PayPassCommandApduIns("CCC", 4);
    private static final ResponseApdu CLA_NOT_SUPPORTED = ResponseApdu.fromStatusWord(Iso7816StatusWord.CLA_NOT_SUPPORTED);
    static final PayPassCommandApduIns GPO = new PayPassCommandApduIns("GPO", 2);
    private static final ResponseApdu INS_NOT_SUPPORTED = ResponseApdu.fromStatusWord(Iso7816StatusWord.INS_NOT_SUPPORTED);
    static final PayPassCommandApduIns READ_RECORD = new PayPassCommandApduIns("READ_RECORD", 3);
    static final PayPassCommandApduIns SELECT = new PayPassCommandApduIns("SELECT", 1);

    private PayPassCommandApduIns(String str, int i) {
        super(str, i);
    }

    public static PayPassCommandApduIns fromByteArray(byte[] bArr) throws CommandApduException {
        byte b = bArr[0];
        byte b2 = bArr[1];
        switch (b) {
            case Byte.MIN_VALUE:
                return typeFromProprietary(b2);
            case (byte) 0:
                return typeFromBase(b2);
            default:
                throw new CommandApduException(CLA_NOT_SUPPORTED);
        }
    }

    private static PayPassCommandApduIns typeFromProprietary(byte b) throws CommandApduException {
        switch (b) {
            case (byte) -88:
                return GPO;
            case (byte) 42:
                return CCC;
            default:
                throw new CommandApduException(INS_NOT_SUPPORTED);
        }
    }

    private static PayPassCommandApduIns typeFromBase(byte b) throws CommandApduException {
        switch (b) {
            case (byte) -92:
                return SELECT;
            case (byte) -78:
                return READ_RECORD;
            default:
                throw new CommandApduException(INS_NOT_SUPPORTED);
        }
    }
}
