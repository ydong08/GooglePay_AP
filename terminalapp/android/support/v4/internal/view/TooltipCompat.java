package android.support.v4.internal.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

public class TooltipCompat {
    public static void setTooltip(View view, final CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            view.setOnLongClickListener(null);
            view.setLongClickable(false);
            return;
        }
        view.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                TooltipCompat.showTooltipToast(view, charSequence);
                return true;
            }
        });
    }

    private static void showTooltipToast(View view, CharSequence charSequence) {
        int[] iArr = new int[2];
        Rect rect = new Rect();
        view.getLocationOnScreen(iArr);
        view.getWindowVisibleDisplayFrame(rect);
        Context context = view.getContext();
        int width = view.getWidth();
        int height = view.getHeight();
        int i = iArr[1] + (height / 2);
        width = (width / 2) + iArr[0];
        if (ViewCompat.getLayoutDirection(view) == 0) {
            width = context.getResources().getDisplayMetrics().widthPixels - width;
        }
        Toast makeText = Toast.makeText(context, charSequence, 0);
        if (i < rect.height()) {
            makeText.setGravity(8388661, width, (iArr[1] + height) - rect.top);
        } else {
            makeText.setGravity(81, 0, height);
        }
        makeText.show();
    }
}
