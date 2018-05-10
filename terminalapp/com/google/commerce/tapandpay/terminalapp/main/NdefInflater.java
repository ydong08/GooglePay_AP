package com.google.commerce.tapandpay.terminalapp.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetAdditionalSmartTapDataRequest;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetSmartTapDataRequest;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetSmartTapDataResponse;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetSmartTapDataResponse.RecordBundle;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NegotiateSmartTapRequest;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NewService;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NewService.ServiceType;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.PushSmartTapDataRequest;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceStatus;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceStatus.Operation;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceStatus.Usage;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionResponse;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.ndef.Text;
import com.google.android.libraries.commerce.hce.terminal.nfc.NdefParser.ParsedNdefNfc;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.util.Arrays;

public class NdefInflater {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();

    private NdefInflater() {
    }

    public static View getView(Context context, NfcMessage nfcMessage, ServiceObjectConverter serviceObjectConverter) {
        ParsedNdefNfc parsedNdefNfc = (ParsedNdefNfc) nfcMessage.getParsedNfc();
        short version = parsedNdefNfc.getVersion();
        byte[] type = NdefRecords.getType(parsedNdefNfc.getRecord(), version);
        if (Arrays.equals(type, SmartTap2Values.NEGOTIATE_NDEF_TYPE) || Arrays.equals(type, SmartTap2Values.SERVICE_REQUEST_NDEF_TYPE) || Arrays.equals(type, SmartTap2Values.ADDITIONAL_SERVICE_NDEF_TYPE) || Arrays.equals(type, SmartTap2Values.PUSH_SERVICE_NDEF_TYPE)) {
            return getRequestView(context, type, nfcMessage);
        }
        if (Arrays.equals(type, SmartTap2Values.NEGOTIATE_RESPONSE_NDEF_TYPE) || Arrays.equals(type, SmartTap2Values.SERVICE_RESPONSE_NDEF_TYPE) || Arrays.equals(type, SmartTap2Values.PUSH_SERVICE_RESPONSE_NDEF_TYPE)) {
            return getResponseView(context, type, nfcMessage, serviceObjectConverter, version);
        }
        LOG.w("Unrecognized NDEF record type: %s.", SmartTap2Values.getNdefType(type));
        return new View(context);
    }

    public static View getRequestView(Context context, byte[] bArr, NfcMessage nfcMessage) {
        LayoutInflater from = LayoutInflater.from(context);
        ViewGroup viewGroup = (ViewGroup) from.inflate(R.layout.parsed_ndef_request_nfc, null);
        ViewGroup viewGroup2 = (ViewGroup) viewGroup.findViewById(R.id.ndef_record);
        byte[] value = nfcMessage.getValue();
        try {
            int version;
            if (Arrays.equals(bArr, SmartTap2Values.NEGOTIATE_NDEF_TYPE)) {
                NegotiateSmartTapRequest parse = NegotiateSmartTapRequest.parse(value);
                addNdefRecord(from, viewGroup2, parse);
                version = parse.getVersion();
            } else if (Arrays.equals(bArr, SmartTap2Values.SERVICE_REQUEST_NDEF_TYPE)) {
                GetSmartTapDataRequest parse2 = GetSmartTapDataRequest.parse(value);
                addNdefRecord(context, from, viewGroup2, parse2);
                version = parse2.getVersion();
            } else if (Arrays.equals(bArr, SmartTap2Values.ADDITIONAL_SERVICE_NDEF_TYPE)) {
                GetAdditionalSmartTapDataRequest parse3 = GetAdditionalSmartTapDataRequest.parse(value);
                addNdefRecord(from, viewGroup2, parse3);
                version = parse3.getVersion();
            } else if (Arrays.equals(bArr, SmartTap2Values.PUSH_SERVICE_NDEF_TYPE)) {
                PushSmartTapDataRequest parse4 = PushSmartTapDataRequest.parse(value);
                addNdefRecord(context, from, viewGroup2, parse4);
                version = parse4.getVersion();
            } else {
                LOG.e("Unrecognized request NDEF record type: %s.", SmartTap2Values.getNdefType(bArr));
                version = 0;
            }
            InflaterHelper.setText(viewGroup, R.id.ndef_version, version);
            return viewGroup;
        } catch (Throwable e) {
            LOG.w(e, "Failed to parse NDEF request view", new Object[0]);
            return ErrorInflater.getView(context, R.string.ndef_request_parse_error, e);
        }
    }

