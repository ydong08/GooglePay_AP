package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECPoint.F2m;

class Tnaf {
    private static final BigInteger MINUS_ONE = ECConstants.ONE.negate();
    private static final BigInteger MINUS_THREE = ECConstants.THREE.negate();
    private static final BigInteger MINUS_TWO = ECConstants.TWO.negate();
    public static final ZTauElement[] alpha0 = new ZTauElement[]{null, new ZTauElement(ECConstants.ONE, ECConstants.ZERO), null, new ZTauElement(MINUS_THREE, MINUS_ONE), null, new ZTauElement(MINUS_ONE, MINUS_ONE), null, new ZTauElement(ECConstants.ONE, MINUS_ONE), null};
    public static final byte[][] alpha0Tnaf;
    public static final ZTauElement[] alpha1 = new ZTauElement[]{null, new ZTauElement(ECConstants.ONE, ECConstants.ZERO), null, new ZTauElement(MINUS_THREE, ECConstants.ONE), null, new ZTauElement(MINUS_ONE, ECConstants.ONE), null, new ZTauElement(ECConstants.ONE, ECConstants.ONE), null};
    public static final byte[][] alpha1Tnaf;

    Tnaf() {
    }

    static {
        r0 = new byte[8][];
        r0[1] = new byte[]{(byte) 1};
        r0[2] = null;
        r0[3] = new byte[]{(byte) -1, (byte) 0, (byte) 1};
        r0[4] = null;
        r0[5] = new byte[]{(byte) 1, (byte) 0, (byte) 1};
        r0[6] = null;
        r0[7] = new byte[]{(byte) -1, (byte) 0, (byte) 0, (byte) 1};
        alpha0Tnaf = r0;
        r0 = new byte[8][];
        r0[1] = new byte[]{(byte) 1};
        r0[2] = null;
        r0[3] = new byte[]{(byte) -1, (byte) 0, (byte) 1};
        r0[4] = null;
        r0[5] = new byte[]{(byte) 1, (byte) 0, (byte) 1};
        r0[6] = null;
        r0[7] = new byte[]{(byte) -1, (byte) 0, (byte) 0, (byte) -1};
        alpha1Tnaf = r0;
    }

    public static BigInteger norm(byte b, ZTauElement zTauElement) {
        BigInteger multiply = zTauElement.u.multiply(zTauElement.u);
        BigInteger multiply2 = zTauElement.u.multiply(zTauElement.v);
        BigInteger shiftLeft = zTauElement.v.multiply(zTauElement.v).shiftLeft(1);
        if (b == (byte) 1) {
            return multiply.add(multiply2).add(shiftLeft);
        }
        if (b == (byte) -1) {
            return multiply.subtract(multiply2).add(shiftLeft);
        }
        throw new IllegalArgumentException("mu must be 1 or -1");
    }

