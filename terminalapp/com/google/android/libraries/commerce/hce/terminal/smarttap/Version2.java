package com.google.android.libraries.commerce.hce.terminal.smarttap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.android.libraries.commerce.hce.applet.smarttap.ose.SmartTap2ProprietaryData;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NewService;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.NewService.ServiceType;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Type;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceStatus;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceStatus.Operation;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceStatus.Usage;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.Session.Status;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.common.SmartTapStatusWord;
import com.google.android.libraries.commerce.hce.common.StatusWords;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2Decryptor;
import com.google.android.libraries.commerce.hce.crypto.SmartTap2MerchantSigner;
import com.google.android.libraries.commerce.hce.crypto.ValuablesCryptoException;
import com.google.android.libraries.commerce.hce.crypto.Version0EncryptionParameters;
import com.google.android.libraries.commerce.hce.crypto.Version1EncryptionParameters;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.StatusWord.Code;
import com.google.android.libraries.commerce.hce.ndef.Format;
import com.google.android.libraries.commerce.hce.ndef.NdefMessages;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.ndef.Primitive;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.terminal.nfc.ApduParser;
import com.google.android.libraries.commerce.hce.terminal.nfc.NdefParser;
import com.google.android.libraries.commerce.hce.terminal.nfc.NdefParser.ParsedNdefNfc;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.Action;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.NfcMessageStatusException;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage.ParsedNfc;
import com.google.android.libraries.commerce.hce.terminal.nfc.Transceiver;
import com.google.android.libraries.commerce.hce.terminal.settings.Preferences;
import com.google.android.libraries.commerce.hce.terminal.settings.SmartTapAid;
import com.google.android.libraries.commerce.hce.util.Bcd;
import com.google.android.libraries.commerce.hce.util.ByteArrays;
import com.google.android.libraries.commerce.hce.util.Decompressor;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Shorts;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.inject.Inject;

public class Version2 implements SmartTapHandler {
    private static final byte[] LE_BYTE = new byte[]{(byte) 0};
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private static final Multimap<ByteArrayWrapper, NdefRecord> NO_ADDED_NDEFS = ImmutableMultimap.of();
    private static final Set<ByteArrayWrapper> NO_REMOVED_NDEFS = ImmutableSet.of();
    private boolean allowPayment = true;
    private final Context context;
    @Inject
    ServiceObjectConverter converter;
    private final Decompressor decompressor;
    private final SmartTap2Decryptor decryptor;
    private byte[] deviceMaxVersion;
    private byte[] deviceMinVersion;
    private byte[] encodedMerchantId;
    private byte[] handsetNonce;
    private final LocalBroadcastManager localBroadcastManager;
    private final NdefParser ndefParser;
    private byte[] negotiateCommandBytes;
    private final Optional<SmartTap2ProprietaryData> oseProprietaryData;
    private final Preferences preferences;
    private byte[] signature;
    private final SmartTap2MerchantSigner signer;
    private final boolean supportsSkippingSelect;
    private byte[] terminalNonce;
    private final Transceiver transceiver;
    private boolean transmittedValuables = false;

    static abstract class CommandParameters {

        static abstract class Builder {
            private byte[] ndefType;
            private byte[] prefix;
            private ImmutableList<NdefRecord> recordsWithoutSession;
            private byte[] sessionId;
            private Short version;

            Builder() {
            }

            Builder setRecordsWithoutSession(List<NdefRecord> list) {
                return setRecordsWithoutSession(ImmutableList.copyOf((Collection) list));
            }

            Builder(byte b) {
                this();
            }

            public Builder setNdefType(byte[] bArr) {
                this.ndefType = bArr;
                return this;
            }

            public Builder setPrefix(byte[] bArr) {
                this.prefix = bArr;
                return this;
            }

            public Builder setRecordsWithoutSession(ImmutableList<NdefRecord> immutableList) {
                this.recordsWithoutSession = immutableList;
                return this;
            }

            public Builder setSessionId(byte[] bArr) {
                this.sessionId = bArr;
                return this;
            }

            public Builder setVersion(short s) {
                this.version = Short.valueOf(s);
                return this;
            }

            public CommandParameters build() {
                String str = "";
                if (this.ndefType == null) {
                    str = String.valueOf(str).concat(" ndefType");
                }
                if (this.prefix == null) {
                    str = String.valueOf(str).concat(" prefix");
                }
                if (this.recordsWithoutSession == null) {
                    str = String.valueOf(str).concat(" recordsWithoutSession");
                }
                if (this.sessionId == null) {
                    str = String.valueOf(str).concat(" sessionId");
                }
                if (this.version == null) {
                    str = String.valueOf(str).concat(" version");
                }
                if (str.isEmpty()) {
                    return new AutoValue_Version2_CommandParameters(this.ndefType, this.prefix, this.recordsWithoutSession, this.sessionId, this.version.shortValue());
                }
                String str2 = "Missing required properties:";
                str = String.valueOf(str);
                throw new IllegalStateException(str.length() != 0 ? str2.concat(str) : new String(str2));
            }
        }

        abstract byte[] ndefType();

        abstract byte[] prefix();

        abstract ImmutableList<NdefRecord> recordsWithoutSession();

        abstract byte[] sessionId();

        abstract short version();

        CommandParameters() {
        }

        static Builder builder() {
            return new Builder((byte) 0);
        }

        byte[] asBytes(byte b) {
            NdefMessage ndefMessage = new NdefMessage(Version2.getSessionNdef(sessionId(), b, version()), (NdefRecord[]) recordsWithoutSession().toArray(new NdefRecord[0]));
            byte[] toByteArray = new NdefMessage(NdefRecords.compose(ndefType(), Bytes.concat(Shorts.toByteArray(version()), ndefMessage.toByteArray()), version()), new NdefRecord[0]).toByteArray();
            r1 = new byte[4][];
            r1[1] = new byte[]{(byte) toByteArray.length};
            r1[2] = toByteArray;
            r1[3] = Version2.getLeByteArrayForVersion(version());
            return Bytes.concat(r1);
        }
    }

    static abstract class CommandResponse {
        abstract byte[] finalCommand();

        abstract byte nextSequenceNumber();

        abstract NfcMessage nfcResponse();

        CommandResponse() {
        }

        static CommandResponse create(byte[] bArr, NfcMessage nfcMessage, byte b) {
            return new AutoValue_Version2_CommandResponse(bArr, nfcMessage, b);
        }
    }

    static class NdefRecordWithSize {
        public NdefRecord record;
        public int size;

        public NdefRecordWithSize(NdefRecord ndefRecord, int i) {
            this.record = ndefRecord;
            this.size = i;
        }
    }

    public static class ParsedNfcSelectSmartTap2 implements ParsedNfc {
        public static final Creator<ParsedNfcSelectSmartTap2> CREATOR = new Creator<ParsedNfcSelectSmartTap2>() {
            public ParsedNfcSelectSmartTap2 createFromParcel(Parcel parcel) {
                return new ParsedNfcSelectSmartTap2(parcel);
            }

            public ParsedNfcSelectSmartTap2[] newArray(int i) {
                return new ParsedNfcSelectSmartTap2[i];
            }
        };
        private final byte[] handsetNonce;
        private final byte[] maxVersion;
        private final byte[] minVersion;
        private final StatusWord statusWord;
        private final String title;

