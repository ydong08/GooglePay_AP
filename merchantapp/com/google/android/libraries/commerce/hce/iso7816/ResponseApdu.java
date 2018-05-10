package com.google.android.libraries.commerce.hce.iso7816;

import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ResponseApdu {
    private final long cmdTimeMillis;
    private final byte[] rdata;
    private final StatusWord sw;

    ResponseApdu(byte[] bArr, StatusWord statusWord, long j) {
        this.rdata = bArr;
        this.sw = statusWord;
        this.cmdTimeMillis = j;
    }

    public static ResponseApdu fromResponse(byte[] bArr) {
        return fromResponse(bArr, -1, TimeUnit.MILLISECONDS);
    }

    public static ResponseApdu fromDataAndStatusWord(byte[] bArr, StatusWord statusWord) {
        return fromResponse(bArr, statusWord, -1, TimeUnit.MILLISECONDS);
    }

    public static ResponseApdu fromStatusWord(StatusWord statusWord) {
        return fromDataAndStatusWord(new byte[0], statusWord);
    }

    public static ResponseApdu fromResponse(byte[] bArr, long j, TimeUnit timeUnit) {
        Preconditions.checkNotNull(bArr);
        long toMillis = timeUnit.toMillis(j);
        int length = bArr.length;
        Preconditions.checkArgument(length >= 2, "Invalid response APDU after %sms. Must be at least %s bytes long: [%s]", Long.valueOf(toMillis), Integer.valueOf(2), Hex.encode(bArr));
        int i = length - 2;
        return fromResponse(Arrays.copyOfRange(bArr, 0, i), Iso7816StatusWord.fromBytes(Arrays.copyOfRange(bArr, i, i + 2)), toMillis);
    }

    public static ResponseApdu fromResponse(byte[] bArr, StatusWord statusWord, long j, TimeUnit timeUnit) {
        return fromResponse(bArr, statusWord, timeUnit.toMillis(j));
    }

    public static ResponseApdu fromResponse(byte[] bArr, StatusWord statusWord, long j) {
        Preconditions.checkNotNull(bArr);
        Preconditions.checkNotNull(statusWord);
        return new ResponseApdu(bArr, statusWord, j);
    }

    public byte[] getResponseData() {
        return (byte[]) this.rdata.clone();
    }

    public StatusWord getStatusWord() {
        return this.sw;
    }

    public long getCommandTimeMillis() {
        return this.cmdTimeMillis;
    }

    public byte[] toByteArray() {
        return Bytes.concat(this.rdata, this.sw.toBytes());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Response: ");
        if (this.rdata != null && this.rdata.length > 0) {
            stringBuilder.append(Hex.encode(this.rdata)).append(", ");
        }
        stringBuilder.append(String.format("SW=%04x", new Object[]{Integer.valueOf(this.sw.toInt())}));
        if (this.cmdTimeMillis > -1) {
            stringBuilder.append(String.format(", elapsed: %dms", new Object[]{Long.valueOf(this.cmdTimeMillis)}));
        }
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ResponseApdu responseApdu = (ResponseApdu) obj;
        if (Arrays.equals(this.rdata, responseApdu.rdata) && Objects.equals(this.sw, responseApdu.sw)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return com.google.common.base.Objects.hashCode(Integer.valueOf(Arrays.hashCode(this.rdata)), this.sw);
    }
}
