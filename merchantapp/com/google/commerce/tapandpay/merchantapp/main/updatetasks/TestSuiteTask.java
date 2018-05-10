package com.google.commerce.tapandpay.merchantapp.main.updatetasks;

import android.app.Application;
import android.database.Cursor;
import android.widget.Toast;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.commerce.tapandpay.merchantapp.main.cursoradapters.TestSuiteListAdapter;
import com.google.commerce.tapandpay.merchantapp.main.updatetasks.CursorUpdateTask.DbOperation;
import com.google.commerce.tapandpay.merchantapp.testcase.TestSuiteHelper;
import dagger.Lazy;
import javax.inject.Inject;

public class TestSuiteTask extends CursorUpdateTask {
    private final Application context;
    private boolean insertSuiteExists;
    @Inject
    Lazy<TestSuiteHelper> lazyTestSuiteHelper;

    public TestSuiteTask(Application application, TestSuiteListAdapter testSuiteListAdapter) {
        super(testSuiteListAdapter);
        ((InjectedApplication) application).inject(this);
        this.context = application;
    }

    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        if (this.insertSuiteExists) {
            Toast.makeText(this.context, R.string.suite_exists, 1).show();
        }
    }

    public Cursor getCursor() {
        return ((TestSuiteHelper) this.lazyTestSuiteHelper.get()).queryAllTestSuites();
    }

    public void executeInsert(final String str) {
        execute(new DbOperation[]{new DbOperation() {
            public void run() {
                if (((TestSuiteHelper) TestSuiteTask.this.lazyTestSuiteHelper.get()).getTestSuiteId(str).isPresent()) {
                    TestSuiteTask.this.insertSuiteExists = true;
                } else {
                    ((TestSuiteHelper) TestSuiteTask.this.lazyTestSuiteHelper.get()).insertTestSuite(str);
                }
            }
        }});
    }
}
