package com.google.android.libraries.commerce.hce.terminal.payment;

import android.content.Context;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvException;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.NfcMessageStatusException;
import com.google.android.libraries.commerce.hce.terminal.nfc.TlvParser;
import com.google.android.libraries.commerce.hce.terminal.nfc.Transceiver;
import com.google.android.libraries.commerce.hce.terminal.settings.Preferences;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.io.IOException;

public class PaymentProcessor {
    private static final byte[] EXPRESS_PAY_AID = Aid.AMERICAN_EXPRESS_AID_PREFIX_CREDIT_OR_DEBIT.array();
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private static final byte[] PAY_PASS_AID = Aid.MASTERCARD_AID_PREFIX_CREDIT_OR_DEBIT.array();
    private static final byte[] PAY_WAVE_CREDIT_AID = Aid.VISA_AID_PREFIX_CREDIT.array();
    private static final byte[] PAY_WAVE_DEBIT_AID = Aid.VISA_AID_PREFIX_DEBIT.array();
    private static final byte[] SELECT_PPSE = new byte[]{(byte) 0, (byte) -92, (byte) 4, (byte) 0, (byte) 14, (byte) 50, (byte) 80, (byte) 65, (byte) 89, (byte) 46, (byte) 83, (byte) 89, (byte) 83, (byte) 46, (byte) 68, (byte) 68, (byte) 70, (byte) 48, (byte) 49, (byte) 0};
    private static final byte[] ZIP_AID = Aid.DISCOVER_AID_PREFIX.array();
    private Context context;
    private Preferences preferences;
    private final TlvParser tlvParser;
    private Transceiver transceiver;

    public PaymentProcessor(Context context, Transceiver transceiver, Preferences preferences) {
        this.context = context;
        this.transceiver = transceiver;
        this.preferences = preferences;
        this.tlvParser = new TlvParser(context);
    }

    public void handlePayment() throws BasicTlvException, NfcMessageStatusException, IOException {
        if (this.preferences.getRequestPayment()) {
            byte[][] paymentAids = getPaymentAids(this.transceiver.transceiveSelect(getSelectPpseCommand(), this.context.getString(R.string.select_ppse), this.tlvParser));
            int length = paymentAids.length;
            int i = 0;
            while (i < length) {
                byte[] bArr = paymentAids[i];
                if (startsWith(bArr, EXPRESS_PAY_AID)) {
                    new ExpressPayProcessor(this.context, this.transceiver).handleExpressPayPayment();
                    return;
                } else if (startsWith(bArr, PAY_PASS_AID)) {
                    new PayPassProcessor(this.context, this.transceiver).handlePayPassPayment();
                    return;
                } else if (startsWith(bArr, PAY_WAVE_CREDIT_AID) || startsWith(bArr, PAY_WAVE_DEBIT_AID)) {
                    new PayWaveProcessor(this.context, this.transceiver).handlePayWavePayment();
                    return;
                } else if (startsWith(bArr, ZIP_AID)) {
                    new ZipProcessor(this.context, this.transceiver).handleZipPayment();
                    return;
                } else {
                    LOG.w("Skipping unknown PPSE AID: %s", bArr);
                    i++;
                }
            }
            LOG.w("Did not find recognized PPSE AID. Abandoning payment.", new Object[0]);
            return;
        }
        LOG.i("Requesting payment disabled.", new Object[0]);
    }

    private static byte[] getSelectPpseCommand() {
        return SELECT_PPSE;
    }

    private static byte[][] getPaymentAids(NfcMessage nfcMessage) {
        return new byte[][]{PAY_PASS_AID};
    }

    private static boolean startsWith(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null || bArr.length < bArr2.length) {
            return false;
        }
        for (int i = 0; i < bArr2.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }
}
