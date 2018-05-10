package com.google.commerce.tapandpay.terminalapp.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.ParsedNfcError;
import com.google.common.base.Throwables;

public class ErrorInflater {
    private ErrorInflater() {
    }

    public static View getView(Context context, NfcMessage nfcMessage) {
        ParsedNfcError parsedNfcError = (ParsedNfcError) nfcMessage.getParsedNfc();
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.parsed_values_error, null);
        InflaterHelper.setText(viewGroup, R.id.error_summary, parsedNfcError.getSummary());
        if (parsedNfcError.getMessage() != null) {
            InflaterHelper.setText(viewGroup, R.id.error_message, parsedNfcError.getMessage());
        } else {
            InflaterHelper.setVisibility(viewGroup, R.id.error_message, 8);
        }
        return viewGroup;
    }

    public static View getView(Context context, int i, Throwable th) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.parsed_values_error, null);
        InflaterHelper.setText(viewGroup, R.id.error_summary, context.getString(i));
        InflaterHelper.setText(viewGroup, R.id.error_message, Throwables.getStackTraceAsString(th));
        return viewGroup;
    }
}
