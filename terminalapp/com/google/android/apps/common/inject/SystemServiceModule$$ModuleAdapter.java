package com.google.android.apps.common.inject;

import android.accounts.AccountManager;
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
import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.Linker;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import java.util.Set;
import javax.inject.Provider;

public final class SystemServiceModule$$ModuleAdapter extends ModuleAdapter<SystemServiceModule> {
    private static final Class<?>[] INCLUDES = new Class[]{ApplicationModule.class};
    private static final String[] INJECTS = new String[0];
    private static final Class<?>[] STATIC_INJECTIONS = new Class[0];

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideAccessibilityManagerProvidesAdapter extends ProvidesBinding<AccessibilityManager> implements Provider<AccessibilityManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideAccessibilityManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.view.accessibility.AccessibilityManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideAccessibilityManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public AccessibilityManager get() {
            return this.module.provideAccessibilityManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideAccountManagerProvidesAdapter extends ProvidesBinding<AccountManager> implements Provider<AccountManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideAccountManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.accounts.AccountManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideAccountManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public AccountManager get() {
            return this.module.provideAccountManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideActivityManagerProvidesAdapter extends ProvidesBinding<ActivityManager> implements Provider<ActivityManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideActivityManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.app.ActivityManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideActivityManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public ActivityManager get() {
            return this.module.provideActivityManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideAlarmManagerProvidesAdapter extends ProvidesBinding<AlarmManager> implements Provider<AlarmManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideAlarmManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.app.AlarmManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideAlarmManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public AlarmManager get() {
            return this.module.provideAlarmManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideAudioManagerProvidesAdapter extends ProvidesBinding<AudioManager> implements Provider<AudioManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideAudioManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.media.AudioManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideAudioManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public AudioManager get() {
            return this.module.provideAudioManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideClipboardManagerProvidesAdapter extends ProvidesBinding<ClipboardManager> implements Provider<ClipboardManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideClipboardManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.content.ClipboardManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideClipboardManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public ClipboardManager get() {
            return this.module.provideClipboardManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideConnectivityManagerProvidesAdapter extends ProvidesBinding<ConnectivityManager> implements Provider<ConnectivityManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideConnectivityManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.net.ConnectivityManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideConnectivityManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public ConnectivityManager get() {
            return this.module.provideConnectivityManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideDevicePolicyManagerProvidesAdapter extends ProvidesBinding<DevicePolicyManager> implements Provider<DevicePolicyManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideDevicePolicyManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.app.admin.DevicePolicyManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideDevicePolicyManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public DevicePolicyManager get() {
            return this.module.provideDevicePolicyManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideDropBoxManagerProvidesAdapter extends ProvidesBinding<DropBoxManager> implements Provider<DropBoxManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideDropBoxManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.os.DropBoxManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideDropBoxManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public DropBoxManager get() {
            return this.module.provideDropBoxManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideInputMethodManagerProvidesAdapter extends ProvidesBinding<InputMethodManager> implements Provider<InputMethodManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideInputMethodManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.view.inputmethod.InputMethodManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideInputMethodManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public InputMethodManager get() {
            return this.module.provideInputMethodManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideKeyguardManagerProvidesAdapter extends ProvidesBinding<KeyguardManager> implements Provider<KeyguardManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideKeyguardManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.app.KeyguardManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideKeyguardManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public KeyguardManager get() {
            return this.module.provideKeyguardManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideLocationManagerProvidesAdapter extends ProvidesBinding<LocationManager> implements Provider<LocationManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideLocationManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.location.LocationManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideLocationManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public LocationManager get() {
            return this.module.provideLocationManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideNfcManagerProvidesAdapter extends ProvidesBinding<NfcManager> implements Provider<NfcManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideNfcManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.nfc.NfcManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideNfcManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public NfcManager get() {
            return this.module.provideNfcManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideNotificationManagerProvidesAdapter extends ProvidesBinding<NotificationManager> implements Provider<NotificationManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideNotificationManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.app.NotificationManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideNotificationManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public NotificationManager get() {
            return this.module.provideNotificationManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvidePackageManagerProvidesAdapter extends ProvidesBinding<PackageManager> implements Provider<PackageManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvidePackageManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.content.pm.PackageManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "providePackageManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public PackageManager get() {
            return this.module.providePackageManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvidePowerManagerProvidesAdapter extends ProvidesBinding<PowerManager> implements Provider<PowerManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvidePowerManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.os.PowerManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "providePowerManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public PowerManager get() {
            return this.module.providePowerManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideSearchManagerProvidesAdapter extends ProvidesBinding<SearchManager> implements Provider<SearchManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideSearchManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.app.SearchManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideSearchManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public SearchManager get() {
            return this.module.provideSearchManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideSensorManagerProvidesAdapter extends ProvidesBinding<SensorManager> implements Provider<SensorManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideSensorManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.hardware.SensorManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideSensorManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public SensorManager get() {
            return this.module.provideSensorManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideTelephonyManagerProvidesAdapter extends ProvidesBinding<TelephonyManager> implements Provider<TelephonyManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideTelephonyManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.telephony.TelephonyManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideTelephonyManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public TelephonyManager get() {
            return this.module.provideTelephonyManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideVibratorProvidesAdapter extends ProvidesBinding<Vibrator> implements Provider<Vibrator> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideVibratorProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.os.Vibrator", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideVibrator");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public Vibrator get() {
            return this.module.provideVibrator((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideWallpaperServiceProvidesAdapter extends ProvidesBinding<WallpaperService> implements Provider<WallpaperService> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideWallpaperServiceProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.service.wallpaper.WallpaperService", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideWallpaperService");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public WallpaperService get() {
            return this.module.provideWallpaperService((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideWifiManagerProvidesAdapter extends ProvidesBinding<WifiManager> implements Provider<WifiManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideWifiManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.net.wifi.WifiManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideWifiManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public WifiManager get() {
            return this.module.provideWifiManager((Context) this.context.get());
        }
    }

    /* compiled from: SystemServiceModule$$ModuleAdapter */
    public static final class ProvideWindowManagerProvidesAdapter extends ProvidesBinding<WindowManager> implements Provider<WindowManager> {
        private Binding<Context> context;
        private final SystemServiceModule module;

        public ProvideWindowManagerProvidesAdapter(SystemServiceModule systemServiceModule) {
            super("android.view.WindowManager", false, "com.google.android.apps.common.inject.SystemServiceModule", "provideWindowManager");
            this.module = systemServiceModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("@com.google.android.apps.common.inject.annotation.ApplicationContext()/android.content.Context", SystemServiceModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public WindowManager get() {
            return this.module.provideWindowManager((Context) this.context.get());
        }
    }

    public SystemServiceModule$$ModuleAdapter() {
        super(SystemServiceModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, true, true);
    }

    public SystemServiceModule newModule() {
        return new SystemServiceModule();
    }

    public void getBindings(BindingsGroup bindingsGroup, SystemServiceModule systemServiceModule) {
        bindingsGroup.contributeProvidesBinding("android.view.accessibility.AccessibilityManager", new ProvideAccessibilityManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.accounts.AccountManager", new ProvideAccountManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.app.ActivityManager", new ProvideActivityManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.app.AlarmManager", new ProvideAlarmManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.media.AudioManager", new ProvideAudioManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.content.ClipboardManager", new ProvideClipboardManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.net.ConnectivityManager", new ProvideConnectivityManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.app.admin.DevicePolicyManager", new ProvideDevicePolicyManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.os.DropBoxManager", new ProvideDropBoxManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.view.inputmethod.InputMethodManager", new ProvideInputMethodManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.app.KeyguardManager", new ProvideKeyguardManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.location.LocationManager", new ProvideLocationManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.nfc.NfcManager", new ProvideNfcManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.app.NotificationManager", new ProvideNotificationManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.content.pm.PackageManager", new ProvidePackageManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.os.PowerManager", new ProvidePowerManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.app.SearchManager", new ProvideSearchManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.hardware.SensorManager", new ProvideSensorManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.telephony.TelephonyManager", new ProvideTelephonyManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.os.Vibrator", new ProvideVibratorProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.service.wallpaper.WallpaperService", new ProvideWallpaperServiceProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.net.wifi.WifiManager", new ProvideWifiManagerProvidesAdapter(systemServiceModule));
        bindingsGroup.contributeProvidesBinding("android.view.WindowManager", new ProvideWindowManagerProvidesAdapter(systemServiceModule));
    }
}
