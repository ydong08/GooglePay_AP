package com.google.commerce.tapandpay.merchantapp.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.merchantapp.sharing.IntentLoadingTask;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Scanner;

public class DownloadTestCasesTask extends IntentLoadingTask {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final Uri testCasesUri;

    public DownloadTestCasesTask(Context context, Uri uri) {
        super(context);
        this.testCasesUri = uri;
        ((InjectedApplication) context.getApplicationContext()).inject(this);
    }

    protected Intent doInBackground(Void... voidArr) {
        Serializable e;
        Intent intent = new Intent("com.google.commerce.tapandpay.merchantapp.TEST_CASES_DOWNLOADED");
        InputStream inputStream = null;
        try {
            inputStream = getContext().getContentResolver().openInputStream(this.testCasesUri);
            Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
            if (useDelimiter.hasNext()) {
                intent.putExtra("test_cases", (Serializable) TestCase.fromJsonArray(useDelimiter.next()));
            } else {
                LOG.d("Test cases file was empty.", new Object[0]);
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    LOG.w("Failed to close test case file.", new Object[0]);
                }
            }
        } catch (IOException e3) {
            e = e3;
            try {
                intent.putExtra("test_cases_exception", e);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e4) {
                        LOG.w("Failed to close test case file.", new Object[0]);
                    }
                }
                return intent;
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e5) {
                        LOG.w("Failed to close test case file.", new Object[0]);
                    }
                }
            }
        } catch (JsonSyntaxException e6) {
            e = e6;
            intent.putExtra("test_cases_exception", e);
            if (inputStream != null) {
                inputStream.close();
            }
            return intent;
        }
        return intent;
    }

    protected void onPostExecute(Intent intent) {
        super.onPostExecute(null);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }
}
