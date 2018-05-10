package com.google.android.libraries.commerce.hce.terminal.settings;

import android.content.Context;

public enum SmartTapAid {
    UNKNOWN(0),
    V1_3_A(R.string.title_smarttap_v1_3a),
    V1_3_B(R.string.title_smarttap_v1_3b),
    V2_0(R.string.title_smarttap_v2_0);
    
    private final int resource;

    private SmartTapAid(int i) {
        this.resource = i;
    }

    public String toString(Context context) {
        return context.getString(this.resource);
    }
}
