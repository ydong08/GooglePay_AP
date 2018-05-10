package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERTaggedObject extends ASN1TaggedObject {
    public BERTaggedObject(int i, ASN1Encodable aSN1Encodable) {
        super(true, i, aSN1Encodable);
    }

    public BERTaggedObject(boolean z, int i, ASN1Encodable aSN1Encodable) {
        super(z, i, aSN1Encodable);
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
        int encodedLength = this.obj.toASN1Primitive().encodedLength();
        if (this.explicit) {
            return encodedLength + (StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(encodedLength));
        }
        return (encodedLength - 1) + StreamUtil.calculateTagLength(this.tagNo);
    }

    void encode(ASN1OutputStream aSN1OutputStream) throws IOException {
        aSN1OutputStream.writeTag(160, this.tagNo);
        aSN1OutputStream.write(128);
        if (!this.empty) {
            if (this.explicit) {
                aSN1OutputStream.writeObject(this.obj);
            } else {
                Enumeration objects;
                if (this.obj instanceof ASN1OctetString) {
                    if (this.obj instanceof BEROctetString) {
                        objects = ((BEROctetString) this.obj).getObjects();
                    } else {
                        objects = new BEROctetString(((ASN1OctetString) this.obj).getOctets()).getObjects();
                    }
                } else if (this.obj instanceof ASN1Sequence) {
                    objects = ((ASN1Sequence) this.obj).getObjects();
                } else if (this.obj instanceof ASN1Set) {
                    objects = ((ASN1Set) this.obj).getObjects();
                } else {
                    String str = "not implemented: ";
                    String valueOf = String.valueOf(this.obj.getClass().getName());
                    throw new RuntimeException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
                }
                while (objects.hasMoreElements()) {
                    aSN1OutputStream.writeObject((ASN1Encodable) objects.nextElement());
                }
            }
        }
        aSN1OutputStream.write(0);
        aSN1OutputStream.write(0);
    }
}
