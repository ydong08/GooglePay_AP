package org.bouncycastle.math.ec;

import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

public abstract class ECFieldElement implements ECConstants {

    public static class F2m extends ECFieldElement {
        private int[] ks;
        private int m;
        private int representation;
        private LongArray x;

        public F2m(int i, int i2, int i3, int i4, BigInteger bigInteger) {
            if (i3 == 0 && i4 == 0) {
                this.representation = 2;
                this.ks = new int[]{i2};
            } else if (i3 >= i4) {
                throw new IllegalArgumentException("k2 must be smaller than k3");
            } else if (i3 <= 0) {
                throw new IllegalArgumentException("k2 must be larger than 0");
            } else {
                this.representation = 3;
                this.ks = new int[]{i2, i3, i4};
            }
            this.m = i;
            this.x = new LongArray(bigInteger);
        }

        private F2m(int i, int[] iArr, LongArray longArray) {
            this.m = i;
            this.representation = iArr.length == 1 ? 2 : 3;
            this.ks = iArr;
            this.x = longArray;
        }

        public int bitLength() {
            return this.x.degree();
        }

        public boolean isOne() {
            return this.x.isOne();
        }

        public boolean isZero() {
            return this.x.isZero();
        }

        public boolean testBitZero() {
            return this.x.testBitZero();
        }

        public BigInteger toBigInteger() {
            return this.x.toBigInteger();
        }

        public int getFieldSize() {
            return this.m;
        }

        public static void checkFieldElements(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            if ((eCFieldElement instanceof F2m) && (eCFieldElement2 instanceof F2m)) {
                F2m f2m = (F2m) eCFieldElement;
                F2m f2m2 = (F2m) eCFieldElement2;
                if (f2m.representation != f2m2.representation) {
                    throw new IllegalArgumentException("One of the F2m field elements has incorrect representation");
                } else if (f2m.m != f2m2.m || !Arrays.areEqual(f2m.ks, f2m2.ks)) {
                    throw new IllegalArgumentException("Field elements are not elements of the same field F2m");
                } else {
                    return;
                }
            }
            throw new IllegalArgumentException("Field elements are not both instances of ECFieldElement.F2m");
        }

        public ECFieldElement add(ECFieldElement eCFieldElement) {
            LongArray longArray = (LongArray) this.x.clone();
            longArray.addShiftedByWords(((F2m) eCFieldElement).x, 0);
            return new F2m(this.m, this.ks, longArray);
        }

        public ECFieldElement addOne() {
            return new F2m(this.m, this.ks, this.x.addOne());
        }

        public ECFieldElement subtract(ECFieldElement eCFieldElement) {
            return add(eCFieldElement);
        }

        public ECFieldElement multiply(ECFieldElement eCFieldElement) {
            return new F2m(this.m, this.ks, this.x.modMultiply(((F2m) eCFieldElement).x, this.m, this.ks));
        }

        public ECFieldElement multiplyMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
            return multiplyPlusProduct(eCFieldElement, eCFieldElement2, eCFieldElement3);
        }

