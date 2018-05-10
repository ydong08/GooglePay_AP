package android.support.v7.content.res;

import java.lang.reflect.Array;

final class GrowingArrayUtils {
    public static <T> T[] append(T[] tArr, int i, T t) {
        T[] tArr2;
        if (i + 1 > tArr.length) {
            tArr2 = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), growSize(i));
            System.arraycopy(tArr, 0, tArr2, 0, i);
        } else {
            tArr2 = tArr;
        }
        tArr2[i] = t;
        return tArr2;
    }

    public static int[] append(int[] iArr, int i, int i2) {
        if (i + 1 > iArr.length) {
            Object obj = new int[growSize(i)];
            System.arraycopy(iArr, 0, obj, 0, i);
            iArr = obj;
        }
        iArr[i] = i2;
        return iArr;
    }

    public static int growSize(int i) {
        return i <= 4 ? 8 : i * 2;
    }

    private GrowingArrayUtils() {
    }
}
