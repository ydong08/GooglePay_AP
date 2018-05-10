package com.google.commerce.tapandpay.merchantapp.testcaseview;

import android.content.Context;
import android.nfc.NdefRecord;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Hex;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class AddNdefRecordInputView extends LinearLayout {
    private Button deleteButton = ((Button) findViewById(R.id.delete_button));
    private TextInputLayout ndefIdLayout = ((TextInputLayout) findViewById(R.id.new_ndef_id_layout));
    private TextInputLayout ndefPayloadLayout = ((TextInputLayout) findViewById(R.id.ndef_payload_layout));
    private TextInputLayout parentNdefIdLayout = ((TextInputLayout) findViewById(R.id.parent_ndef_id_layout));

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        private final String ndefId;
        private final String ndefPayload;
        private final String parentNdefId;

        public SavedState(Parcelable parcelable, String str, String str2, String str3) {
            super(parcelable);
            this.parentNdefId = str;
            this.ndefId = str2;
            this.ndefPayload = str3;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeString(this.parentNdefId);
            parcel.writeString(this.ndefId);
            parcel.writeString(this.ndefPayload);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.parentNdefId = parcel.readString();
            this.ndefId = parcel.readString();
            this.ndefPayload = parcel.readString();
        }
    }

    public AddNdefRecordInputView(Context context) {
        super(context);
        inflate(context, R.layout.input_view_wrapper, this);
        ((LinearLayout) findViewById(R.id.wrapped_data)).addView(LayoutInflater.from(context).inflate(R.layout.add_ndef_input_view, null, false));
    }

    public void setDeleteClickListener(OnClickListener onClickListener) {
        this.deleteButton.setOnClickListener(onClickListener);
    }

    public void requestTextFocus() {
        this.parentNdefIdLayout.getEditText().requestFocus();
    }

    public boolean validate() {
        Context context = getContext();
        if (Validator.hex(context, this.parentNdefIdLayout, 1, -1) && Validator.hex(context, this.ndefIdLayout, 1, -1) && Validator.hex(context, this.ndefPayloadLayout)) {
            return true;
        }
        return false;
    }

    public Entry<ByteArrayWrapper, NdefRecord> getEntry(short s) {
        return new SimpleEntry(new ByteArrayWrapper(Hex.decode(this.parentNdefIdLayout.getEditText().getText().toString())), NdefRecords.compose(Hex.decode(this.ndefIdLayout.getEditText().getText().toString()), Hex.decode(this.ndefPayloadLayout.getEditText().getText().toString()), s));
    }

    public void setNdefData(Entry<ByteArrayWrapper, NdefRecord> entry) {
        NdefRecord ndefRecord = (NdefRecord) entry.getValue();
        this.parentNdefIdLayout.getEditText().setText(Hex.encodeUpper(((ByteArrayWrapper) entry.getKey()).array()));
        this.ndefIdLayout.getEditText().setText(Hex.encodeUpper(ndefRecord.getId()));
        this.ndefPayloadLayout.getEditText().setText(Hex.encodeUpper(ndefRecord.getPayload()));
    }

    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.parentNdefIdLayout.getEditText().getText().toString(), this.ndefIdLayout.getEditText().getText().toString(), this.ndefPayloadLayout.getEditText().getText().toString());
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.parentNdefIdLayout.getEditText().setText(savedState.parentNdefId);
        this.ndefIdLayout.getEditText().setText(savedState.ndefId);
        this.ndefPayloadLayout.getEditText().setText(savedState.ndefPayload);
    }
}
