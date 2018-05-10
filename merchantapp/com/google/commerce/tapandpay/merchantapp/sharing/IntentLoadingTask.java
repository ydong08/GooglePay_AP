package com.google.commerce.tapandpay.merchantapp.sharing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public abstract class IntentLoadingTask extends AsyncTask<Void, Void, Intent> {
    private final Context context;
    private final ProgressDialog progressDialog;

    public IntentLoadingTask(Context context) {
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setMessage(context.getString(R.string.loading));
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setCancelable(false);
        this.context = context;
    }

    protected void onPreExecute() {
        this.progressDialog.show();
    }

    protected void onPostExecute(Intent intent) {
        this.progressDialog.dismiss();
        if (intent != null) {
            this.context.startActivity(intent);
        }
    }

    protected Context getContext() {
        return this.context;
    }
}
