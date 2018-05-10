package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import android.annotation.TargetApi;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import java.text.SimpleDateFormat;
import javax.inject.Inject;

@TargetApi(19)
public class GetSmartTapDataCommand implements SmartTapCommand {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyyMMddkkmmss");
    private final SmartTapCallback smartTapCallback;

    @Inject
    public GetSmartTapDataCommand(SmartTapCallback smartTapCallback) {
        this.smartTapCallback = smartTapCallback;
    }
}
