package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.io.Streams;

public class DERBitString extends ASN1Primitive implements ASN1String {
    private static final char[] table = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    protected byte[] data;
    protected int padBits;

    public static DERBitString getInstance(Object obj) {
        if (obj == null || (obj instanceof DERBitString)) {
            return (DERBitString) obj;
        }
        String str = "illegal object in getInstance: ";
        String valueOf = String.valueOf(obj.getClass().getName());
        throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    public static DERBitString getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        ASN1Primitive object = aSN1TaggedObject.getObject();
        if (z || (object instanceof DERBitString)) {
            return getInstance(object);
        }
        return fromOctetString(((ASN1OctetString) object).getOctets());
    }

    public DERBitString(byte[] bArr, int i) {
        this.data = bArr;
        this.padBits = i;
    }

    public DERBitString(byte[] bArr) {
        this(bArr, 0);
    }

    public DERBitString(ASN1Encodable aSN1Encodable) throws IOException {
        this.data = aSN1Encodable.toASN1Primitive().getEncoded("DER");
        this.padBits = 0;
    }

    public byte[] getBytes() {
        return this.data;
    }

    public int getPadBits() {
        return this.padBits;
    }

    boolean isConstructed() {
        return false;
    }

    int encodedLength() {
        return ((StreamUtil.calculateBodyLength(this.data.length + 1) + 1) + this.data.length) + 1;
    }

    void encode(ASN1OutputStream aSN1OutputStream) throws IOException {
        Object obj = new byte[(getBytes().length + 1)];
        obj[0] = (byte) getPadBits();
        System.arraycopy(getBytes(), 0, obj, 1, obj.length - 1);
        aSN1OutputStream.writeEncoded(3, obj);
    }

    public int hashCode() {
        return this.padBits ^ Arrays.hashCode(this.data);
    }

    protected boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERBitString)) {
            return false;
        }
        DERBitString dERBitString = (DERBitString) aSN1Primitive;
        if (this.padBits == dERBitString.padBits && Arrays.areEqual(this.data, dERBitString.data)) {
            return true;
        }
        return false;
    }

    public String getString() {
        StringBuffer stringBuffer = new StringBuffer("#");
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new ASN1OutputStream(byteArrayOutputStream).writeObject(this);
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            for (int i = 0; i != toByteArray.length; i++) {
                stringBuffer.append(table[(toByteArray[i] >>> 4) & 15]);
                stringBuffer.append(table[toByteArray[i] & 15]);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            throw new RuntimeException("internal error encoding BitString");
        }
    }

    public String toString() {
        return getString();
    }

    static DERBitString fromOctetString(byte[] bArr) {
        if (bArr.length < 1) {
            throw new IllegalArgumentException("truncated BIT STRING detected");
        }
        byte b = bArr[0];
        Object obj = new byte[(bArr.length - 1)];
        if (obj.length != 0) {
            System.arraycopy(bArr, 1, obj, 0, bArr.length - 1);
        }
        return new DERBitString(obj, b);
    }

    static DERBitString fromInputStream(int i, InputStream inputStream) throws IOException {
        if (i < 1) {
            throw new IllegalArgumentException("truncated BIT STRING detected");
        }
        int read = inputStream.read();
        byte[] bArr = new byte[(i - 1)];
        if (bArr.length == 0 || Streams.readFully(inputStream, bArr) == bArr.length) {
            return new DERBitString(bArr, read);
        }
        throw new EOFException("EOF encountered in middle of BIT STRING");
    }
}
