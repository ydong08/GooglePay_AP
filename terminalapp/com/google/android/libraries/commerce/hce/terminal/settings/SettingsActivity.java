package com.google.android.libraries.commerce.hce.terminal.settings;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private EncryptionKeyPreference encryptionKeyPreference;
    private SettingsFragment settingsFragment;

    public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.layout.preference);
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
            defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this);
            setSummaries(defaultSharedPreferences);
            setFilePickerActivity("preference_terminal_long_term_private_key");
            setFilePickerActivity("preference_terminal_ephemeral_private_key");
            try {
                String str = SettingsActivity.this.getPackageManager().getPackageInfo(SettingsActivity.this.getPackageName(), 0).versionName;
                ((PreferenceCategory) findPreference("preference_category_version")).setTitle(getString(R.string.version, new Object[]{str}));
            } catch (Throwable e) {
                SettingsActivity.LOG.e(e, "Could not display version", new Object[0]);
            }
            setupObjectIdsPreference("preference_unspecified_usage_ids", R.string.title_unspecified_usage);
            setupObjectIdsPreference("preference_success_ids", R.string.title_success);
            setupObjectIdsPreference("preference_invalid_format_ids", R.string.title_invalid_format);
            setupObjectIdsPreference("preference_invalid_value_ids", R.string.title_invalid_value);
            setupObjectIdsPreference("preference_no_op_update_ids", R.string.title_add_update_no_op);
            setupObjectIdsPreference("preference_remove_object_update_ids", R.string.title_add_update_remove_object);
            setupObjectIdsPreference("preference_set_balance_update_ids", R.string.title_add_update_set_balance);
            setupObjectIdsPreference("preference_add_balance_update_ids", R.string.title_add_update_add_balance);
            setupObjectIdsPreference("preference_subtract_balance_update_ids", R.string.title_add_update_subtract_balance);
            setupObjectIdsPreference("preference_free_update_ids", R.string.title_add_update_free);
            Bundle arguments = getArguments();
            if (arguments != null) {
                str = arguments.getString("Preference_File", "");
                if (!Strings.isNullOrEmpty(str)) {
                    SettingsActivity.this.loadSettings(Uri.fromFile(new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Terminal_Settings"), str)));
                    startActivity(SettingsActivity.this.getParentActivityIntent().addFlags(67108864));
                }
            }
        }

        public void onDestroy() {
            super.onDestroy();
            clearFilePickerActivity("preference_terminal_long_term_private_key");
            clearFilePickerActivity("preference_terminal_ephemeral_private_key");
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            Preference findPreference = findPreference(str);
            if (str.equals("preference_send_service_request") && (findPreference instanceof CheckBoxPreference)) {
                setRequestServicesEnabled(((CheckBoxPreference) findPreference).isChecked());
            } else if (findPreference instanceof MerchantIdPreference) {
                ((MerchantIdPreference) findPreference).updateMerchantId();
            } else if (findPreference instanceof MerchantCategoryPreference) {
                ((MerchantCategoryPreference) findPreference).updateCategory();
            } else if (findPreference instanceof EditTextPreference) {
                CharSequence string = sharedPreferences.getString(str, "");
                if (string != null) {
                    ((EditTextPreference) findPreference).setSummary(string);
                }
            }
        }

        private void setSummaries(SharedPreferences sharedPreferences) {
            for (String onSharedPreferenceChanged : sharedPreferences.getAll().keySet()) {
                onSharedPreferenceChanged(sharedPreferences, onSharedPreferenceChanged);
            }
        }

        private void setFilePickerActivity(String str) {
            ((EncryptionKeyPreference) findPreference(str)).setFilePickerActivity(SettingsActivity.this);
        }

        private void clearFilePickerActivity(String str) {
            ((EncryptionKeyPreference) findPreference(str)).setFilePickerActivity(null);
        }

        private void setRequestServicesEnabled(boolean z) {
            findPreference("preference_request_all_valuables").setEnabled(z);
            findPreference("preference_request_all_valuables_except_ppse").setEnabled(z);
            findPreference("preference_request_customer_info").setEnabled(z);
            findPreference("preference_request_loyalty_programs").setEnabled(z);
            findPreference("preference_request_offers").setEnabled(z);
            findPreference("preference_request_gift_cards").setEnabled(z);
            findPreference("preference_request_private_label_cards").setEnabled(z);
        }

        public void setAllPreferences(Map<String, ?> map) {
            SettingsActivity.LOG.i("values: %s", map);
            for (Entry entry : map.entrySet()) {
                String str = (String) entry.getKey();
                Object value = entry.getValue();
                if (str != null) {
                    Preference findPreference = findPreference(str);
                    if (findPreference == null) {
                        SettingsActivity.LOG.w("Preference for key %s could not be found. Value will be ignored.", str);
                    } else if (findPreference instanceof CheckBoxPreference) {
                        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference;
                        if (value == null) {
                            checkBoxPreference.setChecked(true);
                        } else if (value instanceof Boolean) {
                            checkBoxPreference.setChecked(((Boolean) value).booleanValue());
                        } else {
                            SettingsActivity.LOG.e("Preference for key %s expected to store value of type Boolean,  but instead contains value of type %s. Value will be ignored.", str, value.getClass().getName());
                        }
                    } else if (findPreference instanceof EditTextPreference) {
                        EditTextPreference editTextPreference = (EditTextPreference) findPreference;
                        if (value == null) {
                            editTextPreference.setText("");
                        } else if (value instanceof String) {
                            editTextPreference.setText((String) value);
                        } else {
                            SettingsActivity.LOG.e("Preference for key %s expected to store value of type String,  but instead contains value of type %s. Value will be ignored.", str, value.getClass().getName());
                        }
                    } else if (findPreference instanceof ListPreference) {
                        ((ListPreference) findPreference).setValue((String) value);
                    } else if (findPreference instanceof ObjectIdSetPreference) {
                        ObjectIdSetPreference objectIdSetPreference = (ObjectIdSetPreference) findPreference;
                        if (value instanceof Collection) {
                            Collection arrayList = new ArrayList();
                            for (Object value2 : (Collection) value2) {
                                if (value2 instanceof String) {
                                    arrayList.add((String) value2);
                                } else {
                                    SettingsActivity.LOG.e("Preference for key %s expected to contain only Strings, but it contains a value of type %s. Value will be ignored.", str, value2.getClass().getName());
                                }
                            }
                            objectIdSetPreference.setStringValues(arrayList);
                        } else {
                            SettingsActivity.LOG.e("Preference for key %s expected to store value of type Collection<?>, but instead contains value of type %s. Value will be ignored.", str, value2.getClass().getName());
                        }
                    } else {
                        SettingsActivity.LOG.e("Unknown preference key %s of type %s. Ignoring set preferences request.", str, value2.getClass().getName());
                    }
                }
            }
        }

        private void setupObjectIdsPreference(String str, int i) {
            ((ObjectIdSetPreference) findPreference(str)).setTitleFormatResId(i);
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.settings_activity);
        View findViewById = findViewById(R.id.toolbar);
        Preconditions.checkNotNull(findViewById);
        Preconditions.checkState(findViewById instanceof Toolbar);
        setSupportActionBar((Toolbar) findViewById);
        findViewById(R.id.save_settings).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingsActivity.this.saveSettings();
            }
        });
        findViewById(R.id.load_settings).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingsActivity.this.showLoadSettingsFilePicker();
            }
        });
        PreferenceManager.setDefaultValues(this, R.layout.preference, false);
        this.settingsFragment = new SettingsFragment();
        Intent intent = getIntent();
        if (Objects.equals(intent.getAction(), "com.google.android.libraries.commerce.hce.terminal.settings.LOAD") && intent.getData() != null) {
            String queryParameter = intent.getData().getQueryParameter("filename");
            if (Strings.isNullOrEmpty(queryParameter)) {
                LOG.i("No settings file to load", new Object[0]);
            } else {
                Bundle bundle2 = new Bundle();
                bundle2.putString("Preference_File", queryParameter);
                this.settingsFragment.setArguments(bundle2);
            }
        }
        getFragmentManager().beginTransaction().replace(R.id.content, this.settingsFragment).commit();
    }

    @TargetApi(19)
    public void saveSettings() {
        String string = getString(R.string.default_filename);
        String string2 = getString(R.string.default_filename_extension);
        final View editText = new EditText(this);
        String valueOf = String.valueOf(string);
        string2 = String.valueOf(string2);
        editText.setText(string2.length() != 0 ? valueOf.concat(string2) : new String(valueOf));
        editText.setSelection(0, string.length());
        new Builder(this).setTitle(R.string.preferences_save_filename_title).setView(editText).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String obj = editText.getText().toString();
                if (Strings.isNullOrEmpty(obj)) {
                    new Builder(SettingsActivity.this).setTitle(R.string.invalid_preferences_filename).setMessage(R.string.preferences_not_saved).setPositiveButton(R.string.ok, null).show();
                } else {
                    SettingsActivity.this.saveSettings(obj);
                }
            }
        }).setNegativeButton(R.string.cancel, null).show();
    }

    private void saveSettings(final String str) {
        LOG.d("Settings save file name: %s", str);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Terminal_Settings");
        if (!file.mkdirs() && !file.isDirectory()) {
            LOG.w("Directory %s could not be created.", file);
        } else if (!Strings.isNullOrEmpty(str)) {
            final File file2 = new File(file, str);
            if (file2.exists()) {
                new Builder(this).setTitle(R.string.preferences_overwrite_file).setMessage(getString(R.string.preferences_file_already_exists, new Object[]{str})).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SettingsActivity.this.saveSettings(str, file2);
                    }
                }).setNegativeButton(R.string.cancel, null).show();
                return;
            }
            saveSettings(str, file2);
        }
    }

    private void saveSettings(String str, File file) {
        Object all = PreferenceManager.getDefaultSharedPreferences(this).getAll();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(new Gson().toJson(all).getBytes());
            fileOutputStream.close();
            new Builder(this).setTitle(getString(R.string.preferences_saved, new Object[]{str})).setPositiveButton(R.string.ok, null).show();
        } catch (Throwable e) {
            LOG.d("Failed to save preferences: %s", Throwables.getStackTraceAsString(e));
            new Builder(this).setTitle(R.string.preferences_failed_to_save).setMessage(e.getMessage()).setPositiveButton(R.string.ok, null).show();
        }
    }

    @TargetApi(19)
    private void loadSettings(Uri uri) {
        try {
            InputStream openInputStream = getContentResolver().openInputStream(uri);
            int available = openInputStream.available();
            byte[] bArr = new byte[available];
            openInputStream.read(bArr, 0, available);
            openInputStream.close();
            try {
                this.settingsFragment.setAllPreferences((Map) new Gson().fromJson(new String(bArr), Map.class));
                new Builder(this).setTitle(getString(R.string.preferences_loaded_title, new Object[]{uri.getLastPathSegment()})).setPositiveButton(R.string.ok, null).show();
            } catch (Throwable e) {
                LOG.d("Failed to parse preferences from URI %s: %s", uri, Throwables.getStackTraceAsString(e));
                new Builder(this).setTitle(R.string.preferences_failed_to_parse).setMessage(e.getMessage()).setPositiveButton(R.string.ok, null).show();
            }
        } catch (Throwable e2) {
            LOG.d("Failed to load preferences from URI %s: %s", uri, Throwables.getStackTraceAsString(e2));
            new Builder(this).setTitle(R.string.preferences_failed_to_load).setMessage(e2.getMessage()).setPositiveButton(R.string.ok, null).show();
        }
    }

    void showEncryptionFilePicker(EncryptionKeyPreference encryptionKeyPreference) {
        this.encryptionKeyPreference = encryptionKeyPreference;
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        intent.addCategory("android.intent.category.OPENABLE");
        showFilePicker(1, intent);
    }

    private void showLoadSettingsFilePicker() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        intent.addCategory("android.intent.category.OPENABLE");
        showFilePicker(2, intent);
    }

    private void showFilePicker(int i, Intent intent) {
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file_manager_installed), 0).show();
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
            Uri data = intent.getData();
            if (i == 1) {
                this.encryptionKeyPreference.readKeyFromFile(data);
            } else if (i == 2) {
                loadSettings(data);
            }
        }
        super.onActivityResult(i, i2, intent);
    }
}
