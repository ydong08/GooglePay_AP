package com.google.commerce.tapandpay.merchantapp.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.xml.preferences);
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
            defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this);
            setSummaries(defaultSharedPreferences);
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            Preference findPreference = getPreferenceManager().findPreference(str);
            if (findPreference instanceof NestedEditTextPreference) {
                ((NestedEditTextPreference) findPreference).updateSummary();
            } else if (findPreference instanceof HexEditTextPreference) {
                ((HexEditTextPreference) findPreference).updateSummary();
            }
        }

        private void setSummaries(SharedPreferences sharedPreferences) {
            for (String onSharedPreferenceChanged : sharedPreferences.getAll().keySet()) {
                onSharedPreferenceChanged(sharedPreferences, onSharedPreferenceChanged);
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.settings_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.content, new SettingsFragment()).commit();
    }
}
