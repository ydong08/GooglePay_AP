package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;

public class CertificateList extends ASN1Object {
    int hashCodeValue;
    boolean isHashCodeSet = false;
    DERBitString sig;
    AlgorithmIdentifier sigAlgId;
    TBSCertList tbsCertList;

    public static CertificateList getInstance(Object obj) {
        if (obj instanceof CertificateList) {
            return (CertificateList) obj;
        }
        if (obj != null) {
            return new CertificateList(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public CertificateList(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() == 3) {
            this.tbsCertList = TBSCertList.getInstance(aSN1Sequence.getObjectAt(0));
            this.sigAlgId = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(1));
            this.sig = DERBitString.getInstance(aSN1Sequence.getObjectAt(2));
            return;
        }
        throw new IllegalArgumentException("sequence wrong size for CertificateList");
    }

    public TBSCertList getTBSCertList() {
        return this.tbsCertList;
    }

    public X500Name getIssuer() {
        return this.tbsCertList.getIssuer();
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.tbsCertList);
        aSN1EncodableVector.add(this.sigAlgId);
        aSN1EncodableVector.add(this.sig);
        return new DERSequence(aSN1EncodableVector);
    }

    public int hashCode() {
        if (!this.isHashCodeSet) {
            this.hashCodeValue = super.hashCode();
            this.isHashCodeSet = true;
        }
        return this.hashCodeValue;
    }
}
