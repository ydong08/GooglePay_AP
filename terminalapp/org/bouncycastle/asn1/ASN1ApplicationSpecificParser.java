package org.bouncycastle.asn1;

import java.io.IOException;

public class ASN1ApplicationSpecificParser implements ASN1Encodable, InMemoryRepresentable {
    private final ASN1StreamParser parser;
    private final int tag;

    ASN1ApplicationSpecificParser(int i, ASN1StreamParser aSN1StreamParser) {
        this.tag = i;
        this.parser = aSN1StreamParser;
    }

    public ASN1Primitive getLoadedObject() throws IOException {
        return new DERApplicationSpecific(this.tag, this.parser.readVector(), (byte) 0);
    }

    public ASN1Primitive toASN1Primitive() {
        try {
            return getLoadedObject();
        } catch (Throwable e) {
            throw new ASN1ParsingException(e.getMessage(), e);
        }
    }
}
