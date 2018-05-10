package com.google.commerce.tapandpay.merchantapp.testcaseview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.apps.common.inject.InjectedApplication;
import com.google.commerce.tapandpay.merchantapp.sharing.FileManager;
import com.google.commerce.tapandpay.merchantapp.sharing.IntentLoadingTask;
import javax.inject.Inject;

public class DownloadVideoTask extends IntentLoadingTask {
    @Inject
    FileManager fileManager;
    private final Uri videoUri;

    public DownloadVideoTask(Context context, Uri uri) {
        super(context);
        this.videoUri = uri;
        ((InjectedApplication) context.getApplicationContext()).inject(this);
    }

    protected Intent doInBackground(Void... voidArr) {
        Intent intent = new Intent("com.google.commerce.tapandpay.merchantapp.VIDEO_DOWNLOAD");
        Parcelable createVideoFile = this.fileManager.createVideoFile(this.videoUri);
        if (createVideoFile != null) {
            intent.putExtra("download_video_uri", createVideoFile);
        }
        return intent;
    }

    protected void onPostExecute(Intent intent) {
        super.onPostExecute(null);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }
}
