package com.google.commerce.tapandpay.merchantapp.testcaseview;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Hex;

public class RemoveNdefRecordInputView extends LinearLayout {
    private Button deleteButton = ((Button) findViewById(R.id.delete_button));
    private TextInputLayout ndefIdLayout = ((TextInputLayout) findViewById(R.id.ndef_id_layout));

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

        public SavedState(Parcelable parcelable, String str) {
            super(parcelable);
            this.ndefId = str;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeString(this.ndefId);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.ndefId = parcel.readString();
        }
    }

    public RemoveNdefRecordInputView(Context context) {
        super(context);
        inflate(context, R.layout.input_view_wrapper, this);
        ((LinearLayout) findViewById(R.id.wrapped_data)).addView(LayoutInflater.from(context).inflate(R.layout.remove_ndef_input_view, null, false));
    }

    public void setDeleteClickListener(OnClickListener onClickListener) {
        this.deleteButton.setOnClickListener(onClickListener);
    }

    public void requestTextFocus() {
        this.ndefIdLayout.getEditText().requestFocus();
    }

    public boolean validate() {
        return Validator.hex(getContext(), this.ndefIdLayout, 1, -1);
    }

    public ByteArrayWrapper getNdefId() {
        return new ByteArrayWrapper(Hex.decode(this.ndefIdLayout.getEditText().getText().toString()));
    }

    public void setNdefData(ByteArrayWrapper byteArrayWrapper) {
        this.ndefIdLayout.getEditText().setText(Hex.encodeUpper(byteArrayWrapper.array()));
    }

    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.ndefIdLayout.getEditText().getText().toString());
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.ndefIdLayout.getEditText().setText(savedState.ndefId);
    }
}
