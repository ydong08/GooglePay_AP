package com.google.android.libraries.commerce.hce.terminal.nfc;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.Action;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.ParsedNfc;

public class StatusWordParser implements ApduParser {
    private final Context context;

    public static class ParsedNfcStatusWord implements ParsedNfc {
        public static final Creator<ParsedNfcStatusWord> CREATOR = new Creator<ParsedNfcStatusWord>() {
            public ParsedNfcStatusWord createFromParcel(Parcel parcel) {
                return new ParsedNfcStatusWord(parcel);
            }

            public ParsedNfcStatusWord[] newArray(int i) {
                return new ParsedNfcStatusWord[i];
            }
        };
        private final StatusWord statusWord;
        private final String title;

        public ParsedNfcStatusWord(String str, StatusWord statusWord) {
            this.title = str;
            this.statusWord = statusWord;
        }

        public String getTitle() {
            return this.title;
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
            return this.statusWord.equals(((ParsedNfcStatusWord) parsedNfc).statusWord);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.title);
            parcel.writeParcelable(this.statusWord, i);
        }

        private ParsedNfcStatusWord(Parcel parcel) {
            this.title = parcel.readString();
            this.statusWord = (StatusWord) parcel.readParcelable(StatusWord.class.getClassLoader());
        }
    }

    public StatusWordParser(Context context) {
        this.context = context;
    }

    public NfcMessage parse(Action action, byte[] bArr, short s) {
        return new NfcMessage(bArr, new ParsedNfcStatusWord(action.getTitle(this.context, R.string.generic_nfc_status_word), NfcMessage.getStatusWord(bArr, s)));
    }
}
