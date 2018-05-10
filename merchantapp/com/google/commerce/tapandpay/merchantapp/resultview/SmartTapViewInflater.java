package com.google.commerce.tapandpay.merchantapp.resultview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.commerce.tapandpay.merchantapp.common.ByteHelper;
import com.google.commerce.tapandpay.merchantapp.result.Result;
import com.google.commerce.tapandpay.merchantapp.result.Result.CommandAndResponse;

public abstract class SmartTapViewInflater {
    private final Context context;
    private final LayoutInflater inflater;
    private final Result result;

    public abstract void renderCommandAndResponse(CommandAndResponse commandAndResponse, ViewGroup viewGroup);

    public SmartTapViewInflater(Context context, Result result) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.result = result;
    }

    protected Context getContext() {
        return this.context;
    }

    protected Result getResult() {
        return this.result;
    }

    protected View inflate(int i, ViewGroup viewGroup) {
        return this.inflater.inflate(i, viewGroup, false);
    }

    protected void setText(View view, int i, int i2) {
        ((TextView) view.findViewById(i)).setText(i2);
    }

    protected void setText(View view, int i, String str) {
        if (str != null) {
            ((TextView) view.findViewById(i)).setText(str);
        }
    }

    protected void setText(View view, int i, byte[] bArr) {
        setText(view, i, ByteHelper.getHexString(this.context, bArr));
    }

    protected void addHexView(String str, byte[] bArr, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.titled_text_view, viewGroup);
        setText(inflate, R.id.title, str);
        setText(inflate, R.id.text, ByteHelper.getHexString(this.context, bArr));
        setText(inflate, R.id.size, this.context.getString(R.string.size, new Object[]{Integer.valueOf(bArr.length)}));
        viewGroup.addView(inflate);
    }
}
