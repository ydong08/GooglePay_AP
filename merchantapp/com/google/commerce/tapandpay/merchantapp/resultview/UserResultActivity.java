package com.google.commerce.tapandpay.merchantapp.resultview;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.VideoView;
import com.google.commerce.tapandpay.merchantapp.result.InsertResultTask;
import com.google.commerce.tapandpay.merchantapp.result.InsertResultTask.InsertResultListener;
import com.google.commerce.tapandpay.merchantapp.result.ReadResultsTask;
import com.google.commerce.tapandpay.merchantapp.result.ReadResultsTask.ReadResultListener;
import com.google.commerce.tapandpay.merchantapp.result.Result;
import com.google.commerce.tapandpay.merchantapp.result.Result.Results;
import com.google.commerce.tapandpay.merchantapp.testcaseview.TestCaseActivity;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;

public class UserResultActivity extends ResultActivity implements InsertResultListener, ReadResultListener {
    public void onNewIntent(Intent intent) {
        Preconditions.checkState(intent.hasExtra("result"));
        startActivity(intent);
        finish();
    }

    protected void initWithIntent() {
        Intent intent = getIntent();
        Result result = (Result) intent.getSerializableExtra("result");
        if (result != null) {
            setTestCaseId(result.testCaseId());
            setResults(new ArrayList(Collections.singletonList(result)));
            setUpResults();
            if (getTestCaseId() != -1) {
                new InsertResultTask(getApplication(), result, this).execute(new Void[0]);
            }
            if (intent.hasExtra("download_video_uri")) {
                playVideo(Uri.parse(intent.getStringExtra("download_video_uri")));
            }
        } else if (intent.hasExtra("test_case_id")) {
            setTestCaseId(intent.getLongExtra("test_case_id", -1));
            new ReadResultsTask(getApplication(), getTestCaseId(), this).execute(new Void[0]);
        } else {
            throw new IllegalArgumentException("Invalid intent: no result or test case id");
        }
    }

    private void playVideo(Uri uri) {
        final View decorView = getWindow().getDecorView();
        final int systemUiVisibility = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(6);
        VideoView videoView = (VideoView) findViewById(R.id.video_layout);
        videoView.setVisibility(0);
        findViewById(R.id.result_layout).setVisibility(8);
        videoView.setVideoURI(uri);
        videoView.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                UserResultActivity.this.findViewById(R.id.result_layout).setVisibility(0);
                UserResultActivity.this.findViewById(R.id.video_layout).setVisibility(8);
                decorView.setSystemUiVisibility(systemUiVisibility);
            }
        });
        videoView.start();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.user_result_activity, menu);
        if (getResults() == null || getResults().isEmpty()) {
            menu.findItem(R.id.action_share_json).setEnabled(false);
        }
        if (getTestCaseId() == -1) {
            menu.findItem(R.id.action_view_edit_test).setEnabled(false);
        }
        if (getExpectedResult() == null) {
            menu.findItem(R.id.action_view_expected_result).setVisible(false);
            menu.findItem(R.id.action_view_expected_result).setEnabled(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_share_json) {
            new ShareAllJsonTask(this, ImmutableList.copyOf(getResults())).execute(new Void[0]);
            return true;
        } else if (menuItem.getItemId() == R.id.action_view_edit_test) {
            startActivity(TestCaseActivity.createEditIntent(this, getTestCaseId()));
            return true;
        } else if (menuItem.getItemId() != R.id.action_view_expected_result) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            startActivity(ExpectedResultActivity.createResultIntent(this, getExpectedResult()));
            return true;
        }
    }

    public static Intent createResultIntent(Context context, String str, Result result) {
        Intent intent = new Intent(context, UserResultActivity.class);
        intent.putExtra("result", result);
        if (str != null) {
            intent.putExtra("download_video_uri", str);
        }
        return intent;
    }

    public static Intent createReadResultsIntent(Context context, long j) {
        Intent intent = new Intent(context, UserResultActivity.class);
        intent.putExtra("test_case_id", j);
        return intent;
    }

    public void onReadResults(Results results) {
        setResults(results.userResults());
        setExpectedResult(results.expected());
        setUpResults();
        invalidateOptionsMenu();
    }

    public void onInsertResult(Boolean bool) {
        if (bool.booleanValue()) {
            new ReadResultsTask(getApplication(), getTestCaseId(), this).execute(new Void[0]);
        } else {
            Snackbar.make(findViewById(R.id.result_activity_layout), getString(R.string.save_result_error), 0).show();
        }
    }
}
