package com.google.android.libraries.commerce.hce.terminal.smarttap;

import android.content.Context;
import android.nfc.FormatException;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.AidInfo;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.Ose;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.Ose.OseResponse;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvException;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2Cipher;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2Decryptor;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager.CryptoClientWrapper;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2ECKeyManager.KeyFactoryWrapper;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2HmacGenerator;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantSigner;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2SharedKey.Factory;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2SharedKey.KeyAgreementWrapper;
import com.google.android.libraries.commerce.hce.crypto.ValuablesCryptoException;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.NfcMessageStatusException;
import com.google.android.libraries.commerce.hce.terminal.nfc.TlvParser;
import com.google.android.libraries.commerce.hce.terminal.nfc.Transceiver;
import com.google.android.libraries.commerce.hce.terminal.settings.Preferences;
import com.google.android.libraries.commerce.hce.terminal.settings.SmartTapAid;
import com.google.android.libraries.commerce.hce.util.Decompressor;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bouncycastle.jce.ECNamedCurveTable;

public class SmartTapProcessor {
    private static final Map<Aid, SmartTapAid> AID_MAP = ImmutableMap.of(Aid.SMART_TAP_AID_V1_3, SmartTapAid.V1_3_B, Aid.SMART_TAP_AID_V2_0, SmartTapAid.V2_0);
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private boolean allowPayment = true;
    private final Context context;
    private final LocalBroadcastManager localBroadcastManager;
    private final Preferences preferences;
    private final TlvParser tlvParser;
    private final Transceiver transceiver;
    private boolean transmittedValuables = false;

    public SmartTapProcessor(Context context, LocalBroadcastManager localBroadcastManager, Transceiver transceiver, Preferences preferences) {
        this.context = context;
        this.localBroadcastManager = localBroadcastManager;
        this.transceiver = transceiver;
        this.preferences = preferences;
        this.tlvParser = new TlvParser(context);
    }

    public void handleSmartTap() throws BasicTlvException, NfcMessageStatusException, IOException, FormatException {
        String str;
        Collection smartTapAids = this.preferences.getSmartTapAids();
        FormattingLogger formattingLogger = LOG;
        String str2 = "SmartTap AIDs supported by the terminal: %s";
        Object[] objArr = new Object[1];
        if (smartTapAids.isEmpty()) {
            str = "none";
        } else {
            Object obj = smartTapAids;
        }
        objArr[0] = str;
        formattingLogger.i(str2, objArr);
        Collection<SmartTapHandler> smartTapHandlersToTry = getSmartTapHandlersToTry(smartTapAids);
        if (smartTapHandlersToTry.isEmpty()) {
            LOG.i("No SmartTap AIDs shared between terminal and handset.", new Object[0]);
            return;
        }
        LOG.i("SmartTap AIDs that the terminal will attempt to select: %s", smartTapHandlersToTry);
        for (SmartTapHandler smartTapHandler : smartTapHandlersToTry) {
            if (smartTapHandler.supportsSkippingSelect() && this.preferences.getAllowSkippingSmartTap2Select()) {
                LOG.i("Supports skipping Smart Tap select on %s, moving on to commands", smartTapHandler.getSmartTapAid().toString(this.context));
                smartTapHandler.executeSmartTap();
                this.transmittedValuables = smartTapHandler.transmittedValuables();
                this.allowPayment = smartTapHandler.allowPayment();
                return;
            }
            if (Iso7816StatusWord.NO_ERROR.equals(smartTapHandler.selectSmartTap().getStatusWord())) {
                LOG.i("Selected SmartTap %s.", smartTapHandler.getSmartTapAid().toString(this.context));
                smartTapHandler.executeSmartTap();
                this.transmittedValuables = smartTapHandler.transmittedValuables();
                this.allowPayment = smartTapHandler.allowPayment();
                return;
            }
            LOG.i("Failed to select SmartTap %s. Moving onto next AID.", smartTapHandler.getSmartTapAid().toString(this.context));
        }
        LOG.w("Failed to select any SmartTap AIDs. Giving up on SmartTap.", new Object[0]);
    }

    public boolean transmittedValuables() {
        return this.transmittedValuables;
    }

    public boolean allowPayment() {
        return this.allowPayment;
    }

    private Collection<SmartTapHandler> getSmartTapHandlersToTry(Collection<SmartTapAid> collection) throws BasicTlvException, IOException {
        if (this.preferences.getUseOse()) {
            LOG.i("Using OSE to get client supported SmartTap AIDs.", new Object[0]);
            return getSmartTapHandlersToTryFromOse(collection);
        }
        LOG.i("not using OSE. Will attempt all SmartTap AIDs supported by the terminal.", new Object[0]);
        return getSmartTapHandlers(Optional.absent(), getMapFromSmartTapAids(collection));
    }

    private Collection<SmartTapHandler> getSmartTapHandlersToTryFromOse(Collection<SmartTapAid> collection) throws BasicTlvException, IOException {
        NfcMessage transceiveSelect = this.transceiver.transceiveSelect(Aid.OSE.getSelectCommand(), this.context.getString(R.string.select_ose), this.tlvParser);
        if (Iso7816StatusWord.NO_ERROR.equals(transceiveSelect.getStatusWord())) {
            Optional mobileDeviceNonce;
            List aidInfos;
            try {
                OseResponse parse = Ose.parse(transceiveSelect.getValue());
                mobileDeviceNonce = parse.mobileDeviceNonce();
                aidInfos = parse.aidInfos();
            } catch (Throwable e) {
                LOG.e(e, "Exception caught while parsing OSE response.", new Object[0]);
                mobileDeviceNonce = Optional.absent();
                aidInfos = Collections.emptyList();
            }
            LOG.i("SmartTap AIDs supported by the client: %s", aidInfos);
            LOG.i("Smart Tap AIDs in common: %s", getOseSmartTapAids(aidInfos));
            return getSmartTapHandlers(mobileDeviceNonce, getOseSmartTapAids(aidInfos));
        }
        LOG.e("Unsuccessful status returned for select mobile wallet command. Will attempt all SmartTap AIDs supported by the terminal.", new Object[0]);
        return getSmartTapHandlers(Optional.absent(), getMapFromSmartTapAids(collection));
    }

