package com.google.commerce.tapandpay.merchantapp.hce;

import android.annotation.TargetApi;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.AidInfo;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.Ose;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.SmartTap2ProprietaryData;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.TransactionalDetails;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.TransactionalDetails.Builder;
import com.google.android.libraries.commerce.hce.common.SmartTapStatusWord;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.result.Result.Status;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import com.google.common.base.Optional;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;
import java.util.ArrayList;
import java.util.Arrays;
import javax.inject.Inject;
import javax.inject.Provider;

@TargetApi(19)
public class MerchantApduService extends HostApduService {
    private static final ResponseApdu CONDITIONS_NOT_SATISFIED = ResponseApdu.fromStatusWord(Iso7816StatusWord.CONDITIONS_NOT_SATISFIED);
    private static final ResponseApdu FILE_NOT_FOUND = ResponseApdu.fromStatusWord(Iso7816StatusWord.FILE_NOT_FOUND);
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private static final ResponseApdu UNKNOWN_ERROR = ResponseApdu.fromStatusWord(Iso7816StatusWord.UNKNOWN_ERROR);
    private boolean lastSelectedOse = false;
    @Inject
    Provider<PayPassProcessor> payPassProcessorProvider;
    private CommandProcessor selectedCommandProcessor;
    private SmartTapV2Processor smartTap2CommandProcessorForOse;
    @Inject
    Provider<SmartTapV1Processor> smartTapV1ProcessorProvider;
    @Inject
    Provider<SmartTapV2Processor> smartTapV2ProcessorProvider;
    @Inject
    TestCaseHelper testCaseHelper;
    private TestProcessor testProcessor = null;

    public void onCreate() {
        super.onCreate();
        ((InjectedApplication) getApplication()).inject(this);
    }

