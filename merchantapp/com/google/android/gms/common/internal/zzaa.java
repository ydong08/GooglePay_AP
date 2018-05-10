package com.google.android.gms.common.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class zzaa {

    public static final class zza {
        private final List<String> KH;
        private final Object zzcmj;

        private zza(Object obj) {
            this.zzcmj = zzab.zzae(obj);
            this.KH = new ArrayList();
        }

        public String toString() {
            StringBuilder append = new StringBuilder(100).append(this.zzcmj.getClass().getSimpleName()).append('{');
            int size = this.KH.size();
            for (int i = 0; i < size; i++) {
                append.append((String) this.KH.get(i));
                if (i < size - 1) {
                    append.append(", ");
                }
            }
            return append.append('}').toString();
        }

        public zza zzh(String str, Object obj) {
            List list = this.KH;
            String str2 = (String) zzab.zzae(str);
            String valueOf = String.valueOf(String.valueOf(obj));
            list.add(new StringBuilder((String.valueOf(str2).length() + 1) + String.valueOf(valueOf).length()).append(str2).append("=").append(valueOf).toString());
            return this;
        }
    }

    public static boolean equal(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static int hashCode(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    public static zza zzad(Object obj) {
        return new zza(obj);
    }
}
