package com.google.android.apps.common.inject;

import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.admin.DevicePolicyManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.nfc.NfcManager;
import android.os.DropBoxManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.service.wallpaper.WallpaperService;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import com.google.android.apps.common.inject.annotation.ApplicationContext;
import dagger.Module;
import dagger.Provides;

@TargetApi(8)
@Module(includes = {ApplicationModule.class}, library = true)
public class SystemServiceModule {
    @Provides
    public AccessibilityManager provideAccessibilityManager(@ApplicationContext Context context) {
        return (AccessibilityManager) context.getSystemService("accessibility");
    }

    @Provides
    public AccountManager provideAccountManager(@ApplicationContext Context context) {
        return (AccountManager) context.getSystemService("account");
    }

    @Provides
    public ActivityManager provideActivityManager(@ApplicationContext Context context) {
        return (ActivityManager) context.getSystemService("activity");
    }

    @Provides
    public AlarmManager provideAlarmManager(@ApplicationContext Context context) {
        return (AlarmManager) context.getSystemService("alarm");
    }

    @Provides
    public AudioManager provideAudioManager(@ApplicationContext Context context) {
        return (AudioManager) context.getSystemService("audio");
    }

    @Provides
    public ClipboardManager provideClipboardManager(@ApplicationContext Context context) {
        return (ClipboardManager) context.getSystemService("clipboard");
    }

    @Provides
    public ConnectivityManager provideConnectivityManager(@ApplicationContext Context context) {
        return (ConnectivityManager) context.getSystemService("connectivity");
    }

    @Provides
    public DevicePolicyManager provideDevicePolicyManager(@ApplicationContext Context context) {
        return (DevicePolicyManager) context.getSystemService("device_policy");
    }

    @Provides
    public DropBoxManager provideDropBoxManager(@ApplicationContext Context context) {
        return (DropBoxManager) context.getSystemService("dropbox");
    }

    @Provides
    public InputMethodManager provideInputMethodManager(@ApplicationContext Context context) {
        return (InputMethodManager) context.getSystemService("input_method");
    }

    @Provides
    public KeyguardManager provideKeyguardManager(@ApplicationContext Context context) {
        return (KeyguardManager) context.getSystemService("keyguard");
    }

    @Provides
    public LocationManager provideLocationManager(@ApplicationContext Context context) {
        return (LocationManager) context.getSystemService("location");
    }

    @Provides
    public NfcManager provideNfcManager(@ApplicationContext Context context) {
        return (NfcManager) context.getSystemService("nfc");
    }

    @Provides
    public NotificationManager provideNotificationManager(@ApplicationContext Context context) {
        return (NotificationManager) context.getSystemService("notification");
    }

    @Provides
    public PackageManager providePackageManager(@ApplicationContext Context context) {
        return context.getPackageManager();
    }

    @Provides
    public PowerManager providePowerManager(@ApplicationContext Context context) {
        return (PowerManager) context.getSystemService("power");
    }

    @Provides
    public SearchManager provideSearchManager(@ApplicationContext Context context) {
        return (SearchManager) context.getSystemService("search");
    }

    @Provides
    public SensorManager provideSensorManager(@ApplicationContext Context context) {
        return (SensorManager) context.getSystemService("sensor");
    }

    @Provides
    public TelephonyManager provideTelephonyManager(@ApplicationContext Context context) {
        return (TelephonyManager) context.getSystemService("phone");
    }

    @Provides
    public Vibrator provideVibrator(@ApplicationContext Context context) {
        return (Vibrator) context.getSystemService("vibrator");
    }

    @Provides
    public WallpaperService provideWallpaperService(@ApplicationContext Context context) {
        return (WallpaperService) context.getSystemService("wallpaper");
    }

    @Provides
    public WifiManager provideWifiManager(@ApplicationContext Context context) {
        return (WifiManager) context.getSystemService("wifi");
    }

    @Provides
    public WindowManager provideWindowManager(@ApplicationContext Context context) {
        return (WindowManager) context.getSystemService("window");
    }
}
