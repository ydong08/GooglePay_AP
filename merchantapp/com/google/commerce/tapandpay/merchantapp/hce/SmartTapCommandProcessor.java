package com.google.commerce.tapandpay.merchantapp.hce;

import com.google.android.libraries.commerce.hce.applet.HceApplet;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapApplet;
import com.google.android.libraries.commerce.hce.common.ResponseApdus;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase.GetSmartTapDataResponse;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;

abstract class SmartTapCommandProcessor implements CommandProcessor {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private static final ResponseApdu UNKNOWN_ERROR = ResponseApdu.fromStatusWord(Iso7816StatusWord.UNKNOWN_ERROR);
    private final HceApplet applet;

    SmartTapCommandProcessor(HceApplet hceApplet) {
        this.applet = hceApplet;
    }

    public ResponseApdu processCommand(TestProcessor testProcessor, byte[] bArr) {
        Optional testCaseDataResponse = testProcessor.getTestCaseDataResponse(bArr);
        if (testCaseDataResponse.isPresent()) {
            return ((GetSmartTapDataResponse) testCaseDataResponse.get()).getResponseApdu();
        }
        testCaseDataResponse = testProcessor.getStatusWordOptional(bArr);
        try {
            ResponseApdu responseApdu = this.applet.processCommand(bArr, true).getResponseApdu();
            if (testCaseDataResponse.isPresent()) {
                return ResponseApdu.fromResponse(responseApdu.getResponseData(), (StatusWord) testCaseDataResponse.get(), responseApdu.getCommandTimeMillis());
            }
            return responseApdu;
        } catch (Throwable e) {
            LOG.e(Throwables.getStackTraceAsString(e), new Object[0]);
            return ResponseApdus.get(e.getResponseCode(), ((Short) ((SmartTapApplet) this.applet).getVersion().or(Short.valueOf((short) 0))).shortValue());
        } catch (Throwable e2) {
            LOG.e(Throwables.getStackTraceAsString(e2), new Object[0]);
            return UNKNOWN_ERROR;
        }
    }
}
