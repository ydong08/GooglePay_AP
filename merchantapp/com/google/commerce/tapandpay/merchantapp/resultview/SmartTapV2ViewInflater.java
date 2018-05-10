package com.google.commerce.tapandpay.merchantapp.resultview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetSmartTapDataRequest;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.GetSmartTapDataResponse;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.MerchantCategory;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.MerchantInfo;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NegotiateSmartTapRequest;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NegotiateSmartTapResponse;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NewService;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NewService.ServiceType;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.PushSmartTapDataRequest;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Issuer;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Request;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Type;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceStatus;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceStatus.Operation;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceStatus.Usage;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionRequest;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionResponse;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.ndef.Format;
import com.google.android.libraries.commerce.hce.ndef.Primitive;
import com.google.android.libraries.commerce.hce.ndef.Text;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.merchantapp.common.ByteHelper;
import com.google.commerce.tapandpay.merchantapp.common.ByteHelper.ByteHelperException;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.result.Result;
import com.google.commerce.tapandpay.merchantapp.result.Result.CommandAndResponse;
import com.google.common.base.Strings;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;

public class SmartTapV2ViewInflater extends SmartTapViewInflater {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    @Inject
    ServiceObjectConverter serviceObjectConverter;

    public SmartTapV2ViewInflater(Context context, Result result) {
        super(context, result);
        ((InjectedApplication) context.getApplicationContext()).inject(this);
    }

    public void renderCommandAndResponse(CommandAndResponse commandAndResponse, ViewGroup viewGroup) {
        ByteHelperException byteHelperException;
        CommandType type = commandAndResponse.type();
        if (type == CommandType.SELECT_SMARTTAP_V2_0) {
            addHexView(type.commandTitle(getContext()), commandAndResponse.command(), viewGroup);
            addHexView(type.responseTitle(getContext(), commandAndResponse.isResponseError()), commandAndResponse.response(), viewGroup);
            addNonceView(commandAndResponse.response(), viewGroup);
            return;
        }
        short s;
        short s2 = (short) 0;
        addHexView(type.commandTitle(getContext()), commandAndResponse.command(), viewGroup);
        try {
            Session requestSession = getRequestSession(commandAndResponse);
            s2 = requestSession.getVersion();
            try {
                addSessionNdefView(requestSession, viewGroup);
                if (type == CommandType.GET_SMARTTAP_DATA) {
                    GetSmartTapDataRequest getSmartTapDataRequest = (GetSmartTapDataRequest) requestSession;
                    addMerchantNdefView(getSmartTapDataRequest.getMerchantInfo(), viewGroup);
                    addServiceListNdefTable(getSmartTapDataRequest.getMerchantInfo(), viewGroup);
                } else if (type == CommandType.NEGOTIATE_SMARTTAP) {
                    addCryptoParamsNdefView((NegotiateSmartTapRequest) requestSession, viewGroup);
                } else if (type == CommandType.PUSH_SMARTTAP_DATA) {
                    PushSmartTapDataRequest pushSmartTapDataRequest = (PushSmartTapDataRequest) requestSession;
                    for (ServiceStatus addServiceStatusNdefView : pushSmartTapDataRequest.getServiceStatuses()) {
                        addServiceStatusNdefView(addServiceStatusNdefView, viewGroup);
                    }
                    for (NewService addNewServiceView : pushSmartTapDataRequest.getNewServices()) {
                        addNewServiceView(addNewServiceView, viewGroup);
                    }
                }
                s = s2;
            } catch (ByteHelperException e) {
                byteHelperException = e;
                s = s2;
                addNdefErrorView(viewGroup, byteHelperException.getMessage());
                addHexView(type.responseTitle(getContext(), commandAndResponse.isResponseError()), commandAndResponse.response(), viewGroup);
                if (commandAndResponse.response() != null) {
                }
                addNdefErrorView(viewGroup, getContext().getResources().getString(R.string.no_response_apdu));
                return;
            }
        } catch (ByteHelperException e2) {
            byteHelperException = e2;
            s = s2;
            addNdefErrorView(viewGroup, byteHelperException.getMessage());
            addHexView(type.responseTitle(getContext(), commandAndResponse.isResponseError()), commandAndResponse.response(), viewGroup);
            if (commandAndResponse.response() != null) {
            }
            addNdefErrorView(viewGroup, getContext().getResources().getString(R.string.no_response_apdu));
            return;
        }
        addHexView(type.responseTitle(getContext(), commandAndResponse.isResponseError()), commandAndResponse.response(), viewGroup);
        try {
            if (commandAndResponse.response() != null || commandAndResponse.response().length == 0) {
                addNdefErrorView(viewGroup, getContext().getResources().getString(R.string.no_response_apdu));
                return;
            }
            Session responseSession = getResponseSession(commandAndResponse, s);
            addSessionNdefView(responseSession, viewGroup);
            if (type == CommandType.NEGOTIATE_SMARTTAP) {
                addHandsetEphemeralKeyRecord((NegotiateSmartTapResponse) responseSession, viewGroup);
            } else if (type == CommandType.GET_SMARTTAP_DATA || type == CommandType.GET_ADDITIONAL_SMARTTAP_DATA) {
                GetSmartTapDataResponse getSmartTapDataResponse = (GetSmartTapDataResponse) responseSession;
                if (getResult().encryptedValuables() != null) {
                    viewGroup.addView(inflate(R.layout.result_encrypted_indicator, viewGroup));
                    addServiceNdefView(new HashSet(getResult().encryptedValuables()), viewGroup);
                } else {
                    addServiceNdefView(getSmartTapDataResponse.getServiceObjects(), viewGroup);
                }
            }
            addStatusWordView(responseSession.getStatusWord(), viewGroup);
        } catch (ByteHelperException e22) {
            addNdefErrorView(viewGroup, e22.getMessage());
        }
    }