        public ECFieldElement multiplyPlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
            LongArray longArray = this.x;
            LongArray longArray2 = ((F2m) eCFieldElement).x;
            LongArray longArray3 = ((F2m) eCFieldElement2).x;
            LongArray longArray4 = ((F2m) eCFieldElement3).x;
            LongArray multiply = longArray.multiply(longArray2, this.m, this.ks);
            longArray3 = longArray3.multiply(longArray4, this.m, this.ks);
            if (multiply == longArray || multiply == longArray2) {
                multiply = (LongArray) multiply.clone();
            }
            multiply.addShiftedByWords(longArray3, 0);
            multiply.reduce(this.m, this.ks);
            return new F2m(this.m, this.ks, multiply);
        }

        public ECFieldElement divide(ECFieldElement eCFieldElement) {
            return multiply(eCFieldElement.invert());
        }

        public ECFieldElement negate() {
            return this;
        }

        public ECFieldElement square() {
            return new F2m(this.m, this.ks, this.x.modSquare(this.m, this.ks));
        }

        public ECFieldElement squarePlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            LongArray longArray = this.x;
            LongArray longArray2 = ((F2m) eCFieldElement).x;
            LongArray longArray3 = ((F2m) eCFieldElement2).x;
            LongArray square = longArray.square(this.m, this.ks);
            longArray2 = longArray2.multiply(longArray3, this.m, this.ks);
            if (square == longArray) {
                square = (LongArray) square.clone();
            }
            square.addShiftedByWords(longArray2, 0);
            square.reduce(this.m, this.ks);
            return new F2m(this.m, this.ks, square);
        }

        public ECFieldElement invert() {
            return new F2m(this.m, this.ks, this.x.modInverse(this.m, this.ks));
        }

        public ECFieldElement sqrt() {
            LongArray longArray = this.x;
            if (longArray.isOne() || longArray.isZero()) {
                return this;
            }
            return new F2m(this.m, this.ks, longArray.modSquareN(this.m - 1, this.m, this.ks));
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof F2m)) {
                return false;
            }
            F2m f2m = (F2m) obj;
            if (this.m == f2m.m && this.representation == f2m.representation && Arrays.areEqual(this.ks, f2m.ks) && this.x.equals(f2m.x)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.x.hashCode() ^ this.m) ^ Arrays.hashCode(this.ks);
        }
    }

    public static class Fp extends ECFieldElement {
        BigInteger q;
        BigInteger r;
        BigInteger x;

        static BigInteger calculateResidue(BigInteger bigInteger) {
            int bitLength = bigInteger.bitLength();
            if (bitLength < 96 || bigInteger.shiftRight(bitLength - 64).longValue() != -1) {
                return null;
            }
            return ONE.shiftLeft(bitLength).subtract(bigInteger);
        }

        public Fp(BigInteger bigInteger, BigInteger bigInteger2) {
            this(bigInteger, calculateResidue(bigInteger), bigInteger2);
        }

        Fp(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            if (bigInteger3 == null || bigInteger3.signum() < 0 || bigInteger3.compareTo(bigInteger) >= 0) {
                throw new IllegalArgumentException("x value invalid in Fp field element");
            }
            this.q = bigInteger;
            this.r = bigInteger2;
            this.x = bigInteger3;
        }

        public BigInteger toBigInteger() {
            return this.x;
        }

        public int getFieldSize() {
            return this.q.bitLength();
        }

        public ECFieldElement add(ECFieldElement eCFieldElement) {
            return new Fp(this.q, this.r, modAdd(this.x, eCFieldElement.toBigInteger()));
        }

        public ECFieldElement addOne() {
            BigInteger add = this.x.add(ECConstants.ONE);
            if (add.compareTo(this.q) == 0) {
                add = ECConstants.ZERO;
            }
            return new Fp(this.q, this.r, add);
        }

        public ECFieldElement subtract(ECFieldElement eCFieldElement) {
            return new Fp(this.q, this.r, modSubtract(this.x, eCFieldElement.toBigInteger()));
        }

        public ECFieldElement multiply(ECFieldElement eCFieldElement) {
            return new Fp(this.q, this.r, modMult(this.x, eCFieldElement.toBigInteger()));
        }

        public ECFieldElement multiplyMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
            BigInteger bigInteger = this.x;
            BigInteger toBigInteger = eCFieldElement.toBigInteger();
            BigInteger toBigInteger2 = eCFieldElement2.toBigInteger();
            BigInteger toBigInteger3 = eCFieldElement3.toBigInteger();
            return new Fp(this.q, this.r, modReduce(bigInteger.multiply(toBigInteger).subtract(toBigInteger2.multiply(toBigInteger3))));
        }

        public ECFieldElement multiplyPlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
            BigInteger bigInteger = this.x;
            BigInteger toBigInteger = eCFieldElement.toBigInteger();
            BigInteger toBigInteger2 = eCFieldElement2.toBigInteger();
            BigInteger toBigInteger3 = eCFieldElement3.toBigInteger();
            return new Fp(this.q, this.r, modReduce(bigInteger.multiply(toBigInteger).add(toBigInteger2.multiply(toBigInteger3))));
        }

        public ECFieldElement divide(ECFieldElement eCFieldElement) {
            return new Fp(this.q, this.r, modMult(this.x, modInverse(eCFieldElement.toBigInteger())));
        }

        public ECFieldElement negate() {
            return this.x.signum() == 0 ? this : new Fp(this.q, this.r, this.q.subtract(this.x));
        }

        public ECFieldElement square() {
            return new Fp(this.q, this.r, modMult(this.x, this.x));
        }

        public ECFieldElement squarePlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            BigInteger bigInteger = this.x;
            BigInteger toBigInteger = eCFieldElement.toBigInteger();
            BigInteger toBigInteger2 = eCFieldElement2.toBigInteger();
            return new Fp(this.q, this.r, modReduce(bigInteger.multiply(bigInteger).add(toBigInteger.multiply(toBigInteger2))));
        }

        public ECFieldElement invert() {
            return new Fp(this.q, this.r, modInverse(this.x));
        }

        public ECFieldElement sqrt() {
            if (isZero() || isOne()) {
                return this;
            }
            if (!this.q.testBit(0)) {
                throw new RuntimeException("not done yet");
            } else if (this.q.testBit(1)) {
                return checkSqrt(new Fp(this.q, this.r, this.x.modPow(this.q.shiftRight(2).add(ECConstants.ONE), this.q)));
            } else if (this.q.testBit(2)) {
                BigInteger modPow = this.x.modPow(this.q.shiftRight(3), this.q);
                r1 = modMult(modPow, this.x);
                if (modMult(r1, modPow).equals(ECConstants.ONE)) {
                    return checkSqrt(new Fp(this.q, this.r, r1));
                }
                return checkSqrt(new Fp(this.q, this.r, modMult(r1, ECConstants.TWO.modPow(this.q.shiftRight(2), this.q))));
            } else {
                r1 = this.q.shiftRight(1);
                if (!this.x.modPow(r1, this.q).equals(ECConstants.ONE)) {
                    return null;
                }
                BigInteger bigInteger = this.x;
                BigInteger modDouble = modDouble(modDouble(bigInteger));
                BigInteger add = r1.add(ECConstants.ONE);
                BigInteger subtract = this.q.subtract(ECConstants.ONE);
                Random random = new Random();
                while (true) {
                    BigInteger bigInteger2 = new BigInteger(this.q.bitLength(), random);
                    if (bigInteger2.compareTo(this.q) < 0 && modReduce(bigInteger2.multiply(bigInteger2).subtract(modDouble)).modPow(r1, this.q).equals(subtract)) {
                        BigInteger[] lucasSequence = lucasSequence(bigInteger2, bigInteger, add);
                        BigInteger bigInteger3 = lucasSequence[0];
                        bigInteger2 = lucasSequence[1];
                        if (modMult(bigInteger2, bigInteger2).equals(modDouble)) {
                            return new Fp(this.q, this.r, modHalfAbs(bigInteger2));
                        }
                        if (!(bigInteger3.equals(ECConstants.ONE) || bigInteger3.equals(subtract))) {
                            return null;
                        }
                    }
                }
            }
        }

        private ECFieldElement checkSqrt(ECFieldElement eCFieldElement) {
            return eCFieldElement.square().equals(this) ? eCFieldElement : null;
        }

        private BigInteger[] lucasSequence(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            int bitLength = bigInteger3.bitLength();
            int lowestSetBit = bigInteger3.getLowestSetBit();
            BigInteger bigInteger4 = ECConstants.ONE;
            BigInteger bigInteger5 = ECConstants.TWO;
            BigInteger bigInteger6 = ECConstants.ONE;
            int i = bitLength - 1;
            BigInteger bigInteger7 = bigInteger;
            BigInteger bigInteger8 = bigInteger5;
            BigInteger bigInteger9 = ECConstants.ONE;
            BigInteger bigInteger10 = bigInteger6;
            while (i >= lowestSetBit + 1) {
                bigInteger10 = modMult(bigInteger10, bigInteger9);
                if (bigInteger3.testBit(i)) {
                    bigInteger9 = modMult(bigInteger10, bigInteger2);
                    bigInteger4 = modMult(bigInteger4, bigInteger7);
                    bigInteger5 = modReduce(bigInteger7.multiply(bigInteger8).subtract(bigInteger.multiply(bigInteger10)));
                    bigInteger6 = modReduce(bigInteger7.multiply(bigInteger7).subtract(bigInteger9.shiftLeft(1)));
                } else {
                    bigInteger5 = modReduce(bigInteger4.multiply(bigInteger8).subtract(bigInteger10));
                    bigInteger9 = modReduce(bigInteger7.multiply(bigInteger8).subtract(bigInteger.multiply(bigInteger10)));
                    bigInteger4 = bigInteger5;
                    bigInteger5 = modReduce(bigInteger8.multiply(bigInteger8).subtract(bigInteger10.shiftLeft(1)));
                    bigInteger6 = bigInteger9;
                    bigInteger9 = bigInteger10;
                }
                i--;
                bigInteger7 = bigInteger6;
                bigInteger8 = bigInteger5;
            }
            bigInteger9 = modMult(bigInteger10, bigInteger9);
            bigInteger5 = modMult(bigInteger9, bigInteger2);
            bigInteger6 = modReduce(bigInteger4.multiply(bigInteger8).subtract(bigInteger9));
            bigInteger10 = modReduce(bigInteger7.multiply(bigInteger8).subtract(bigInteger.multiply(bigInteger9)));
            bigInteger9 = modMult(bigInteger9, bigInteger5);
            bigInteger5 = bigInteger6;
            bigInteger6 = bigInteger10;
            bigInteger10 = bigInteger9;
            for (bitLength = 1; bitLength <= lowestSetBit; bitLength++) {
                bigInteger5 = modMult(bigInteger5, bigInteger6);
                bigInteger6 = modReduce(bigInteger6.multiply(bigInteger6).subtract(bigInteger10.shiftLeft(1)));
                bigInteger10 = modMult(bigInteger10, bigInteger10);
            }
            return new BigInteger[]{bigInteger5, bigInteger6};
        }

        protected BigInteger modAdd(BigInteger bigInteger, BigInteger bigInteger2) {
            BigInteger add = bigInteger.add(bigInteger2);
            if (add.compareTo(this.q) >= 0) {
                return add.subtract(this.q);
            }
            return add;
        }

        protected BigInteger modDouble(BigInteger bigInteger) {
            BigInteger shiftLeft = bigInteger.shiftLeft(1);
            if (shiftLeft.compareTo(this.q) >= 0) {
                return shiftLeft.subtract(this.q);
            }
            return shiftLeft;
        }

        protected BigInteger modHalfAbs(BigInteger bigInteger) {
            if (bigInteger.testBit(0)) {
                bigInteger = this.q.subtract(bigInteger);
            }
            return bigInteger.shiftRight(1);
        }

        protected BigInteger modInverse(BigInteger bigInteger) {
            int fieldSize = getFieldSize();
            int i = (fieldSize + 31) >> 5;
            int[] fromBigInteger = Nat.fromBigInteger(fieldSize, this.q);
            int[] fromBigInteger2 = Nat.fromBigInteger(fieldSize, bigInteger);
            int[] create = Nat.create(i);
            Mod.invert(fromBigInteger, fromBigInteger2, create);
            return Nat.toBigInteger(i, create);
        }

        protected BigInteger modMult(BigInteger bigInteger, BigInteger bigInteger2) {
            return modReduce(bigInteger.multiply(bigInteger2));
        }

        protected BigInteger modReduce(BigInteger bigInteger) {
            if (this.r == null) {
                return bigInteger.mod(this.q);
            }
            Object obj;
            if (bigInteger.signum() < 0) {
                obj = 1;
            } else {
                obj = null;
            }
            if (obj != null) {
                bigInteger = bigInteger.abs();
            }
            int bitLength = this.q.bitLength();
            boolean equals = this.r.equals(ECConstants.ONE);
            while (bigInteger.bitLength() > bitLength + 1) {
                BigInteger shiftRight = bigInteger.shiftRight(bitLength);
                BigInteger subtract = bigInteger.subtract(shiftRight.shiftLeft(bitLength));
                if (!equals) {
                    shiftRight = shiftRight.multiply(this.r);
                }
                bigInteger = shiftRight.add(subtract);
            }
            while (bigInteger.compareTo(this.q) >= 0) {
                bigInteger = bigInteger.subtract(this.q);
            }
            if (obj == null || bigInteger.signum() == 0) {
                return bigInteger;
            }
            return this.q.subtract(bigInteger);
        }

        protected BigInteger modSubtract(BigInteger bigInteger, BigInteger bigInteger2) {
            BigInteger subtract = bigInteger.subtract(bigInteger2);
            if (subtract.signum() < 0) {
                return subtract.add(this.q);
            }
            return subtract;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Fp)) {
                return false;
            }
            Fp fp = (Fp) obj;
            if (this.q.equals(fp.q) && this.x.equals(fp.x)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.q.hashCode() ^ this.x.hashCode();
        }
    }

    public abstract ECFieldElement add(ECFieldElement eCFieldElement);

    public abstract ECFieldElement addOne();

    public abstract ECFieldElement divide(ECFieldElement eCFieldElement);

    public abstract int getFieldSize();

    public abstract ECFieldElement invert();

    public abstract ECFieldElement multiply(ECFieldElement eCFieldElement);

    public abstract ECFieldElement negate();

    public abstract ECFieldElement sqrt();

    public abstract ECFieldElement square();

    public abstract ECFieldElement subtract(ECFieldElement eCFieldElement);

    public abstract BigInteger toBigInteger();

    public int bitLength() {
        return toBigInteger().bitLength();
    }

    public boolean isOne() {
        return bitLength() == 1;
    }

    public boolean isZero() {
        return toBigInteger().signum() == 0;
    }

    public ECFieldElement multiplyMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
        return multiply(eCFieldElement).subtract(eCFieldElement2.multiply(eCFieldElement3));
    }

    public ECFieldElement multiplyPlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
        return multiply(eCFieldElement).add(eCFieldElement2.multiply(eCFieldElement3));
    }

    public ECFieldElement squarePlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return square().add(eCFieldElement.multiply(eCFieldElement2));
    }

    public boolean testBitZero() {
        return toBigInteger().testBit(0);
    }

    public String toString() {
        return toBigInteger().toString(16);
    }

    public byte[] getEncoded() {
        return BigIntegers.asUnsignedByteArray((getFieldSize() + 7) / 8, toBigInteger());
    }
}
