package com.google.commerce.tapandpay.merchantapp.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.android.apps.common.inject.annotation.ApplicationContext;
import javax.inject.Inject;

public class Settings {
    private final SharedPreferences sharedPreferences;

    @Inject
    public Settings(@ApplicationContext Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getSendLoyalty() {
        return this.sharedPreferences.getBoolean("send_loyalty", true);
    }

    public void setSendLoyalty(boolean z) {
        this.sharedPreferences.edit().putBoolean("send_loyalty", z).commit();
    }

    public boolean getSendOffer() {
        return this.sharedPreferences.getBoolean("send_offer", true);
    }

    public void setSendOffer(boolean z) {
        this.sharedPreferences.edit().putBoolean("send_offer", z).commit();
    }

    public boolean getSendGiftCard() {
        return this.sharedPreferences.getBoolean("send_gift_card", true);
    }

    public void setSendGiftCard(boolean z) {
        this.sharedPreferences.edit().putBoolean("send_gift_card", z).commit();
    }

    public boolean getSendPlc() {
        return this.sharedPreferences.getBoolean("send_plc", true);
    }

    public void setSendPlc(boolean z) {
        this.sharedPreferences.edit().putBoolean("send_plc", z).commit();
    }

    public boolean getAutoAdvance() {
        return this.sharedPreferences.getBoolean("auto_advance", false);
    }

    public void setAutoAdvance(boolean z) {
        this.sharedPreferences.edit().putBoolean("auto_advance", z).commit();
    }
}
