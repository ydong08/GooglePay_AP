package org.bouncycastle.math.raw;

import java.math.BigInteger;
import org.bouncycastle.util.Pack;

public abstract class Nat {
    public static int add(int i, int[] iArr, int[] iArr2, int[] iArr3) {
        long j = 0;
        for (int i2 = 0; i2 < i; i2++) {
            j += (((long) iArr[i2]) & 4294967295L) + (((long) iArr2[i2]) & 4294967295L);
            iArr3[i2] = (int) j;
            j >>>= 32;
        }
        return (int) j;
    }

    public static int add33To(int i, int i2, int[] iArr) {
        long j = (((long) iArr[0]) & 4294967295L) + (((long) i2) & 4294967295L);
        iArr[0] = (int) j;
        j = (j >>> 32) + ((((long) iArr[1]) & 4294967295L) + 1);
        iArr[1] = (int) j;
        if ((j >>> 32) == 0) {
            return 0;
        }
        return incAt(i, iArr, 2);
    }

    public static int addBothTo(int i, int[] iArr, int[] iArr2, int[] iArr3) {
        long j = 0;
        for (int i2 = 0; i2 < i; i2++) {
            j += ((((long) iArr[i2]) & 4294967295L) + (((long) iArr2[i2]) & 4294967295L)) + (((long) iArr3[i2]) & 4294967295L);
            iArr3[i2] = (int) j;
            j >>>= 32;
        }
        return (int) j;
    }

    public static int addTo(int i, int[] iArr, int[] iArr2) {
        long j = 0;
        for (int i2 = 0; i2 < i; i2++) {
            j += (((long) iArr[i2]) & 4294967295L) + (((long) iArr2[i2]) & 4294967295L);
            iArr2[i2] = (int) j;
            j >>>= 32;
        }
        return (int) j;
    }

    public static int addTo(int i, int[] iArr, int i2, int[] iArr2, int i3) {
        long j = 0;
        for (int i4 = 0; i4 < i; i4++) {
            j += (((long) iArr[i2 + i4]) & 4294967295L) + (((long) iArr2[i3 + i4]) & 4294967295L);
            iArr2[i3 + i4] = (int) j;
            j >>>= 32;
        }
        return (int) j;
    }

    public static int addWordAt(int i, int i2, int[] iArr, int i3) {
        long j = (((long) i2) & 4294967295L) + (((long) iArr[i3]) & 4294967295L);
        iArr[i3] = (int) j;
        return (j >>> 32) == 0 ? 0 : incAt(i, iArr, i3 + 1);
    }

    public static int addWordTo(int i, int i2, int[] iArr) {
        long j = (((long) i2) & 4294967295L) + (((long) iArr[0]) & 4294967295L);
        iArr[0] = (int) j;
        if ((j >>> 32) == 0) {
            return 0;
        }
        return incAt(i, iArr, 1);
    }

    public static int[] copy(int i, int[] iArr) {
        Object obj = new int[i];
        System.arraycopy(iArr, 0, obj, 0, i);
        return obj;
    }

    public static int[] create(int i) {
        return new int[i];
    }

