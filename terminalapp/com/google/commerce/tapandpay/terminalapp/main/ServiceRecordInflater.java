package com.google.commerce.tapandpay.terminalapp.main;

import android.content.Context;
import android.nfc.NdefRecord;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.smarttap.Version2.ParsedRecordsNfc;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;

public class ServiceRecordInflater {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();

    private ServiceRecordInflater() {
    }

    public static View getView(Context context, NfcMessage nfcMessage, ServiceObjectConverter serviceObjectConverter) {
        ParsedRecordsNfc parsedRecordsNfc = (ParsedRecordsNfc) nfcMessage.getParsedNfc();
        LayoutInflater from = LayoutInflater.from(context);
        ViewGroup viewGroup = (ViewGroup) from.inflate(R.layout.service_records_nfc, null);
        short version = parsedRecordsNfc.getVersion();
        for (NdefRecord convert : parsedRecordsNfc.getRecords()) {
            try {
                for (ServiceObject addServiceView : serviceObjectConverter.convert(convert, version)) {
                    ServiceRecords.addServiceView(context, from, viewGroup, addServiceView);
                }
            } catch (Throwable e) {
                LOG.w(e, "Caught exception while attempting to convert NDEF record.", new Object[0]);
            }
        }
        return viewGroup;
    }
}
