package com.google.android.libraries.commerce.hce.terminal.nfc;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.Action;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.ParsedNfc;
import com.google.android.libraries.commerce.hce.terminal.nfc.StatusWordParser.ParsedNfcStatusWord;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Preconditions;
import java.util.Arrays;

public class NdefParser implements ApduParser {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private Context context;
    boolean lastResponseHadMoreData = false;

    public static class ParsedNdefNfc implements ParsedNfc {
        public static final Creator<ParsedNdefNfc> CREATOR = new Creator<ParsedNdefNfc>() {
            public ParsedNdefNfc createFromParcel(Parcel parcel) {
                return new ParsedNdefNfc(parcel);
            }

            public ParsedNdefNfc[] newArray(int i) {
                return new ParsedNdefNfc[i];
            }
        };
        private final NdefRecord record;
        private final StatusWord statusWord;
        private final String title;
        private final short version;

        public ParsedNdefNfc(String str, short s, NdefRecord ndefRecord, StatusWord statusWord) {
            this.title = str;
            this.version = s;
            this.record = ndefRecord;
            this.statusWord = statusWord;
        }

        public String getTitle() {
            return this.title;
        }

        public short getVersion() {
            return this.version;
        }

        public NdefRecord getRecord() {
            return this.record;
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
            ParsedNdefNfc parsedNdefNfc = (ParsedNdefNfc) parsedNfc;
            if (this.statusWord.equals(parsedNdefNfc.statusWord) && this.record.equals(parsedNdefNfc.record)) {
                return true;
            }
            return false;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.title);
            parcel.writeInt(this.version);
            parcel.writeParcelable(this.record, i);
            parcel.writeParcelable(this.statusWord, i);
        }

