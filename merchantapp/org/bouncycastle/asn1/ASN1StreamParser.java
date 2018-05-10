package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.InputStream;

public class ASN1StreamParser {
    private final InputStream _in;
    private final int _limit;
    private final byte[][] tmpBuffers;

    public ASN1StreamParser(InputStream inputStream) {
        this(inputStream, StreamUtil.findLimit(inputStream));
    }

    public ASN1StreamParser(InputStream inputStream, int i) {
        this._in = inputStream;
        this._limit = i;
        this.tmpBuffers = new byte[11][];
    }

    ASN1Encodable readIndef(int i) throws IOException {
        switch (i) {
            case 4:
                return new BEROctetStringParser(this);
            case 8:
                return new DERExternalParser(this);
            case 16:
                return new BERSequenceParser(this);
            case 17:
                return new BERSetParser(this);
            default:
                String str = "unknown BER object encountered: 0x";
                String valueOf = String.valueOf(Integer.toHexString(i));
                if (valueOf.length() != 0) {
                    valueOf = str.concat(valueOf);
                } else {
                    valueOf = new String(str);
                }
                throw new ASN1Exception(valueOf);
        }
    }

    ASN1Primitive readTaggedObject(boolean z, int i) throws IOException {
        if (!z) {
            return new DERTaggedObject(false, i, new DEROctetString(((DefiniteLengthInputStream) this._in).toByteArray()));
        }
        ASN1EncodableVector readVector = readVector();
        if (this._in instanceof IndefiniteLengthInputStream) {
            if (readVector.size() == 1) {
                return new BERTaggedObject(true, i, readVector.get(0));
            }
            return new BERTaggedObject(false, i, BERFactory.createSequence(readVector));
        } else if (readVector.size() == 1) {
            return new DERTaggedObject(true, i, readVector.get(0));
        } else {
            return new DERTaggedObject(false, i, DERFactory.createSequence(readVector));
        }
    }

    public ASN1Encodable readObject() throws IOException {
        boolean z = false;
        int read = this._in.read();
        if (read == -1) {
            return null;
        }
        set00Check(false);
        int readTagNumber = ASN1InputStream.readTagNumber(this._in, read);
        if ((read & 32) != 0) {
            z = true;
        }
        int readLength = ASN1InputStream.readLength(this._in, this._limit);
        if (readLength >= 0) {
            InputStream definiteLengthInputStream = new DefiniteLengthInputStream(this._in, readLength);
            if ((read & 64) != 0) {
                return new DERApplicationSpecific(z, readTagNumber, definiteLengthInputStream.toByteArray());
            }
            if ((read & 128) != 0) {
                return new BERTaggedObjectParser(z, readTagNumber, new ASN1StreamParser(definiteLengthInputStream));
            }
            if (z) {
                switch (readTagNumber) {
                    case 4:
                        return new BEROctetStringParser(new ASN1StreamParser(definiteLengthInputStream));
                    case 8:
                        return new DERExternalParser(new ASN1StreamParser(definiteLengthInputStream));
                    case 16:
                        return new DERSequenceParser(new ASN1StreamParser(definiteLengthInputStream));
                    case 17:
                        return new DERSetParser(new ASN1StreamParser(definiteLengthInputStream));
                    default:
                        throw new IOException("unknown tag " + readTagNumber + " encountered");
                }
            }
            switch (readTagNumber) {
                case 4:
                    return new DEROctetStringParser(definiteLengthInputStream);
                default:
                    try {
                        return ASN1InputStream.createPrimitiveDERObject(readTagNumber, definiteLengthInputStream, this.tmpBuffers);
                    } catch (Throwable e) {
                        throw new ASN1Exception("corrupted stream detected", e);
                    }
            }
        } else if (z) {
            ASN1StreamParser aSN1StreamParser = new ASN1StreamParser(new IndefiniteLengthInputStream(this._in, this._limit), this._limit);
            if ((read & 64) != 0) {
                return new ASN1ApplicationSpecificParser(readTagNumber, aSN1StreamParser);
            }
            if ((read & 128) != 0) {
                return new BERTaggedObjectParser(true, readTagNumber, aSN1StreamParser);
            }
            return aSN1StreamParser.readIndef(readTagNumber);
        } else {
            throw new IOException("indefinite length primitive encoding encountered");
        }
    }

    private void set00Check(boolean z) {
        if (this._in instanceof IndefiniteLengthInputStream) {
            ((IndefiniteLengthInputStream) this._in).setEofOn00(z);
        }
    }

    ASN1EncodableVector readVector() throws IOException {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        while (true) {
            ASN1Encodable readObject = readObject();
            if (readObject == null) {
                return aSN1EncodableVector;
            }
            if (readObject instanceof InMemoryRepresentable) {
                aSN1EncodableVector.add(((InMemoryRepresentable) readObject).getLoadedObject());
            } else {
                aSN1EncodableVector.add(readObject.toASN1Primitive());
            }
        }
    }
}
