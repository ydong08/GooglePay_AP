package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class BEROctetString extends ASN1OctetString {
    private ASN1OctetString[] octs;

    private static byte[] toBytes(ASN1OctetString[] aSN1OctetStringArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i != aSN1OctetStringArr.length) {
            try {
                byteArrayOutputStream.write(((DEROctetString) aSN1OctetStringArr[i]).getOctets());
                i++;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(String.valueOf(aSN1OctetStringArr[i].getClass().getName()).concat(" found in input should only contain DEROctetString"));
            } catch (IOException e2) {
                String str = "exception converting octets ";
                String valueOf = String.valueOf(e2.toString());
                throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    public BEROctetString(byte[] bArr) {
        super(bArr);
    }

    public BEROctetString(ASN1OctetString[] aSN1OctetStringArr) {
        super(toBytes(aSN1OctetStringArr));
        this.octs = aSN1OctetStringArr;
    }

    public byte[] getOctets() {
        return this.string;
    }

    public Enumeration getObjects() {
        if (this.octs == null) {
            return generateOcts().elements();
        }
        return new Enumeration() {
            int counter = 0;

            public boolean hasMoreElements() {
                return this.counter < BEROctetString.this.octs.length;
            }

            public Object nextElement() {
                ASN1OctetString[] access$000 = BEROctetString.this.octs;
                int i = this.counter;
                this.counter = i + 1;
                return access$000[i];
            }
        };
    }

    private Vector generateOcts() {
        Vector vector = new Vector();
        for (int i = 0; i < this.string.length; i += 1000) {
            int length;
            if (i + 1000 > this.string.length) {
                length = this.string.length;
            } else {
                length = i + 1000;
            }
            Object obj = new byte[(length - i)];
            System.arraycopy(this.string, i, obj, 0, obj.length);
            vector.addElement(new DEROctetString(obj));
        }
        return vector;
    }

    boolean isConstructed() {
        return true;
    }

    int encodedLength() throws IOException {
        Enumeration objects = getObjects();
        int i = 0;
        while (objects.hasMoreElements()) {
            i = ((ASN1Encodable) objects.nextElement()).toASN1Primitive().encodedLength() + i;
        }
        return (i + 2) + 2;
    }

    public void encode(ASN1OutputStream aSN1OutputStream) throws IOException {
        aSN1OutputStream.write(36);
        aSN1OutputStream.write(128);
        Enumeration objects = getObjects();
        while (objects.hasMoreElements()) {
            aSN1OutputStream.writeObject((ASN1Encodable) objects.nextElement());
        }
        aSN1OutputStream.write(0);
        aSN1OutputStream.write(0);
    }

    static BEROctetString fromSequence(ASN1Sequence aSN1Sequence) {
        ASN1OctetString[] aSN1OctetStringArr = new ASN1OctetString[aSN1Sequence.size()];
        Enumeration objects = aSN1Sequence.getObjects();
        int i = 0;
        while (objects.hasMoreElements()) {
            int i2 = i + 1;
            aSN1OctetStringArr[i] = (ASN1OctetString) objects.nextElement();
            i = i2;
        }
        return new BEROctetString(aSN1OctetStringArr);
    }
}
