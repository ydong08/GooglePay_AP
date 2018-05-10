package com.google.commerce.tapandpay.merchantapp.resultview;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import com.google.commerce.tapandpay.merchantapp.result.Result;
import com.google.commerce.tapandpay.merchantapp.testcaseview.TestCaseActivity;
import java.util.Collections;

public class ExpectedResultActivity extends ResultActivity {
    protected void initWithIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("result")) {
            findViewById(R.id.page_indicator).setVisibility(8);
            Result result = (Result) intent.getSerializableExtra("result");
            setResults(Collections.singletonList(result));
            setTestCaseId(result.testCaseId());
            setUpResults();
            return;
        }
        throw new IllegalArgumentException("Invalid intent: no result");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.expected_result_activity, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.action_view_edit_test) {
            return super.onOptionsItemSelected(menuItem);
        }
        startActivity(TestCaseActivity.createEditIntent(this, getTestCaseId()));
        return true;
    }

    public static Intent createResultIntent(Context context, Result result) {
        Intent intent = new Intent(context, ExpectedResultActivity.class);
        intent.putExtra("result", result);
        return intent;
    }
}