        private ParsedNdefNfc(Parcel parcel) {
            this.title = parcel.readString();
            this.version = (short) parcel.readInt();
            this.record = (NdefRecord) parcel.readParcelable(NdefRecord.class.getClassLoader());
            this.statusWord = (StatusWord) parcel.readParcelable(StatusWord.class.getClassLoader());
        }
    }

    public static class PartialNdefNfc implements ParsedNfc {
        public static final Creator<PartialNdefNfc> CREATOR = new Creator<PartialNdefNfc>() {
            public PartialNdefNfc createFromParcel(Parcel parcel) {
                return new PartialNdefNfc(parcel);
            }

            public PartialNdefNfc[] newArray(int i) {
                return new PartialNdefNfc[i];
            }
        };
        private final byte[] bytes;
        private final StatusWord statusWord;
        private final String title;
        private final short version;

        PartialNdefNfc(String str, short s, byte[] bArr, StatusWord statusWord) {
            this.title = str;
            this.version = s;
            this.bytes = (byte[]) bArr.clone();
            this.statusWord = statusWord;
        }

        public String getTitle() {
            return this.title;
        }

        public StatusWord getStatusWord() {
            return this.statusWord;
        }

        public byte[] getBytes() {
            return this.bytes;
        }

        public short getVersion() {
            return this.version;
        }

        public boolean isEqualTo(ParsedNfc parsedNfc) {
            if (this == parsedNfc) {
                return true;
            }
            if (parsedNfc.getClass() != getClass()) {
                return false;
            }
            PartialNdefNfc partialNdefNfc = (PartialNdefNfc) parsedNfc;
            if (this.statusWord.equals(partialNdefNfc.statusWord) && Arrays.equals(this.bytes, partialNdefNfc.getBytes())) {
                return true;
            }
            return false;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.title);
            parcel.writeInt(this.version);
            parcel.writeInt(this.bytes.length);
            parcel.writeByteArray(this.bytes);
            parcel.writeParcelable(this.statusWord, i);
        }

        private PartialNdefNfc(Parcel parcel) {
            this.title = parcel.readString();
            this.version = (short) parcel.readInt();
            this.bytes = new byte[parcel.readInt()];
            parcel.readByteArray(this.bytes);
            this.statusWord = (StatusWord) parcel.readParcelable(StatusWord.class.getClassLoader());
        }
    }

    public NdefParser(Context context) {
        this.context = context;
    }

    public NfcMessage parse(Action action, byte[] bArr, short s) {
        byte[] copyOfRange;
        boolean z = true;
        String title = action.getTitle(this.context, R.string.generic_nfc_ndef);
        StatusWord statusWord = Iso7816StatusWord.NO_ERROR;
        if (action == Action.COMMAND) {
            copyOfRange = Arrays.copyOfRange(bArr, 5, s == (short) 0 ? bArr.length : bArr.length - 1);
        } else if (action != Action.RESPONSE) {
            copyOfRange = bArr;
        } else if (bArr.length < 2) {
            LOG.w("%s is too short to contain a status word: %s", action, Hex.encode(bArr));
            return new NfcMessage(bArr, NfcMessage.getParseError(title, this.context.getString(R.string.action_status_too_short)));
        } else {
            statusWord = NfcMessage.getStatusWord(bArr, s);
            if (bArr.length == 2) {
                LOG.d("%s does not contain any NDEF records: %s", action, Hex.encode(bArr));
                return new NfcMessage(bArr, new ParsedNfcStatusWord(title, statusWord));
            }
            byte[] copyOf = Arrays.copyOf(bArr, bArr.length - 2);
            if (statusWord.getCode() == Code.SUCCESS_MORE_PAYLOAD || this.lastResponseHadMoreData) {
                if (this.lastResponseHadMoreData) {
                    copyOfRange = SmartTap2Values.ADDITIONAL_SERVICE_NDEF_TYPE;
                } else {
                    copyOfRange = SmartTap2Values.SERVICE_REQUEST_NDEF_TYPE;
                }
                if (statusWord.getCode() != Code.SUCCESS_MORE_PAYLOAD) {
                    z = false;
                }
                this.lastResponseHadMoreData = z;
                return new NfcMessage(bArr, new PartialNdefNfc(getTitle(action, copyOfRange), s, copyOf, statusWord));
            }
            this.lastResponseHadMoreData = false;
            copyOfRange = copyOf;
        }
        try {
            boolean z2;
            NdefRecord[] records = new NdefMessage(copyOfRange).getRecords();
            if (records.length == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            Preconditions.checkArgument(z2, "Expected NFC %s to have exactly one NDEF record. Found: %s records.", (Object) action, records.length);
            return new NfcMessage(bArr, new ParsedNdefNfc(getTitle(action, records[0], s), s, records[0], statusWord));
        } catch (Throwable e) {
            LOG.d(e, "Failed to decode NFC %s NDEF records. %s", action, Hex.encodeUpper(bArr));
            return new NfcMessage(bArr, NfcMessage.getParseError(title, this.context.getString(R.string.ndef_parse_error)));
        }
    }

    private String getTitle(Action action, NdefRecord ndefRecord, short s) {
        return getTitle(action, NdefRecords.getType(ndefRecord, s));
    }

    private String getTitle(Action action, byte[] bArr) {
        String string;
        if (Arrays.equals(bArr, SmartTap2Values.NEGOTIATE_NDEF_TYPE)) {
            string = this.context.getString(R.string.ndef_id_negotiate_request);
        } else if (Arrays.equals(bArr, SmartTap2Values.NEGOTIATE_RESPONSE_NDEF_TYPE)) {
            string = this.context.getString(R.string.ndef_id_negotiate_response);
        } else if (Arrays.equals(bArr, SmartTap2Values.SERVICE_REQUEST_NDEF_TYPE)) {
            string = this.context.getString(R.string.ndef_id_get_service_request);
        } else if (Arrays.equals(bArr, SmartTap2Values.ADDITIONAL_SERVICE_NDEF_TYPE)) {
            string = this.context.getString(R.string.ndef_id_get_additional_service);
        } else if (Arrays.equals(bArr, SmartTap2Values.SERVICE_RESPONSE_NDEF_TYPE)) {
            string = this.context.getString(R.string.ndef_id_get_service_response);
        } else if (Arrays.equals(bArr, SmartTap2Values.PUSH_SERVICE_NDEF_TYPE)) {
            string = this.context.getString(R.string.ndef_id_push_service);
        } else if (Arrays.equals(bArr, SmartTap2Values.PUSH_SERVICE_RESPONSE_NDEF_TYPE)) {
            string = this.context.getString(R.string.ndef_id_push_response);
        } else {
            string = this.context.getString(R.string.ndef_id_unknown, new Object[]{Hex.encodeUpper(bArr), SmartTap2Values.getNdefType(bArr)});
        }
        return action.getTitle(this.context, string);
    }
}
