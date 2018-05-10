package com.google.commerce.tapandpay.merchantapp.main.cursoradapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.CursorAdapter;

public abstract class ActiveCursorAdapter extends CursorAdapter {
    private final Context context;

    protected abstract String getSharedPrefsKey();

    public ActiveCursorAdapter(Context context) {
        super(context, null, 0);
        this.context = context;
    }

    public void setActiveId(long j) {
        PreferenceManager.getDefaultSharedPreferences(this.context).edit().putLong(getSharedPrefsKey(), j).apply();
        notifyDataSetChanged();
    }

    public long getActiveId() {
        return PreferenceManager.getDefaultSharedPreferences(this.context).getLong(getSharedPrefsKey(), -1);
    }

    public void deleteActiveId() {
        PreferenceManager.getDefaultSharedPreferences(this.context).edit().remove(getSharedPrefsKey()).apply();
    }
}
