package android.support.v4.app;

import android.content.Context;
import android.os.Build.VERSION;

public final class AppOpsManagerCompat {
    private static final AppOpsManagerImpl IMPL;

    static class AppOpsManagerImpl {
        AppOpsManagerImpl() {
        }

        public String permissionToOp(String str) {
            return null;
        }

        public int noteProxyOp(Context context, String str, String str2) {
            return 1;
        }
    }

    static class AppOpsManager23 extends AppOpsManagerImpl {
        AppOpsManager23() {
        }

        public String permissionToOp(String str) {
            return AppOpsManagerCompat23.permissionToOp(str);
        }

        public int noteProxyOp(Context context, String str, String str2) {
            return AppOpsManagerCompat23.noteProxyOp(context, str, str2);
        }
    }

    static {
        if (VERSION.SDK_INT >= 23) {
            IMPL = new AppOpsManager23();
        } else {
            IMPL = new AppOpsManagerImpl();
        }
    }

    private AppOpsManagerCompat() {
    }

    public static String permissionToOp(String str) {
        return IMPL.permissionToOp(str);
    }

    public static int noteProxyOp(Context context, String str, String str2) {
        return IMPL.noteProxyOp(context, str, str2);
    }
}
