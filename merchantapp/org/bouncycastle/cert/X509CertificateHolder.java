package org.bouncycastle.cert;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.Extensions;

public class X509CertificateHolder {
    private Extensions extensions;
    private Certificate x509Certificate;

    private static Certificate parseBytes(byte[] bArr) throws IOException {
        Throwable th;
        String str;
        String valueOf;
        try {
            return Certificate.getInstance(ASN1Primitive.fromByteArray(bArr));
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

    public X509CertificateHolder(byte[] bArr) throws IOException {
        this(parseBytes(bArr));
    }

    public X509CertificateHolder(Certificate certificate) {
        this.x509Certificate = certificate;
        this.extensions = certificate.getTBSCertificate().getExtensions();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof X509CertificateHolder)) {
            return false;
        }
        return this.x509Certificate.equals(((X509CertificateHolder) obj).x509Certificate);
    }

    public int hashCode() {
        return this.x509Certificate.hashCode();
    }
}
