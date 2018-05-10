package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat256;

public class SecP256R1Field {
    static final int[] P = new int[]{-1, -1, -1, 0, 0, 0, 1, -1};
    static final int[] PExt = new int[]{1, 0, 0, -2, -1, -1, -2, 1, -2, 1, -2, 1, 1, -2, 2, -2};

    public static void add(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat256.add(iArr, iArr2, iArr3) != 0 || (iArr3[7] == -1 && Nat256.gte(iArr3, P))) {
            addPInvTo(iArr3);
        }
    }

    public static void addOne(int[] iArr, int[] iArr2) {
        if (Nat.inc(8, iArr, iArr2) != 0 || (iArr2[7] == -1 && Nat256.gte(iArr2, P))) {
            addPInvTo(iArr2);
        }
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] fromBigInteger = Nat256.fromBigInteger(bigInteger);
        if (fromBigInteger[7] == -1 && Nat256.gte(fromBigInteger, P)) {
            Nat256.subFrom(P, fromBigInteger);
        }
        return fromBigInteger;
    }

    public static void multiply(int[] iArr, int[] iArr2, int[] iArr3) {
        int[] createExt = Nat256.createExt();
        Nat256.mul(iArr, iArr2, createExt);
        reduce(createExt, iArr3);
    }

    public static void multiplyAddToExt(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat256.mulAddTo(iArr, iArr2, iArr3) != 0 || ((iArr3[15] & -1) == -1 && Nat.gte(16, iArr3, PExt))) {
            Nat.subFrom(16, PExt, iArr3);
        }
    }

    public static void negate(int[] iArr, int[] iArr2) {
        if (Nat256.isZero(iArr)) {
            Nat256.zero(iArr2);
        } else {
            Nat256.sub(P, iArr, iArr2);
        }
    }

    public static void reduce(int[] iArr, int[] iArr2) {
        long j = ((long) iArr[9]) & 4294967295L;
        long j2 = ((long) iArr[10]) & 4294967295L;
        long j3 = ((long) iArr[11]) & 4294967295L;
        long j4 = ((long) iArr[12]) & 4294967295L;
        long j5 = ((long) iArr[13]) & 4294967295L;
        long j6 = ((long) iArr[14]) & 4294967295L;
        long j7 = ((long) iArr[15]) & 4294967295L;
        long j8 = (((long) iArr[8]) & 4294967295L) - 6;
        long j9 = j8 + j;
        j += j2;
        j2 = (j2 + j3) - j7;
        j3 += j4;
        j4 += j5;
        long j10 = j5 + j6;
        long j11 = j6 + j7;
        long j12 = 0 + ((((((long) iArr[0]) & 4294967295L) + j9) - j3) - j10);
        iArr2[0] = (int) j12;
        j12 = (j12 >> 32) + ((((((long) iArr[1]) & 4294967295L) + j) - j4) - j11);
        iArr2[1] = (int) j12;
        j12 = (j12 >> 32) + (((((long) iArr[2]) & 4294967295L) + j2) - j10);
        iArr2[2] = (int) j12;
        j3 = (((((j3 << 1) + (((long) iArr[3]) & 4294967295L)) + j5) - j7) - j9) + (j12 >> 32);
        iArr2[3] = (int) j3;
        j = ((((((long) iArr[4]) & 4294967295L) + (j4 << 1)) + j6) - j) + (j3 >> 32);
        iArr2[4] = (int) j;
        j = (j >> 32) + (((((long) iArr[5]) & 4294967295L) + (j10 << 1)) - j2);
        iArr2[5] = (int) j;
        j = (j >> 32) + ((((((long) iArr[6]) & 4294967295L) + (j11 << 1)) + j10) - j9);
        iArr2[6] = (int) j;
        j8 = (((j8 + ((((long) iArr[7]) & 4294967295L) + (j7 << 1))) - j2) - j4) + (j >> 32);
        iArr2[7] = (int) j8;
        reduce32((int) ((j8 >> 32) + 6), iArr2);
    }

    public static void reduce32(int i, int[] iArr) {
        long j;
        if (i != 0) {
            long j2 = ((long) i) & 4294967295L;
            j = ((((long) iArr[0]) & 4294967295L) + j2) + 0;
            iArr[0] = (int) j;
            j >>= 32;
            if (j != 0) {
                j += ((long) iArr[1]) & 4294967295L;
                iArr[1] = (int) j;
                j = (j >> 32) + (((long) iArr[2]) & 4294967295L);
                iArr[2] = (int) j;
                j >>= 32;
            }
            j += (((long) iArr[3]) & 4294967295L) - j2;
            iArr[3] = (int) j;
            j >>= 32;
            if (j != 0) {
                j += ((long) iArr[4]) & 4294967295L;
                iArr[4] = (int) j;
                j = (j >> 32) + (((long) iArr[5]) & 4294967295L);
                iArr[5] = (int) j;
                j >>= 32;
            }
            j += (((long) iArr[6]) & 4294967295L) - j2;
            iArr[6] = (int) j;
            j = (j >> 32) + (j2 + (((long) iArr[7]) & 4294967295L));
            iArr[7] = (int) j;
            j >>= 32;
        } else {
            j = 0;
        }
        if (j != 0 || (iArr[7] == -1 && Nat256.gte(iArr, P))) {
            addPInvTo(iArr);
        }
    }

    public static void square(int[] iArr, int[] iArr2) {
        int[] createExt = Nat256.createExt();
        Nat256.square(iArr, createExt);
        reduce(createExt, iArr2);
    }

    public static void squareN(int[] iArr, int i, int[] iArr2) {
        int[] createExt = Nat256.createExt();
        Nat256.square(iArr, createExt);
        reduce(createExt, iArr2);
        while (true) {
            i--;
            if (i > 0) {
                Nat256.square(iArr2, createExt);
                reduce(createExt, iArr2);
            } else {
                return;
            }
        }
    }

    public static void subtract(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat256.sub(iArr, iArr2, iArr3) != 0) {
            subPInvFrom(iArr3);
        }
    }

    public static void twice(int[] iArr, int[] iArr2) {
        if (Nat.shiftUpBit(8, iArr, 0, iArr2) != 0 || (iArr2[7] == -1 && Nat256.gte(iArr2, P))) {
            addPInvTo(iArr2);
        }
    }

    private static void addPInvTo(int[] iArr) {
        long j = (((long) iArr[0]) & 4294967295L) + 1;
        iArr[0] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[1]) & 4294967295L;
            iArr[1] = (int) j;
            j = (j >> 32) + (((long) iArr[2]) & 4294967295L);
            iArr[2] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[3]) & 4294967295L) - 1;
        iArr[3] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[4]) & 4294967295L;
            iArr[4] = (int) j;
            j = (j >> 32) + (((long) iArr[5]) & 4294967295L);
            iArr[5] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[6]) & 4294967295L) - 1;
        iArr[6] = (int) j;
        iArr[7] = (int) ((j >> 32) + ((((long) iArr[7]) & 4294967295L) + 1));
    }

    private static void subPInvFrom(int[] iArr) {
        long j = (((long) iArr[0]) & 4294967295L) - 1;
        iArr[0] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[1]) & 4294967295L;
            iArr[1] = (int) j;
            j = (j >> 32) + (((long) iArr[2]) & 4294967295L);
            iArr[2] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[3]) & 4294967295L) + 1;
        iArr[3] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[4]) & 4294967295L;
            iArr[4] = (int) j;
            j = (j >> 32) + (((long) iArr[5]) & 4294967295L);
            iArr[5] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[6]) & 4294967295L) + 1;
        iArr[6] = (int) j;
        iArr[7] = (int) ((j >> 32) + ((((long) iArr[7]) & 4294967295L) - 1));
    }
}
