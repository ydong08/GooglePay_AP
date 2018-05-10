package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat192;

public class SecP192R1Field {
    static final int[] P = new int[]{-1, -1, -2, -1, -1, -1};
    static final int[] PExt = new int[]{1, 0, 2, 0, 1, 0, -2, -1, -3, -1, -1, -1};
    private static final int[] PExtInv = new int[]{-1, -1, -3, -1, -2, -1, 1, 0, 2};

    public static void add(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat192.add(iArr, iArr2, iArr3) != 0 || (iArr3[5] == -1 && Nat192.gte(iArr3, P))) {
            addPInvTo(iArr3);
        }
    }

    public static void addOne(int[] iArr, int[] iArr2) {
        if (Nat.inc(6, iArr, iArr2) != 0 || (iArr2[5] == -1 && Nat192.gte(iArr2, P))) {
            addPInvTo(iArr2);
        }
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] fromBigInteger = Nat192.fromBigInteger(bigInteger);
        if (fromBigInteger[5] == -1 && Nat192.gte(fromBigInteger, P)) {
            Nat192.subFrom(P, fromBigInteger);
        }
        return fromBigInteger;
    }

    public static void multiply(int[] iArr, int[] iArr2, int[] iArr3) {
        int[] createExt = Nat192.createExt();
        Nat192.mul(iArr, iArr2, createExt);
        reduce(createExt, iArr3);
    }

    public static void multiplyAddToExt(int[] iArr, int[] iArr2, int[] iArr3) {
        if ((Nat192.mulAddTo(iArr, iArr2, iArr3) != 0 || (iArr3[11] == -1 && Nat.gte(12, iArr3, PExt))) && Nat.addTo(PExtInv.length, PExtInv, iArr3) != 0) {
            Nat.incAt(12, iArr3, PExtInv.length);
        }
    }

    public static void negate(int[] iArr, int[] iArr2) {
        if (Nat192.isZero(iArr)) {
            Nat192.zero(iArr2);
        } else {
            Nat192.sub(P, iArr, iArr2);
        }
    }

    public static void reduce(int[] iArr, int[] iArr2) {
        long j = ((long) iArr[6]) & 4294967295L;
        long j2 = ((long) iArr[7]) & 4294967295L;
        long j3 = ((long) iArr[8]) & 4294967295L;
        long j4 = ((long) iArr[9]) & 4294967295L;
        long j5 = (((long) iArr[10]) & 4294967295L) + j;
        long j6 = (((long) iArr[11]) & 4294967295L) + j2;
        long j7 = 0 + ((((long) iArr[0]) & 4294967295L) + j5);
        int i = (int) j7;
        j7 = (j7 >> 32) + ((((long) iArr[1]) & 4294967295L) + j6);
        iArr2[1] = (int) j7;
        j3 += j5;
        j4 += j6;
        j5 = ((((long) iArr[2]) & 4294967295L) + j3) + (j7 >> 32);
        j6 = 4294967295L & j5;
        j5 = (j5 >> 32) + ((((long) iArr[3]) & 4294967295L) + j4);
        iArr2[3] = (int) j5;
        j2 = j4 - j2;
        j = ((j3 - j) + (((long) iArr[4]) & 4294967295L)) + (j5 >> 32);
        iArr2[4] = (int) j;
        j = (j >> 32) + (j2 + (((long) iArr[5]) & 4294967295L));
        iArr2[5] = (int) j;
        j2 = j >> 32;
        j = j6 + j2;
        j2 += ((long) i) & 4294967295L;
        iArr2[0] = (int) j2;
        j2 >>= 32;
        if (j2 != 0) {
            j2 += ((long) iArr2[1]) & 4294967295L;
            iArr2[1] = (int) j2;
            j += j2 >> 32;
        }
        iArr2[2] = (int) j;
        if ((j >> 32) == 0 || Nat.incAt(6, iArr2, 3) == 0) {
            if (iArr2[5] == -1) {
                if (!Nat192.gte(iArr2, P)) {
                    return;
                }
            }
            return;
        }
        addPInvTo(iArr2);
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
                j >>= 32;
            }
            j += j2 + (((long) iArr[2]) & 4294967295L);
            iArr[2] = (int) j;
            j >>= 32;
        } else {
            j = 0;
        }
        if ((j != 0 && Nat.incAt(6, iArr, 3) != 0) || (iArr[5] == -1 && Nat192.gte(iArr, P))) {
            addPInvTo(iArr);
        }
    }

    public static void square(int[] iArr, int[] iArr2) {
        int[] createExt = Nat192.createExt();
        Nat192.square(iArr, createExt);
        reduce(createExt, iArr2);
    }

    public static void squareN(int[] iArr, int i, int[] iArr2) {
        int[] createExt = Nat192.createExt();
        Nat192.square(iArr, createExt);
        reduce(createExt, iArr2);
        while (true) {
            i--;
            if (i > 0) {
                Nat192.square(iArr2, createExt);
                reduce(createExt, iArr2);
            } else {
                return;
            }
        }
    }

    public static void subtract(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat192.sub(iArr, iArr2, iArr3) != 0) {
            subPInvFrom(iArr3);
        }
    }

    public static void twice(int[] iArr, int[] iArr2) {
        if (Nat.shiftUpBit(6, iArr, 0, iArr2) != 0 || (iArr2[5] == -1 && Nat192.gte(iArr2, P))) {
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
            j >>= 32;
        }
        j += (((long) iArr[2]) & 4294967295L) + 1;
        iArr[2] = (int) j;
        if ((j >> 32) != 0) {
            Nat.incAt(6, iArr, 3);
        }
    }

    private static void subPInvFrom(int[] iArr) {
        long j = (((long) iArr[0]) & 4294967295L) - 1;
        iArr[0] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[1]) & 4294967295L;
            iArr[1] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[2]) & 4294967295L) - 1;
        iArr[2] = (int) j;
        if ((j >> 32) != 0) {
            Nat.decAt(6, iArr, 3);
        }
    }
}
