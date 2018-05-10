package com.google.commerce.tapandpay.merchantapp.common;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.widget.Toast;

public class Uris {
    private Uris() {
    }

    public static void showFilePicker(Activity activity, String str, int i) {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.setType(str);
        intent.addCategory("android.intent.category.OPENABLE");
        try {
            activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.select_file)), i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, activity.getString(R.string.no_file_manager_installed), 0).show();
        }
    }
}
