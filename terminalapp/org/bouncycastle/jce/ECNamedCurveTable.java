package org.bouncycastle.jce;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.jce.spec.ECParameterSpec;

public class ECNamedCurveTable {
    public static ECParameterSpec getParameterSpec$5166KOBMC4NMOOBECSNL6T3ID5N6EEP99HNN4PPFC9NNARJ3F5HM2SRKDHIIUQJ3CKNN6S35CCNKAGQEC5MMAP23ELP7CPAGC5P62RB5EHIN4KRGCLHJM___0(String str) {
        X9ECParameters byOID;
        X9ECParameters byName = CustomNamedCurves.getByName(str);
        if (byName == null) {
            try {
                byName = CustomNamedCurves.getByOID(new ASN1ObjectIdentifier(str));
            } catch (IllegalArgumentException e) {
            }
            if (byName == null) {
                byName = org.bouncycastle.asn1.x9.ECNamedCurveTable.getByName(str);
                if (byName == null) {
                    try {
                        byOID = org.bouncycastle.asn1.x9.ECNamedCurveTable.getByOID(new ASN1ObjectIdentifier(str));
                    } catch (IllegalArgumentException e2) {
                    }
                    if (byOID == null) {
                        return null;
                    }
                    return new ECParameterSpec(str, byOID.getCurve(), byOID.getG(), byOID.getN(), byOID.getH(), byOID.getSeed());
                }
            }
        }
        byOID = byName;
        if (byOID == null) {
            return null;
        }
        return new ECParameterSpec(str, byOID.getCurve(), byOID.getG(), byOID.getN(), byOID.getH(), byOID.getSeed());
    }
}
