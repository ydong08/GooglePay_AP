package android.support.v4.app;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class BundleCompatGingerbread {
    private static Method sGetIBinderMethod;
    private static boolean sGetIBinderMethodFetched;

    BundleCompatGingerbread() {
    }

    public static IBinder getBinder(Bundle bundle, String str) {
        if (!sGetIBinderMethodFetched) {
            try {
                sGetIBinderMethod = Bundle.class.getMethod("getIBinder", new Class[]{String.class});
                sGetIBinderMethod.setAccessible(true);
            } catch (Throwable e) {
                Throwable e2;
                Log.i("BundleCompatGingerbread", "Failed to retrieve getIBinder method", e2);
            }
            sGetIBinderMethodFetched = true;
        }
        if (sGetIBinderMethod != null) {
            try {
                return (IBinder) sGetIBinderMethod.invoke(bundle, new Object[]{str});
            } catch (InvocationTargetException e3) {
                e2 = e3;
            } catch (IllegalAccessException e4) {
                e2 = e4;
            } catch (IllegalArgumentException e5) {
                e2 = e5;
            }
        }
        return null;
        Log.i("BundleCompatGingerbread", "Failed to invoke getIBinder via reflection", e2);
        sGetIBinderMethod = null;
        return null;
    }
}
