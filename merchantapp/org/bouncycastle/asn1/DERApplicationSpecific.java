package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bouncycastle.util.Arrays;

public class DERApplicationSpecific extends ASN1Primitive {
    private final boolean isConstructed;
    private final byte[] octets;
    private final int tag;

    DERApplicationSpecific(boolean z, int i, byte[] bArr) {
        this.isConstructed = z;
        this.tag = i;
        this.octets = bArr;
    }

    public DERApplicationSpecific(int i, ASN1EncodableVector aSN1EncodableVector) {
        this.tag = i;
        this.isConstructed = true;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i2 = 0;
        while (i2 != aSN1EncodableVector.size()) {
            try {
                byteArrayOutputStream.write(((ASN1Object) aSN1EncodableVector.get(i2)).getEncoded("DER"));
                i2++;
            } catch (Throwable e) {
                String valueOf = String.valueOf(e);
                throw new ASN1ParsingException(new StringBuilder(String.valueOf(valueOf).length() + 18).append("malformed object: ").append(valueOf).toString(), e);
            }
        }
        this.octets = byteArrayOutputStream.toByteArray();
    }

    public boolean isConstructed() {
        return this.isConstructed;
    }

    int encodedLength() throws IOException {
        return (StreamUtil.calculateTagLength(this.tag) + StreamUtil.calculateBodyLength(this.octets.length)) + this.octets.length;
    }

    void encode(ASN1OutputStream aSN1OutputStream) throws IOException {
        int i = 64;
        if (this.isConstructed) {
            i = 96;
        }
        aSN1OutputStream.writeEncoded(i, this.tag, this.octets);
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERApplicationSpecific)) {
            return false;
        }
        DERApplicationSpecific dERApplicationSpecific = (DERApplicationSpecific) aSN1Primitive;
        if (this.isConstructed == dERApplicationSpecific.isConstructed && this.tag == dERApplicationSpecific.tag && Arrays.areEqual(this.octets, dERApplicationSpecific.octets)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((this.isConstructed ? 1 : 0) ^ this.tag) ^ Arrays.hashCode(this.octets);
    }

    public DERApplicationSpecific(int i, ASN1EncodableVector aSN1EncodableVector, byte b) {
        this(i, aSN1EncodableVector);
    }
}