    private void addNonceView(byte[] bArr, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_nonce_view, viewGroup);
        try {
            setText(inflate, R.id.handset_nonce, ByteHelper.getHandsetNonce(bArr));
            viewGroup.addView(inflate);
        } catch (ByteHelperException e) {
            addNdefErrorView(viewGroup, e.getMessage());
        }
    }

    private void addHandsetEphemeralKeyRecord(NegotiateSmartTapResponse negotiateSmartTapResponse, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_ephemeral_key_view, viewGroup);
        if (negotiateSmartTapResponse.getHandsetEphemeralPublicKey() != null) {
            setText(inflate, R.id.handset_ephemeral_public_key, negotiateSmartTapResponse.getHandsetEphemeralPublicKey());
        } else {
            setText(inflate, R.id.handset_ephemeral_public_key, R.string.none);
        }
        viewGroup.addView(inflate);
    }

    private void addStatusWordView(StatusWord statusWord, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_status_word_view, viewGroup);
        setText(inflate, R.id.status_word, statusWord.toString());
        viewGroup.addView(inflate);
    }

    private void addNdefErrorView(ViewGroup viewGroup, String str) {
        View inflate = inflate(R.layout.error_text_view, viewGroup);
        setText(inflate, R.id.title, R.string.ndefs);
        setText(inflate, R.id.error_summary, R.string.ndef_error);
        setText(inflate, R.id.error_message, str);
        viewGroup.addView(inflate);
    }

    private void addServiceStatusNdefView(ServiceStatus serviceStatus, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_service_status_view, viewGroup);
        setText(inflate, R.id.service_id, serviceStatus.getId());
        String serviceUsageString = getServiceUsageString(getContext(), serviceStatus.getUsage());
        String serviceOperationString = getServiceOperationString(getContext(), serviceStatus.getOperation());
        setText(inflate, R.id.service_usage_status, serviceUsageString);
        setText(inflate, R.id.service_update_operation, serviceOperationString);
        if (serviceStatus.getTitle().isPresent()) {
            setText(inflate, R.id.service_usage_title, ((Text) serviceStatus.getTitle().get()).toString());
        } else {
            inflate.findViewById(R.id.service_usage_title_layout).setVisibility(8);
        }
        if (serviceStatus.getDescription().isPresent()) {
            setText(inflate, R.id.service_usage_description, ((Text) serviceStatus.getDescription().get()).toString());
        } else {
            inflate.findViewById(R.id.service_usage_description_layout).setVisibility(8);
        }
        if (serviceStatus.getPayload() == null || serviceStatus.getPayload().length == 0) {
            inflate.findViewById(R.id.service_update_payload_layout).setVisibility(8);
        } else {
            setText(inflate, R.id.service_update_payload, serviceStatus.getPayload());
        }
        viewGroup.addView(inflate);
    }

    private void addNewServiceView(NewService newService, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_new_service_view, viewGroup);
        setText(inflate, R.id.new_service_type, getNewServiceTypeString(getContext(), newService.getServiceType()));
        setText(inflate, R.id.new_service_title, newService.getTitle().toString());
        setText(inflate, R.id.new_service_uri, newService.getUri().toString());
        viewGroup.addView(inflate);
    }

    private void addCryptoParamsNdefView(NegotiateSmartTapRequest negotiateSmartTapRequest, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_crypto_params_view, viewGroup);
        setText(inflate, R.id.terminal_nonce, negotiateSmartTapRequest.getTerminalNonce());
        setText(inflate, R.id.live_authentication, negotiateSmartTapRequest.getShouldLiveAuthenticate() ? "true" : "false");
        setText(inflate, R.id.merchant_ephemeral_public_key, negotiateSmartTapRequest.getEphemeralPublicKey());
        setText(inflate, R.id.long_term_key_version, Integer.toString(negotiateSmartTapRequest.getLongTermKeyVersion()));
        setText(inflate, R.id.signature, negotiateSmartTapRequest.getSignature());
        setText(inflate, R.id.merchant_id, Long.toHexString(negotiateSmartTapRequest.getMerchantId()));
        viewGroup.addView(inflate);
    }

    private void addSessionNdefView(Session session, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_session_view, viewGroup);
        setText(inflate, R.id.session_id, session.getSessionId());
        setText(inflate, R.id.sequence_number, new byte[]{session.getSequenceNumber()});
        setText(inflate, R.id.status, getSessionStatusString(getContext(), session.getStatus()));
        viewGroup.addView(inflate);
    }

    private void addServiceNdefView(Set<ServiceObject> set, ViewGroup viewGroup) {
        for (ServiceObject serviceObject : set) {
            View inflate = inflate(R.layout.result_service_view, viewGroup);
            setText(inflate, R.id.service_id, serviceObject.serviceId());
            setText(inflate, R.id.type, getServiceTypeString(getContext(), serviceObject.type()));
            setValueWithFormat(inflate, R.id.service_number, R.id.service_number_format, serviceObject.serviceNumberId(), serviceObject.serviceNumberIdFormat());
            if (serviceObject.type() == Type.WALLET_CUSTOMER) {
                inflate.findViewById(R.id.issuer_layout).setVisibility(8);
            } else {
                setValueWithFormat(inflate, R.id.issuer_id, R.id.issuer_id_format, serviceObject.issuerId(), serviceObject.issuerFormat());
                setText(inflate, R.id.issuer, getIssuerString(getContext(), serviceObject.issuer()));
            }
            viewGroup.addView(inflate);
            if (serviceObject.type() == Type.GIFT_CARD) {
                addGiftCardView(serviceObject, viewGroup);
            } else if (serviceObject.type() == Type.CLOSED_LOOP_CARD) {
                addPlcView(serviceObject, viewGroup);
            } else if (serviceObject.type() == Type.WALLET_CUSTOMER) {
                addCustomerView(serviceObject, viewGroup);
            }
        }
    }

    private void addMerchantNdefView(MerchantInfo merchantInfo, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_merchant_info, viewGroup);
        setText(inflate, R.id.merchant_id, Long.valueOf(merchantInfo.getMerchantId()).toString());
        if (merchantInfo.getLocationId().isPresent()) {
            setText(inflate, R.id.store_location_id, ((Primitive) merchantInfo.getLocationId().get()).getPayload());
        }
        if (merchantInfo.getTerminalId().isPresent()) {
            setText(inflate, R.id.terminal_id, ((Primitive) merchantInfo.getTerminalId().get()).getPayload());
        }
        if (merchantInfo.getName().isPresent()) {
            Text text = (Text) merchantInfo.getName().get();
            if (text != null) {
                setText(inflate, R.id.merchant_name, text.getText());
            }
        }
        if (merchantInfo.getCategory() != MerchantCategory.UNKNOWN) {
            setText(inflate, R.id.merchant_category, getCategoryString(getContext(), merchantInfo.getCategory()));
        }
        viewGroup.addView(inflate);
    }

    private void addServiceListNdefTable(MerchantInfo merchantInfo, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_service_list_table, viewGroup);
        TableLayout tableLayout = (TableLayout) inflate.findViewById(R.id.table);
        for (Request request : merchantInfo.getRequestedServiceObjects()) {
            View inflate2 = inflate(R.layout.result_service_list_row, tableLayout);
            Context context = getContext();
            String serviceTypeString = getServiceTypeString(getContext(), request.getType());
            String issuerString = getIssuerString(context, request.getIssuer());
            String formatString = getFormatString(context, request.getFormat());
            setText(inflate2, R.id.service_type, serviceTypeString);
            setText(inflate2, R.id.service_issuer, issuerString);
            setText(inflate2, R.id.expected_format, formatString);
            tableLayout.addView(inflate2);
        }
        viewGroup.addView(inflate);
    }

    private void addGiftCardView(ServiceObject serviceObject, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_giftcard_view, viewGroup);
        setValueWithFormat(inflate, R.id.pin, R.id.pin_format, serviceObject.pin(), serviceObject.pinFormat());
        viewGroup.addView(inflate);
    }

    private void addPlcView(ServiceObject serviceObject, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_plc_view, viewGroup);
        setValueWithFormat(inflate, R.id.cvc, R.id.cvc_format, serviceObject.cvc(), serviceObject.cvcFormat());
        setValueWithFormat(inflate, R.id.expiration, R.id.expiration_format, serviceObject.expiration(), serviceObject.expirationFormat());
        viewGroup.addView(inflate);
    }

    private void addCustomerView(ServiceObject serviceObject, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_customer_view, viewGroup);
        String preferedLanguage = serviceObject.preferedLanguage();
        if (Strings.isNullOrEmpty(preferedLanguage)) {
            inflate.findViewById(R.id.preferred_language_layout).setVisibility(8);
        } else {
            setText(inflate, R.id.preferred_language, preferedLanguage);
        }
        byte[] tapId = serviceObject.tapId();
        if (tapId != null) {
            setText(inflate, R.id.tap_id, tapId);
            setText(inflate, R.id.tap_id_format, String.valueOf(serviceObject.tapIdFormat()));
        } else {
            inflate.findViewById(R.id.tap_id_layout).setVisibility(8);
            inflate.findViewById(R.id.tap_id_format_layout).setVisibility(8);
        }
        tapId = serviceObject.deviceId();
        if (tapId != null) {
            setText(inflate, R.id.device_id, tapId);
            setText(inflate, R.id.device_id_format, String.valueOf(serviceObject.deviceIdFormat()));
        } else {
            inflate.findViewById(R.id.device_id_layout).setVisibility(8);
            inflate.findViewById(R.id.device_id_format_layout).setVisibility(8);
        }
        viewGroup.addView(inflate);
    }

    private void setValueWithFormat(View view, int i, int i2, byte[] bArr, Format format) {
        Context context = getContext();
        setText(view, i2, getFormatString(context, format));
        switch (format) {
            case ASCII:
            case UTF_8:
            case UTF_16:
                String str = new String(bArr, format.getCharset());
                setText(view, i, context.getString(R.string.clarified_text_value, new Object[]{Hex.encodeUpper(bArr), str}));
                return;
            default:
                setText(view, i, bArr);
                return;
        }
    }

    private SessionRequest getRequestSession(CommandAndResponse commandAndResponse) throws ByteHelperException {
        switch (commandAndResponse.type()) {
            case GET_SMARTTAP_DATA:
                return ByteHelper.getSmartTapDataRequest(commandAndResponse.command());
            case GET_ADDITIONAL_SMARTTAP_DATA:
                return ByteHelper.getAdditionalSmartTapDataRequest(commandAndResponse.command());
            case NEGOTIATE_SMARTTAP:
                return ByteHelper.negotiateSmartTapRequest(commandAndResponse.command());
            case PUSH_SMARTTAP_DATA:
                return ByteHelper.pushSmartTapDataRequest(commandAndResponse.command());
            default:
                String valueOf = String.valueOf(commandAndResponse.type());
                throw new ByteHelperException(new StringBuilder(String.valueOf(valueOf).length() + 28).append("Unknown CommandAndResponse: ").append(valueOf).toString());
        }
    }

    private SessionResponse getResponseSession(CommandAndResponse commandAndResponse, short s) throws ByteHelperException {
        switch (commandAndResponse.type()) {
            case GET_SMARTTAP_DATA:
            case GET_ADDITIONAL_SMARTTAP_DATA:
                return ByteHelper.getSmartTapDataResponse(commandAndResponse.response(), this.serviceObjectConverter, s);
            case NEGOTIATE_SMARTTAP:
                return ByteHelper.negotiateSmartTapResponse(commandAndResponse.response(), s);
            case PUSH_SMARTTAP_DATA:
                return ByteHelper.sessionResponse(commandAndResponse.response(), SmartTap2Values.PUSH_SERVICE_RESPONSE_NDEF_TYPE, s);
            default:
                String valueOf = String.valueOf(commandAndResponse.type());
                throw new ByteHelperException(new StringBuilder(String.valueOf(valueOf).length() + 28).append("Unknown CommandAndResponse: ").append(valueOf).toString());
        }
    }

    private static String getSessionStatusString(Context context, Status status) {
        switch (status) {
            case UNKNOWN:
                return context.getString(R.string.session_status_unknown);
            case OK:
                return context.getString(R.string.session_status_ok);
            case NDEF_FORMAT_ERROR:
                return context.getString(R.string.session_status_ndef_format_error);
            case UNSUPPORTED_VERSION:
                return context.getString(R.string.session_status_unsupported_version);
            case INVALID_SEQUENCE_NUMBER:
                return context.getString(R.string.session_status_invalid_sequence_number);
            case UNSUPPORTED_MERCHANT:
                return context.getString(R.string.session_status_unsupported_merchant);
            case MERCHANT_DATA_MISSING:
                return context.getString(R.string.session_status_merchant_data_missing);
            case SERVICE_DATA_MISSING:
                return context.getString(R.string.session_status_service_data_missing);
            case RESEND_REQUEST:
                return context.getString(R.string.session_status_resend_request);
            case DATA_NOT_YET_AVAILABLE:
                return context.getString(R.string.session_status_data_not_available);
            default:
                LOG.w("Unknown session status: %s", status);
                return status.name();
        }
    }

    private static String getCategoryString(Context context, MerchantCategory merchantCategory) {
        switch (merchantCategory) {
            case UNKNOWN:
                return context.getString(R.string.merchant_category_unknown);
            case AGRICULTURAL_SERVICES:
                return context.getString(R.string.merchant_category_agriculture);
            case CONTRACTED_SERVICES:
                return context.getString(R.string.merchant_category_contract);
            case AIRLINES:
                return context.getString(R.string.merchant_category_airline);
            case CAR_RENTAL:
                return context.getString(R.string.merchant_category_car_rental);
            case LODGING:
                return context.getString(R.string.merchant_category_lodging);
            case TRANSPORTATION_SERVICES:
                return context.getString(R.string.merchant_category_transportation);
            case UTILITY_SERVICES:
                return context.getString(R.string.merchant_category_utility);
            case RETAIL_OUTLET_SERVICES:
                return context.getString(R.string.merchant_category_retail);
            case CLOTHING_STORES:
                return context.getString(R.string.merchant_category_clothing);
            case MISCELLANEOUS_STORES:
                return context.getString(R.string.merchant_category_misc);
            case BUSINESS_SERVICES:
                return context.getString(R.string.merchant_category_business);
            case PROFESSIONAL_SERVICES_MEMBERSHIP_ORGANIZATIONS:
                return context.getString(R.string.merchant_category_membership);
            case GOVERNMENT_SERVICES:
                return context.getString(R.string.merchant_category_government);
            default:
                LOG.w("Unknown merchant category: %s", merchantCategory);
                return merchantCategory.name();
        }
    }

    private static String getServiceTypeString(Context context, Type type) {
        switch (type) {
            case ALL:
                return context.getString(R.string.service_type_all);
            case ALL_EXCEPT_PPSE:
                return context.getString(R.string.service_type_all_except_ppse);
            case PPSE:
                return context.getString(R.string.service_type_ppse);
            case LOYALTY:
                return context.getString(R.string.service_type_loyalty);
            case OFFER:
                return context.getString(R.string.service_type_offer);
            case GIFT_CARD:
                return context.getString(R.string.service_type_gift_card);
            case CLOSED_LOOP_CARD:
                return context.getString(R.string.service_type_closed_loop_card);
            case CLOUD_WALLET:
                return context.getString(R.string.service_type_cloud_wallet);
            case MOBILE_MARKETING:
                return context.getString(R.string.service_type_mobile_marketing);
            case WALLET_CUSTOMER:
                return context.getString(R.string.service_type_wallet_customer);
            default:
                LOG.w("Unknown request type: %s", type);
                return type.name();
        }
    }

    private static String getIssuerString(Context context, Issuer issuer) {
        switch (issuer) {
            case UNSPECIFIED:
                return context.getString(R.string.issuer_unspecified);
            case MERCHANT:
                return context.getString(R.string.issuer_merchant);
            case WALLET:
                return context.getString(R.string.issuer_wallet);
            case MANUFACTURER:
                return context.getString(R.string.issuer_manufacturer);
            default:
                LOG.w("Unknown request issuer: %s", issuer);
                return issuer.name();
        }
    }

    private static String getFormatString(Context context, Format format) {
        switch (format) {
            case ASCII:
                return context.getString(R.string.service_format_ascii);
            case UTF_8:
                return context.getString(R.string.service_format_utf_8);
            case UTF_16:
                return context.getString(R.string.service_format_utf_16);
            case BINARY:
                return context.getString(R.string.service_format_binary);
            case BCD:
                return context.getString(R.string.service_format_bcd);
            case UNSPECIFIED:
                return context.getString(R.string.service_format_unspecified);
            default:
                LOG.w("Unknown request format: %s", format);
                return format.name();
        }
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

    private static String getNewServiceTypeString(Context context, ServiceType serviceType) {
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
                return context.getString(R.string.new_service_type_signup);
            default:
                LOG.w("Unknown new service type: %s", serviceType);
                return serviceType.name();
        }
    }
}
