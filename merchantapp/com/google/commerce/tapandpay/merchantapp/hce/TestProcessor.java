package com.google.commerce.tapandpay.merchantapp.hce;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.android.libraries.commerce.hce.common.StatusWords;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.commerce.tapandpay.merchantapp.application.QualifierAnnotations.CurrentTimeMillis;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.result.Result;
import com.google.commerce.tapandpay.merchantapp.result.Result.Builder;
import com.google.commerce.tapandpay.merchantapp.result.Result.CommandAndResponse;
import com.google.commerce.tapandpay.merchantapp.result.Result.InstructionDuration;
import com.google.commerce.tapandpay.merchantapp.result.Result.Status;
import com.google.commerce.tapandpay.merchantapp.resultview.UserResultActivity;
import com.google.commerce.tapandpay.merchantapp.settings.Settings;
import com.google.commerce.tapandpay.merchantapp.smarttap.v2.MerchantValuable;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase.GetSmartTapDataResponse;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults;
import com.google.common.base.Optional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Provider;

public class TestProcessor {
    private final List<CommandAndResponse> commandAndResponses = new ArrayList();
    private final Context context;
    private final Set<MerchantValuable> encryptedValuables = new HashSet();
    private BroadcastReceiver encryptedValuablesReceiver;
    private final List<InstructionDuration> instructionDurations = new ArrayList();
    private Status paymentStatus = null;
    private final Map<Byte, Integer> returnedCustomStatusesForInstruction = new HashMap();
    @Inject
    Settings settings;
    private Status smartTapStatus = null;
    private long startTimeMillis;
    private TestCase testCase;
    @Inject
    TestCaseHelper testCaseHelper;
    private long testCaseId;
    @Inject
    @CurrentTimeMillis
    Provider<Long> timeProviderMillis;

    public TestProcessor(Service service) {
        this.context = service;
        ((InjectedApplication) service.getApplication()).inject(this);
        this.testCaseId = PreferenceManager.getDefaultSharedPreferences(this.context).getLong("active_testcase", -1);
        if (this.testCaseId == -1) {
            Toast.makeText(this.context, R.string.no_active_test_case, 1).show();
        }
        this.testCase = this.testCaseHelper.readFullTestCase(this.testCaseId);
        this.startTimeMillis = ((Long) this.timeProviderMillis.get()).longValue();
        setUpBroadcastReceiver(service);
    }

