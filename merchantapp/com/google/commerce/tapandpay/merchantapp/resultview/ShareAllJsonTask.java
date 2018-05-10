package com.google.commerce.tapandpay.merchantapp.resultview;

import android.content.Context;
import android.content.Intent;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.commerce.tapandpay.merchantapp.result.Result;
import com.google.commerce.tapandpay.merchantapp.sharing.IntentLoadingTask;
import com.google.commerce.tapandpay.merchantapp.sharing.JsonConverter;
import java.util.List;
import javax.inject.Inject;

public class ShareAllJsonTask extends IntentLoadingTask {
    @Inject
    JsonConverter jsonConverter;
    private final List<Result> results;

    public ShareAllJsonTask(Context context, List<Result> list) {
        super(context);
        this.results = list;
        ((InjectedApplication) context.getApplicationContext()).inject(this);
    }

    protected Intent doInBackground(Void... voidArr) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", this.jsonConverter.toJsonArray(this.results));
        intent.setType("text/plain");
        return intent;
    }
}
