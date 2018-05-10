package org.bouncycastle.asn1.x9;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECFieldElement;

public class X9IntegerConverter {
    public int getByteLength(ECFieldElement eCFieldElement) {
        return (eCFieldElement.getFieldSize() + 7) / 8;
    }

    public byte[] integerToBytes(BigInteger bigInteger, int i) {
        Object toByteArray = bigInteger.toByteArray();
        if (i < toByteArray.length) {
            Object obj = new byte[i];
            System.arraycopy(toByteArray, toByteArray.length - obj.length, obj, 0, obj.length);
            return obj;
        } else if (i <= toByteArray.length) {
            return toByteArray;
        } else {
            byte[] bArr = new byte[i];
            System.arraycopy(toByteArray, 0, bArr, bArr.length - toByteArray.length, toByteArray.length);
            return bArr;
        }
    }
}
