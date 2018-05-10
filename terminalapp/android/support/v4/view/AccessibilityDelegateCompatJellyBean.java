package android.support.v4.view;

import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

class AccessibilityDelegateCompatJellyBean {

    public interface AccessibilityDelegateBridgeJellyBean {
        final /* synthetic */ AccessibilityDelegateJellyBeanImpl this$0;
        final /* synthetic */ AccessibilityDelegateCompat val$compat;

        AccessibilityDelegateBridgeJellyBean(AccessibilityDelegateJellyBeanImpl accessibilityDelegateJellyBeanImpl, AccessibilityDelegateCompat accessibilityDelegateCompat) {
            this.this$0 = accessibilityDelegateJellyBeanImpl;
            this.val$compat = accessibilityDelegateCompat;
        }

        boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            return this.val$compat.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
        }

        void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            this.val$compat.onInitializeAccessibilityEvent(view, accessibilityEvent);
        }

        void onInitializeAccessibilityNodeInfo(View view, Object obj) {
            this.val$compat.onInitializeAccessibilityNodeInfo(view, new AccessibilityNodeInfoCompat(obj));
        }

        void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            this.val$compat.onPopulateAccessibilityEvent(view, accessibilityEvent);
        }

        boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            return this.val$compat.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }

        void sendAccessibilityEvent(View view, int i) {
            this.val$compat.sendAccessibilityEvent(view, i);
        }

        void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
            this.val$compat.sendAccessibilityEventUnchecked(view, accessibilityEvent);
        }

        Object getAccessibilityNodeProvider(View view) {
            AccessibilityNodeProviderCompat accessibilityNodeProvider = this.val$compat.getAccessibilityNodeProvider(view);
            return accessibilityNodeProvider != null ? accessibilityNodeProvider.getProvider() : null;
        }

        boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            return this.val$compat.performAccessibilityAction(view, i, bundle);
        }
    }

    AccessibilityDelegateCompatJellyBean() {
    }

    public static Object newAccessibilityDelegateBridge(final AccessibilityDelegateBridgeJellyBean accessibilityDelegateBridgeJellyBean) {
        return new AccessibilityDelegate() {
            public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                return accessibilityDelegateBridgeJellyBean.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
            }

            public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                accessibilityDelegateBridgeJellyBean.onInitializeAccessibilityEvent(view, accessibilityEvent);
            }

            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                accessibilityDelegateBridgeJellyBean.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            }

            public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                accessibilityDelegateBridgeJellyBean.onPopulateAccessibilityEvent(view, accessibilityEvent);
            }

            public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
                return accessibilityDelegateBridgeJellyBean.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
            }

            public void sendAccessibilityEvent(View view, int i) {
                accessibilityDelegateBridgeJellyBean.sendAccessibilityEvent(view, i);
            }

            public void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
                accessibilityDelegateBridgeJellyBean.sendAccessibilityEventUnchecked(view, accessibilityEvent);
            }

            public AccessibilityNodeProvider getAccessibilityNodeProvider(View view) {
                return (AccessibilityNodeProvider) accessibilityDelegateBridgeJellyBean.getAccessibilityNodeProvider(view);
            }

            public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                return accessibilityDelegateBridgeJellyBean.performAccessibilityAction(view, i, bundle);
            }
        };
    }

    public static Object getAccessibilityNodeProvider(Object obj, View view) {
        return ((AccessibilityDelegate) obj).getAccessibilityNodeProvider(view);
    }

    public static boolean performAccessibilityAction(Object obj, View view, int i, Bundle bundle) {
        return ((AccessibilityDelegate) obj).performAccessibilityAction(view, i, bundle);
    }
}
