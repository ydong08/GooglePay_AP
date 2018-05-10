package com.google.android.libraries.commerce.hce.util;

import com.google.common.base.Preconditions;

public class MoreArrays {
    private MoreArrays() {
    }

    public static boolean equals(byte[] bArr, int i, byte[] bArr2, int i2, int i3) throws IndexOutOfBoundsException, IllegalArgumentException {
        int i4 = i + i3;
        int i5 = i2 + i3;
        if (i3 > 0) {
            Preconditions.checkPositionIndexes(i, i4 - 1, bArr.length);
            Preconditions.checkPositionIndexes(i2, i5 - 1, bArr2.length);
        } else {
            Preconditions.checkArgument(i3 == 0, "len may not be negative: %s", i3);
            if (bArr.length > 0) {
                Preconditions.checkElementIndex(i, bArr.length, String.format("base1 is out of bounds: base1=%s, a1.length=%s", new Object[]{Integer.valueOf(i), Integer.valueOf(bArr.length)}));
            } else {
                Preconditions.checkArgument(i == 0, "base1 must be zero for zero-length array: %s", i);
            }
            if (bArr2.length > 0) {
                Preconditions.checkElementIndex(i2, bArr2.length, String.format("base2 is out of bounds: base2=%s, a2.length=%s", new Object[]{Integer.valueOf(i2), Integer.valueOf(bArr2.length)}));
            } else {
                Preconditions.checkArgument(i2 == 0, "base2 must be zero for zero-length array: %s", i2);
            }
        }
        while (i < i4) {
            if (bArr[i] != bArr2[i2]) {
                return false;
            }
            i++;
            i2++;
        }
        return true;
    }
}
