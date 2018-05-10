package com.google.commerce.tapandpay.terminalapp.main;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ConscryptInstaller;
import com.google.android.libraries.commerce.hce.terminal.nfc.Transceiver;
import com.google.android.libraries.commerce.hce.terminal.payment.PaymentProcessor;
import com.google.android.libraries.commerce.hce.terminal.settings.Preferences;
import com.google.android.libraries.commerce.hce.terminal.smarttap.SmartTapProcessor;

public class IsoDepTransceiver extends AsyncTask<Void, Void, Exception> {
    SmartTap2ConscryptInstaller conscryptInstaller;
    private final OnError onError;
    PaymentProcessor paymentProcessor;
    private final Preferences preferences;
    SmartTapProcessor smartTapProcessor;
    private final Transceiver transceiver;

    public interface OnError {
        void onError(Exception exception);
    }

    public IsoDepTransceiver(Context context, LocalBroadcastManager localBroadcastManager, Transceiver transceiver, OnError onError, Preferences preferences) {
        this.onError = onError;
        this.preferences = preferences;
        this.transceiver = transceiver;
        this.conscryptInstaller = new SmartTap2ConscryptInstaller(context);
        this.smartTapProcessor = new SmartTapProcessor(context, localBroadcastManager, transceiver, preferences);
        this.paymentProcessor = new PaymentProcessor(context, transceiver, preferences);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected java.lang.Exception doInBackground(java.lang.Void... r7) {
        /*
        r6 = this;
        r3 = 0;
        r0 = 0;
        r1 = r6.transceiver;	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r1.connect();	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r4 = 1;
        r1 = r6.preferences;	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r1 = r1.getCapabilityPaymentOnly();	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        if (r1 != 0) goto L_0x0073;
    L_0x0010:
        r1 = r6.conscryptInstaller;	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r1.installConscryptIfNeeded();	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r1 = r6.smartTapProcessor;	 Catch:{ NfcMessageStatusException -> 0x0045, BasicTlvException -> 0x0057, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r1.handleSmartTap();	 Catch:{ NfcMessageStatusException -> 0x0045, BasicTlvException -> 0x0057, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r1 = r6.smartTapProcessor;	 Catch:{ NfcMessageStatusException -> 0x0045, BasicTlvException -> 0x0057, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r2 = r1.transmittedValuables();	 Catch:{ NfcMessageStatusException -> 0x0045, BasicTlvException -> 0x0057, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r1 = r6.smartTapProcessor;	 Catch:{ NfcMessageStatusException -> 0x0071, BasicTlvException -> 0x0057, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r3 = r1.allowPayment();	 Catch:{ NfcMessageStatusException -> 0x0071, BasicTlvException -> 0x0057, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
    L_0x0026:
        r1 = r6.preferences;	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r1 = r1.getCapabilityVasOnly();	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        if (r1 != 0) goto L_0x003f;
    L_0x002e:
        r1 = r6.preferences;	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r1 = r1.getCapabilityVasOverPayment();	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        if (r1 == 0) goto L_0x0038;
    L_0x0036:
        if (r2 != 0) goto L_0x003f;
    L_0x0038:
        if (r3 == 0) goto L_0x003f;
    L_0x003a:
        r1 = r6.paymentProcessor;	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r1.handlePayment();	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
    L_0x003f:
        r1 = r6.transceiver;	 Catch:{ Exception -> 0x006f }
        r1.close();	 Catch:{ Exception -> 0x006f }
    L_0x0044:
        return r0;
    L_0x0045:
        r1 = move-exception;
        r2 = r3;
    L_0x0047:
        r5 = r6.preferences;	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        r5 = r5.getStopPaymentIfSmartTapFails();	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        if (r5 != 0) goto L_0x0026;
    L_0x004f:
        r1 = r1.allowsPayment();	 Catch:{ BasicTlvException -> 0x0057, NfcMessageStatusException -> 0x006d, IOException -> 0x006b, FormatException -> 0x0069, all -> 0x0060 }
        if (r1 == 0) goto L_0x0026;
    L_0x0055:
        r3 = r4;
        goto L_0x0026;
    L_0x0057:
        r0 = move-exception;
    L_0x0058:
        r1 = r6.transceiver;	 Catch:{ Exception -> 0x005e }
        r1.close();	 Catch:{ Exception -> 0x005e }
        goto L_0x0044;
    L_0x005e:
        r1 = move-exception;
        goto L_0x0044;
    L_0x0060:
        r0 = move-exception;
        r1 = r6.transceiver;	 Catch:{ Exception -> 0x0067 }
        r1.close();	 Catch:{ Exception -> 0x0067 }
    L_0x0066:
        throw r0;
    L_0x0067:
        r1 = move-exception;
        goto L_0x0066;
    L_0x0069:
        r0 = move-exception;
        goto L_0x0058;
    L_0x006b:
        r0 = move-exception;
        goto L_0x0058;
    L_0x006d:
        r0 = move-exception;
        goto L_0x0058;
    L_0x006f:
        r1 = move-exception;
        goto L_0x0044;
    L_0x0071:
        r1 = move-exception;
        goto L_0x0047;
    L_0x0073:
        r2 = r3;
        r3 = r4;
        goto L_0x0026;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.commerce.tapandpay.terminalapp.main.IsoDepTransceiver.doInBackground(java.lang.Void[]):java.lang.Exception");
    }

    protected void onPostExecute(Exception exception) {
        if (exception != null) {
            this.onError.onError(exception);
        }
    }

    protected void onCancelled(Exception exception) {
        if (exception != null) {
            this.onError.onError(exception);
        }
    }
}
