package com.google.android.libraries.commerce.hce.terminal.nfc;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.libraries.commerce.hce.common.SmartTapStatusWord;
import com.google.android.libraries.commerce.hce.common.StatusWords;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;

public class NfcMessage {
    public static final String BROADCAST_NFC_MESSAGE = String.valueOf(NfcMessage.class.getPackage().getName()).concat(".NFC_MESSAGE");
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    static final ImmutableMap<Code, Integer> STATUS_CODE_MESSAGE_LOCALIZATION_MAP = ImmutableMap.builder().put(Code.SUCCESS, Integer.valueOf(R.string.status_code_success)).put(Code.SUCCESS_NO_PAYLOAD, Integer.valueOf(R.string.status_code_no_payload)).put(Code.SUCCESS_PRESIGNED_AUTH, Integer.valueOf(R.string.status_code_presigned_auth)).put(Code.SUCCESS_PAYMENT_NOT_READY_UNKNOWN_REASON, Integer.valueOf(R.string.status_code_payment_not_ready)).put(Code.SUCCESS_MORE_PAYLOAD, Integer.valueOf(R.string.status_code_more_payload)).put(Code.SUCCESS_WITH_PAYLOAD_PAYMENT_NOT_READY, Integer.valueOf(R.string.status_code_payload_payment_not_ready)).put(Code.SUCCESS_NO_PAYLOAD_PAYMENT_NOT_READY, Integer.valueOf(R.string.status_code_no_payload_payment_not_ready)).put(Code.UNKNOWN_TRANSIENT_FAILURE, Integer.valueOf(R.string.status_code_transient_failure)).put(Code.CRYPTO_FAILURE, Integer.valueOf(R.string.status_code_crypto_failure)).put(Code.TIMEOUT_FAILURE, Integer.valueOf(R.string.status_code_timeout)).put(Code.EXECUTION_FAILURE, Integer.valueOf(R.string.status_code_execution_failure)).put(Code.REQUEST_MORE_NOT_APPLICABLE, Integer.valueOf(R.string.status_code_more_not_applicable)).put(Code.MORE_DATA_NOT_AVAILABLE, Integer.valueOf(R.string.status_code_more_data_not_available)).put(Code.TOO_MANY_REQUESTS, Integer.valueOf(R.string.status_code_more_too_many_requests)).put(Code.NO_MERCHANT_SET, Integer.valueOf(R.string.status_code_no_merchant_set)).put(Code.INVALID_PUSHBACK_URI, Integer.valueOf(R.string.status_code_invalid_pushback_uri)).put(Code.UNKNOWN_USER_ACTION_NEEDED, Integer.valueOf(R.string.status_code_user_action_needed)).put(Code.DEVICE_LOCKED, Integer.valueOf(R.string.status_code_device_locked)).put(Code.NO_PAYMENT_INSTRUMENT, Integer.valueOf(R.string.status_code_no_payment_instrument)).put(Code.PARSING_FAILURE, Integer.valueOf(R.string.status_code_parsing_failure)).put(Code.UNKNOWN_TERMINAL_COMMAND, Integer.valueOf(R.string.status_code_unknown_command)).put(Code.UNKNOWN_NDEF_RECORD, Integer.valueOf(R.string.status_code_unknown_ndef)).put(Code.INVALID_CRYPTO_INPUT, Integer.valueOf(R.string.status_code_invalid_crypto_input)).put(Code.UNKNOWN_PERMANENT_ERROR, Integer.valueOf(R.string.status_code_smarttap_failure)).put(Code.AUTH_FAILED, Integer.valueOf(R.string.status_code_auth_failed)).put(Code.PUSH_FAIL_NO_AUTH, Integer.valueOf(R.string.status_code_push_failed_no_auth)).put(Code.VERSION_NOT_SUPPORTED, Integer.valueOf(R.string.status_code_versions_not_supported)).put(Code.UNKNOWN_ERROR, Integer.valueOf(R.string.status_code_unknown_error)).put(Code.UNKNOWN_CODE, Integer.valueOf(R.string.status_code_unknown_code)).put(Code.UNSPECIFIED, Integer.valueOf(R.string.status_code_unspecified)).put(Code.UNKNOWN_AID, Integer.valueOf(R.string.status_code_unknown_aid)).put(Code.UNKNOWN_HANDSET_ERROR, Integer.valueOf(R.string.status_code_payment_not_ready)).put(Code.UNKNOWN_TERMINAL_ERROR, Integer.valueOf(R.string.status_code_payment_not_ready)).build();
    private final ParsedNfc parsedNfc;
    private final byte[] value;

