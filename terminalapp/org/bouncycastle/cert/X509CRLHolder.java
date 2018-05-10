package org.bouncycastle.cert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;

public class X509CRLHolder {
    private Extensions extensions;
    private boolean isIndirect;
    private GeneralNames issuerName;
    private CertificateList x509CRL;

    private static CertificateList parseStream(InputStream inputStream) throws IOException {
        Throwable th;
        String str;
        String valueOf;
        try {
            return CertificateList.getInstance(new ASN1InputStream(inputStream, true).readObject());
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

    private static boolean isIndirectCRL(Extensions extensions) {
        if (extensions == null) {
            return false;
        }
        Extension extension = extensions.getExtension(Extension.issuingDistributionPoint);
        if (extension == null || !IssuingDistributionPoint.getInstance(extension.getParsedValue()).isIndirectCRL()) {
            return false;
        }
        return true;
    }

    public X509CRLHolder(byte[] bArr) throws IOException {
        this(parseStream(new ByteArrayInputStream(bArr)));
    }

    public X509CRLHolder(CertificateList certificateList) {
        this.x509CRL = certificateList;
        this.extensions = certificateList.getTBSCertList().getExtensions();
        this.isIndirect = isIndirectCRL(this.extensions);
        this.issuerName = new GeneralNames(new GeneralName(certificateList.getIssuer()));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof X509CRLHolder)) {
            return false;
        }
        return this.x509CRL.equals(((X509CRLHolder) obj).x509CRL);
    }

    public int hashCode() {
        return this.x509CRL.hashCode();
    }
}
