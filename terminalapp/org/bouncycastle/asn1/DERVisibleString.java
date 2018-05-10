package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class DERVisibleString extends ASN1Primitive implements ASN1String {
    private byte[] string;

    DERVisibleString(byte[] bArr) {
        this.string = bArr;
    }

    public String getString() {
        return Strings.fromByteArray(this.string);
    }

    public String toString() {
        return getString();
    }

    boolean isConstructed() {
        return false;
    }

    int encodedLength() {
        return (StreamUtil.calculateBodyLength(this.string.length) + 1) + this.string.length;
    }

    void encode(ASN1OutputStream aSN1OutputStream) throws IOException {
        aSN1OutputStream.writeEncoded(26, this.string);
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (aSN1Primitive instanceof DERVisibleString) {
            return Arrays.areEqual(this.string, ((DERVisibleString) aSN1Primitive).string);
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }
}
