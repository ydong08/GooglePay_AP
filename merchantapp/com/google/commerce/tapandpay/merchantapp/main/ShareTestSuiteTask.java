package com.google.commerce.tapandpay.merchantapp.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.commerce.tapandpay.merchantapp.result.ResultHelper;
import com.google.commerce.tapandpay.merchantapp.sharing.FileManager;
import com.google.commerce.tapandpay.merchantapp.sharing.IntentLoadingTask;
import com.google.commerce.tapandpay.merchantapp.sharing.JsonConverter;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ShareTestSuiteTask extends IntentLoadingTask {
    @Inject
    FileManager fileManager;
    @Inject
    JsonConverter jsonConverter;
    @Inject
    Lazy<ResultHelper> lazyResultHelper;
    @Inject
    Lazy<TestCaseHelper> lazyTestCaseHelper;
    private final long testSuiteId;

    public ShareTestSuiteTask(Context context, long j) {
        super(context);
        this.testSuiteId = j;
        ((InjectedApplication) context.getApplicationContext()).inject(this);
    }

    protected Intent doInBackground(Void... voidArr) {
        List readTestCasesFromSuite = ((TestCaseHelper) this.lazyTestCaseHelper.get()).readTestCasesFromSuite(this.testSuiteId);
        List readUserResultsFromTestSuite = ((ResultHelper) this.lazyResultHelper.get()).readUserResultsFromTestSuite(this.testSuiteId);
        Uri createTextFile = this.fileManager.createTextFile(TestCase.toJson(readTestCasesFromSuite), "test-cases", ".json");
        Uri createTextFile2 = this.fileManager.createTextFile(this.jsonConverter.toJsonArray(readUserResultsFromTestSuite), "results", ".json");
        if (createTextFile == null || createTextFile2 == null) {
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("com.google.commerce.tapandpay.merchantapp.SUITE_ERROR"));
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(createTextFile);
        arrayList.add(createTextFile2);
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        intent.setType("text/plain");
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        return intent;
    }
}
