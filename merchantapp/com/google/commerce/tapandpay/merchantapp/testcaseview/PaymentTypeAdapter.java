package com.google.commerce.tapandpay.merchantapp.testcaseview;

import android.content.Context;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;

public class PaymentTypeAdapter extends ObjectStringAdapter<CommandType> {
    public PaymentTypeAdapter(Context context) {
        super(context, CommandType.PAYMENT_TYPES);
    }

    public String getTitle(CommandType commandType) {
        return getContext().getString(commandType.titleResId());
    }
}
