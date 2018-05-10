package com.google.commerce.tapandpay.terminalapp.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.AidInfo;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.Ose;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.Ose.OseResponse;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.SmartTap2ProprietaryData;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.TransactionalDetails;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlv;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.nfc.TlvParser.ParsedTlvNfc;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Optional;
import java.util.Iterator;

public class TlvInflater {
    private static final FormattingLogger logger = FormattingLoggers.newContextLogger();

    private TlvInflater() {
    }

    public static View getView(Context context, NfcMessage nfcMessage) {
        ParsedTlvNfc parsedTlvNfc = (ParsedTlvNfc) nfcMessage.getParsedNfc();
        LayoutInflater from = LayoutInflater.from(context);
        ViewGroup viewGroup = (ViewGroup) from.inflate(R.layout.parsed_tlv_nfc, null);
        try {
            Iterator it = parsedTlvNfc.getTlvs().values().iterator();
            while (it.hasNext()) {
                BasicTlv basicTlv = (BasicTlv) it.next();
                switch (basicTlv.getTag()) {
                    case 111:
                        for (BasicTlv basicTlv2 : basicTlv2.asConstructedTlv().getChildren()) {
                            if (basicTlv2.getTag() != 80) {
                                if (basicTlv2.getTag() == 132) {
                                    break;
                                }
                            }
                            addOseRecord(from, viewGroup, Ose.parse(nfcMessage.getValue()));
                            break;
                        }
                        continue;
                    default:
                        break;
                }
            }
            InflaterHelper.setStatusText(context, viewGroup, R.id.status_word, parsedTlvNfc.getStatusWord());
            return viewGroup;
        } catch (Throwable e) {
            logger.w(e, "Failed to parse TLV response view", new Object[0]);
            return ErrorInflater.getView(context, R.string.tlv_parse_error, e);
        }
    }

    private static void addOseRecord(LayoutInflater layoutInflater, ViewGroup viewGroup, OseResponse oseResponse) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.tlv_ose_response_view, viewGroup, false);
        if (oseResponse.mobileDeviceNonce().isPresent()) {
            InflaterHelper.setText(viewGroup2, R.id.master_nonce, ((ByteArrayWrapper) oseResponse.mobileDeviceNonce().get()).array());
        } else {
            InflaterHelper.setText(viewGroup2, R.id.master_nonce, viewGroup.getResources().getString(R.string.not_present));
        }
        if (oseResponse.transactionalDetails().isPresent()) {
            LinearLayout linearLayout = (LinearLayout) viewGroup2.findViewById(R.id.transactional_details_layout);
            TransactionalDetails transactionalDetails = (TransactionalDetails) oseResponse.transactionalDetails().get();
            setTextFromBool(viewGroup2, R.id.generic_key_auth, transactionalDetails.genericKeyAuth());
            setTextFromBool(viewGroup2, R.id.key_generation, transactionalDetails.ecies());
            setTextFromBool(viewGroup2, R.id.payment_enabled, transactionalDetails.paymentEnabled());
            setTextFromBool(viewGroup2, R.id.payment_requested, transactionalDetails.paymentRequested());
            setTextFromBool(viewGroup2, R.id.vas_enabled, transactionalDetails.vasEnabled());
            setTextFromBool(viewGroup2, R.id.vas_requested, transactionalDetails.vasRequested());
            linearLayout.setVisibility(0);
        }
        for (AidInfo aidInfo : oseResponse.aidInfos()) {
            ViewGroup viewGroup3 = (ViewGroup) layoutInflater.inflate(R.layout.tlv_aid_info_view, viewGroup2, false);
            InflaterHelper.setText(viewGroup3, R.id.aid, aidInfo.aid().toString());
            InflaterHelper.setText(viewGroup3, R.id.priority, aidInfo.priority());
            if (aidInfo.proprietaryDataOptional().isPresent()) {
                String string;
                SmartTap2ProprietaryData smartTap2ProprietaryData = (SmartTap2ProprietaryData) aidInfo.proprietaryDataOptional().get();
                ViewGroup viewGroup4 = (ViewGroup) layoutInflater.inflate(R.layout.tlv_ose_proprietary_data_view, viewGroup3, false);
                setTextIfShortPresent(viewGroup4, R.id.min_version, smartTap2ProprietaryData.minVersion());
                setTextIfShortPresent(viewGroup4, R.id.max_version, smartTap2ProprietaryData.maxVersion());
                setTextIfByteArrayPresent(viewGroup4, R.id.handset_nonce, smartTap2ProprietaryData.mobileDeviceNonce());
                if (smartTap2ProprietaryData.supportsSkippingSelect()) {
                    string = viewGroup.getResources().getString(R.string.yes);
                } else {
                    string = viewGroup.getResources().getString(R.string.no);
                }
                InflaterHelper.setText(viewGroup4, R.id.supports_skipping_select, string);
                viewGroup3.addView(viewGroup4);
            }
            viewGroup2.addView(viewGroup3);
        }
        viewGroup.addView(viewGroup2);
    }

    private static void setTextIfShortPresent(ViewGroup viewGroup, int i, Optional<Short> optional) {
        if (optional.isPresent()) {
            InflaterHelper.setText(viewGroup, i, ((Short) optional.get()).shortValue());
        } else {
            InflaterHelper.setText(viewGroup, i, viewGroup.getResources().getString(R.string.not_present));
        }
    }

    private static void setTextIfByteArrayPresent(ViewGroup viewGroup, int i, Optional<ByteArrayWrapper> optional) {
        if (optional.isPresent()) {
            InflaterHelper.setText(viewGroup, i, ((ByteArrayWrapper) optional.get()).array());
        } else {
            InflaterHelper.setText(viewGroup, i, viewGroup.getResources().getString(R.string.not_present));
        }
    }

    private static void setTextFromBool(ViewGroup viewGroup, int i, boolean z) {
        InflaterHelper.setText(viewGroup, i, viewGroup.getResources().getString(z ? R.string.yes : R.string.no));
    }
}
