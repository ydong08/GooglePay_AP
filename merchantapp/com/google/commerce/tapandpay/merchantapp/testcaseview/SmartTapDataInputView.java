package com.google.commerce.tapandpay.merchantapp.testcaseview;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.google.commerce.tapandpay.merchantapp.testcase.EncodingValue;
import com.google.commerce.tapandpay.merchantapp.testcase.EncodingValue.Encoding;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.internal.tapandpay.v1.valuables.nano.CommonProto.SmartTap;

public class SmartTapDataInputView extends LinearLayout {
    private Spinner cvcEncoding;
    private TextInputLayout cvcLayout;
    private Button deleteButton;
    private TextInputLayout expMonthLayout;
    private TextInputLayout expYearLayout;
    private TextInputLayout issuerLayout = ((TextInputLayout) findViewById(R.id.issuer_id_layout));
    private Spinner pinEncoding;
    private TextInputLayout pinLayout;
    private int type;
    private Spinner valueEncoding;
    private TextInputLayout valueLayout = ((TextInputLayout) findViewById(R.id.value_layout));

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        private final String cvc;
        private final Encoding cvcEncoding;
        private final String expMonth;
        private final String expYear;
        private final String issuerId;
        private final String pin;
        private final Encoding pinEncoding;
        private final int type;
        private final String value;
        private final Encoding valueEncoding;

