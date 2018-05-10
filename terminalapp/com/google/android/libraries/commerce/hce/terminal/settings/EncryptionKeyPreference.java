package com.google.android.libraries.commerce.hce.terminal.settings;

import android.content.Context;
import android.net.Uri;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ConscryptInstaller;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2CryptoFileUtils;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager.CryptoClientWrapper;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager.KeyFactoryWrapper;
import com.google.android.libraries.commerce.hce.crypto.ValuablesCryptoException;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.io.IOException;

public class EncryptionKeyPreference extends EditTextPreference {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private SettingsActivity settingsActivity = null;

    public EncryptionKeyPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public EncryptionKeyPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public EncryptionKeyPreference(Context context) {
        super(context);
    }

    public void setFilePickerActivity(SettingsActivity settingsActivity) {
        this.settingsActivity = settingsActivity;
    }

    public void readKeyFromFile(Uri uri) {
        Throwable e;
        try {
            new SmartTap2ConscryptInstaller(getContext()).installConscryptIfNeeded();
            SmartTap2ECKeyManager smartTap2ECKeyManager = new SmartTap2ECKeyManager(new CryptoClientWrapper(), new KeyFactoryWrapper());
            getEditText().setText(smartTap2ECKeyManager.hexRepresentation(SmartTap2CryptoFileUtils.readPrivateKey(getContext(), uri, smartTap2ECKeyManager)));
            return;
        } catch (IOException e2) {
            e = e2;
        } catch (ValuablesCryptoException e3) {
            e = e3;
        }
        FormattingLogger formattingLogger = LOG;
        String valueOf = String.valueOf(uri);
        formattingLogger.w(e, new StringBuilder(String.valueOf(valueOf).length() + 26).append("Could not read from file: ").append(valueOf).toString(), new Object[0]);
        Toast.makeText(getContext(), getContext().getString(R.string.file_read_error), 0).show();
    }

    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        if (this.settingsActivity != null) {
            ViewGroup viewGroup = (ViewGroup) getEditText().getParent();
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.file_picker, viewGroup, false);
            viewGroup.addView(inflate);
            ((Button) inflate.findViewById(R.id.encryption_key_file_button)).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    EncryptionKeyPreference.this.settingsActivity.showEncryptionFilePicker(EncryptionKeyPreference.this);
                }
            });
        }
    }
}
