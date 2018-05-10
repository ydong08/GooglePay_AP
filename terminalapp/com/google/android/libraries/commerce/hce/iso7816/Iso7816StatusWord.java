package com.google.android.libraries.commerce.hce.iso7816;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;

public final class Iso7816StatusWord extends StatusWord {
    public static final Set<Iso7816StatusWord> ALL_KNOWN_STATUS_WORDS = ImmutableSet.of(NO_ERROR, WARNING_STATE_UNCHANGED, CARD_MANAGER_LOCKED, WARNING_NO_INFO_GIVEN, WARNING_MORE_DATA, VERIFY_FAILED, WRONG_LENGTH, SECURITY_STATUS_NOT_SATISFIED, FILE_INVALID, REFERENCE_DATA_NOT_USABLE, CONDITIONS_NOT_SATISFIED, COMMAND_NOT_ALLOWED, APPLET_SELECT_FAILED, WRONG_DATA, FUNCTION_NOT_SUPPORTED, FILE_NOT_FOUND, RECORD_NOT_FOUND, INCORRECT_P1P2, DATA_NOT_FOUND, FILE_ALREADY_EXISTS, WRONG_P1P2, INS_NOT_SUPPORTED, CLA_NOT_SUPPORTED, EXECUTION_ERROR, UNKNOWN_ERROR);
    public static final Iso7816StatusWord APPLET_SELECT_FAILED = new Iso7816StatusWord(Code.UNKNOWN_TERMINAL_COMMAND, 27033, "Applet selection failed");
    public static final Iso7816StatusWord CARD_MANAGER_LOCKED = new Iso7816StatusWord(Code.UNKNOWN_ERROR, 25219, "Warning: Card Manager is locked");
    public static final Iso7816StatusWord CLA_NOT_SUPPORTED = new Iso7816StatusWord(Code.UNKNOWN_TERMINAL_COMMAND, 28160, "Class not supported");
    private static final Map<Code, Iso7816StatusWord> CODE_TO_ISO7816_STATUS_MAP;
    public static final Iso7816StatusWord COMMAND_NOT_ALLOWED = new Iso7816StatusWord(Code.TOO_MANY_REQUESTS, 27014, "Command not allowed");
    public static final Iso7816StatusWord CONDITIONS_NOT_SATISFIED = new Iso7816StatusWord(Code.UNKNOWN_TRANSIENT_FAILURE, 27013, "Conditions of use not satisfied");
    public static final Creator<Iso7816StatusWord> CREATOR = new Creator<Iso7816StatusWord>() {
        public Iso7816StatusWord createFromParcel(Parcel parcel) {
            return new Iso7816StatusWord(parcel);
        }

        public Iso7816StatusWord[] newArray(int i) {
            return new Iso7816StatusWord[i];
        }
    };
    public static final Iso7816StatusWord DATA_NOT_FOUND = new Iso7816StatusWord(Code.UNKNOWN_TRANSIENT_FAILURE, 27272, "Referenced data not found");
    public static final Iso7816StatusWord EXECUTION_ERROR = new Iso7816StatusWord(Code.EXECUTION_FAILURE, 25600, "Execution error");
    public static final Iso7816StatusWord FILE_ALREADY_EXISTS = new Iso7816StatusWord(Code.UNKNOWN_ERROR, 27273, "File already exists");
    public static final Iso7816StatusWord FILE_INVALID = new Iso7816StatusWord(Code.UNKNOWN_AID, 27011, "File invalid");
    public static final Iso7816StatusWord FILE_NOT_FOUND = new Iso7816StatusWord(Code.UNKNOWN_AID, 27266, "File not found");
    public static final Iso7816StatusWord FUNCTION_NOT_SUPPORTED = new Iso7816StatusWord(Code.UNKNOWN_TERMINAL_COMMAND, 27265, "Function not supported");
    public static final Iso7816StatusWord INCORRECT_P1P2 = new Iso7816StatusWord(Code.UNKNOWN_TERMINAL_COMMAND, 27270, "Incorrect P1 or P2");
    public static final Iso7816StatusWord INS_NOT_SUPPORTED = new Iso7816StatusWord(Code.UNKNOWN_TERMINAL_COMMAND, 27904, "Instruction not supported or invalid");
    public static final Iso7816StatusWord NO_ERROR = new Iso7816StatusWord(Code.SUCCESS, 36864, "no error");
    public static final Iso7816StatusWord RECORD_NOT_FOUND = new Iso7816StatusWord(Code.UNKNOWN_TRANSIENT_FAILURE, 27267, "Record not found");
    public static final Iso7816StatusWord REFERENCE_DATA_NOT_USABLE = new Iso7816StatusWord(Code.UNKNOWN_ERROR, 27012, "Reference data not usable");
    public static final Iso7816StatusWord SECURITY_STATUS_NOT_SATISFIED = new Iso7816StatusWord(Code.UNKNOWN_TRANSIENT_FAILURE, 27010, "Security status not satisfied");
    private static final Map<Integer, Iso7816StatusWord> STATUS_WORD_MAP;
    public static final Iso7816StatusWord UNKNOWN_ERROR = new Iso7816StatusWord(Code.UNKNOWN_ERROR, 28416, "Unknown error (no precise diagnosis)");
    public static final Iso7816StatusWord VERIFY_FAILED = new Iso7816StatusWord(Code.UNKNOWN_USER_ACTION_NEEDED, 25536, "PIN authentication failed.");
    public static final Iso7816StatusWord WARNING_MORE_DATA = new Iso7816StatusWord(Code.SUCCESS_MORE_PAYLOAD, 25360, "more data");
    public static final Iso7816StatusWord WARNING_NO_INFO_GIVEN = new Iso7816StatusWord(Code.UNKNOWN_ERROR, 25344, "Warning: State changed (no information given)");
    public static final Iso7816StatusWord WARNING_STATE_UNCHANGED = new Iso7816StatusWord(Code.UNKNOWN_ERROR, 25088, "Warning: State unchanged");
    public static final Iso7816StatusWord WRONG_DATA = new Iso7816StatusWord(Code.PARSING_FAILURE, 27264, "Wrong data");
    public static final Iso7816StatusWord WRONG_LENGTH = new Iso7816StatusWord(Code.PARSING_FAILURE, 26368, "Wrong length");
    public static final Iso7816StatusWord WRONG_P1P2 = new Iso7816StatusWord(Code.UNKNOWN_TERMINAL_COMMAND, 27392, "Wrong P1 or P2");
    private final String message;
    private final int statusWord;

