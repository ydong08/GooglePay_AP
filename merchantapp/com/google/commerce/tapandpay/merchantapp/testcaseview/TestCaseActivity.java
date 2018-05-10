package com.google.commerce.tapandpay.merchantapp.testcaseview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefRecord;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ConscryptInstaller;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2CryptoFileUtils;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager.CryptoClientWrapper;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager.KeyFactoryWrapper;
import com.google.android.libraries.commerce.hce.crypto.ValuablesCryptoException;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.merchantapp.common.BaseActivity;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.common.Uris;
import com.google.commerce.tapandpay.merchantapp.testcase.EncodingValue.Encoding;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase.GetSmartTapDataResponse;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import com.google.commerce.tapandpay.merchantapp.validation.Schema;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.gson.JsonSyntaxException;
import com.google.internal.tapandpay.v1.valuables.nano.CommonProto.SmartTap;
import dagger.Lazy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.inject.Inject;

public class TestCaseActivity extends BaseActivity {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private LinearLayout addNdefLayout;
    private SwitchCompat allowSkippingSmartTap2SelectSwitch;
    private SwitchCompat capabilitiesSwitch;
    private SwitchCompat checkMerchantIdSwitch;
    @Inject
    SmartTap2ConscryptInstaller conscryptInstaller;
    private TextInputLayout consumerIdLayout;
    private TextInputLayout customAidPriorityLayout;
    private TextInputLayout customAidValueLayout;
    private LinearLayout customStatusLayout;
    private TextInputLayout deviceIdLayout;
    private BroadcastReceiver downloadReceiver;
    private LinearLayout failPaymentsLayout;
    private SwitchCompat failPaymentsSwitch;
    private Spinner getDataResponseStatusSpinner;
    private LinearLayout getSmartTapDataResponseLayout;
    private LinearLayout giftCardsLayout;
    private SwitchCompat includeMasterNonceSwitch;
    private SwitchCompat includeNonceInProprietaryDataSwitch;
    @Inject
    Lazy<TestCaseHelper> lazyTestCaseHelper;
    private LinearLayout loyaltyCardsLayout;
    private TextInputLayout merchantIdLayout;
    private LinearLayout merchantIdSettingsLayout;
    private byte[] merchantPublicKey;
    private SwitchCompat modifyGetSmartTapDataResponseSwitch;
    private LinearLayout offersLayout;
    private long originalId = -1;
    private SwitchCompat oseCustomAidSwitch;
    private SwitchCompat oseSmarttap13Switch;
    private SwitchCompat oseSmarttap20Switch;
    private TextInputLayout overrideOseStatusWordLayout;
    private SwitchCompat overrideOseStatusWordSwitch;
    private SwitchCompat overrideTransactionModeSwitch;
    private SwitchCompat paymentEnabledSwitch;
    private SwitchCompat paymentRequestedSwitch;
    private Uri pendingTapVideoUri;
    private LinearLayout plcsLayout;
    private LinearLayout removeNdefLayout;
    private Button removePublicKeyButton;
    private Button removeTapVideoButton;
    private Button removeValidationFileButton;
    private SwitchCompat requireEncryptionSwitch;
    private SwitchCompat requireLiveAuthenticationSwitch;
    private Button saveButton;
    private Button setPublicKeyButton;
    private Button setTapVideoButton;
    private Button setValidationFileButton;
    private TextInputLayout smarttap13PriorityLayout;
    private TextInputLayout smarttap20PriorityLayout;
    private TextInputLayout smarttapMaxVersionLayout;
    private TextInputLayout smarttapMinVersionLayout;
    private Spinner stopPaymentAfterApduSpinner;
    private SwitchCompat supportEciesSwitch;
    private SwitchCompat supportGenericKeyAuthSwitch;
    private SwitchCompat supportSmarttap13Switch;
    private SwitchCompat supportSmarttap20Switch;
    private TextInputLayout tapIdLayout;
    private Uri tapVideoUri;
    private EditText testCaseExpectedTerminalBehaviorEditText;
    private EditText testCaseNameEditText;
    private long testSuiteId;
    private LinearLayout transactionModeSettingsLayout;
    private Map<Integer, LinearLayout> typeLayouts;
    private SwitchCompat useOseSwitch;
    private BroadcastReceiver validationReceiver;
    private Schema validationSchema;
    private SwitchCompat vasEnabledSwitch;
    private SwitchCompat vasRequestedSwitch;
    private VideoView videoView;
    private EditText waitingEditText;

    class DbQueryTask extends AsyncTask<Void, Void, TestCase> {
        private final long testCaseId;

        public DbQueryTask(long j) {
            this.testCaseId = j;
        }

        protected TestCase doInBackground(Void... voidArr) {
            return ((TestCaseHelper) TestCaseActivity.this.lazyTestCaseHelper.get()).readFullTestCase(this.testCaseId);
        }

