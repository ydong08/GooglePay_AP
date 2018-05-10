package com.google.commerce.tapandpay.merchantapp.main;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.merchantapp.common.BaseActivity;
import com.google.commerce.tapandpay.merchantapp.common.Uris;
import com.google.commerce.tapandpay.merchantapp.main.cursoradapters.TestCaseListAdapter;
import com.google.commerce.tapandpay.merchantapp.main.cursoradapters.TestSuiteListAdapter;
import com.google.commerce.tapandpay.merchantapp.main.updatetasks.CursorUpdateTask.DbOperation;
import com.google.commerce.tapandpay.merchantapp.main.updatetasks.TestSuiteTask;
import com.google.commerce.tapandpay.merchantapp.main.updatetasks.UiUpdateTask;
import com.google.commerce.tapandpay.merchantapp.main.updatetasks.UiUpdateTask.UiUpdateListener;
import com.google.commerce.tapandpay.merchantapp.resultview.UserResultActivity;
import com.google.commerce.tapandpay.merchantapp.settings.SettingsActivity;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import com.google.commerce.tapandpay.merchantapp.testcase.TestSuiteHelper;
import com.google.commerce.tapandpay.merchantapp.testcaseview.TestCaseActivity;
import com.google.common.base.Strings;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;

public class MainActivity extends BaseActivity implements Callback, UiUpdateListener {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private ActionMode actionMode;
    private FloatingActionButton addTestCaseButton;
    private Button addTestSuiteButton;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private Button loadTestSuiteButton;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver suiteErrorReceiver;
    @Inject
    TestCaseHelper testCaseHelper;
    private TestCaseListAdapter testCaseListAdapter;
    private ListView testCaseListView;
    @Inject
    TestSuiteHelper testSuiteHelper;
    private TestSuiteListAdapter testSuiteListAdapter;
    private BroadcastReceiver testSuiteReceiver;
    private BroadcastReceiver updateReceiver;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.localBroadcastManager = LocalBroadcastManager.getInstance(this);
        setContentView(R.layout.main_activity);
        this.drawerListView = (ListView) findViewById(R.id.navigation_view);
        this.drawerListView.addHeaderView(getLayoutInflater().inflate(R.layout.test_suite_header_view, this.drawerListView, false));
        this.drawerListView.addFooterView(getLayoutInflater().inflate(R.layout.test_suite_footer_view, this.drawerListView, false));
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.testCaseListView = (ListView) findViewById(R.id.test_case_list_view);
        this.addTestCaseButton = (FloatingActionButton) findViewById(R.id.add_test_case_button);
        this.addTestSuiteButton = (Button) this.drawerListView.findViewById(R.id.add_test_suite_button);
        this.loadTestSuiteButton = (Button) this.drawerListView.findViewById(R.id.load_test_suite_button);
        this.testSuiteReceiver = newTestSuiteReceiver();
        this.updateReceiver = newUpdateReceiver();
        this.suiteErrorReceiver = newSuiteErrorReceiver();
        try {
            String str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            ((TextView) findViewById(R.id.version)).setText(getString(R.string.version, new Object[]{str}));
        } catch (Throwable e) {
            LOG.e(e, "Could not display version", new Object[0]);
        }
        setUpToolbar();
        setUpTestCaseList();
        setUpNavigationDrawer();
    }

    private void setUpTestCaseList() {
        this.testCaseListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent createReadResultsIntent = UserResultActivity.createReadResultsIntent(MainActivity.this, j);
                MainActivity.this.testCaseListView.setItemChecked(i, false);
                MainActivity.this.startActivity(createReadResultsIntent);
            }
        });
        this.testCaseListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (MainActivity.this.actionMode != null) {
                    MainActivity.this.actionMode.finish();
                }
                MainActivity.this.testCaseListView.setItemChecked(i, true);
                MainActivity.this.actionMode = MainActivity.this.startSupportActionMode(MainActivity.this);
                return true;
            }
        });
        this.addTestCaseButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.startActivityForResult(TestCaseActivity.createAddIntent(MainActivity.this, MainActivity.this.testSuiteListAdapter.getActiveId()), 0);
            }
        });
        this.testCaseListAdapter = new TestCaseListAdapter(this);
        this.testCaseListView.setAdapter(this.testCaseListAdapter);
    }

    private void setUpToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setHomeAsUpIndicator(R.drawable.quantum_ic_menu_white_24);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setUpNavigationDrawer() {
        this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, R.string.drawer_open, R.string.drawer_close);
        this.drawerLayout.setDrawerListener(this.drawerToggle);
        this.drawerToggle.syncState();
        this.testSuiteListAdapter = new TestSuiteListAdapter(this);
        this.drawerListView.setAdapter(this.testSuiteListAdapter);
        this.drawerListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                MainActivity.this.testSuiteListAdapter.setActiveId(j);
                MainActivity.this.createUiUpdateTask().execute(new DbOperation[0]);
                MainActivity.this.drawerLayout.closeDrawers();
            }
        });
        this.addTestSuiteButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final EditText editText = (EditText) MainActivity.this.getLayoutInflater().inflate(R.layout.test_suite_alert_view, null);
                new Builder(MainActivity.this).setTitle(MainActivity.this.getString(R.string.choose_name)).setPositiveButton(MainActivity.this.getString(R.string.create), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new TestSuiteTask(MainActivity.this.getApplication(), MainActivity.this.testSuiteListAdapter).executeInsert(editText.getText().toString());
                    }
                }).setNegativeButton(MainActivity.this.getString(R.string.cancel), null).setView(editText).show();
            }
        });
        this.loadTestSuiteButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Uris.showFilePicker(MainActivity.this, "*/*", 1);
            }
        });
    }

    private void activateSelectedTestCase() {
        this.testCaseListAdapter.setActiveId(this.testCaseListAdapter.getItemId(this.testCaseListView.getCheckedItemPosition()));
    }

    private void editSelectedTestCase() {
        startActivityForResult(TestCaseActivity.createEditIntent(this, this.testCaseListAdapter.getItemId(this.testCaseListView.getCheckedItemPosition())), 0);
    }

    private void deleteSelectedTestCase() {
        createUiUpdateTask().executeDeleteTestCase(this.testCaseListAdapter.getItemId(this.testCaseListView.getCheckedItemPosition()));
    }

    protected void onResume() {
        super.onResume();
        this.localBroadcastManager.registerReceiver(this.testSuiteReceiver, new IntentFilter("com.google.commerce.tapandpay.merchantapp.TEST_CASES_DOWNLOADED"));
        this.localBroadcastManager.registerReceiver(this.updateReceiver, new IntentFilter("com.google.commerce.tapandpay.merchantapp.UPDATE_CURSORS"));
        this.localBroadcastManager.registerReceiver(this.suiteErrorReceiver, new IntentFilter("com.google.commerce.tapandpay.merchantapp.SUITE_ERROR"));
        createUiUpdateTask().execute(new DbOperation[0]);
    }

    protected void onPause() {
        this.localBroadcastManager.unregisterReceiver(this.testSuiteReceiver);
        this.localBroadcastManager.unregisterReceiver(this.updateReceiver);
        this.localBroadcastManager.unregisterReceiver(this.suiteErrorReceiver);
        super.onPause();
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            return;
        }
        if (i == 0) {
            createUiUpdateTask().execute(new DbOperation[0]);
        } else if (i == 1) {
            new DownloadTestCasesTask(this, intent.getData()).execute(new Void[0]);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (this.drawerToggle.onOptionsItemSelected(menuItem)) {
            return true;
        }
        if (itemId == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (itemId == R.id.action_delete_test_suite) {
            createUiUpdateTask().executeDeleteActiveTestSuite();
            return true;
        } else if (itemId != R.id.action_export_test_suite) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            new ShareTestSuiteTask(this, this.testSuiteListAdapter.getActiveId()).execute(new Void[0]);
            return true;
        }
    }

    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.test_case_context_menu, menu);
        return true;
    }

    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_activate) {
            activateSelectedTestCase();
        } else if (itemId == R.id.action_edit) {
            editSelectedTestCase();
        } else {
            deleteSelectedTestCase();
        }
        actionMode.finish();
        return true;
    }

    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        this.testCaseListView.setItemChecked(this.testCaseListView.getCheckedItemPosition(), false);
    }

    private BroadcastReceiver newTestSuiteReceiver() {
        return new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra("test_cases")) {
                    MainActivity.this.createNewTestSuite((List) intent.getSerializableExtra("test_cases"));
                } else if (intent.hasExtra("test_cases_exception")) {
                    MainActivity.this.handleTestCasesException((Throwable) intent.getExtras().getSerializable("test_cases_exception"));
                } else {
                    Snackbar.make(MainActivity.this.findViewById(R.id.main_activity_layout), R.string.retrieve_test_cases_failure, 0).show();
                }
            }
        };
    }

    private void createNewTestSuite(final List<TestCase> list) {
        final View findViewById = findViewById(R.id.main_activity_layout);
        final EditText editText = (EditText) getLayoutInflater().inflate(R.layout.test_suite_alert_view, null);
        new Builder(this).setTitle(getString(R.string.choose_name)).setPositiveButton(getString(R.string.create), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String trim = editText.getText().toString().trim();
                if (Strings.isNullOrEmpty(trim)) {
                    Snackbar.make(findViewById, R.string.test_suite_name_empty, 0).show();
                    return;
                }
                long insertTestSuite = MainActivity.this.testSuiteHelper.insertTestSuite(trim);
                if (insertTestSuite < 0) {
                    Snackbar.make(findViewById, R.string.test_suite_creation_error, 0).show();
                    return;
                }
                for (TestCase toBuilder : list) {
                    MainActivity.this.testCaseHelper.insertTestCase(toBuilder.toBuilder().setTestSuiteId(insertTestSuite).build());
                }
                MainActivity.this.createUiUpdateTask().execute(new DbOperation[0]);
                Snackbar.make(findViewById, MainActivity.this.getString(R.string.test_suite_loaded_successful, new Object[]{trim}), 0).show();
            }
        }).setNegativeButton(getString(R.string.cancel), null).setView(editText).show();
    }

    private void handleTestCasesException(Throwable th) {
        CharSequence string;
        LOG.d(th, "Exception occured while attempting to load test cases.", new Object[0]);
        if (th instanceof IOException) {
            string = getString(R.string.cannot_open_test_cases, new Object[]{th.getMessage()});
        } else if (th instanceof JsonSyntaxException) {
            string = getString(R.string.test_cases_not_valid_json, new Object[]{th.getMessage()});
        } else {
            string = getString(R.string.test_cases_exception, new Object[]{th.getMessage()});
        }
        new Builder(this).setTitle(R.string.retrieve_test_cases_failure).setMessage(string).show();
    }

    private BroadcastReceiver newUpdateReceiver() {
        return new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                MainActivity.this.createUiUpdateTask().execute(new DbOperation[0]);
            }
        };
    }

    private BroadcastReceiver newSuiteErrorReceiver() {
        return new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Snackbar.make(MainActivity.this.findViewById(R.id.main_activity_layout), R.string.suite_error, 0).show();
            }
        };
    }

    private UiUpdateTask createUiUpdateTask() {
        return new UiUpdateTask(getApplication(), this.testCaseListAdapter, this.testSuiteListAdapter, this);
    }

    public void onFinishUpdate() {
        if (this.testCaseListAdapter.getCount() > 0) {
            findViewById(R.id.no_test_cases_text).setVisibility(8);
            findViewById(R.id.test_case_list_view).setVisibility(0);
            return;
        }
        findViewById(R.id.no_test_cases_text).setVisibility(0);
        findViewById(R.id.test_case_list_view).setVisibility(8);
    }
}
