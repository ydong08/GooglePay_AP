package org.bouncycastle.asn1;

import java.io.IOException;

public class DLTaggedObject extends ASN1TaggedObject {
    private static final byte[] ZERO_BYTES = new byte[0];

    public DLTaggedObject(boolean z, int i, ASN1Encodable aSN1Encodable) {
        super(z, i, aSN1Encodable);
    }

    boolean isConstructed() {
        if (this.empty || this.explicit) {
            return true;
        }
        return this.obj.toASN1Primitive().toDLObject().isConstructed();
    }

    int encodedLength() throws IOException {
        if (this.empty) {
            return StreamUtil.calculateTagLength(this.tagNo) + 1;
        }
        int encodedLength = this.obj.toASN1Primitive().toDLObject().encodedLength();
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
        ASN1Primitive toDLObject = this.obj.toASN1Primitive().toDLObject();
        if (this.explicit) {
            aSN1OutputStream.writeTag(160, this.tagNo);
            aSN1OutputStream.writeLength(toDLObject.encodedLength());
            aSN1OutputStream.writeObject(toDLObject);
            return;
        }
        if (!toDLObject.isConstructed()) {
            i = 128;
        }
        aSN1OutputStream.writeTag(i, this.tagNo);
        aSN1OutputStream.writeImplicitObject(toDLObject);
    }
}