        public SavedState(Parcelable parcelable, int i, String str, String str2, Encoding encoding, String str3, Encoding encoding2, String str4, Encoding encoding3, String str5, String str6) {
            super(parcelable);
            this.type = i;
            this.issuerId = str;
            this.value = str2;
            this.valueEncoding = encoding;
            this.pin = str3;
            this.pinEncoding = encoding2;
            this.cvc = str4;
            this.cvcEncoding = encoding3;
            this.expMonth = str5;
            this.expYear = str6;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.type);
            parcel.writeString(this.issuerId);
            parcel.writeString(this.value);
            parcel.writeSerializable(this.valueEncoding);
            parcel.writeString(this.pin);
            parcel.writeSerializable(this.pinEncoding);
            parcel.writeString(this.cvc);
            parcel.writeSerializable(this.cvcEncoding);
            parcel.writeString(this.expMonth);
            parcel.writeString(this.expYear);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.type = parcel.readInt();
            this.issuerId = parcel.readString();
            this.value = parcel.readString();
            this.valueEncoding = (Encoding) parcel.readSerializable();
            this.pin = parcel.readString();
            this.pinEncoding = (Encoding) parcel.readSerializable();
            this.cvc = parcel.readString();
            this.cvcEncoding = (Encoding) parcel.readSerializable();
            this.expMonth = parcel.readString();
            this.expYear = parcel.readString();
        }
    }

    public SmartTapDataInputView(Context context, int i) {
        super(context);
        this.type = i;
        inflate(context, R.layout.input_view_wrapper, this);
        inflateValuableDataEntry(context);
        this.valueEncoding = (Spinner) findViewWithTag(context.getString(R.string.value_encoding_tag));
        if (i == 3) {
            this.pinLayout = (TextInputLayout) findViewById(R.id.pin_layout);
            this.pinEncoding = (Spinner) findViewWithTag(context.getString(R.string.pin_encoding_tag));
        } else if (i == 4) {
            this.cvcLayout = (TextInputLayout) findViewById(R.id.cvc_layout);
            this.cvcEncoding = (Spinner) findViewWithTag(context.getString(R.string.cvc_encoding_tag));
            this.expMonthLayout = (TextInputLayout) findViewById(R.id.exp_month_layout);
            this.expYearLayout = (TextInputLayout) findViewById(R.id.exp_year_layout);
        }
        this.deleteButton = (Button) findViewById(R.id.delete_button);
        setEncodingListeners(context);
    }

    private void inflateValuableDataEntry(Context context) {
        View inflate;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.wrapped_data);
        LayoutInflater from = LayoutInflater.from(context);
        switch (this.type) {
            case 1:
                inflate = from.inflate(R.layout.loyalty_input_view, null, false);
                break;
            case 2:
                inflate = from.inflate(R.layout.offer_input_view, null, false);
                break;
            case 3:
                inflate = from.inflate(R.layout.gift_card_input_view, null, false);
                break;
            case 4:
                inflate = from.inflate(R.layout.plc_input_view, null, false);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        linearLayout.addView(inflate);
    }

    private void setEncodingListeners(Context context) {
        this.valueEncoding.setAdapter(new ArrayAdapter(context, 17367043, Encoding.values()));
        this.valueEncoding.setOnItemSelectedListener(getEncodingListener(this.valueEncoding, this.valueLayout));
        if (this.type == 3) {
            this.pinEncoding.setAdapter(new ArrayAdapter(context, 17367043, Encoding.values()));
            this.pinEncoding.setOnItemSelectedListener(getEncodingListener(this.pinEncoding, this.pinLayout));
        } else if (this.type == 4) {
            this.cvcEncoding.setAdapter(new ArrayAdapter(context, 17367043, Encoding.values()));
            this.cvcEncoding.setOnItemSelectedListener(getEncodingListener(this.cvcEncoding, this.cvcLayout));
        }
    }

    private OnItemSelectedListener getEncodingListener(final Spinner spinner, final TextInputLayout textInputLayout) {
        return new OnItemSelectedListener(this) {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ((Encoding) spinner.getItemAtPosition(i)).setInputType(textInputLayout.getEditText());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
    }

    public void setDeleteClickListener(OnClickListener onClickListener) {
        this.deleteButton.setOnClickListener(onClickListener);
    }

    public SmartTap getSmartTap() {
        switch (this.type) {
            case 1:
                return (SmartTap) getLoyaltySmartTapObject().orNull();
            case 2:
                return (SmartTap) getOfferSmartTapObject().orNull();
            case 3:
                return (SmartTap) getGiftCardSmartTapObject().orNull();
            case 4:
                return (SmartTap) getPlcSmartTapObject().orNull();
            default:
                throw new UnsupportedOperationException();
        }
    }

    private Optional<SmartTap> getLoyaltySmartTapObject() {
        return getBaseSmartTapObject();
    }

    private Optional<SmartTap> getOfferSmartTapObject() {
        return getBaseSmartTapObject();
    }

    private Optional<SmartTap> getGiftCardSmartTapObject() {
        Optional<SmartTap> baseSmartTapObject = getBaseSmartTapObject();
        if (!baseSmartTapObject.isPresent()) {
            return Optional.absent();
        }
        ((Encoding) this.pinEncoding.getSelectedItem()).setTapPin((SmartTap) baseSmartTapObject.get(), getInput(this.pinLayout));
        return baseSmartTapObject;
    }

    private Optional<SmartTap> getPlcSmartTapObject() {
        Optional<SmartTap> baseSmartTapObject = getBaseSmartTapObject();
        if (!baseSmartTapObject.isPresent()) {
            return Optional.absent();
        }
        SmartTap smartTap = (SmartTap) baseSmartTapObject.get();
        ((Encoding) this.cvcEncoding.getSelectedItem()).setTapCvc(smartTap, getInput(this.cvcLayout));
        String input = getInput(this.expMonthLayout);
        String input2 = getInput(this.expYearLayout);
        try {
            smartTap.expirationMonth = Integer.valueOf(input).intValue();
            smartTap.expirationYear = Integer.valueOf(input2).intValue();
        } catch (NumberFormatException e) {
        }
        return baseSmartTapObject;
    }

    private Optional<SmartTap> getBaseSmartTapObject() {
        String input = getInput(this.issuerLayout);
        String input2 = getInput(this.valueLayout);
        if (Strings.isNullOrEmpty(input2)) {
            return Optional.absent();
        }
        SmartTap smartTap = new SmartTap();
        smartTap.type = this.type;
        trySetProgramId(smartTap, input);
        ((Encoding) this.valueEncoding.getSelectedItem()).setTapValue(smartTap, input2);
        return Optional.of(smartTap);
    }

    private String getInput(TextInputLayout textInputLayout) {
        return textInputLayout.getEditText().getText().toString();
    }

    private void trySetProgramId(SmartTap smartTap, String str) {
        try {
            smartTap.programId = Long.valueOf(str).longValue();
        } catch (NumberFormatException e) {
        }
    }

    public void setSmartTapData(SmartTap smartTap) {
        this.type = smartTap.type;
        if (smartTap.programId > 0) {
            this.issuerLayout.getEditText().setText(String.valueOf(smartTap.programId));
        }
        EncodingValue valueEncoding = EncodingValue.getValueEncoding(smartTap);
        EncodingValue pinEncoding = EncodingValue.getPinEncoding(smartTap);
        EncodingValue cvcEncoding = EncodingValue.getCvcEncoding(smartTap);
        this.valueLayout.getEditText().setText(valueEncoding.getValue());
        if (this.type == 3) {
            this.pinLayout.getEditText().setText(pinEncoding.getValue());
        } else if (this.type == 4) {
            this.cvcLayout.getEditText().setText(cvcEncoding.getValue());
            this.expMonthLayout.getEditText().setText(String.valueOf(smartTap.expirationMonth));
            this.expYearLayout.getEditText().setText(String.valueOf(smartTap.expirationYear));
        }
        int i = 0;
        while (i < this.valueEncoding.getAdapter().getCount()) {
            if (this.valueEncoding.getItemAtPosition(i).equals(valueEncoding.getEncoding())) {
                this.valueEncoding.setSelection(i);
            }
            if (this.type == 3 && this.pinEncoding.getItemAtPosition(i).equals(pinEncoding.getEncoding())) {
                this.pinEncoding.setSelection(i);
            } else if (this.type == 4 && this.cvcEncoding.getItemAtPosition(i).equals(cvcEncoding.getEncoding())) {
                this.cvcEncoding.setSelection(i);
            }
            i++;
        }
    }

    public void requestTextFocus() {
        this.valueLayout.getEditText().requestFocus();
    }

    public boolean validate() {
        boolean validateIssuer = (validateIssuer() & 1) & validateEncoding(this.valueLayout, this.valueEncoding);
        if (this.type == 3) {
            return validateIssuer & validateEncoding(this.pinLayout, this.pinEncoding);
        }
        if (this.type == 4) {
            return ((validateIssuer & validateEncoding(this.cvcLayout, this.cvcEncoding)) & validateExpiration(this.expMonthLayout, 1, 12)) & validateExpiration(this.expYearLayout, 1000, 9999);
        }
        return validateIssuer;
    }

    private boolean validateIssuer() {
        String obj = this.issuerLayout.getEditText().getText().toString();
        if (!Strings.isNullOrEmpty(obj)) {
            try {
                Long.parseLong(obj);
            } catch (NumberFormatException e) {
                this.issuerLayout.setErrorEnabled(true);
                this.issuerLayout.setError(getResources().getString(R.string.long_error));
                return false;
            }
        }
        this.issuerLayout.setErrorEnabled(false);
        this.issuerLayout.setError(null);
        return true;
    }

    private boolean validateEncoding(TextInputLayout textInputLayout, Spinner spinner) {
        if (((Encoding) spinner.getSelectedItem()).isValid(textInputLayout.getEditText().getText().toString())) {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError(null);
            return true;
        }
        CharSequence string = getResources().getString(R.string.input_error, new Object[]{r0.toString()});
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(string);
        return false;
    }

    private boolean validateExpiration(TextInputLayout textInputLayout, int i, int i2) {
        boolean z;
        try {
            int parseInt = Integer.parseInt(textInputLayout.getEditText().getText().toString());
            if (parseInt < i || parseInt > i2) {
                z = false;
            } else {
                z = true;
            }
        } catch (NumberFormatException e) {
            z = false;
        }
        if (z) {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError(null);
        } else {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(getResources().getString(R.string.exp_error));
        }
        return z;
    }

    protected Parcelable onSaveInstanceState() {
        String str;
        Encoding encoding;
        String str2;
        Encoding encoding2;
        String str3;
        String str4 = null;
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        int i = this.type;
        String obj = this.issuerLayout.getEditText().getText().toString();
        String obj2 = this.valueLayout.getEditText().getText().toString();
        Encoding encoding3 = (Encoding) this.valueEncoding.getSelectedItem();
        if (this.pinLayout == null) {
            str = null;
        } else {
            str = this.pinLayout.getEditText().getText().toString();
        }
        if (this.pinEncoding == null) {
            encoding = null;
        } else {
            encoding = (Encoding) this.pinEncoding.getSelectedItem();
        }
        if (this.cvcLayout == null) {
            str2 = null;
        } else {
            str2 = this.cvcLayout.getEditText().getText().toString();
        }
        if (this.cvcEncoding == null) {
            encoding2 = null;
        } else {
            encoding2 = (Encoding) this.cvcEncoding.getSelectedItem();
        }
        if (this.expMonthLayout == null) {
            str3 = null;
        } else {
            str3 = this.expMonthLayout.getEditText().getText().toString();
        }
        if (this.expYearLayout != null) {
            str4 = this.expYearLayout.getEditText().getText().toString();
        }
        return new SavedState(onSaveInstanceState, i, obj, obj2, encoding3, str, encoding, str2, encoding2, str3, str4);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.type = savedState.type;
        this.issuerLayout.getEditText().setText(savedState.issuerId);
        this.valueLayout.getEditText().setText(savedState.value);
        if (this.type == 3) {
            this.pinLayout.getEditText().setText(savedState.pin);
        } else if (this.type == 4) {
            this.cvcLayout.getEditText().setText(savedState.cvc);
            this.expMonthLayout.getEditText().setText(savedState.expMonth);
            this.expYearLayout.getEditText().setText(savedState.expYear);
        }
        int i = 0;
        while (i < this.valueEncoding.getAdapter().getCount()) {
            if (savedState.valueEncoding.equals(this.valueEncoding.getItemAtPosition(i))) {
                this.valueEncoding.setSelection(i);
            }
            if (this.type == 3 && savedState.pinEncoding.equals(this.pinEncoding.getItemAtPosition(i))) {
                this.pinEncoding.setSelection(i);
            } else if (this.type == 4 && savedState.cvcEncoding.equals(this.cvcEncoding.getItemAtPosition(i))) {
                this.cvcEncoding.setSelection(i);
            }
            i++;
        }
    }
}
