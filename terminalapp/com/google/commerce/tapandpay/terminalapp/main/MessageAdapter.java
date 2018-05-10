package com.google.commerce.tapandpay.terminalapp.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter;
import com.google.android.libraries.commerce.hce.terminal.nfc.NdefParser.ParsedNdefNfc;
import com.google.android.libraries.commerce.hce.terminal.nfc.NdefParser.PartialNdefNfc;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.ParsedNfc;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.ParsedNfcError;
import com.google.android.libraries.commerce.hce.terminal.nfc.StatusWordParser.ParsedNfcStatusWord;
import com.google.android.libraries.commerce.hce.terminal.nfc.TlvParser.ParsedTlvNfc;
import com.google.android.libraries.commerce.hce.terminal.nfc.Transceiver.ParsedNfcSelect;
import com.google.android.libraries.commerce.hce.terminal.smarttap.Version2.ParsedNfcSelectSmartTap2;
import com.google.android.libraries.commerce.hce.terminal.smarttap.Version2.ParsedRecordsNfc;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Throwables;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import javax.inject.Inject;

public class MessageAdapter extends ArrayAdapter<Message> {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    @Inject
    ServiceObjectConverter converter;
    private final ArrayList<Message> messages;

    private MessageAdapter(Context context, ArrayList<Message> arrayList) {
        super(context, R.layout.nfc_message_list_item, arrayList);
        ((InjectedApplication) context.getApplicationContext()).inject(this);
        this.messages = arrayList;
    }

    public static MessageAdapter create(Context context) {
        return new MessageAdapter(context, new ArrayList());
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate;
        if (view == null) {
            inflate = LayoutInflater.from(getContext()).inflate(R.layout.nfc_message_list_item, viewGroup, false);
        } else {
            inflate = view;
        }
        Message message = (Message) getItem(i);
        if (message.getNfcMessage().isPresent()) {
            setNfcMessage((ViewGroup) inflate, (NfcMessage) message.getNfcMessage().get());
        } else {
            setException((ViewGroup) inflate, (Exception) message.getException().get());
        }
        return inflate;
    }

    public void add(NfcMessage nfcMessage) {
        FormattingLogger formattingLogger = LOG;
        String valueOf = String.valueOf(nfcMessage);
        formattingLogger.i(new StringBuilder(String.valueOf(valueOf).length() + 13).append("NFC Message: ").append(valueOf).toString(), new Object[0]);
        add(new Message(nfcMessage));
    }

    public void add(Exception exception) {
        FormattingLogger formattingLogger = LOG;
        String valueOf = String.valueOf(exception);
        formattingLogger.i(new StringBuilder(String.valueOf(valueOf).length() + 11).append("Exception: ").append(valueOf).toString(), new Object[0]);
        add(new Message(exception));
    }

    public ArrayList<String> getMessages() {
        LayoutInflater from = LayoutInflater.from(getContext());
        ArrayList<String> arrayList = new ArrayList();
        Iterator it = this.messages.iterator();
        while (it.hasNext()) {
            Message message = (Message) it.next();
            arrayList.add(">>>>>>>>>>");
            View inflate = from.inflate(R.layout.nfc_message_list_item, null, false);
            if (message.getNfcMessage().isPresent()) {
                setNfcMessage((ViewGroup) inflate, (NfcMessage) message.getNfcMessage().get());
            } else {
                setException((ViewGroup) inflate, (Exception) message.getException().get());
            }
            arrayList.addAll(getSerializedView((ViewGroup) inflate));
            arrayList.add("<<<<<<<<<<");
        }
        return arrayList;
    }

    private ArrayList<String> getSerializedView(ViewGroup viewGroup) {
        ArrayList<String> arrayList = new ArrayList();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                arrayList.addAll(getSerializedView((ViewGroup) viewGroup.getChildAt(i)));
            } else if (viewGroup.getChildAt(i) instanceof TextView) {
                arrayList.add(((TextView) viewGroup.getChildAt(i)).getText().toString());
            }
        }
        return arrayList;
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return Objects.equals(this.messages, ((MessageAdapter) obj).messages);
    }

    public int hashCode() {
        return this.messages.hashCode();
    }

    private void setNfcMessage(ViewGroup viewGroup, NfcMessage nfcMessage) {
        View view;
        Context context = getContext();
        byte[] value = nfcMessage.getValue();
        String string = context.getString(R.string.payload_size, new Object[]{Integer.valueOf(value.length)});
        ParsedNfc parsedNfc = nfcMessage.getParsedNfc();
        InflaterHelper.setText(viewGroup, R.id.title, parsedNfc.getTitle());
        InflaterHelper.setText(viewGroup, R.id.payload, value);
        InflaterHelper.setText(viewGroup, R.id.size, string);
        InflaterHelper.setVisibility(viewGroup, R.id.message, 8);
        if (parsedNfc instanceof ParsedNfcError) {
            view = ErrorInflater.getView(context, nfcMessage);
        } else if (parsedNfc instanceof ParsedNfcStatusWord) {
            view = setParsedNfcView(context, nfcMessage);
        } else if (parsedNfc instanceof ParsedNfcSelect) {
            view = SelectInflater.getView(context, nfcMessage);
        } else if (parsedNfc instanceof ParsedNfcSelectSmartTap2) {
            view = SelectSmartTap2Inflater.getView(context, nfcMessage);
        } else if (parsedNfc instanceof ParsedTlvNfc) {
            view = TlvInflater.getView(context, nfcMessage);
        } else if (parsedNfc instanceof ParsedNdefNfc) {
            view = NdefInflater.getView(context, nfcMessage, this.converter);
        } else if (parsedNfc instanceof PartialNdefNfc) {
            view = NdefChunkInflater.getView(context, nfcMessage);
        } else if (parsedNfc instanceof ParsedRecordsNfc) {
            InflaterHelper.setVisibility(viewGroup, R.id.payload, 8);
            InflaterHelper.setVisibility(viewGroup, R.id.size, 8);
            view = ServiceRecordInflater.getView(context, nfcMessage, this.converter);
        } else {
            LOG.w("Unknown ParsedNfc implementation, do not know how to render: %s", parsedNfc.getClass().getSimpleName());
            view = setParsedNfcView(context, nfcMessage);
        }
        ViewGroup viewGroup2 = (ViewGroup) viewGroup.findViewById(R.id.details);
        viewGroup2.removeAllViews();
        viewGroup2.addView(view);
    }

    private static ViewGroup setParsedNfcView(Context context, NfcMessage nfcMessage) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.parsed_status_word, null);
        InflaterHelper.setStatusText(context, viewGroup, R.id.status_word, nfcMessage.getParsedNfc().getStatusWord());
        return viewGroup;
    }

    private static void setException(ViewGroup viewGroup, Exception exception) {
        InflaterHelper.setText(viewGroup, R.id.title, exception.getMessage());
        InflaterHelper.setVisibility(viewGroup, R.id.payload, 8);
        InflaterHelper.setVisibility(viewGroup, R.id.size, 8);
        InflaterHelper.setText(viewGroup, R.id.message, Throwables.getStackTraceAsString(exception));
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Messages", this.messages);
        return bundle;
    }

    public static MessageAdapter fromBundle(Context context, Bundle bundle) {
        return new MessageAdapter(context, bundle.getParcelableArrayList("Messages"));
    }
}
