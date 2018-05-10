package org.bouncycastle.math.ec;

public class ECPointMap {
    protected final ECFieldElement scale;

    public ECPointMap(ECFieldElement eCFieldElement) {
        this.scale = eCFieldElement;
    }

    public ECPoint map(ECPoint eCPoint) {
        return eCPoint.scaleX(this.scale);
    }
}
