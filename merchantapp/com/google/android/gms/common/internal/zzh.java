package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.base.R;
import com.google.android.gms.common.GooglePlayServicesUtil;

public final class zzh {
    private static final SimpleArrayMap<String, String> JQ = new SimpleArrayMap();

    public static String zzb(Context context, int i, String str) {
        Resources resources = context.getResources();
        switch (i) {
            case 1:
                if (com.google.android.gms.common.util.zzh.zzb(resources)) {
                    return resources.getString(R.string.common_google_play_services_install_text_tablet, new Object[]{str});
                }
                return resources.getString(R.string.common_google_play_services_install_text_phone, new Object[]{str});
            case 2:
                return resources.getString(R.string.common_google_play_services_update_text, new Object[]{str});
            case 3:
                return resources.getString(R.string.common_google_play_services_enable_text, new Object[]{str});
            case 5:
                return zze(context, "common_google_play_services_invalid_account_text", str);
            case 7:
                return zze(context, "common_google_play_services_network_error_text", str);
            case 9:
                return resources.getString(R.string.common_google_play_services_unsupported_text, new Object[]{str});
            case 16:
                return zze(context, "common_google_play_services_api_unavailable_text", str);
            case 17:
                return zze(context, "common_google_play_services_sign_in_failed_text", str);
            case 18:
                return resources.getString(R.string.common_google_play_services_updating_text, new Object[]{str});
            case 20:
                return zze(context, "common_google_play_services_restricted_profile_text", str);
            case 42:
                return resources.getString(R.string.common_google_play_services_wear_update_text);
            default:
                return resources.getString(R.string.common_google_play_services_unknown_issue, new Object[]{str});
        }
    }

    public static String zzc(Context context, int i, String str) {
        return i == 6 ? zze(context, "common_google_play_services_resolution_required_text", str) : zzb(context, i, str);
    }

    public static String zze(Context context, int i) {
        Resources resources = context.getResources();
        switch (i) {
            case 1:
                return resources.getString(R.string.common_google_play_services_install_title);
            case 2:
            case 42:
                return resources.getString(R.string.common_google_play_services_update_title);
            case 3:
                return resources.getString(R.string.common_google_play_services_enable_title);
            case 4:
            case 6:
                return null;
            case 5:
                Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                return zzo(context, "common_google_play_services_invalid_account_title");
            case 7:
                Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                return zzo(context, "common_google_play_services_network_error_title");
            case 8:
                Log.e("GoogleApiAvailability", "Internal error occurred. Please see logs for detailed information");
                return null;
            case 9:
                Log.e("GoogleApiAvailability", "Google Play services is invalid. Cannot recover.");
                return resources.getString(R.string.common_google_play_services_unsupported_title);
            case 10:
                Log.e("GoogleApiAvailability", "Developer error occurred. Please see logs for detailed information");
                return null;
            case 11:
                Log.e("GoogleApiAvailability", "The application is not licensed to the user.");
                return null;
            case 16:
                Log.e("GoogleApiAvailability", "One of the API components you attempted to connect to is not available.");
                return null;
            case 17:
                Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                return zzo(context, "common_google_play_services_sign_in_failed_title");
            case 18:
                return resources.getString(R.string.common_google_play_services_updating_title);
            case 20:
                Log.e("GoogleApiAvailability", "The current user profile is restricted and could not use authenticated features.");
                return zzo(context, "common_google_play_services_restricted_profile_title");
            default:
                Log.e("GoogleApiAvailability", "Unexpected error code " + i);
                return null;
        }
    }

    private static String zze(Context context, String str, String str2) {
        Resources resources = context.getResources();
        String zzo = zzo(context, str);
        if (zzo == null) {
            zzo = resources.getString(R.string.common_google_play_services_unknown_issue);
        }
        return String.format(resources.getConfiguration().locale, zzo, new Object[]{str2});
    }

    public static String zzf(Context context, int i) {
        return i == 6 ? zzo(context, "common_google_play_services_resolution_required_title") : zze(context, i);
    }

    public static String zzg(Context context, int i) {
        Resources resources = context.getResources();
        switch (i) {
            case 1:
                return resources.getString(R.string.common_google_play_services_install_button);
            case 2:
                return resources.getString(R.string.common_google_play_services_update_button);
            case 3:
                return resources.getString(R.string.common_google_play_services_enable_button);
            default:
                return resources.getString(17039370);
        }
    }

    private static String zzo(Context context, String str) {
        synchronized (JQ) {
            String str2 = (String) JQ.get(str);
            if (str2 != null) {
                return str2;
            }
            Resources remoteResource = GooglePlayServicesUtil.getRemoteResource(context);
            if (remoteResource == null) {
                return null;
            }
            int identifier = remoteResource.getIdentifier(str, "string", "com.google.android.gms");
            if (identifier == 0) {
                String str3 = "GoogleApiAvailability";
                String str4 = "Missing resource: ";
                str2 = String.valueOf(str);
                Log.w(str3, str2.length() != 0 ? str4.concat(str2) : new String(str4));
                return null;
            }
            Object string = remoteResource.getString(identifier);
            if (TextUtils.isEmpty(string)) {
                str3 = "GoogleApiAvailability";
                str4 = "Got empty resource: ";
                str2 = String.valueOf(str);
                Log.w(str3, str2.length() != 0 ? str4.concat(str2) : new String(str4));
                return null;
            }
            JQ.put(str, string);
            return string;
        }
    }
}
