package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERBitString;

public class ReasonFlags extends DERBitString {
    public ReasonFlags(DERBitString dERBitString) {
        super(dERBitString.getBytes(), dERBitString.getPadBits());
    }
}
