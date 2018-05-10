package com.google.android.libraries.commerce.hce.terminal.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ObjectIdSetPreference extends DialogPreference {
    private List<String> objectIds;
    private int titleFormatResId;
    private View view;

    public ObjectIdSetPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ObjectIdSetPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ObjectIdSetPreference(Context context) {
        super(context);
    }

    public void setTitleFormatResId(int i) {
        this.titleFormatResId = i;
        setTitle();
    }

    public void setStringValues(Collection<String> collection) {
        this.objectIds = new ArrayList(collection);
        Collections.sort(this.objectIds);
        setSharedPreference();
        if (this.view != null) {
            setObjectIdFields();
        }
    }

    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.view = view;
        this.objectIds = new ArrayList(getSharedPreference());
        Collections.sort(this.objectIds);
        setObjectIdFields();
        ((Button) view.findViewById(R.id.add_object_id_button)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ObjectIdSetPreference.this.objectIds.add("A1B2C3D4");
                ObjectIdSetPreference.this.setObjectIdFields();
            }
        });
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case -1:
                setSharedPreference();
                break;
        }
        dialogInterface.dismiss();
    }

    private void setObjectIdFields() {
        LinearLayout linearLayout = (LinearLayout) this.view.findViewById(R.id.object_id_layout);
        linearLayout.removeAllViews();
        LayoutInflater from = LayoutInflater.from(getContext());
        int i = 0;
        for (String str : this.objectIds) {
            int i2 = i + 1;
            LinearLayout linearLayout2 = (LinearLayout) from.inflate(R.layout.one_object_id, linearLayout, false);
            linearLayout.addView(linearLayout2);
            final EditText editText = (EditText) linearLayout2.findViewById(R.id.object_id);
            editText.setText(str);
            setTextColorForObjectId(editText, str);
            editText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    String obj = editable.toString();
                    if (!obj.equals(ObjectIdSetPreference.this.objectIds.get(i))) {
                        ObjectIdSetPreference.this.objectIds.remove(i);
                        ObjectIdSetPreference.this.objectIds.add(i, obj);
                        ObjectIdSetPreference.this.setTextColorForObjectId(editText, obj);
                    }
                }
            });
            ((Button) linearLayout2.findViewById(R.id.remove_object_id)).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ObjectIdSetPreference.this.objectIds.remove(i);
                    ObjectIdSetPreference.this.setObjectIdFields();
                }
            });
            i = i2;
        }
    }

    private void setTextColorForObjectId(EditText editText, String str) {
        try {
            Hex.decode(str);
            editText.setTextColor(-16777216);
        } catch (IllegalArgumentException e) {
            editText.setTextColor(-65536);
        }
    }

    private Set<String> getSharedPreference() {
        return getSharedPreferences().getStringSet(getKey(), ImmutableSet.of());
    }

    private void setSharedPreference() {
        Editor edit = getSharedPreferences().edit();
        edit.putStringSet(getKey(), ImmutableSet.copyOf(this.objectIds));
        edit.apply();
        setTitle();
    }

    private void setTitle() {
        setTitle(getContext().getString(this.titleFormatResId, new Object[]{TextUtils.join(", ", getSharedPreference())}));
    }
}