    public static View getResponseView(Context context, byte[] bArr, NfcMessage nfcMessage, ServiceObjectConverter serviceObjectConverter, short s) {
        LayoutInflater from = LayoutInflater.from(context);
        ViewGroup viewGroup = (ViewGroup) from.inflate(R.layout.parsed_ndef_response_nfc, null);
        ViewGroup viewGroup2 = (ViewGroup) viewGroup.findViewById(R.id.ndef_record);
        InflaterHelper.setStatusText(context, viewGroup, R.id.status_word, nfcMessage.getParsedNfc().getStatusWord());
        byte[] value = nfcMessage.getValue();
        try {
            if (Arrays.equals(bArr, SmartTap2Values.NEGOTIATE_RESPONSE_NDEF_TYPE)) {
                addSessionNdefRecord(from, viewGroup2, SessionResponse.parse(value, SmartTap2Values.NEGOTIATE_RESPONSE_NDEF_TYPE, s));
                return viewGroup;
            } else if (Arrays.equals(bArr, SmartTap2Values.SERVICE_RESPONSE_NDEF_TYPE)) {
                addNdefRecord(context, from, viewGroup2, GetSmartTapDataResponse.parse(value, serviceObjectConverter, s));
                return viewGroup;
            } else if (Arrays.equals(bArr, SmartTap2Values.PUSH_SERVICE_RESPONSE_NDEF_TYPE)) {
                addSessionNdefRecord(from, viewGroup2, SessionResponse.parse(value, SmartTap2Values.PUSH_SERVICE_RESPONSE_NDEF_TYPE, s));
                return viewGroup;
            } else {
                LOG.e("Unrecognized response NDEF record type: %s.", SmartTap2Values.getNdefType(bArr));
                return viewGroup;
            }
        } catch (Throwable e) {
            LOG.w(e, "Failed to parse NDEF response view", new Object[0]);
            return ErrorInflater.getView(context, R.string.ndef_response_parse_error, e);
        }
    }

