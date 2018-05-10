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

    public static ECPoint validatePoint(ECPoint eCPoint) {
        if (eCPoint.isValid()) {
            return eCPoint;
        }
        throw new IllegalArgumentException("Invalid point");
    }

    static ECPoint implShamirsTrickWNaf(ECPoint eCPoint, BigInteger bigInteger, ECPoint eCPoint2, BigInteger bigInteger2) {
        boolean z = false;
        boolean z2 = bigInteger.signum() < 0;
        if (bigInteger2.signum() < 0) {
            z = true;
        }
        BigInteger abs = bigInteger.abs();
        BigInteger abs2 = bigInteger2.abs();
        int max = Math.max(2, Math.min(16, WNafUtil.getWindowSize(abs.bitLength())));
        int max2 = Math.max(2, Math.min(16, WNafUtil.getWindowSize(abs2.bitLength())));
        WNafPreCompInfo precompute = WNafUtil.precompute(eCPoint, max, true);
        WNafPreCompInfo precompute2 = WNafUtil.precompute(eCPoint2, max2, true);
        return implShamirsTrickWNaf(z2 ? precompute.getPreCompNeg() : precompute.getPreComp(), z2 ? precompute.getPreComp() : precompute.getPreCompNeg(), WNafUtil.generateWindowNaf(max, abs), z ? precompute2.getPreCompNeg() : precompute2.getPreComp(), z ? precompute2.getPreComp() : precompute2.getPreCompNeg(), WNafUtil.generateWindowNaf(max2, abs2));
    }

    static ECPoint implShamirsTrickWNaf(ECPoint eCPoint, BigInteger bigInteger, ECPointMap eCPointMap, BigInteger bigInteger2) {
        boolean z = false;
        boolean z2 = bigInteger.signum() < 0;
        if (bigInteger2.signum() < 0) {
            z = true;
        }
        BigInteger abs = bigInteger.abs();
        BigInteger abs2 = bigInteger2.abs();
        int max = Math.max(2, Math.min(16, WNafUtil.getWindowSize(Math.max(abs.bitLength(), abs2.bitLength()))));
        ECPoint mapPointWithPrecomp = WNafUtil.mapPointWithPrecomp(eCPoint, max, true, eCPointMap);
        WNafPreCompInfo wNafPreCompInfo = WNafUtil.getWNafPreCompInfo(eCPoint);
        WNafPreCompInfo wNafPreCompInfo2 = WNafUtil.getWNafPreCompInfo(mapPointWithPrecomp);
        return implShamirsTrickWNaf(z2 ? wNafPreCompInfo.getPreCompNeg() : wNafPreCompInfo.getPreComp(), z2 ? wNafPreCompInfo.getPreComp() : wNafPreCompInfo.getPreCompNeg(), WNafUtil.generateWindowNaf(max, abs), z ? wNafPreCompInfo2.getPreCompNeg() : wNafPreCompInfo2.getPreComp(), z ? wNafPreCompInfo2.getPreComp() : wNafPreCompInfo2.getPreCompNeg(), WNafUtil.generateWindowNaf(max, abs2));
    }

    private static ECPoint implShamirsTrickWNaf(ECPoint[] eCPointArr, ECPoint[] eCPointArr2, byte[] bArr, ECPoint[] eCPointArr3, ECPoint[] eCPointArr4, byte[] bArr2) {
        int max = Math.max(bArr.length, bArr2.length);
        ECPoint infinity = eCPointArr[0].getCurve().getInfinity();
        int i = max - 1;
        int i2 = 0;
        ECPoint eCPoint = infinity;
        while (i >= 0) {
            int i3;
            if (i < bArr.length) {
                max = bArr[i];
            } else {
                max = 0;
            }
            if (i < bArr2.length) {
                i3 = bArr2[i];
            } else {
                i3 = 0;
            }
            if ((max | i3) == 0) {
                max = i2 + 1;
            } else {
                ECPoint add;
                ECPoint timesPow2;
                if (max != 0) {
                    add = infinity.add((max < 0 ? eCPointArr2 : eCPointArr)[Math.abs(max) >>> 1]);
                } else {
                    add = infinity;
                }
                if (i3 != 0) {
                    ECPoint[] eCPointArr5;
                    int abs = Math.abs(i3);
                    if (i3 < 0) {
                        eCPointArr5 = eCPointArr4;
                    } else {
                        eCPointArr5 = eCPointArr3;
                    }
                    add = add.add(eCPointArr5[abs >>> 1]);
                }
                if (i2 > 0) {
                    timesPow2 = eCPoint.timesPow2(i2);
                    max = 0;
                } else {
                    max = i2;
                    timesPow2 = eCPoint;
                }
                eCPoint = timesPow2.twicePlus(add);
            }
            i--;
            i2 = max;
        }
        if (i2 > 0) {
            return eCPoint.timesPow2(i2);
        }
        return eCPoint;
    }
}
