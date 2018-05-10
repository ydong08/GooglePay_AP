package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class ASN1UTCTime extends ASN1Primitive {
    private byte[] time;

    ASN1UTCTime(byte[] bArr) {
        this.time = bArr;
    }

    public String getTime() {
        String fromByteArray = Strings.fromByteArray(this.time);
        if (fromByteArray.indexOf(45) >= 0 || fromByteArray.indexOf(43) >= 0) {
            int indexOf = fromByteArray.indexOf(45);
            if (indexOf < 0) {
                indexOf = fromByteArray.indexOf(43);
            }
            if (indexOf == fromByteArray.length() - 3) {
                fromByteArray = String.valueOf(fromByteArray).concat("00");
            }
            String valueOf;
            String valueOf2;
            if (indexOf == 10) {
                valueOf = String.valueOf(fromByteArray.substring(0, 10));
                valueOf2 = String.valueOf(fromByteArray.substring(10, 13));
                fromByteArray = String.valueOf(fromByteArray.substring(13, 15));
                return new StringBuilder(((String.valueOf(valueOf).length() + 6) + String.valueOf(valueOf2).length()) + String.valueOf(fromByteArray).length()).append(valueOf).append("00GMT").append(valueOf2).append(":").append(fromByteArray).toString();
            }
            valueOf = String.valueOf(fromByteArray.substring(0, 12));
            valueOf2 = String.valueOf(fromByteArray.substring(12, 15));
            fromByteArray = String.valueOf(fromByteArray.substring(15, 17));
            return new StringBuilder(((String.valueOf(valueOf).length() + 4) + String.valueOf(valueOf2).length()) + String.valueOf(fromByteArray).length()).append(valueOf).append("GMT").append(valueOf2).append(":").append(fromByteArray).toString();
        } else if (fromByteArray.length() == 11) {
            return String.valueOf(fromByteArray.substring(0, 10)).concat("00GMT+00:00");
        } else {
            return String.valueOf(fromByteArray.substring(0, 12)).concat("GMT+00:00");
        }
    }

    public String getAdjustedTime() {
        String time = getTime();
        String str;
        if (time.charAt(0) < '5') {
            str = "20";
            time = String.valueOf(time);
            return time.length() != 0 ? str.concat(time) : new String(str);
        } else {
            str = "19";
            time = String.valueOf(time);
            return time.length() != 0 ? str.concat(time) : new String(str);
        }
    }

    boolean isConstructed() {
        return false;
    }

    int encodedLength() {
        int length = this.time.length;
        return length + (StreamUtil.calculateBodyLength(length) + 1);
    }

    void encode(ASN1OutputStream aSN1OutputStream) throws IOException {
        aSN1OutputStream.write(23);
        int length = this.time.length;
        aSN1OutputStream.writeLength(length);
        for (int i = 0; i != length; i++) {
            aSN1OutputStream.write(this.time[i]);
        }
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (aSN1Primitive instanceof ASN1UTCTime) {
            return Arrays.areEqual(this.time, ((ASN1UTCTime) aSN1Primitive).time);
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.time);
    }

    public String toString() {
        return Strings.fromByteArray(this.time);
    }
}