    public static ZTauElement round(SimpleBigDecimal simpleBigDecimal, SimpleBigDecimal simpleBigDecimal2, byte b) {
        int i = 0;
        int i2 = 1;
        if (simpleBigDecimal2.getScale() != simpleBigDecimal.getScale()) {
            throw new IllegalArgumentException("lambda0 and lambda1 do not have same scale");
        } else if (b == (byte) 1 || b == (byte) -1) {
            BigInteger round = simpleBigDecimal.round();
            BigInteger round2 = simpleBigDecimal2.round();
            SimpleBigDecimal subtract = simpleBigDecimal.subtract(round);
            SimpleBigDecimal subtract2 = simpleBigDecimal2.subtract(round2);
            SimpleBigDecimal add = subtract.add(subtract);
            if (b == (byte) 1) {
                add = add.add(subtract2);
            } else {
                add = add.subtract(subtract2);
            }
            SimpleBigDecimal add2 = subtract2.add(subtract2).add(subtract2);
            SimpleBigDecimal add3 = add2.add(subtract2);
            if (b == (byte) 1) {
                subtract2 = subtract.subtract(add2);
                subtract = subtract.add(add3);
            } else {
                subtract2 = subtract.add(add2);
                subtract = subtract.subtract(add3);
            }
            if (add.compareTo(ECConstants.ONE) >= 0) {
                if (subtract2.compareTo(MINUS_ONE) < 0) {
                    i2 = 0;
                    i = b;
                }
            } else if (subtract.compareTo(ECConstants.TWO) >= 0) {
                i2 = 0;
                byte b2 = b;
            } else {
                i2 = 0;
            }
            if (add.compareTo(MINUS_ONE) < 0) {
                if (subtract2.compareTo(ECConstants.ONE) >= 0) {
                    i = (byte) (-b);
                } else {
                    i2 = -1;
                }
            } else if (subtract.compareTo(MINUS_TWO) < 0) {
                i = (byte) (-b);
            }
            return new ZTauElement(round.add(BigInteger.valueOf((long) i2)), round2.add(BigInteger.valueOf((long) i)));
        } else {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
    }

    public static SimpleBigDecimal approximateDivisionByN(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, byte b, int i, int i2) {
        int i3 = ((i + 5) / 2) + i2;
        BigInteger multiply = bigInteger2.multiply(bigInteger.shiftRight(((i - i3) - 2) + b));
        BigInteger add = multiply.add(bigInteger3.multiply(multiply.shiftRight(i)));
        multiply = add.shiftRight(i3 - i2);
        if (add.testBit((i3 - i2) - 1)) {
            multiply = multiply.add(ECConstants.ONE);
        }
        return new SimpleBigDecimal(multiply, i2);
    }

    public static F2m tau(F2m f2m) {
        return f2m.tau();
    }

    public static byte getMu(ECCurve.F2m f2m) {
        if (!f2m.isKoblitz()) {
            throw new IllegalArgumentException("No Koblitz curve (ABC), TNAF multiplication not possible");
        } else if (f2m.getA().isZero()) {
            return (byte) -1;
        } else {
            return (byte) 1;
        }
    }

    public static BigInteger[] getLucas(byte b, int i, boolean z) {
        if (b == (byte) 1 || b == (byte) -1) {
            BigInteger bigInteger;
            BigInteger valueOf;
            if (z) {
                bigInteger = ECConstants.TWO;
                valueOf = BigInteger.valueOf((long) b);
            } else {
                bigInteger = ECConstants.ZERO;
                valueOf = ECConstants.ONE;
            }
            int i2 = 1;
            BigInteger bigInteger2 = bigInteger;
            bigInteger = valueOf;
            while (i2 < i) {
                if (b == (byte) 1) {
                    valueOf = bigInteger;
                } else {
                    valueOf = bigInteger.negate();
                }
                i2++;
                BigInteger subtract = valueOf.subtract(bigInteger2.shiftLeft(1));
                bigInteger2 = bigInteger;
                bigInteger = subtract;
            }
            return new BigInteger[]{bigInteger2, bigInteger};
        }
        throw new IllegalArgumentException("mu must be 1 or -1");
    }

    public static BigInteger getTw(byte b, int i) {
        if (i != 4) {
            BigInteger[] lucas = getLucas(b, i, false);
            BigInteger bit = ECConstants.ZERO.setBit(i);
            return ECConstants.TWO.multiply(lucas[0]).multiply(lucas[1].modInverse(bit)).mod(bit);
        } else if (b == (byte) 1) {
            return BigInteger.valueOf(6);
        } else {
            return BigInteger.valueOf(10);
        }
    }

    public static BigInteger[] getSi(ECCurve.F2m f2m) {
        if (f2m.isKoblitz()) {
            int m = f2m.getM();
            int intValue = f2m.getA().toBigInteger().intValue();
            byte mu = f2m.getMu();
            int shiftsForCofactor = getShiftsForCofactor(f2m.getCofactor());
            BigInteger[] lucas = getLucas(mu, (m + 3) - intValue, false);
            if (mu == (byte) 1) {
                lucas[0] = lucas[0].negate();
                lucas[1] = lucas[1].negate();
            }
            BigInteger shiftRight = ECConstants.ONE.add(lucas[1]).shiftRight(shiftsForCofactor);
            BigInteger negate = ECConstants.ONE.add(lucas[0]).shiftRight(shiftsForCofactor).negate();
            return new BigInteger[]{shiftRight, negate};
        }
        throw new IllegalArgumentException("si is defined for Koblitz curves only");
    }

    protected static int getShiftsForCofactor(BigInteger bigInteger) {
        if (bigInteger != null) {
            if (bigInteger.equals(ECConstants.TWO)) {
                return 1;
            }
            if (bigInteger.equals(ECConstants.FOUR)) {
                return 2;
            }
        }
        throw new IllegalArgumentException("h (Cofactor) must be 2 or 4");
    }

    public static ZTauElement partModReduction(BigInteger bigInteger, int i, byte b, BigInteger[] bigIntegerArr, byte b2, byte b3) {
        BigInteger add;
        if (b2 == (byte) 1) {
            add = bigIntegerArr[0].add(bigIntegerArr[1]);
        } else {
            add = bigIntegerArr[0].subtract(bigIntegerArr[1]);
        }
        BigInteger bigInteger2 = getLucas(b2, i, true)[1];
        ZTauElement round = round(approximateDivisionByN(bigInteger, bigIntegerArr[0], bigInteger2, b, i, b3), approximateDivisionByN(bigInteger, bigIntegerArr[1], bigInteger2, b, i, b3), b2);
        return new ZTauElement(bigInteger.subtract(add.multiply(round.u)).subtract(BigInteger.valueOf(2).multiply(bigIntegerArr[1]).multiply(round.v)), bigIntegerArr[1].multiply(round.u).subtract(bigIntegerArr[0].multiply(round.v)));
    }

    public static F2m multiplyFromTnaf(F2m f2m, byte[] bArr) {
        F2m f2m2 = (F2m) ((ECCurve.F2m) f2m.getCurve()).getInfinity();
        for (int length = bArr.length - 1; length >= 0; length--) {
            f2m2 = tau(f2m2);
            if (bArr[length] == (byte) 1) {
                f2m2 = f2m2.addSimple(f2m);
            } else if (bArr[length] == (byte) -1) {
                f2m2 = f2m2.subtractSimple(f2m);
            }
        }
        return f2m2;
    }

    public static byte[] tauAdicWNaf(byte b, ZTauElement zTauElement, byte b2, BigInteger bigInteger, BigInteger bigInteger2, ZTauElement[] zTauElementArr) {
        if (b == (byte) 1 || b == (byte) -1) {
            int bitLength = norm(b, zTauElement).bitLength();
            byte[] bArr = new byte[(bitLength > 30 ? (bitLength + 4) + b2 : b2 + 34)];
            BigInteger shiftRight = bigInteger.shiftRight(1);
            BigInteger bigInteger3 = zTauElement.u;
            BigInteger bigInteger4 = zTauElement.v;
            bitLength = 0;
            while (true) {
                if (bigInteger3.equals(ECConstants.ZERO) && bigInteger4.equals(ECConstants.ZERO)) {
                    return bArr;
                }
                BigInteger mod;
                if (bigInteger3.testBit(0)) {
                    byte intValue;
                    int i;
                    mod = bigInteger3.add(bigInteger4.multiply(bigInteger2)).mod(bigInteger);
                    if (mod.compareTo(shiftRight) >= 0) {
                        intValue = (byte) mod.subtract(bigInteger).intValue();
                    } else {
                        intValue = (byte) mod.intValue();
                    }
                    bArr[bitLength] = intValue;
                    if (intValue < (byte) 0) {
                        i = (byte) (-intValue);
                        intValue = (byte) 0;
                    } else {
                        byte b3 = intValue;
                        intValue = (byte) 1;
                    }
                    if (intValue != (byte) 0) {
                        bigInteger3 = bigInteger3.subtract(zTauElementArr[i].u);
                        bigInteger4 = bigInteger4.subtract(zTauElementArr[i].v);
                    } else {
                        bigInteger3 = bigInteger3.add(zTauElementArr[i].u);
                        bigInteger4 = bigInteger4.add(zTauElementArr[i].v);
                    }
                    mod = bigInteger3;
                } else {
                    bArr[bitLength] = (byte) 0;
                    mod = bigInteger3;
                }
                if (b == (byte) 1) {
                    bigInteger3 = bigInteger4.add(mod.shiftRight(1));
                } else {
                    bigInteger3 = bigInteger4.subtract(mod.shiftRight(1));
                }
                bigInteger4 = mod.shiftRight(1).negate();
                bitLength++;
            }
        } else {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
    }

    public static F2m[] getPreComp(F2m f2m, byte b) {
        byte[][] bArr;
        ECPoint[] eCPointArr = new F2m[16];
        eCPointArr[1] = f2m;
        if (b == (byte) 0) {
            bArr = alpha0Tnaf;
        } else {
            bArr = alpha1Tnaf;
        }
        int length = bArr.length;
        for (int i = 3; i < length; i += 2) {
            eCPointArr[i] = multiplyFromTnaf(f2m, bArr[i]);
        }
        f2m.getCurve().normalizeAll(eCPointArr);
        return eCPointArr;
    }
}
