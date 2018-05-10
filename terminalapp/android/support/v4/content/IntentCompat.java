package android.support.v4.content;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build.VERSION;

public final class IntentCompat {
    private static final IntentCompatImpl IMPL;

    interface IntentCompatImpl {
        IntentCompatImpl() {
        }

        Intent makeMainActivity(ComponentName componentName) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setComponent(componentName);
            intent.addCategory("android.intent.category.LAUNCHER");
            return intent;
        }
    }

    static class IntentCompatImplHC extends IntentCompatImpl {
        IntentCompatImplHC() {
        }

        public Intent makeMainActivity(ComponentName componentName) {
            return IntentCompatHoneycomb.makeMainActivity(componentName);
        }

        IntentCompatImplHC(byte b) {
            this();
        }
    }

    static {
        int i = VERSION.SDK_INT;
        if (i >= 15) {
            IMPL = new IntentCompatImplHC((byte) 0);
        } else if (i >= 11) {
            IMPL = new IntentCompatImplHC();
        } else {
            IMPL = new IntentCompatImpl();
        }
    }

    private IntentCompat() {
    }

    public static Intent makeMainActivity(ComponentName componentName) {
        return IMPL.makeMainActivity(componentName);
    }
}
