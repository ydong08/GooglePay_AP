package org.bouncycastle.asn1;

import java.util.Enumeration;

class LazyConstructionEnumeration implements Enumeration {
    private ASN1InputStream aIn;
    private Object nextObj = readObject();

    public LazyConstructionEnumeration(byte[] bArr) {
        this.aIn = new ASN1InputStream(bArr, true);
    }

    public boolean hasMoreElements() {
        return this.nextObj != null;
    }

    public Object nextElement() {
        Object obj = this.nextObj;
        this.nextObj = readObject();
        return obj;
    }

    private Object readObject() {
        try {
            return this.aIn.readObject();
        } catch (Throwable e) {
            String valueOf = String.valueOf(e);
            throw new ASN1ParsingException(new StringBuilder(String.valueOf(valueOf).length() + 28).append("malformed DER construction: ").append(valueOf).toString(), e);
        }
    }
}
