package org.bouncycastle.cert;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.asn1.x509.Extensions;

public class X509AttributeCertificateHolder {
    private static Attribute[] EMPTY_ARRAY = new Attribute[0];
    private AttributeCertificate attrCert;
    private Extensions extensions;

    private static AttributeCertificate parseBytes(byte[] bArr) throws IOException {
        Throwable th;
        String str;
        String valueOf;
        try {
            return AttributeCertificate.getInstance(ASN1Primitive.fromByteArray(bArr));
        } catch (Throwable e) {
            th = e;
            str = "malformed data: ";
            valueOf = String.valueOf(th.getMessage());
            throw new CertIOException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), th);
        } catch (Throwable e2) {
            th = e2;
            str = "malformed data: ";
            valueOf = String.valueOf(th.getMessage());
            throw new CertIOException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), th);
        }
    }

    public X509AttributeCertificateHolder(byte[] bArr) throws IOException {
        this(parseBytes(bArr));
    }

    public X509AttributeCertificateHolder(AttributeCertificate attributeCertificate) {
        this.attrCert = attributeCertificate;
        this.extensions = attributeCertificate.getAcinfo().getExtensions();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof X509AttributeCertificateHolder)) {
            return false;
        }
        return this.attrCert.equals(((X509AttributeCertificateHolder) obj).attrCert);
    }

    public int hashCode() {
        return this.attrCert.hashCode();
    }
}
