package org.bouncycastle.math.ec;

import java.math.BigInteger;

class SimpleBigDecimal {
    private final BigInteger bigInt;
    private final int scale;

    public SimpleBigDecimal(BigInteger bigInteger, int i) {
        if (i < 0) {
            throw new IllegalArgumentException("scale may not be negative");
        }
        this.bigInt = bigInteger;
        this.scale = i;
    }

    private void checkScale(SimpleBigDecimal simpleBigDecimal) {
        if (this.scale != simpleBigDecimal.scale) {
            throw new IllegalArgumentException("Only SimpleBigDecimal of same scale allowed in arithmetic operations");
        }
    }

    public SimpleBigDecimal adjustScale(int i) {
        if (i >= 0) {
            return i == this.scale ? this : new SimpleBigDecimal(this.bigInt.shiftLeft(i - this.scale), i);
        } else {
            throw new IllegalArgumentException("scale may not be negative");
        }
    }

    public SimpleBigDecimal add(SimpleBigDecimal simpleBigDecimal) {
        checkScale(simpleBigDecimal);
        return new SimpleBigDecimal(this.bigInt.add(simpleBigDecimal.bigInt), this.scale);
    }

    public SimpleBigDecimal negate() {
        return new SimpleBigDecimal(this.bigInt.negate(), this.scale);
    }

    public SimpleBigDecimal subtract(SimpleBigDecimal simpleBigDecimal) {
        return add(simpleBigDecimal.negate());
    }

    public SimpleBigDecimal subtract(BigInteger bigInteger) {
        return new SimpleBigDecimal(this.bigInt.subtract(bigInteger.shiftLeft(this.scale)), this.scale);
    }

    public int compareTo(BigInteger bigInteger) {
        return this.bigInt.compareTo(bigInteger.shiftLeft(this.scale));
    }

    public BigInteger floor() {
        return this.bigInt.shiftRight(this.scale);
    }

    public BigInteger round() {
        return add(new SimpleBigDecimal(ECConstants.ONE, 1).adjustScale(this.scale)).floor();
    }

    public int getScale() {
        return this.scale;
    }

    public String toString() {
        if (this.scale == 0) {
            return this.bigInt.toString();
        }
        int i;
        BigInteger floor = floor();
        BigInteger subtract = this.bigInt.subtract(floor.shiftLeft(this.scale));
        if (this.bigInt.signum() == -1) {
            subtract = ECConstants.ONE.shiftLeft(this.scale).subtract(subtract);
        }
        if (floor.signum() == -1 && !subtract.equals(ECConstants.ZERO)) {
            floor = floor.add(ECConstants.ONE);
        }
        String bigInteger = floor.toString();
        char[] cArr = new char[this.scale];
        String bigInteger2 = subtract.toString(2);
        int length = bigInteger2.length();
        int i2 = this.scale - length;
        for (i = 0; i < i2; i++) {
            cArr[i] = '0';
        }
        for (i = 0; i < length; i++) {
            cArr[i2 + i] = bigInteger2.charAt(i);
        }
        String str = new String(cArr);
        StringBuffer stringBuffer = new StringBuffer(bigInteger);
        stringBuffer.append(".");
        stringBuffer.append(str);
        return stringBuffer.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SimpleBigDecimal)) {
            return false;
        }
        SimpleBigDecimal simpleBigDecimal = (SimpleBigDecimal) obj;
        if (this.bigInt.equals(simpleBigDecimal.bigInt) && this.scale == simpleBigDecimal.scale) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.bigInt.hashCode() ^ this.scale;
    }
}
