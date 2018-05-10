package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1Set extends ASN1Primitive {
    private boolean isSorted = false;
    private Vector set = new Vector();

    abstract void encode(ASN1OutputStream aSN1OutputStream) throws IOException;

    public static ASN1Set getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1Set)) {
            return (ASN1Set) obj;
        }
        if (obj instanceof ASN1SetParser) {
            return getInstance(((ASN1SetParser) obj).toASN1Primitive());
        }
        if (obj instanceof byte[]) {
            try {
                return getInstance(ASN1Primitive.fromByteArray((byte[]) obj));
            } catch (IOException e) {
                String str = "failed to construct set from byte[]: ";
                String valueOf = String.valueOf(e.getMessage());
                throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            }
        }
        if (obj instanceof ASN1Encodable) {
            ASN1Primitive toASN1Primitive = ((ASN1Encodable) obj).toASN1Primitive();
            if (toASN1Primitive instanceof ASN1Set) {
                return (ASN1Set) toASN1Primitive;
            }
        }
        str = "unknown object in getInstance: ";
        valueOf = String.valueOf(obj.getClass().getName());
        throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    public static ASN1Set getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        if (z) {
            if (aSN1TaggedObject.isExplicit()) {
                return (ASN1Set) aSN1TaggedObject.getObject();
            }
            throw new IllegalArgumentException("object implicit - explicit expected.");
        } else if (aSN1TaggedObject.isExplicit()) {
            if (aSN1TaggedObject instanceof BERTaggedObject) {
                return new BERSet(aSN1TaggedObject.getObject());
            }
            return new DLSet(aSN1TaggedObject.getObject());
        } else if (aSN1TaggedObject.getObject() instanceof ASN1Set) {
            return (ASN1Set) aSN1TaggedObject.getObject();
        } else {
            if (aSN1TaggedObject.getObject() instanceof ASN1Sequence) {
                ASN1Sequence aSN1Sequence = (ASN1Sequence) aSN1TaggedObject.getObject();
                if (aSN1TaggedObject instanceof BERTaggedObject) {
                    return new BERSet(aSN1Sequence.toArray());
                }
                return new DLSet(aSN1Sequence.toArray());
            }
            String str = "unknown object in getInstance: ";
            String valueOf = String.valueOf(aSN1TaggedObject.getClass().getName());
            throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        }
    }

    protected ASN1Set() {
    }

    protected ASN1Set(ASN1Encodable aSN1Encodable) {
        this.set.addElement(aSN1Encodable);
    }

    protected ASN1Set(ASN1EncodableVector aSN1EncodableVector, boolean z) {
        int i = 0;
        while (i != aSN1EncodableVector.size()) {
            this.set.addElement(aSN1EncodableVector.get(i));
            i++;
        }
        if (z) {
            sort();
        }
    }

    protected ASN1Set(ASN1Encodable[] aSN1EncodableArr, boolean z) {
        int i = 0;
        while (i != aSN1EncodableArr.length) {
            this.set.addElement(aSN1EncodableArr[i]);
            i++;
        }
        if (z) {
            sort();
        }
    }

    public Enumeration getObjects() {
        return this.set.elements();
    }

    public ASN1Encodable getObjectAt(int i) {
        return (ASN1Encodable) this.set.elementAt(i);
    }

    public int size() {
        return this.set.size();
    }

    public int hashCode() {
        Enumeration objects = getObjects();
        int size = size();
        while (objects.hasMoreElements()) {
            size = (size * 17) ^ getNext(objects).hashCode();
        }
        return size;
    }

    ASN1Primitive toDERObject() {
        if (this.isSorted) {
            ASN1Primitive dERSet = new DERSet();
            dERSet.set = this.set;
            return dERSet;
        }
        Vector vector = new Vector();
        for (int i = 0; i != this.set.size(); i++) {
            vector.addElement(this.set.elementAt(i));
        }
        dERSet = new DERSet();
        dERSet.set = vector;
        dERSet.sort();
        return dERSet;
    }

    ASN1Primitive toDLObject() {
        ASN1Primitive dLSet = new DLSet();
        dLSet.set = this.set;
        return dLSet;
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1Set)) {
            return false;
        }
        ASN1Set aSN1Set = (ASN1Set) aSN1Primitive;
        if (size() != aSN1Set.size()) {
            return false;
        }
        Enumeration objects = getObjects();
        Enumeration objects2 = aSN1Set.getObjects();
        while (objects.hasMoreElements()) {
            ASN1Encodable next = getNext(objects);
            ASN1Encodable next2 = getNext(objects2);
            ASN1Primitive toASN1Primitive = next.toASN1Primitive();
            ASN1Primitive toASN1Primitive2 = next2.toASN1Primitive();
            if (toASN1Primitive != toASN1Primitive2) {
                if (!toASN1Primitive.equals(toASN1Primitive2)) {
                    return false;
                }
            }
        }
        return true;
    }

    private ASN1Encodable getNext(Enumeration enumeration) {
        ASN1Encodable aSN1Encodable = (ASN1Encodable) enumeration.nextElement();
        if (aSN1Encodable == null) {
            return DERNull.INSTANCE;
        }
        return aSN1Encodable;
    }

    private boolean lessThanOrEqual(byte[] bArr, byte[] bArr2) {
        int min = Math.min(bArr.length, bArr2.length);
        int i = 0;
        while (i != min) {
            if (bArr[i] == bArr2[i]) {
                i++;
            } else if ((bArr[i] & 255) < (bArr2[i] & 255)) {
                return true;
            } else {
                return false;
            }
        }
        if (min != bArr.length) {
            return false;
        }
        return true;
    }

    private byte[] getDEREncoded(ASN1Encodable aSN1Encodable) {
        try {
            return aSN1Encodable.toASN1Primitive().getEncoded("DER");
        } catch (IOException e) {
            throw new IllegalArgumentException("cannot encode object added to SET");
        }
    }

    protected void sort() {
        if (!this.isSorted) {
            this.isSorted = true;
            if (this.set.size() > 1) {
                int size = this.set.size() - 1;
                boolean z = true;
                while (z) {
                    byte[] dEREncoded = getDEREncoded((ASN1Encodable) this.set.elementAt(0));
                    int i = 0;
                    int i2 = 0;
                    z = false;
                    while (i2 != size) {
                        int i3;
                        boolean z2;
                        byte[] dEREncoded2 = getDEREncoded((ASN1Encodable) this.set.elementAt(i2 + 1));
                        if (lessThanOrEqual(dEREncoded, dEREncoded2)) {
                            i3 = i;
                            z2 = z;
                        } else {
                            Object elementAt = this.set.elementAt(i2);
                            this.set.setElementAt(this.set.elementAt(i2 + 1), i2);
                            this.set.setElementAt(elementAt, i2 + 1);
                            dEREncoded2 = dEREncoded;
                            z2 = true;
                            i3 = i2;
                        }
                        i2++;
                        z = z2;
                        i = i3;
                        dEREncoded = dEREncoded2;
                    }
                    size = i;
                }
            }
        }
    }

    boolean isConstructed() {
        return true;
    }

    public String toString() {
        return this.set.toString();
    }
}
