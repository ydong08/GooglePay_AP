package com.google.android.libraries.commerce.hce.terminal.smarttap;

import android.content.Context;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.MerchantCapability;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvInvalidLengthException;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvInvalidTagException;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvUtil;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.nfc.TlvParser;
import com.google.android.libraries.commerce.hce.terminal.nfc.Transceiver;
import com.google.android.libraries.commerce.hce.terminal.settings.Preferences;
import com.google.android.libraries.commerce.hce.terminal.settings.SmartTapAid;
import com.google.android.libraries.commerce.hce.util.Bcd;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.primitives.Bytes;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Version13 implements SmartTapHandler {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final Context context;
    private final Preferences preferences;
    private final SmartTapAid smartTapAid;
    private final TlvParser tlvParser;
    private final Transceiver transceiver;
    private boolean transmittedValuables = false;

    public Version13(Context context, Transceiver transceiver, Preferences preferences, SmartTapAid smartTapAid) {
        this.context = context;
        this.transceiver = transceiver;
        this.preferences = preferences;
        this.smartTapAid = smartTapAid;
        this.tlvParser = new TlvParser(context);
    }

    public SmartTapAid getSmartTapAid() {
        return this.smartTapAid;
    }

    public boolean supportsSkippingSelect() {
        return false;
    }

    public NfcMessage selectSmartTap() throws IOException {
        Aid aid;
        String string;
        switch (this.smartTapAid) {
            case V1_3_A:
                aid = SmartTapValues.SMART_TAP_AID_V1_3_OLD;
                string = this.context.getString(R.string.select_smart_tap_v1_3a);
                break;
            case V1_3_B:
                aid = Aid.SMART_TAP_AID_V1_3;
                string = this.context.getString(R.string.select_smart_tap_v1_3b);
                break;
            default:
                LOG.w("Smart tap AID not recognized (%s). Reverting to Smart Tap 1.3 (new) AID.", this.smartTapAid);
                aid = Aid.SMART_TAP_AID_V1_3;
                string = this.context.getString(R.string.select_smart_tap_v1_3b);
                break;
        }
        return this.transceiver.transceiveSelect(aid.getSelectCommand(), string);
    }

    public void executeSmartTap() throws BasicTlvInvalidLengthException, BasicTlvInvalidTagException, IOException {
        if (this.preferences.getSendServiceRequest()) {
            this.transceiver.transceive(getGetSmartTapDataCommand(), this.tlvParser);
        }
    }

    public boolean transmittedValuables() {
        return this.transmittedValuables;
    }

    public boolean allowPayment() {
        return true;
    }

    private byte[] getGetSmartTapDataCommand() throws BasicTlvInvalidLengthException, BasicTlvInvalidTagException {
        r1 = new byte[3][];
        r1[1] = new byte[]{(byte) Bytes.concat(getDateTlv().toByteArray(), getSmartTapTlv().toByteArray(), getMerchantTlv().toByteArray(), getLocationTlv().toByteArray(), getMerchantCapabilitiesTlv().toByteArray()).length};
        r1[2] = r0;
        return Bytes.concat(r1);
    }

    private static BasicTlv getDateTlv() {
        return BasicTlvUtil.tlv(57105, Hex.decode(DATE_FORMAT.format(new Date())));
    }

    private static BasicTlv getSmartTapTlv() {
        return BasicTlvUtil.tlv(57106, SmartTapValues.SMART_TAP_V1_TLV_BYTES);
    }

    private BasicTlv getMerchantTlv() {
        Object encode = Bcd.encode((long) this.preferences.getMerchantId());
        Preconditions.checkArgument(encode.length <= 8, "Merchant ID must be less than %s bytes long. %s is %s bytes long.", Integer.valueOf(8), encode, Integer.valueOf(encode.length));
        return BasicTlvUtil.tlv(57137, encode);
    }

    private BasicTlv getLocationTlv() {
        Object locationId = this.preferences.getLocationId();
        Preconditions.checkArgument(locationId.length <= 32, "Location ID must be less than %s bytes long. %s is %s bytes long.", Integer.valueOf(32), locationId, Integer.valueOf(locationId.length));
        return BasicTlvUtil.tlv(57138, locationId);
    }

    private BasicTlv getMerchantCapabilitiesTlv() {
        Builder builder = ImmutableSet.builder();
        if (this.preferences.getRequestLoyalty()) {
            builder.add(MerchantCapability.LOYALTY_SUPPORT);
        }
        if (this.preferences.getRequestOffers()) {
            builder.add(MerchantCapability.OFFERS_SUPPORT);
        }
        return BasicTlvUtil.tlv(57139, MerchantCapability.compose(builder.build()));
    }

    public String toString() {
        String valueOf = String.valueOf(this.smartTapAid);
        return new StringBuilder(String.valueOf(valueOf).length() + 17).append("SmartTap ").append(valueOf).append(" Handler").toString();
    }
}
