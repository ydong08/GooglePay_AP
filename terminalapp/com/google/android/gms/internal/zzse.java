package com.google.android.gms.internal;

import android.os.Binder;

public abstract class zzse<T> {
    private static zza Hh = null;
    private static int Hi = 0;
    private static String READ_PERMISSION = "com.google.android.providers.gsf.permission.READ_GSERVICES";
    private static final Object zzank = new Object();
    protected final String zzayh;
    protected final T zzayi;
    private T zzcze = null;

    class AnonymousClass2 extends zzse<Long> {
        AnonymousClass2(String str, Long l) {
            super(str, l);
        }

        protected /* synthetic */ Object zzge(String str) {
            return zzgg(str);
        }

        protected Long zzgg(String str) {
            return zzse.Hh.getLong(this.zzayh, (Long) this.zzayi);
        }
    }

    class AnonymousClass3 extends zzse<Integer> {
        AnonymousClass3(String str, Integer num) {
            super(str, num);
        }

        protected /* synthetic */ Object zzge(String str) {
            return zzgh(str);
        }

        protected Integer zzgh(String str) {
            return zzse.Hh.zzb(this.zzayh, (Integer) this.zzayi);
        }
    }

    class AnonymousClass5 extends zzse<String> {
        AnonymousClass5(String str, String str2) {
            super(str, str2);
        }

        protected /* synthetic */ Object zzge(String str) {
            return zzgj(str);
        }

        protected String zzgj(String str) {
            return zzse.Hh.getString(this.zzayh, (String) this.zzayi);
        }
    }

    interface zza {
        Long getLong(String str, Long l);

        String getString(String str, String str2);

        Integer zzb(String str, Integer num);
    }

    protected zzse(String str, T t) {
        this.zzayh = str;
        this.zzayi = t;
    }

    public static zzse<Integer> zza(String str, Integer num) {
        return new AnonymousClass3(str, num);
    }

    public static zzse<Long> zza(String str, Long l) {
        return new AnonymousClass2(str, l);
    }

    public static zzse<String> zzai(String str, String str2) {
        return new AnonymousClass5(str, str2);
    }

    public final T get() {
        long clearCallingIdentity;
        if (this.zzcze != null) {
            return this.zzcze;
        }
        try {
            return zzge(this.zzayh);
        } catch (SecurityException e) {
            clearCallingIdentity = Binder.clearCallingIdentity();
            T zzge = zzge(this.zzayh);
            return zzge;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    protected abstract T zzge(String str);
}
