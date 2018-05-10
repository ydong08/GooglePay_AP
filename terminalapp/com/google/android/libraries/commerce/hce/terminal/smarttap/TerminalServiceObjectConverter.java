package com.google.android.libraries.commerce.hce.terminal.smarttap;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Type;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SmartTapV2Exception;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.terminal.smarttap.TerminalServiceObject.Builder;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TerminalServiceObjectConverter implements ServiceObjectConverter {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private static final Set<Type> SUPPORTED_SERVICE_TYPES = ImmutableSet.of(Type.WALLET_CUSTOMER, Type.LOYALTY, Type.OFFER, Type.GIFT_CARD, Type.CLOSED_LOOP_CARD);

    public List<ServiceObject> convert(NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
        NdefRecord[] records = SmartTapV2Exception.tryParseNdefMessage(ndefRecord.getPayload(), "service record").getRecords();
        byte[] bArr = null;
        NdefRecord ndefRecord2 = null;
        List<NdefRecord> arrayList = new ArrayList();
        int length = records.length;
        int i = 0;
        while (i < length) {
            byte[] payload;
            NdefRecord ndefRecord3 = records[i];
            byte[] type = NdefRecords.getType(ndefRecord3, s);
            if (Arrays.equals(type, SmartTap2Values.getServiceIdNdefType(s))) {
                NdefRecord ndefRecord4 = ndefRecord2;
                payload = ndefRecord3.getPayload();
                ndefRecord3 = ndefRecord4;
            } else if (Arrays.equals(type, SmartTap2Values.ISSUER_NDEF_TYPE)) {
                payload = bArr;
            } else if (ndefRecord3.getTnf() == (short) 1) {
                byte[] id = ndefRecord3.getId();
                if (!(Arrays.equals(type, NdefRecord.RTD_TEXT) && Arrays.equals(id, SmartTap2Values.ISSUER_NDEF_TYPE))) {
                    LOG.w("Unsupported text value inside of service: %s", Hex.encodeUpper(id));
                    ndefRecord3 = ndefRecord2;
                }
                payload = bArr;
            } else {
                Type type2 = Type.get(type);
                if (SUPPORTED_SERVICE_TYPES.contains(type2)) {
                    arrayList.add(ndefRecord3);
                    ndefRecord3 = ndefRecord2;
                    payload = bArr;
                } else {
                    FormattingLogger formattingLogger = LOG;
                    String str = "Unsupported service type: %s";
                    Object[] objArr = new Object[1];
                    objArr[0] = type2 == null ? "unknown" : type2.toString();
                    formattingLogger.w(str, objArr);
                    ndefRecord3 = ndefRecord2;
                    payload = bArr;
                }
            }
            i++;
            bArr = payload;
            ndefRecord2 = ndefRecord3;
        }
        if (arrayList.isEmpty()) {
            LOG.w("No recognized service objects defined inside of service.", new Object[0]);
            return ImmutableList.of();
        } else if (s != (short) 0 || arrayList.size() <= 1) {
            List<ServiceObject> arrayList2 = new ArrayList();
            for (NdefRecord ndefRecord32 : arrayList) {
                Builder builder = TerminalServiceObject.builder();
                if (s == (short) 0) {
                    if (bArr != null) {
                        builder.setServiceId(bArr);
                    } else {
                        LOG.w("Service object contains no service ID.", new Object[0]);
                    }
                } else if (ndefRecord2 != null) {
                    builder.setIssuer(ndefRecord2, s);
                } else {
                    LOG.w("Service object contains no issuer.", new Object[0]);
                }
                parseServiceObjectRecord(builder, ndefRecord32, s);
                arrayList2.add(builder.build());
            }
            return arrayList2;
        } else {
            LOG.w("Multiple service objects defined inside of service.", new Object[0]);
            return ImmutableList.of();
        }
    }

    private void parseServiceObjectRecord(Builder builder, NdefRecord ndefRecord, short s) throws SmartTapV2Exception {
        builder.setType(NdefRecords.getType(ndefRecord, s));
        for (NdefRecord ndefRecord2 : SmartTapV2Exception.tryParseNdefMessage(ndefRecord.getPayload(), "service record object").getRecords()) {
            byte[] type = NdefRecords.getType(ndefRecord2, s);
            if (Arrays.equals(type, SmartTap2Values.getServiceIdNdefType(s))) {
                builder.setServiceId(ndefRecord2.getPayload());
            } else if (Arrays.equals(type, SmartTap2Values.ISSUER_NDEF_TYPE)) {
                builder.setIssuer(ndefRecord2, s);
            } else if (Arrays.equals(type, SmartTap2Values.SERVICE_NUMBER_NDEF_TYPE) || Arrays.equals(type, SmartTap2Values.CUSTOMER_ID_NDEF_TYPE)) {
                builder.setServiceNumber(ndefRecord2, s);
            } else if (Arrays.equals(type, SmartTap2Values.CUSTOMER_TAP_NDEF_TYPE)) {
                builder.setTapId(ndefRecord2, s);
            } else if (Arrays.equals(type, SmartTap2Values.CUSTOMER_DEVICE_NDEF_TYPE)) {
                builder.setDeviceId(ndefRecord2, s);
            } else if (Arrays.equals(type, SmartTap2Values.PIN_NDEF_TYPE)) {
                builder.setPin(ndefRecord2, s);
            } else if (Arrays.equals(type, SmartTap2Values.CVC_NDEF_TYPE)) {
                builder.setCvc(ndefRecord2, s);
            } else if (Arrays.equals(type, SmartTap2Values.EXPIRATION_NDEF_TYPE)) {
                builder.setExpiration(ndefRecord2, s);
            } else if (Arrays.equals(type, SmartTap2Values.CUSTOMER_LANGUAGE_NDEF_TYPE)) {
                builder.setLanguage(ndefRecord2, s);
            } else if (ndefRecord2.getTnf() == (short) 1 && Arrays.equals(ndefRecord2.getType(), NdefRecord.RTD_TEXT)) {
                byte[] id = ndefRecord2.getId();
                if (Arrays.equals(id, SmartTap2Values.ISSUER_NDEF_TYPE)) {
                    builder.setIssuer(ndefRecord2, s);
                } else if (Arrays.equals(id, SmartTap2Values.SERVICE_NUMBER_NDEF_TYPE) || Arrays.equals(type, SmartTap2Values.CUSTOMER_ID_NDEF_TYPE)) {
                    builder.setServiceNumber(ndefRecord2, s);
                } else if (Arrays.equals(id, SmartTap2Values.CUSTOMER_TAP_NDEF_TYPE)) {
                    builder.setTapId(ndefRecord2, s);
                } else if (Arrays.equals(id, SmartTap2Values.CUSTOMER_DEVICE_NDEF_TYPE)) {
                    builder.setDeviceId(ndefRecord2, s);
                } else if (Arrays.equals(id, SmartTap2Values.PIN_NDEF_TYPE)) {
                    builder.setPin(ndefRecord2, s);
                } else if (Arrays.equals(id, SmartTap2Values.CVC_NDEF_TYPE)) {
                    builder.setCvc(ndefRecord2, s);
                } else if (Arrays.equals(id, SmartTap2Values.EXPIRATION_NDEF_TYPE)) {
                    builder.setExpiration(ndefRecord2, s);
                } else if (Arrays.equals(id, SmartTap2Values.CUSTOMER_LANGUAGE_NDEF_TYPE)) {
                    builder.setLanguage(ndefRecord2, s);
                } else {
                    LOG.w("Unrecognized NDEF ID %s inside of service object TEXT record.", SmartTap2Values.getNdefType(id));
                }
            } else {
                LOG.w("Unrecognized service object field ndef ID %s", SmartTap2Values.getNdefType(type));
            }
        }
    }
}
