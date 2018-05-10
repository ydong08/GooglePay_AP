package android.support.v4.view.accessibility;

import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.ArrayList;
import java.util.List;

class AccessibilityNodeProviderCompatJellyBean {

    interface AccessibilityNodeInfoBridge {
        final /* synthetic */ AccessibilityNodeProviderJellyBeanImpl this$0;
        final /* synthetic */ AccessibilityNodeProviderCompat val$compat;

        AccessibilityNodeInfoBridge(AccessibilityNodeProviderJellyBeanImpl accessibilityNodeProviderJellyBeanImpl, AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            this.this$0 = accessibilityNodeProviderJellyBeanImpl;
            this.val$compat = accessibilityNodeProviderCompat;
        }

        boolean performAction(int i, int i2, Bundle bundle) {
            return this.val$compat.performAction(i, i2, bundle);
        }

        List<Object> findAccessibilityNodeInfosByText(String str, int i) {
            List findAccessibilityNodeInfosByText = this.val$compat.findAccessibilityNodeInfosByText(str, i);
            if (findAccessibilityNodeInfosByText == null) {
                return null;
            }
            List<Object> arrayList = new ArrayList();
            int size = findAccessibilityNodeInfosByText.size();
            for (int i2 = 0; i2 < size; i2++) {
                arrayList.add(((AccessibilityNodeInfoCompat) findAccessibilityNodeInfosByText.get(i2)).getInfo());
            }
            return arrayList;
        }

        Object createAccessibilityNodeInfo(int i) {
            AccessibilityNodeInfoCompat createAccessibilityNodeInfo = this.val$compat.createAccessibilityNodeInfo(i);
            if (createAccessibilityNodeInfo == null) {
                return null;
            }
            return createAccessibilityNodeInfo.getInfo();
        }
    }

    AccessibilityNodeProviderCompatJellyBean() {
    }

    public static Object newAccessibilityNodeProviderBridge(final AccessibilityNodeInfoBridge accessibilityNodeInfoBridge) {
        return new AccessibilityNodeProvider() {
            public AccessibilityNodeInfo createAccessibilityNodeInfo(int i) {
                return (AccessibilityNodeInfo) accessibilityNodeInfoBridge.createAccessibilityNodeInfo(i);
            }

            public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String str, int i) {
                return accessibilityNodeInfoBridge.findAccessibilityNodeInfosByText(str, i);
            }

            public boolean performAction(int i, int i2, Bundle bundle) {
                return accessibilityNodeInfoBridge.performAction(i, i2, bundle);
            }
        };
    }
}
