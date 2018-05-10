package org.bouncycastle.asn1;

import java.io.IOException;

public class DERTaggedObject extends ASN1TaggedObject {
    private static final byte[] ZERO_BYTES = new byte[0];

    public DERTaggedObject(boolean z, int i, ASN1Encodable aSN1Encodable) {
        super(z, i, aSN1Encodable);
    }

    public DERTaggedObject(int i, ASN1Encodable aSN1Encodable) {
        super(true, i, aSN1Encodable);
    }

    boolean isConstructed() {
        if (this.empty || this.explicit) {
            return true;
        }
        return this.obj.toASN1Primitive().toDERObject().isConstructed();
    }

    int encodedLength() throws IOException {
        if (this.empty) {
            return StreamUtil.calculateTagLength(this.tagNo) + 1;
        }
        int encodedLength = this.obj.toASN1Primitive().toDERObject().encodedLength();
        if (this.explicit) {
            return encodedLength + (StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(encodedLength));
        }
        return (encodedLength - 1) + StreamUtil.calculateTagLength(this.tagNo);
    }

    void encode(ASN1OutputStream aSN1OutputStream) throws IOException {
        int i = 160;
        if (this.empty) {
            aSN1OutputStream.writeEncoded(160, this.tagNo, ZERO_BYTES);
            return;
        }
        ASN1Primitive toDERObject = this.obj.toASN1Primitive().toDERObject();
        if (this.explicit) {
            aSN1OutputStream.writeTag(160, this.tagNo);
            aSN1OutputStream.writeLength(toDERObject.encodedLength());
            aSN1OutputStream.writeObject(toDERObject);
            return;
        }
        if (!toDERObject.isConstructed()) {
            i = 128;
        }
        aSN1OutputStream.writeTag(i, this.tagNo);
        aSN1OutputStream.writeImplicitObject(toDERObject);
    }
}
