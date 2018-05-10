package com.google.commerce.tapandpay.merchantapp.result;

import android.app.Application;
import android.os.AsyncTask;
import com.google.android.apps.common.inject.InjectedApplication;
import dagger.Lazy;
import javax.inject.Inject;

public class InsertResultTask extends AsyncTask<Void, Void, Boolean> {
    @Inject
    Lazy<ResultHelper> lazyResultHelper;
    private final InsertResultListener listener;
    private final Result result;

    public interface InsertResultListener {
        void onInsertResult(Boolean bool);
    }

    public InsertResultTask(Application application, Result result, InsertResultListener insertResultListener) {
        this.result = result;
        this.listener = insertResultListener;
        ((InjectedApplication) application).inject(this);
    }

    protected Boolean doInBackground(Void... voidArr) {
        return Boolean.valueOf(((ResultHelper) this.lazyResultHelper.get()).insertUserResult(this.result) >= 0);
    }

    protected void onPostExecute(Boolean bool) {
        this.listener.onInsertResult(bool);
    }
}
