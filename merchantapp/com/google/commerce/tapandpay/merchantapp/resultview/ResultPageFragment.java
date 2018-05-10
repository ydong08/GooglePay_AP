package com.google.commerce.tapandpay.merchantapp.resultview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.AidInfo;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.Ose;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.Ose.OseResponse;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.SmartTap2ProprietaryData;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.TransactionalDetails;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvException;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.merchantapp.common.ByteHelper;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.result.Result;
import com.google.commerce.tapandpay.merchantapp.result.Result.CommandAndResponse;
import com.google.commerce.tapandpay.merchantapp.result.Result.InstructionDuration;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Status;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.Iterator;
import java.util.List;

public class ResultPageFragment extends Fragment {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private LinearLayout durationsLayout;
    private Result expectedResult;
    private BroadcastReceiver expectedResultReceiver;
    private TextView expectedResultText;
    private TextView paymentStatus;
    private Result result;
    private LinearLayout resultLayout;
    private TextView smarttapStatus;
    private TextView timestamp;
    private TextView validationResults;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.result_view, viewGroup, false);
        this.resultLayout = (LinearLayout) inflate.findViewById(R.id.result_layout);
        this.smarttapStatus = (TextView) inflate.findViewById(R.id.smarttap_status);
        this.paymentStatus = (TextView) inflate.findViewById(R.id.payment_status);
        this.validationResults = (TextView) inflate.findViewById(R.id.validation_results);
        this.timestamp = (TextView) inflate.findViewById(R.id.timestamp);
        this.durationsLayout = (LinearLayout) inflate.findViewById(R.id.durations_list);
        this.expectedResultText = (TextView) inflate.findViewById(R.id.expected_result);
        Bundle bundle2 = (Bundle) Preconditions.checkNotNull(getArguments());
        this.result = (Result) Preconditions.checkNotNull((Result) bundle2.getSerializable("result"));
        this.expectedResult = (Result) bundle2.getSerializable("expected_result");
        setUpResult();
        if (this.expectedResult != null) {
            showExpectedResultStatus();
        }
        if (this.expectedResultReceiver == null) {
            IntentFilter intentFilter = new IntentFilter("com.google.commerce.tapandpay.merchantapp.RECEIVE_EXPECTED_RESULT");
            this.expectedResultReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    Preconditions.checkNotNull(intent.getSerializableExtra("expected_result"));
                    ResultPageFragment.this.expectedResult = (Result) intent.getSerializableExtra("expected_result");
                    ResultPageFragment.this.showExpectedResultStatus();
                    ResultPageFragment.this.getArguments().putSerializable("expected_result", ResultPageFragment.this.expectedResult);
                }
            };
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.expectedResultReceiver, intentFilter);
        }
        return inflate;
    }

    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.expectedResultReceiver);
        this.expectedResultReceiver = null;
    }

    private void showExpectedResultStatus() {
        this.expectedResultText.setVisibility(0);
        if (this.result.commandsEquals(this.expectedResult)) {
            this.expectedResultText.setText(getString(R.string.passing));
            this.expectedResultText.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen200));
            return;
        }
        this.expectedResultText.setText(getString(R.string.failing));
        this.expectedResultText.setBackgroundColor(getResources().getColor(R.color.quantum_googred200));
    }

    private void setUpResult() {
        this.timestamp.setText(this.result.timestamp());
        addInstructionDurations(this.result.instructionDurations());
        this.smarttapStatus.setText(this.result.smarttapStatus().stringResId());
        this.paymentStatus.setText(this.result.paymentStatus().stringResId());
        this.validationResults.setText(this.result.validationResults().status().stringResId());
        if (this.result.validationResults().status() != Status.NO_SCHEMA) {
            addValidationResultsView(this.result.validationResults());
        }
        SmartTapViewInflater smartTapViewInflater = null;
        Iterator it = this.result.commandAndResponses().iterator();
        while (it.hasNext()) {
            CommandAndResponse commandAndResponse = (CommandAndResponse) it.next();
            String commandTitle = commandAndResponse.type().commandTitle(getActivity());
            String responseTitle = commandAndResponse.type().responseTitle(getActivity(), commandAndResponse.isResponseError());
            if (commandAndResponse.type() == CommandType.SELECT_SMARTTAP_V1_3) {
                smartTapViewInflater = new SmartTapV1ViewInflater(getActivity(), this.result);
            } else if (commandAndResponse.type() == CommandType.SELECT_SMARTTAP_V2_0) {
                smartTapViewInflater = new SmartTapV2ViewInflater(getActivity(), this.result);
                smartTapViewInflater.renderCommandAndResponse(commandAndResponse, this.resultLayout);
            } else if (commandAndResponse.type() == CommandType.SELECT_OSE) {
                addOseResponseInfo(commandAndResponse.response());
                smartTapViewInflater = new SmartTapV2ViewInflater(getActivity(), this.result);
            } else if (commandAndResponse.type().isSmartTap() && smartTapViewInflater != null) {
                smartTapViewInflater.renderCommandAndResponse(commandAndResponse, this.resultLayout);
            }
            addHexView(commandTitle, commandAndResponse.command());
            addHexView(responseTitle, commandAndResponse.response());
        }
        this.resultLayout.setVisibility(0);
    }

    private void addValidationResultsView(ValidationResults validationResults) {
        View inflate = getLayoutInflater(null).inflate(R.layout.validation_view, this.resultLayout, false);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.validation_results_list);
        addValidationResults(0, validationResults, linearLayout);
        if (linearLayout.getChildCount() > 0) {
            this.resultLayout.addView(inflate);
        }
    }

    private void addValidationResults(int i, ValidationResults validationResults, LinearLayout linearLayout) {
        int i2 = 0;
        LOG.d("Level: %s, Results: %s", Integer.valueOf(i), validationResults);
        if (validationResults.status() != Status.SUCCESS) {
            StringBuilder stringBuilder = new StringBuilder();
            while (i2 < i) {
                stringBuilder.append("> ");
                i2++;
            }
            if (Strings.isNullOrEmpty(validationResults.message())) {
                stringBuilder.append(validationResults.status());
            } else {
                stringBuilder.append(validationResults.message());
            }
            View textView = new TextView(getContext());
            textView.setText(stringBuilder.toString());
            linearLayout.addView(textView);
        }
        Iterator it = validationResults.nestedResults().iterator();
        while (it.hasNext()) {
            addValidationResults(i + 1, (ValidationResults) it.next(), linearLayout);
        }
    }

    private void addHexView(String str, byte[] bArr) {
        View inflate = getLayoutInflater(null).inflate(R.layout.titled_text_view, this.resultLayout, false);
        ((TextView) inflate.findViewById(R.id.title)).setText(str);
        ((TextView) inflate.findViewById(R.id.text)).setText(ByteHelper.getHexString(getActivity(), bArr));
        ((TextView) inflate.findViewById(R.id.size)).setText(getString(R.string.size, Integer.valueOf(bArr.length)));
        this.resultLayout.addView(inflate);
    }

    private void addInstructionDurations(List<InstructionDuration> list) {
        for (InstructionDuration instructionDuration : list) {
            View textView = new TextView(getActivity());
            textView.setText(getString(R.string.instruction_duration, getString(instructionDuration.type().titleResId()), Long.valueOf(instructionDuration.duration())));
            this.durationsLayout.addView(textView);
        }
    }

    private void addOseResponseInfo(byte[] bArr) {
        View inflate = getLayoutInflater(null).inflate(R.layout.result_ose_response, this.resultLayout, false);
        ViewGroup viewGroup = (ViewGroup) inflate.findViewById(R.id.ose_aid_list);
        try {
            OseResponse parse = Ose.parse(bArr);
            Optional mobileDeviceNonce = parse.mobileDeviceNonce();
            String string = inflate.getResources().getString(R.string.not_present);
            int i = R.string.master_nonce;
            Object[] objArr = new Object[1];
            objArr[0] = mobileDeviceNonce.isPresent() ? Hex.encode(((ByteArrayWrapper) mobileDeviceNonce.get()).array()) : string;
            viewGroup.addView(createSimpleTextView(getString(i, objArr)));
            mobileDeviceNonce = parse.transactionalDetails();
            if (mobileDeviceNonce.isPresent()) {
                Object obj;
                String str;
                Object obj2;
                TransactionalDetails transactionalDetails = (TransactionalDetails) mobileDeviceNonce.get();
                String string2 = inflate.getResources().getString(R.string.yes);
                String string3 = inflate.getResources().getString(R.string.no);
                int i2 = R.string.generic_key_auth;
                Object[] objArr2 = new Object[1];
                if (transactionalDetails.genericKeyAuth()) {
                    obj = string2;
                } else {
                    str = string3;
                }
                objArr2[0] = obj;
                viewGroup.addView(createSimpleTextView(getString(i2, objArr2)));
                i2 = R.string.key_generation;
                objArr2 = new Object[1];
                if (transactionalDetails.ecies()) {
                    obj = string2;
                } else {
                    str = string3;
                }
                objArr2[0] = obj;
                viewGroup.addView(createSimpleTextView(getString(i2, objArr2)));
                i2 = R.string.payment_enabled;
                objArr2 = new Object[1];
                if (transactionalDetails.paymentEnabled()) {
                    obj = string2;
                } else {
                    str = string3;
                }
                objArr2[0] = obj;
                viewGroup.addView(createSimpleTextView(getString(i2, objArr2)));
                i2 = R.string.payment_requested;
                objArr2 = new Object[1];
                if (transactionalDetails.paymentRequested()) {
                    obj = string2;
                } else {
                    str = string3;
                }
                objArr2[0] = obj;
                viewGroup.addView(createSimpleTextView(getString(i2, objArr2)));
                i2 = R.string.vas_enabled;
                objArr2 = new Object[1];
                if (transactionalDetails.vasEnabled()) {
                    obj = string2;
                } else {
                    str = string3;
                }
                objArr2[0] = obj;
                viewGroup.addView(createSimpleTextView(getString(i2, objArr2)));
                int i3 = R.string.vas_requested;
                Object[] objArr3 = new Object[1];
                if (transactionalDetails.vasRequested()) {
                    obj2 = string2;
                } else {
                    String str2 = string3;
                }
                objArr3[0] = obj2;
                viewGroup.addView(createSimpleTextView(getString(i3, objArr3)));
            }
            for (AidInfo priority : parse.aidInfos()) {
                viewGroup.addView(createSimpleTextView(getString(R.string.priority_aid, Integer.valueOf(priority.priority()), ((AidInfo) r5.next()).aid().toString())));
                mobileDeviceNonce = priority.proprietaryDataOptional();
                if (mobileDeviceNonce.isPresent()) {
                    CharSequence encode;
                    Object obj3;
                    View inflate2 = getLayoutInflater(null).inflate(R.layout.result_ose_proprietary_data, viewGroup, false);
                    SmartTap2ProprietaryData smartTap2ProprietaryData = (SmartTap2ProprietaryData) mobileDeviceNonce.get();
                    if (smartTap2ProprietaryData.mobileDeviceNonce().isPresent()) {
                        encode = Hex.encode(((ByteArrayWrapper) smartTap2ProprietaryData.mobileDeviceNonce().get()).array());
                    } else {
                        obj3 = string;
                    }
                    ((TextView) inflate2.findViewById(R.id.handset_nonce)).setText(encode);
                    if (smartTap2ProprietaryData.minVersion().isPresent()) {
                        encode = Short.toString(((Short) smartTap2ProprietaryData.minVersion().get()).shortValue());
                    } else {
                        obj3 = string;
                    }
                    ((TextView) inflate2.findViewById(R.id.min_version)).setText(encode);
                    if (smartTap2ProprietaryData.maxVersion().isPresent()) {
                        encode = Short.toString(((Short) smartTap2ProprietaryData.maxVersion().get()).shortValue());
                    } else {
                        obj3 = string;
                    }
                    ((TextView) inflate2.findViewById(R.id.max_version)).setText(encode);
                    ((TextView) inflate2.findViewById(R.id.supports_skipping_select)).setText(inflate.getResources().getString(smartTap2ProprietaryData.supportsSkippingSelect() ? R.string.yes : R.string.no));
                    viewGroup.addView(inflate2);
                }
            }
        } catch (BasicTlvException e) {
            viewGroup.addView(createSimpleTextView(getString(R.string.tlv_error)));
        }
        this.resultLayout.addView(inflate);
    }

    private TextView createSimpleTextView(String str) {
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new LayoutParams(-2, -2));
        textView.setText(str);
        return textView;
    }

    public static Intent createExpectedResultBroadcast(Result result) {
        Intent intent = new Intent("com.google.commerce.tapandpay.merchantapp.RECEIVE_EXPECTED_RESULT");
        intent.putExtra("expected_result", result);
        return intent;
    }

    public static Bundle createArgs(Result result, Result result2) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("result", result);
        bundle.putSerializable("expected_result", result2);
        return bundle;
    }
}
