package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import com.google.android.libraries.commerce.hce.applet.smarttap.SmartTapException;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import java.util.Objects;

public interface SmartTapCommand {

    public static class Response {
        private final ResponseApdu responseApdu;

        public Response(ResponseApdu responseApdu) {
            this.responseApdu = responseApdu;
        }

        public final ResponseApdu getResponseApdu() {
            return this.responseApdu;
        }

        public final boolean hasMoreData() {
            return this.responseApdu.getStatusWord().equals(Iso7816StatusWord.WARNING_MORE_DATA);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Response)) {
                return false;
            }
            return Objects.equals(this.responseApdu, ((Response) obj).responseApdu);
        }

        public int hashCode() {
            return Objects.hashCode(this.responseApdu);
        }

        public Response(Iso7816StatusWord iso7816StatusWord) {
            this(ResponseApdu.fromStatusWord(iso7816StatusWord));
        }

        public Response(ResponseApdu responseApdu, byte b) {
            this(responseApdu);
        }
    }

    Response getMoreData();

    Response process(byte[] bArr) throws SmartTapException;
}
