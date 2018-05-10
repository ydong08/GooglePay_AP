package com.google.commerce.tapandpay.merchantapp.result;

import android.app.Application;
import android.os.AsyncTask;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.commerce.tapandpay.merchantapp.result.Result.Results;
import com.google.common.collect.Lists;
import dagger.Lazy;
import javax.inject.Inject;

public class ReadResultsTask extends AsyncTask<Void, Void, Results> {
    @Inject
    Lazy<ResultHelper> lazyResultHelper;
    private final ReadResultListener listener;
    private final long testCaseId;

    public interface ReadResultListener {
        void onReadResults(Results results);
    }

    public ReadResultsTask(Application application, long j, ReadResultListener readResultListener) {
        this.testCaseId = j;
        this.listener = readResultListener;
        ((InjectedApplication) application).inject(this);
    }

    protected Results doInBackground(Void... voidArr) {
        return Results.create(Lists.reverse(((ResultHelper) this.lazyResultHelper.get()).readUserResults(this.testCaseId)), ((ResultHelper) this.lazyResultHelper.get()).readExpectedResult(this.testCaseId));
    }

    protected void onPostExecute(Results results) {
        this.listener.onReadResults(results);
    }
}
