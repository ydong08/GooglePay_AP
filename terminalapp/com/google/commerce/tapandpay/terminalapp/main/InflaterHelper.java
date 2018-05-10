package com.google.commerce.tapandpay.terminalapp.main;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.util.Hex;

public class InflaterHelper {
    private InflaterHelper() {
    }

    public static void setText(ViewGroup viewGroup, int i, String str) {
        ((TextView) viewGroup.findViewById(i)).setText(str);
    }

    public static void setText(ViewGroup viewGroup, int i, int i2) {
        ((TextView) viewGroup.findViewById(i)).setText(Integer.valueOf(i2).toString());
    }

    public static void setText(ViewGroup viewGroup, int i, long j) {
        ((TextView) viewGroup.findViewById(i)).setText(Long.valueOf(j).toString());
    }

    public static void setText(ViewGroup viewGroup, int i, byte[] bArr) {
        setText(viewGroup, i, Hex.encodeUpper(bArr));
    }

    public static void setText(ViewGroup viewGroup, int i, boolean z) {
        setText(viewGroup, i, z ? "true" : "false");
    }

    public static void setStatusText(Context context, ViewGroup viewGroup, int i, StatusWord statusWord) {
        String localizedStatusWordMessage = NfcMessage.getLocalizedStatusWordMessage(context, statusWord);
        setText(viewGroup, i, context.getString(R.string.status_word, new Object[]{localizedStatusWordMessage, Hex.encodeUpper(statusWord.toBytes())}));
    }

    public static void setVisibility(ViewGroup viewGroup, int i, int i2) {
        viewGroup.findViewById(i).setVisibility(i2);
    }
}
