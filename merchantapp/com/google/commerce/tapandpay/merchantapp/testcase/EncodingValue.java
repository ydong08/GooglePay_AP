package com.google.commerce.tapandpay.merchantapp.testcase;

import android.widget.EditText;
import com.google.android.libraries.commerce.hce.util.Bcd;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import com.google.internal.tapandpay.v1.valuables.nano.CommonProto.SmartTap;
import java.nio.charset.StandardCharsets;

public abstract class EncodingValue {

    public enum Encoding {
        BCD {
            public byte formatByte() {
                return (byte) 1;
            }

            public boolean isValid(String str) {
                return str.matches("[0-9]*");
            }

            public void setInputType(EditText editText) {
                editText.setInputType(2);
            }

            public void setTapValue(SmartTap smartTap, String str) {
                smartTap.bcdStringValue = str;
            }

            public void setTapPin(SmartTap smartTap, String str) {
                smartTap.bcdStringPin = str;
            }

            public void setTapCvc(SmartTap smartTap, String str) {
                smartTap.bcdStringCvc = str;
            }

            public byte[] encode(String str) {
                return Bcd.encode(str);
            }
        },
        ASCII {
            public byte formatByte() {
                return (byte) 2;
            }

            public boolean isValid(String str) {
                return CharMatcher.ascii().matchesAllOf(str);
            }

            public void setInputType(EditText editText) {
                editText.setInputType(1);
            }

            public void setTapValue(SmartTap smartTap, String str) {
                smartTap.asciiValue = str;
            }

            public void setTapPin(SmartTap smartTap, String str) {
                smartTap.asciiPin = str;
            }

            public void setTapCvc(SmartTap smartTap, String str) {
                smartTap.asciiCvc = str;
            }

            public byte[] encode(String str) {
                return str.getBytes(StandardCharsets.US_ASCII);
            }
        },
        Binary {
            public byte formatByte() {
                return (byte) 0;
            }

            public boolean isValid(String str) {
                return str.matches("[0-9]*");
            }

            public void setInputType(EditText editText) {
                editText.setInputType(2);
            }

            public void setTapValue(SmartTap smartTap, String str) {
                smartTap.binaryStringValue = str;
            }

            public void setTapPin(SmartTap smartTap, String str) {
                smartTap.binaryStringPin = str;
            }

            public void setTapCvc(SmartTap smartTap, String str) {
                smartTap.binaryStringCvc = str;
            }

            public byte[] encode(String str) {
                return Longs.toByteArray(Long.parseLong(str));
            }
        };

        public abstract byte[] encode(String str);

        public abstract byte formatByte();

        public abstract boolean isValid(String str);

        public abstract void setInputType(EditText editText);

        public abstract void setTapCvc(SmartTap smartTap, String str);

        public abstract void setTapPin(SmartTap smartTap, String str);

        public abstract void setTapValue(SmartTap smartTap, String str);

        public byte[] encodeWithFormat(String str) {
            byte[] bArr = new byte[]{formatByte()};
            return Bytes.concat(bArr, encode(str));
        }
    }

    public abstract Encoding getEncoding();

    public abstract String getValue();

    public static EncodingValue create(String str, Encoding encoding) {
        return new AutoValue_EncodingValue(str, encoding);
    }

    public static EncodingValue getValueEncoding(SmartTap smartTap) {
        String str = "";
        Encoding encoding = Encoding.BCD;
        if (!smartTap.bcdStringValue.isEmpty()) {
            str = smartTap.bcdStringValue;
            encoding = Encoding.BCD;
        } else if (!smartTap.asciiValue.isEmpty()) {
            str = smartTap.asciiValue;
            encoding = Encoding.ASCII;
        } else if (!smartTap.binaryStringValue.isEmpty()) {
            str = smartTap.binaryStringValue;
            encoding = Encoding.Binary;
        }
        return create(str, encoding);
    }

    public static EncodingValue getPinEncoding(SmartTap smartTap) {
        String str = "";
        Encoding encoding = Encoding.BCD;
        if (!smartTap.bcdStringPin.isEmpty()) {
            str = smartTap.bcdStringPin;
            encoding = Encoding.BCD;
        } else if (!smartTap.asciiPin.isEmpty()) {
            str = smartTap.asciiPin;
            encoding = Encoding.ASCII;
        } else if (!smartTap.binaryStringPin.isEmpty()) {
            str = smartTap.binaryStringPin;
            encoding = Encoding.Binary;
        }
        return create(str, encoding);
    }

    public static EncodingValue getCvcEncoding(SmartTap smartTap) {
        String str = "";
        Encoding encoding = Encoding.BCD;
        if (!smartTap.bcdStringCvc.isEmpty()) {
            str = smartTap.bcdStringCvc;
            encoding = Encoding.BCD;
        } else if (!smartTap.asciiCvc.isEmpty()) {
            str = smartTap.asciiCvc;
            encoding = Encoding.ASCII;
        } else if (!smartTap.binaryStringCvc.isEmpty()) {
            str = smartTap.binaryStringCvc;
            encoding = Encoding.Binary;
        }
        return create(str, encoding);
    }

    public byte[] getByteValueWithFormat() {
        Preconditions.checkArgument(getEncoding().isValid(getValue()));
        return getEncoding().encodeWithFormat(getValue());
    }

    public byte[] getByteValue() {
        Preconditions.checkArgument(getEncoding().isValid(getValue()));
        return getEncoding().encode(getValue());
    }
}
