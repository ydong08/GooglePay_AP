package com.google.android.libraries.commerce.hce.applet;

public class InvalidCommandException extends Exception {
    public InvalidCommandException(String str) {
        super(str);
    }

    public InvalidCommandException(String str, Throwable th) {
        super(str, th);
    }
}