    private void setUpBroadcastReceiver(Service service) {
        this.encryptedValuablesReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra("merchant_valuables")) {
                    TestProcessor.this.encryptedValuables.addAll((Set) intent.getSerializableExtra("merchant_valuables"));
                }
            }
        };
        LocalBroadcastManager.getInstance(service).registerReceiver(this.encryptedValuablesReceiver, new IntentFilter("add_valuables_action"));
    }

    public TestCase getTestCase() {
        return this.testCase;
    }

    public Optional<GetSmartTapDataResponse> getTestCaseDataResponse(byte[] bArr) {
        if (this.testCase == null || CommandType.commandToType(bArr) != CommandType.GET_SMARTTAP_DATA) {
            return Optional.absent();
        }
        return Optional.fromNullable(this.testCase.getDataResponseStatus());
    }

    public Optional<StatusWord> getStatusWordOptional(byte[] bArr) {
        if (this.testCase == null) {
            return Optional.absent();
        }
        byte b = bArr[1];
        Map customStatuses = this.testCase.customStatuses();
        if (customStatuses == null || !customStatuses.containsKey(Byte.valueOf(b))) {
            return Optional.absent();
        }
        Map maxCustomStatuses = this.testCase.maxCustomStatuses();
        if (maxCustomStatuses != null && maxCustomStatuses.containsKey(Byte.valueOf(b))) {
            int intValue = ((Integer) maxCustomStatuses.get(Byte.valueOf(b))).intValue();
            if (intValue <= 0) {
                return Optional.absent();
            }
            if (this.returnedCustomStatusesForInstruction.containsKey(Byte.valueOf(b))) {
                int intValue2 = ((Integer) this.returnedCustomStatusesForInstruction.get(Byte.valueOf(b))).intValue();
                if (intValue2 >= intValue) {
                    return Optional.absent();
                }
                this.returnedCustomStatusesForInstruction.put(Byte.valueOf(b), Integer.valueOf(intValue2 + 1));
            } else {
                this.returnedCustomStatusesForInstruction.put(Byte.valueOf(b), Integer.valueOf(1));
            }
        }
        return Optional.of(StatusWords.get((byte[]) customStatuses.get(Byte.valueOf(b)), (short) 1));
    }

    public byte[] finishCurrentCommand(byte[] bArr, ResponseApdu responseApdu) {
        if (responseApdu == null) {
            finishCurrentCommand(bArr, new byte[0], Optional.of(Status.STATUS_ERROR));
            return null;
        }
        byte[] toByteArray = responseApdu.toByteArray();
        if (Code.SUCCESS_MORE_PAYLOAD.equals(responseApdu.getStatusWord().getCode())) {
            finishCurrentCommand(bArr, toByteArray, Optional.absent());
            return toByteArray;
        } else if (Code.SUCCESS.equals(responseApdu.getStatusWord().getCode())) {
            finishCurrentCommand(bArr, toByteArray, Optional.of(Status.STATUS_SUCCESS));
            return toByteArray;
        } else {
            finishCurrentCommand(bArr, toByteArray, Optional.of(Status.STATUS_ERROR));
            return toByteArray;
        }
    }

    public void finishCurrentCommand(byte[] bArr, byte[] bArr2, Optional<Status> optional) {
        CommandType commandToType = CommandType.commandToType(bArr);
        boolean z = optional.orNull() == Status.STATUS_ERROR;
        if (z) {
            if (commandToType.isPayment()) {
                this.paymentStatus = (Status) optional.orNull();
            } else if (commandToType.isSmartTap()) {
                this.smartTapStatus = (Status) optional.orNull();
            }
        } else if (commandToType.isPayment() && this.paymentStatus != Status.STATUS_ERROR && commandToType == CommandType.COMPUTE_CRYPTOGRAPHIC_CHECKSUM) {
            this.paymentStatus = (Status) optional.orNull();
        } else if (commandToType.isSmartTap() && this.smartTapStatus != Status.STATUS_ERROR) {
            this.smartTapStatus = (Status) optional.orNull();
        }
        this.commandAndResponses.add(CommandAndResponse.create(bArr, bArr2, commandToType, z));
        this.instructionDurations.add(InstructionDuration.create(commandToType, ((Long) this.timeProviderMillis.get()).longValue() - this.startTimeMillis));
    }

    public void startResultsActivityAndFinish(Status status) {
        ValidationResults create;
        this.smartTapStatus = this.smartTapStatus != null ? this.smartTapStatus : status;
        if (this.paymentStatus != null) {
            status = this.paymentStatus;
        }
        this.paymentStatus = status;
        if (this.testCase == null || this.testCase.validationSchema() == null) {
            create = ValidationResults.create(ValidationResults.Status.NO_SCHEMA);
        } else {
            create = this.testCase.validationSchema().validate(getAllApdus(this.commandAndResponses));
        }
        Builder testCaseId = Result.builder().setSmarttapStatus(this.smartTapStatus).setPaymentStatus(this.paymentStatus).setValidationResults(create).setTimestamp(new Timestamp(((Long) this.timeProviderMillis.get()).longValue()).toString()).setCommandAndResponses(this.commandAndResponses).setInstructionDurations(this.instructionDurations).setTestCaseId(this.testCaseId);
        if (!this.encryptedValuables.isEmpty()) {
            testCaseId.setEncryptedValuables(this.encryptedValuables);
        }
        Intent createResultIntent = UserResultActivity.createResultIntent(this.context, this.testCase != null ? this.testCase.tapVideoUri() : null, testCaseId.build());
        createResultIntent.setFlags(805306368);
        this.context.startActivity(createResultIntent);
        this.smartTapStatus = null;
        this.paymentStatus = null;
        if (this.settings.getAutoAdvance()) {
            autoAdvanceTestCase();
        }
        LocalBroadcastManager.getInstance(this.context).unregisterReceiver(this.encryptedValuablesReceiver);
    }

    private static List<byte[]> getAllApdus(List<CommandAndResponse> list) {
        List arrayList = new ArrayList(list.size() * 2);
        for (CommandAndResponse commandAndResponse : list) {
            arrayList.add(commandAndResponse.command());
            arrayList.add(commandAndResponse.response());
        }
        return arrayList;
    }

    private void autoAdvanceTestCase() {
        Optional nextValidId = this.testCaseHelper.getNextValidId(this.testCaseId);
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this.context).edit();
        if (nextValidId.isPresent()) {
            edit.putLong("active_testcase", ((Long) nextValidId.get()).longValue()).commit();
        }
    }
}
