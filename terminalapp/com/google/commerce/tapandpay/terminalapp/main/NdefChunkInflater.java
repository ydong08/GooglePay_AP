package com.google.commerce.tapandpay.terminalapp.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.libraries.commerce.hce.terminal.nfc.NdefParser.PartialNdefNfc;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.util.Hex;

public class NdefChunkInflater {
    private NdefChunkInflater() {
    }

    public static View getView(Context context, NfcMessage nfcMessage) {
        PartialNdefNfc partialNdefNfc = (PartialNdefNfc) nfcMessage.getParsedNfc();
        return getResponseView(context, partialNdefNfc.getBytes(), nfcMessage, partialNdefNfc.getVersion());
    }

    public static View getResponseView(Context context, byte[] bArr, NfcMessage nfcMessage, short s) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.partial_ndef_response_nfc, null);
        ((TextView) viewGroup.findViewById(R.id.raw_bytes)).setText(Hex.encodeUpper(bArr));
        InflaterHelper.setStatusText(context, viewGroup, R.id.status_word, nfcMessage.getParsedNfc().getStatusWord());
        return viewGroup;
    }
}
