package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat384;

public class SecP384R1Field {
    static final int[] P = new int[]{-1, 0, 0, -1, -2, -1, -1, -1, -1, -1, -1, -1};
    static final int[] PExt = new int[]{1, -2, 0, 2, 0, -2, 0, 2, 1, 0, 0, 0, -2, 1, 0, -2, -3, -1, -1, -1, -1, -1, -1, -1};
    private static final int[] PExtInv = new int[]{-1, 1, -1, -3, -1, 1, -1, -3, -2, -1, -1, -1, 1, -2, -1, 1, 2};

    public static void add(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat.add(12, iArr, iArr2, iArr3) != 0 || (iArr3[11] == -1 && Nat.gte(12, iArr3, P))) {
            addPInvTo(iArr3);
        }
    }

    public static void addExt(int[] iArr, int[] iArr2, int[] iArr3) {
        if ((Nat.add(24, iArr, iArr2, iArr3) != 0 || (iArr3[23] == -1 && Nat.gte(24, iArr3, PExt))) && Nat.addTo(PExtInv.length, PExtInv, iArr3) != 0) {
            Nat.incAt(24, iArr3, PExtInv.length);
        }
    }

    public static void addOne(int[] iArr, int[] iArr2) {
        if (Nat.inc(12, iArr, iArr2) != 0 || (iArr2[11] == -1 && Nat.gte(12, iArr2, P))) {
            addPInvTo(iArr2);
        }
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] fromBigInteger = Nat.fromBigInteger(384, bigInteger);
        if (fromBigInteger[11] == -1 && Nat.gte(12, fromBigInteger, P)) {
            Nat.subFrom(12, P, fromBigInteger);
        }
        return fromBigInteger;
    }

    public static void multiply(int[] iArr, int[] iArr2, int[] iArr3) {
        int[] create = Nat.create(24);
        Nat384.mul(iArr, iArr2, create);
        reduce(create, iArr3);
    }

    public static void negate(int[] iArr, int[] iArr2) {
        if (Nat.isZero(12, iArr)) {
            Nat.zero(12, iArr2);
        } else {
            Nat.sub(12, P, iArr, iArr2);
        }
    }

    public static void reduce(int[] iArr, int[] iArr2) {
        long j = ((long) iArr[16]) & 4294967295L;
        long j2 = ((long) iArr[17]) & 4294967295L;
        long j3 = ((long) iArr[18]) & 4294967295L;
        long j4 = ((long) iArr[19]) & 4294967295L;
        long j5 = ((long) iArr[20]) & 4294967295L;
        long j6 = ((long) iArr[21]) & 4294967295L;
        long j7 = ((long) iArr[22]) & 4294967295L;
        long j8 = ((long) iArr[23]) & 4294967295L;
        long j9 = ((((long) iArr[12]) & 4294967295L) + j5) - 1;
        long j10 = (((long) iArr[13]) & 4294967295L) + j7;
        long j11 = ((((long) iArr[14]) & 4294967295L) + j7) + j8;
        long j12 = (((long) iArr[15]) & 4294967295L) + j8;
        long j13 = j2 + j6;
        long j14 = j6 - j8;
        j7 -= j8;
        long j15 = 0 + (((((long) iArr[0]) & 4294967295L) + j9) + j14);
        iArr2[0] = (int) j15;
        j8 = (((j8 + (((long) iArr[1]) & 4294967295L)) - j9) + j10) + (j15 >> 32);
        iArr2[1] = (int) j8;
        j8 = (j8 >> 32) + ((((((long) iArr[2]) & 4294967295L) - j6) - j10) + j11);
        iArr2[2] = (int) j8;
        j8 = (j8 >> 32) + (((((((long) iArr[3]) & 4294967295L) + j9) - j11) + j12) + j14);
        iArr2[3] = (int) j8;
        j6 = (((((j6 + ((((long) iArr[4]) & 4294967295L) + j)) + j9) + j10) - j12) + j14) + (j8 >> 32);
        iArr2[4] = (int) j6;
        j6 = (j6 >> 32) + (((((((long) iArr[5]) & 4294967295L) - j) + j10) + j11) + j13);
        iArr2[5] = (int) j6;
        j6 = (j6 >> 32) + (((((((long) iArr[6]) & 4294967295L) + j3) - j2) + j11) + j12);
        iArr2[6] = (int) j6;
        j6 = (j6 >> 32) + (((((((long) iArr[7]) & 4294967295L) + j) + j4) - j3) + j12);
        iArr2[7] = (int) j6;
        j = ((((j + (((long) iArr[8]) & 4294967295L)) + j2) + j5) - j4) + (j6 >> 32);
        iArr2[8] = (int) j;
        j = (j >> 32) + ((((((long) iArr[9]) & 4294967295L) + j3) - j5) + j13);
        iArr2[9] = (int) j;
        j = (j >> 32) + (((((((long) iArr[10]) & 4294967295L) + j3) + j4) - j14) + j7);
        iArr2[10] = (int) j;
        j = (j >> 32) + ((((((long) iArr[11]) & 4294967295L) + j4) + j5) - j7);
        iArr2[11] = (int) j;
        reduce32((int) ((j >> 32) + 1), iArr2);
    }

    public static void reduce32(int i, int[] iArr) {
        long j;
        if (i != 0) {
            long j2 = ((long) i) & 4294967295L;
            j = ((((long) iArr[0]) & 4294967295L) + j2) + 0;
            iArr[0] = (int) j;
            j = (j >> 32) + ((((long) iArr[1]) & 4294967295L) - j2);
            iArr[1] = (int) j;
            j >>= 32;
            if (j != 0) {
                j += ((long) iArr[2]) & 4294967295L;
                iArr[2] = (int) j;
                j >>= 32;
            }
            j += (((long) iArr[3]) & 4294967295L) + j2;
            iArr[3] = (int) j;
            j = (j >> 32) + (j2 + (((long) iArr[4]) & 4294967295L));
            iArr[4] = (int) j;
            j >>= 32;
        } else {
            j = 0;
        }
        if ((j != 0 && Nat.incAt(12, iArr, 5) != 0) || (iArr[11] == -1 && Nat.gte(12, iArr, P))) {
            addPInvTo(iArr);
        }
    }

    public static void square(int[] iArr, int[] iArr2) {
        int[] create = Nat.create(24);
        Nat384.square(iArr, create);
        reduce(create, iArr2);
    }

    public static void squareN(int[] iArr, int i, int[] iArr2) {
        int[] create = Nat.create(24);
        Nat384.square(iArr, create);
        reduce(create, iArr2);
        while (true) {
            i--;
            if (i > 0) {
                Nat384.square(iArr2, create);
                reduce(create, iArr2);
            } else {
                return;
            }
        }
    }

    public static void subtract(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat.sub(12, iArr, iArr2, iArr3) != 0) {
            subPInvFrom(iArr3);
        }
    }

    public static void twice(int[] iArr, int[] iArr2) {
        if (Nat.shiftUpBit(12, iArr, 0, iArr2) != 0 || (iArr2[11] == -1 && Nat.gte(12, iArr2, P))) {
            addPInvTo(iArr2);
        }
    }

    private static void addPInvTo(int[] iArr) {
        long j = (((long) iArr[0]) & 4294967295L) + 1;
        iArr[0] = (int) j;
        j = (j >> 32) + ((((long) iArr[1]) & 4294967295L) - 1);
        iArr[1] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[2]) & 4294967295L;
            iArr[2] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[3]) & 4294967295L) + 1;
        iArr[3] = (int) j;
        j = (j >> 32) + ((((long) iArr[4]) & 4294967295L) + 1);
        iArr[4] = (int) j;
        if ((j >> 32) != 0) {
            Nat.incAt(12, iArr, 5);
        }
    }

    private static void subPInvFrom(int[] iArr) {
        long j = (((long) iArr[0]) & 4294967295L) - 1;
        iArr[0] = (int) j;
        j = (j >> 32) + ((((long) iArr[1]) & 4294967295L) + 1);
        iArr[1] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[2]) & 4294967295L;
            iArr[2] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[3]) & 4294967295L) - 1;
        iArr[3] = (int) j;
        j = (j >> 32) + ((((long) iArr[4]) & 4294967295L) - 1);
        iArr[4] = (int) j;
        if ((j >> 32) != 0) {
            Nat.decAt(12, iArr, 5);
        }
    }
}
