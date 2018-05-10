package com.google.commerce.tapandpay.terminalapp.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.nfc.Transceiver.ParsedNfcSelect;
import com.google.android.libraries.commerce.hce.util.Hex;

public class SelectInflater {
    private SelectInflater() {
    }

    public static View getView(Context context, NfcMessage nfcMessage) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.parsed_select, null);
        InflaterHelper.setText(viewGroup, R.id.aid, Hex.encodeUpper(((ParsedNfcSelect) nfcMessage.getParsedNfc()).getAid()));
        return viewGroup;
    }
}