    Collection<SmartTapHandler> getSmartTapHandlers(Optional<ByteArrayWrapper> optional, Map<SmartTapAid, AidInfo> map) {
        Collection arrayList = new ArrayList();
        for (Entry entry : map.entrySet()) {
            SmartTapAid smartTapAid = (SmartTapAid) entry.getKey();
            AidInfo aidInfo = (AidInfo) entry.getValue();
            switch (smartTapAid) {
                case V1_3_A:
                case V1_3_B:
                    arrayList.add(new Version13(this.context, this.transceiver, this.preferences, smartTapAid));
                    break;
                case V2_0:
                    SmartTap2Decryptor smartTap2Decryptor;
                    SmartTap2ECKeyManager smartTap2ECKeyManager = new SmartTap2ECKeyManager(new CryptoClientWrapper(), new KeyFactoryWrapper());
                    SmartTap2HmacGenerator smartTap2HmacGenerator = new SmartTap2HmacGenerator();
                    Factory factory = new Factory(new KeyAgreementWrapper());
                    SmartTap2Cipher smartTap2Cipher = new SmartTap2Cipher();
                    try {
                        smartTap2Decryptor = new SmartTap2Decryptor(smartTap2HmacGenerator, smartTap2ECKeyManager, factory, getKeyPairFromPrivateKeyBytes(smartTap2ECKeyManager, this.preferences.getEphemeralPrivateKey()), smartTap2Cipher);
                    } catch (ValuablesCryptoException e) {
                        LOG.e("Unable to decode private key", e);
                        smartTap2Decryptor = new SmartTap2Decryptor(smartTap2HmacGenerator, smartTap2ECKeyManager, factory, smartTap2Cipher);
                    }
                    Decompressor decompressor = new Decompressor();
                    arrayList.add(new Version2(this.context, this.localBroadcastManager, this.transceiver, this.preferences, new SmartTap2MerchantSigner(smartTap2ECKeyManager), smartTap2Decryptor, decompressor, aidInfo.proprietaryDataOptional(), optional));
                    break;
                default:
                    LOG.e("Unrecognized SmartTap AID %s will be ignored.", smartTapAid);
                    break;
            }
        }
        return arrayList;
    }

    private KeyPair getKeyPairFromPrivateKeyBytes(SmartTap2ECKeyManager smartTap2ECKeyManager, byte[] bArr) throws ValuablesCryptoException {
        PrivateKey decodePrivateKey = smartTap2ECKeyManager.decodePrivateKey(bArr);
        return new KeyPair(smartTap2ECKeyManager.decodeCompressedPublicKey(ECNamedCurveTable.getParameterSpec$5166KOBMC4NMOOBECSNL6T3ID5N6EEP99HNN4PPFC9NNARJ3F5HM2SRKDHIIUQJ3CKNN6S35CCNKAGQEC5MMAP23ELP7CPAGC5P62RB5EHIN4KRGCLHJM___0("prime256v1").getG().multiply(decodePrivateKey.getS()).getEncoded(true)), decodePrivateKey);
    }

    private static Map<SmartTapAid, AidInfo> getOseSmartTapAids(List<AidInfo> list) {
        Builder builder = ImmutableMap.builder();
        for (AidInfo aidInfo : list) {
            Aid aid = aidInfo.aid();
            if (AID_MAP.containsKey(aid)) {
                builder.put((SmartTapAid) AID_MAP.get(aid), aidInfo);
            } else {
                FormattingLogger formattingLogger = LOG;
                String valueOf = String.valueOf(aid);
                formattingLogger.w(new StringBuilder(String.valueOf(valueOf).length() + 38).append("Ignoring unknown AID in OSE response: ").append(valueOf).toString(), new Object[0]);
            }
        }
        return builder.build();
    }

    private static Map<SmartTapAid, AidInfo> getMapFromSmartTapAids(Collection<SmartTapAid> collection) {
        Builder builder = ImmutableMap.builder();
        int i = 1;
        for (SmartTapAid smartTapAid : collection) {
            Aid aid;
            switch (smartTapAid) {
                case V1_3_A:
                    LOG.w("Ignoring unsupported v1.3a", new Object[0]);
                    continue;
                case V1_3_B:
                    aid = Aid.SMART_TAP_AID_V1_3;
                    break;
                case V2_0:
                    aid = Aid.SMART_TAP_AID_V2_0;
                    break;
                default:
                    FormattingLogger formattingLogger = LOG;
                    String str = "Ignoring unknown case ";
                    String valueOf = String.valueOf(smartTapAid.toString());
                    if (valueOf.length() != 0) {
                        valueOf = str.concat(valueOf);
                    } else {
                        valueOf = new String(str);
                    }
                    formattingLogger.w(valueOf, new Object[0]);
                    continue;
            }
            int i2 = i + 1;
            builder.put(smartTapAid, AidInfo.create(aid, i, Optional.absent()));
            i = i2;
        }
        return builder.build();
    }
}
