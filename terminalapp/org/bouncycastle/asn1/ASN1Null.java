package org.bouncycastle.asn1;

import java.io.IOException;

public abstract class ASN1Null extends ASN1Primitive {
    abstract void encode(ASN1OutputStream aSN1OutputStream) throws IOException;

    public int hashCode() {
        return -1;
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (aSN1Primitive instanceof ASN1Null) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "NULL";
    }
}
