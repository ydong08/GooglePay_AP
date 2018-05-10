package com.google.android.libraries.commerce.hce.terminal.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.widget.Toast;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Preferences {
    private static final ImmutableSet<String> BOOLEAN_KEYS = ImmutableSet.of("preference_use_ose", "preference_smarttap_v1_3a", "preference_smarttap_v1_3b", "preference_smarttap_v2_0", "preference_request_payment", "preference_stop_payment_if_smart_tap_fails", "preference_encryption_supported", "preference_presigned_auth", "preference_send_service_request", "preference_request_all_valuables", "preference_request_all_valuables_except_ppse", "preference_request_customer_info", "preference_request_loyalty_programs", "preference_request_offers", "preference_request_offers", "preference_request_gift_cards", "preference_request_private_label_cards", "preference_consolidate_status", "preference_push_valuable_usage", "preference_push_manual_usage", "preference_push_unspecified_service", "preference_push_new_valuable", "preference_push_receipt", "preference_push_survey", "preference_push_goods", "preference_push_signup", "preference_capabilities_standalone", "preference_capabilities_semi_integrated", "preference_capabilities_unattended", "preference_capabilities_online", "preference_capabilities_offline", "preference_capabilities_mmp", "preference_capabilities_zlib", "preference_capabilities_printer", "preference_capabilities_printer_graphics", "preference_capabilities_display", "preference_capabilities_images", "preference_capabilities_audio", "preference_capabilities_animation", "preference_capabilities_video", "preference_capabilities_payment", "preference_capabilities_digital_receipt", "preference_capabilities_service_issuance", "preference_capabilities_ota_pos_data", "preference_capabilities_online_pin", "preference_capabilities_cd_pin", "preference_capabilities_signature", "preference_capabilities_none", "preference_capabilities_device_gen_code", "preference_capabilities_sp_gen_code", "preference_capabilities_id_capture", "preference_capabilities_biometric", "preference_capabilities_vas_only", "preference_capabilities_payment_only", "preference_capabilities_vas_and_payment", "preference_capabilities_vas_over_payment", "preference_allow_skipping_smart_tap_2_select");
    private static final ImmutableMap<String, ByteArrayProvider> BYTE_ARRAY_PROVIDERS = ImmutableMap.of("preference_location_id", LOCATION_ID_PROVIDER, "preference_terminal_id", TERMINAL_ID_PROVIDER, "preference_terminal_nonce", TERMINAL_NONCE_PROVIDER, "preference_terminal_long_term_private_key", LONG_TERM_PRIVATE_KEY_PROVIDER, "preference_terminal_ephemeral_private_key", EPHEMERAL_PRIVATE_KEY_PROVIDER);
    private static final Set<String> DEFAULT_STRING_SET_VALUE = ImmutableSet.of();
    static final ByteArrayProvider EPHEMERAL_PRIVATE_KEY_PROVIDER = new EncryptionKeyProvider(R.string.title_ephemeral_private_key, 32);
    private static final Map<String, Integer> INT_RESOURCES = ImmutableMap.of("preference_smarttap_version", Integer.valueOf(R.string.title_smarttap_version), "preference_merchant_id", Integer.valueOf(R.string.title_merchant_id), "preference_merchant_category", Integer.valueOf(R.string.title_merchant_category), "preference_long_term_key_version", Integer.valueOf(R.string.title_long_term_key_version), "preference_retry_count", Integer.valueOf(R.string.title_retry_count));
    static final ByteArrayProvider LOCATION_ID_PROVIDER = new LocationIdProvider();
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    static final ByteArrayProvider LONG_TERM_PRIVATE_KEY_PROVIDER = new EncryptionKeyProvider(R.string.title_long_term_private_key, 32);
    private static final ImmutableSet<String> STRING_KEYS = ImmutableSet.of("preference_merchant_name");
    private static final ImmutableSet<String> STRING_SET_KEYS = ImmutableSet.of("preference_unspecified_usage_ids", "preference_success_ids", "preference_invalid_format_ids", "preference_invalid_value_ids", "preference_no_op_update_ids", "preference_remove_object_update_ids", "preference_set_balance_update_ids", "preference_add_balance_update_ids", "preference_subtract_balance_update_ids", "preference_free_update_ids");
    static final ByteArrayProvider TERMINAL_ID_PROVIDER = new TerminalIdProvider();
    static final ByteArrayProvider TERMINAL_NONCE_PROVIDER = new TerminalNonceProvider();
    private final Map<String, Boolean> booleanValues = Maps.newHashMap();
    private final Map<String, byte[]> byteArrayValues = Maps.newHashMap();
    private final Context context;
    private final Map<String, Integer> intValues = Maps.newHashMap();
    private final SharedPreferences prefs;
    private final Map<String, Set<String>> stringSetValues = Maps.newHashMap();
    private final Map<String, String> stringValues = Maps.newHashMap();

    interface ByteArrayProvider {
        byte[] getByteArray(Context context, String str);
    }

    static class EncryptionKeyProvider implements ByteArrayProvider {
        private static final byte[] FALLBACK = Hex.decode("DEADBEEFDEADBEEFDEADBEEFDEADBEEFDEADBEEFDEADBEEFDEADBEEFDEADBEEFDE");
        private int keyLength;
        private int keyNameResource;

        public EncryptionKeyProvider(int i, int i2) {
            this.keyNameResource = i;
            this.keyLength = i2;
        }

        public byte[] getByteArray(Context context, String str) {
            return Preferences.getValue(context, str, R.string.default_encryption_key, this.keyNameResource, this.keyLength, this.keyLength, FALLBACK);
        }
    }

    static class LocationIdProvider implements ByteArrayProvider {
        private static final byte[] FALLBACK = Hex.decode("0102030405060708");

        private LocationIdProvider() {
        }

        public byte[] getByteArray(Context context, String str) {
            return Preferences.getValue(context, str, R.string.default_location_id, R.string.title_location_id, 0, 32, FALLBACK);
        }
    }

    static class TerminalIdProvider implements ByteArrayProvider {
        private static final byte[] FALLBACK = Hex.decode("0204060801030507");

        private TerminalIdProvider() {
        }

        public byte[] getByteArray(Context context, String str) {
            return Preferences.getValue(context, str, R.string.default_terminal_id, R.string.title_terminal_id, 0, 8, FALLBACK);
        }
    }

    static class TerminalNonceProvider implements ByteArrayProvider {
        private static final byte[] FALLBACK = Hex.decode("1111222233334444555566667777888811112222333344445555666677778888");

        private TerminalNonceProvider() {
        }

        public byte[] getByteArray(Context context, String str) {
            return Preferences.getValue(context, str, R.string.default_terminal_nonce, R.string.title_terminal_nonce, 32, 32, FALLBACK);
        }
    }

    public Preferences(Context context) {
        this.context = context;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        retrieveValues();
    }

    public boolean getUseOse() {
        return getBooleanValue("preference_use_ose");
    }

    public Collection<SmartTapAid> getSmartTapAids() {
        Collection arrayList = new ArrayList();
        if (getBooleanValue("preference_smarttap_v1_3a")) {
            arrayList.add(SmartTapAid.V1_3_A);
        }
        if (getBooleanValue("preference_smarttap_v1_3b")) {
            arrayList.add(SmartTapAid.V1_3_B);
        }
        if (getBooleanValue("preference_smarttap_v2_0")) {
            arrayList.add(SmartTapAid.V2_0);
        }
        return arrayList;
    }

    public short getSmartTapVersion() {
        return (short) getIntValue("preference_smarttap_version");
    }

    public boolean getRequestPayment() {
        return getBooleanValue("preference_request_payment");
    }

    public boolean getStopPaymentIfSmartTapFails() {
        return getBooleanValue("preference_stop_payment_if_smart_tap_fails");
    }

    public int getRetryCount() {
        return getIntValue("preference_retry_count");
    }

    public int getMerchantId() {
        return getIntValue("preference_merchant_id");
    }

    public byte[] getLocationId() {
        return getByteArrayValue("preference_location_id");
    }

    public byte[] getTerminalId() {
        return getByteArrayValue("preference_terminal_id");
    }

    public String getMerchantName() {
        return getStringValue("preference_merchant_name");
    }

    public int getMerchantCategory() {
        return getIntValue("preference_merchant_category");
    }

    public boolean getEncryptionSupprted() {
        return getBooleanValue("preference_encryption_supported");
    }

    public boolean usePresignedAuth() {
        return getBooleanValue("preference_presigned_auth");
    }

    public byte[] getTerminalNonce() {
        return getByteArrayValue("preference_terminal_nonce");
    }

    public byte[] getEphemeralPrivateKey() {
        return getByteArrayValue("preference_terminal_ephemeral_private_key");
    }

    public byte[] getLongTermPrivateKey() {
        return getByteArrayValue("preference_terminal_long_term_private_key");
    }

    public int getLongTermKeyVersion() {
        return getIntValue("preference_long_term_key_version");
    }

    public boolean getSendServiceRequest() {
        return getBooleanValue("preference_send_service_request");
    }

    public boolean getAllValuables() {
        return getBooleanValue("preference_request_all_valuables");
    }

    public boolean getAllValuablesExceptPpse() {
        return getBooleanValue("preference_request_all_valuables_except_ppse");
    }

    public boolean getCustomerInfo() {
        return getBooleanValue("preference_request_customer_info");
    }

    public boolean getRequestLoyalty() {
        return getBooleanValue("preference_request_loyalty_programs");
    }

    public boolean getRequestOffers() {
        return getBooleanValue("preference_request_offers");
    }

    public boolean getRequestGiftCards() {
        return getBooleanValue("preference_request_gift_cards");
    }

    public boolean getRequestPlc() {
        return getBooleanValue("preference_request_private_label_cards");
    }

    public boolean getConsolidateStatusRecords() {
        return getBooleanValue("preference_consolidate_status");
    }

    public boolean getPushBackStatus() {
        return getBooleanValue("preference_push_valuable_usage");
    }

    public boolean getPushBackManuallySpecifiedStatus() {
        return getBooleanValue("preference_push_manual_usage");
    }

    public boolean getPushBackManuallySpecifiedUpdate() {
        return getBooleanValue("preference_push_manual_update");
    }

    public boolean getPushBackUnspecifiedService() {
        return getBooleanValue("preference_push_unspecified_service");
    }

    public boolean getPushBackValuable() {
        return getBooleanValue("preference_push_new_valuable");
    }

    public boolean getPushBackReceipt() {
        return getBooleanValue("preference_push_receipt");
    }

    public boolean getPushBackSurvey() {
        return getBooleanValue("preference_push_survey");
    }

    public boolean getPushBackGoods() {
        return getBooleanValue("preference_push_goods");
    }

    public boolean getPushBackSignup() {
        return getBooleanValue("preference_push_signup");
    }

    public Set<String> getUsageUnspecifiedIds() {
        return getStringSetValue("preference_unspecified_usage_ids");
    }

    public Set<String> getUsageSuccessIds() {
        return getStringSetValue("preference_success_ids");
    }

    public Set<String> getUsageInvalidFormatIds() {
        return getStringSetValue("preference_invalid_format_ids");
    }

    public Set<String> getUsageInvalidValueIds() {
        return getStringSetValue("preference_invalid_value_ids");
    }

    public Set<String> getUpdateNoOpIds() {
        return getStringSetValue("preference_no_op_update_ids");
    }

    public Set<String> getUpdateRemoveObjectIds() {
        return getStringSetValue("preference_remove_object_update_ids");
    }

    public Set<String> getUpdateSetBalanceIds() {
        return getStringSetValue("preference_set_balance_update_ids");
    }

    public Set<String> getUpdateAddBalanceIds() {
        return getStringSetValue("preference_add_balance_update_ids");
    }

    public Set<String> getUpdateSubtractBalanceIds() {
        return getStringSetValue("preference_subtract_balance_update_ids");
    }

    public Set<String> getUpdateFreeIds() {
        return getStringSetValue("preference_free_update_ids");
    }

    public boolean getCapabilityStandalone() {
        return getBooleanValue("preference_capabilities_standalone");
    }

    public boolean getCapabilitySemiIntegrated() {
        return getBooleanValue("preference_capabilities_semi_integrated");
    }

    public boolean getCapabilityUnattended() {
        return getBooleanValue("preference_capabilities_unattended");
    }

    public boolean getCapabilityOnline() {
        return getBooleanValue("preference_capabilities_online");
    }

    public boolean getCapabilityOffline() {
        return getBooleanValue("preference_capabilities_offline");
    }

    public boolean getCapabilityMmp() {
        return getBooleanValue("preference_capabilities_mmp");
    }

    public boolean getCapabilityZlib() {
        return getBooleanValue("preference_capabilities_zlib");
    }

    public boolean getCapabilityPrinter() {
        return getBooleanValue("preference_capabilities_printer");
    }

    public boolean getCapabilityPrinterGraphics() {
        return getBooleanValue("preference_capabilities_printer_graphics");
    }

    public boolean getCapabilityDisplay() {
        return getBooleanValue("preference_capabilities_display");
    }

    public boolean getCapabilityImages() {
        return getBooleanValue("preference_capabilities_images");
    }

    public boolean getCapabilityAudio() {
        return getBooleanValue("preference_capabilities_audio");
    }

    public boolean getCapabilityAnimation() {
        return getBooleanValue("preference_capabilities_animation");
    }

    public boolean getCapabilityVideo() {
        return getBooleanValue("preference_capabilities_video");
    }

    public boolean getCapabilityPayment() {
        return getBooleanValue("preference_capabilities_payment");
    }

    public boolean getCapabilityDigitalReceipt() {
        return getBooleanValue("preference_capabilities_digital_receipt");
    }

    public boolean getCapabilityServiceIssuance() {
        return getBooleanValue("preference_capabilities_service_issuance");
    }

    public boolean getCapabilityOtaPosData() {
        return getBooleanValue("preference_capabilities_ota_pos_data");
    }

    public boolean getCapabilityOnlinePin() {
        return getBooleanValue("preference_capabilities_online_pin");
    }

    public boolean getCapabilityCdPin() {
        return getBooleanValue("preference_capabilities_cd_pin");
    }

    public boolean getCapabilitySignature() {
        return getBooleanValue("preference_capabilities_signature");
    }

    public boolean getCapabilityNone() {
        return getBooleanValue("preference_capabilities_none");
    }

    public boolean getCapabilityDeviceGenCode() {
        return getBooleanValue("preference_capabilities_device_gen_code");
    }

    public boolean getCapabilitySpGenCode() {
        return getBooleanValue("preference_capabilities_sp_gen_code");
    }

    public boolean getCapabilityIdCapture() {
        return getBooleanValue("preference_capabilities_id_capture");
    }

    public boolean getCapabilityBiometric() {
        return getBooleanValue("preference_capabilities_biometric");
    }

    public boolean getCapabilityVasOnly() {
        return getBooleanValue("preference_capabilities_vas_only");
    }

    public boolean getCapabilityPaymentOnly() {
        return getBooleanValue("preference_capabilities_payment_only");
    }

    public boolean getCapabilityVasAndPayment() {
        return getBooleanValue("preference_capabilities_vas_and_payment");
    }

    public boolean getCapabilityVasOverPayment() {
        return getBooleanValue("preference_capabilities_vas_over_payment");
    }

    public boolean getAllowSkippingSmartTap2Select() {
        return getBooleanValue("preference_allow_skipping_smart_tap_2_select");
    }

    private String getStringValue(String str) {
        if (this.stringValues.containsKey(str)) {
            return (String) this.stringValues.get(str);
        }
        LOG.w("String preferences value map does not contain key %s. Returning default value null.", str);
        return null;
    }

    private byte[] getByteArrayValue(String str) {
        if (this.byteArrayValues.containsKey(str)) {
            return (byte[]) this.byteArrayValues.get(str);
        }
        LOG.w("Byte array preferences value map does not contain key %s. Returning default value null.", str);
        return null;
    }

    private boolean getBooleanValue(String str) {
        if (this.booleanValues.containsKey(str)) {
            return ((Boolean) this.booleanValues.get(str)).booleanValue();
        }
        LOG.w("Boolean preferences value map does not contain key %s. Returning default value %s.", str, Boolean.valueOf(true));
        return true;
    }

    private int getIntValue(String str) {
        if (this.intValues.containsKey(str)) {
            return ((Integer) this.intValues.get(str)).intValue();
        }
        LOG.w("Integer preferences value map does not contain key %s. Returning default value %s.", str, Integer.valueOf(1));
        return 1;
    }

    private Set<String> getStringSetValue(String str) {
        if (this.stringSetValues.containsKey(str)) {
            return (Set) this.stringSetValues.get(str);
        }
        LOG.w("String Set preferences value map does not contain key %s. Returning default value %s.", str, DEFAULT_STRING_SET_VALUE);
        return DEFAULT_STRING_SET_VALUE;
    }

    private void retrieveValues() {
        Iterator it = STRING_KEYS.iterator();
        while (it.hasNext()) {
            cacheStringValue((String) it.next());
        }
        it = STRING_SET_KEYS.iterator();
        while (it.hasNext()) {
            cacheStringSetValue((String) it.next());
        }
        it = BOOLEAN_KEYS.iterator();
        while (it.hasNext()) {
            cacheBooleanValue((String) it.next());
        }
        for (Entry entry : INT_RESOURCES.entrySet()) {
            Entry entry2;
            cacheIntValue((String) entry2.getKey(), ((Integer) entry2.getValue()).intValue());
        }
        Iterator it2 = BYTE_ARRAY_PROVIDERS.entrySet().iterator();
        while (it2.hasNext()) {
            entry2 = (Entry) it2.next();
            cacheByteArrayValue((String) entry2.getKey(), (ByteArrayProvider) entry2.getValue());
        }
    }

    public void registerChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.prefs.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public void unregisterChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.prefs.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public void retrieveUpdatedValue(String str) {
        if (BYTE_ARRAY_PROVIDERS.containsKey(str)) {
            cacheByteArrayValue(str, (ByteArrayProvider) BYTE_ARRAY_PROVIDERS.get(str));
        } else if (STRING_KEYS.contains(str)) {
            cacheStringValue(str);
        } else if (BOOLEAN_KEYS.contains(str)) {
            cacheBooleanValue(str);
        } else if (INT_RESOURCES.containsKey(str)) {
            cacheIntValue(str, ((Integer) INT_RESOURCES.get(str)).intValue());
        } else if (STRING_SET_KEYS.contains(str)) {
            cacheStringSetValue(str);
        } else {
            LOG.w("Unknown preference key %s. Ignoring update preferences request.", str);
        }
    }

    private void cacheStringValue(String str) {
        checkForPref(str);
        this.stringValues.put(str, this.prefs.getString(str, ""));
    }

    private void cacheByteArrayValue(String str, ByteArrayProvider byteArrayProvider) {
        checkForPref(str);
        byte[] byteArray = byteArrayProvider.getByteArray(this.context, this.prefs.getString(str, ""));
        setValue(str, byteArray);
        this.byteArrayValues.put(str, byteArray);
    }

    private void cacheBooleanValue(String str) {
        checkForPref(str);
        this.booleanValues.put(str, Boolean.valueOf(this.prefs.getBoolean(str, true)));
    }

    private void cacheIntValue(String str, int i) {
        checkForPref(str);
        String string = this.context.getString(i);
        String string2 = this.prefs.getString(str, "");
        if (Strings.isNullOrEmpty(string2)) {
            LOG.w("%s is null or empty. Reverting to %s", string, Integer.valueOf(1));
            toast(R.string.missing_value, string);
            setValue(str, 1);
            this.intValues.put(str, Integer.valueOf(1));
            return;
        }
        try {
            this.intValues.put(str, Integer.valueOf(Integer.parseInt(string2)));
        } catch (NumberFormatException e) {
            LOG.w("%s could not be parsed to an integer. Reverting to %s", string, Integer.valueOf(1));
            toast(R.string.invalid_int, string);
            setValue(str, 1);
            this.intValues.put(str, Integer.valueOf(1));
        }
    }

    private void cacheStringSetValue(String str) {
        checkForPref(str);
        this.stringSetValues.put(str, this.prefs.getStringSet(str, DEFAULT_STRING_SET_VALUE));
    }

    private void checkForPref(String str) {
        if (!this.prefs.contains(str)) {
            LOG.e("Preferences do not contain a value for key %s", str);
        }
    }

    private static byte[] getValue(Context context, String str, int i, int i2, int i3, int i4, byte[] bArr) {
        byte[] decode;
        Preconditions.checkArgument(i3 <= i4);
        String string = context.getString(i2);
        if (str == null) {
            str = "";
        }
        if (Strings.isNullOrEmpty(str) && i3 > 0) {
            str = context.getString(i);
            LOG.w("%s is Null or empty. Reverting to default value %s.", string, str);
            toast(context, R.string.missing_value, string);
        }
        try {
            decode = Hex.decode(str);
        } catch (Throwable e) {
            String encodeUpper = Hex.encodeUpper(bArr);
            LOG.w(e, "%s %s could not be decoded. Reverting to value %s", string, str, encodeUpper);
            toast(context, R.string.invalid_hex, string);
            decode = bArr;
        }
        if (decode.length > i4) {
            decode = Arrays.copyOf(decode, i4);
            encodeUpper = Hex.encodeUpper(bArr);
            LOG.w("%s max length is %s bytes. %s is %s bytes long. Truncating to %s", string, Integer.valueOf(i4), str, Integer.valueOf(decode.length), encodeUpper);
            toast(context, R.string.value_too_long, string, Integer.valueOf(i4), Integer.valueOf(decode.length));
        }
        if (decode.length >= i3) {
            return decode;
        }
        decode = Arrays.copyOf(decode, i3);
        encodeUpper = Hex.encodeUpper(bArr);
        LOG.w("%s min length is %s bytes. %s is %s bytes long. Padding to %s", string, Integer.valueOf(i4), str, Integer.valueOf(decode.length), encodeUpper);
        toast(context, R.string.value_too_short, string, Integer.valueOf(i3), Integer.valueOf(decode.length));
        return decode;
    }

    private void setValue(String str, int i) {
        setValue(str, Integer.valueOf(i).toString());
    }

    private void setValue(String str, byte[] bArr) {
        setValue(str, Hex.encodeUpper(bArr));
    }

    private void setValue(String str, String str2) {
        Editor edit = this.prefs.edit();
        edit.putString(str, str2);
        edit.commit();
    }

    private void toast(int i, Object... objArr) {
        toast(this.context, i, objArr);
    }

    private static void toast(Context context, int i, Object... objArr) {
        Toast.makeText(context, context.getString(i, objArr), 0).show();
    }
}
