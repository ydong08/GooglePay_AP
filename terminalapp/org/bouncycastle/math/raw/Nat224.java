package org.bouncycastle.math.raw;

import java.math.BigInteger;
import org.bouncycastle.util.Pack;

public abstract class Nat224 {
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
        j = (j >>> 32) + ((((long) iArr[6]) & 4294967295L) + (((long) iArr2[6]) & 4294967295L));
        iArr3[6] = (int) j;
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
        j = (j >>> 32) + (((((long) iArr[6]) & 4294967295L) + (((long) iArr2[6]) & 4294967295L)) + (((long) iArr3[6]) & 4294967295L));
        iArr3[6] = (int) j;
        return (int) (j >>> 32);
    }

    public static void copy(int[] iArr, int[] iArr2) {
        iArr2[0] = iArr[0];
        iArr2[1] = iArr[1];
        iArr2[2] = iArr[2];
        iArr2[3] = iArr[3];
        iArr2[4] = iArr[4];
        iArr2[5] = iArr[5];
        iArr2[6] = iArr[6];
    }

    public static int[] create() {
        return new int[7];
    }

    public static int[] createExt() {
        return new int[14];
    }

    public static boolean eq(int[] iArr, int[] iArr2) {
        for (int i = 6; i >= 0; i--) {
            if (iArr[i] != iArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        if (bigInteger.signum() < 0 || bigInteger.bitLength() > 224) {
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
        if (i2 < 0 || i2 >= 7) {
            return 0;
        }
        return (iArr[i2] >>> (i & 31)) & 1;
    }

    public static boolean gte(int[] iArr, int[] iArr2) {
        for (int i = 6; i >= 0; i--) {
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

    public static boolean isOne(int[] iArr) {
        if (iArr[0] != 1) {
            return false;
        }
        for (int i = 1; i < 7; i++) {
            if (iArr[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isZero(int[] iArr) {
        for (int i = 0; i < 7; i++) {
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
        long j7 = 4294967295L & ((long) iArr2[6]);
        long j8 = ((long) iArr[0]) & 4294967295L;
        long j9 = 0 + (j8 * j);
        iArr3[0] = (int) j9;
        j9 = (j9 >>> 32) + (j8 * j2);
        iArr3[1] = (int) j9;
        j9 = (j9 >>> 32) + (j8 * j3);
        iArr3[2] = (int) j9;
        j9 = (j9 >>> 32) + (j8 * j4);
        iArr3[3] = (int) j9;
        j9 = (j9 >>> 32) + (j8 * j5);
        iArr3[4] = (int) j9;
        j9 = (j9 >>> 32) + (j8 * j6);
        iArr3[5] = (int) j9;
        j9 = (j9 >>> 32) + (j8 * j7);
        iArr3[6] = (int) j9;
        iArr3[7] = (int) (j9 >>> 32);
        for (int i = 1; i < 7; i++) {
            long j10 = ((long) iArr[i]) & 4294967295L;
            j8 = 0 + ((j10 * j) + (((long) iArr3[i + 0]) & 4294967295L));
            iArr3[i + 0] = (int) j8;
            j8 = (j8 >>> 32) + ((j10 * j2) + (((long) iArr3[i + 1]) & 4294967295L));
            iArr3[i + 1] = (int) j8;
            j8 = (j8 >>> 32) + ((j10 * j3) + (((long) iArr3[i + 2]) & 4294967295L));
            iArr3[i + 2] = (int) j8;
            j8 = (j8 >>> 32) + ((j10 * j4) + (((long) iArr3[i + 3]) & 4294967295L));
            iArr3[i + 3] = (int) j8;
            j8 = (j8 >>> 32) + ((j10 * j5) + (((long) iArr3[i + 4]) & 4294967295L));
            iArr3[i + 4] = (int) j8;
            j8 = (j8 >>> 32) + ((j10 * j6) + (((long) iArr3[i + 5]) & 4294967295L));
            iArr3[i + 5] = (int) j8;
            j8 = (j8 >>> 32) + ((j10 * j7) + (((long) iArr3[i + 6]) & 4294967295L));
            iArr3[i + 6] = (int) j8;
            iArr3[i + 7] = (int) (j8 >>> 32);
        }
    }

    public static int mulAddTo(int[] iArr, int[] iArr2, int[] iArr3) {
        long j = ((long) iArr2[0]) & 4294967295L;
        long j2 = ((long) iArr2[1]) & 4294967295L;
        long j3 = ((long) iArr2[2]) & 4294967295L;
        long j4 = ((long) iArr2[3]) & 4294967295L;
        long j5 = ((long) iArr2[4]) & 4294967295L;
        long j6 = ((long) iArr2[5]) & 4294967295L;
        long j7 = ((long) iArr2[6]) & 4294967295L;
        long j8 = 0;
        for (int i = 0; i < 7; i++) {
            long j9 = ((long) iArr[i]) & 4294967295L;
            long j10 = 0 + ((j9 * j) + (((long) iArr3[i + 0]) & 4294967295L));
            iArr3[i + 0] = (int) j10;
            j10 = (j10 >>> 32) + ((j9 * j2) + (((long) iArr3[i + 1]) & 4294967295L));
            iArr3[i + 1] = (int) j10;
            j10 = (j10 >>> 32) + ((j9 * j3) + (((long) iArr3[i + 2]) & 4294967295L));
            iArr3[i + 2] = (int) j10;
            j10 = (j10 >>> 32) + ((j9 * j4) + (((long) iArr3[i + 3]) & 4294967295L));
            iArr3[i + 3] = (int) j10;
            j10 = (j10 >>> 32) + ((j9 * j5) + (((long) iArr3[i + 4]) & 4294967295L));
            iArr3[i + 4] = (int) j10;
            j10 = (j10 >>> 32) + ((j9 * j6) + (((long) iArr3[i + 5]) & 4294967295L));
            iArr3[i + 5] = (int) j10;
            j10 = (j10 >>> 32) + ((j9 * j7) + (((long) iArr3[i + 6]) & 4294967295L));
            iArr3[i + 6] = (int) j10;
            j8 = (j8 + (((long) iArr3[i + 7]) & 4294967295L)) + (j10 >>> 32);
            iArr3[i + 7] = (int) j8;
            j8 >>>= 32;
        }
        return (int) j8;
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
        j3 = (j3 >>> 32) + ((j2 + (j * j4)) + (((long) iArr2[i3 + 5]) & 4294967295L));
        iArr3[i4 + 5] = (int) j3;
        j2 = ((long) iArr[i2 + 6]) & 4294967295L;
        j3 = (j3 >>> 32) + (((j * j2) + j4) + (((long) iArr2[i3 + 6]) & 4294967295L));
        iArr3[i4 + 6] = (int) j3;
        return (j3 >>> 32) + j2;
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
        return (j4 >>> 32) == 0 ? 0 : Nat.incAt(7, iArr, i2, 4);
    }

    public static int mul33WordAdd(int i, int i2, int[] iArr, int i3) {
        long j = ((long) i2) & 4294967295L;
        long j2 = (((((long) i) & 4294967295L) * j) + (((long) iArr[i3 + 0]) & 4294967295L)) + 0;
        iArr[i3 + 0] = (int) j2;
        j2 = (j2 >>> 32) + (j + (((long) iArr[i3 + 1]) & 4294967295L));
        iArr[i3 + 1] = (int) j2;
        j2 = (j2 >>> 32) + (((long) iArr[i3 + 2]) & 4294967295L);
        iArr[i3 + 2] = (int) j2;
        return (j2 >>> 32) == 0 ? 0 : Nat.incAt(7, iArr, i3, 3);
    }

    public static void square(int[] iArr, int[] iArr2) {
        long j = ((long) iArr[0]) & 4294967295L;
        int i = 6;
        int i2 = 14;
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
                j4 = (j4 & 4294967295L) + (j11 * j);
                i3 = (int) j4;
                iArr2[5] = i4 | (i3 << 1);
                i4 = i3 >>> 31;
                j4 = ((j4 >>> 32) + (j11 * j2)) + j6;
                j6 = ((j4 >>> 32) + (j11 * j5)) + j8;
                j8 = ((j6 >>> 32) + (j11 * j7)) + j10;
                j6 &= 4294967295L;
                j10 = ((j8 >>> 32) + (j11 * j9)) + j12;
                j8 &= 4294967295L;
                j12 = (j10 >>> 32) + j13;
                j10 &= 4294967295L;
                j13 = ((long) iArr[6]) & 4294967295L;
                long j14 = ((long) iArr2[11]) & 4294967295L;
                long j15 = ((long) iArr2[12]) & 4294967295L;
                j4 = (j4 & 4294967295L) + (j * j13);
                i3 = (int) j4;
                iArr2[6] = i4 | (i3 << 1);
                j4 = ((j4 >>> 32) + (j13 * j2)) + j6;
                j = ((j4 >>> 32) + (j13 * j5)) + j8;
                j2 = ((j >>> 32) + (j13 * j7)) + j10;
                j5 = ((j2 >>> 32) + (j13 * j9)) + j12;
                j6 = ((j5 >>> 32) + (j13 * j11)) + j14;
                j7 = (j6 >>> 32) + j15;
                i2 = (int) j4;
                iArr2[7] = (i3 >>> 31) | (i2 << 1);
                i = (int) j;
                iArr2[8] = (i2 >>> 31) | (i << 1);
                i2 = i >>> 31;
                i = (int) j2;
                iArr2[9] = i2 | (i << 1);
                i2 = i >>> 31;
                i = (int) j5;
                iArr2[10] = i2 | (i << 1);
                i2 = i >>> 31;
                i = (int) j6;
                iArr2[11] = i2 | (i << 1);
                i2 = i >>> 31;
                i = (int) j7;
                iArr2[12] = i2 | (i << 1);
                iArr2[13] = (i >>> 31) | ((iArr2[13] + ((int) (j7 >> 32))) << 1);
                return;
            }
            i3 = i;
            i = i4;
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
        j = (j >> 32) + ((((long) iArr[6]) & 4294967295L) - (((long) iArr2[6]) & 4294967295L));
        iArr3[6] = (int) j;
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
        j = (j >> 32) + ((((long) iArr2[6]) & 4294967295L) - (((long) iArr[6]) & 4294967295L));
        iArr2[6] = (int) j;
        return (int) (j >> 32);
    }

    public static BigInteger toBigInteger(int[] iArr) {
        byte[] bArr = new byte[28];
        for (int i = 0; i < 7; i++) {
            int i2 = iArr[i];
            if (i2 != 0) {
                Pack.intToBigEndian(i2, bArr, (6 - i) << 2);
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
        iArr[6] = 0;
    }
}