    public static int dec(int i, int[] iArr) {
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = iArr[i2] - 1;
            iArr[i2] = i3;
            if (i3 != -1) {
                return 0;
            }
        }
        return -1;
    }

    public static int decAt(int i, int[] iArr, int i2) {
        while (i2 < i) {
            int i3 = iArr[i2] - 1;
            iArr[i2] = i3;
            if (i3 != -1) {
                return 0;
            }
            i2++;
        }
        return -1;
    }

    public static boolean eq(int i, int[] iArr, int[] iArr2) {
        for (int i2 = i - 1; i2 >= 0; i2--) {
            if (iArr[i2] != iArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static int[] fromBigInteger(int i, BigInteger bigInteger) {
        if (bigInteger.signum() < 0 || bigInteger.bitLength() > i) {
            throw new IllegalArgumentException();
        }
        int[] create = create((i + 31) >> 5);
        int i2 = 0;
        while (bigInteger.signum() != 0) {
            int i3 = i2 + 1;
            create[i2] = bigInteger.intValue();
            bigInteger = bigInteger.shiftRight(32);
            i2 = i3;
        }
        return create;
    }

    public static int getBit(int[] iArr, int i) {
        if (i == 0) {
            return iArr[0] & 1;
        }
        int i2 = i >> 5;
        if (i2 < 0 || i2 >= iArr.length) {
            return 0;
        }
        return (iArr[i2] >>> (i & 31)) & 1;
    }

    public static boolean gte(int i, int[] iArr, int[] iArr2) {
        for (int i2 = i - 1; i2 >= 0; i2--) {
            int i3 = iArr[i2] ^ Integer.MIN_VALUE;
            int i4 = iArr2[i2] ^ Integer.MIN_VALUE;
            if (i3 < i4) {
                return false;
            }
            if (i3 > i4) {
                return true;
            }
        }
        return true;
    }

    public static int inc(int i, int[] iArr) {
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = iArr[i2] + 1;
            iArr[i2] = i3;
            if (i3 != 0) {
                return 0;
            }
        }
        return 1;
    }

    public static int inc(int i, int[] iArr, int[] iArr2) {
        int i2 = 0;
        while (i2 < i) {
            int i3 = iArr[i2] + 1;
            iArr2[i2] = i3;
            i2++;
            if (i3 != 0) {
                while (i2 < i) {
                    iArr2[i2] = iArr[i2];
                    i2++;
                }
                return 0;
            }
        }
        return 1;
    }

    public static int incAt(int i, int[] iArr, int i2) {
        while (i2 < i) {
            int i3 = iArr[i2] + 1;
            iArr[i2] = i3;
            if (i3 != 0) {
                return 0;
            }
            i2++;
        }
        return 1;
    }

    public static int incAt(int i, int[] iArr, int i2, int i3) {
        while (i3 < i) {
            int i4 = i2 + i3;
            int i5 = iArr[i4] + 1;
            iArr[i4] = i5;
            if (i5 != 0) {
                return 0;
            }
            i3++;
        }
        return 1;
    }

    public static boolean isOne(int i, int[] iArr) {
        if (iArr[0] != 1) {
            return false;
        }
        for (int i2 = 1; i2 < i; i2++) {
            if (iArr[i2] != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isZero(int i, int[] iArr) {
        for (int i2 = 0; i2 < i; i2++) {
            if (iArr[i2] != 0) {
                return false;
            }
        }
        return true;
    }

    public static int mul31BothAdd(int i, int i2, int[] iArr, int i3, int[] iArr2, int[] iArr3, int i4) {
        long j = 0;
        long j2 = 4294967295L & ((long) i2);
        long j3 = 4294967295L & ((long) i3);
        int i5 = 0;
        do {
            j += (((((long) iArr[i5]) & 4294967295L) * j2) + ((((long) iArr2[i5]) & 4294967295L) * j3)) + (((long) iArr3[i4 + i5]) & 4294967295L);
            iArr3[i4 + i5] = (int) j;
            j >>>= 32;
            i5++;
        } while (i5 < i);
        return (int) j;
    }

    public static int mulWordAddTo(int i, int i2, int[] iArr, int i3, int[] iArr2, int i4) {
        long j = 0;
        long j2 = 4294967295L & ((long) i2);
        int i5 = 0;
        do {
            j += ((((long) iArr[i3 + i5]) & 4294967295L) * j2) + (((long) iArr2[i4 + i5]) & 4294967295L);
            iArr2[i4 + i5] = (int) j;
            j >>>= 32;
            i5++;
        } while (i5 < i);
        return (int) j;
    }

    public static int shiftDownBit(int i, int[] iArr, int i2) {
        while (true) {
            i--;
            if (i < 0) {
                return i2 << 31;
            }
            int i3 = iArr[i];
            iArr[i] = (i3 >>> 1) | (i2 << 31);
            i2 = i3;
        }
    }

    public static int shiftDownBits(int i, int[] iArr, int i2, int i3) {
        while (true) {
            i--;
            if (i < 0) {
                return i3 << (-i2);
            }
            int i4 = iArr[i];
            iArr[i] = (i4 >>> i2) | (i3 << (-i2));
            i3 = i4;
        }
    }

    public static int shiftDownBits(int i, int[] iArr, int i2, int i3, int i4, int[] iArr2, int i5) {
        while (true) {
            i--;
            if (i < 0) {
                return i4 << (-i3);
            }
            int i6 = iArr[i2 + i];
            iArr2[i5 + i] = (i6 >>> i3) | (i4 << (-i3));
            i4 = i6;
        }
    }

    public static int shiftDownWord(int i, int[] iArr, int i2) {
        while (true) {
            i--;
            if (i < 0) {
                return i2;
            }
            int i3 = iArr[i];
            iArr[i] = i2;
            i2 = i3;
        }
    }

    public static int shiftUpBit(int i, int[] iArr, int i2, int[] iArr2) {
        int i3 = 0;
        while (i3 < i) {
            int i4 = iArr[i3];
            iArr2[i3] = (i4 << 1) | (i2 >>> 31);
            i3++;
            i2 = i4;
        }
        return i2 >>> 31;
    }

    public static int shiftUpBit(int i, int[] iArr, int i2, int i3, int[] iArr2, int i4) {
        int i5 = 0;
        while (i5 < i) {
            int i6 = iArr[i2 + i5];
            iArr2[i4 + i5] = (i6 << 1) | (i3 >>> 31);
            i5++;
            i3 = i6;
        }
        return i3 >>> 31;
    }

    public static int shiftUpBits(int i, int[] iArr, int i2, int i3) {
        int i4 = 0;
        while (i4 < i) {
            int i5 = iArr[i4];
            iArr[i4] = (i5 << i2) | (i3 >>> (-i2));
            i4++;
            i3 = i5;
        }
        return i3 >>> (-i2);
    }

    public static int shiftUpBits(int i, int[] iArr, int i2, int i3, int[] iArr2) {
        int i4 = 0;
        while (i4 < i) {
            int i5 = iArr[i4];
            iArr2[i4] = (i5 << i2) | (i3 >>> (-i2));
            i4++;
            i3 = i5;
        }
        return i3 >>> (-i2);
    }

    public static int sub(int i, int[] iArr, int[] iArr2, int[] iArr3) {
        long j = 0;
        for (int i2 = 0; i2 < i; i2++) {
            j += (((long) iArr[i2]) & 4294967295L) - (((long) iArr2[i2]) & 4294967295L);
            iArr3[i2] = (int) j;
            j >>= 32;
        }
        return (int) j;
    }

    public static int sub33From(int i, int i2, int[] iArr) {
        long j = (((long) iArr[0]) & 4294967295L) - (((long) i2) & 4294967295L);
        iArr[0] = (int) j;
        j = (j >> 32) + ((((long) iArr[1]) & 4294967295L) - 1);
        iArr[1] = (int) j;
        if ((j >> 32) == 0) {
            return 0;
        }
        return decAt(i, iArr, 2);
    }

    public static int subFrom(int i, int[] iArr, int[] iArr2) {
        long j = 0;
        for (int i2 = 0; i2 < i; i2++) {
            j += (((long) iArr2[i2]) & 4294967295L) - (((long) iArr[i2]) & 4294967295L);
            iArr2[i2] = (int) j;
            j >>= 32;
        }
        return (int) j;
    }

    public static int subFrom(int i, int[] iArr, int i2, int[] iArr2, int i3) {
        long j = 0;
        for (int i4 = 0; i4 < i; i4++) {
            j += (((long) iArr2[i3 + i4]) & 4294967295L) - (((long) iArr[i2 + i4]) & 4294967295L);
            iArr2[i3 + i4] = (int) j;
            j >>= 32;
        }
        return (int) j;
    }

    public static BigInteger toBigInteger(int i, int[] iArr) {
        byte[] bArr = new byte[(i << 2)];
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = iArr[i2];
            if (i3 != 0) {
                Pack.intToBigEndian(i3, bArr, ((i - 1) - i2) << 2);
            }
        }
        return new BigInteger(1, bArr);
    }

    public static void zero(int i, int[] iArr) {
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = 0;
        }
    }
}
