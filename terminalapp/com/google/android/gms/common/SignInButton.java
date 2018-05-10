package com.google.android.gms.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import com.google.android.gms.base.R;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzaf;
import com.google.android.gms.common.internal.zzag;
import com.google.android.gms.dynamic.zzg.zza;

public final class SignInButton extends FrameLayout implements OnClickListener {
    private Scope[] Cx;
    private View Cy;
    private OnClickListener Cz;
    private int mColor;
    private int mSize;

    public SignInButton(Context context) {
        this(context, null);
    }

    public SignInButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SignInButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.Cz = null;
        zzb(context, attributeSet);
        setStyle(this.mSize, this.mColor, this.Cx);
    }

    private static Button zza(Context context, int i, int i2, Scope[] scopeArr) {
        Button com_google_android_gms_common_internal_zzag = new zzag(context);
        com_google_android_gms_common_internal_zzag.zza(context.getResources(), i, i2, scopeArr);
        return com_google_android_gms_common_internal_zzag;
    }

    private void zzb(Context context, AttributeSet attributeSet) {
        int i = 0;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.SignInButton, 0, 0);
        try {
            this.mSize = obtainStyledAttributes.getInt(R.styleable.SignInButton_buttonSize, 0);
            this.mColor = obtainStyledAttributes.getInt(R.styleable.SignInButton_colorScheme, 2);
            String string = obtainStyledAttributes.getString(R.styleable.SignInButton_scopeUris);
            if (string == null) {
                this.Cx = null;
            } else {
                String[] split = string.trim().split("\\s+");
                this.Cx = new Scope[split.length];
                while (i < split.length) {
                    this.Cx[i] = new Scope(split[i].toString());
                    i++;
                }
            }
            obtainStyledAttributes.recycle();
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
        }
    }

    private void zzcb(Context context) {
        if (this.Cy != null) {
            removeView(this.Cy);
        }
        try {
            this.Cy = zzaf.zzb(context, this.mSize, this.mColor, this.Cx);
        } catch (zza e) {
            Log.w("SignInButton", "Sign in button not found, using placeholder instead");
            this.Cy = zza(context, this.mSize, this.mColor, this.Cx);
        }
        addView(this.Cy);
        this.Cy.setEnabled(isEnabled());
        this.Cy.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (this.Cz != null && view == this.Cy) {
            this.Cz.onClick(this);
        }
    }

    public void setColorScheme(int i) {
        setStyle(this.mSize, i, this.Cx);
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.Cy.setEnabled(z);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.Cz = onClickListener;
        if (this.Cy != null) {
            this.Cy.setOnClickListener(this);
        }
    }

    public void setScopes(Scope[] scopeArr) {
        setStyle(this.mSize, this.mColor, scopeArr);
    }

    public void setSize(int i) {
        setStyle(i, this.mColor, this.Cx);
    }

    public void setStyle(int i, int i2) {
        setStyle(i, i2, this.Cx);
    }

    public void setStyle(int i, int i2, Scope[] scopeArr) {
        this.mSize = i;
        this.mColor = i2;
        this.Cx = scopeArr;
        zzcb(getContext());
    }
}
