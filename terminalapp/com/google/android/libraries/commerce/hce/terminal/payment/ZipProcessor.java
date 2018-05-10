package com.google.android.libraries.commerce.hce.terminal.payment;

import android.content.Context;
import com.google.android.libraries.commerce.hce.terminal.nfc.Transceiver;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.io.IOException;

public class ZipProcessor {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final Context context;
    private final Transceiver transceiver;

    ZipProcessor(Context context, Transceiver transceiver) {
        this.context = context;
        this.transceiver = transceiver;
    }

    void handleZipPayment() throws IOException {
        LOG.w("Zip Payment not yet supported!", new Object[0]);
    }
}
