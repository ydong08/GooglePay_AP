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
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import com.google.android.libraries.commerce.hce.util.Hex;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class CustomStatusInputView extends LinearLayout {
    private TextInputLayout commandLayout;
    private Button deleteButton;
    private NumberPicker numberPicker;
    private int repeatCount = 0;
    private TextInputLayout statusLayout;

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        private final String command;
        private final int repeatCount;
        private final String status;

        public SavedState(Parcelable parcelable, String str, String str2, int i) {
            super(parcelable);
            this.command = str;
            this.status = str2;
            this.repeatCount = i;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeString(this.command);
            parcel.writeString(this.status);
            parcel.writeInt(this.repeatCount);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.command = parcel.readString();
            this.status = parcel.readString();
            this.repeatCount = parcel.readInt();
        }
    }

    public CustomStatusInputView(Context context) {
        super(context);
        inflate(context, R.layout.input_view_wrapper, this);
        ((LinearLayout) findViewById(R.id.wrapped_data)).addView(LayoutInflater.from(context).inflate(R.layout.custom_status_input_view, null, false));
        this.commandLayout = (TextInputLayout) findViewById(R.id.custom_status_command_layout);
        this.statusLayout = (TextInputLayout) findViewById(R.id.custom_status_layout);
        this.numberPicker = (NumberPicker) findViewById(R.id.custom_status_repeats);
        this.deleteButton = (Button) findViewById(R.id.delete_button);
        this.numberPicker.setValue(this.repeatCount);
        this.numberPicker.setMinValue(0);
        this.numberPicker.setMaxValue(10);
        this.numberPicker.setWrapSelectorWheel(true);
        this.numberPicker.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                CustomStatusInputView.this.repeatCount = i2;
            }
        });
    }

    public void setDeleteClickListener(OnClickListener onClickListener) {
        this.deleteButton.setOnClickListener(onClickListener);
    }

    public void requestTextFocus() {
        this.commandLayout.getEditText().requestFocus();
    }

    public TextInputLayout getCommandLayout() {
        return this.commandLayout;
    }

    public boolean validate() {
        Context context = getContext();
        if (Validator.hex(context, this.commandLayout, 1) && Validator.hex(context, this.statusLayout, 2)) {
            return true;
        }
        return false;
    }

    public Entry<Byte, byte[]> getEntry() {
        return new SimpleEntry(Byte.valueOf(Hex.decode(this.commandLayout.getEditText().getText().toString())[0]), Hex.decode(this.statusLayout.getEditText().getText().toString()));
    }

    public void setStatusData(Entry<Byte, byte[]> entry) {
        byte[] bArr = (byte[]) entry.getValue();
        this.commandLayout.getEditText().setText(Hex.encodeUpper(((Byte) entry.getKey()).byteValue()));
        this.statusLayout.getEditText().setText(Hex.encodeUpper(bArr));
    }

    public Entry<Byte, Integer> getMaxOverrides() {
        return new SimpleEntry(Byte.valueOf(Hex.decode(this.commandLayout.getEditText().getText().toString())[0]), Integer.valueOf(this.repeatCount));
    }

    public void setMaxOverrides(int i) {
        this.repeatCount = i;
        this.numberPicker.setValue(this.repeatCount);
    }

    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.commandLayout.getEditText().getText().toString(), this.statusLayout.getEditText().getText().toString(), this.repeatCount);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.commandLayout.getEditText().setText(savedState.command);
        this.statusLayout.getEditText().setText(savedState.status);
        this.repeatCount = savedState.repeatCount;
    }
}
