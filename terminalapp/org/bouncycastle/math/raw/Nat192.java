package org.bouncycastle.math.raw;

import java.math.BigInteger;
import org.bouncycastle.util.Pack;

public abstract class Nat192 {
    public static int add(int[] iArr, int[] iArr2, int[] iArr3) {
        long j = 0 + ((((long) iArr[0]) & 4294967295L) + (((long) iArr2[0]) & 4294967295L));
        iArr3[0] = (int) j;
        j = (j >>> 32) + ((((long) iArr[1]) & 4294967295L) + (((long) iArr2[1]) & 4294967295L));
        iArr3[1] = (int) j;
        j = (j >>> 32) + ((((long) iArr[2]) & 4294967295L) + (((long) iArr2[2]) & 4294967295L));
        iArr3[2] = (int) j;
        j = (j >>> 32) + ((((long) iArr[3]) & 4294967295L) + (((long) iArr2[3]) & 4294967295L));
        iArr3[3] = (int) j;
        j = (j >>> 32) + ((((long) iArr[4]) & 4294967295L) + (((long) iArr2[4]) & 4294967295L));
        iArr3[4] = (int) j;
        j = (j >>> 32) + ((((long) iArr[5]) & 4294967295L) + (((long) iArr2[5]) & 4294967295L));
        iArr3[5] = (int) j;
        return (int) (j >>> 32);
    }

    public static int addBothTo(int[] iArr, int[] iArr2, int[] iArr3) {
        long j = 0 + (((((long) iArr[0]) & 4294967295L) + (((long) iArr2[0]) & 4294967295L)) + (((long) iArr3[0]) & 4294967295L));
        iArr3[0] = (int) j;
        j = (j >>> 32) + (((((long) iArr[1]) & 4294967295L) + (((long) iArr2[1]) & 4294967295L)) + (((long) iArr3[1]) & 4294967295L));
        iArr3[1] = (int) j;
        j = (j >>> 32) + (((((long) iArr[2]) & 4294967295L) + (((long) iArr2[2]) & 4294967295L)) + (((long) iArr3[2]) & 4294967295L));
        iArr3[2] = (int) j;
        j = (j >>> 32) + (((((long) iArr[3]) & 4294967295L) + (((long) iArr2[3]) & 4294967295L)) + (((long) iArr3[3]) & 4294967295L));
        iArr3[3] = (int) j;
        j = (j >>> 32) + (((((long) iArr[4]) & 4294967295L) + (((long) iArr2[4]) & 4294967295L)) + (((long) iArr3[4]) & 4294967295L));
        iArr3[4] = (int) j;
        j = (j >>> 32) + (((((long) iArr[5]) & 4294967295L) + (((long) iArr2[5]) & 4294967295L)) + (((long) iArr3[5]) & 4294967295L));
        iArr3[5] = (int) j;
        return (int) (j >>> 32);
    }

    public static int addTo(int[] iArr, int i, int[] iArr2, int i2, int i3) {
        long j = (((long) i3) & 4294967295L) + ((((long) iArr[i + 0]) & 4294967295L) + (((long) iArr2[i2 + 0]) & 4294967295L));
        iArr2[i2 + 0] = (int) j;
        j = (j >>> 32) + ((((long) iArr[i + 1]) & 4294967295L) + (((long) iArr2[i2 + 1]) & 4294967295L));
        iArr2[i2 + 1] = (int) j;
        j = (j >>> 32) + ((((long) iArr[i + 2]) & 4294967295L) + (((long) iArr2[i2 + 2]) & 4294967295L));
        iArr2[i2 + 2] = (int) j;
        j = (j >>> 32) + ((((long) iArr[i + 3]) & 4294967295L) + (((long) iArr2[i2 + 3]) & 4294967295L));
        iArr2[i2 + 3] = (int) j;
        j = (j >>> 32) + ((((long) iArr[i + 4]) & 4294967295L) + (((long) iArr2[i2 + 4]) & 4294967295L));
        iArr2[i2 + 4] = (int) j;
        j = (j >>> 32) + ((((long) iArr[i + 5]) & 4294967295L) + (((long) iArr2[i2 + 5]) & 4294967295L));
        iArr2[i2 + 5] = (int) j;
        return (int) (j >>> 32);
    }