    public byte[] processCommandApdu(byte[] bArr, Bundle bundle) {
        byte[] toByteArray;
        if (this.testProcessor == null) {
            this.testProcessor = new TestProcessor(this);
        }
        long currentTimeMillis = System.currentTimeMillis();
        LOG.d("Command APDU: %s", Hex.encodeUpper(bArr));
        if (isApduSelect(bArr)) {
            LOG.d("Select command", new Object[0]);
            toByteArray = getSelectResponse(bArr).toByteArray();
            this.testProcessor.finishCurrentCommand(bArr, toByteArray, Optional.absent());
        } else if (this.selectedCommandProcessor != null) {
            LOG.d("Executing test processor", new Object[0]);
            toByteArray = this.testProcessor.finishCurrentCommand(bArr, this.selectedCommandProcessor.processCommand(this.testProcessor, bArr));
        } else {
            toByteArray = this.testProcessor.finishCurrentCommand(bArr, CONDITIONS_NOT_SATISFIED);
        }
        LOG.d("Responded to command in %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        FormattingLogger formattingLogger = LOG;
        String str = "Response APDU: %s";
        Object[] objArr = new Object[1];
        objArr[0] = toByteArray != null ? Hex.encodeUpper(toByteArray) : "null";
        formattingLogger.d(str, objArr);
        return toByteArray;
    }

    private ResponseApdu getSelectResponse(byte[] bArr) {
        byte[] extractAidFromSelectCommand = extractAidFromSelectCommand(bArr);
        LOG.d("Select AID: %s", Hex.encodeUpper(extractAidFromSelectCommand));
        TestCase readFullTestCase = this.testCaseHelper.readFullTestCase(PreferenceManager.getDefaultSharedPreferences(this).getLong("active_testcase", -1));
        if (Arrays.equals(extractAidFromSelectCommand, Aid.OSE.array())) {
            this.smartTap2CommandProcessorForOse = (SmartTapV2Processor) this.smartTapV2ProcessorProvider.get();
            this.lastSelectedOse = true;
            if (readFullTestCase != null && readFullTestCase.allowSkippingSmartTap2Select()) {
                this.selectedCommandProcessor = this.smartTap2CommandProcessorForOse;
            }
            return getOseResponse(readFullTestCase);
        } else if (Arrays.equals(extractAidFromSelectCommand, Aid.PPSE_AID.array())) {
            this.selectedCommandProcessor = null;
            if (readFullTestCase == null || readFullTestCase.stopPaymentsCommand() == CommandType.SELECT_PPSE) {
                return FILE_NOT_FOUND;
            }
            return ResponseApdu.fromResponse(PaymentConstants.SELECT_PPSE_RESPONSE);
        } else {
            if (Arrays.equals(extractAidFromSelectCommand, Aid.SMART_TAP_AID_V1_3.array()) && readFullTestCase != null && readFullTestCase.supportSmartTap13()) {
                this.selectedCommandProcessor = (CommandProcessor) this.smartTapV1ProcessorProvider.get();
            } else if (Arrays.equals(extractAidFromSelectCommand, Aid.SMART_TAP_AID_V2_0.array()) && readFullTestCase != null && readFullTestCase.supportSmartTap20()) {
                if (this.lastSelectedOse) {
                    this.selectedCommandProcessor = this.smartTap2CommandProcessorForOse;
                } else {
                    this.selectedCommandProcessor = (CommandProcessor) this.smartTapV2ProcessorProvider.get();
                }
            } else if (Arrays.equals(extractAidFromSelectCommand, Aid.MASTERCARD_AID_PREFIX_CREDIT_OR_DEBIT.array())) {
                this.selectedCommandProcessor = (CommandProcessor) this.payPassProcessorProvider.get();
            } else {
                this.selectedCommandProcessor = null;
                this.lastSelectedOse = false;
                return FILE_NOT_FOUND;
            }
            this.lastSelectedOse = false;
            return this.selectedCommandProcessor.processCommand(this.testProcessor, bArr);
        }
    }

    public void onDeactivated(int i) {
        Status status = Status.STATUS_ERROR;
        if (i == 1) {
            status = Status.STATUS_DEACTIVATED_DESELECTED;
        } else if (i == 0) {
            status = Status.STATUS_DEACTIVATED_LINK_LOSS;
        }
        if (this.testProcessor != null) {
            this.testProcessor.startResultsActivityAndFinish(status);
            this.testProcessor = null;
        }
    }

    private static boolean isApduSelect(byte[] bArr) {
        return Ints.fromByteArray(bArr) == 10748928;
    }

    private static byte[] extractAidFromSelectCommand(byte[] bArr) {
        return Arrays.copyOfRange(bArr, 5, bArr[4] + 5);
    }

    private ResponseApdu getOseResponse(TestCase testCase) {
        Optional of = Optional.of(new ByteArrayWrapper(this.smartTap2CommandProcessorForOse.getHandsetNonce()));
        if (testCase == null || !testCase.oseEnabled()) {
            return FILE_NOT_FOUND;
        }
        if (testCase.useCustomOseStatusWord()) {
            return ResponseApdu.fromStatusWord(SmartTapStatusWord.fromBytes(testCase.customOseStatusWord()));
        }
        Iterable arrayList = new ArrayList();
        if (testCase.oseSmartTap13()) {
            arrayList.add(AidInfo.create(Aid.SMART_TAP_AID_V1_3, testCase.oseSmartTap13Priority(), Optional.absent()));
        }
        if (testCase.oseSmartTap20()) {
            Optional optional;
            Optional of2 = Optional.of(Short.valueOf(Shorts.fromByteArray(testCase.smartTapMinVersion())));
            Optional of3 = Optional.of(Short.valueOf(Shorts.fromByteArray(testCase.smartTapMaxVersion())));
            if (testCase.includeNonceInProprietaryData()) {
                optional = of;
            } else {
                optional = Optional.absent();
            }
            arrayList.add(AidInfo.create(Aid.SMART_TAP_AID_V2_0, testCase.oseSmartTap20Priority(), Optional.of(SmartTap2ProprietaryData.create(of2, of3, optional, testCase.allowSkippingSmartTap2Select()))));
        }
        if (testCase.oseCustom()) {
            arrayList.add(AidInfo.create(testCase.oseCustomAid(), testCase.oseCustomPriority(), Optional.absent()));
        }
        try {
            Builder ecies = TransactionalDetails.builder().setGenericKeyAuth(testCase.supportGenericKeyAuth()).setEcies(testCase.supportEcies());
            if (testCase.overrideTransactionMode()) {
                ecies.setPaymentEnabled(testCase.paymentEnabled()).setPaymentRequested(testCase.paymentRequested()).setVasEnabled(testCase.vasEnabled()).setVasRequested(testCase.vasRequested());
            }
            if (!testCase.includeMasterNonceInOseResponse()) {
                of = Optional.absent();
            }
            return ResponseApdu.fromResponse(Ose.compose(ecies.build(), of, arrayList));
        } catch (Throwable e) {
            LOG.e(e, "Exception caught while composing OSE response.", new Object[0]);
            return UNKNOWN_ERROR;
        }
    }
}
