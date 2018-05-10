package com.google.android.libraries.commerce.hce.terminal.settings;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class MerchantIdPreference extends EditTextPreference implements TextWatcher, OnClickListener {
    private final int[] merchantIds;
    private TextView merchantNameButton;
    private final String[] merchantNames;

    public MerchantIdPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Resources resources = context.getResources();
        this.merchantNames = resources.getStringArray(R.array.merchant_names);
        this.merchantIds = resources.getIntArray(R.array.merchant_ids);
    }

    public MerchantIdPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Resources resources = context.getResources();
        this.merchantNames = resources.getStringArray(R.array.merchant_names);
        this.merchantIds = resources.getIntArray(R.array.merchant_ids);
    }

    public MerchantIdPreference(Context context) {
        super(context);
        Resources resources = context.getResources();
        this.merchantNames = resources.getStringArray(R.array.merchant_names);
        this.merchantIds = resources.getIntArray(R.array.merchant_ids);
    }

    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.merchantNameButton = new Button(getContext());
        this.merchantNameButton.setLayoutParams(new LayoutParams(-1, -2));
        this.merchantNameButton.setOnClickListener(this);
        updateMerchantId();
        getEditText().addTextChangedListener(this);
        ((ViewGroup) getEditText().getParent()).addView(this.merchantNameButton);
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        updateMerchantId();
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void afterTextChanged(Editable editable) {
    }

    public void onClick(View view) {
        Builder builder = new Builder(getContext());
        builder.setSingleChoiceItems(this.merchantNames, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i >= 0 && i < MerchantIdPreference.this.merchantIds.length) {
                    MerchantIdPreference.this.getEditText().setText(Integer.valueOf(MerchantIdPreference.this.merchantIds[i]).toString());
                    MerchantIdPreference.this.updateMerchantId();
                }
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public void updateMerchantId() {
        CharSequence merchantIdString = getMerchantIdString();
        setSummary(merchantIdString);
        if (this.merchantNameButton != null) {
            this.merchantNameButton.setText(merchantIdString);
        }
    }

    private String getMerchantIdString() {
        String obj;
        Context context = getContext();
        if (getEditText().isShown()) {
            obj = getEditText().getText().toString();
        } else {
            obj = getText().toString();
        }
        try {
            int parseInt = Integer.parseInt(obj);
            for (int i : this.merchantIds) {
                if (parseInt == i) {
                    return context.getString(R.string.merchant_name_format, new Object[]{this.merchantNames[r0], Integer.valueOf(parseInt)});
                }
            }
            obj = context.getString(R.string.merchant_name_unknown);
            return context.getString(R.string.merchant_name_format, new Object[]{obj, Integer.valueOf(parseInt)});
        } catch (NumberFormatException e) {
            obj = context.getString(R.string.merchant_name_invalid);
            String string = context.getString(R.string.merchant_id_invalid);
            return context.getString(R.string.merchant_name_format, new Object[]{obj, string});
        }
    }
}
