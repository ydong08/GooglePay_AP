package org.bouncycastle.pkcs;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.CertificationRequest;

public class PKCS10CertificationRequest {
    private static Attribute[] EMPTY_ARRAY = new Attribute[0];
    private CertificationRequest certificationRequest;

    private static CertificationRequest parseBytes(byte[] bArr) throws IOException {
        Throwable th;
        String str;
        String valueOf;
        try {
            return CertificationRequest.getInstance(ASN1Primitive.fromByteArray(bArr));
        } catch (Throwable e) {
            th = e;
            str = "malformed data: ";
            valueOf = String.valueOf(th.getMessage());
            throw new PKCSIOException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), th);
        } catch (Throwable e2) {
            th = e2;
            str = "malformed data: ";
            valueOf = String.valueOf(th.getMessage());
            throw new PKCSIOException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), th);
        }
    }

    public PKCS10CertificationRequest(CertificationRequest certificationRequest) {
        this.certificationRequest = certificationRequest;
    }

    public PKCS10CertificationRequest(byte[] bArr) throws IOException {
        this(parseBytes(bArr));
    }

    public CertificationRequest toASN1Structure() {
        return this.certificationRequest;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PKCS10CertificationRequest)) {
            return false;
        }
        return toASN1Structure().equals(((PKCS10CertificationRequest) obj).toASN1Structure());
    }

    public int hashCode() {
        return toASN1Structure().hashCode();
    }
}
