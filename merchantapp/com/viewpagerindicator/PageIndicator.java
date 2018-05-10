package com.viewpagerindicator;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public interface PageIndicator extends OnPageChangeListener {
    void setViewPager(ViewPager viewPager);
}
