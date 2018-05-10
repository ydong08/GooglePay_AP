package com.google.commerce.tapandpay.terminalapp.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.MerchantCategory;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.MerchantInfo;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Issuer;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Request;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Type;
import com.google.android.libraries.commerce.hce.ndef.Format;
import com.google.android.libraries.commerce.hce.ndef.Primitive;
import com.google.android.libraries.commerce.hce.ndef.Text;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Strings;
import java.util.Set;

public class ServiceRecords {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();

    private ServiceRecords() {
    }

    public static void addServiceView(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup, ServiceObject serviceObject) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.ndef_service_view, viewGroup, false);
        ViewGroup viewGroup3 = (ViewGroup) viewGroup2.findViewById(R.id.additional_service_info);
        String typeString = getTypeString(context, serviceObject.type());
        if (serviceObject.serviceId() != null) {
            InflaterHelper.setText(viewGroup2, R.id.service_id, serviceObject.serviceId());
        } else {
            InflaterHelper.setText(viewGroup2, R.id.service_id, R.string.null_string);
        }
        InflaterHelper.setText(viewGroup2, R.id.service_type, typeString);
        setValueWithFormat(context, viewGroup2, R.id.service_number, R.id.service_number_format, serviceObject.serviceNumberId(), serviceObject.serviceNumberIdFormat());
        if (serviceObject.type() == Type.WALLET_CUSTOMER) {
            InflaterHelper.setVisibility(viewGroup2, R.id.issuer_layout, 8);
            viewGroup3.addView(getCustomerView(context, layoutInflater, viewGroup2, serviceObject));
        } else {
            String issuerString = getIssuerString(context, serviceObject.issuer());
            setValueWithFormat(context, viewGroup2, R.id.issuer_id, R.id.issuer_id_format, serviceObject.issuerId(), serviceObject.issuerFormat());
            InflaterHelper.setText(viewGroup2, R.id.issuer, issuerString);
            if (serviceObject.type() == Type.GIFT_CARD) {
                viewGroup3.addView(getGiftCardView(context, layoutInflater, viewGroup2, serviceObject));
            } else if (serviceObject.type() == Type.CLOSED_LOOP_CARD) {
                viewGroup3.addView(getPlcView(context, layoutInflater, viewGroup2, serviceObject));
            }
        }
        viewGroup.addView(viewGroup2);
    }

    public static void addMerchantInfo(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup, MerchantInfo merchantInfo) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.ndef_merchant_view, viewGroup, false);
        InflaterHelper.setText(viewGroup2, R.id.merchant_id, Long.toString(merchantInfo.getMerchantId()));
        if (merchantInfo.getLocationId().isPresent()) {
            InflaterHelper.setText(viewGroup2, R.id.store_location_id, ((Primitive) merchantInfo.getLocationId().get()).getPayload());
        }
        if (merchantInfo.getTerminalId().isPresent()) {
            InflaterHelper.setText(viewGroup2, R.id.terminal_id, ((Primitive) merchantInfo.getTerminalId().get()).getPayload());
        }
        if (merchantInfo.getName().isPresent()) {
            Text text = (Text) merchantInfo.getName().get();
            if (text != null) {
                InflaterHelper.setText(viewGroup2, R.id.merchant_name, text.getText());
            }
        }
        if (merchantInfo.getCategory() != MerchantCategory.UNKNOWN) {
            InflaterHelper.setText(viewGroup2, R.id.merchant_category, getCategoryString(context, merchantInfo.getCategory()));
        }
        viewGroup.addView(viewGroup2);
    }

    public static void addServiceObjects(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup, Set<Request> set) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.ndef_service_table, viewGroup, false);
        TableLayout tableLayout = (TableLayout) viewGroup2.findViewById(R.id.table);
        for (Request request : set) {
            ViewGroup viewGroup3 = (ViewGroup) layoutInflater.inflate(R.layout.ndef_service_row, tableLayout, false);
            String typeString = getTypeString(context, request.getType());
            String issuerString = getIssuerString(context, request.getIssuer());
            String formatString = getFormatString(context, request.getFormat());
            InflaterHelper.setText(viewGroup3, R.id.service_type, typeString);
            InflaterHelper.setText(viewGroup3, R.id.service_issuer, issuerString);
            InflaterHelper.setText(viewGroup3, R.id.expected_format, formatString);
            tableLayout.addView(viewGroup3);
        }
        viewGroup.addView(viewGroup2);
    }

    private static View getCustomerView(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup, ServiceObject serviceObject) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.ndef_customer_view, viewGroup, false);
        String preferedLanguage = serviceObject.preferedLanguage();
        if (Strings.isNullOrEmpty(preferedLanguage)) {
            InflaterHelper.setVisibility(viewGroup2, R.id.preferred_language_layout, 8);
        } else {
            InflaterHelper.setText(viewGroup2, R.id.preferred_language, preferedLanguage);
        }
        byte[] tapId = serviceObject.tapId();
        if (tapId == null || tapId.length <= 0) {
            InflaterHelper.setVisibility(viewGroup2, R.id.tap_id_layout, 8);
            InflaterHelper.setVisibility(viewGroup2, R.id.tap_id_format_layout, 8);
        } else {
            setValueWithFormat(context, viewGroup2, R.id.tap_id, R.id.tap_id_format, tapId, serviceObject.tapIdFormat());
        }
        tapId = serviceObject.deviceId();
        if (tapId == null || tapId.length <= 0) {
            InflaterHelper.setVisibility(viewGroup2, R.id.device_id_layout, 8);
            InflaterHelper.setVisibility(viewGroup2, R.id.device_id_format_layout, 8);
        } else {
            setValueWithFormat(context, viewGroup2, R.id.device_id, R.id.device_id_format, tapId, serviceObject.deviceIdFormat());
        }
        return viewGroup2;
    }

    private static View getGiftCardView(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup, ServiceObject serviceObject) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.ndef_gift_card_view, viewGroup, false);
        setValueWithFormat(context, viewGroup2, R.id.pin, R.id.pin_format, serviceObject.pin(), serviceObject.pinFormat());
        return viewGroup2;
    }

    private static View getPlcView(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup, ServiceObject serviceObject) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.ndef_plc_view, viewGroup, false);
        setValueWithFormat(context, viewGroup2, R.id.cvc, R.id.cvc_format, serviceObject.cvc(), serviceObject.cvcFormat());
        setValueWithFormat(context, viewGroup2, R.id.expiration, R.id.expiration_format, serviceObject.expiration(), serviceObject.expirationFormat());
        return viewGroup2;
    }

    private static void setValueWithFormat(Context context, ViewGroup viewGroup, int i, int i2, byte[] bArr, Format format) {
        InflaterHelper.setText(viewGroup, i2, getFormatString(context, format));
        switch (format) {
            case ASCII:
            case UTF_8:
            case UTF_16:
                String str = new String(bArr, format.getCharset());
                InflaterHelper.setText(viewGroup, i, context.getString(R.string.clarified_text_value, new Object[]{Hex.encodeUpper(bArr), str}));
                return;
            default:
                InflaterHelper.setText(viewGroup, i, bArr);
                return;
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
                LOG.w("Unknown merchant category for localization: %s", merchantCategory);
                return merchantCategory.name();
        }
    }

    private static String getTypeString(Context context, Type type) {
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
                LOG.w("Unknown request type for localization: %s", type);
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
                LOG.w("Unknown request issuer for localization: %s", issuer);
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
                LOG.w("Unknown request format for localization: %s", format);
                return format.name();
        }
    }
}
