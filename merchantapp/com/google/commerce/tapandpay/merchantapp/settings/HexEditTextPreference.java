package com.google.commerce.tapandpay.merchantapp.settings;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class HexEditTextPreference extends EditTextPreference implements TextWatcher {
    public HexEditTextPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public HexEditTextPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected View onCreateView(ViewGroup viewGroup) {
        View onCreateView = super.onCreateView(viewGroup);
        updateSummary();
        return onCreateView;
    }

    protected View onCreateDialogView() {
        View onCreateDialogView = super.onCreateDialogView();
        getEditText().addTextChangedListener(this);
        updateSummary();
        return onCreateDialogView;
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        updateSummary();
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void afterTextChanged(Editable editable) {
    }

    public void updateSummary() {
        CharSequence obj;
        if (getEditText().isShown()) {
            obj = getEditText().getText().toString();
        } else {
            obj = getText().toString();
        }
        setSummary(obj);
    }
}
