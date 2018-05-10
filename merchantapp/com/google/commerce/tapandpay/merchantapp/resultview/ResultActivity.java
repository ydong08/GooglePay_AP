package com.google.commerce.tapandpay.merchantapp.resultview;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import com.google.commerce.tapandpay.merchantapp.common.BaseActivity;
import com.google.commerce.tapandpay.merchantapp.result.Result;
import com.viewpagerindicator.PageIndicator;
import java.util.ArrayList;
import java.util.List;

public abstract class ResultActivity extends BaseActivity {
    private Result expectedResult;
    private ViewPager pager;
    private FragmentStatePagerAdapter resultPagerAdapter$9HHMUR9FCTNMUPRCCKNM6RRDDLIN4OR55TQ62S31DPI70OBP5TMMASJ3D1GMST31E1O2USJ5EDQMOT3MD5INEBQICLPNAR3KA1GMEPBI85I62S3KCLP3M___0;
    private List<Result> results;
    private long testCaseId;

    protected abstract void initWithIntent();

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.result_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.pager = (ViewPager) findViewById(R.id.pager);
        if (bundle == null) {
            initWithIntent();
        }
        invalidateOptionsMenu();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putLong("test_case_id", this.testCaseId);
        bundle.putSerializable("results", new ArrayList(getResults()));
        bundle.putInt("current_page", this.pager.getCurrentItem());
        bundle.putSerializable("expected_result", this.expectedResult);
    }

    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.testCaseId = bundle.getLong("test_case_id");
        this.results = (List) bundle.getSerializable("results");
        this.expectedResult = (Result) bundle.getSerializable("expected_result");
        setUpResults();
        this.pager.setCurrentItem(bundle.getInt("current_page"));
    }

    protected void setUpResults() {
        if (this.resultPagerAdapter$9HHMUR9FCTNMUPRCCKNM6RRDDLIN4OR55TQ62S31DPI70OBP5TMMASJ3D1GMST31E1O2USJ5EDQMOT3MD5INEBQICLPNAR3KA1GMEPBI85I62S3KCLP3M___0 != null) {
            this.resultPagerAdapter$9HHMUR9FCTNMUPRCCKNM6RRDDLIN4OR55TQ62S31DPI70OBP5TMMASJ3D1GMST31E1O2USJ5EDQMOT3MD5INEBQICLPNAR3KA1GMEPBI85I62S3KCLP3M___0.setResults(this.results);
            if (this.expectedResult != null) {
                this.resultPagerAdapter$9HHMUR9FCTNMUPRCCKNM6RRDDLIN4OR55TQ62S31DPI70OBP5TMMASJ3D1GMST31E1O2USJ5EDQMOT3MD5INEBQICLPNAR3KA1GMEPBI85I62S3KCLP3M___0.updateExpectedResult(this.expectedResult);
            }
        } else if (this.results == null || this.results.isEmpty()) {
            findViewById(R.id.no_result).setVisibility(0);
        } else {
            this.resultPagerAdapter$9HHMUR9FCTNMUPRCCKNM6RRDDLIN4OR55TQ62S31DPI70OBP5TMMASJ3D1GMST31E1O2USJ5EDQMOT3MD5INEBQICLPNAR3KA1GMEPBI85I62S3KCLP3M___0 = new FragmentStatePagerAdapter(getSupportFragmentManager(), getApplicationContext(), this.results, this.expectedResult);
            this.pager.setAdapter(this.resultPagerAdapter$9HHMUR9FCTNMUPRCCKNM6RRDDLIN4OR55TQ62S31DPI70OBP5TMMASJ3D1GMST31E1O2USJ5EDQMOT3MD5INEBQICLPNAR3KA1GMEPBI85I62S3KCLP3M___0);
            ((PageIndicator) findViewById(R.id.page_indicator)).setViewPager(this.pager);
        }
    }

    protected long getTestCaseId() {
        return this.testCaseId;
    }

    protected void setTestCaseId(long j) {
        this.testCaseId = j;
    }

    protected List<Result> getResults() {
        return this.results;
    }

    protected void setResults(List<Result> list) {
        this.results = list;
    }

    protected void setExpectedResult(Result result) {
        this.expectedResult = result;
    }

    protected Result getExpectedResult() {
        return this.expectedResult;
    }
}
