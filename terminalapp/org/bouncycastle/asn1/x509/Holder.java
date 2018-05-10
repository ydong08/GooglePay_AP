package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

public class Holder extends ASN1Object {
    IssuerSerial baseCertificateID;
    GeneralNames entityName;
    ObjectDigestInfo objectDigestInfo;
    private int version = 1;

    public static Holder getInstance(Object obj) {
        if (obj instanceof Holder) {
            return (Holder) obj;
        }
        if (obj instanceof ASN1TaggedObject) {
            return new Holder(ASN1TaggedObject.getInstance(obj));
        }
        if (obj != null) {
            return new Holder(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    private Holder(ASN1TaggedObject aSN1TaggedObject) {
        switch (aSN1TaggedObject.getTagNo()) {
            case 0:
                this.baseCertificateID = IssuerSerial.getInstance(aSN1TaggedObject, true);
                break;
            case 1:
                this.entityName = GeneralNames.getInstance(aSN1TaggedObject, true);
                break;
            default:
                throw new IllegalArgumentException("unknown tag in Holder");
        }
        this.version = 0;
    }

    private Holder(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() > 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        for (int i = 0; i != aSN1Sequence.size(); i++) {
            ASN1TaggedObject instance = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(i));
            switch (instance.getTagNo()) {
                case 0:
                    this.baseCertificateID = IssuerSerial.getInstance(instance, false);
                    break;
                case 1:
                    this.entityName = GeneralNames.getInstance(instance, false);
                    break;
                case 2:
                    this.objectDigestInfo = ObjectDigestInfo.getInstance(instance, false);
                    break;
                default:
                    throw new IllegalArgumentException("unknown tag in Holder");
            }
        }
        this.version = 1;
    }

    public ASN1Primitive toASN1Primitive() {
        if (this.version == 1) {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            if (this.baseCertificateID != null) {
                aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.baseCertificateID));
            }
            if (this.entityName != null) {
                aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.entityName));
            }
            if (this.objectDigestInfo != null) {
                aSN1EncodableVector.add(new DERTaggedObject(false, 2, this.objectDigestInfo));
            }
            return new DERSequence(aSN1EncodableVector);
        } else if (this.entityName != null) {
            return new DERTaggedObject(true, 1, this.entityName);
        } else {
            return new DERTaggedObject(true, 0, this.baseCertificateID);
        }
    }
}
