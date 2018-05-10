package com.google.android.libraries.commerce.hce.terminal.nfc;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvUtil;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.Action;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.ParsedNfc;
import com.google.android.libraries.commerce.hce.terminal.nfc.StatusWordParser.ParsedNfcStatusWord;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

public class TlvParser implements ApduParser {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private Context context;

    public static class ParsedTlvNfc implements ParsedNfc {
        public static final Creator<ParsedTlvNfc> CREATOR = new Creator<ParsedTlvNfc>() {
            public ParsedTlvNfc createFromParcel(Parcel parcel) {
                return new ParsedTlvNfc(parcel);
            }

            public ParsedTlvNfc[] newArray(int i) {
                return new ParsedTlvNfc[i];
            }
        };
        private final StatusWord statusWord;
        private final String title;
        private final ImmutableMultimap<Integer, BasicTlv> tlvs;

        static class FilterPredicate implements Predicate<Entry<Integer, BasicTlv>> {
            private final Multimap<Integer, BasicTlv> filterAgainst;

            public FilterPredicate(Multimap<Integer, BasicTlv> multimap) {
                this.filterAgainst = multimap;
            }

            public boolean apply(Entry<Integer, BasicTlv> entry) {
                return !this.filterAgainst.containsEntry(entry.getKey(), entry.getValue());
            }
        }

        ParsedTlvNfc(String str, List<BasicTlv> list, StatusWord statusWord) {
            this.title = str;
            Builder builder = ImmutableSetMultimap.builder();
            for (BasicTlv basicTlv : list) {
                builder.put(Integer.valueOf(basicTlv.getTag()), basicTlv);
            }
            this.tlvs = builder.build();
            this.statusWord = statusWord;
        }

        public String getTitle() {
            return this.title;
        }

        public ImmutableMultimap<Integer, BasicTlv> getTlvs() {
            return this.tlvs;
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
            ParsedTlvNfc parsedTlvNfc = (ParsedTlvNfc) parsedNfc;
            if (this.statusWord.equals(parsedTlvNfc.statusWord) && tlvsAreEqual(this.tlvs, parsedTlvNfc.tlvs)) {
                return true;
            }
            return false;
        }

        private static boolean tlvsAreEqual(ImmutableMultimap<Integer, BasicTlv> immutableMultimap, ImmutableMultimap<Integer, BasicTlv> immutableMultimap2) {
            if (immutableMultimap == immutableMultimap2) {
                return true;
            }
            if (immutableMultimap == null || immutableMultimap2 == null) {
                return false;
            }
            if (Multimaps.filterEntries((Multimap) immutableMultimap, new FilterPredicate(immutableMultimap2)).isEmpty() && Multimaps.filterEntries((Multimap) immutableMultimap2, new FilterPredicate(immutableMultimap)).isEmpty()) {
                return true;
            }
            return false;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.title);
            byte[] tlvToByteArray = BasicTlvUtil.tlvToByteArray(this.tlvs.values());
            parcel.writeInt(tlvToByteArray.length);
            parcel.writeByteArray(tlvToByteArray);
            parcel.writeParcelable(this.statusWord, i);
        }

        private ParsedTlvNfc(Parcel parcel) {
            ImmutableMultimap build;
            this.title = parcel.readString();
            byte[] bArr = new byte[parcel.readInt()];
            parcel.readByteArray(bArr);
            try {
                ImmutableSetMultimap.Builder builder = ImmutableSetMultimap.builder();
                for (Object obj : BasicTlv.getDecodedInstances(bArr)) {
                    builder.put(Integer.valueOf(obj.getTag()), obj);
                }
                build = builder.build();
            } catch (Throwable e) {
                FormattingLogger access$100 = TlvParser.LOG;
                String str = "Failed to extract TLVs for NfcMessage from Bundle: ";
                String valueOf = String.valueOf(Throwables.getStackTraceAsString(e));
                access$100.e(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), new Object[0]);
                build = null;
            }
            this.tlvs = build;
            this.statusWord = (StatusWord) parcel.readParcelable(StatusWord.class.getClassLoader());
        }
    }

    public TlvParser(Context context) {
        this.context = context;
    }

    public NfcMessage parse(Action action, byte[] bArr, short s) {
        byte[] copyOfRange;
        String title = action.getTitle(this.context, R.string.generic_nfc_tlv);
        StatusWord statusWord = Iso7816StatusWord.NO_ERROR;
        if (action == Action.COMMAND) {
            copyOfRange = Arrays.copyOfRange(bArr, 5, bArr.length);
        } else if (action != Action.RESPONSE) {
            copyOfRange = bArr;
        } else if (bArr.length < 2) {
            LOG.w("%s is too short to contain a status word: %s", action, Hex.encode(bArr));
            return new NfcMessage(bArr, NfcMessage.getParseError(title, this.context.getString(R.string.action_status_too_short)));
        } else {
            statusWord = NfcMessage.getStatusWord(bArr, s);
            if (bArr.length == 2) {
                LOG.d("%s does not contain any TLVs: %s", action, Hex.encode(bArr));
                return new NfcMessage(bArr, new ParsedNfcStatusWord(title, statusWord));
            }
            copyOfRange = Arrays.copyOf(bArr, bArr.length - 2);
        }
        try {
            return new NfcMessage(bArr, new ParsedTlvNfc(title, BasicTlv.getDecodedInstances(copyOfRange), statusWord));
        } catch (Throwable e) {
            LOG.d(e, "Failed to decode NFC %s TLV instances. %s", action, Hex.encodeUpper(bArr));
            return new NfcMessage(bArr, NfcMessage.getParseError(title, this.context.getString(R.string.tlv_parse_error)));
        }
    }
}
