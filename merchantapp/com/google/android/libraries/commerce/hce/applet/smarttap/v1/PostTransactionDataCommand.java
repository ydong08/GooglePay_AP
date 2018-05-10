package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCommand.Response;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;

public class PostTransactionDataCommand implements SmartTapCommand {
    public Response process(byte[] bArr) {
        return new Response(Iso7816StatusWord.INS_NOT_SUPPORTED);
    }

    public Response getMoreData() {
        throw new IllegalStateException("More data is never indicated");
    }
}
