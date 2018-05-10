package org.bouncycastle.asn1;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class ASN1GeneralizedTime extends ASN1Primitive {
    private byte[] time;

    public static ASN1GeneralizedTime getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1GeneralizedTime)) {
            return (ASN1GeneralizedTime) obj;
        }
        if (obj instanceof byte[]) {
            try {
                return (ASN1GeneralizedTime) ASN1Primitive.fromByteArray((byte[]) obj);
            } catch (Exception e) {
                String str = "encoding error in getInstance: ";
                String valueOf = String.valueOf(e.toString());
                throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            }
        }
        str = "illegal object in getInstance: ";
        valueOf = String.valueOf(obj.getClass().getName());
        throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    ASN1GeneralizedTime(byte[] bArr) {
        this.time = bArr;
    }

    public String getTime() {
        String fromByteArray = Strings.fromByteArray(this.time);
        if (fromByteArray.charAt(fromByteArray.length() - 1) == 'Z') {
            return String.valueOf(fromByteArray.substring(0, fromByteArray.length() - 1)).concat("GMT+00:00");
        }
        int length = fromByteArray.length() - 5;
        char charAt = fromByteArray.charAt(length);
        if (charAt == '-' || charAt == '+') {
            String valueOf = String.valueOf(fromByteArray.substring(0, length));
            String valueOf2 = String.valueOf(fromByteArray.substring(length, length + 3));
            fromByteArray = String.valueOf(fromByteArray.substring(length + 3));
            return new StringBuilder(((String.valueOf(valueOf).length() + 4) + String.valueOf(valueOf2).length()) + String.valueOf(fromByteArray).length()).append(valueOf).append("GMT").append(valueOf2).append(":").append(fromByteArray).toString();
        }
        length = fromByteArray.length() - 3;
        charAt = fromByteArray.charAt(length);
        if (charAt == '-' || charAt == '+') {
            valueOf = String.valueOf(fromByteArray.substring(0, length));
            fromByteArray = String.valueOf(fromByteArray.substring(length));
            return new StringBuilder((String.valueOf(valueOf).length() + 6) + String.valueOf(fromByteArray).length()).append(valueOf).append("GMT").append(fromByteArray).append(":00").toString();
        }
        String valueOf3 = String.valueOf(fromByteArray);
        fromByteArray = String.valueOf(calculateGMTOffset());
        return fromByteArray.length() != 0 ? valueOf3.concat(fromByteArray) : new String(valueOf3);
    }

    private String calculateGMTOffset() {
        String valueOf;
        String valueOf2;
        String str = "+";
        TimeZone timeZone = TimeZone.getDefault();
        int rawOffset = timeZone.getRawOffset();
        if (rawOffset < 0) {
            str = "-";
            rawOffset = -rawOffset;
        }
        int i = rawOffset / 3600000;
        int i2 = (rawOffset - (((i * 60) * 60) * 1000)) / 60000;
        try {
            if (timeZone.useDaylightTime() && timeZone.inDaylightTime(getDate())) {
                rawOffset = (str.equals("+") ? 1 : -1) + i;
                valueOf = String.valueOf(convert(rawOffset));
                valueOf2 = String.valueOf(convert(i2));
                return new StringBuilder(((String.valueOf(str).length() + 4) + String.valueOf(valueOf).length()) + String.valueOf(valueOf2).length()).append("GMT").append(str).append(valueOf).append(":").append(valueOf2).toString();
            }
        } catch (ParseException e) {
        }
        rawOffset = i;
        valueOf = String.valueOf(convert(rawOffset));
        valueOf2 = String.valueOf(convert(i2));
        return new StringBuilder(((String.valueOf(str).length() + 4) + String.valueOf(valueOf).length()) + String.valueOf(valueOf2).length()).append("GMT").append(str).append(valueOf).append(":").append(valueOf2).toString();
    }

    private String convert(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return Integer.toString(i);
    }

    public Date getDate() throws ParseException {
        SimpleDateFormat simpleDateFormat;
        String str;
        String fromByteArray = Strings.fromByteArray(this.time);
        SimpleDateFormat simpleDateFormat2;
        String str2;
        if (fromByteArray.endsWith("Z")) {
            if (hasFractionalSeconds()) {
                simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss.SSS'Z'");
            } else {
                simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
            }
            simpleDateFormat2.setTimeZone(new SimpleTimeZone(0, "Z"));
            str2 = fromByteArray;
            simpleDateFormat = simpleDateFormat2;
            str = str2;
        } else if (fromByteArray.indexOf(45) > 0 || fromByteArray.indexOf(43) > 0) {
            fromByteArray = getTime();
            if (hasFractionalSeconds()) {
                simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss.SSSz");
            } else {
                simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmssz");
            }
            simpleDateFormat2.setTimeZone(new SimpleTimeZone(0, "Z"));
            str2 = fromByteArray;
            simpleDateFormat = simpleDateFormat2;
            str = str2;
        } else {
            if (hasFractionalSeconds()) {
                simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
            } else {
                simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");
            }
            simpleDateFormat2.setTimeZone(new SimpleTimeZone(0, TimeZone.getDefault().getID()));
            str2 = fromByteArray;
            simpleDateFormat = simpleDateFormat2;
            str = str2;
        }
        if (hasFractionalSeconds()) {
            String substring = str.substring(14);
            int i = 1;
            while (i < substring.length()) {
                char charAt = substring.charAt(i);
                if ('0' > charAt || charAt > '9') {
                    break;
                }
                i++;
            }
            String valueOf;
            String valueOf2;
            if (i - 1 > 3) {
                valueOf = String.valueOf(substring.substring(0, 4));
                valueOf2 = String.valueOf(substring.substring(i));
                Object concat = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
                valueOf = String.valueOf(str.substring(0, 14));
                str = String.valueOf(concat);
                str = str.length() != 0 ? valueOf.concat(str) : new String(valueOf);
            } else if (i - 1 == 1) {
                valueOf = String.valueOf(substring.substring(0, i));
                valueOf2 = String.valueOf(substring.substring(i));
                valueOf2 = new StringBuilder((String.valueOf(valueOf).length() + 2) + String.valueOf(valueOf2).length()).append(valueOf).append("00").append(valueOf2).toString();
                valueOf = String.valueOf(str.substring(0, 14));
                str = String.valueOf(valueOf2);
                str = str.length() != 0 ? valueOf.concat(str) : new String(valueOf);
            } else if (i - 1 == 2) {
                valueOf = String.valueOf(substring.substring(0, i));
                valueOf2 = String.valueOf(substring.substring(i));
                valueOf2 = new StringBuilder((String.valueOf(valueOf).length() + 1) + String.valueOf(valueOf2).length()).append(valueOf).append("0").append(valueOf2).toString();
                valueOf = String.valueOf(str.substring(0, 14));
                str = String.valueOf(valueOf2);
                str = str.length() != 0 ? valueOf.concat(str) : new String(valueOf);
            }
        }
        return simpleDateFormat.parse(str);
    }

    private boolean hasFractionalSeconds() {
        int i = 0;
        while (i != this.time.length) {
            if (this.time[i] == (byte) 46 && i == 14) {
                return true;
            }
            i++;
        }
        return false;
    }

    boolean isConstructed() {
        return false;
    }

    int encodedLength() {
        int length = this.time.length;
        return length + (StreamUtil.calculateBodyLength(length) + 1);
    }

    void encode(ASN1OutputStream aSN1OutputStream) throws IOException {
        aSN1OutputStream.writeEncoded(24, this.time);
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (aSN1Primitive instanceof ASN1GeneralizedTime) {
            return Arrays.areEqual(this.time, ((ASN1GeneralizedTime) aSN1Primitive).time);
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.time);
    }
}