        ParsedNfcSelectSmartTap2(String str, byte[] bArr, byte[] bArr2, byte[] bArr3, StatusWord statusWord) {
            this.title = str;
            this.minVersion = bArr;
            this.maxVersion = bArr2;
            this.handsetNonce = bArr3;
            this.statusWord = statusWord;
        }

        ParsedNfcSelectSmartTap2(String str, byte[] bArr, byte[] bArr2, byte[] bArr3) {
            this(str, bArr, bArr2, bArr3, Iso7816StatusWord.NO_ERROR);
        }

        public String getTitle() {
            return this.title;
        }

        public byte[] getMinVersion() {
            return this.minVersion;
        }

        public byte[] getMaxVersion() {
            return this.maxVersion;
        }

        public byte[] getHandsetNonce() {
            return this.handsetNonce;
        }

        public StatusWord getStatusWord() {
            return this.statusWord;
        }

        public boolean isEqualTo(ParsedNfc parsedNfc) {
            if (this == parsedNfc) {
                return true;
            }
            if (parsedNfc == null || parsedNfc.getClass() != getClass()) {
                return false;
            }
            ParsedNfcSelectSmartTap2 parsedNfcSelectSmartTap2 = (ParsedNfcSelectSmartTap2) parsedNfc;
            if (this.title.equals(parsedNfcSelectSmartTap2.title) && Arrays.equals(this.minVersion, parsedNfcSelectSmartTap2.minVersion) && Arrays.equals(this.maxVersion, parsedNfcSelectSmartTap2.maxVersion) && Arrays.equals(this.handsetNonce, parsedNfcSelectSmartTap2.handsetNonce)) {
                return true;
            }
            return false;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.title);
            parcel.writeInt(this.minVersion.length);
            parcel.writeByteArray(this.minVersion);
            parcel.writeInt(this.maxVersion.length);
            parcel.writeByteArray(this.maxVersion);
            parcel.writeInt(this.handsetNonce.length);
            parcel.writeByteArray(this.handsetNonce);
            parcel.writeInt(this.statusWord.toBytes().length);
            parcel.writeByteArray(this.statusWord.toBytes());
        }

