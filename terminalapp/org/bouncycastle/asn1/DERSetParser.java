package org.bouncycastle.asn1;

import java.io.IOException;

public class DERSetParser implements ASN1SetParser {
    private ASN1StreamParser _parser;

    DERSetParser(ASN1StreamParser aSN1StreamParser) {
        this._parser = aSN1StreamParser;
    }

    public ASN1Primitive getLoadedObject() throws IOException {
        return new DERSet(this._parser.readVector(), false);
    }

    public ASN1Primitive toASN1Primitive() {
        try {
            return getLoadedObject();
        } catch (Throwable e) {
            throw new ASN1ParsingException(e.getMessage(), e);
        }
    }
}