    static {
        Builder builder = ImmutableMap.builder();
        builder.put(Code.SUCCESS, NO_ERROR);
        builder.put(Code.SUCCESS_NO_PAYLOAD, NO_ERROR);
        builder.put(Code.SUCCESS_PRESIGNED_AUTH, NO_ERROR);
        builder.put(Code.SUCCESS_PAYMENT_NOT_READY_UNKNOWN_REASON, NO_ERROR);
        builder.put(Code.SUCCESS_MORE_PAYLOAD, WARNING_MORE_DATA);
        builder.put(Code.SUCCESS_WITH_PAYLOAD_PAYMENT_NOT_READY, NO_ERROR);
        builder.put(Code.SUCCESS_NO_PAYLOAD_PAYMENT_NOT_READY, NO_ERROR);
        builder.put(Code.UNKNOWN_TRANSIENT_FAILURE, CONDITIONS_NOT_SATISFIED);
        builder.put(Code.CRYPTO_FAILURE, CONDITIONS_NOT_SATISFIED);
        builder.put(Code.TIMEOUT_FAILURE, CONDITIONS_NOT_SATISFIED);
        builder.put(Code.EXECUTION_FAILURE, EXECUTION_ERROR);
        builder.put(Code.UNKNOWN_HANDSET_ERROR, EXECUTION_ERROR);
        builder.put(Code.UNKNOWN_USER_ACTION_NEEDED, CONDITIONS_NOT_SATISFIED);
        builder.put(Code.DEVICE_LOCKED, COMMAND_NOT_ALLOWED);
        builder.put(Code.NO_PAYMENT_INSTRUMENT, CONDITIONS_NOT_SATISFIED);
        builder.put(Code.PARSING_FAILURE, WRONG_DATA);
        builder.put(Code.UNKNOWN_TERMINAL_COMMAND, WRONG_DATA);
        builder.put(Code.UNKNOWN_NDEF_RECORD, WRONG_DATA);
        builder.put(Code.INVALID_CRYPTO_INPUT, WRONG_DATA);
        builder.put(Code.REQUEST_MORE_NOT_APPLICABLE, CONDITIONS_NOT_SATISFIED);
        builder.put(Code.MORE_DATA_NOT_AVAILABLE, COMMAND_NOT_ALLOWED);
        builder.put(Code.TOO_MANY_REQUESTS, COMMAND_NOT_ALLOWED);
        builder.put(Code.NO_MERCHANT_SET, COMMAND_NOT_ALLOWED);
        builder.put(Code.INVALID_PUSHBACK_URI, COMMAND_NOT_ALLOWED);
        builder.put(Code.UNKNOWN_TERMINAL_ERROR, WRONG_DATA);
        builder.put(Code.UNKNOWN_PERMANENT_ERROR, CONDITIONS_NOT_SATISFIED);
        builder.put(Code.AUTH_FAILED, CONDITIONS_NOT_SATISFIED);
        builder.put(Code.PUSH_FAIL_NO_AUTH, CONDITIONS_NOT_SATISFIED);
        builder.put(Code.VERSION_NOT_SUPPORTED, CONDITIONS_NOT_SATISFIED);
        builder.put(Code.UNKNOWN_ERROR, UNKNOWN_ERROR);
        builder.put(Code.UNKNOWN_CODE, UNKNOWN_ERROR);
        builder.put(Code.UNSPECIFIED, UNKNOWN_ERROR);
        builder.put(Code.UNKNOWN_AID, FILE_NOT_FOUND);
        CODE_TO_ISO7816_STATUS_MAP = builder.build();
        Builder builder2 = ImmutableMap.builder();
        for (Iso7816StatusWord iso7816StatusWord : ALL_KNOWN_STATUS_WORDS) {
            builder2.put(Integer.valueOf(iso7816StatusWord.statusWord), iso7816StatusWord);
        }
        STATUS_WORD_MAP = builder2.build();
    }

