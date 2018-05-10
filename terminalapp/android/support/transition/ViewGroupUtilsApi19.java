package android.support.transition;

import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.Method;

class ViewGroupUtilsApi19 extends ViewGroupUtilsApi18 {
    private static Method sSuppressLayoutMethod;
    private static boolean sSuppressLayoutMethodFetched;

    ViewGroupUtilsApi19() {
    }

    public void suppressLayout(ViewGroup viewGroup, boolean z) {
        fetchSuppressLayoutMethod();
        if (sSuppressLayoutMethod != null) {
            try {
                sSuppressLayoutMethod.invoke(viewGroup, new Object[]{Boolean.valueOf(z)});
            } catch (Throwable e) {
                Log.i("ViewUtilsApi19", "Failed to invoke suppressLayout method", e);
            } catch (Throwable e2) {
                Log.i("ViewUtilsApi19", "Error invoking suppressLayout method", e2);
            }
        }
    }

    private void fetchSuppressLayoutMethod() {
        if (!sSuppressLayoutMethodFetched) {
            try {
                sSuppressLayoutMethod = ViewGroup.class.getDeclaredMethod("suppressLayout", new Class[]{Boolean.TYPE});
                sSuppressLayoutMethod.setAccessible(true);
            } catch (Throwable e) {
                Log.i("ViewUtilsApi19", "Failed to retrieve suppressLayout method", e);
            }
            sSuppressLayoutMethodFetched = true;
        }
    }
}