    public static int addToEachOther(int[] iArr, int i, int[] iArr2, int i2) {
        long j = 0 + ((((long) iArr[i + 0]) & 4294967295L) + (((long) iArr2[i2 + 0]) & 4294967295L));
        iArr[i + 0] = (int) j;
        iArr2[i2 + 0] = (int) j;
        j = (j >>> 32) + ((((long) iArr[i + 1]) & 4294967295L) + (((long) iArr2[i2 + 1]) & 4294967295L));
        iArr[i + 1] = (int) j;
        iArr2[i2 + 1] = (int) j;
        j = (j >>> 32) + ((((long) iArr[i + 2]) & 4294967295L) + (((long) iArr2[i2 + 2]) & 4294967295L));
        iArr[i + 2] = (int) j;
        iArr2[i2 + 2] = (int) j;
        j = (j >>> 32) + ((((long) iArr[i + 3]) & 4294967295L) + (((long) iArr2[i2 + 3]) & 4294967295L));
        iArr[i + 3] = (int) j;
        iArr2[i2 + 3] = (int) j;
        j = (j >>> 32) + ((((long) iArr[i + 4]) & 4294967295L) + (((long) iArr2[i2 + 4]) & 4294967295L));
        iArr[i + 4] = (int) j;
        iArr2[i2 + 4] = (int) j;
        j = (j >>> 32) + ((((long) iArr[i + 5]) & 4294967295L) + (((long) iArr2[i2 + 5]) & 4294967295L));
        iArr[i + 5] = (int) j;
        iArr2[i2 + 5] = (int) j;
        return (int) (j >>> 32);
    }

    public static int[] create() {
        return new int[6];
    }

    public static int[] createExt() {
        return new int[12];
    }

    public static boolean diff(int[] iArr, int i, int[] iArr2, int i2, int[] iArr3, int i3) {
        boolean gte = gte(iArr, i, iArr2, i2);
        if (gte) {
            sub(iArr, i, iArr2, i2, iArr3, i3);
        } else {
            sub(iArr2, i2, iArr, i, iArr3, i3);
        }
        return gte;
    }

