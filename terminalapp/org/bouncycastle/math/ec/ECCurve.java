package org.bouncycastle.math.ec;

import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Random;
import org.bouncycastle.math.ec.endo.ECEndomorphism;
import org.bouncycastle.math.ec.endo.GLVEndomorphism;
import org.bouncycastle.math.field.FiniteField;
import org.bouncycastle.math.field.FiniteFields;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Integers;

public abstract class ECCurve {
    protected ECFieldElement a;
    protected ECFieldElement b;
    protected BigInteger cofactor;
    protected int coord;
    protected ECEndomorphism endomorphism;
    protected FiniteField field;
    protected ECMultiplier multiplier;
    protected BigInteger order;

    public class Config {
        protected int coord;
        protected ECEndomorphism endomorphism;
        protected ECMultiplier multiplier;

        Config(int i, ECEndomorphism eCEndomorphism, ECMultiplier eCMultiplier) {
            this.coord = i;
            this.endomorphism = eCEndomorphism;
            this.multiplier = eCMultiplier;
        }

        public Config setEndomorphism(ECEndomorphism eCEndomorphism) {
            this.endomorphism = eCEndomorphism;
            return this;
        }

        public ECCurve create() {
            if (ECCurve.this.supportsCoordinateSystem(this.coord)) {
                ECCurve cloneCurve = ECCurve.this.cloneCurve();
                if (cloneCurve == ECCurve.this) {
                    throw new IllegalStateException("implementation returned current curve");
                }
                cloneCurve.coord = this.coord;
                cloneCurve.endomorphism = this.endomorphism;
                cloneCurve.multiplier = this.multiplier;
                return cloneCurve;
            }
            throw new IllegalStateException("unsupported coordinate system");
        }
    }

    public static class F2m extends ECCurve {
        private org.bouncycastle.math.ec.ECPoint.F2m infinity;
        private int k1;
        private int k2;
        private int k3;
        private int m;
        private byte mu;
        private BigInteger[] si;

        public F2m(int i, int i2, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
            this(i, i2, 0, 0, bigInteger, bigInteger2, bigInteger3, bigInteger4);
        }

        public F2m(int i, int i2, int i3, int i4, BigInteger bigInteger, BigInteger bigInteger2) {
            this(i, i2, i3, i4, bigInteger, bigInteger2, null, null);
        }

        public F2m(int i, int i2, int i3, int i4, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
            super(i, i2, i3, i4);
            this.mu = (byte) 0;
            this.si = null;
            this.m = i;
            this.k1 = i2;
            this.k2 = i3;
            this.k3 = i4;
            this.order = bigInteger3;
            this.cofactor = bigInteger4;
            this.infinity = new org.bouncycastle.math.ec.ECPoint.F2m(this, null, null);
            this.a = fromBigInteger(bigInteger);
            this.b = fromBigInteger(bigInteger2);
            this.coord = 6;
        }

        protected F2m(int i, int i2, int i3, int i4, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, BigInteger bigInteger, BigInteger bigInteger2) {
            super(i, i2, i3, i4);
            this.mu = (byte) 0;
            this.si = null;
            this.m = i;
            this.k1 = i2;
            this.k2 = i3;
            this.k3 = i4;
            this.order = bigInteger;
            this.cofactor = bigInteger2;
            this.infinity = new org.bouncycastle.math.ec.ECPoint.F2m(this, null, null);
            this.a = eCFieldElement;
            this.b = eCFieldElement2;
            this.coord = 6;
        }

        protected ECCurve cloneCurve() {
            return new F2m(this.m, this.k1, this.k2, this.k3, this.a, this.b, this.order, this.cofactor);
        }

        public boolean supportsCoordinateSystem(int i) {
            switch (i) {
                case 0:
                case 1:
                case 6:
                    return true;
                default:
                    return false;
            }
        }

        protected ECMultiplier createDefaultMultiplier() {
            if (isKoblitz()) {
                return new WTauNafMultiplier();
            }
            return super.createDefaultMultiplier();
        }

        public int getFieldSize() {
            return this.m;
        }

        public ECFieldElement fromBigInteger(BigInteger bigInteger) {
            return new org.bouncycastle.math.ec.ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, bigInteger);
        }

