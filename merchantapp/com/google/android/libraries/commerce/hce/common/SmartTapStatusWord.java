package com.google.android.libraries.commerce.hce.common;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableBiMap.Builder;
import java.nio.ByteBuffer;

public class SmartTapStatusWord extends StatusWord {
    private static final ImmutableBiMap<Code, Integer> CODE_TO_STATUS_WORD_MAP;
    public static final Creator<SmartTapStatusWord> CREATOR = new Creator<SmartTapStatusWord>() {
        public SmartTapStatusWord createFromParcel(Parcel parcel) {
            return new SmartTapStatusWord(parcel);
        }

        public SmartTapStatusWord[] newArray(int i) {
            return new SmartTapStatusWord[i];
        }
    };
    private final int statusWord;

    static {
        Builder builder = ImmutableBiMap.builder();
        builder.put(Code.SUCCESS, Integer.valueOf(36864));
        builder.put(Code.SUCCESS_NO_PAYLOAD, Integer.valueOf(36865));
        builder.put(Code.SUCCESS_PRESIGNED_AUTH, Integer.valueOf(36866));
        builder.put(Code.SUCCESS_MORE_PAYLOAD, Integer.valueOf(37120));
        builder.put(Code.SUCCESS_WITH_PAYLOAD_PAYMENT_NOT_READY, Integer.valueOf(37121));
        builder.put(Code.SUCCESS_NO_PAYLOAD_PAYMENT_NOT_READY, Integer.valueOf(37122));
        builder.put(Code.SUCCESS_PAYMENT_NOT_READY_UNKNOWN_REASON, Integer.valueOf(37375));
        builder.put(Code.UNKNOWN_TRANSIENT_FAILURE, Integer.valueOf(37376));
        builder.put(Code.CRYPTO_FAILURE, Integer.valueOf(37377));
        builder.put(Code.TIMEOUT_FAILURE, Integer.valueOf(37378));
        builder.put(Code.EXECUTION_FAILURE, Integer.valueOf(37379));
        builder.put(Code.UNKNOWN_HANDSET_ERROR, Integer.valueOf(37631));
        builder.put(Code.DEVICE_LOCKED, Integer.valueOf(37632));
        builder.put(Code.NO_PAYMENT_INSTRUMENT, Integer.valueOf(37633));
        builder.put(Code.UNKNOWN_USER_ACTION_NEEDED, Integer.valueOf(37887));
        builder.put(Code.UNKNOWN_TERMINAL_COMMAND, Integer.valueOf(37888));
        builder.put(Code.UNKNOWN_NDEF_RECORD, Integer.valueOf(37889));
        builder.put(Code.PARSING_FAILURE, Integer.valueOf(37890));
        builder.put(Code.INVALID_CRYPTO_INPUT, Integer.valueOf(37891));
        builder.put(Code.REQUEST_MORE_NOT_APPLICABLE, Integer.valueOf(37892));
        builder.put(Code.MORE_DATA_NOT_AVAILABLE, Integer.valueOf(37893));
        builder.put(Code.TOO_MANY_REQUESTS, Integer.valueOf(37894));
        builder.put(Code.NO_MERCHANT_SET, Integer.valueOf(37895));
        builder.put(Code.INVALID_PUSHBACK_URI, Integer.valueOf(37896));
        builder.put(Code.UNKNOWN_TERMINAL_ERROR, Integer.valueOf(38143));
        builder.put(Code.AUTH_FAILED, Integer.valueOf(38144));
        builder.put(Code.PUSH_FAIL_NO_AUTH, Integer.valueOf(38145));
        builder.put(Code.VERSION_NOT_SUPPORTED, Integer.valueOf(38146));
        builder.put(Code.UNKNOWN_PERMANENT_ERROR, Integer.valueOf(38399));
        builder.put(Code.UNKNOWN_ERROR, Integer.valueOf(39319));
        builder.put(Code.UNKNOWN_CODE, Integer.valueOf(39320));
        builder.put(Code.UNSPECIFIED, Integer.valueOf(39321));
        builder.put(Code.UNKNOWN_AID, Integer.valueOf(27266));
        CODE_TO_STATUS_WORD_MAP = builder.build();
    }

    public SmartTapStatusWord(Code code) {
        super(code);
        this.statusWord = ((Integer) CODE_TO_STATUS_WORD_MAP.get(code)).intValue();
    }

    private SmartTapStatusWord(Code code, int i) {
        super(code);
        this.statusWord = i;
    }

    public byte[] toBytes() {
        return ByteBuffer.allocate(2).putShort((short) this.statusWord).array();
    }

    public int toInt() {
        return this.statusWord;
    }

    public static SmartTapStatusWord fromBytes(byte[] bArr) {
        Preconditions.checkArgument(bArr.length == 2);
        int i = ByteBuffer.wrap(bArr).getShort() & 65535;
        Code code = (Code) CODE_TO_STATUS_WORD_MAP.inverse().get(Integer.valueOf(i));
        if (code == null) {
            code = Code.UNKNOWN_CODE;
        }
        return new SmartTapStatusWord(code, i);
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        SmartTapStatusWord smartTapStatusWord = (SmartTapStatusWord) obj;
        if (smartTapStatusWord.statusWord == this.statusWord && smartTapStatusWord.getCode() == getCode()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.statusWord), getCode());
    }

    public String toString() {
        return String.format("'%04X': %s", new Object[]{Integer.valueOf(this.statusWord), getCode().getMessage()});
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.statusWord);
    }

    private SmartTapStatusWord(Parcel parcel) {
        super(parcel);
        this.statusWord = parcel.readInt();
    }
}