        private ParsedNfcSelectSmartTap2(Parcel parcel) {
            this.title = parcel.readString();
            this.minVersion = new byte[parcel.readInt()];
            parcel.readByteArray(this.minVersion);
            this.maxVersion = new byte[parcel.readInt()];
            parcel.readByteArray(this.maxVersion);
            this.handsetNonce = new byte[parcel.readInt()];
            parcel.readByteArray(this.handsetNonce);
            byte[] bArr = new byte[parcel.readInt()];
            parcel.readByteArray(bArr);
            this.statusWord = StatusWords.get(bArr, Shorts.fromByteArray(this.maxVersion));
        }
    }

    public static class ParsedRecordsNfc implements ParsedNfc {
        public static final Creator<ParsedRecordsNfc> CREATOR = new Creator<ParsedRecordsNfc>() {
            public ParsedRecordsNfc createFromParcel(Parcel parcel) {
                return new ParsedRecordsNfc(parcel);
            }

            public ParsedRecordsNfc[] newArray(int i) {
                return new ParsedRecordsNfc[i];
            }
        };
        private final List<NdefRecord> records;
        private final String title;
        private final short version;

        ParsedRecordsNfc(String str, short s, List<NdefRecord> list) {
            this.title = str;
            this.version = s;
            this.records = list;
        }

        public String getTitle() {
            return this.title;
        }

        public short getVersion() {
            return this.version;
        }

        public List<NdefRecord> getRecords() {
            return this.records;
        }

        public StatusWord getStatusWord() {
            return Iso7816StatusWord.NO_ERROR;
        }

        public boolean isEqualTo(ParsedNfc parsedNfc) {
            if (this == parsedNfc) {
                return true;
            }
            if (parsedNfc == null || parsedNfc.getClass() != getClass()) {
                return false;
            }
            return this.records.equals(((ParsedRecordsNfc) parsedNfc).records);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.title);
            parcel.writeInt(this.version);
            parcel.writeList(this.records);
        }

        private ParsedRecordsNfc(Parcel parcel) {
            this.title = parcel.readString();
            this.version = (short) parcel.readInt();
            this.records = new ArrayList();
            parcel.readList(this.records, NdefRecord.class.getClassLoader());
        }
    }

    public static class SelectSmartTap2Parser implements ApduParser {
        private Context context;

        public SelectSmartTap2Parser(Context context) {
            this.context = context;
        }

        public NfcMessage parse(Action action, byte[] bArr, short s) {
            String string = this.context.getString(R.string.select_smart_tap_v2_0);
            ResponseApdu fromResponse = ResponseApdu.fromResponse(bArr);
            StatusWord statusWord = fromResponse.getStatusWord();
            if (statusWord != Iso7816StatusWord.NO_ERROR) {
                StatusWord fromBytes = SmartTapStatusWord.fromBytes(statusWord.toBytes());
                Version2.LOG.e("Select response contains error status word: %s", statusWord);
                return new NfcMessage(bArr, NfcMessage.getParseError(string, this.context.getString(R.string.select_smart_tap_v2_0_status_word, new Object[]{Integer.toHexString(statusWord.toInt())}), null, fromBytes));
            }
            byte[] responseData = fromResponse.getResponseData();
            if (responseData.length < 4) {
                Version2.LOG.e("Select response too short to min and max version: %s", Hex.encodeUpper(responseData));
                return new NfcMessage(bArr, NfcMessage.getParseError(string, this.context.getString(R.string.select_smart_tap_v2_0_missing_version)));
            }
            byte[] copyOf = Arrays.copyOf(responseData, 2);
            byte[] copyOfRange = Arrays.copyOfRange(responseData, 2, 4);
            if (responseData.length <= 4) {
                Version2.LOG.e("Select response too short to contain handset nonce: ", Hex.encodeUpper(responseData));
                return new NfcMessage(bArr, NfcMessage.getParseError(string, this.context.getString(R.string.select_smart_tap_v2_0_missing_ndef_message)));
            }
            try {
                responseData = null;
                for (NdefRecord ndefRecord : new NdefMessage(Arrays.copyOfRange(responseData, 4, responseData.length)).getRecords()) {
                    if (Arrays.equals(NdefRecords.getType(ndefRecord, s), SmartTap2Values.HANDSET_NONCE_NDEF_TYPE)) {
                        Primitive primitive = new Primitive(ndefRecord);
                        Preconditions.checkArgument(primitive.getFormat() == Format.BINARY, "Nonce not in binary format.");
                        responseData = primitive.getPayload();
                        if (responseData.length != 32) {
                            Version2.LOG.e("Handset nonce is %s bytes long, expected %s bytes.", Integer.valueOf(responseData.length), Integer.valueOf(32));
                            return new NfcMessage(bArr, NfcMessage.getParseError(string, this.context.getString(R.string.select_smart_tap_v2_0_wrong_nonce_length)));
                        }
                        responseData = Arrays.copyOf(responseData, 32);
                    } else {
                        Version2.LOG.w("Unrecognized NDEF type %s inside of select response: %s", SmartTap2Values.getNdefType(NdefRecords.getType(ndefRecord, s)));
                    }
                }
                if (responseData == null) {
                    Version2.LOG.w("Select response did not contain handset nonce inside of NDEF message.", new Object[0]);
                    return new NfcMessage(bArr, NfcMessage.getParseError(string, this.context.getString(R.string.select_smart_tap_v2_0_missing_nonce)));
                }
                return new NfcMessage(bArr, new ParsedNfcSelectSmartTap2(string, copyOf, copyOfRange, responseData));
            } catch (Throwable e) {
                Version2.LOG.w(e, "Exception while parsing NDEF message inside of select response.", new Object[0]);
                return new NfcMessage(bArr, NfcMessage.getParseError(string, this.context.getString(R.string.select_smart_tap_v2_0_ndef_parse_error)));
            }
        }
    }

    public Version2(Context context, LocalBroadcastManager localBroadcastManager, Transceiver transceiver, Preferences preferences, SmartTap2MerchantSigner smartTap2MerchantSigner, SmartTap2Decryptor smartTap2Decryptor, Decompressor decompressor, Optional<SmartTap2ProprietaryData> optional, Optional<ByteArrayWrapper> optional2) {
        ((InjectedApplication) context.getApplicationContext()).inject(this);
        this.context = context;
        this.localBroadcastManager = localBroadcastManager;
        this.transceiver = transceiver;
        this.preferences = preferences;
        this.ndefParser = new NdefParser(context);
        this.signer = smartTap2MerchantSigner;
        this.decryptor = smartTap2Decryptor;
        this.decompressor = decompressor;
        this.oseProprietaryData = optional;
        if (this.oseProprietaryData.isPresent()) {
            SmartTap2ProprietaryData smartTap2ProprietaryData = (SmartTap2ProprietaryData) this.oseProprietaryData.get();
            if (smartTap2ProprietaryData.minVersion().isPresent()) {
                this.deviceMinVersion = Shorts.toByteArray(((Short) smartTap2ProprietaryData.minVersion().get()).shortValue());
            }
            if (smartTap2ProprietaryData.maxVersion().isPresent()) {
                this.deviceMaxVersion = Shorts.toByteArray(((Short) smartTap2ProprietaryData.maxVersion().get()).shortValue());
            }
            if (smartTap2ProprietaryData.mobileDeviceNonce().isPresent()) {
                this.handsetNonce = ((ByteArrayWrapper) smartTap2ProprietaryData.mobileDeviceNonce().get()).array();
            }
            this.supportsSkippingSelect = supportSkippingSelectForProprietaryData(smartTap2ProprietaryData, optional2.isPresent());
        } else {
            this.supportsSkippingSelect = false;
        }
        if (this.handsetNonce == null && optional2.isPresent()) {
            this.handsetNonce = ((ByteArrayWrapper) optional2.get()).array();
        }
    }

    public SmartTapAid getSmartTapAid() {
        return SmartTapAid.V2_0;
    }

    public boolean supportsSkippingSelect() {
        return this.supportsSkippingSelect;
    }

    public NfcMessage selectSmartTap() throws IOException, NfcMessageStatusException {
        NfcMessage transceiveSelectWithRetries = transceiveSelectWithRetries(this.context.getString(R.string.select_smart_tap_v2_0), this.preferences.getSmartTapVersion());
        if (Iso7816StatusWord.NO_ERROR.equals(transceiveSelectWithRetries.getStatusWord())) {
            ParsedNfcSelectSmartTap2 parsedNfcSelectSmartTap2 = (ParsedNfcSelectSmartTap2) transceiveSelectWithRetries.getParsedNfc();
            this.deviceMinVersion = parsedNfcSelectSmartTap2.getMinVersion();
            this.deviceMaxVersion = parsedNfcSelectSmartTap2.getMaxVersion();
            this.handsetNonce = parsedNfcSelectSmartTap2.getHandsetNonce();
            return transceiveSelectWithRetries;
        }
        StatusWord statusWord = transceiveSelectWithRetries.getStatusWord();
        String valueOf = String.valueOf(Hex.encode(transceiveSelectWithRetries.getStatusWord().toBytes()));
        throw new NfcMessageStatusException(statusWord, new StringBuilder(String.valueOf(valueOf).length() + 55).append("Received unexpected status word ").append(valueOf).append(" from Select Smart Tap.").toString());
    }

    public void executeSmartTap() throws NfcMessageStatusException, IOException, FormatException {
        short fromByteArray = Shorts.fromByteArray(this.deviceMinVersion);
        if (SmartTap2Values.SMARTTAP_MIN_VERSION_SHORT > Shorts.fromByteArray(this.deviceMaxVersion) || SmartTap2Values.SMARTTAP_MAX_VERSION_SHORT < fromByteArray) {
            LOG.d("Skipping smarttap. No overlap in terminal and device smarttap versions. Terminal min version: %s  max version: %s. Device min version: %s  max version: %s. ", Hex.encodeUpper(SmartTap2Values.SMARTTAP_MIN_VERSION), Hex.encodeUpper(SmartTap2Values.SMARTTAP_MAX_VERSION), Hex.encodeUpper(this.deviceMinVersion), Hex.encodeUpper(this.deviceMaxVersion));
            return;
        }
        Optional optional;
        byte b;
        byte b2;
        CommandResponse transceiveCommandWithRetries;
        byte[] generateNewSessionId = generateNewSessionId();
        short smartTapVersion = this.preferences.getSmartTapVersion();
        byte[] bArr = new byte[0];
        List<NdefRecord> arrayList = new ArrayList();
        Optional absent = Optional.absent();
        if (this.preferences.getEncryptionSupprted()) {
            Optional of = Optional.of(this.decryptor);
            CommandResponse transceiveNegotiateSmartTapDataCommandWithRetries = transceiveNegotiateSmartTapDataCommandWithRetries(generateNewSessionId, (byte) 1, (SmartTap2Decryptor) of.get(), smartTapVersion);
            this.negotiateCommandBytes = (byte[]) transceiveNegotiateSmartTapDataCommandWithRetries.finalCommand().clone();
            NfcMessage nfcResponse = transceiveNegotiateSmartTapDataCommandWithRetries.nfcResponse();
            nfcResponse.verifyStatus(this.context, smartTapVersion, Code.SUCCESS, Code.SUCCESS_PRESIGNED_AUTH, Code.AUTH_FAILED);
            byte nextSequenceNumber = transceiveNegotiateSmartTapDataCommandWithRetries.nextSequenceNumber();
            byte[] extractHandsetPublicKeyBytes = extractHandsetPublicKeyBytes(nfcResponse, smartTapVersion);
            if (smartTapVersion == (short) 0) {
                try {
                    this.decryptor.setCryptoParams(new Version0EncryptionParameters(extractHandsetPublicKeyBytes, this.handsetNonce, this.negotiateCommandBytes));
                } catch (ValuablesCryptoException e) {
                    e.printStackTrace();
                    optional = of;
                    b = nextSequenceNumber;
                }
            } else if (smartTapVersion >= (short) 1) {
                this.decryptor.setCryptoParams(new Version1EncryptionParameters(extractHandsetPublicKeyBytes, this.handsetNonce, this.terminalNonce, this.encodedMerchantId, this.decryptor.getEphemeralPublicKeyBytes(), this.signature));
            }
            optional = of;
            b = nextSequenceNumber;
        } else {
            Optional optional2 = absent;
            b = (byte) 1;
            optional = optional2;
        }
        if (this.preferences.getSendServiceRequest()) {
            List<CommandResponse> transceiveServiceRequestCommandWithRetries = transceiveServiceRequestCommandWithRetries(generateNewSessionId, b, smartTapVersion);
            byte[] bArr2 = bArr;
            b2 = b;
            for (CommandResponse transceiveCommandWithRetries2 : transceiveServiceRequestCommandWithRetries) {
                if (smartTapVersion == (short) 0) {
                    arrayList.addAll(handleV0DataRequest(transceiveCommandWithRetries2.nfcResponse(), optional, smartTapVersion));
                } else {
                    bArr2 = Bytes.concat(bArr2, extractGetDataBytes(transceiveCommandWithRetries2.nfcResponse()));
                }
                b2 = transceiveCommandWithRetries2.nextSequenceNumber();
            }
            nfcResponse = ((CommandResponse) transceiveServiceRequestCommandWithRetries.get(transceiveServiceRequestCommandWithRetries.size() - 1)).nfcResponse();
            nfcResponse.verifyStatus(this.context, smartTapVersion, Code.SUCCESS, Code.SUCCESS_NO_PAYLOAD, Code.SUCCESS_NO_PAYLOAD_PAYMENT_NOT_READY, Code.SUCCESS_WITH_PAYLOAD_PAYMENT_NOT_READY);
            this.allowPayment = nfcResponse.getStatusWord().allowsPayment();
            if (smartTapVersion >= (short) 1) {
                LOG.d("Concatenated get data bytes: 0x%s", Hex.encodeUpper(bArr2));
                arrayList.addAll(getServiceRecordsFromGetDataBytes(bArr2, smartTapVersion, this.decryptor, this.decompressor));
                displayConstructedServiceRecords(smartTapVersion, arrayList);
            }
        } else {
            b2 = b;
        }
        for (NdefRecord convert : arrayList) {
            try {
                for (ServiceObject type : this.converter.convert(convert, smartTapVersion)) {
                    if (type.type() != Type.WALLET_CUSTOMER) {
                        this.transmittedValuables = true;
                        break;
                    }
                }
            } catch (Throwable e2) {
                LOG.w(e2, "Failed to convert service object.", new Object[0]);
            }
        }
        for (CommandParameters transceiveCommandWithRetries3 : getPushSmartTapDataCommand(generateNewSessionId, r3, arrayList, smartTapVersion)) {
            transceiveCommandWithRetries2 = transceiveCommandWithRetries(transceiveCommandWithRetries3, b2);
            transceiveCommandWithRetries2.nfcResponse().verifySuccessStatus(this.context, smartTapVersion);
            b2 = transceiveCommandWithRetries2.nextSequenceNumber();
        }
    }

    public boolean transmittedValuables() {
        return this.transmittedValuables;
    }

    public boolean allowPayment() {
        return this.allowPayment;
    }

    private static byte[] getLeByteArrayForVersion(short s) {
        if (s == (short) 0) {
            return new byte[0];
        }
        return LE_BYTE;
    }

    private CommandResponse transceiveNegotiateSmartTapDataCommandWithRetries(byte[] bArr, byte b, SmartTap2Decryptor smartTap2Decryptor, short s) throws IOException {
        return transceiveCommandWithRetries(CommandParameters.builder().setNdefType(SmartTap2Values.NEGOTIATE_NDEF_TYPE).setPrefix(SmartTapValues.NEGOTIATE_SMARTTAP_DATA_PREFIX).setRecordsWithoutSession(ImmutableList.of(getCryptoParamsNdef(smartTap2Decryptor, s))).setSessionId(bArr).setVersion(s).build(), b);
    }

    private List<CommandResponse> transceiveServiceRequestCommandWithRetries(byte[] bArr, byte b, short s) throws IOException {
        Builder builder;
        ImmutableList of = ImmutableList.of(getMerchantNdef(s), getServiceListNdef(s), getPosCapabilitiesNdef(s));
        int retryCount = this.preferences.getRetryCount();
        while (true) {
            builder = ImmutableList.builder();
            byte[] asBytes = CommandParameters.builder().setNdefType(SmartTap2Values.SERVICE_REQUEST_NDEF_TYPE).setPrefix(SmartTapValues.GET_SMARTTAP_DATA_PREFIX).setRecordsWithoutSession(of).setSessionId(bArr).setVersion(s).build().asBytes(b);
            NfcMessage transceive = this.transceiver.transceive(asBytes, this.ndefParser, s);
            b = (byte) (b + 2);
            builder.add(CommandResponse.create(asBytes, transceive, b));
            while (dataRemaining(transceive)) {
                if (s == (short) 0) {
                    byte[] asBytes2 = CommandParameters.builder().setNdefType(SmartTap2Values.ADDITIONAL_SERVICE_NDEF_TYPE).setPrefix(SmartTapValues.GET_REMAINING_DATA_PREFIX).setRecordsWithoutSession(ImmutableList.of()).setSessionId(bArr).setVersion(s).build().asBytes(b);
                    b = (byte) (b + 2);
                    asBytes = asBytes2;
                } else {
                    asBytes = Bytes.concat(SmartTapValues.GET_REMAINING_DATA_PREFIX, new byte[1], getLeByteArrayForVersion(s));
                }
                transceive = this.transceiver.transceive(asBytes, this.ndefParser, s);
                builder.add(CommandResponse.create(asBytes, transceive, b));
            }
            if (!transceive.getStatusWord().isRetriableFailure()) {
                break;
            }
            int i = retryCount - 1;
            if (retryCount <= 0) {
                break;
            }
            retryCount = i;
        }
        return builder.build();
    }

    private List<CommandParameters> getPushSmartTapDataCommand(byte[] bArr, byte b, List<NdefRecord> list, short s) throws FormatException {
        List<NdefRecordWithSize> serviceNdefsWithSize = getServiceNdefsWithSize(list, s);
        List<CommandParameters> arrayList = new ArrayList();
        if (serviceNdefsWithSize.isEmpty()) {
            return arrayList;
        }
        NdefRecord sessionNdef = getSessionNdef(bArr, b, s);
        NdefRecord posCapabilitiesNdef = getPosCapabilitiesNdef(s);
        int initialPushBackSize = getInitialPushBackSize(sessionNdef, posCapabilitiesNdef, s);
        List arrayList2 = new ArrayList();
        arrayList2.add(posCapabilitiesNdef);
        int i = initialPushBackSize;
        for (NdefRecordWithSize ndefRecordWithSize : serviceNdefsWithSize) {
            if (ndefRecordWithSize.size + i > 255) {
                arrayList.add(CommandParameters.builder().setNdefType(SmartTap2Values.PUSH_SERVICE_NDEF_TYPE).setPrefix(SmartTapValues.PUSH_DATA_PREFIX).setRecordsWithoutSession(arrayList2).setSessionId(bArr).setVersion(s).build());
                b = (byte) (b + 2);
                i = getInitialPushBackSize(getSessionNdef(bArr, b, s), null, s);
                arrayList2.clear();
            }
            arrayList2.add(ndefRecordWithSize.record);
            i = ndefRecordWithSize.size + i;
        }
        arrayList.add(CommandParameters.builder().setNdefType(SmartTap2Values.PUSH_SERVICE_NDEF_TYPE).setPrefix(SmartTapValues.PUSH_DATA_PREFIX).setRecordsWithoutSession(arrayList2).setSessionId(bArr).setVersion(s).build());
        return arrayList;
    }

    private static byte[] generateNewSessionId() {
        byte[] bArr = new byte[8];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }

    private static NdefRecord getSessionNdef(byte[] bArr, byte b, short s) {
        byte[] bArr2 = new byte[]{b};
        byte[] bArr3 = new byte[]{Status.OK.value()};
        return NdefRecords.compose(SmartTap2Values.SESSION_NDEF_TYPE, Bytes.concat(bArr, bArr2, bArr3), s);
    }

    private NdefRecord getCryptoParamsNdef(SmartTap2Decryptor smartTap2Decryptor, short s) {
        byte[] bArr;
        NdefRecord createPrimitive = NdefRecords.createPrimitive(s == (short) 0 ? SmartTap2Values.MERCHANT_ID_V0_NDEF_TYPE : SmartTap2Values.COLLECTOR_ID_NDEF_TYPE, Integer.valueOf(this.preferences.getMerchantId()), s);
        this.terminalNonce = Arrays.copyOf(this.preferences.getTerminalNonce(), 32);
        byte[] ephemeralPublicKeyBytes = smartTap2Decryptor.getEphemeralPublicKeyBytes();
        byte[] updatePayloadLength = ByteArrays.updatePayloadLength(ByteBuffer.allocate(4).putInt(this.preferences.getLongTermKeyVersion()).array(), 4);
        boolean usePresignedAuth = this.preferences.usePresignedAuth();
        byte[] bArr2 = usePresignedAuth ? new byte[32] : this.handsetNonce;
        byte[] concat;
        if (s == (short) 0) {
            concat = Bytes.concat(this.terminalNonce, bArr2, ephemeralPublicKeyBytes, updatePayloadLength);
            this.encodedMerchantId = Bcd.encodeWithPadding((long) this.preferences.getMerchantId(), 4);
            bArr = concat;
        } else {
            byte b;
            byte[] bArr3 = new byte[1];
            if (usePresignedAuth) {
                b = (byte) 0;
            } else {
                b = (byte) 1;
            }
            bArr3[0] = b;
            concat = Bytes.concat(this.terminalNonce, bArr3, ephemeralPublicKeyBytes, updatePayloadLength);
            this.encodedMerchantId = ByteArrays.updatePayloadLength(Primitive.fromServiceRecord(createPrimitive, s).getPayload(), 4);
            bArr = concat;
        }
        try {
            this.signature = this.signer.generateSignature(this.preferences.getLongTermPrivateKey(), this.terminalNonce, bArr2, this.encodedMerchantId, ephemeralPublicKeyBytes);
        } catch (Throwable e) {
            LOG.d(e, "Unable to sign negotiate command.", new Object[0]);
            this.signature = new byte[72];
            Arrays.fill(this.signature, (byte) -85);
        }
        NdefMessage ndefMessage = new NdefMessage(NdefRecords.createPrimitive(SmartTap2Values.SIGNATURE_NDEF_TYPE, Format.BINARY, this.signature, s), new NdefRecord[]{createPrimitive});
        return NdefRecords.compose(SmartTap2Values.CRYPTO_PARAMS_NDEF_TYPE, Bytes.concat(bArr, ndefMessage.toByteArray()), s);
    }

    private NdefRecord getMerchantNdef(short s) {
        this.encodedMerchantId = ByteArrays.updatePayloadLength(Primitive.fromServiceRecord(NdefRecords.createPrimitive(s == (short) 0 ? SmartTap2Values.MERCHANT_ID_V0_NDEF_TYPE : SmartTap2Values.COLLECTOR_ID_NDEF_TYPE, Integer.valueOf(this.preferences.getMerchantId()), s), s).getPayload(), 4);
        Optional absent = Optional.absent();
        Optional absent2 = Optional.absent();
        Optional absent3 = Optional.absent();
        Optional absent4 = Optional.absent();
        byte[] locationId = this.preferences.getLocationId();
        byte[] terminalId = this.preferences.getTerminalId();
        String merchantName = this.preferences.getMerchantName();
        int merchantCategory = this.preferences.getMerchantCategory();
        if (locationId != null && locationId.length > 0) {
            absent = Optional.of(NdefRecords.createPrimitive(SmartTap2Values.LOCATION_ID_NDEF_TYPE, Format.BINARY, locationId, s));
        }
        if (terminalId != null && terminalId.length > 0) {
            absent2 = Optional.of(NdefRecords.createPrimitive(SmartTap2Values.TERMINAL_ID_NDEF_TYPE, Format.BINARY, terminalId, s));
        }
        if (!Strings.isNullOrEmpty(merchantName)) {
            absent3 = Optional.of(NdefRecords.createText(SmartTap2Values.MERCHANT_NAME_NDEF_TYPE, merchantName, s));
        }
        if (merchantCategory > 0) {
            absent4 = Optional.of(NdefRecords.createPrimitive(SmartTap2Values.MERCHANT_CATEGORY_NDEF_TYPE, Integer.valueOf(merchantCategory), s));
        }
        return NdefRecords.compose(SmartTap2Values.MERCHANT_NDEF_TYPE, ((NdefMessage) NdefMessages.fromOptionals(NO_ADDED_NDEFS, NO_REMOVED_NDEFS, null, s, Optional.of(r4), absent, absent2, absent3, absent4).get()).toByteArray(), s);
    }

    private NdefRecord getServiceListNdef(short s) {
        List arrayList = new ArrayList();
        if (this.preferences.getAllValuables()) {
            arrayList.add(getServiceRequest(Type.ALL, s));
        }
        if (this.preferences.getAllValuablesExceptPpse()) {
            arrayList.add(getServiceRequest(Type.ALL_EXCEPT_PPSE, s));
        }
        if (this.preferences.getCustomerInfo()) {
            arrayList.add(getServiceRequest(Type.WALLET_CUSTOMER, s));
        }
        if (this.preferences.getRequestLoyalty()) {
            arrayList.add(getServiceRequest(Type.LOYALTY, s));
        }
        if (this.preferences.getRequestOffers()) {
            arrayList.add(getServiceRequest(Type.OFFER, s));
        }
        if (this.preferences.getRequestGiftCards()) {
            arrayList.add(getServiceRequest(Type.GIFT_CARD, s));
        }
        if (this.preferences.getRequestPlc()) {
            arrayList.add(getServiceRequest(Type.CLOSED_LOOP_CARD, s));
        }
        if (arrayList.isEmpty()) {
            return NdefRecords.compose(SmartTap2Values.SERVICE_LIST_NDEF_TYPE, new byte[0], s);
        }
        return NdefRecords.compose(SmartTap2Values.SERVICE_LIST_NDEF_TYPE, new NdefMessage((NdefRecord[]) arrayList.toArray(new NdefRecord[arrayList.size()])).toByteArray(), s);
    }

    private NdefRecord getServiceRequest(Type type, short s) {
        return NdefRecords.compose(SmartTap2Values.SERVICE_TYPE_REQUEST_NDEF_TYPE, new byte[]{type.value()}, s);
    }

    private NdefRecord getPosCapabilitiesNdef(short s) {
        int i;
        int i2 = 8;
        int i3 = this.preferences.getCapabilityStandalone() ? 1 : 0;
        if (this.preferences.getCapabilitySemiIntegrated()) {
            i = 2;
        } else {
            i = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityUnattended()) {
            i3 = 4;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityOnline()) {
            i3 = 8;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityOffline()) {
            i3 = 16;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityMmp()) {
            i3 = 32;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityZlib()) {
            i3 = 64;
        } else {
            i3 = 0;
        }
        byte b = (byte) (i3 | i);
        if (this.preferences.getCapabilityPrinter()) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        if (this.preferences.getCapabilityPrinterGraphics()) {
            i = 2;
        } else {
            i = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityDisplay()) {
            i3 = 4;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityImages()) {
            i3 = 8;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityAudio()) {
            i3 = 16;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityAnimation()) {
            i3 = 32;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityVideo()) {
            i3 = 64;
        } else {
            i3 = 0;
        }
        byte b2 = (byte) (i3 | i);
        if (this.preferences.getCapabilityPayment()) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        if (this.preferences.getCapabilityDigitalReceipt()) {
            i = 2;
        } else {
            i = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityServiceIssuance()) {
            i3 = 4;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityOtaPosData()) {
            i3 = 8;
        } else {
            i3 = 0;
        }
        byte b3 = (byte) (i3 | i);
        if (this.preferences.getCapabilityOnlinePin()) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        if (this.preferences.getCapabilityCdPin()) {
            i = 2;
        } else {
            i = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilitySignature()) {
            i3 = 4;
        } else {
            i3 = 0;
        }
        i3 |= i;
        if (!this.preferences.getCapabilityNone()) {
            i2 = 0;
        }
        i = i3 | i2;
        if (this.preferences.getCapabilityDeviceGenCode()) {
            i3 = 16;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilitySpGenCode()) {
            i3 = 32;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityIdCapture()) {
            i3 = 64;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityBiometric()) {
            i3 = -128;
        } else {
            i3 = 0;
        }
        byte b4 = (byte) (i3 | i);
        if (this.preferences.getCapabilityVasOnly()) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        if (this.preferences.getCapabilityPaymentOnly()) {
            i = 2;
        } else {
            i = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityVasAndPayment()) {
            i3 = 3;
        } else {
            i3 = 0;
        }
        i |= i3;
        if (this.preferences.getCapabilityVasOverPayment()) {
            i3 = 4;
        } else {
            i3 = 0;
        }
        byte b5 = (byte) (i3 | i);
        return NdefRecords.compose(SmartTap2Values.POS_CAPABILITIES_NDEF_TYPE, new byte[]{b, b2, b3, b4, b5}, s);
    }

    private NdefRecord createServiceStatusFromServiceObject(NdefRecord ndefRecord, short s) throws FormatException {
        return createServiceUsageStatus(((NdefRecord) NdefRecords.extractRecords(ndefRecord, s).get(new ByteArrayWrapper(SmartTap2Values.getServiceIdNdefType(s))).iterator().next()).getPayload(), Usage.SUCCESS, s);
    }

    private NdefRecord createServiceUsageStatus(byte[] bArr, Usage usage, short s) {
        return createServiceUsageAndUpdateStatus(bArr, usage, Operation.UNKNOWN, null, s);
    }

    private NdefRecord createServiceUsageAndUpdateStatus(byte[] bArr, Usage usage, Operation operation, byte[] bArr2, short s) {
        return new ServiceStatus(bArr, usage, this.context.getString(R.string.service_status_title), this.context.getString(R.string.service_status_description), operation, bArr2).toNdef(NO_ADDED_NDEFS, NO_REMOVED_NDEFS, s);
    }

    private List<NdefRecordWithSize> getServiceNdefsWithSize(List<NdefRecord> list, short s) throws FormatException {
        NdefRecord createServiceStatusFromServiceObject;
        List<NdefRecordWithSize> arrayList = new ArrayList();
        if (this.preferences.getPushBackStatus()) {
            for (NdefRecord createServiceStatusFromServiceObject2 : list) {
                if (s == (short) 0) {
                    createServiceStatusFromServiceObject2 = createServiceStatusFromServiceObject(createServiceStatusFromServiceObject2, s);
                    arrayList.add(new NdefRecordWithSize(createServiceStatusFromServiceObject2, SmartTap2Values.SERVICE_STATUS_V0_NDEF_HEADER_SIZE + createServiceStatusFromServiceObject2.getPayload().length));
                } else {
                    Iterator it = NdefRecords.extractRecords(createServiceStatusFromServiceObject2, s).entries().iterator();
                    while (it.hasNext()) {
                        Entry entry = (Entry) it.next();
                        if (!(Arrays.equals(((ByteArrayWrapper) entry.getKey()).array(), SmartTap2Values.ISSUER_NDEF_TYPE) || Arrays.equals(((ByteArrayWrapper) entry.getKey()).array(), SmartTap2Values.CUSTOMER_NDEF_TYPE))) {
                            createServiceStatusFromServiceObject2 = createServiceStatusFromServiceObject((NdefRecord) entry.getValue(), s);
                            arrayList.add(new NdefRecordWithSize(createServiceStatusFromServiceObject2, SmartTap2Values.SERVICE_RESPONSE_V1_NDEF_HEADER_SIZE + createServiceStatusFromServiceObject2.getPayload().length));
                        }
                    }
                }
            }
        }
        ImmutableMultimap.Builder builder = ImmutableMultimap.builder();
        if (this.preferences.getPushBackManuallySpecifiedStatus()) {
            for (String put : this.preferences.getUsageUnspecifiedIds()) {
                builder.put(put, Usage.UNSPECIFIED);
            }
            for (String put2 : this.preferences.getUsageSuccessIds()) {
                builder.put(put2, Usage.SUCCESS);
            }
            for (String put22 : this.preferences.getUsageInvalidFormatIds()) {
                builder.put(put22, Usage.INVALID_FORMAT);
            }
            for (String put222 : this.preferences.getUsageInvalidValueIds()) {
                builder.put(put222, Usage.INVALID_VALUE);
            }
        }
        ImmutableMultimap build = builder.build();
        ImmutableTable.Builder builder2 = ImmutableTable.builder();
        if (this.preferences.getPushBackManuallySpecifiedUpdate()) {
            for (String put2222 : this.preferences.getUpdateNoOpIds()) {
                builder2.put(put2222, Operation.NO_OP, new byte[0]);
            }
            for (String put22222 : this.preferences.getUpdateRemoveObjectIds()) {
                builder2.put(put22222, Operation.REMOVE_SERVICE, new byte[0]);
            }
            for (String put222222 : this.preferences.getUpdateSetBalanceIds()) {
                builder2.put(put222222, Operation.SET_BALANCE, new byte[]{(byte) 4, (byte) -18});
            }
            for (String put2222222 : this.preferences.getUpdateAddBalanceIds()) {
                builder2.put(put2222222, Operation.ADD_BALANCE, new byte[]{(byte) 35});
            }
            for (String put22222222 : this.preferences.getUpdateSubtractBalanceIds()) {
                builder2.put(put22222222, Operation.SUBTRACT_BALANCE, new byte[]{(byte) 17});
            }
            for (String put222222222 : this.preferences.getUpdateFreeIds()) {
                builder2.put(put222222222, Operation.FREE, new byte[]{(byte) 85, (byte) 119});
            }
        }
        ImmutableTable build2 = builder2.build();
        Iterator it2 = ImmutableSet.builder().addAll(build.keySet()).addAll(build2.rowKeySet()).build().iterator();
        while (it2.hasNext()) {
            Object obj = (String) it2.next();
            Iterator it3 = build.get(obj).iterator();
            Iterator it4 = build2.row(obj).keySet().iterator();
            if (this.preferences.getConsolidateStatusRecords()) {
                while (it3.hasNext() && it4.hasNext()) {
                    Operation operation = (Operation) it4.next();
                    addUsageAndUpdateRecordForObjectIdHex(obj, (Usage) it3.next(), operation, (byte[]) build2.get(obj, operation), s, arrayList);
                }
            }
            while (it3.hasNext()) {
                addUsageAndUpdateRecordForObjectIdHex(obj, (Usage) it3.next(), Operation.UNKNOWN, null, s, arrayList);
            }
            while (it4.hasNext()) {
                operation = (Operation) it4.next();
                addUsageAndUpdateRecordForObjectIdHex(obj, Usage.UNKNOWN, operation, (byte[]) build2.get(obj, operation), s, arrayList);
            }
        }
        if (this.preferences.getPushBackUnspecifiedService()) {
            createServiceStatusFromServiceObject2 = composeNewService(this.context, ServiceType.UNSPECIFIED, R.string.unspecified_title, R.string.unspecified_uri, s);
            arrayList.add(new NdefRecordWithSize(createServiceStatusFromServiceObject2, SmartTap2Values.NEW_SERVICE_V0_NDEF_HEADER_SIZE + createServiceStatusFromServiceObject2.getPayload().length));
        }
        if (this.preferences.getPushBackValuable()) {
            createServiceStatusFromServiceObject2 = composeNewService(this.context, ServiceType.VALUABLE, R.string.valuable_title, R.string.valuable_uri, s);
            arrayList.add(new NdefRecordWithSize(createServiceStatusFromServiceObject2, SmartTap2Values.NEW_SERVICE_V0_NDEF_HEADER_SIZE + createServiceStatusFromServiceObject2.getPayload().length));
        }
        if (this.preferences.getPushBackReceipt()) {
            createServiceStatusFromServiceObject2 = composeNewService(this.context, ServiceType.RECEIPT, R.string.receipt_title, R.string.receipt_uri, s);
            arrayList.add(new NdefRecordWithSize(createServiceStatusFromServiceObject2, SmartTap2Values.NEW_SERVICE_V0_NDEF_HEADER_SIZE + createServiceStatusFromServiceObject2.getPayload().length));
        }
        if (this.preferences.getPushBackSurvey()) {
            createServiceStatusFromServiceObject2 = composeNewService(this.context, ServiceType.SURVEY, R.string.survey_title, R.string.survey_uri, s);
            arrayList.add(new NdefRecordWithSize(createServiceStatusFromServiceObject2, SmartTap2Values.NEW_SERVICE_V0_NDEF_HEADER_SIZE + createServiceStatusFromServiceObject2.getPayload().length));
        }
        if (this.preferences.getPushBackGoods()) {
            createServiceStatusFromServiceObject2 = composeNewService(this.context, ServiceType.GOODS, R.string.goods_title, R.string.goods_uri, s);
            arrayList.add(new NdefRecordWithSize(createServiceStatusFromServiceObject2, SmartTap2Values.NEW_SERVICE_V0_NDEF_HEADER_SIZE + createServiceStatusFromServiceObject2.getPayload().length));
        }
        if (this.preferences.getPushBackSignup()) {
            createServiceStatusFromServiceObject2 = composeNewService(this.context, ServiceType.SIGNUP, R.string.signup_title, R.string.signup_uri, s);
            arrayList.add(new NdefRecordWithSize(createServiceStatusFromServiceObject2, SmartTap2Values.NEW_SERVICE_V0_NDEF_HEADER_SIZE + createServiceStatusFromServiceObject2.getPayload().length));
        }
        return arrayList;
    }

    private void addUsageAndUpdateRecordForObjectIdHex(String str, Usage usage, Operation operation, byte[] bArr, short s, Collection<NdefRecordWithSize> collection) {
        try {
            NdefRecord createServiceUsageAndUpdateStatus = createServiceUsageAndUpdateStatus(Hex.decode(str), usage, operation, bArr, s);
            collection.add(new NdefRecordWithSize(createServiceUsageAndUpdateStatus, SmartTap2Values.SERVICE_RESPONSE_V1_NDEF_HEADER_SIZE + createServiceUsageAndUpdateStatus.getPayload().length));
        } catch (IllegalArgumentException e) {
            LOG.w("Unable to decode object ID %s, skipping", str);
        }
    }

    private int getInitialPushBackSize(NdefRecord ndefRecord, NdefRecord ndefRecord2, short s) {
        int i;
        if (s == (short) 0) {
            i = SmartTap2Values.PUSH_SERVICE_V0_NDEF_HEADER_SIZE;
        } else {
            i = SmartTap2Values.PUSH_SERVICE_V1_NDEF_HEADER_SIZE;
        }
        int i2 = s == (short) 0 ? SmartTap2Values.SESSION_V0_NDEF_HEADER_SIZE : SmartTap2Values.SESSION_V1_NDEF_HEADER_SIZE;
        int i3 = 0;
        if (ndefRecord2 != null) {
            if (s == (short) 0) {
                i3 = SmartTap2Values.POS_CAPABILITIES_V0_NDEF_HEADER_SIZE;
            } else {
                i3 = SmartTap2Values.POS_CAPABILITIES_V1_NDEF_HEADER_SIZE;
            }
            i3 += ndefRecord2.getPayload().length;
        }
        return (i3 + ((i2 + ((i + SmartTapValues.PUSH_DATA_PREFIX.length) + 2)) + ndefRecord.getPayload().length)) + 1;
    }

    private static NdefRecord composeNewService(Context context, ServiceType serviceType, int i, int i2, short s) {
        return new NewService(serviceType, context.getString(i), Uri.parse(context.getString(i2))).toNdef(NO_ADDED_NDEFS, NO_REMOVED_NDEFS, s);
    }

    private final byte[] extractHandsetPublicKeyBytes(NfcMessage nfcMessage, short s) throws FormatException {
        Iterator it = NdefRecords.extractRecords(((ParsedNdefNfc) nfcMessage.getParsedNfc()).getRecord(), s).get(new ByteArrayWrapper(SmartTap2Values.HANDSET_EPHEMERAL_PUBLIC_KEY_NDEF_TYPE)).iterator();
        if (it.hasNext()) {
            return ((NdefRecord) it.next()).getPayload();
        }
        return null;
    }

    private List<NdefRecord> handleV0DataRequest(NfcMessage nfcMessage, Optional<SmartTap2Decryptor> optional, short s) throws FormatException {
        List<NdefRecord> asList = extractServiceRecords(nfcMessage, optional, s).asList();
        if (optional.isPresent()) {
            displayDecryptedServiceRecords(s, asList);
        }
        return asList;
    }

    private final ImmutableCollection<NdefRecord> extractServiceRecords(NfcMessage nfcMessage, Optional<SmartTap2Decryptor> optional, short s) throws FormatException {
        if (!(nfcMessage.getParsedNfc() instanceof ParsedNdefNfc)) {
            return ImmutableList.of();
        }
        NdefRecord record = ((ParsedNdefNfc) nfcMessage.getParsedNfc()).getRecord();
        if (record == null) {
            throw new FormatException("No NDEF record in NFC response.");
        }
        ImmutableMultimap extractRecords = NdefRecords.extractRecords(record, s);
        Iterable iterable = extractRecords.get(new ByteArrayWrapper(SmartTap2Values.getServiceValueNdefType(s)));
        if (!optional.isPresent()) {
            return iterable;
        }
        SmartTap2Decryptor smartTap2Decryptor = (SmartTap2Decryptor) optional.get();
        ImmutableCollection immutableCollection = extractRecords.get(new ByteArrayWrapper(SmartTap2Values.ENCRYPTED_SERVICE_VALUE_NDEF_TYPE));
        Builder builder = ImmutableList.builder();
        builder.addAll(iterable);
        Iterator it = immutableCollection.iterator();
        while (it.hasNext()) {
            try {
                builder.addAll(Arrays.asList(new NdefMessage(smartTap2Decryptor.decryptMessage(((NdefRecord) it.next()).getPayload())).getRecords()));
            } catch (Throwable e) {
                LOG.d(e, "Error decrypting payload.", new Object[0]);
            }
        }
        return builder.build();
    }

    private static byte[] extractGetDataBytes(NfcMessage nfcMessage) throws FormatException {
        StatusWord statusWord = nfcMessage.getStatusWord();
        if (nfcMessage.getValue().length <= 2 || (statusWord.getCode() != Code.SUCCESS && statusWord.getCode() != Code.SUCCESS_MORE_PAYLOAD)) {
            return new byte[0];
        }
        return Arrays.copyOf(nfcMessage.getValue(), nfcMessage.getValue().length - 2);
    }

    private static List<NdefRecord> getServiceRecordsFromGetDataBytes(byte[] bArr, short s, SmartTap2Decryptor smartTap2Decryptor, Decompressor decompressor) throws FormatException {
        if (bArr.length == 0) {
            return ImmutableList.of();
        }
        ImmutableMultimap extractRecords = NdefRecords.extractRecords(new NdefRecord(bArr), s);
        List<NdefRecord> arrayList = new ArrayList();
        Iterator it = extractRecords.get(new ByteArrayWrapper(SmartTap2Values.RECORD_BUNDLE_NDEF_TYPE)).iterator();
        while (it.hasNext()) {
            int i;
            NdefRecord ndefRecord = (NdefRecord) it.next();
            byte b = NdefRecords.getByte(ndefRecord, 0);
            int i2 = (b & 1) != 0 ? 1 : 0;
            if ((b & 2) != 0) {
                i = 1;
            } else {
                i = 0;
            }
            byte[] allBytes = NdefRecords.getAllBytes(ndefRecord, 1);
            if (i2 != 0) {
                try {
                    allBytes = smartTap2Decryptor.decryptMessage(allBytes);
                } catch (Throwable e) {
                    LOG.d(e, "Error decrypting payload.", new Object[0]);
                }
            }
            if (i != 0) {
                try {
                    allBytes = decompressor.decompress(allBytes);
                } catch (Throwable e2) {
                    LOG.d(e2, "Error decompressing payload.", new Object[0]);
                }
            }
            arrayList.addAll(Arrays.asList(new NdefMessage(allBytes).getRecords()));
        }
        return arrayList;
    }

    private void displayDecryptedServiceRecords(short s, List<NdefRecord> list) {
        displayServiceRecords(this.context.getString(R.string.decrypted_service_records), s, list);
    }

    private void displayConstructedServiceRecords(short s, List<NdefRecord> list) {
        displayServiceRecords(this.context.getString(R.string.constructed_service_records), s, list);
    }

    private void displayServiceRecords(String str, short s, List<NdefRecord> list) {
        this.localBroadcastManager.sendBroadcast(new Intent(NfcMessage.BROADCAST_NFC_MESSAGE).putExtras(new NfcMessage(new byte[0], new ParsedRecordsNfc(str, s, list)).toBundle()));
    }

    private static boolean dataRemaining(NfcMessage nfcMessage) {
        return Code.SUCCESS_MORE_PAYLOAD.equals(nfcMessage.getStatusWord().getCode());
    }

    public String toString() {
        String valueOf = String.valueOf(SmartTapAid.V2_0);
        return new StringBuilder(String.valueOf(valueOf).length() + 17).append("SmartTap ").append(valueOf).append(" Handler").toString();
    }

    private boolean supportSkippingSelectForProprietaryData(SmartTap2ProprietaryData smartTap2ProprietaryData, boolean z) {
        if (!smartTap2ProprietaryData.supportsSkippingSelect()) {
            return false;
        }
        if (smartTap2ProprietaryData.minVersion().isPresent() && smartTap2ProprietaryData.maxVersion().isPresent() && (z || smartTap2ProprietaryData.mobileDeviceNonce().isPresent())) {
            return true;
        }
        LOG.d("Client reports supporting skipping select, but not all of the necessary data fields were set.", new Object[0]);
        return false;
    }

    private NfcMessage transceiveSelectWithRetries(String str, short s) throws IOException {
        NfcMessage transceiveSelect;
        int retryCount = this.preferences.getRetryCount();
        while (true) {
            transceiveSelect = this.transceiver.transceiveSelect(Aid.SMART_TAP_AID_V2_0.getSelectCommand(), str, new SelectSmartTap2Parser(this.context), s);
            if (!transceiveSelect.getStatusWord().isRetriableFailure()) {
                break;
            }
            int i = retryCount - 1;
            if (retryCount <= 0) {
                break;
            }
            retryCount = i;
        }
        return transceiveSelect;
    }

    private CommandResponse transceiveCommandWithRetries(CommandParameters commandParameters, byte b) throws IOException {
        byte[] asBytes;
        NfcMessage transceive;
        int retryCount = this.preferences.getRetryCount();
        while (true) {
            asBytes = commandParameters.asBytes(b);
            transceive = this.transceiver.transceive(asBytes, this.ndefParser, commandParameters.version());
            b = (byte) (b + 2);
            if (!transceive.getStatusWord().isRetriableFailure()) {
                break;
            }
            int i = retryCount - 1;
            if (retryCount <= 0) {
                break;
            }
            retryCount = i;
        }
        return CommandResponse.create(asBytes, transceive, b);
    }
}
