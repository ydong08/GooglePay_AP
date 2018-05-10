package org.bouncycastle.math.raw;

import java.util.Random;

public abstract class Mod {
    public static void invert(int[] iArr, int[] iArr2, int[] iArr3) {
        int i = 0;
        int length = iArr.length;
        if (Nat.isZero(length, iArr2)) {
            throw new IllegalArgumentException("'x' cannot be 0");
        } else if (Nat.isOne(length, iArr2)) {
            System.arraycopy(iArr2, 0, iArr3, 0, length);
        } else {
            int inversionStep;
            int[] copy = Nat.copy(length, iArr2);
            int[] create = Nat.create(length);
            create[0] = 1;
            if ((copy[0] & 1) == 0) {
                inversionStep = inversionStep(iArr, copy, length, create, 0);
            } else {
                inversionStep = 0;
            }
            if (Nat.isOne(length, copy)) {
                inversionResult(iArr, inversionStep, create, iArr3);
                return;
            }
            int[] copy2 = Nat.copy(length, iArr);
            int[] create2 = Nat.create(length);
            int i2 = inversionStep;
            inversionStep = length;
            while (true) {
                if (copy[inversionStep - 1] == 0 && copy2[inversionStep - 1] == 0) {
                    inversionStep--;
                } else if (Nat.gte(inversionStep, copy, copy2)) {
                    Nat.subFrom(inversionStep, copy2, copy);
                    i2 = inversionStep(iArr, copy, inversionStep, create, i2 + (Nat.subFrom(length, create2, create) - i));
                    if (Nat.isOne(inversionStep, copy)) {
                        inversionResult(iArr, i2, create, iArr3);
                        return;
                    }
                } else {
                    Nat.subFrom(inversionStep, copy, copy2);
                    i = inversionStep(iArr, copy2, inversionStep, create2, i + (Nat.subFrom(length, create, create2) - i2));
                    if (Nat.isOne(inversionStep, copy2)) {
                        inversionResult(iArr, i, create2, iArr3);
                        return;
                    }
                }
            }
        }
    }

    public static int[] random(int[] iArr) {
        int length = iArr.length;
        Random random = new Random();
        int[] create = Nat.create(length);
        int i = iArr[length - 1];
        i |= i >>> 1;
        i |= i >>> 2;
        i |= i >>> 4;
        i |= i >>> 8;
        int i2 = (i >>> 16) | i;
        do {
            for (i = 0; i != length; i++) {
                create[i] = random.nextInt();
            }
            i = length - 1;
            create[i] = create[i] & i2;
        } while (Nat.gte(length, create, iArr));
        return create;
    }

    private static void inversionResult(int[] iArr, int i, int[] iArr2, int[] iArr3) {
        if (i < 0) {
            Nat.add(iArr.length, iArr2, iArr, iArr3);
        } else {
            System.arraycopy(iArr2, 0, iArr3, 0, iArr.length);
        }
    }

    private static int inversionStep(int[] iArr, int[] iArr2, int i, int[] iArr3, int i2) {
        int length = iArr.length;
        int i3 = 0;
        while (iArr2[0] == 0) {
            Nat.shiftDownWord(i, iArr2, 0);
            i3 += 32;
        }
        int trailingZeroes = getTrailingZeroes(iArr2[0]);
        if (trailingZeroes > 0) {
            Nat.shiftDownBits(i, iArr2, trailingZeroes, 0);
            i3 += trailingZeroes;
        }
        trailingZeroes = i2;
        for (int i4 = 0; i4 < i3; i4++) {
            if ((iArr3[0] & 1) != 0) {
                if (trailingZeroes < 0) {
                    trailingZeroes += Nat.addTo(length, iArr, iArr3);
                } else {
                    trailingZeroes += Nat.subFrom(length, iArr, iArr3);
                }
            }
            Nat.shiftDownBit(length, iArr3, trailingZeroes);
        }
        return trailingZeroes;
    }

    private static int getTrailingZeroes(int i) {
        int i2 = 0;
        while ((i & 1) == 0) {
            i >>>= 1;
            i2++;
        }
        return i2;
    }
}
