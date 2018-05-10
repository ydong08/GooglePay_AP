package com.google.android.libraries.commerce.hce.applet.smarttap;

import com.google.android.libraries.commerce.hce.applet.InvalidCommandException;

public class SmartTapException extends InvalidCommandException {
    public SmartTapException(String str) {
        super(str);
    }

    public SmartTapException(String str, Throwable th) {
        super(str, th);
    }
}
