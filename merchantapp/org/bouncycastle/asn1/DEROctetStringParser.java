package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.InputStream;

public class DEROctetStringParser implements ASN1OctetStringParser {
    private DefiniteLengthInputStream stream;

    DEROctetStringParser(DefiniteLengthInputStream definiteLengthInputStream) {
        this.stream = definiteLengthInputStream;
    }

    public InputStream getOctetStream() {
        return this.stream;
    }

    public ASN1Primitive getLoadedObject() throws IOException {
        return new DEROctetString(this.stream.toByteArray());
    }

    public ASN1Primitive toASN1Primitive() {
        try {
            return getLoadedObject();
        } catch (Throwable e) {
            String str = "IOException converting stream to byte array: ";
            String valueOf = String.valueOf(e.getMessage());
            throw new ASN1ParsingException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), e);
        }
    }
}
