package com.google.commerce.tapandpay.terminalapp.main;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.ReaderCallback;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.nfc.Transceiver;
import com.google.android.libraries.commerce.hce.terminal.settings.Preferences;
import com.google.android.libraries.commerce.hce.terminal.settings.SettingsActivity;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.terminalapp.main.IsoDepTransceiver.OnError;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener, ReaderCallback, OnError {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private LocalBroadcastManager localBroadcastManager;
    private MessageAdapter messageAdapter;
    private NfcAdapter nfcAdapter;
    private Preferences preferences;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.messageAdapter.add(NfcMessage.fromBundle(intent.getExtras()));
        }
    };

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.preferences = new Preferences(this);
        this.localBroadcastManager = LocalBroadcastManager.getInstance(this);
        setContentView(R.layout.main_activity);
        setupToolbar();
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        verifyNfcSupport();
        if (bundle != null) {
            Bundle bundle2 = bundle.getBundle("TERMINAL_LOGS_BUNDLE_KEY");
            if (bundle2 != null) {
                this.messageAdapter = MessageAdapter.fromBundle(this, bundle2);
            } else {
                this.messageAdapter = MessageAdapter.create(this);
            }
        } else {
            this.messageAdapter = MessageAdapter.create(this);
        }
        ((ListView) findViewById(R.id.terminal_log_text)).setAdapter(this.messageAdapter);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBundle("TERMINAL_LOGS_BUNDLE_KEY", this.messageAdapter.toBundle());
    }

    public void onStart() {
        super.onStart();
        this.preferences.registerChangeListener(this);
    }

    public void onResume() {
        super.onResume();
        this.localBroadcastManager.registerReceiver(this.receiver, new IntentFilter(NfcMessage.BROADCAST_NFC_MESSAGE));
        this.nfcAdapter.enableReaderMode(this, this, 129, null);
    }

    public void onPause() {
        super.onPause();
        this.nfcAdapter.disableReaderMode(this);
        this.localBroadcastManager.unregisterReceiver(this.receiver);
    }

    public void onDestroy() {
        super.onDestroy();
        this.preferences.unregisterChangeListener(this);
    }

    public void onTagDiscovered(Tag tag) {
        new IsoDepTransceiver(this, this.localBroadcastManager, new Transceiver(this, IsoDep.get(tag)), this, this.preferences).execute(new Void[0]);
    }

    public void onError(Exception exception) {
        this.messageAdapter.add(exception);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.action_settings) {
            return super.onOptionsItemSelected(menuItem);
        }
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setFlags(536870912);
        startActivity(intent);
        return true;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        this.preferences.retrieveUpdatedValue(str);
    }

    private void setupToolbar() {
        View findViewById = findViewById(R.id.toolbar);
        if (findViewById == null) {
            LOG.w("Toolbar view could not be found by ID.", new Object[0]);
        } else {
            setSupportActionBar((Toolbar) findViewById);
        }
    }

    private void verifyNfcSupport() {
        if (this.nfcAdapter == null) {
            Toast.makeText(this, R.string.nfc_not_supported, 1).show();
            LOG.w("NFC not supported", new Object[0]);
            finish();
        }
        if (!this.nfcAdapter.isEnabled()) {
            Toast.makeText(this, R.string.nfc_disabled, 1).show();
            LOG.w("NFC disabled", new Object[0]);
            finish();
        }
    }

    @TargetApi(19)
    public void saveTerminalLog(View view) {
        Throwable th;
        Throwable th2;
        BufferedWriter bufferedWriter;
        if (!this.messageAdapter.getMessages().isEmpty()) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Terminal_App_Logs");
            if (file.mkdirs() || file.isDirectory()) {
                String valueOf = String.valueOf("Log_");
                String valueOf2 = String.valueOf(DATE_FORMAT.format(new Date()));
                String valueOf3 = String.valueOf(".txt");
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter(new File(file, new StringBuilder((String.valueOf(valueOf).length() + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()).append(valueOf).append(valueOf2).append(valueOf3).toString())));
                    th = null;
                    try {
                        Iterator it = this.messageAdapter.getMessages().iterator();
                        while (it.hasNext()) {
                            bufferedWriter.write((String) it.next());
                            bufferedWriter.newLine();
                        }
                        if (bufferedWriter != null) {
                            if (th != null) {
                                try {
                                    bufferedWriter.close();
                                } catch (Throwable th22) {
                                    th.addSuppressed(th22);
                                }
                            } else {
                                bufferedWriter.close();
                            }
                        }
                        alertSaveTerminalLogResult(R.string.file_write_success, R.string.save_terminal_log_success, valueOf3, file);
                        return;
                    } catch (Throwable th3) {
                        Throwable th4 = th3;
                        th3 = th22;
                        th22 = th4;
                    }
                } catch (Throwable th222) {
                    LOG.w(th222, "File %s in directory %s could not be created.", valueOf3, file);
                    alertSaveTerminalLogResult(R.string.file_write_failure, R.string.file_not_found, valueOf3, file);
                    return;
                } catch (Throwable th2222) {
                    LOG.w(th2222, "IOException occured while attempting to write to file %s in directory %s.", valueOf3, file);
                    alertSaveTerminalLogResult(R.string.file_write_failure, R.string.io_exception, valueOf3, file);
                    return;
                }
            }
            LOG.w("Directory %s could not be created.", file);
            alertSaveTerminalLogResult(R.string.file_write_failure, R.string.could_not_create_dir, file);
            return;
        }
        return;
        throw th2222;
        if (bufferedWriter != null) {
            if (th3 != null) {
                try {
                    bufferedWriter.close();
                } catch (Throwable th5) {
                    th3.addSuppressed(th5);
                }
            } else {
                bufferedWriter.close();
            }
        }
        throw th2222;
    }

    private void alertSaveTerminalLogResult(int i, int i2, Object... objArr) {
        new Builder(this).setTitle(i).setMessage(getString(i2, objArr)).setPositiveButton(R.string.confirm_yes, null).show();
    }

    public void clearTerminalLog(View view) {
        if (!this.messageAdapter.getMessages().isEmpty()) {
            new Builder(this).setTitle(R.string.clear_confirm_title).setMessage(R.string.clear_confirm_message).setPositiveButton(R.string.confirm_yes, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.this.messageAdapter.clear();
                }
            }).setNegativeButton(R.string.confirm_no, null).show();
        }
    }
}
