package org.bouncycastle.math.ec;

import java.math.BigInteger;

public abstract class ECMultiplier {
    protected abstract ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger);

    public ECPoint multiply(ECPoint eCPoint, BigInteger bigInteger) {
        int signum = bigInteger.signum();
        if (signum == 0 || eCPoint.isInfinity()) {
            return eCPoint.getCurve().getInfinity();
        }
        ECPoint multiplyPositive = multiplyPositive(eCPoint, bigInteger.abs());
        if (signum <= 0) {
            multiplyPositive = multiplyPositive.negate();
        }
        return ECAlgorithms.validatePoint(multiplyPositive);
    }
}
