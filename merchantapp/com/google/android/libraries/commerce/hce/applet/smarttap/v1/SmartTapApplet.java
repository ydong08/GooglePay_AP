package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import com.google.android.libraries.commerce.hce.applet.HceApplet;
import com.google.android.libraries.commerce.hce.applet.smarttap.SmartTapException;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.QualifierAnnotations.GetSmartTapData;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.QualifierAnnotations.PostTransactionData;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback.RedeemableEntity;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCommand.Response;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.util.Arrays;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Provider;

public class SmartTapApplet implements HceApplet {
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

    private SmartTapResponse createResponse(Set<? extends RedeemableEntity> set, long j, ResponseApdu responseApdu, int i) {
        return new SmartTapResponse(i, responseApdu, Long.valueOf(j), set);
    }

    private SmartTapResponse createResponse(ResponseApdu responseApdu, int i) {
        return new SmartTapResponse(i, responseApdu, null, null);
    }

    public SmartTapResponse processCommand(byte[] bArr, boolean z) throws SmartTapException {
        LOG.d("SmartTap v1.3 applet chosen", new Object[0]);
        byte b = bArr[0];
        byte b2 = bArr[1];
        if (b == (byte) 0 && b2 == (byte) -92) {
            if (Arrays.equals(SELECT_COMMAND, bArr)) {
                return createResponse(ResponseApdu.fromStatusWord(Iso7816StatusWord.NO_ERROR), b2);
            }
            return createResponse(ResponseApdu.fromStatusWord(Iso7816StatusWord.APPLET_SELECT_FAILED), b2);
        } else if (b != (byte) -112) {
            return createResponse(ResponseApdu.fromStatusWord(Iso7816StatusWord.CLA_NOT_SUPPORTED), b2);
        } else {
            Response moreData;
            SmartTapResponse createResponse;
            switch (b2) {
                case (byte) -64:
                    if (this.currentSmartTapCommand != null) {
                        moreData = this.currentSmartTapCommand.getMoreData();
                        createResponse = createResponse(moreData.getResponseApdu(), b2);
                        break;
                    }
                    moreData = new Response(ResponseApdu.fromStatusWord(Iso7816StatusWord.CONDITIONS_NOT_SATISFIED), (byte) 0);
                    createResponse = createResponse(moreData.getResponseApdu(), b2);
                    break;
                case (byte) 80:
                    this.currentSmartTapCommand = (SmartTapCommand) this.getSmartTapDataCommandProvider.get();
                    moreData = this.currentSmartTapCommand.process(bArr);
                    if (!(moreData instanceof GetSmartTapDataResponse)) {
                        createResponse = createResponse(moreData.getResponseApdu(), b2);
                        break;
                    }
                    GetSmartTapDataResponse getSmartTapDataResponse = (GetSmartTapDataResponse) moreData;
                    createResponse = createResponse(getSmartTapDataResponse.getTransmittedCards(), getSmartTapDataResponse.getMerchantId(), getSmartTapDataResponse.getResponseApdu(), b2);
                    break;
                case (byte) 82:
                    this.currentSmartTapCommand = (SmartTapCommand) this.postTransactionDataCommandProvider.get();
                    moreData = this.currentSmartTapCommand.process(bArr);
                    createResponse = createResponse(moreData.getResponseApdu(), b2);
                    break;
                default:
                    moreData = new Response(ResponseApdu.fromStatusWord(Iso7816StatusWord.INS_NOT_SUPPORTED), (byte) 0);
                    createResponse = createResponse(moreData.getResponseApdu(), b2);
                    break;
            }
            if (moreData == null || moreData.hasMoreData()) {
                return createResponse;
            }
            this.currentSmartTapCommand = null;
            return createResponse;
        }
    }
}