    private Iso7816StatusWord(Code code, int i, String str) {
        super(code);
        Preconditions.checkArgument((i >> 16) == 0);
        this.statusWord = i;
        this.message = str;
    }

    public static Iso7816StatusWord fromCode(Code code) {
        Iso7816StatusWord iso7816StatusWord = (Iso7816StatusWord) CODE_TO_ISO7816_STATUS_MAP.get(code);
        return code.equals(iso7816StatusWord.getCode()) ? iso7816StatusWord : new Iso7816StatusWord(code, iso7816StatusWord.toInt(), iso7816StatusWord.getMessage());
    }

    public static Iso7816StatusWord fromBytes(byte[] bArr) {
        Preconditions.checkArgument(bArr.length == 2);
        int i = ByteBuffer.wrap(bArr).getShort() & 65535;
        Iso7816StatusWord iso7816StatusWord = (Iso7816StatusWord) STATUS_WORD_MAP.get(Integer.valueOf(i));
        return iso7816StatusWord != null ? iso7816StatusWord : new Iso7816StatusWord(Code.UNKNOWN_CODE, i, "Unknown status word");
    }

    public int toInt() {
        return this.statusWord;
    }

    public byte[] toBytes() {
        return ByteBuffer.allocate(2).putShort((short) this.statusWord).array();
    }

    public String getMessage() {
        return this.message;
    }

    public String toString() {
        return String.format("'%04X': %s", new Object[]{Integer.valueOf(this.statusWord), this.message});
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Iso7816StatusWord iso7816StatusWord = (Iso7816StatusWord) obj;
        if (iso7816StatusWord.statusWord == this.statusWord && Objects.equal(iso7816StatusWord.message, this.message)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.statusWord), this.message);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.message);
        parcel.writeInt(this.statusWord);
    }

    private Iso7816StatusWord(Parcel parcel) {
        super(parcel);
        this.message = parcel.readString();
        this.statusWord = parcel.readInt();
    }
}
