package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x500.X500Name;

public class TBSCertList extends ASN1Object {
    Extensions crlExtensions;
    X500Name issuer;
    Time nextUpdate;
    ASN1Sequence revokedCertificates;
    AlgorithmIdentifier signature;
    Time thisUpdate;
    ASN1Integer version;

    public static TBSCertList getInstance(Object obj) {
        if (obj instanceof TBSCertList) {
            return (TBSCertList) obj;
        }
        if (obj != null) {
            return new TBSCertList(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public TBSCertList(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 3 || aSN1Sequence.size() > 7) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        int i = 0;
        if (aSN1Sequence.getObjectAt(0) instanceof ASN1Integer) {
            this.version = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0));
            i = 1;
        } else {
            this.version = null;
        }
        int i2 = i + 1;
        this.signature = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(i));
        int i3 = i2 + 1;
        this.issuer = X500Name.getInstance(aSN1Sequence.getObjectAt(i2));
        i = i3 + 1;
        this.thisUpdate = Time.getInstance(aSN1Sequence.getObjectAt(i3));
        if (i < aSN1Sequence.size() && ((aSN1Sequence.getObjectAt(i) instanceof ASN1UTCTime) || (aSN1Sequence.getObjectAt(i) instanceof ASN1GeneralizedTime) || (aSN1Sequence.getObjectAt(i) instanceof Time))) {
            i2 = i + 1;
            this.nextUpdate = Time.getInstance(aSN1Sequence.getObjectAt(i));
            i = i2;
        }
        if (i < aSN1Sequence.size() && !(aSN1Sequence.getObjectAt(i) instanceof DERTaggedObject)) {
            i2 = i + 1;
            this.revokedCertificates = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(i));
            i = i2;
        }
        if (i < aSN1Sequence.size() && (aSN1Sequence.getObjectAt(i) instanceof DERTaggedObject)) {
            this.crlExtensions = Extensions.getInstance(ASN1Sequence.getInstance((ASN1TaggedObject) aSN1Sequence.getObjectAt(i), true));
        }
    }

    public X500Name getIssuer() {
        return this.issuer;
    }

    public Extensions getExtensions() {
        return this.crlExtensions;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.version != null) {
            aSN1EncodableVector.add(this.version);
        }
        aSN1EncodableVector.add(this.signature);
        aSN1EncodableVector.add(this.issuer);
        aSN1EncodableVector.add(this.thisUpdate);
        if (this.nextUpdate != null) {
            aSN1EncodableVector.add(this.nextUpdate);
        }
        if (this.revokedCertificates != null) {
            aSN1EncodableVector.add(this.revokedCertificates);
        }
        if (this.crlExtensions != null) {
            aSN1EncodableVector.add(new DERTaggedObject(0, this.crlExtensions));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
