package android.support.v7.widget;

import android.support.v7.view.menu.ActionMenuItem;
import android.view.View;
import android.view.View.OnClickListener;

class ToolbarWidgetWrapper$1 implements OnClickListener {
    final ActionMenuItem mNavItem = new ActionMenuItem(this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNL8RRFDHH62SIND5I6EPBKATP62S3GCLP3M___0.mToolbar.getContext(), 0, 16908332, 0, 0, this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNL8RRFDHH62SIND5I6EPBKATP62S3GCLP3M___0.mTitle);
    final /* synthetic */ DecorToolbar this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNL8RRFDHH62SIND5I6EPBKATP62S3GCLP3M___0;

    ToolbarWidgetWrapper$1(DecorToolbar decorToolbar) {
        this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNL8RRFDHH62SIND5I6EPBKATP62S3GCLP3M___0 = decorToolbar;
    }

    public void onClick(View view) {
        if (this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNL8RRFDHH62SIND5I6EPBKATP62S3GCLP3M___0.mWindowCallback != null && this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNL8RRFDHH62SIND5I6EPBKATP62S3GCLP3M___0.mMenuPrepared) {
            this.this$0$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNL8RRFDHH62SIND5I6EPBKATP62S3GCLP3M___0.mWindowCallback.onMenuItemSelected(0, this.mNavItem);
        }
    }
}
