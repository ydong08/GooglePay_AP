package org.bouncycastle.asn1;

import java.io.IOException;

public class DERExternalParser implements ASN1Encodable, InMemoryRepresentable {
    private ASN1StreamParser _parser;

    public DERExternalParser(ASN1StreamParser aSN1StreamParser) {
        this._parser = aSN1StreamParser;
    }

    public ASN1Primitive getLoadedObject() throws IOException {
        try {
            return new DERExternal(this._parser.readVector());
        } catch (Throwable e) {
            throw new ASN1Exception(e.getMessage(), e);
        }
    }

    public ASN1Primitive toASN1Primitive() {
        try {
            return getLoadedObject();
        } catch (Throwable e) {
            throw new ASN1ParsingException("unable to get DER object", e);
        } catch (Throwable e2) {
            throw new ASN1ParsingException("unable to get DER object", e2);
        }
    }
}