        public ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2, boolean z) {
            ECFieldElement fromBigInteger = fromBigInteger(bigInteger);
            ECFieldElement fromBigInteger2 = fromBigInteger(bigInteger2);
            switch (getCoordinateSystem()) {
                case 5:
                case 6:
                    if (!fromBigInteger.isZero()) {
                        fromBigInteger2 = fromBigInteger2.divide(fromBigInteger).add(fromBigInteger);
                        break;
                    } else if (!fromBigInteger2.square().equals(getB())) {
                        throw new IllegalArgumentException();
                    }
                    break;
            }
            return createRawPoint(fromBigInteger, fromBigInteger2, z);
        }

        protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
            return new org.bouncycastle.math.ec.ECPoint.F2m(this, eCFieldElement, eCFieldElement2, z);
        }

        protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
            return new org.bouncycastle.math.ec.ECPoint.F2m(this, eCFieldElement, eCFieldElement2, eCFieldElementArr, z);
        }

        public ECPoint getInfinity() {
            return this.infinity;
        }

        public boolean isKoblitz() {
            return this.order != null && this.cofactor != null && this.b.isOne() && (this.a.isZero() || this.a.isOne());
        }

        synchronized byte getMu() {
            if (this.mu == (byte) 0) {
                this.mu = Tnaf.getMu(this);
            }
            return this.mu;
        }

        synchronized BigInteger[] getSi() {
            if (this.si == null) {
                this.si = Tnaf.getSi(this);
            }
            return this.si;
        }

        protected ECPoint decompressPoint(int i, BigInteger bigInteger) {
            ECFieldElement sqrt;
            ECFieldElement fromBigInteger = fromBigInteger(bigInteger);
            if (fromBigInteger.isZero()) {
                sqrt = this.b.sqrt();
            } else {
                sqrt = solveQuadraticEquation(fromBigInteger.square().invert().multiply(this.b).add(this.a).add(fromBigInteger));
                if (sqrt != null) {
                    if (sqrt.testBitZero() != (i == 1)) {
                        sqrt = sqrt.addOne();
                    }
                    switch (getCoordinateSystem()) {
                        case 5:
                        case 6:
                            sqrt = sqrt.add(fromBigInteger);
                            break;
                        default:
                            sqrt = sqrt.multiply(fromBigInteger);
                            break;
                    }
                }
                sqrt = null;
            }
            if (sqrt != null) {
                return createRawPoint(fromBigInteger, sqrt, true);
            }
            throw new IllegalArgumentException("Invalid point compression");
        }

        private ECFieldElement solveQuadraticEquation(ECFieldElement eCFieldElement) {
            if (eCFieldElement.isZero()) {
                return eCFieldElement;
            }
            ECFieldElement eCFieldElement2;
            ECFieldElement fromBigInteger = fromBigInteger(ECConstants.ZERO);
            Random random = new Random();
            do {
                ECFieldElement fromBigInteger2 = fromBigInteger(new BigInteger(this.m, random));
                ECFieldElement eCFieldElement3 = eCFieldElement;
                eCFieldElement2 = fromBigInteger;
                for (int i = 1; i <= this.m - 1; i++) {
                    eCFieldElement3 = eCFieldElement3.square();
                    eCFieldElement2 = eCFieldElement2.square().add(eCFieldElement3.multiply(fromBigInteger2));
                    eCFieldElement3 = eCFieldElement3.add(eCFieldElement);
                }
                if (!eCFieldElement3.isZero()) {
                    return null;
                }
            } while (eCFieldElement2.square().add(eCFieldElement2).isZero());
            return eCFieldElement2;
        }

        public int getM() {
            return this.m;
        }
    }

    public static class Fp extends ECCurve {
        org.bouncycastle.math.ec.ECPoint.Fp infinity;
        BigInteger q;
        BigInteger r;

        public Fp(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            this(bigInteger, bigInteger2, bigInteger3, null, null);
        }

        public Fp(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5) {
            super(bigInteger);
            this.q = bigInteger;
            this.r = org.bouncycastle.math.ec.ECFieldElement.Fp.calculateResidue(bigInteger);
            this.infinity = new org.bouncycastle.math.ec.ECPoint.Fp(this, null, null);
            this.a = fromBigInteger(bigInteger2);
            this.b = fromBigInteger(bigInteger3);
            this.order = bigInteger4;
            this.cofactor = bigInteger5;
            this.coord = 4;
        }

        protected Fp(BigInteger bigInteger, BigInteger bigInteger2, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, BigInteger bigInteger3, BigInteger bigInteger4) {
            super(bigInteger);
            this.q = bigInteger;
            this.r = bigInteger2;
            this.infinity = new org.bouncycastle.math.ec.ECPoint.Fp(this, null, null);
            this.a = eCFieldElement;
            this.b = eCFieldElement2;
            this.order = bigInteger3;
            this.cofactor = bigInteger4;
            this.coord = 4;
        }

        protected ECCurve cloneCurve() {
            return new Fp(this.q, this.r, this.a, this.b, this.order, this.cofactor);
        }

        public boolean supportsCoordinateSystem(int i) {
            switch (i) {
                case 0:
                case 1:
                case 2:
                case 4:
                    return true;
                default:
                    return false;
            }
        }

        public int getFieldSize() {
            return this.q.bitLength();
        }

        public ECFieldElement fromBigInteger(BigInteger bigInteger) {
            return new org.bouncycastle.math.ec.ECFieldElement.Fp(this.q, this.r, bigInteger);
        }

        protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
            return new org.bouncycastle.math.ec.ECPoint.Fp(this, eCFieldElement, eCFieldElement2, z);
        }

        protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
            return new org.bouncycastle.math.ec.ECPoint.Fp(this, eCFieldElement, eCFieldElement2, eCFieldElementArr, z);
        }

        public ECPoint importPoint(ECPoint eCPoint) {
            if (!(this == eCPoint.getCurve() || getCoordinateSystem() != 2 || eCPoint.isInfinity())) {
                switch (eCPoint.getCurve().getCoordinateSystem()) {
                    case 2:
                    case 3:
                    case 4:
                        return new org.bouncycastle.math.ec.ECPoint.Fp(this, fromBigInteger(eCPoint.x.toBigInteger()), fromBigInteger(eCPoint.y.toBigInteger()), new ECFieldElement[]{fromBigInteger(eCPoint.zs[0].toBigInteger())}, eCPoint.withCompression);
                }
            }
            return super.importPoint(eCPoint);
        }

        public ECPoint getInfinity() {
            return this.infinity;
        }
    }

    protected abstract ECCurve cloneCurve();

    protected abstract ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z);

    protected abstract ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z);

    public abstract ECFieldElement fromBigInteger(BigInteger bigInteger);

    public abstract int getFieldSize();

    public abstract ECPoint getInfinity();

    protected ECCurve(FiniteField finiteField) {
        this.coord = 0;
        this.endomorphism = null;
        this.multiplier = null;
        this.field = finiteField;
    }

    public Config configure() {
        return new Config(this.coord, this.endomorphism, this.multiplier);
    }

    public ECPoint validatePoint(BigInteger bigInteger, BigInteger bigInteger2) {
        ECPoint createPoint = createPoint(bigInteger, bigInteger2);
        if (createPoint.isValid()) {
            return createPoint;
        }
        throw new IllegalArgumentException("Invalid point coordinates");
    }

    public ECPoint validatePoint(BigInteger bigInteger, BigInteger bigInteger2, boolean z) {
        ECPoint createPoint = createPoint(bigInteger, bigInteger2, z);
        if (createPoint.isValid()) {
            return createPoint;
        }
        throw new IllegalArgumentException("Invalid point coordinates");
    }

    public ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2) {
        return createPoint(bigInteger, bigInteger2, false);
    }

    public ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2, boolean z) {
        return createRawPoint(fromBigInteger(bigInteger), fromBigInteger(bigInteger2), z);
    }

    protected ECMultiplier createDefaultMultiplier() {
        if (this.endomorphism instanceof GLVEndomorphism) {
            return new GLVMultiplier(this, (GLVEndomorphism) this.endomorphism);
        }
        return new WNafL2RMultiplier();
    }

    public boolean supportsCoordinateSystem(int i) {
        return i == 0;
    }

    public PreCompInfo getPreCompInfo(ECPoint eCPoint, String str) {
        PreCompInfo preCompInfo;
        checkPoint(eCPoint);
        synchronized (eCPoint) {
            Hashtable hashtable = eCPoint.preCompTable;
            preCompInfo = hashtable == null ? null : (PreCompInfo) hashtable.get(str);
        }
        return preCompInfo;
    }

    public void setPreCompInfo(ECPoint eCPoint, String str, PreCompInfo preCompInfo) {
        checkPoint(eCPoint);
        synchronized (eCPoint) {
            Hashtable hashtable = eCPoint.preCompTable;
            if (hashtable == null) {
                hashtable = new Hashtable(4);
                eCPoint.preCompTable = hashtable;
            }
            hashtable.put(str, preCompInfo);
        }
    }

    public ECPoint importPoint(ECPoint eCPoint) {
        if (this == eCPoint.getCurve()) {
            return eCPoint;
        }
        if (eCPoint.isInfinity()) {
            return getInfinity();
        }
        ECPoint normalize = eCPoint.normalize();
        return validatePoint(normalize.getXCoord().toBigInteger(), normalize.getYCoord().toBigInteger(), normalize.withCompression);
    }

    public void normalizeAll(ECPoint[] eCPointArr) {
        normalizeAll(eCPointArr, 0, eCPointArr.length, null);
    }

    public void normalizeAll(ECPoint[] eCPointArr, int i, int i2, ECFieldElement eCFieldElement) {
        checkPoints(eCPointArr, i, i2);
        switch (getCoordinateSystem()) {
            case 0:
            case 5:
                if (eCFieldElement != null) {
                    throw new IllegalArgumentException("'iso' not valid for affine coordinates");
                }
                return;
            default:
                int i3;
                ECFieldElement[] eCFieldElementArr = new ECFieldElement[i2];
                int[] iArr = new int[i2];
                int i4 = 0;
                for (int i5 = 0; i5 < i2; i5++) {
                    ECPoint eCPoint = eCPointArr[i + i5];
                    if (!(eCPoint == null || (eCFieldElement == null && eCPoint.isNormalized()))) {
                        eCFieldElementArr[i4] = eCPoint.getZCoord(0);
                        i3 = i4 + 1;
                        iArr[i4] = i + i5;
                        i4 = i3;
                    }
                }
                if (i4 != 0) {
                    ECAlgorithms.montgomeryTrick(eCFieldElementArr, 0, i4, eCFieldElement);
                    for (i3 = 0; i3 < i4; i3++) {
                        int i6 = iArr[i3];
                        eCPointArr[i6] = eCPointArr[i6].normalize(eCFieldElementArr[i3]);
                    }
                    return;
                }
                return;
        }
    }

    public FiniteField getField() {
        return this.field;
    }

    public ECFieldElement getA() {
        return this.a;
    }

    public ECFieldElement getB() {
        return this.b;
    }

    public BigInteger getOrder() {
        return this.order;
    }

    public BigInteger getCofactor() {
        return this.cofactor;
    }

    public int getCoordinateSystem() {
        return this.coord;
    }

    public synchronized ECMultiplier getMultiplier() {
        if (this.multiplier == null) {
            this.multiplier = createDefaultMultiplier();
        }
        return this.multiplier;
    }

    public ECPoint decodePoint(byte[] bArr) {
        ECPoint infinity;
        boolean z = true;
        int fieldSize = (getFieldSize() + 7) / 8;
        byte b = bArr[0];
        switch (b) {
            case (byte) 0:
                if (bArr.length == 1) {
                    infinity = getInfinity();
                    break;
                }
                throw new IllegalArgumentException("Incorrect length for infinity encoding");
            case (byte) 2:
            case (byte) 3:
                if (bArr.length != fieldSize + 1) {
                    throw new IllegalArgumentException("Incorrect length for compressed encoding");
                }
                infinity = decompressPoint(b & 1, BigIntegers.fromUnsignedByteArray(bArr, 1, fieldSize));
                if (!infinity.satisfiesCofactor()) {
                    throw new IllegalArgumentException("Invalid point");
                }
                break;
            case (byte) 4:
                if (bArr.length == (fieldSize * 2) + 1) {
                    infinity = validatePoint(BigIntegers.fromUnsignedByteArray(bArr, 1, fieldSize), BigIntegers.fromUnsignedByteArray(bArr, fieldSize + 1, fieldSize));
                    break;
                }
                throw new IllegalArgumentException("Incorrect length for uncompressed encoding");
            case (byte) 6:
            case (byte) 7:
                if (bArr.length == (fieldSize * 2) + 1) {
                    BigInteger fromUnsignedByteArray = BigIntegers.fromUnsignedByteArray(bArr, 1, fieldSize);
                    BigInteger fromUnsignedByteArray2 = BigIntegers.fromUnsignedByteArray(bArr, fieldSize + 1, fieldSize);
                    boolean testBit = fromUnsignedByteArray2.testBit(0);
                    if (b != (byte) 7) {
                        z = false;
                    }
                    if (testBit == z) {
                        infinity = validatePoint(fromUnsignedByteArray, fromUnsignedByteArray2);
                        break;
                    }
                    throw new IllegalArgumentException("Inconsistent Y coordinate in hybrid encoding");
                }
                throw new IllegalArgumentException("Incorrect length for hybrid encoding");
            default:
                String str = "Invalid point encoding 0x";
                String valueOf = String.valueOf(Integer.toString(b, 16));
                if (valueOf.length() != 0) {
                    valueOf = str.concat(valueOf);
                } else {
                    valueOf = new String(str);
                }
                throw new IllegalArgumentException(valueOf);
        }
        if (b == (byte) 0 || !infinity.isInfinity()) {
            return infinity;
        }
        throw new IllegalArgumentException("Invalid infinity encoding");
    }

    protected void checkPoint(ECPoint eCPoint) {
        if (eCPoint == null || this != eCPoint.getCurve()) {
            throw new IllegalArgumentException("'point' must be non-null and on this curve");
        }
    }

    protected void checkPoints(ECPoint[] eCPointArr, int i, int i2) {
        if (eCPointArr == null) {
            throw new IllegalArgumentException("'points' cannot be null");
        } else if (i < 0 || i2 < 0 || i > eCPointArr.length - i2) {
            throw new IllegalArgumentException("invalid range specified for 'points'");
        } else {
            int i3 = 0;
            while (i3 < i2) {
                ECPoint eCPoint = eCPointArr[i + i3];
                if (eCPoint == null || this == eCPoint.getCurve()) {
                    i3++;
                } else {
                    throw new IllegalArgumentException("'points' entries must be null or on this curve");
                }
            }
        }
    }

    public boolean equals(ECCurve eCCurve) {
        return this == eCCurve || (eCCurve != null && getField().equals(eCCurve.getField()) && getA().toBigInteger().equals(eCCurve.getA().toBigInteger()) && getB().toBigInteger().equals(eCCurve.getB().toBigInteger()));
    }

    public boolean equals(Object obj) {
        return this == obj || ((obj instanceof ECCurve) && equals((ECCurve) obj));
    }

    public int hashCode() {
        return (getField().hashCode() ^ Integers.rotateLeft(getA().toBigInteger().hashCode(), 8)) ^ Integers.rotateLeft(getB().toBigInteger().hashCode(), 16);
    }

    protected ECPoint decompressPoint(int i, BigInteger bigInteger) {
        ECFieldElement fromBigInteger = fromBigInteger(bigInteger);
        ECFieldElement sqrt = fromBigInteger.square().add(this.a).multiply(fromBigInteger).add(this.b).sqrt();
        if (sqrt == null) {
            throw new IllegalArgumentException("Invalid point compression");
        }
        if (sqrt.testBitZero() != (i == 1)) {
            sqrt = sqrt.negate();
        }
        return createRawPoint(fromBigInteger, sqrt, true);
    }

    private static FiniteField buildField(int i, int i2, int i3, int i4) {
        if (i2 == 0) {
            throw new IllegalArgumentException("k1 must be > 0");
        } else if (i3 == 0) {
            if (i4 != 0) {
                throw new IllegalArgumentException("k3 must be 0 if k2 == 0");
            }
            return FiniteFields.getBinaryExtensionField(new int[]{0, i2, i});
        } else if (i3 <= i2) {
            throw new IllegalArgumentException("k2 must be > k1");
        } else if (i4 <= i3) {
            throw new IllegalArgumentException("k3 must be > k2");
        } else {
            return FiniteFields.getBinaryExtensionField(new int[]{0, i2, i3, i4, i});
        }
    }

    protected ECCurve(int i, int i2, int i3, int i4) {
        this(buildField(i, i2, i3, i4));
    }

    protected ECCurve(BigInteger bigInteger) {
        this(FiniteFields.getPrimeField(bigInteger));
    }
}