    private static void addNdefRecord(LayoutInflater layoutInflater, ViewGroup viewGroup, NegotiateSmartTapRequest negotiateSmartTapRequest) {
        addSessionNdefRecord(layoutInflater, viewGroup, negotiateSmartTapRequest);
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.ndef_crypto_params_view, viewGroup, false);
        InflaterHelper.setText(viewGroup2, R.id.terminal_nonce, negotiateSmartTapRequest.getTerminalNonce());
        InflaterHelper.setText(viewGroup2, R.id.merchant_ephemeral_public_key, negotiateSmartTapRequest.getEphemeralPublicKey());
        InflaterHelper.setText(viewGroup2, R.id.authentication_type, viewGroup.getResources().getString(negotiateSmartTapRequest.getShouldLiveAuthenticate() ? R.string.live_auth : R.string.presigned_auth));
        InflaterHelper.setText(viewGroup2, R.id.long_term_key_version, negotiateSmartTapRequest.getLongTermKeyVersion());
        InflaterHelper.setText(viewGroup2, R.id.signature, negotiateSmartTapRequest.getSignature());
        InflaterHelper.setText(viewGroup2, R.id.merchant_id, negotiateSmartTapRequest.getMerchantId());
        viewGroup.addView(viewGroup2);
    }

    private static void addNdefRecord(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup, GetSmartTapDataRequest getSmartTapDataRequest) {
        addSessionNdefRecord(layoutInflater, viewGroup, getSmartTapDataRequest);
        ServiceRecords.addMerchantInfo(context, layoutInflater, viewGroup, getSmartTapDataRequest.getMerchantInfo());
        ServiceRecords.addServiceObjects(context, layoutInflater, viewGroup, getSmartTapDataRequest.getMerchantInfo().getRequestedServiceObjects());
    }

    private static void addNdefRecord(LayoutInflater layoutInflater, ViewGroup viewGroup, GetAdditionalSmartTapDataRequest getAdditionalSmartTapDataRequest) {
        addSessionNdefRecord(layoutInflater, viewGroup, getAdditionalSmartTapDataRequest);
    }

    private static void addNdefRecord(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup, PushSmartTapDataRequest pushSmartTapDataRequest) {
        addSessionNdefRecord(layoutInflater, viewGroup, pushSmartTapDataRequest);
        for (ServiceStatus addServiceStatus : pushSmartTapDataRequest.getServiceStatuses()) {
            addServiceStatus(context, layoutInflater, viewGroup, addServiceStatus);
        }
        for (NewService addNewService : pushSmartTapDataRequest.getNewServices()) {
            addNewService(context, layoutInflater, viewGroup, addNewService);
        }
    }

    private static void addNdefRecord(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup, GetSmartTapDataResponse getSmartTapDataResponse) {
        addSessionNdefRecord(layoutInflater, viewGroup, getSmartTapDataResponse);
        for (ServiceObject addServiceView : getSmartTapDataResponse.getServiceObjects()) {
            ServiceRecords.addServiceView(context, layoutInflater, viewGroup, addServiceView);
        }
        for (RecordBundle recordBundle : getSmartTapDataResponse.getRecordBundles()) {
            ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.ndef_record_bundle_view, viewGroup, false);
            InflaterHelper.setText(viewGroup2, R.id.encrypted, recordBundle.isEncrypted());
            InflaterHelper.setText(viewGroup2, R.id.compressed, recordBundle.isCompressed());
            InflaterHelper.setText(viewGroup2, R.id.record_bundle_size, Integer.valueOf(recordBundle.getPayload().length).intValue());
            viewGroup.addView(viewGroup2);
        }
    }

    private static void addSessionNdefRecord(LayoutInflater layoutInflater, ViewGroup viewGroup, Session session) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.ndef_session_view, viewGroup, false);
        InflaterHelper.setText(viewGroup2, R.id.session_id, session.getSessionId());
        InflaterHelper.setText(viewGroup2, R.id.sequence_number, Integer.valueOf(session.getSequenceNumber()).intValue());
        InflaterHelper.setText(viewGroup2, R.id.status, session.getStatus().name());
        viewGroup.addView(viewGroup2);
    }

    private static void addServiceStatus(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup, ServiceStatus serviceStatus) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.ndef_push_status_view, viewGroup, false);
        InflaterHelper.setText(viewGroup2, R.id.service_id, serviceStatus.getId());
        String serviceUsageString = getServiceUsageString(context, serviceStatus.getUsage());
        String serviceOperationString = getServiceOperationString(context, serviceStatus.getOperation());
        InflaterHelper.setText(viewGroup2, R.id.service_usage_status, serviceUsageString);
        InflaterHelper.setText(viewGroup2, R.id.service_update_operation, serviceOperationString);
        if (serviceStatus.getTitle().isPresent()) {
            InflaterHelper.setText(viewGroup2, R.id.service_usage_title, ((Text) serviceStatus.getTitle().get()).toString());
        } else {
            InflaterHelper.setVisibility(viewGroup2, R.id.service_usage_title_layout, 8);
        }
        if (serviceStatus.getDescription().isPresent()) {
            InflaterHelper.setText(viewGroup2, R.id.service_usage_description, ((Text) serviceStatus.getDescription().get()).toString());
        } else {
            InflaterHelper.setVisibility(viewGroup2, R.id.service_usage_description_layout, 8);
        }
        if (serviceStatus.getPayload() == null || serviceStatus.getPayload().length == 0) {
            InflaterHelper.setVisibility(viewGroup2, R.id.service_update_payload_layout, 8);
        } else {
            InflaterHelper.setText(viewGroup2, R.id.service_update_payload, serviceStatus.getPayload());
        }
        viewGroup.addView(viewGroup2);
    }

    private static void addNewService(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup, NewService newService) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.ndef_push_service_view, viewGroup, false);
        InflaterHelper.setText(viewGroup2, R.id.new_service_type, getServiceTypeString(context, newService.getServiceType()));
        InflaterHelper.setText(viewGroup2, R.id.new_service_title, newService.getTitle().toString());
        InflaterHelper.setText(viewGroup2, R.id.new_service_uri, newService.getUri().toString());
        viewGroup.addView(viewGroup2);
    }

    private static String getServiceUsageString(Context context, Usage usage) {
        switch (usage) {
            case UNSPECIFIED:
                return context.getString(R.string.service_usage_unspecified);
            case SUCCESS:
                return context.getString(R.string.service_usage_success);
            case INVALID_FORMAT:
                return context.getString(R.string.service_usage_invalid_format);
            case INVALID_VALUE:
                return context.getString(R.string.service_usage_invalid_value);
            default:
                LOG.w("Unknown new service usage: %s", usage);
                return usage.name();
        }
    }

    private static String getServiceOperationString(Context context, Operation operation) {
        switch (operation) {
            case NO_OP:
                return context.getString(R.string.service_operation_no_op);
            case REMOVE_SERVICE:
                return context.getString(R.string.service_operation_remove_service);
            case SET_BALANCE:
                return context.getString(R.string.service_operation_set_balance);
            case ADD_BALANCE:
                return context.getString(R.string.service_operation_add_balance);
            case SUBTRACT_BALANCE:
                return context.getString(R.string.service_operation_subtract_balance);
            case FREE:
                return context.getString(R.string.service_operation_free);
            default:
                LOG.w("Unknown new service operation: %s", operation);
                return operation.name();
        }
    }

    private static String getServiceTypeString(Context context, ServiceType serviceType) {
        switch (serviceType) {
            case UNSPECIFIED:
                return context.getString(R.string.new_service_type_unspecified);
            case VALUABLE:
                return context.getString(R.string.new_service_type_valuable);
            case RECEIPT:
                return context.getString(R.string.new_service_type_receipt);
            case SURVEY:
                return context.getString(R.string.new_service_type_survey);
            case GOODS:
                return context.getString(R.string.new_service_type_goods);
            case SIGNUP:
                return context.getString(R.string.new_service_type_sign_up);
            default:
                LOG.w("Unknown new service type: %s", serviceType);
                return serviceType.name();
        }
    }
}