    public static boolean eq(int[] iArr, int[] iArr2) {
        for (int i = 5; i >= 0; i--) {
            if (iArr[i] != iArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        if (bigInteger.signum() < 0 || bigInteger.bitLength() > 192) {
            throw new IllegalArgumentException();
        }
        int[] create = create();
        int i = 0;
        while (bigInteger.signum() != 0) {
            int i2 = i + 1;
            create[i] = bigInteger.intValue();
            bigInteger = bigInteger.shiftRight(32);
            i = i2;
        }
        return create;
    }

    public static int getBit(int[] iArr, int i) {
        if (i == 0) {
            return iArr[0] & 1;
        }
        int i2 = i >> 5;
        if (i2 < 0 || i2 >= 6) {
            return 0;
        }
        return (iArr[i2] >>> (i & 31)) & 1;
    }

    public static boolean gte(int[] iArr, int[] iArr2) {
        for (int i = 5; i >= 0; i--) {
            int i2 = iArr[i] ^ Integer.MIN_VALUE;
            int i3 = iArr2[i] ^ Integer.MIN_VALUE;
            if (i2 < i3) {
                return false;
            }
            if (i2 > i3) {
                return true;
            }
        }
        return true;
    }

    public static boolean gte(int[] iArr, int i, int[] iArr2, int i2) {
        for (int i3 = 5; i3 >= 0; i3--) {
            int i4 = iArr[i + i3] ^ Integer.MIN_VALUE;
            int i5 = iArr2[i2 + i3] ^ Integer.MIN_VALUE;
            if (i4 < i5) {
                return false;
            }
            if (i4 > i5) {
                return true;
            }
        }
        return true;
    }

    public static boolean isOne(int[] iArr) {
        if (iArr[0] != 1) {
            return false;
        }
        for (int i = 1; i < 6; i++) {
            if (iArr[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isZero(int[] iArr) {
        for (int i = 0; i < 6; i++) {
            if (iArr[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static void mul(int[] iArr, int[] iArr2, int[] iArr3) {
        long j = 4294967295L & ((long) iArr2[0]);
        long j2 = 4294967295L & ((long) iArr2[1]);
        long j3 = 4294967295L & ((long) iArr2[2]);
        long j4 = 4294967295L & ((long) iArr2[3]);
        long j5 = 4294967295L & ((long) iArr2[4]);
        long j6 = 4294967295L & ((long) iArr2[5]);
        long j7 = ((long) iArr[0]) & 4294967295L;
        long j8 = 0 + (j7 * j);
        iArr3[0] = (int) j8;
        j8 = (j8 >>> 32) + (j7 * j2);
        iArr3[1] = (int) j8;
        j8 = (j8 >>> 32) + (j7 * j3);
        iArr3[2] = (int) j8;
        j8 = (j8 >>> 32) + (j7 * j4);
        iArr3[3] = (int) j8;
        j8 = (j8 >>> 32) + (j7 * j5);
        iArr3[4] = (int) j8;
        j8 = (j8 >>> 32) + (j7 * j6);
        iArr3[5] = (int) j8;
        iArr3[6] = (int) (j8 >>> 32);
        for (int i = 1; i < 6; i++) {
            long j9 = ((long) iArr[i]) & 4294967295L;
            j7 = 0 + ((j9 * j) + (((long) iArr3[i + 0]) & 4294967295L));
            iArr3[i + 0] = (int) j7;
            j7 = (j7 >>> 32) + ((j9 * j2) + (((long) iArr3[i + 1]) & 4294967295L));
            iArr3[i + 1] = (int) j7;
            j7 = (j7 >>> 32) + ((j9 * j3) + (((long) iArr3[i + 2]) & 4294967295L));
            iArr3[i + 2] = (int) j7;
            j7 = (j7 >>> 32) + ((j9 * j4) + (((long) iArr3[i + 3]) & 4294967295L));
            iArr3[i + 3] = (int) j7;
            j7 = (j7 >>> 32) + ((j9 * j5) + (((long) iArr3[i + 4]) & 4294967295L));
            iArr3[i + 4] = (int) j7;
            j7 = (j7 >>> 32) + ((j9 * j6) + (((long) iArr3[i + 5]) & 4294967295L));
            iArr3[i + 5] = (int) j7;
            iArr3[i + 6] = (int) (j7 >>> 32);
        }
    }

    public static void mul(int[] iArr, int i, int[] iArr2, int i2, int[] iArr3, int i3) {
        long j = 4294967295L & ((long) iArr2[i2 + 0]);
        long j2 = 4294967295L & ((long) iArr2[i2 + 1]);
        long j3 = 4294967295L & ((long) iArr2[i2 + 2]);
        long j4 = 4294967295L & ((long) iArr2[i2 + 3]);
        long j5 = 4294967295L & ((long) iArr2[i2 + 4]);
        long j6 = 4294967295L & ((long) iArr2[i2 + 5]);
        long j7 = ((long) iArr[i + 0]) & 4294967295L;
        long j8 = 0 + (j7 * j);
        iArr3[i3 + 0] = (int) j8;
        j8 = (j8 >>> 32) + (j7 * j2);
        iArr3[i3 + 1] = (int) j8;
        j8 = (j8 >>> 32) + (j7 * j3);
        iArr3[i3 + 2] = (int) j8;
        j8 = (j8 >>> 32) + (j7 * j4);
        iArr3[i3 + 3] = (int) j8;
        j8 = (j8 >>> 32) + (j7 * j5);
        iArr3[i3 + 4] = (int) j8;
        j8 = (j8 >>> 32) + (j7 * j6);
        iArr3[i3 + 5] = (int) j8;
        iArr3[i3 + 6] = (int) (j8 >>> 32);
        for (int i4 = 1; i4 < 6; i4++) {
            i3++;
            long j9 = ((long) iArr[i + i4]) & 4294967295L;
            j7 = 0 + ((j9 * j) + (((long) iArr3[i3 + 0]) & 4294967295L));
            iArr3[i3 + 0] = (int) j7;
            j7 = (j7 >>> 32) + ((j9 * j2) + (((long) iArr3[i3 + 1]) & 4294967295L));
            iArr3[i3 + 1] = (int) j7;
            j7 = (j7 >>> 32) + ((j9 * j3) + (((long) iArr3[i3 + 2]) & 4294967295L));
            iArr3[i3 + 2] = (int) j7;
            j7 = (j7 >>> 32) + ((j9 * j4) + (((long) iArr3[i3 + 3]) & 4294967295L));
            iArr3[i3 + 3] = (int) j7;
            j7 = (j7 >>> 32) + ((j9 * j5) + (((long) iArr3[i3 + 4]) & 4294967295L));
            iArr3[i3 + 4] = (int) j7;
            j7 = (j7 >>> 32) + ((j9 * j6) + (((long) iArr3[i3 + 5]) & 4294967295L));
            iArr3[i3 + 5] = (int) j7;
            iArr3[i3 + 6] = (int) (j7 >>> 32);
        }
    }

    public static int mulAddTo(int[] iArr, int[] iArr2, int[] iArr3) {
        long j = ((long) iArr2[0]) & 4294967295L;
        long j2 = ((long) iArr2[1]) & 4294967295L;
        long j3 = ((long) iArr2[2]) & 4294967295L;
        long j4 = ((long) iArr2[3]) & 4294967295L;
        long j5 = ((long) iArr2[4]) & 4294967295L;
        long j6 = ((long) iArr2[5]) & 4294967295L;
        long j7 = 0;
        for (int i = 0; i < 6; i++) {
            long j8 = ((long) iArr[i]) & 4294967295L;
            long j9 = 0 + ((j8 * j) + (((long) iArr3[i + 0]) & 4294967295L));
            iArr3[i + 0] = (int) j9;
            j9 = (j9 >>> 32) + ((j8 * j2) + (((long) iArr3[i + 1]) & 4294967295L));
            iArr3[i + 1] = (int) j9;
            j9 = (j9 >>> 32) + ((j8 * j3) + (((long) iArr3[i + 2]) & 4294967295L));
            iArr3[i + 2] = (int) j9;
            j9 = (j9 >>> 32) + ((j8 * j4) + (((long) iArr3[i + 3]) & 4294967295L));
            iArr3[i + 3] = (int) j9;
            j9 = (j9 >>> 32) + ((j8 * j5) + (((long) iArr3[i + 4]) & 4294967295L));
            iArr3[i + 4] = (int) j9;
            j9 = (j9 >>> 32) + ((j8 * j6) + (((long) iArr3[i + 5]) & 4294967295L));
            iArr3[i + 5] = (int) j9;
            j7 = (j7 + (((long) iArr3[i + 6]) & 4294967295L)) + (j9 >>> 32);
            iArr3[i + 6] = (int) j7;
            j7 >>>= 32;
        }
        return (int) j7;
    }

    public static long mul33Add(int i, int[] iArr, int i2, int[] iArr2, int i3, int[] iArr3, int i4) {
        long j = ((long) i) & 4294967295L;
        long j2 = ((long) iArr[i2 + 0]) & 4294967295L;
        long j3 = 0 + ((j * j2) + (((long) iArr2[i3 + 0]) & 4294967295L));
        iArr3[i4 + 0] = (int) j3;
        long j4 = ((long) iArr[i2 + 1]) & 4294967295L;
        j3 = (j3 >>> 32) + ((j2 + (j * j4)) + (((long) iArr2[i3 + 1]) & 4294967295L));
        iArr3[i4 + 1] = (int) j3;
        j2 = ((long) iArr[i2 + 2]) & 4294967295L;
        j3 = (j3 >>> 32) + ((j4 + (j * j2)) + (((long) iArr2[i3 + 2]) & 4294967295L));
        iArr3[i4 + 2] = (int) j3;
        j4 = ((long) iArr[i2 + 3]) & 4294967295L;
        j3 = (j3 >>> 32) + ((j2 + (j * j4)) + (((long) iArr2[i3 + 3]) & 4294967295L));
        iArr3[i4 + 3] = (int) j3;
        j2 = ((long) iArr[i2 + 4]) & 4294967295L;
        j3 = (j3 >>> 32) + ((j4 + (j * j2)) + (((long) iArr2[i3 + 4]) & 4294967295L));
        iArr3[i4 + 4] = (int) j3;
        j4 = ((long) iArr[i2 + 5]) & 4294967295L;
        j3 = (j3 >>> 32) + (((j * j4) + j2) + (((long) iArr2[i3 + 5]) & 4294967295L));
        iArr3[i4 + 5] = (int) j3;
        return (j3 >>> 32) + j4;
    }

    public static int mul33DWordAdd(int i, long j, int[] iArr, int i2) {
        long j2 = ((long) i) & 4294967295L;
        long j3 = 4294967295L & j;
        long j4 = 0 + ((j2 * j3) + (((long) iArr[i2 + 0]) & 4294967295L));
        iArr[i2 + 0] = (int) j4;
        long j5 = j >>> 32;
        j4 = (j4 >>> 32) + (((j2 * j5) + j3) + (((long) iArr[i2 + 1]) & 4294967295L));
        iArr[i2 + 1] = (int) j4;
        j4 = (j4 >>> 32) + ((((long) iArr[i2 + 2]) & 4294967295L) + j5);
        iArr[i2 + 2] = (int) j4;
        j4 = (j4 >>> 32) + (((long) iArr[i2 + 3]) & 4294967295L);
        iArr[i2 + 3] = (int) j4;
        return (j4 >>> 32) == 0 ? 0 : Nat.incAt(6, iArr, i2, 4);
    }

    public static int mul33WordAdd(int i, int i2, int[] iArr, int i3) {
        long j = ((long) i2) & 4294967295L;
        long j2 = (((((long) i) & 4294967295L) * j) + (((long) iArr[i3 + 0]) & 4294967295L)) + 0;
        iArr[i3 + 0] = (int) j2;
        j2 = (j2 >>> 32) + (j + (((long) iArr[i3 + 1]) & 4294967295L));
        iArr[i3 + 1] = (int) j2;
        j2 = (j2 >>> 32) + (((long) iArr[i3 + 2]) & 4294967295L);
        iArr[i3 + 2] = (int) j2;
        return (j2 >>> 32) == 0 ? 0 : Nat.incAt(6, iArr, i3, 3);
    }

    public static void square(int[] iArr, int[] iArr2) {
        long j = ((long) iArr[0]) & 4294967295L;
        int i = 5;
        int i2 = 12;
        int i3 = 0;
        while (true) {
            int i4 = i - 1;
            long j2 = ((long) iArr[i]) & 4294967295L;
            j2 *= j2;
            i2--;
            iArr2[i2] = (i3 << 31) | ((int) (j2 >>> 33));
            i2--;
            iArr2[i2] = (int) (j2 >>> 1);
            i = (int) j2;
            if (i4 <= 0) {
                long j3 = j * j;
                long j4 = (((long) (i << 31)) & 4294967295L) | (j3 >>> 33);
                iArr2[0] = (int) j3;
                j2 = ((long) iArr[1]) & 4294967295L;
                long j5 = ((long) iArr2[2]) & 4294967295L;
                j4 += j2 * j;
                i3 = (int) j4;
                iArr2[1] = (((int) (j3 >>> 32)) & 1) | (i3 << 1);
                i4 = i3 >>> 31;
                j4 = (j4 >>> 32) + j5;
                j5 = ((long) iArr[2]) & 4294967295L;
                long j6 = ((long) iArr2[3]) & 4294967295L;
                long j7 = ((long) iArr2[4]) & 4294967295L;
                j4 += j5 * j;
                i3 = (int) j4;
                iArr2[2] = i4 | (i3 << 1);
                i4 = i3 >>> 31;
                j4 = ((j4 >>> 32) + (j5 * j2)) + j6;
                j6 = (j4 >>> 32) + j7;
                j7 = ((long) iArr[3]) & 4294967295L;
                long j8 = ((long) iArr2[5]) & 4294967295L;
                long j9 = ((long) iArr2[6]) & 4294967295L;
                j4 = (j4 & 4294967295L) + (j7 * j);
                i3 = (int) j4;
                iArr2[3] = i4 | (i3 << 1);
                i4 = i3 >>> 31;
                j4 = ((j4 >>> 32) + (j7 * j2)) + j6;
                j6 = ((j4 >>> 32) + (j7 * j5)) + j8;
                j8 = (j6 >>> 32) + j9;
                j6 &= 4294967295L;
                j9 = ((long) iArr[4]) & 4294967295L;
                long j10 = ((long) iArr2[7]) & 4294967295L;
                long j11 = ((long) iArr2[8]) & 4294967295L;
                j4 = (j4 & 4294967295L) + (j9 * j);
                i3 = (int) j4;
                iArr2[4] = i4 | (i3 << 1);
                i4 = i3 >>> 31;
                j4 = ((j4 >>> 32) + (j9 * j2)) + j6;
                j6 = ((j4 >>> 32) + (j9 * j5)) + j8;
                j8 = ((j6 >>> 32) + (j9 * j7)) + j10;
                j6 &= 4294967295L;
                j10 = (j8 >>> 32) + j11;
                j8 &= 4294967295L;
                j11 = ((long) iArr[5]) & 4294967295L;
                long j12 = ((long) iArr2[9]) & 4294967295L;
                long j13 = ((long) iArr2[10]) & 4294967295L;
                j4 = (j4 & 4294967295L) + (j * j11);
                i3 = (int) j4;
                iArr2[5] = i4 | (i3 << 1);
                j4 = ((j4 >>> 32) + (j11 * j2)) + j6;
                j = ((j4 >>> 32) + (j11 * j5)) + j8;
                j2 = ((j >>> 32) + (j11 * j7)) + j10;
                j5 = ((j2 >>> 32) + (j11 * j9)) + j12;
                j6 = (j5 >>> 32) + j13;
                i2 = (int) j4;
                iArr2[6] = (i3 >>> 31) | (i2 << 1);
                i = (int) j;
                iArr2[7] = (i2 >>> 31) | (i << 1);
                i2 = i >>> 31;
                i = (int) j2;
                iArr2[8] = i2 | (i << 1);
                i2 = i >>> 31;
                i = (int) j5;
                iArr2[9] = i2 | (i << 1);
                i2 = i >>> 31;
                i = (int) j6;
                iArr2[10] = i2 | (i << 1);
                iArr2[11] = (i >>> 31) | ((iArr2[11] + ((int) (j6 >> 32))) << 1);
                return;
            }
            i3 = i;
            i = i4;
        }
    }

    public static void square(int[] iArr, int i, int[] iArr2, int i2) {
        long j = ((long) iArr[i + 0]) & 4294967295L;
        int i3 = 5;
        int i4 = 12;
        int i5 = 0;
        while (true) {
            int i6 = i3 - 1;
            long j2 = ((long) iArr[i3 + i]) & 4294967295L;
            j2 *= j2;
            i4--;
            iArr2[i2 + i4] = (i5 << 31) | ((int) (j2 >>> 33));
            i4--;
            iArr2[i2 + i4] = (int) (j2 >>> 1);
            i3 = (int) j2;
            if (i6 <= 0) {
                long j3 = j * j;
                long j4 = (((long) (i3 << 31)) & 4294967295L) | (j3 >>> 33);
                iArr2[i2 + 0] = (int) j3;
                j2 = ((long) iArr[i + 1]) & 4294967295L;
                long j5 = ((long) iArr2[i2 + 2]) & 4294967295L;
                j4 += j2 * j;
                i5 = (int) j4;
                iArr2[i2 + 1] = (((int) (j3 >>> 32)) & 1) | (i5 << 1);
                i6 = i5 >>> 31;
                j4 = (j4 >>> 32) + j5;
                j5 = ((long) iArr[i + 2]) & 4294967295L;
                long j6 = ((long) iArr2[i2 + 3]) & 4294967295L;
                long j7 = ((long) iArr2[i2 + 4]) & 4294967295L;
                j4 += j5 * j;
                i5 = (int) j4;
                iArr2[i2 + 2] = i6 | (i5 << 1);
                i6 = i5 >>> 31;
                j4 = ((j4 >>> 32) + (j5 * j2)) + j6;
                j6 = (j4 >>> 32) + j7;
                j7 = ((long) iArr[i + 3]) & 4294967295L;
                long j8 = ((long) iArr2[i2 + 5]) & 4294967295L;
                long j9 = ((long) iArr2[i2 + 6]) & 4294967295L;
                j4 = (j4 & 4294967295L) + (j7 * j);
                i5 = (int) j4;
                iArr2[i2 + 3] = i6 | (i5 << 1);
                i6 = i5 >>> 31;
                j4 = ((j4 >>> 32) + (j7 * j2)) + j6;
                j6 = ((j4 >>> 32) + (j7 * j5)) + j8;
                j8 = (j6 >>> 32) + j9;
                j6 &= 4294967295L;
                j9 = ((long) iArr[i + 4]) & 4294967295L;
                long j10 = ((long) iArr2[i2 + 7]) & 4294967295L;
                long j11 = ((long) iArr2[i2 + 8]) & 4294967295L;
                j4 = (j4 & 4294967295L) + (j9 * j);
                i5 = (int) j4;
                iArr2[i2 + 4] = i6 | (i5 << 1);
                i6 = i5 >>> 31;
                j4 = ((j4 >>> 32) + (j9 * j2)) + j6;
                j6 = ((j4 >>> 32) + (j9 * j5)) + j8;
                j8 = ((j6 >>> 32) + (j9 * j7)) + j10;
                j6 &= 4294967295L;
                j10 = (j8 >>> 32) + j11;
                j8 &= 4294967295L;
                j11 = ((long) iArr[i + 5]) & 4294967295L;
                long j12 = ((long) iArr2[i2 + 9]) & 4294967295L;
                long j13 = ((long) iArr2[i2 + 10]) & 4294967295L;
                j4 = (j4 & 4294967295L) + (j * j11);
                i5 = (int) j4;
                iArr2[i2 + 5] = i6 | (i5 << 1);
                j4 = ((j4 >>> 32) + (j11 * j2)) + j6;
                j = ((j4 >>> 32) + (j11 * j5)) + j8;
                j2 = ((j >>> 32) + (j11 * j7)) + j10;
                j5 = ((j2 >>> 32) + (j11 * j9)) + j12;
                j6 = (j5 >>> 32) + j13;
                i4 = (int) j4;
                iArr2[i2 + 6] = (i5 >>> 31) | (i4 << 1);
                i3 = (int) j;
                iArr2[i2 + 7] = (i4 >>> 31) | (i3 << 1);
                i4 = i3 >>> 31;
                i3 = (int) j2;
                iArr2[i2 + 8] = i4 | (i3 << 1);
                i4 = i3 >>> 31;
                i3 = (int) j5;
                iArr2[i2 + 9] = i4 | (i3 << 1);
                i4 = i3 >>> 31;
                i3 = (int) j6;
                iArr2[i2 + 10] = i4 | (i3 << 1);
                iArr2[i2 + 11] = (i3 >>> 31) | ((iArr2[i2 + 11] + ((int) (j6 >> 32))) << 1);
                return;
            }
            i5 = i3;
            i3 = i6;
        }
    }

    public static int sub(int[] iArr, int[] iArr2, int[] iArr3) {
        long j = 0 + ((((long) iArr[0]) & 4294967295L) - (((long) iArr2[0]) & 4294967295L));
        iArr3[0] = (int) j;
        j = (j >> 32) + ((((long) iArr[1]) & 4294967295L) - (((long) iArr2[1]) & 4294967295L));
        iArr3[1] = (int) j;
        j = (j >> 32) + ((((long) iArr[2]) & 4294967295L) - (((long) iArr2[2]) & 4294967295L));
        iArr3[2] = (int) j;
        j = (j >> 32) + ((((long) iArr[3]) & 4294967295L) - (((long) iArr2[3]) & 4294967295L));
        iArr3[3] = (int) j;
        j = (j >> 32) + ((((long) iArr[4]) & 4294967295L) - (((long) iArr2[4]) & 4294967295L));
        iArr3[4] = (int) j;
        j = (j >> 32) + ((((long) iArr[5]) & 4294967295L) - (((long) iArr2[5]) & 4294967295L));
        iArr3[5] = (int) j;
        return (int) (j >> 32);
    }

    public static int sub(int[] iArr, int i, int[] iArr2, int i2, int[] iArr3, int i3) {
        long j = 0 + ((((long) iArr[i + 0]) & 4294967295L) - (((long) iArr2[i2 + 0]) & 4294967295L));
        iArr3[i3 + 0] = (int) j;
        j = (j >> 32) + ((((long) iArr[i + 1]) & 4294967295L) - (((long) iArr2[i2 + 1]) & 4294967295L));
        iArr3[i3 + 1] = (int) j;
        j = (j >> 32) + ((((long) iArr[i + 2]) & 4294967295L) - (((long) iArr2[i2 + 2]) & 4294967295L));
        iArr3[i3 + 2] = (int) j;
        j = (j >> 32) + ((((long) iArr[i + 3]) & 4294967295L) - (((long) iArr2[i2 + 3]) & 4294967295L));
        iArr3[i3 + 3] = (int) j;
        j = (j >> 32) + ((((long) iArr[i + 4]) & 4294967295L) - (((long) iArr2[i2 + 4]) & 4294967295L));
        iArr3[i3 + 4] = (int) j;
        j = (j >> 32) + ((((long) iArr[i + 5]) & 4294967295L) - (((long) iArr2[i2 + 5]) & 4294967295L));
        iArr3[i3 + 5] = (int) j;
        return (int) (j >> 32);
    }

    public static int subFrom(int[] iArr, int[] iArr2) {
        long j = 0 + ((((long) iArr2[0]) & 4294967295L) - (((long) iArr[0]) & 4294967295L));
        iArr2[0] = (int) j;
        j = (j >> 32) + ((((long) iArr2[1]) & 4294967295L) - (((long) iArr[1]) & 4294967295L));
        iArr2[1] = (int) j;
        j = (j >> 32) + ((((long) iArr2[2]) & 4294967295L) - (((long) iArr[2]) & 4294967295L));
        iArr2[2] = (int) j;
        j = (j >> 32) + ((((long) iArr2[3]) & 4294967295L) - (((long) iArr[3]) & 4294967295L));
        iArr2[3] = (int) j;
        j = (j >> 32) + ((((long) iArr2[4]) & 4294967295L) - (((long) iArr[4]) & 4294967295L));
        iArr2[4] = (int) j;
        j = (j >> 32) + ((((long) iArr2[5]) & 4294967295L) - (((long) iArr[5]) & 4294967295L));
        iArr2[5] = (int) j;
        return (int) (j >> 32);
    }

    public static BigInteger toBigInteger(int[] iArr) {
        byte[] bArr = new byte[24];
        for (int i = 0; i < 6; i++) {
            int i2 = iArr[i];
            if (i2 != 0) {
                Pack.intToBigEndian(i2, bArr, (5 - i) << 2);
            }
        }
        return new BigInteger(1, bArr);
    }

    public static void zero(int[] iArr) {
        iArr[0] = 0;
        iArr[1] = 0;
        iArr[2] = 0;
        iArr[3] = 0;
        iArr[4] = 0;
        iArr[5] = 0;
    }
}
