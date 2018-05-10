package com.google.android.libraries.commerce.hce.terminal.nfc;

import android.content.Context;
import android.content.Intent;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.Action;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.ParsedNfc;
import java.io.IOException;
import java.util.Arrays;

public class Transceiver {
    private final Context context;
    private final IsoDep isoDep;
    private final LocalBroadcastManager localBroadcastManager;
    private final StatusWordParser statusWordParser;

    public static class ParsedNfcSelect implements ParsedNfc {
        public static final Creator<ParsedNfcSelect> CREATOR = new Creator<ParsedNfcSelect>() {
            public ParsedNfcSelect createFromParcel(Parcel parcel) {
                return new ParsedNfcSelect(parcel);
            }

            public ParsedNfcSelect[] newArray(int i) {
                return new ParsedNfcSelect[i];
            }
        };
        private final byte[] aid;
        private final String title;

        ParsedNfcSelect(String str, byte[] bArr) {
            this.title = str;
            this.aid = Arrays.copyOfRange(bArr, 5, bArr[4] + 5);
        }

        public String getTitle() {
            return this.title;
        }

        public byte[] getAid() {
            return this.aid;
        }

        public StatusWord getStatusWord() {
            return Iso7816StatusWord.NO_ERROR;
        }

        public boolean isEqualTo(ParsedNfc parsedNfc) {
            if (this == parsedNfc) {
                return true;
            }
            if (parsedNfc.getClass() != getClass()) {
                return false;
            }
            return this.title.equals(((ParsedNfcSelect) parsedNfc).title);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.title);
            parcel.writeInt(this.aid.length);
            parcel.writeByteArray(this.aid);
        }

        private ParsedNfcSelect(Parcel parcel) {
            this.title = parcel.readString();
            this.aid = new byte[parcel.readInt()];
            parcel.readByteArray(this.aid);
        }
    }

    public Transceiver(Context context, IsoDep isoDep) {
        this.context = context;
        this.isoDep = isoDep;
        this.statusWordParser = new StatusWordParser(context);
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public void connect() throws IOException {
        this.isoDep.connect();
        this.isoDep.setTimeout(5000);
    }

    public void close() throws IOException {
        this.isoDep.close();
    }

    public NfcMessage transceiveSelect(byte[] bArr, String str) throws IOException {
        return transceiveSelect(bArr, str, (short) 0);
    }

    public NfcMessage transceiveSelect(byte[] bArr, String str, short s) throws IOException {
        return transceiveSelect(bArr, str, null, s);
    }

    public NfcMessage transceiveSelect(byte[] bArr, String str, ApduParser apduParser) throws IOException {
        return transceiveSelect(bArr, str, apduParser, (short) 0);
    }

    public NfcMessage transceiveSelect(byte[] bArr, String str, ApduParser apduParser, short s) throws IOException {
        Bundle toBundle = new NfcMessage(bArr, new ParsedNfcSelect(Action.COMMAND.getTitle(this.context, str), bArr)).toBundle();
        if (apduParser == null) {
            apduParser = this.statusWordParser;
        }
        return transceive(bArr, toBundle, apduParser, s);
    }

    public NfcMessage transceive(byte[] bArr) throws IOException {
        return transceive(bArr, null, (short) 0);
    }

    public NfcMessage transceive(byte[] bArr, ApduParser apduParser) throws IOException {
        return transceive(bArr, apduParser, (short) 0);
    }

    public NfcMessage transceive(byte[] bArr, ApduParser apduParser, short s) throws IOException {
        if (apduParser == null) {
            apduParser = this.statusWordParser;
        }
        return transceive(bArr, apduParser.parse(Action.COMMAND, bArr, s).toBundle(), apduParser, s);
    }

    private NfcMessage transceive(byte[] bArr, Bundle bundle, ApduParser apduParser, short s) throws IOException {
        this.localBroadcastManager.sendBroadcast(new Intent(NfcMessage.BROADCAST_NFC_MESSAGE).putExtras(bundle));
        NfcMessage parse = apduParser.parse(Action.RESPONSE, this.isoDep.transceive(bArr), s);
        this.localBroadcastManager.sendBroadcast(new Intent(NfcMessage.BROADCAST_NFC_MESSAGE).putExtras(parse.toBundle()));
        return parse;
    }
}