    public interface ParsedNfc extends Parcelable {
        StatusWord getStatusWord();

        String getTitle();

        boolean isEqualTo(ParsedNfc parsedNfc);
    }

    public enum Action {
        COMMAND(R.string.nfc_command),
        RESPONSE(R.string.nfc_response),
        INFO(R.string.nfc_info);
        
        private final int stringResource;

        private Action(int i) {
            this.stringResource = i;
        }

        public String toString(Context context) {
            return context.getString(this.stringResource);
        }

        public String getTitle(Context context, int i) {
            return getTitle(context, context.getString(i));
        }

        public String getTitle(Context context, String str) {
            return context.getString(R.string.nfc_title, new Object[]{toString(context), str});
        }
    }

    public static class NfcMessageStatusException extends Exception {
        private final boolean allowsPayment;

        public NfcMessageStatusException(StatusWord statusWord, String str) {
            super(str);
            this.allowsPayment = statusWord == null ? true : statusWord.allowsPayment();
        }

        public boolean allowsPayment() {
            return this.allowsPayment;
        }
    }

    public static class ParsedNfcError implements ParsedNfc {
        public static final Creator<ParsedNfcError> CREATOR = new Creator<ParsedNfcError>() {
            public ParsedNfcError createFromParcel(Parcel parcel) {
                return new ParsedNfcError(parcel);
            }

            public ParsedNfcError[] newArray(int i) {
                return new ParsedNfcError[i];
            }
        };
        private final String message;
        private final StatusWord statusWord;
        private final String summary;
        private final String title;

        ParsedNfcError(String str, String str2, String str3, StatusWord statusWord) {
            this.title = str;
            this.summary = str2;
            this.message = str3;
            this.statusWord = statusWord;
        }

        public String getTitle() {
            return this.title;
        }

        public String getSummary() {
            return this.summary;
        }

        public String getMessage() {
            return this.message;
        }

        public StatusWord getStatusWord() {
            return this.statusWord;
        }

        public boolean isEqualTo(ParsedNfc parsedNfc) {
            if (this == parsedNfc) {
                return true;
            }
            if (parsedNfc.getClass() != getClass()) {
                return false;
            }
            ParsedNfcError parsedNfcError = (ParsedNfcError) parsedNfc;
            if (this.summary.equals(parsedNfcError.summary) && Strings.nullToEmpty(this.message).equals(parsedNfcError.message)) {
                return true;
            }
            return false;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.title);
            parcel.writeString(this.summary);
            parcel.writeString(this.message);
            parcel.writeByteArray(this.statusWord.toBytes());
        }

