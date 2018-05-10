package com.google.commerce.tapandpay.merchantapp.testcaseview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.commerce.tapandpay.merchantapp.sharing.IntentLoadingTask;
import com.google.commerce.tapandpay.merchantapp.validation.Schema;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.Serializable;

public class DownloadValidationSchemaTask extends IntentLoadingTask {
    private final Uri schemaUri;

    public DownloadValidationSchemaTask(Context context, Uri uri) {
        super(context);
        this.schemaUri = uri;
        ((InjectedApplication) context.getApplicationContext()).inject(this);
    }

    protected Intent doInBackground(Void... voidArr) {
        Serializable e;
        Intent intent = new Intent("com.google.commerce.tapandpay.merchantapp.VALIDATION_SCHEMA_DOWNLOADED");
        try {
            intent.putExtra("validation_schema", Schema.fromUri(getContext().getContentResolver(), this.schemaUri));
        } catch (IOException e2) {
            e = e2;
            intent.putExtra("validation_schema_exception", e);
            return intent;
        } catch (JsonSyntaxException e3) {
            e = e3;
            intent.putExtra("validation_schema_exception", e);
            return intent;
        }
        return intent;
    }

    protected void onPostExecute(Intent intent) {
        super.onPostExecute(null);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }
}
