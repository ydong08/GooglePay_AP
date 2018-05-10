package com.google.android.libraries.commerce.hce.util;

import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Preconditions;

public class ByteArrays {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();

    public static byte[] updatePayloadLength(byte[] bArr, int i) {
        int i2 = 0;
        Preconditions.checkNotNull(bArr);
        Preconditions.checkArgument(i >= 0);
        if (bArr.length == i) {
            return bArr;
        }
        byte[] bArr2 = new byte[i];
        int i3;
        if (bArr.length > i) {
            for (i3 = 0; i3 < bArr.length - i; i3++) {
                if (bArr[i3] != (byte) 0) {
                    LOG.e("Payload does not fit inside of length %s. Payload: %s", Integer.valueOf(i), Hex.encodeUpper(bArr));
                    return null;
                }
            }
            while (i2 < i) {
                bArr2[i2] = bArr[(bArr.length - i) + i2];
                i2++;
            }
        } else {
            for (i3 = 0; i3 < i; i3++) {
                if (i3 < i - bArr.length) {
                    bArr2[i3] = (byte) 0;
                } else {
                    bArr2[i3] = bArr[(i3 - i) + bArr.length];
                }
            }
        }
        return bArr2;
    }
}
