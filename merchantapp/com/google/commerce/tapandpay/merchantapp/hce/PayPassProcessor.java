package com.google.commerce.tapandpay.merchantapp.hce;

import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.paypass.PayPassMagStripe;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase;
import javax.inject.Inject;

public class PayPassProcessor implements CommandProcessor {
    private final PayPassMagStripe payPassMagStripe;

    @Inject
    public PayPassProcessor(PayPassMagStripe payPassMagStripe) {
        this.payPassMagStripe = payPassMagStripe;
    }

    public ResponseApdu processCommand(TestProcessor testProcessor, byte[] bArr) {
        this.payPassMagStripe.initialize(PaymentConstants.getPayPassCredentials());
        CommandType commandToType = CommandType.commandToType(bArr);
        TestCase testCase = testProcessor.getTestCase();
        if (testCase == null || commandToType == testCase.stopPaymentsCommand()) {
            return ResponseApdu.fromStatusWord(Iso7816StatusWord.UNKNOWN_ERROR);
        }
        if (commandToType.isPayment()) {
            return this.payPassMagStripe.processCommand(bArr);
        }
        return null;
    }
}