        protected void onPostExecute(TestCase testCase) {
            if (testCase != null) {
                TestCaseActivity.this.restoreTestCase(testCase);
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_case_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.testCaseNameEditText = (EditText) findViewById(R.id.test_case_name);
        this.testCaseExpectedTerminalBehaviorEditText = (EditText) findViewById(R.id.test_case_expected_terminal_behavior);
        this.smarttap13PriorityLayout = (TextInputLayout) findViewById(R.id.ose_smarttap_v1_3_priority_layout);
        this.smarttap20PriorityLayout = (TextInputLayout) findViewById(R.id.ose_smarttap_v2_0_priority_layout);
        this.customAidValueLayout = (TextInputLayout) findViewById(R.id.ose_custom_aid_value_layout);
        this.customAidPriorityLayout = (TextInputLayout) findViewById(R.id.ose_custom_aid_priority_layout);
        this.smarttapMinVersionLayout = (TextInputLayout) findViewById(R.id.smarttap_min_version_layout);
        this.smarttapMaxVersionLayout = (TextInputLayout) findViewById(R.id.smarttap_max_version_layout);
        this.consumerIdLayout = (TextInputLayout) findViewById(R.id.consumer_id_layout);
        this.tapIdLayout = (TextInputLayout) findViewById(R.id.tap_id_layout);
        this.deviceIdLayout = (TextInputLayout) findViewById(R.id.device_id_layout);
        this.customStatusLayout = (LinearLayout) findViewById(R.id.custom_statuses);
        this.addNdefLayout = (LinearLayout) findViewById(R.id.added_ndefs);
        this.removeNdefLayout = (LinearLayout) findViewById(R.id.removed_ndefs);
        this.loyaltyCardsLayout = (LinearLayout) findViewById(R.id.loyalty_cards);
        this.offersLayout = (LinearLayout) findViewById(R.id.offers);
        this.giftCardsLayout = (LinearLayout) findViewById(R.id.gift_cards);
        this.plcsLayout = (LinearLayout) findViewById(R.id.plcs);
        this.supportGenericKeyAuthSwitch = (SwitchCompat) findViewById(R.id.support_generic_key_auth);
        this.supportEciesSwitch = (SwitchCompat) findViewById(R.id.support_ecies);
        this.overrideTransactionModeSwitch = (SwitchCompat) findViewById(R.id.override_transaction_mode);
        this.transactionModeSettingsLayout = (LinearLayout) findViewById(R.id.transaction_mode_settings_layout);
        this.paymentEnabledSwitch = (SwitchCompat) findViewById(R.id.payment_enabled);
        this.paymentRequestedSwitch = (SwitchCompat) findViewById(R.id.payment_requested);
        this.vasEnabledSwitch = (SwitchCompat) findViewById(R.id.vas_enabled);
        this.vasRequestedSwitch = (SwitchCompat) findViewById(R.id.vas_requested);
        this.overrideTransactionModeSwitch.setOnCheckedChangeListener(newCheckedVisibilityListener(this.transactionModeSettingsLayout));
        this.useOseSwitch = (SwitchCompat) findViewById(R.id.ose_enabled);
        this.oseSmarttap13Switch = (SwitchCompat) findViewById(R.id.ose_smarttap_v1_3);
        this.oseSmarttap20Switch = (SwitchCompat) findViewById(R.id.ose_smarttap_v2_0);
        this.oseCustomAidSwitch = (SwitchCompat) findViewById(R.id.ose_custom_aid);
        this.supportSmarttap13Switch = (SwitchCompat) findViewById(R.id.support_smarttap_v1_3);
        this.supportSmarttap20Switch = (SwitchCompat) findViewById(R.id.support_smarttap_v2_0);
        this.allowSkippingSmartTap2SelectSwitch = (SwitchCompat) findViewById(R.id.allow_skipping_smart_tap_2_select);
        this.includeMasterNonceSwitch = (SwitchCompat) findViewById(R.id.include_master_nonce);
        this.includeNonceInProprietaryDataSwitch = (SwitchCompat) findViewById(R.id.include_nonce_in_proprietary_data);
        this.capabilitiesSwitch = (SwitchCompat) findViewById(R.id.merchant_capabilities);
        this.getSmartTapDataResponseLayout = (LinearLayout) findViewById(R.id.get_smarttap_data_response_layout);
        this.getDataResponseStatusSpinner = (Spinner) findViewById(R.id.get_smarttap_data_response_status);
        this.getDataResponseStatusSpinner.setAdapter(new StatusWordAdapter(this));
        this.modifyGetSmartTapDataResponseSwitch = (SwitchCompat) findViewById(R.id.modify_get_smarttap_data_response);
        this.modifyGetSmartTapDataResponseSwitch.setOnCheckedChangeListener(newCheckedVisibilityListener(this.getSmartTapDataResponseLayout));
        this.merchantIdSettingsLayout = (LinearLayout) findViewById(R.id.merchant_id_settings_layout);
        this.merchantIdLayout = (TextInputLayout) findViewById(R.id.merchant_id_layout);
        this.requireEncryptionSwitch = (SwitchCompat) findViewById(R.id.require_encryption);
        this.requireLiveAuthenticationSwitch = (SwitchCompat) findViewById(R.id.require_live_authentication);
        this.checkMerchantIdSwitch = (SwitchCompat) findViewById(R.id.check_merchant_id);
        this.checkMerchantIdSwitch.setOnCheckedChangeListener(newCheckedVisibilityListener(this.merchantIdSettingsLayout));
        this.failPaymentsLayout = (LinearLayout) findViewById(R.id.stop_payment_spinner_layout);
        this.stopPaymentAfterApduSpinner = (Spinner) findViewById(R.id.stop_payment_after_command);
        this.stopPaymentAfterApduSpinner.setAdapter(new PaymentTypeAdapter(this));
        this.failPaymentsSwitch = (SwitchCompat) findViewById(R.id.fail_payments);
        this.failPaymentsSwitch.setOnCheckedChangeListener(newCheckedVisibilityListener(this.failPaymentsLayout));
        this.overrideOseStatusWordLayout = (TextInputLayout) findViewById(R.id.override_ose_status_word_layout);
        this.overrideOseStatusWordSwitch = (SwitchCompat) findViewById(R.id.override_ose_status_word_switch);
        this.overrideOseStatusWordSwitch.setOnCheckedChangeListener(newCheckedVisibilityListener(this.overrideOseStatusWordLayout));
        ((Button) findViewById(R.id.add_custom_status_button)).setOnClickListener(newAddCustomStatusListener());
        ((Button) findViewById(R.id.add_ndef_button)).setOnClickListener(newAddNdefListener());
        ((Button) findViewById(R.id.remove_ndef_button)).setOnClickListener(newRemoveNdefListener());
        ((Button) findViewById(R.id.add_loyalty_button)).setOnClickListener(newAddSmartTapDataListener(1));
        ((Button) findViewById(R.id.add_offer_button)).setOnClickListener(newAddSmartTapDataListener(2));
        ((Button) findViewById(R.id.add_gift_card_button)).setOnClickListener(newAddSmartTapDataListener(3));
        ((Button) findViewById(R.id.add_plc_button)).setOnClickListener(newAddSmartTapDataListener(4));
        this.typeLayouts = ImmutableMap.of(Integer.valueOf(1), this.loyaltyCardsLayout, Integer.valueOf(2), this.offersLayout, Integer.valueOf(3), this.giftCardsLayout, Integer.valueOf(4), this.plcsLayout);
        this.saveButton = (Button) findViewById(R.id.save_button);
        this.saveButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (TestCaseActivity.this.validateForm()) {
                    TestCaseHelper testCaseHelper = (TestCaseHelper) TestCaseActivity.this.lazyTestCaseHelper.get();
                    TestCase access$100 = TestCaseActivity.this.getTestCaseFromForm();
                    if (!(TestCaseActivity.this.tapVideoUri == null || TestCaseActivity.this.tapVideoUri.equals(TestCaseActivity.this.pendingTapVideoUri))) {
                        TestCaseActivity.this.getContentResolver().delete(TestCaseActivity.this.tapVideoUri, null, null);
                    }
                    if (TestCaseActivity.this.originalId >= 0) {
                        testCaseHelper.updateTestCase(access$100);
                    } else {
                        testCaseHelper.insertTestCase(access$100);
                    }
                    TestCaseActivity.this.setResult(-1);
                    TestCaseActivity.this.finish();
                }
            }
        });
        ((Button) findViewById(R.id.cancel_button)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!(TestCaseActivity.this.pendingTapVideoUri == null || TestCaseActivity.this.pendingTapVideoUri.equals(TestCaseActivity.this.tapVideoUri))) {
                    TestCaseActivity.this.getContentResolver().delete(TestCaseActivity.this.pendingTapVideoUri, null, null);
                }
                TestCaseActivity.this.setResult(0);
                TestCaseActivity.this.finish();
            }
        });
        this.setValidationFileButton = (Button) findViewById(R.id.validation_file);
        this.setValidationFileButton.setOnClickListener(newValidationFileButtonClickListener());
        this.removeValidationFileButton = (Button) findViewById(R.id.remove_validation_file);
        this.removeValidationFileButton.setOnClickListener(newRemoveValidationFileButtonClickListener());
        setValidationSchema(null);
        this.setTapVideoButton = (Button) findViewById(R.id.view_video);
        this.setTapVideoButton.setOnClickListener(newViewTapVideoButtonClickListener());
        this.removeTapVideoButton = (Button) findViewById(R.id.remove_video);
        this.removeTapVideoButton.setOnClickListener(newRemoveTapVideoButtonClickListener());
        setTapVideoUri(null);
        this.setPublicKeyButton = (Button) findViewById(R.id.view_merchant_public_key);
        this.setPublicKeyButton.setOnClickListener(newPublicKeyButtonClickListener());
        this.removePublicKeyButton = (Button) findViewById(R.id.remove_merchant_public_key);
        this.removePublicKeyButton.setOnClickListener(newRemovePublicKeyButtonClickListener());
        setPublicKey(null);
        if (bundle == null && getIntent().hasExtra("testcase_id")) {
            this.originalId = getIntent().getLongExtra("testcase_id", -1);
            new DbQueryTask(this.originalId).execute(new Void[0]);
        } else if (bundle == null) {
            this.testSuiteId = getIntent().getLongExtra("testsuite_id", -1);
        }
    }

    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.downloadReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.validationReceiver);
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        registerValidationReceiver();
        registerVideoReceiver();
    }

    private OnCheckedChangeListener newCheckedVisibilityListener(final View view) {
        return new OnCheckedChangeListener(this) {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    view.setVisibility(0);
                } else {
                    view.setVisibility(8);
                }
            }
        };
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putLong("testcase_id", this.originalId);
        bundle.putLong("testsuite_id", this.testSuiteId);
        bundle.putParcelable("tap_video_uri", this.tapVideoUri);
        bundle.putParcelable("pending_video_uri", this.pendingTapVideoUri);
        bundle.putParcelable("validation_schema", this.validationSchema);
        bundle.putByteArray("public_key", this.merchantPublicKey);
        bundle.putSerializable("custom_status_views", getViewIds(this.customStatusLayout));
        bundle.putSerializable("add_ndef_views", getViewIds(this.addNdefLayout));
        bundle.putSerializable("remove_ndef_views", getViewIds(this.removeNdefLayout));
        bundle.putSerializable("loyalty_views", getViewIds(this.loyaltyCardsLayout));
        bundle.putSerializable("offer_views", getViewIds(this.offersLayout));
        bundle.putSerializable("gift_card_views", getViewIds(this.giftCardsLayout));
        bundle.putSerializable("plc_views", getViewIds(this.plcsLayout));
    }

    protected void onRestoreInstanceState(Bundle bundle) {
        this.originalId = bundle.getLong("testcase_id");
        this.testSuiteId = bundle.getLong("testsuite_id");
        this.tapVideoUri = (Uri) bundle.getParcelable("tap_video_uri");
        this.pendingTapVideoUri = (Uri) bundle.getParcelable("pending_video_uri");
        this.validationSchema = (Schema) bundle.getParcelable("validation_schema");
        this.merchantPublicKey = bundle.getByteArray("public_key");
        restoreAddedNdefViewIds((List) bundle.getSerializable("add_ndef_views"));
        restoreRemovedNdefViewIds((List) bundle.getSerializable("remove_ndef_views"));
        restoreSmartTapViewIds((List) bundle.getSerializable("loyalty_views"), 1);
        restoreSmartTapViewIds((List) bundle.getSerializable("offer_views"), 2);
        restoreSmartTapViewIds((List) bundle.getSerializable("gift_card_views"), 3);
        restoreSmartTapViewIds((List) bundle.getSerializable("plc_views"), 4);
        super.onRestoreInstanceState(bundle);
    }

    private void setValidationSchema(Schema schema) {
        this.validationSchema = schema;
        if (this.validationSchema != null) {
            this.setValidationFileButton.setText(R.string.change);
            this.removeValidationFileButton.setVisibility(0);
            return;
        }
        this.setValidationFileButton.setText(R.string.none_set);
        this.removeValidationFileButton.setVisibility(8);
    }

    private void setTapVideoUri(Uri uri) {
        if (!(this.pendingTapVideoUri == null || uri.equals(this.pendingTapVideoUri))) {
            getContentResolver().delete(this.pendingTapVideoUri, null, null);
        }
        this.pendingTapVideoUri = uri;
        if (this.pendingTapVideoUri != null) {
            this.setTapVideoButton.setText(R.string.change);
            this.removeTapVideoButton.setVisibility(0);
            return;
        }
        this.setTapVideoButton.setText(R.string.none_set);
        this.removeTapVideoButton.setVisibility(8);
    }

    private void setPublicKeyString(String str) {
        try {
            setPublicKey(Strings.isNullOrEmpty(str) ? null : Hex.decode(str));
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, getString(R.string.invalid_key), 0).show();
        }
    }

    private void setPublicKey(byte[] bArr) {
        this.merchantPublicKey = bArr;
        if (this.merchantPublicKey != null) {
            this.setPublicKeyButton.setText(R.string.change);
            this.removePublicKeyButton.setVisibility(0);
            return;
        }
        this.setPublicKeyButton.setText(R.string.none_set);
        this.removePublicKeyButton.setVisibility(8);
    }

    private OnClickListener newValidationFileButtonClickListener() {
        return new OnClickListener() {
            public void onClick(View view) {
                Uris.showFilePicker(TestCaseActivity.this, "*/*", 0);
            }
        };
    }

    private OnClickListener newRemoveValidationFileButtonClickListener() {
        return new OnClickListener() {
            public void onClick(View view) {
                TestCaseActivity.this.setValidationSchema(null);
            }
        };
    }

    private OnClickListener newViewTapVideoButtonClickListener() {
        return new OnClickListener() {
            public void onClick(View view) {
                ViewGroup viewGroup = (ViewGroup) TestCaseActivity.this.getLayoutInflater().inflate(R.layout.tap_video_picker_view, null);
                TestCaseActivity.this.videoView = (VideoView) viewGroup.findViewById(R.id.tap_video);
                if (TestCaseActivity.this.pendingTapVideoUri != null) {
                    TestCaseActivity.this.videoView.setVideoURI(TestCaseActivity.this.pendingTapVideoUri);
                    TestCaseActivity.this.videoView.start();
                } else {
                    TestCaseActivity.this.videoView.setVisibility(8);
                }
                ((Button) viewGroup.findViewById(R.id.video_file_button)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Uris.showFilePicker(TestCaseActivity.this, "video/mp4", 1);
                    }
                });
                TestCaseActivity.this.showSaveAlertDialog(TestCaseActivity.this.getString(R.string.tap_video), viewGroup, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TestCaseActivity.this.setTapVideoUri((Uri) TestCaseActivity.this.videoView.getTag());
                    }
                }, new OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        Uri uri = (Uri) TestCaseActivity.this.videoView.getTag();
                        if (uri != null && uri != TestCaseActivity.this.pendingTapVideoUri) {
                            TestCaseActivity.this.videoView.stopPlayback();
                            TestCaseActivity.this.videoView.setVideoURI(null);
                            TestCaseActivity.this.getApplicationContext().getContentResolver().delete(uri, null, null);
                        }
                    }
                });
            }
        };
    }

    private OnClickListener newRemoveTapVideoButtonClickListener() {
        return new OnClickListener() {
            public void onClick(View view) {
                TestCaseActivity.this.setTapVideoUri(null);
            }
        };
    }

    private OnClickListener newPublicKeyButtonClickListener() {
        return new OnClickListener() {
            public void onClick(View view) {
                ViewGroup viewGroup = (ViewGroup) TestCaseActivity.this.getLayoutInflater().inflate(R.layout.merchant_public_key_text_view, null);
                final EditText editText = (EditText) viewGroup.findViewById(R.id.encryption_key_text);
                if (TestCaseActivity.this.merchantPublicKey != null) {
                    editText.setText(Hex.encode(TestCaseActivity.this.merchantPublicKey));
                }
                ((Button) viewGroup.findViewById(R.id.encryption_key_file_button)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        TestCaseActivity.this.waitingEditText = editText;
                        Uris.showFilePicker(TestCaseActivity.this, "*/*", 2);
                    }
                });
                TestCaseActivity.this.showSaveAlertDialog(TestCaseActivity.this.getString(R.string.merchant_public_key), viewGroup, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String obj = editText.getText().toString();
                        if (!Strings.isNullOrEmpty(obj)) {
                            TestCaseActivity.this.setPublicKeyString(obj);
                        }
                    }
                }, null);
            }
        };
    }

    private OnClickListener newRemovePublicKeyButtonClickListener() {
        return new OnClickListener() {
            public void onClick(View view) {
                TestCaseActivity.this.setPublicKey(null);
            }
        };
    }

    private void showSaveAlertDialog(String str, View view, DialogInterface.OnClickListener onClickListener, final OnCancelListener onCancelListener) {
        new Builder(this).setTitle((CharSequence) str).setPositiveButton(R.string.save_key, onClickListener).setNegativeButton(R.string.cancel_key, new DialogInterface.OnClickListener(this) {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onCancelListener != null) {
                    onCancelListener.onCancel(dialogInterface);
                }
            }
        }).setOnCancelListener(onCancelListener).setView(view).show();
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
            if (i == 0) {
                this.saveButton.setEnabled(false);
                new DownloadValidationSchemaTask(this, intent.getData()).execute(new Void[0]);
            } else if (i == 1 && i2 == -1) {
                new DownloadVideoTask(this, intent.getData()).execute(new Void[0]);
            } else if (i == 2 && this.waitingEditText != null) {
                readKeyFromFile(this.waitingEditText, intent.getData());
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    private void readKeyFromFile(EditText editText, Uri uri) {
        Throwable e;
        try {
            this.conscryptInstaller.installConscryptIfNeeded();
            SmartTap2ECKeyManager smartTap2ECKeyManager = new SmartTap2ECKeyManager(new CryptoClientWrapper(), new KeyFactoryWrapper());
            editText.setText(smartTap2ECKeyManager.hexRepresentation(SmartTap2CryptoFileUtils.readPublicKey(this, uri, smartTap2ECKeyManager)));
            return;
        } catch (IOException e2) {
            e = e2;
        } catch (ValuablesCryptoException e3) {
            e = e3;
        }
        FormattingLogger formattingLogger = LOG;
        String valueOf = String.valueOf(uri);
        formattingLogger.w(e, new StringBuilder(String.valueOf(valueOf).length() + 26).append("Could not read from file: ").append(valueOf).toString(), new Object[0]);
        Toast.makeText(this, getString(R.string.file_read_error), 0).show();
    }

    private void restoreAddedNdefViewIds(List<Integer> list) {
        for (Integer intValue : list) {
            addAddNdefInputView(intValue.intValue(), -1);
        }
    }

    private void restoreRemovedNdefViewIds(List<Integer> list) {
        for (Integer intValue : list) {
            addRemoveNdefInputView(intValue.intValue(), -1);
        }
    }

    private void restoreSmartTapViewIds(List<Integer> list, int i) {
        for (Integer intValue : list) {
            addSmartTapDataInputView(i, intValue.intValue(), -1);
        }
    }

    private ArrayList<Integer> getViewIds(LinearLayout linearLayout) {
        ArrayList<Integer> arrayList = new ArrayList();
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            arrayList.add(Integer.valueOf(linearLayout.getChildAt(i).getId()));
        }
        return arrayList;
    }

    private OnClickListener newAddCustomStatusListener() {
        return new OnClickListener() {
            public void onClick(View view) {
                TestCaseActivity.this.addCustomStatusInputView(View.generateViewId(), 0).requestTextFocus();
            }
        };
    }

    private CustomStatusInputView addCustomStatusInputView(final int i, int i2) {
        View customStatusInputView = new CustomStatusInputView(this);
        customStatusInputView.setId(i);
        customStatusInputView.setDeleteClickListener(new OnClickListener() {
            public void onClick(View view) {
                TestCaseActivity.this.customStatusLayout.removeView(TestCaseActivity.this.customStatusLayout.findViewById(i));
            }
        });
        if (i2 >= 0) {
            this.customStatusLayout.addView(customStatusInputView, i2);
        } else {
            this.customStatusLayout.addView(customStatusInputView);
        }
        return customStatusInputView;
    }

    private OnClickListener newAddNdefListener() {
        return new OnClickListener() {
            public void onClick(View view) {
                TestCaseActivity.this.addAddNdefInputView(View.generateViewId(), 0).requestTextFocus();
            }
        };
    }

    private AddNdefRecordInputView addAddNdefInputView(final int i, int i2) {
        View addNdefRecordInputView = new AddNdefRecordInputView(this);
        addNdefRecordInputView.setId(i);
        addNdefRecordInputView.setDeleteClickListener(new OnClickListener() {
            public void onClick(View view) {
                TestCaseActivity.this.addNdefLayout.removeView(TestCaseActivity.this.addNdefLayout.findViewById(i));
            }
        });
        if (i2 >= 0) {
            this.addNdefLayout.addView(addNdefRecordInputView, i2);
        } else {
            this.addNdefLayout.addView(addNdefRecordInputView);
        }
        return addNdefRecordInputView;
    }

    private OnClickListener newRemoveNdefListener() {
        return new OnClickListener() {
            public void onClick(View view) {
                TestCaseActivity.this.addRemoveNdefInputView(View.generateViewId(), 0).requestTextFocus();
            }
        };
    }

    private RemoveNdefRecordInputView addRemoveNdefInputView(final int i, int i2) {
        View removeNdefRecordInputView = new RemoveNdefRecordInputView(this);
        removeNdefRecordInputView.setId(i);
        removeNdefRecordInputView.setDeleteClickListener(new OnClickListener() {
            public void onClick(View view) {
                TestCaseActivity.this.removeNdefLayout.removeView(TestCaseActivity.this.removeNdefLayout.findViewById(i));
            }
        });
        if (i2 >= 0) {
            this.removeNdefLayout.addView(removeNdefRecordInputView, i2);
        } else {
            this.removeNdefLayout.addView(removeNdefRecordInputView);
        }
        return removeNdefRecordInputView;
    }

    private OnClickListener newAddSmartTapDataListener(final int i) {
        return new OnClickListener() {
            public void onClick(View view) {
                TestCaseActivity.this.addSmartTapDataInputView(i, View.generateViewId(), 0).requestTextFocus();
            }
        };
    }

    private SmartTapDataInputView addSmartTapDataInputView(final int i, final int i2, int i3) {
        LinearLayout linearLayout = (LinearLayout) this.typeLayouts.get(Integer.valueOf(i));
        View smartTapDataInputView = new SmartTapDataInputView(this, i);
        smartTapDataInputView.setId(i2);
        smartTapDataInputView.setDeleteClickListener(new OnClickListener() {
            public void onClick(View view) {
                TestCaseActivity.this.removeSmartTapDataInputView(i, i2);
            }
        });
        if (i3 >= 0) {
            linearLayout.addView(smartTapDataInputView, i3);
        } else {
            linearLayout.addView(smartTapDataInputView);
        }
        return smartTapDataInputView;
    }

    private void removeSmartTapDataInputView(int i, int i2) {
        LinearLayout linearLayout = (LinearLayout) this.typeLayouts.get(Integer.valueOf(i));
        linearLayout.removeView(linearLayout.findViewById(i2));
    }

    private boolean validateCustomStatusViews() {
        Set hashSet = new HashSet();
        int i = 0;
        boolean z = true;
        while (i < this.customStatusLayout.getChildCount()) {
            boolean z2;
            CustomStatusInputView customStatusInputView = (CustomStatusInputView) this.customStatusLayout.getChildAt(i);
            if (customStatusInputView.validate()) {
                Byte b = (Byte) customStatusInputView.getEntry().getKey();
                if (hashSet.contains(b)) {
                    TextInputLayout commandLayout = customStatusInputView.getCommandLayout();
                    commandLayout.setErrorEnabled(true);
                    commandLayout.setError(getString(R.string.custom_status_for_same_instruction));
                    z2 = false;
                } else {
                    hashSet.add(b);
                    z2 = z;
                }
            } else {
                z2 = false;
            }
            i++;
            z = z2;
        }
        return z;
    }

    private boolean validateNdefViews() {
        int i;
        boolean z = true;
        for (i = 0; i < this.addNdefLayout.getChildCount(); i++) {
            if (!((AddNdefRecordInputView) this.addNdefLayout.getChildAt(i)).validate()) {
                z = false;
            }
        }
        for (i = 0; i < this.removeNdefLayout.getChildCount(); i++) {
            if (!((RemoveNdefRecordInputView) this.removeNdefLayout.getChildAt(i)).validate()) {
                z = false;
            }
        }
        return z;
    }

    private boolean validateSmartTapViews(LinearLayout linearLayout) {
        boolean z = true;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (!((SmartTapDataInputView) linearLayout.getChildAt(i)).validate()) {
                z = false;
            }
        }
        return z;
    }

    private boolean validateForm() {
        return validateTextLayout(this.smarttap13PriorityLayout, Encoding.BCD) && validateTextLayout(this.smarttap20PriorityLayout, Encoding.BCD) && Validator.hex(this, this.customAidValueLayout, 5, 16) && validateTextLayout(this.customAidPriorityLayout, Encoding.BCD) && Validator.hex(this, this.smarttapMinVersionLayout, 2, 2) && Validator.hex(this, this.smarttapMaxVersionLayout, 2, 2) && validateTextLayout(this.consumerIdLayout, Encoding.BCD) && validateTextLayout(this.tapIdLayout, Encoding.BCD) && validateTextLayout(this.deviceIdLayout, Encoding.BCD) && validateCustomStatusViews() && validateNdefViews() && validateSmartTapViews(this.loyaltyCardsLayout) && validateSmartTapViews(this.offersLayout) && validateSmartTapViews(this.giftCardsLayout) && validateSmartTapViews(this.plcsLayout);
    }

    private boolean validateTextLayout(TextInputLayout textInputLayout, Encoding encoding) {
        if (encoding.isValid(textInputLayout.getEditText().getText().toString())) {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError(null);
            return true;
        }
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(getString(R.string.input_error, new Object[]{encoding.toString()}));
        return false;
    }

    private void restoreTestCase(TestCase testCase) {
        this.testSuiteId = testCase.testSuiteId();
        this.testCaseNameEditText.setText(testCase.name());
        this.testCaseExpectedTerminalBehaviorEditText.setText(testCase.expectedTerminalBehavior());
        this.supportGenericKeyAuthSwitch.setChecked(testCase.supportGenericKeyAuth());
        this.supportEciesSwitch.setChecked(testCase.supportEcies());
        this.overrideTransactionModeSwitch.setChecked(testCase.overrideTransactionMode());
        this.paymentEnabledSwitch.setChecked(testCase.paymentEnabled());
        this.paymentRequestedSwitch.setChecked(testCase.paymentRequested());
        this.vasEnabledSwitch.setChecked(testCase.vasEnabled());
        this.vasRequestedSwitch.setChecked(testCase.vasRequested());
        this.useOseSwitch.setChecked(testCase.oseEnabled());
        this.oseSmarttap13Switch.setChecked(testCase.oseSmartTap13());
        this.smarttap13PriorityLayout.getEditText().setText(Integer.toString(testCase.oseSmartTap13Priority()));
        this.oseSmarttap20Switch.setChecked(testCase.oseSmartTap20());
        this.smarttap20PriorityLayout.getEditText().setText(Integer.toString(testCase.oseSmartTap20Priority()));
        this.oseCustomAidSwitch.setChecked(testCase.oseCustom());
        setHex(this.customAidValueLayout, testCase.oseCustomAid().array());
        this.customAidPriorityLayout.getEditText().setText(Integer.toString(testCase.oseCustomPriority()));
        this.supportSmarttap13Switch.setChecked(testCase.supportSmartTap13());
        this.supportSmarttap20Switch.setChecked(testCase.supportSmartTap20());
        this.allowSkippingSmartTap2SelectSwitch.setChecked(testCase.allowSkippingSmartTap2Select());
        this.includeMasterNonceSwitch.setChecked(testCase.includeMasterNonceInOseResponse());
        this.includeNonceInProprietaryDataSwitch.setChecked(testCase.includeNonceInProprietaryData());
        setHex(this.smarttapMinVersionLayout, testCase.smartTapMinVersion());
        setHex(this.smarttapMaxVersionLayout, testCase.smartTapMaxVersion());
        this.consumerIdLayout.getEditText().setText(testCase.consumerId());
        this.tapIdLayout.getEditText().setText(testCase.tapId());
        this.deviceIdLayout.getEditText().setText(testCase.deviceId());
        this.capabilitiesSwitch.setChecked(testCase.useMerchantCapabilities());
        setValidationSchema(testCase.validationSchema());
        String tapVideoUri = testCase.tapVideoUri();
        if (tapVideoUri != null) {
            this.pendingTapVideoUri = Uri.parse(tapVideoUri);
            setTapVideoUri(this.pendingTapVideoUri);
        }
        setPublicKey(testCase.merchantPublicKey());
        GetSmartTapDataResponse dataResponseStatus = testCase.getDataResponseStatus();
        if (dataResponseStatus != null) {
            this.modifyGetSmartTapDataResponseSwitch.setChecked(true);
            this.getSmartTapDataResponseLayout.setVisibility(0);
            setSpinnerSelection(this.getDataResponseStatusSpinner, dataResponseStatus);
        }
        this.merchantIdLayout.getEditText().setText(Long.toString(testCase.merchantId()));
        this.requireEncryptionSwitch.setChecked(testCase.requireEncryption());
        this.requireLiveAuthenticationSwitch.setChecked(testCase.requireLiveAuthentication());
        if (testCase.checkMerchantId()) {
            this.checkMerchantIdSwitch.setChecked(true);
            this.merchantIdSettingsLayout.setVisibility(0);
        } else {
            this.checkMerchantIdSwitch.setChecked(false);
        }
        CommandType stopPaymentsCommand = testCase.stopPaymentsCommand();
        if (stopPaymentsCommand != null) {
            this.failPaymentsSwitch.setChecked(true);
            this.failPaymentsLayout.setVisibility(0);
            setSpinnerSelection(this.stopPaymentAfterApduSpinner, stopPaymentsCommand);
        }
        this.overrideOseStatusWordSwitch.setChecked(testCase.useCustomOseStatusWord());
        setHex(this.overrideOseStatusWordLayout, testCase.customOseStatusWord());
        Map maxCustomStatuses = testCase.maxCustomStatuses();
        for (Entry entry : testCase.customStatuses().entrySet()) {
            CustomStatusInputView addCustomStatusInputView = addCustomStatusInputView(View.generateViewId(), -1);
            addCustomStatusInputView.setStatusData(entry);
            if (maxCustomStatuses != null && maxCustomStatuses.containsKey(entry.getKey())) {
                addCustomStatusInputView.setMaxOverrides(((Integer) maxCustomStatuses.get(entry.getKey())).intValue());
            }
        }
        for (Entry entry2 : testCase.addedNdefs().entries()) {
            addAddNdefInputView(View.generateViewId(), -1).setNdefData(entry2);
        }
        for (ByteArrayWrapper ndefData : testCase.removedNdefs()) {
            addRemoveNdefInputView(View.generateViewId(), -1).setNdefData(ndefData);
        }
        for (SmartTap smartTap : testCase.smartTaps()) {
            addSmartTapDataInputView(smartTap.type, View.generateViewId(), -1).setSmartTapData(smartTap);
        }
    }

    private void setSpinnerSelection(Spinner spinner, Object obj) {
        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(obj)) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    private TestCase getTestCaseFromForm() {
        List arrayList = new ArrayList();
        addSmartTaps(arrayList, this.loyaltyCardsLayout);
        addSmartTaps(arrayList, this.offersLayout);
        addSmartTaps(arrayList, this.giftCardsLayout);
        addSmartTaps(arrayList, this.plcsLayout);
        TestCase.Builder id = TestCase.builder().setName(this.testCaseNameEditText.getText().toString()).setExpectedTerminalBehavior(this.testCaseExpectedTerminalBehaviorEditText.getText().toString()).setUseCustomOseStatusWord(this.overrideOseStatusWordSwitch.isChecked()).setCustomOseStatusWord(getHex(this.overrideOseStatusWordLayout)).setCustomStatuses(getCustomStatuses()).setMaxCustomStatuses(getMaxCustomStatuses()).setAddedNdefs(getAddedNdefs()).setRemovedNdefs(getRemovedNdefs()).setSmartTaps(arrayList).setSmartTaps(arrayList).setSupportGenericKeyAuth(this.supportGenericKeyAuthSwitch.isChecked()).setSupportEcies(this.supportEciesSwitch.isChecked()).setOverrideTransactionMode(this.overrideTransactionModeSwitch.isChecked()).setPaymentEnabled(this.paymentEnabledSwitch.isChecked()).setPaymentRequested(this.paymentRequestedSwitch.isChecked()).setVasEnabled(this.vasEnabledSwitch.isChecked()).setVasRequested(this.vasRequestedSwitch.isChecked()).setOseEnabled(this.useOseSwitch.isChecked()).setOseSmartTap13(this.oseSmarttap13Switch.isChecked()).setOseSmartTap13Priority(getInt(this.smarttap13PriorityLayout)).setOseSmartTap20(this.oseSmarttap20Switch.isChecked()).setOseSmartTap20Priority(getInt(this.smarttap20PriorityLayout)).setOseCustom(this.oseCustomAidSwitch.isChecked()).setOseCustomAid(Aid.valueOf(getHex(this.customAidValueLayout))).setOseCustomPriority(getInt(this.customAidPriorityLayout)).setSupportSmartTap13(this.supportSmarttap13Switch.isChecked()).setSupportSmartTap20(this.supportSmarttap20Switch.isChecked()).setAllowSkippingSmartTap2Select(this.allowSkippingSmartTap2SelectSwitch.isChecked()).setIncludeMasterNonceInOseResponse(this.includeMasterNonceSwitch.isChecked()).setIncludeNonceInProprietaryData(this.includeNonceInProprietaryDataSwitch.isChecked()).setSmartTapMinVersion(getHex(this.smarttapMinVersionLayout)).setSmartTapMaxVersion(getHex(this.smarttapMaxVersionLayout)).setConsumerId(this.consumerIdLayout.getEditText().getText().toString()).setTapId(this.tapIdLayout.getEditText().getText().toString()).setDeviceId(this.deviceIdLayout.getEditText().getText().toString()).setCheckMerchantId(this.checkMerchantIdSwitch.isChecked()).setMerchantId(getLong(this.merchantIdLayout)).setRequireEncryption(this.requireEncryptionSwitch.isChecked()).setRequireLiveAuthentication(this.requireLiveAuthenticationSwitch.isChecked()).setMerchantPublicKey(this.merchantPublicKey).setUseMerchantCapabilities(this.capabilitiesSwitch.isChecked()).setTestSuiteId(this.testSuiteId).setValidationSchema(this.validationSchema).setTapVideoUri(this.pendingTapVideoUri == null ? null : this.pendingTapVideoUri.toString()).setId(this.originalId);
        if (this.modifyGetSmartTapDataResponseSwitch.isChecked()) {
            id.setGetDataResponseStatus((GetSmartTapDataResponse) this.getDataResponseStatusSpinner.getSelectedItem());
        }
        if (this.failPaymentsSwitch.isChecked()) {
            id.setStopPaymentsCommand((CommandType) this.stopPaymentAfterApduSpinner.getSelectedItem());
        }
        return id.build();
    }

    private static long getLong(TextInputLayout textInputLayout) {
        Long tryParse = Longs.tryParse(textInputLayout.getEditText().getText().toString());
        return tryParse != null ? tryParse.longValue() : 0;
    }

    private static int getInt(TextInputLayout textInputLayout) {
        Integer tryParse = Ints.tryParse(textInputLayout.getEditText().getText().toString());
        return tryParse != null ? tryParse.intValue() : 0;
    }

    private static byte[] getHex(TextInputLayout textInputLayout) {
        try {
            return Hex.decode(textInputLayout.getEditText().getText().toString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static void setHex(TextInputLayout textInputLayout, byte[] bArr) {
        textInputLayout.getEditText().setText(bArr == null ? "" : Hex.encodeUpper(bArr));
    }

    private Map<Byte, byte[]> getCustomStatuses() {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (int i = 0; i < this.customStatusLayout.getChildCount(); i++) {
            builder.put(((CustomStatusInputView) this.customStatusLayout.getChildAt(i)).getEntry());
        }
        return builder.build();
    }

    private Map<Byte, Integer> getMaxCustomStatuses() {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (int i = 0; i < this.customStatusLayout.getChildCount(); i++) {
            builder.put(((CustomStatusInputView) this.customStatusLayout.getChildAt(i)).getMaxOverrides());
        }
        return builder.build();
    }

    private Multimap<ByteArrayWrapper, NdefRecord> getAddedNdefs() {
        ImmutableMultimap.Builder builder = ImmutableMultimap.builder();
        for (int i = 0; i < this.addNdefLayout.getChildCount(); i++) {
            builder.put(((AddNdefRecordInputView) this.addNdefLayout.getChildAt(i)).getEntry((short) 1));
        }
        return builder.build();
    }

    private Set<ByteArrayWrapper> getRemovedNdefs() {
        ImmutableSet.Builder builder = ImmutableSet.builder();
        for (int i = 0; i < this.removeNdefLayout.getChildCount(); i++) {
            builder.add(((RemoveNdefRecordInputView) this.removeNdefLayout.getChildAt(i)).getNdefId());
        }
        return builder.build();
    }

    private void addSmartTaps(List<SmartTap> list, LinearLayout linearLayout) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            SmartTap smartTap = ((SmartTapDataInputView) linearLayout.getChildAt(i)).getSmartTap();
            if (smartTap != null) {
                list.add(smartTap);
            }
        }
    }

    public static Intent createAddIntent(Context context, long j) {
        Intent intent = new Intent(context, TestCaseActivity.class);
        intent.putExtra("testsuite_id", j);
        return intent;
    }

    public static Intent createEditIntent(Context context, long j) {
        Intent intent = new Intent(context, TestCaseActivity.class);
        intent.putExtra("testcase_id", j);
        return intent;
    }

    private void registerValidationReceiver() {
        IntentFilter intentFilter = new IntentFilter("com.google.commerce.tapandpay.merchantapp.VALIDATION_SCHEMA_DOWNLOADED");
        this.validationReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                View findViewById = TestCaseActivity.this.findViewById(R.id.test_case_activity_layout);
                if (intent.hasExtra("validation_schema")) {
                    TestCaseActivity.this.setValidationSchema((Schema) intent.getParcelableExtra("validation_schema"));
                    Snackbar.make(findViewById, R.string.schema_loaded_successful, 0).show();
                } else if (intent.hasExtra("validation_schema_exception")) {
                    CharSequence string;
                    Throwable th = (Throwable) intent.getExtras().getSerializable("validation_schema_exception");
                    TestCaseActivity.LOG.d(th, "Exception occured while attempting to retrieve validation schema", new Object[0]);
                    if (th instanceof IOException) {
                        string = context.getString(R.string.cannot_open_schema, new Object[]{th.getMessage()});
                    } else if (th instanceof JsonSyntaxException) {
                        string = context.getString(R.string.schema_not_valid_json, new Object[]{th.getMessage()});
                    } else {
                        string = context.getString(R.string.schema_exception, new Object[]{th.getMessage()});
                    }
                    new Builder(TestCaseActivity.this).setTitle(R.string.retrieve_schema_failure).setMessage(string).show();
                } else {
                    Snackbar.make(findViewById, R.string.retrieve_schema_failure, 0).show();
                }
                TestCaseActivity.this.saveButton.setEnabled(true);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(this.validationReceiver, intentFilter);
    }

    private void registerVideoReceiver() {
        IntentFilter intentFilter = new IntentFilter("com.google.commerce.tapandpay.merchantapp.VIDEO_DOWNLOAD");
        this.downloadReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (TestCaseActivity.this.videoView != null && intent.hasExtra("download_video_uri")) {
                    Uri uri = (Uri) intent.getParcelableExtra("download_video_uri");
                    TestCaseActivity.this.videoView.setVisibility(0);
                    TestCaseActivity.this.videoView.setVideoURI(uri);
                    TestCaseActivity.this.videoView.setTag(uri);
                    TestCaseActivity.this.videoView.start();
                } else if (!intent.hasExtra("download_video_uri")) {
                    Snackbar.make(TestCaseActivity.this.findViewById(R.id.test_case_activity_layout), R.string.download_error, 0).show();
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(this.downloadReceiver, intentFilter);
    }
}
