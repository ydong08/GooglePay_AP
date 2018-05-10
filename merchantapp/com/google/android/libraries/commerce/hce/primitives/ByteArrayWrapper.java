package com.google.android.libraries.commerce.hce.primitives;

import com.google.android.libraries.commerce.hce.util.Hex;
import java.io.Serializable;
import java.util.Arrays;

public class ByteArrayWrapper implements Serializable {
    private static final long serialVersionUID = 1;
    protected final byte[] bytes;

    public ByteArrayWrapper(byte[] bArr) {
        this.bytes = bArr;
    }

    public byte[] array() {
        return this.bytes;
    }

    public int hashCode() {
        return Arrays.hashCode(this.bytes);
    }

    public boolean equals(Object obj) {
        return this == obj || (obj != null && obj.getClass() == getClass() && Arrays.equals(this.bytes, ((ByteArrayWrapper) obj).bytes));
    }

    public String toString() {
        if (this.bytes == null) {
            return "<null>";
        }
        if (this.bytes.length == 0) {
            return "<empty>";
        }
        String str = "0x";
        String valueOf = String.valueOf(Hex.encodeUpper(this.bytes));
        return valueOf.length() != 0 ? str.concat(valueOf) : new String(str);
    }
}
