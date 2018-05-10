package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.util.Arrays;

public class DERBMPString extends ASN1Primitive implements ASN1String {
    private char[] string;

    DERBMPString(char[] cArr) {
        this.string = cArr;
    }

    public String getString() {
        return new String(this.string);
    }

    public String toString() {
        return getString();
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }

    protected boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERBMPString)) {
            return false;
        }
        return Arrays.areEqual(this.string, ((DERBMPString) aSN1Primitive).string);
    }

    boolean isConstructed() {
        return false;
    }

    int encodedLength() {
        return (StreamUtil.calculateBodyLength(this.string.length * 2) + 1) + (this.string.length * 2);
    }

    void encode(ASN1OutputStream aSN1OutputStream) throws IOException {
        aSN1OutputStream.write(30);
        aSN1OutputStream.writeLength(this.string.length * 2);
        for (int i = 0; i != this.string.length; i++) {
            char c = this.string[i];
            aSN1OutputStream.write((byte) (c >> 8));
            aSN1OutputStream.write((byte) c);
        }
    }
}
