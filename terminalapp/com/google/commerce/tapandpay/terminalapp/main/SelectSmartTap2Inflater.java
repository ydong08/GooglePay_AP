package com.google.commerce.tapandpay.terminalapp.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.smarttap.Version2.ParsedNfcSelectSmartTap2;
import com.google.android.libraries.commerce.hce.util.Hex;

public class SelectSmartTap2Inflater {
    private SelectSmartTap2Inflater() {
    }

    public static View getView(Context context, NfcMessage nfcMessage) {
        ParsedNfcSelectSmartTap2 parsedNfcSelectSmartTap2 = (ParsedNfcSelectSmartTap2) nfcMessage.getParsedNfc();
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.parsed_select_smarttap_2, null);
        InflaterHelper.setText(viewGroup, R.id.min_version, Hex.encodeUpper(parsedNfcSelectSmartTap2.getMinVersion()));
        InflaterHelper.setText(viewGroup, R.id.max_version, Hex.encodeUpper(parsedNfcSelectSmartTap2.getMaxVersion()));
        InflaterHelper.setText(viewGroup, R.id.handset_nonce, Hex.encodeUpper(parsedNfcSelectSmartTap2.getHandsetNonce()));
        return viewGroup;
    }
}
