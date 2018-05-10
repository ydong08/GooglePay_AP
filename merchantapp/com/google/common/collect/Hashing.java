package com.google.common.collect;

final class Hashing {
    private Hashing() {
    }

    static int smear(int i) {
        return 461845907 * Integer.rotateLeft(-862048943 * i, 15);
    }

    static int smearedHash(Object obj) {
        return smear(obj == null ? 0 : obj.hashCode());
    }

    static int closedTableSize(int i, double d) {
        int max = Math.max(i, 2);
        int highestOneBit = Integer.highestOneBit(max);
        if (max <= ((int) (((double) highestOneBit) * d))) {
            return highestOneBit;
        }
        highestOneBit <<= 1;
        if (highestOneBit > 0) {
            return highestOneBit;
        }
        return 1073741824;
    }
}
