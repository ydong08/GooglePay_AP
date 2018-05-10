package android.support.v4.view.accessibility;

import android.os.Build.VERSION;

public class AccessibilityRecordCompat {
    private static final AccessibilityRecordImpl IMPL;
    private final Object mRecord;

    interface AccessibilityRecordImpl {
        AccessibilityRecordImpl() {
        }

        void setFromIndex(Object obj, int i) {
        }

        void setItemCount(Object obj, int i) {
        }

        void setMaxScrollX(Object obj, int i) {
        }

        void setMaxScrollY(Object obj, int i) {
        }

        void setScrollX(Object obj, int i) {
        }

        void setScrollY(Object obj, int i) {
        }

        void setScrollable(Object obj, boolean z) {
        }

        void setToIndex(Object obj, int i) {
        }
    }

    static class AccessibilityRecordIcsImpl extends AccessibilityRecordImpl {
        AccessibilityRecordIcsImpl() {
        }

        public void setFromIndex(Object obj, int i) {
            AccessibilityRecordCompatIcs.setFromIndex(obj, i);
        }

        public void setItemCount(Object obj, int i) {
            AccessibilityRecordCompatIcs.setItemCount(obj, i);
        }

        public void setScrollX(Object obj, int i) {
            AccessibilityRecordCompatIcs.setScrollX(obj, i);
        }

        public void setScrollY(Object obj, int i) {
            AccessibilityRecordCompatIcs.setScrollY(obj, i);
        }

        public void setScrollable(Object obj, boolean z) {
            AccessibilityRecordCompatIcs.setScrollable(obj, z);
        }

        public void setToIndex(Object obj, int i) {
            AccessibilityRecordCompatIcs.setToIndex(obj, i);
        }
    }

    static class AccessibilityRecordIcsMr1Impl extends AccessibilityRecordIcsImpl {
        AccessibilityRecordIcsMr1Impl() {
        }

        public void setMaxScrollX(Object obj, int i) {
            AccessibilityRecordCompatIcsMr1.setMaxScrollX(obj, i);
        }

        public void setMaxScrollY(Object obj, int i) {
            AccessibilityRecordCompatIcsMr1.setMaxScrollY(obj, i);
        }

        AccessibilityRecordIcsMr1Impl(byte b) {
            this();
        }
    }

    static {
        if (VERSION.SDK_INT >= 16) {
            IMPL = new AccessibilityRecordIcsMr1Impl((byte) 0);
        } else if (VERSION.SDK_INT >= 15) {
            IMPL = new AccessibilityRecordIcsMr1Impl();
        } else if (VERSION.SDK_INT >= 14) {
            IMPL = new AccessibilityRecordIcsImpl();
        } else {
            IMPL = new AccessibilityRecordImpl();
        }
    }

    @Deprecated
    public AccessibilityRecordCompat(Object obj) {
        this.mRecord = obj;
    }

    public void setScrollable(boolean z) {
        IMPL.setScrollable(this.mRecord, z);
    }

    public void setItemCount(int i) {
        IMPL.setItemCount(this.mRecord, i);
    }

    public void setFromIndex(int i) {
        IMPL.setFromIndex(this.mRecord, i);
    }

    public void setToIndex(int i) {
        IMPL.setToIndex(this.mRecord, i);
    }

    public void setScrollX(int i) {
        IMPL.setScrollX(this.mRecord, i);
    }

    public void setScrollY(int i) {
        IMPL.setScrollY(this.mRecord, i);
    }

    public void setMaxScrollX(int i) {
        IMPL.setMaxScrollX(this.mRecord, i);
    }

    public void setMaxScrollY(int i) {
        IMPL.setMaxScrollY(this.mRecord, i);
    }

    public int hashCode() {
        return this.mRecord == null ? 0 : this.mRecord.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AccessibilityRecordCompat accessibilityRecordCompat = (AccessibilityRecordCompat) obj;
        if (this.mRecord == null) {
            if (accessibilityRecordCompat.mRecord != null) {
                return false;
            }
            return true;
        } else if (this.mRecord.equals(accessibilityRecordCompat.mRecord)) {
            return true;
        } else {
            return false;
        }
    }
}
