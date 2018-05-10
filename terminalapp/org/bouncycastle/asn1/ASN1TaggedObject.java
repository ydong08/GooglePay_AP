package org.bouncycastle.asn1;

import java.io.IOException;

public abstract class ASN1TaggedObject extends ASN1Primitive implements ASN1TaggedObjectParser {
    boolean empty = false;
    boolean explicit = true;
    ASN1Encodable obj = null;
    int tagNo;

    abstract void encode(ASN1OutputStream aSN1OutputStream) throws IOException;

    public static ASN1TaggedObject getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        if (z) {
            return (ASN1TaggedObject) aSN1TaggedObject.getObject();
        }
        throw new IllegalArgumentException("implicitly tagged tagged object");
    }

    public static ASN1TaggedObject getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1TaggedObject)) {
            return (ASN1TaggedObject) obj;
        }
        if (obj instanceof byte[]) {
            try {
                return getInstance(ASN1Primitive.fromByteArray((byte[]) obj));
            } catch (IOException e) {
                String str = "failed to construct tagged object from byte[]: ";
                String valueOf = String.valueOf(e.getMessage());
                throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            }
        }
        str = "unknown object in getInstance: ";
        valueOf = String.valueOf(obj.getClass().getName());
        throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    public ASN1TaggedObject(boolean z, int i, ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable instanceof ASN1Choice) {
            this.explicit = true;
        } else {
            this.explicit = z;
        }
        this.tagNo = i;
        if (this.explicit) {
            this.obj = aSN1Encodable;
            return;
        }
        aSN1Encodable.toASN1Primitive();
        this.obj = aSN1Encodable;
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1TaggedObject)) {
            return false;
        }
        ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) aSN1Primitive;
        if (this.tagNo != aSN1TaggedObject.tagNo || this.empty != aSN1TaggedObject.empty || this.explicit != aSN1TaggedObject.explicit) {
            return false;
        }
        if (this.obj == null) {
            if (aSN1TaggedObject.obj != null) {
                return false;
            }
        } else if (!this.obj.toASN1Primitive().equals(aSN1TaggedObject.obj.toASN1Primitive())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i = this.tagNo;
        if (this.obj != null) {
            return i ^ this.obj.hashCode();
        }
        return i;
    }

    public int getTagNo() {
        return this.tagNo;
    }

    public boolean isExplicit() {
        return this.explicit;
    }

    public ASN1Primitive getObject() {
        if (this.obj != null) {
            return this.obj.toASN1Primitive();
        }
        return null;
    }

    public ASN1Primitive getLoadedObject() {
        return toASN1Primitive();
    }

    ASN1Primitive toDERObject() {
        return new DERTaggedObject(this.explicit, this.tagNo, this.obj);
    }

    ASN1Primitive toDLObject() {
        return new DLTaggedObject(this.explicit, this.tagNo, this.obj);
    }

    public String toString() {
        int i = this.tagNo;
        String valueOf = String.valueOf(this.obj);
        return new StringBuilder(String.valueOf(valueOf).length() + 13).append("[").append(i).append("]").append(valueOf).toString();
    }
}