        private ParsedNfcError(Parcel parcel) {
            this.title = parcel.readString();
            this.summary = parcel.readString();
            this.message = parcel.readString();
            byte[] bArr = new byte[2];
            parcel.readByteArray(bArr);
            this.statusWord = SmartTapStatusWord.fromBytes(bArr);
        }
    }

    public NfcMessage(byte[] bArr, ParsedNfc parsedNfc) {
        Preconditions.checkNotNull(bArr, "NFC Message must contain a byte array value.");
        Preconditions.checkNotNull(parsedNfc, "NFC Message must contain a parced NFC message.");
        this.value = bArr;
        this.parsedNfc = parsedNfc;
    }

    public byte[] getValue() {
        return this.value;
    }

    public ParsedNfc getParsedNfc() {
        return this.parsedNfc;
    }

    public String toString() {
        String valueOf = String.valueOf(this.parsedNfc.getTitle());
        String valueOf2 = String.valueOf(Hex.encodeUpper(getValue()));
        return new StringBuilder((String.valueOf(valueOf).length() + 4) + String.valueOf(valueOf2).length()).append(valueOf).append(": 0x").append(valueOf2).toString();
    }

    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(Arrays.hashCode(getValue())));
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        NfcMessage nfcMessage = (NfcMessage) obj;
        if (Arrays.equals(getValue(), nfcMessage.getValue()) && getParsedNfc().isEqualTo(nfcMessage.getParsedNfc())) {
            return true;
        }
        return false;
    }

    public StatusWord getStatusWord() {
        return this.parsedNfc.getStatusWord();
    }

    public void verifySuccessStatus(Context context, short s) throws NfcMessageStatusException {
        verifyStatus(context, s, Code.SUCCESS);
    }

    public void verifyStatus(Context context, short s, Code... codeArr) throws NfcMessageStatusException {
        StatusWord[] statusWordArr = new StatusWord[codeArr.length];
        for (int i = 0; i < codeArr.length; i++) {
            statusWordArr[i] = StatusWords.get(codeArr[i], s);
        }
        verifyStatus(context, statusWordArr);
    }

    public void verifyStatus(Context context, StatusWord... statusWordArr) throws NfcMessageStatusException {
        StatusWord statusWord = getStatusWord();
        if (statusWord == null) {
            throw new NfcMessageStatusException(statusWord, context.getString(R.string.no_status_word));
        }
        int length = statusWordArr.length;
        int i = 0;
        while (i < length) {
            if (!statusWordArr[i].equals(statusWord)) {
                i++;
            } else {
                return;
            }
        }
        String localizedStatusWordMessage = getLocalizedStatusWordMessage(context, statusWord);
        throw new NfcMessageStatusException(statusWord, context.getString(R.string.received_status_error, new Object[]{localizedStatusWordMessage}));
    }

    public static StatusWord getStatusWord(byte[] bArr, short s) {
        return StatusWords.get(Arrays.copyOfRange(bArr, Math.max(0, bArr.length - 2), bArr.length), s);
    }

    public static String getLocalizedStatusWordMessage(Context context, StatusWord statusWord) {
        if (statusWord == null) {
            return context.getString(R.string.status_code_null);
        }
        if (STATUS_CODE_MESSAGE_LOCALIZATION_MAP.containsKey(statusWord.getCode())) {
            return context.getString(((Integer) STATUS_CODE_MESSAGE_LOCALIZATION_MAP.get(statusWord.getCode())).intValue());
        }
        FormattingLogger formattingLogger = LOG;
        String valueOf = String.valueOf(statusWord);
        formattingLogger.w(new StringBuilder(String.valueOf(valueOf).length() + 38).append("Unknown Status Word for localization: ").append(valueOf).toString(), new Object[0]);
        return context.getString(R.string.status_code_unknown, new Object[]{statusWord.toString()});
    }

    public static ParsedNfc getParseError(String str, String str2) {
        return getParseError(str, str2, null, null);
    }

    public static ParsedNfc getParseError(String str, String str2, String str3, StatusWord statusWord) {
        return new ParsedNfcError(str, str2, str3, statusWord);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putByteArray("BUNDLE_KEY_VALUE", getValue());
        bundle.putParcelable("BUNDLE_KEY_PARSED_NFC", getParsedNfc());
        return bundle;
    }

    public static NfcMessage fromBundle(Bundle bundle) {
        return new NfcMessage(bundle.getByteArray("BUNDLE_KEY_VALUE"), (ParsedNfc) bundle.getParcelable("BUNDLE_KEY_PARSED_NFC"));
    }
}
