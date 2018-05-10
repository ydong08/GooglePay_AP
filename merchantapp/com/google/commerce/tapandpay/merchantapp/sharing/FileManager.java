package com.google.commerce.tapandpay.merchantapp.sharing;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import com.google.android.apps.common.inject.annotation.ApplicationContext;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import javax.inject.Inject;

@TargetApi(19)
public class FileManager {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final Context context;

    @Inject
    public FileManager(@ApplicationContext Context context) {
        this.context = context;
    }

    public Uri createVideoFile(Uri uri) {
        OutputStream fileOutputStream;
        Throwable th;
        Throwable th2;
        try {
            File createVideoFile = createVideoFile(this.context);
            if (createVideoFile != null) {
                fileOutputStream = new FileOutputStream(createVideoFile);
                th = null;
                try {
                    byte[] bArr = new byte[1024];
                    InputStream openInputStream = this.context.getContentResolver().openInputStream(uri);
                    while (openInputStream != null && openInputStream.read(bArr) > 0) {
                        fileOutputStream.write(bArr, 0, 1024);
                    }
                    Uri uriForFile = FileProvider.getUriForFile(this.context, "com.google.commerce.tapandpay.merchantapp.fileprovider", createVideoFile);
                    if (fileOutputStream == null) {
                        return uriForFile;
                    }
                    if (null != null) {
                        try {
                            fileOutputStream.close();
                            return uriForFile;
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                            return uriForFile;
                        }
                    }
                    fileOutputStream.close();
                    return uriForFile;
                } catch (Throwable th4) {
                    Throwable th5 = th4;
                    th4 = th2;
                    th2 = th5;
                }
            }
        } catch (IOException e) {
            LOG.d("Error occurred while getting file stream", e);
        }
        return null;
        if (fileOutputStream != null) {
            if (th4 != null) {
                try {
                    fileOutputStream.close();
                } catch (Throwable th32) {
                    th4.addSuppressed(th32);
                }
            } else {
                fileOutputStream.close();
            }
        }
        throw th2;
        throw th2;
    }

    public Uri createTextFile(String str, String str2, String str3) {
        OutputStreamWriter outputStreamWriter;
        Throwable th;
        Throwable th2;
        try {
            File createTempFile = createTempFile(this.context, str2, str3);
            if (createTempFile != null) {
                outputStreamWriter = new OutputStreamWriter(new FileOutputStream(createTempFile), StandardCharsets.UTF_8);
                th = null;
                try {
                    outputStreamWriter.append(str);
                    Uri uriForFile = FileProvider.getUriForFile(this.context, "com.google.commerce.tapandpay.merchantapp.fileprovider", createTempFile);
                    if (outputStreamWriter == null) {
                        return uriForFile;
                    }
                    if (null != null) {
                        try {
                            outputStreamWriter.close();
                            return uriForFile;
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                            return uriForFile;
                        }
                    }
                    outputStreamWriter.close();
                    return uriForFile;
                } catch (Throwable th4) {
                    Throwable th5 = th4;
                    th4 = th2;
                    th2 = th5;
                }
            }
        } catch (IOException e) {
            LOG.d("Error occurred while getting file stream", e);
        }
        return null;
        if (outputStreamWriter != null) {
            if (th4 != null) {
                try {
                    outputStreamWriter.close();
                } catch (Throwable th32) {
                    th4.addSuppressed(th32);
                }
            } else {
                outputStreamWriter.close();
            }
        }
        throw th2;
        throw th2;
    }

    private File createVideoFile(Context context) {
        File file = new File(context.getFilesDir(), "videos");
        if (file.exists() || file.mkdir()) {
            return new File(file, "video-" + new Random().nextInt() + ".mp4");
        }
        LOG.d("Could not create video directory", new Object[0]);
        return null;
    }

    private File createTempFile(Context context, String str, String str2) throws IOException {
        int i = 0;
        File file = new File(context.getCacheDir(), "shared_files");
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            int length = listFiles.length;
            while (i < length) {
                File file2 = listFiles[i];
                if (file2.getName().startsWith(str) && file2.getName().endsWith(str2)) {
                    file2.delete();
                }
                i++;
            }
        } else if (!file.mkdir()) {
            LOG.d("Could not create cache directory for sharing", new Object[0]);
            return null;
        }
        return File.createTempFile(str, str2, file);
    }
}
