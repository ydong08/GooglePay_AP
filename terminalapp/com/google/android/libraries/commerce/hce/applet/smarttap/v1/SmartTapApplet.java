package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import com.google.android.libraries.commerce.hce.applet.smarttap.v1.QualifierAnnotations.GetSmartTapData;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.QualifierAnnotations.PostTransactionData;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import javax.inject.Inject;
import javax.inject.Provider;

public class SmartTapApplet {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private static final byte[] SELECT_COMMAND = Aid.SMART_TAP_AID_V1_3.getSelectCommand();
    private SmartTapCommand currentSmartTapCommand = null;
    private Provider<SmartTapCommand> getSmartTapDataCommandProvider;
    private Provider<SmartTapCommand> postTransactionDataCommandProvider;

    @Inject
    public SmartTapApplet(@GetSmartTapData Provider<SmartTapCommand> provider, @PostTransactionData Provider<SmartTapCommand> provider2) {
        this.getSmartTapDataCommandProvider = provider;
        this.postTransactionDataCommandProvider = provider2;
    }
}
