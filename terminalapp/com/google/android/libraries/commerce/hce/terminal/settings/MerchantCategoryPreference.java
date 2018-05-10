package com.google.android.libraries.commerce.hce.terminal.settings;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.MerchantCategory;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Strings;

public class MerchantCategoryPreference extends EditTextPreference implements TextWatcher {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private TextView categoryView;

    public MerchantCategoryPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MerchantCategoryPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MerchantCategoryPreference(Context context) {
        super(context);
    }

    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.categoryView = new TextView(getContext());
        this.categoryView.setId(View.generateViewId());
        this.categoryView.setLayoutParams(new LayoutParams(-1, -2));
        updateCategory();
        getEditText().addTextChangedListener(this);
        ((ViewGroup) getEditText().getParent()).addView(this.categoryView);
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        updateCategory();
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void afterTextChanged(Editable editable) {
    }

    public void updateCategory() {
        CharSequence categoryString = getCategoryString();
        setSummary(categoryString);
        if (this.categoryView != null) {
            this.categoryView.setText(categoryString);
        }
    }

    private String getCategoryString() {
        String obj;
        int i;
        MerchantCategory merchantCategory = MerchantCategory.UNKNOWN;
        if (getEditText().isShown()) {
            obj = getEditText().getText().toString();
        } else {
            obj = getText().toString();
        }
        if (!Strings.isNullOrEmpty(obj)) {
            try {
                merchantCategory = MerchantCategory.get(Integer.parseInt(obj));
            } catch (NumberFormatException e) {
                LOG.w("Invalid int for merchant category: %s", obj);
            }
        }
        switch (merchantCategory) {
            case UNKNOWN:
                i = R.string.merchant_category_none;
                break;
            case AGRICULTURAL_SERVICES:
                i = R.string.merchant_category_agriculture;
                break;
            case CONTRACTED_SERVICES:
                i = R.string.merchant_category_contract;
                break;
            case AIRLINES:
                i = R.string.merchant_category_airline;
                break;
            case CAR_RENTAL:
                i = R.string.merchant_category_car_rental;
                break;
            case LODGING:
                i = R.string.merchant_category_lodging;
                break;
            case TRANSPORTATION_SERVICES:
                i = R.string.merchant_category_transportation;
                break;
            case UTILITY_SERVICES:
                i = R.string.merchant_category_utility;
                break;
            case RETAIL_OUTLET_SERVICES:
                i = R.string.merchant_category_retail;
                break;
            case CLOTHING_STORES:
                i = R.string.merchant_category_clothing;
                break;
            case MISCELLANEOUS_STORES:
                i = R.string.merchant_category_misc;
                break;
            case BUSINESS_SERVICES:
                i = R.string.merchant_category_business;
                break;
            case PROFESSIONAL_SERVICES_MEMBERSHIP_ORGANIZATIONS:
                i = R.string.merchant_category_membership;
                break;
            case GOVERNMENT_SERVICES:
                i = R.string.merchant_category_government;
                break;
            default:
                LOG.w("Unexpected merchant category: %s", merchantCategory);
                i = R.string.merchant_category_unknown;
                break;
        }
        Context context = getContext();
        String string = context.getString(i);
        return context.getString(R.string.merchant_category_format, new Object[]{string, obj});
    }
}
