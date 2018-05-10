package android.support.v4.content.res;

import android.content.res.Resources;
import android.os.Build.VERSION;

public final class ConfigurationHelper {
    private static final ConfigurationHelperImpl IMPL;

    interface ConfigurationHelperImpl {
        ConfigurationHelperImpl() {
        }

        int getScreenHeightDp(Resources resources) {
            return ConfigurationHelperGingerbread.getScreenHeightDp(resources);
        }

        int getScreenWidthDp(Resources resources) {
            return ConfigurationHelperGingerbread.getScreenWidthDp(resources);
        }

        int getSmallestScreenWidthDp(Resources resources) {
            return ConfigurationHelperGingerbread.getSmallestScreenWidthDp(resources);
        }
    }

    static class HoneycombMr2Impl extends ConfigurationHelperImpl {
        HoneycombMr2Impl() {
        }

        public int getScreenHeightDp(Resources resources) {
            return ConfigurationHelperHoneycombMr2.getScreenHeightDp(resources);
        }

        public int getScreenWidthDp(Resources resources) {
            return ConfigurationHelperHoneycombMr2.getScreenWidthDp(resources);
        }

        public int getSmallestScreenWidthDp(Resources resources) {
            return ConfigurationHelperHoneycombMr2.getSmallestScreenWidthDp(resources);
        }

        HoneycombMr2Impl(byte b) {
            this();
        }
    }

    static {
        int i = VERSION.SDK_INT;
        if (i >= 17) {
            IMPL = new HoneycombMr2Impl((byte) 0);
        } else if (i >= 13) {
            IMPL = new HoneycombMr2Impl();
        } else {
            IMPL = new ConfigurationHelperImpl();
        }
    }

    private ConfigurationHelper() {
    }

    public static int getScreenHeightDp(Resources resources) {
        return IMPL.getScreenHeightDp(resources);
    }

    public static int getScreenWidthDp(Resources resources) {
        return IMPL.getScreenWidthDp(resources);
    }

    public static int getSmallestScreenWidthDp(Resources resources) {
        return IMPL.getSmallestScreenWidthDp(resources);
    }
}
