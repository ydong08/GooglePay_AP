package com.google.commerce.tapandpay.merchantapp.main.updatetasks;

import android.app.Application;
import android.database.Cursor;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.commerce.tapandpay.merchantapp.main.cursoradapters.TestCaseListAdapter;
import com.google.commerce.tapandpay.merchantapp.main.cursoradapters.TestSuiteListAdapter;
import com.google.commerce.tapandpay.merchantapp.main.updatetasks.CursorUpdateTask.DbOperation;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import com.google.commerce.tapandpay.merchantapp.testcase.TestSuiteHelper;
import com.google.common.base.Optional;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class UiUpdateTask extends CursorUpdateTask {
    private final Application context;
    @Inject
    Lazy<TestCaseHelper> lazyTestCaseHelper;
    @Inject
    Lazy<TestSuiteHelper> lazyTestSuiteHelper;
    private final UiUpdateListener listener;
    private final TestCaseListAdapter testCaseListAdapter;
    private long testSuiteId;
    private final TestSuiteListAdapter testSuiteListAdapter;

    public interface UiUpdateListener {
        void onFinishUpdate();
    }

    public UiUpdateTask(Application application, TestCaseListAdapter testCaseListAdapter, TestSuiteListAdapter testSuiteListAdapter, UiUpdateListener uiUpdateListener) {
        super(testCaseListAdapter);
        this.context = application;
        this.testCaseListAdapter = testCaseListAdapter;
        this.testSuiteListAdapter = testSuiteListAdapter;
        this.listener = uiUpdateListener;
        ((InjectedApplication) application).inject(this);
    }

    protected Cursor doInBackground(DbOperation... dbOperationArr) {
        List arrayList = new ArrayList(Arrays.asList(dbOperationArr));
        arrayList.add(new DbOperation() {
            public void run() {
                UiUpdateTask.this.testSuiteId = UiUpdateTask.this.testSuiteListAdapter.getActiveId();
                if (UiUpdateTask.this.testSuiteId == -1) {
                    Optional firstTestSuiteId = ((TestSuiteHelper) UiUpdateTask.this.lazyTestSuiteHelper.get()).getFirstTestSuiteId();
                    if (firstTestSuiteId.isPresent()) {
                        UiUpdateTask.this.testSuiteId = ((Long) firstTestSuiteId.get()).longValue();
                    } else {
                        UiUpdateTask.this.testSuiteId = ((TestSuiteHelper) UiUpdateTask.this.lazyTestSuiteHelper.get()).insertTestSuite(UiUpdateTask.this.context.getString(R.string.default_test_suite_name));
                    }
                }
            }
        });
        return super.doInBackground((DbOperation[]) arrayList.toArray(new DbOperation[arrayList.size()]));
    }

    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        this.testSuiteListAdapter.setActiveId(this.testSuiteId);
        new TestSuiteTask(this.context, this.testSuiteListAdapter).execute(new DbOperation[0]);
        this.listener.onFinishUpdate();
    }

    public Cursor getCursor() {
        return ((TestCaseHelper) this.lazyTestCaseHelper.get()).queryTestCaseSuite(this.testSuiteId);
    }

    public void executeDeleteTestCase(final long j) {
        execute(new DbOperation[]{new DbOperation() {
            public void run() {
                ((TestCaseHelper) UiUpdateTask.this.lazyTestCaseHelper.get()).deleteTestCase(j);
                if (UiUpdateTask.this.testCaseListAdapter.getActiveId() == j) {
                    UiUpdateTask.this.testCaseListAdapter.deleteActiveId();
                }
            }
        }});
    }

    public void executeDeleteActiveTestSuite() {
        execute(new DbOperation[]{new DbOperation() {
            public void run() {
                ((TestSuiteHelper) UiUpdateTask.this.lazyTestSuiteHelper.get()).deleteTestSuite(UiUpdateTask.this.testSuiteListAdapter.getActiveId());
                UiUpdateTask.this.testSuiteListAdapter.deleteActiveId();
            }
        }});
    }
}
