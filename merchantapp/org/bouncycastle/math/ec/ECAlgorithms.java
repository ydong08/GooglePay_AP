package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.field.FiniteField;
import org.bouncycastle.math.field.PolynomialExtensionField;

public class ECAlgorithms {
    public static boolean isF2mCurve(ECCurve eCCurve) {
        FiniteField field = eCCurve.getField();
        if (field.getDimension() > 1 && field.getCharacteristic().equals(ECConstants.TWO) && (field instanceof PolynomialExtensionField)) {
            return true;
        }
        return false;
    }

    public static boolean isFpCurve(ECCurve eCCurve) {
        return eCCurve.getField().getDimension() == 1;
    }

    public static void montgomeryTrick(ECFieldElement[] eCFieldElementArr, int i, int i2, ECFieldElement eCFieldElement) {
        int i3 = 0;
        ECFieldElement[] eCFieldElementArr2 = new ECFieldElement[i2];
        eCFieldElementArr2[0] = eCFieldElementArr[i];
        while (true) {
            i3++;
            if (i3 >= i2) {
                break;
            }
            eCFieldElementArr2[i3] = eCFieldElementArr2[i3 - 1].multiply(eCFieldElementArr[i + i3]);
        }
        int i4 = i3 - 1;
        if (eCFieldElement != null) {
            eCFieldElementArr2[i4] = eCFieldElementArr2[i4].multiply(eCFieldElement);
        }
        ECFieldElement invert = eCFieldElementArr2[i4].invert();
        while (i4 > 0) {
            int i5 = i4 - 1;
            i4 += i;
            ECFieldElement eCFieldElement2 = eCFieldElementArr[i4];
            eCFieldElementArr[i4] = eCFieldElementArr2[i5].multiply(invert);
            invert = invert.multiply(eCFieldElement2);
            i4 = i5;
        }
        eCFieldElementArr[i] = invert;
    }

    public static ECPoint referenceMultiply(ECPoint eCPoint, BigInteger bigInteger) {
        BigInteger abs = bigInteger.abs();
        ECPoint infinity = eCPoint.getCurve().getInfinity();
        int bitLength = abs.bitLength();
        if (bitLength > 0) {
            if (abs.testBit(0)) {
                infinity = eCPoint;
            }
            for (int i = 1; i < bitLength; i++) {
                eCPoint = eCPoint.twice();
                if (abs.testBit(i)) {
                    infinity = infinity.add(eCPoint);
                }
            }
        }
        return bigInteger.signum() < 0 ? infinity.negate() : infinity;
    }
}
